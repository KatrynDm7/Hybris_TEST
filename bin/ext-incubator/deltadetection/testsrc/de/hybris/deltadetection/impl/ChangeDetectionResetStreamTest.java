/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package de.hybris.deltadetection.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class ChangeDetectionResetStreamTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ChangeDetectionService changeDetectionService;
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private static final String TEST_STREAM = "testStream";

	private ComposedTypeModel titleComposedType;

	private TitleModel fooTitle;
	private TitleModel barTitle;
	private TitleModel bazTitle;
	private StreamConfigurationContainerModel streamContainer;
	private StreamConfigurationModel streamConfig;

	private static enum VersionMarkerTS
	{
		PRESENT, PAST
	}

	private ItemVersionMarkerModel createVersionMarker(final TitleModel title, final VersionMarkerTS currentTime,
			final ItemVersionMarkerStatus status)
	{
		final ItemVersionMarkerModel marker = modelService.create(ItemVersionMarkerModel.class);
		marker.setItemPK(title.getPk().getLong());

		if (currentTime.equals(VersionMarkerTS.PRESENT))
		{
			marker.setVersionTS(title.getModifiedtime());
		}
		else
		{
			marker.setVersionTS(Date.from(title.getModifiedtime().toInstant().minus(10, ChronoUnit.MINUTES)));
		}
		marker.setItemComposedType(titleComposedType);
		marker.setStreamId(TEST_STREAM);
		marker.setStatus(status);
		modelService.save(marker);

		return marker;
	}

	@Before
	public void prepare()
	{
		fooTitle = modelService.create(TitleModel.class);
		fooTitle.setCode("foo");
		barTitle = modelService.create(TitleModel.class);
		barTitle.setCode("bar");
		bazTitle = modelService.create(TitleModel.class);
		bazTitle.setCode("baz");

		titleComposedType = typeService.getComposedTypeForClass(TitleModel.class);

		final StreamConfigurationContainerModel streamContainer = modelService.create(StreamConfigurationContainerModel.class);
		streamContainer.setId("test-container");
		modelService.save(streamContainer);

		streamConfig = modelService.create(StreamConfigurationModel.class);
		streamConfig.setItemTypeForStream(titleComposedType);
		streamConfig.setStreamId(TEST_STREAM);
		streamConfig.setWhereClause("1=1");
		streamConfig.setContainer(streamContainer);

		modelService.save(fooTitle);
		modelService.save(streamConfig);
	}



	@Test
	public void deletedItemChangeConsumptionShouldChangeStatusToDeleted()
	{
		// given
		final ItemVersionMarkerModel activeIvm = createVersionMarker(fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
		final PK versionMarkerPK = activeIvm.getPk();
		modelService.remove(fooTitle);

		// when
		final InMemoryChangesCollector collector = new InMemoryChangesCollector();
		changeDetectionService.collectChangesForType(titleComposedType, TEST_STREAM, collector);
		changeDetectionService.consumeChanges(collector.getChanges());

		// then
		final ItemVersionMarkerModel deletedIvm = modelService.<ItemVersionMarkerModel> get(versionMarkerPK);
		assertThat(deletedIvm.getStatus()).isEqualTo(ItemVersionMarkerStatus.DELETED);
	}

	@Test
	public void ivmWithDeletedStatusShouldBeRestoredInCaseOfSameNewItem()
	{
		// given
		createVersionMarker(fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.DELETED);

		// when
		final InMemoryChangesCollector collector = new InMemoryChangesCollector();
		changeDetectionService.collectChangesForType(titleComposedType, TEST_STREAM, collector);
		changeDetectionService.consumeChanges(collector.getChanges());

		// then
		assertThat(countVersionMarkerByPK(TEST_STREAM, fooTitle.getPk())).isEqualTo(1);
	}

	@Test
	public void resetStreamShouldChangeIvmTimestamp()
	{
		// given
		final ItemVersionMarkerModel ivm = createVersionMarker(fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.ACTIVE);
		assertChangesNumberForStream(TEST_STREAM, 0);

		// when
		changeDetectionService.resetStream(TEST_STREAM);

		// then
		assertChangesNumberForStream(TEST_STREAM, 1);
		modelService.refresh(ivm);
		final Date date = Date.from(LocalDate.parse("1971-01-01").atStartOfDay().toInstant(ZoneOffset.MIN));
		assertThat(ivm.getVersionTS().before(date)).isTrue();
	}

	@Test
	public void shouldRevertVersionToNull()
	{
		// given
		final ItemVersionMarkerModel ivm = createVersionMarker(fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
		ivm.setVersionValue("foo");
		modelService.save(ivm);

		// when
		changeDetectionService.resetStream(TEST_STREAM);

		// then
		modelService.refresh(ivm);
		assertThat(ivm.getVersionValue()).isNull();
	}

	@Test
	public void shouldRevertVersionToPreviousValue()
	{
		// given
		final ItemVersionMarkerModel ivm = createVersionMarker(fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
		ivm.setVersionValue("foo");
		ivm.setLastVersionValue("bar");
		modelService.save(ivm);

		// when
		changeDetectionService.resetStream(TEST_STREAM);

		// then
		modelService.refresh(ivm);
		assertThat(ivm.getVersionValue()).isEqualTo("bar");
	}

	@Test
	public void shouldNotRevertValueInCaseOfDeletion()
	{
		// given
		final ItemVersionMarkerModel ivm = createVersionMarker(fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);
		ivm.setVersionValue("foo");
		ivm.setLastVersionValue("bar");
		ivm.setStatus(ItemVersionMarkerStatus.DELETED);
		modelService.save(ivm);

		// when
		changeDetectionService.resetStream(TEST_STREAM);

		// then
		modelService.refresh(ivm);
		assertThat(ivm.getVersionValue()).isEqualTo("foo");
	}

	@Test
	public void resetShouldReactiveIvmWithDeletedStatus()
	{
		// given
		final ItemVersionMarkerModel ivm = createVersionMarker(fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);
		modelService.remove(fooTitle);
		assertChangesNumberForStream(TEST_STREAM, 0);

		// when
		changeDetectionService.resetStream(TEST_STREAM);

		// then
		final InMemoryChangesCollector collector = new InMemoryChangesCollector();
		changeDetectionService.collectChangesForType(titleComposedType, TEST_STREAM, collector);
		assertChangesNumberForStream(TEST_STREAM, 1);

		modelService.refresh(ivm);
		assertThat(ivm.getStatus()).isEqualTo(ItemVersionMarkerStatus.ACTIVE);
	}


	/**
	 * More comprehensive end-to-end reset test: - ivm actual with timestamp matching item -> back to epoch - ivm with
	 * timestamp before item modifications -> back to epoch - ivm deleted before -> activate
	 */
	@Test
	public void resetStreamEndToEndTest()
	{
		// given
		modelService.saveAll(barTitle, bazTitle);
		final InMemoryChangesCollector beforeResetCollector = new InMemoryChangesCollector();
		final InMemoryChangesCollector afterResetCollector = new InMemoryChangesCollector();

		createVersionMarker(fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.ACTIVE);
		createVersionMarker(barTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
		createVersionMarker(bazTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);

		// when
		changeDetectionService.collectChangesForType(titleComposedType, TEST_STREAM, beforeResetCollector);
		changeDetectionService.consumeChanges(beforeResetCollector.getChanges());

		// then
		assertThat(beforeResetCollector.getChanges()).hasSize(2);
		assertThat(beforeResetCollector.getChanges()).onProperty("changeType").containsOnly(ChangeType.NEW, ChangeType.MODIFIED);

		// and when
		changeDetectionService.resetStream(TEST_STREAM);
		changeDetectionService.collectChangesForType(titleComposedType, TEST_STREAM, afterResetCollector);

		// then
		assertThat(afterResetCollector.getChanges()).hasSize(3);
		assertThat(afterResetCollector.getChanges()).onProperty("changeType").containsOnly(ChangeType.MODIFIED);
	}

	@Test
	public void deleteStreamShouldDeleteAllVersionMarkers()
	{
		// given
		modelService.saveAll(barTitle, bazTitle);

		createVersionMarker(fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.ACTIVE);
		createVersionMarker(barTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
		createVersionMarker(bazTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);

		// when
		changeDetectionService.deleteItemVersionMarkersForStream(TEST_STREAM);

		// then
		assertThat(countStreamVersionMarkers(TEST_STREAM)).isEqualTo(0);
	}

	private int countStreamVersionMarkers(final String streamId)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(
				"SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId}=?streamId");
		fsq.addQueryParameter("streamId", streamId);
		return flexibleSearchService.search(fsq).getCount();
	}

	private int countVersionMarkerByPK(final String streamId, final PK pk)
	{
		final FlexibleSearchQuery fsq = new FlexibleSearchQuery(
				"SELECT {PK} FROM {ItemVersionMarker as ivm} WHERE {ivm.streamId}=?streamId AND {ivm.itemPK}=?itemPK");
		fsq.addQueryParameters(ImmutableMap.of("streamId", streamId, "itemPK", pk));
		return flexibleSearchService.search(fsq).getCount();
	}

	private void assertChangesNumberForStream(final String streamId, final int changesNumber)
	{
		final InMemoryChangesCollector collector = new InMemoryChangesCollector();
		changeDetectionService.collectChangesForType(titleComposedType, streamId, collector);
		assertThat(collector.getChanges()).hasSize(changesNumber);
	}
}

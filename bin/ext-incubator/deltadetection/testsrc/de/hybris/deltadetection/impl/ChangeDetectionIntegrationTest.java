/*
 * 
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2014 hybris AG
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
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for change detection feature
 */
@IntegrationTest
public class ChangeDetectionIntegrationTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private ChangeDetectionService changeDetectionService;
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;

	private InMemoryChangesCollector inMemoryChangesCollector;
	private CustomerModel testCustomerJan;
	private CustomerModel testCustomerPiotr;
	private TitleModel testTitleFoo;
	private TitleModel testTitleBar;

	private static final String STREAM_ID_XXX = "FeedXXX";
	private static final String STREAM_ID_YYY = "FeedYYY";

	@Before
	public void setUp() throws Exception
	{
		inMemoryChangesCollector = new InMemoryChangesCollector();
		testCustomerJan = prepareCustomer("Jan", "Jan C.");
		testCustomerPiotr = prepareCustomer("Piotr", "Piotr H.");
		testTitleFoo = modelService.create(TitleModel.class);
		testTitleFoo.setCode("Foo");
		testTitleBar = modelService.create(TitleModel.class);
		testTitleBar.setCode("Bar");
		// don't persist
		// modelService.saveAll(testCustomerJan, testCustomerPiotr);
	}


	@Test
	public void testFindChangesForNewItem() throws Exception
	{
		modelService.save(testCustomerJan);

		final ItemChangeDTO change = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX);

		assertThat(change).isNotNull();
		assertThat(change.getItemPK()).isEqualTo(testCustomerJan.getPk().getLong());
		assertThat(change.getChangeType()).isEqualTo(ChangeType.NEW);
		assertThat(change.getVersion()).isEqualTo(testCustomerJan.getModifiedtime());
	}

	@Test
	public void testFindChangesForModifiedItem() throws Exception
	{
		modelService.save(testCustomerJan);
		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(),
				"New item created(" + testCustomerJan.toString() + ")",
				typeService.getComposedTypeForCode(testCustomerJan.getItemtype()), STREAM_ID_XXX);
		final Date markerVersion = testCustomerJan.getModifiedtime();

		Thread.sleep(2000L);
		testCustomerJan.setName("Jan is changed now");
		modelService.save(testCustomerJan);

		final ItemChangeDTO change = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX);

		assertThat(change).isNotNull();
		assertThat(change.getItemPK()).isEqualTo(testCustomerJan.getPk().getLong());
		assertThat(change.getChangeType()).isEqualTo(ChangeType.MODIFIED);
		assertThat(change.getVersion()).isEqualTo(testCustomerJan.getModifiedtime());
		assertThat(markerVersion.before(change.getVersion()));

	}


	@Test
	public void testFindChangesForNotChangedItem() throws Exception
	{
		modelService.save(testCustomerJan);

		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(),
				"New item created(" + testCustomerJan.toString() + ")",
				typeService.getComposedTypeForCode(testCustomerJan.getItemtype()), STREAM_ID_XXX);

		final ItemChangeDTO change = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX);
		assertThat(change).isNull();
	}

	@Test
	public void testFindChangeForRemovedItem() throws Exception
	{
		modelService.saveAll(testCustomerJan);
		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(),
				"New item created(" + testCustomerJan.toString() + ")",
				typeService.getComposedTypeForCode(testCustomerJan.getItemtype()), STREAM_ID_XXX);

		final PK oldPkJan = testCustomerJan.getPk();
		final Date oldModifiedTimeJan = testCustomerJan.getModifiedtime();
		modelService.removeAll(testCustomerJan);

		final ItemChangeDTO change = changeDetectionService.getChangeForRemovedItem(oldPkJan, STREAM_ID_XXX);

		assertThat(change).isNotNull();
		assertThat(change.getItemPK()).isEqualTo(oldPkJan.getLong());
		assertThat(change.getChangeType()).isEqualTo(ChangeType.DELETED);
		assertThat(change.getVersion()).isEqualTo(oldModifiedTimeJan);
	}

	@Test
	public void testFindChangesForRemovedItems() throws Exception
	{
		modelService.saveAll(testCustomerJan, testCustomerPiotr, testTitleFoo, testTitleBar);
		final ComposedTypeModel composedTypeCustomer = typeService.getComposedTypeForClass(CustomerModel.class);
		final ComposedTypeModel composedTypeTitle = typeService.getComposedTypeForClass(TitleModel.class);

		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(),
				"New item created(" + testCustomerJan.toString() + ")", composedTypeCustomer, STREAM_ID_XXX);
		saveVersionMarker(testCustomerPiotr.getPk(), testCustomerPiotr.getModifiedtime(),
				"New item created(" + testCustomerPiotr.toString() + ")", composedTypeCustomer, STREAM_ID_XXX);
		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(),
				"New item created(" + testTitleFoo.toString() + ")", composedTypeTitle, STREAM_ID_XXX);
		saveVersionMarker(testTitleBar.getPk(), testTitleBar.getModifiedtime(),
				"New item created(" + testTitleBar.toString() + ")", composedTypeTitle, STREAM_ID_XXX);

		final PK oldPkJan = testCustomerJan.getPk();
		final PK oldPkPiotr = testCustomerPiotr.getPk();
		final PK oldPkTitleFoo = testTitleFoo.getPk();
		modelService.removeAll(testCustomerJan, testCustomerPiotr, testTitleFoo);
		// 3 of 4 items removed - find these changes now

		final List<ItemChangeDTO> changes = changeDetectionService.getChangesForRemovedItems(STREAM_ID_XXX);
		assertThat(changes).hasSize(3);
		assertThat(changes).onProperty("itemPK").containsOnly(oldPkJan.getLong(), oldPkPiotr.getLong(), oldPkTitleFoo.getLong());
	}

	@Test
	public void testConsumeChangesForOneNewItem() throws Exception
	{
		modelService.saveAll(testCustomerJan);
		ItemChangeDTO change = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX);
		assertThat(change).isNotNull();

		changeDetectionService.consumeChanges(Arrays.asList(change));

		// changes consumed - should Not be found anymore
		change = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX);
		assertThat(change).isNull();
	}

	@Test
	public void testConsumeChangesForDifferentChangeTypesAndDifferentItemTypes() throws Exception
	{
		final ComposedTypeModel composedTypeCustomer = typeService.getComposedTypeForClass(CustomerModel.class);
		final ComposedTypeModel composedTypeTitle = typeService.getComposedTypeForClass(TitleModel.class);
		// one new, one modified, one removed and one up to date item
		modelService.saveAll(testCustomerJan, testCustomerPiotr, testTitleFoo, testTitleBar);
		saveVersionMarker(testCustomerPiotr.getPk(), testCustomerPiotr.getModifiedtime(),
				"New item created(" + testCustomerPiotr.toString() + ")", composedTypeCustomer, STREAM_ID_XXX);
		Thread.sleep(2000L);
		testCustomerPiotr.setName("Piotr is changed now");
		modelService.save(testCustomerPiotr); // Piotr is the modified item

		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(),
				"New item created(" + testTitleFoo.toString() + ")", composedTypeTitle, STREAM_ID_XXX);
		final PK oldPkTitleFoo = testTitleFoo.getPk();
		modelService.remove(testTitleFoo); // foo is deleted item

		saveVersionMarker(testTitleBar.getPk(), testTitleBar.getModifiedtime(),
				"New item created(" + testTitleBar.toString() + ")", composedTypeTitle, STREAM_ID_XXX);
		// bar is up to date
		ItemChangeDTO changesJan, changesPiotr, changesFoo, changesBar;

		assertThat(changesJan = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX)).isNotNull();
		assertThat(changesPiotr = changeDetectionService.getChangeForExistingItem(testCustomerPiotr, STREAM_ID_XXX)).isNotNull();
		assertThat(changesFoo = changeDetectionService.getChangeForRemovedItem(oldPkTitleFoo, STREAM_ID_XXX)).isNotNull();
		assertThat(changesBar = changeDetectionService.getChangeForExistingItem(testTitleBar, STREAM_ID_XXX)).isNull();

		changeDetectionService.consumeChanges(Arrays.asList(changesJan, changesPiotr, changesFoo, changesBar));

		// changes consumed - should Not be found anymore

		assertThat(changesJan = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX)).isNull();
		assertThat(changesPiotr = changeDetectionService.getChangeForExistingItem(testCustomerPiotr, STREAM_ID_XXX)).isNull();
		assertThat(changesFoo = changeDetectionService.getChangeForRemovedItem(oldPkTitleFoo, STREAM_ID_XXX)).isNull();
		assertThat(changesBar = changeDetectionService.getChangeForExistingItem(testTitleBar, STREAM_ID_XXX)).isNull();

	}

	@Test
	public void testFindChangesByType() throws Exception
	{
		final ComposedTypeModel composedTypeCustomer = typeService.getComposedTypeForClass(CustomerModel.class);
		final ComposedTypeModel composedTypeTitle = typeService.getComposedTypeForClass(TitleModel.class);
		// one new Title and customer, one modified Customer, one removed Title
		modelService.saveAll(testCustomerJan, testCustomerPiotr, testTitleFoo, testTitleBar);
		saveVersionMarker(testCustomerPiotr.getPk(), testCustomerPiotr.getModifiedtime(),
				"New item created(" + testCustomerPiotr.toString() + ")", composedTypeCustomer, STREAM_ID_XXX);
		Thread.sleep(2000L);
		testCustomerPiotr.setName("Piotr is changed now");
		modelService.save(testCustomerPiotr); // Piotr is the modified item

		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(),
				"New item created(" + testTitleFoo.toString() + ")", composedTypeTitle, STREAM_ID_XXX);
		final PK oldPkTitleFoo = testTitleFoo.getPk();
		modelService.remove(testTitleFoo); // foo is deleted item
		// bar is new item

		changeDetectionService.collectChangesForType(composedTypeTitle, STREAM_ID_XXX, inMemoryChangesCollector);
		final List<ItemChangeDTO> changes = inMemoryChangesCollector.getChanges();
		// should find only changes for title items

		assertThat(changes).hasSize(2);
		assertThat(changes).onProperty("itemPK").containsOnly(oldPkTitleFoo.getLong(), testTitleBar.getPk().getLong());
		assertThat(changes).onProperty("itemComposedType").containsOnly("Title");
	}

	@Test
	public void testFindChangesForNewItemStreamAware() throws Exception
	{
		modelService.save(testCustomerJan);

		final ItemChangeDTO changeXXX = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX);
		final ItemChangeDTO changeYYY = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_YYY);

		for (final ItemChangeDTO change : Arrays.asList(changeXXX, changeYYY))
		{
			assertThat(change).isNotNull();
			assertThat(change.getItemPK()).isEqualTo(testCustomerJan.getPk().getLong());
			assertThat(change.getChangeType()).isEqualTo(ChangeType.NEW);
			assertThat(change.getVersion()).isEqualTo(testCustomerJan.getModifiedtime());
		}
		assertThat(changeXXX.getStreamId()).isEqualTo(STREAM_ID_XXX);
		assertThat(changeYYY.getStreamId()).isEqualTo(STREAM_ID_YYY);
	}

	@Test
	public void testFindChangesForRemovedItemStreamAware() throws Exception
	{
		modelService.save(testCustomerJan);

		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(),
				"New item created(" + testCustomerJan.toString() + ")",
				typeService.getComposedTypeForCode(testCustomerJan.getItemtype()), STREAM_ID_XXX);
		final PK oldPkJan = testCustomerJan.getPk();
		final Date oldModifiedTimeJan = testCustomerJan.getModifiedtime();
		modelService.removeAll(testCustomerJan);

		final ItemChangeDTO changeXXX = changeDetectionService.getChangeForRemovedItem(oldPkJan, STREAM_ID_XXX);
		final ItemChangeDTO changeYYY = changeDetectionService.getChangeForRemovedItem(oldPkJan, STREAM_ID_YYY);

		assertThat(changeXXX.getStreamId()).isEqualTo(STREAM_ID_XXX);
		assertThat(changeXXX.getItemPK()).isEqualTo(oldPkJan.getLong());
		assertThat(changeXXX.getChangeType()).isEqualTo(ChangeType.DELETED);
		assertThat(changeXXX.getVersion()).isEqualTo(oldModifiedTimeJan);

		assertThat(changeYYY).isNull(); // up-to-date state
	}

	@Test
	public void testFindChangesForModifiedItemStreamAware() throws Exception
	{
		modelService.save(testCustomerJan);

		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(),
				"New item created(" + testCustomerJan.toString() + ")",
				typeService.getComposedTypeForCode(testCustomerJan.getItemtype()), STREAM_ID_XXX);
		final Date markerVersion = testCustomerJan.getModifiedtime();

		Thread.sleep(2000L);
		testCustomerJan.setName("Jan is changed now");
		modelService.save(testCustomerJan);

		final ItemChangeDTO changeXXX = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_XXX);
		final ItemChangeDTO changeYYY = changeDetectionService.getChangeForExistingItem(testCustomerJan, STREAM_ID_YYY);

		for (final ItemChangeDTO change : Arrays.asList(changeXXX, changeYYY))
		{
			assertThat(change).isNotNull();
			assertThat(change.getItemPK()).isEqualTo(testCustomerJan.getPk().getLong());
			assertThat(change.getVersion()).isEqualTo(testCustomerJan.getModifiedtime());
		}
		assertThat(changeXXX.getStreamId()).isEqualTo(STREAM_ID_XXX);
		assertThat(changeXXX.getChangeType()).isEqualTo(ChangeType.MODIFIED);
		assertThat(markerVersion.before(changeXXX.getVersion()));

		assertThat(changeYYY.getStreamId()).isEqualTo(STREAM_ID_YYY);
		assertThat(changeYYY.getChangeType()).isEqualTo(ChangeType.NEW);
	}

	@Test
	public void testFindChangesByTypeStreamAware() throws Exception
	{
		final ComposedTypeModel composedTypeCustomer = typeService.getComposedTypeForClass(CustomerModel.class);
		final ComposedTypeModel composedTypeTitle = typeService.getComposedTypeForClass(TitleModel.class);
		// one new Title and customer, one modified Customer, one removed Title -- for Stream XXX
		modelService.saveAll(testCustomerJan, testCustomerPiotr, testTitleFoo, testTitleBar);
		saveVersionMarker(testCustomerPiotr.getPk(), testCustomerPiotr.getModifiedtime(),
				"New item created(" + testCustomerPiotr.toString() + ")", composedTypeCustomer, STREAM_ID_XXX);
		Thread.sleep(2000L);
		testCustomerPiotr.setName("Piotr is changed now");
		modelService.save(testCustomerPiotr); // Piotr is the modified item

		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(),
				"New item created(" + testTitleFoo.toString() + ")", composedTypeTitle, STREAM_ID_XXX);
		final PK oldPkTitleFoo = testTitleFoo.getPk();
		modelService.remove(testTitleFoo); // foo is deleted item
		// bar is new item

		changeDetectionService.collectChangesForType(composedTypeTitle, STREAM_ID_XXX, inMemoryChangesCollector);
		final List<ItemChangeDTO> changesXXX = inMemoryChangesCollector.getChanges();
		// should find only changes for title items (1 New, 1 deleted)
		assertThat(changesXXX).hasSize(2);
		assertThat(changesXXX).onProperty("itemPK").containsOnly(oldPkTitleFoo.getLong(), testTitleBar.getPk().getLong());
		assertThat(changesXXX).onProperty("itemComposedType").containsOnly("Title");
		assertThat(changesXXX).onProperty("streamId").containsOnly(STREAM_ID_XXX);

		inMemoryChangesCollector.clearChanges();
		changeDetectionService.collectChangesForType(composedTypeTitle, STREAM_ID_YYY, inMemoryChangesCollector);
		final List<ItemChangeDTO> changesYYY = inMemoryChangesCollector.getChanges();
		// should find only 1 new title
		assertThat(changesYYY).hasSize(1);
		assertThat(changesYYY).onProperty("itemPK").containsOnly(testTitleBar.getPk().getLong());
		assertThat(changesYYY).onProperty("itemComposedType").containsOnly("Title");
		assertThat(changesYYY).onProperty("streamId").containsOnly(STREAM_ID_YYY);
	}

	private void saveVersionMarker(final PK itemPK, final Date version, final String info,
			final ComposedTypeModel itemComposedType, final String streamID)
	{
		final ItemVersionMarkerModel marker = modelService.create(ItemVersionMarkerModel.class);
		marker.setItemPK(itemPK.getLong());
		marker.setVersionTS(version);
		marker.setInfo(info);
		marker.setItemComposedType(itemComposedType);
		marker.setStreamId(streamID);
		marker.setStatus(ItemVersionMarkerStatus.ACTIVE);
		modelService.save(marker);
	}

	private CustomerModel prepareCustomer(final String id, final String name)
	{
		final CustomerModel result = modelService.create(CustomerModel.class);
		result.setName(name);
		result.setUid(id);
		return result;
	}
}

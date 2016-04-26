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
package de.hybris.deltadetection;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.deltadetection.model.ChangeDetectionJobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for detecting changes with given cronjob
 */
@IntegrationTest
public class ChangeDetectionJobTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	CronJobService cronJobService;
	@Resource
	MediaService mediaService;

	private ChangeDetectionJobModel testJobForTitle, testJobForDeliveryMode, testJobForCustomer;
	private TitleModel testTitleFoo, testTitleBar;
	private CustomerModel testCustomerJan, testCustomerPiotr;
	private DeliveryModeModel testDeliveryModeX, testDeliveryModeY;

	private static final String STREAM_ID_DEFAULT = "FeedDefault";
	private static final String STREAM_ID_BLABLA = "FeedBlaBla";

	@Before
	public void setUp() throws Exception
	{
		testTitleFoo = modelService.create(TitleModel.class);
		testTitleFoo.setCode("Foo");
		testTitleBar = modelService.create(TitleModel.class);
		testTitleBar.setCode("Bar");
		testCustomerJan = prepareCustomer("Jan", "Jan C.");
		testCustomerPiotr = prepareCustomer("Piotr", "Piotr H.");
		testDeliveryModeX = modelService.create(DeliveryModeModel.class);
		testDeliveryModeX.setCode("testDeliveryModeX");
		testDeliveryModeY = modelService.create(DeliveryModeModel.class);
		testDeliveryModeY.setCode("testDeliveryModeY");
		modelService.saveAll(testTitleFoo, testTitleBar, testCustomerJan, testCustomerPiotr, testDeliveryModeX, testDeliveryModeY);
		testJobForTitle = modelService.create(ChangeDetectionJobModel.class);
		testJobForTitle.setCode("testChangeDetectionJobForTitle");
		testJobForTitle.setStreamId(STREAM_ID_DEFAULT);
		testJobForTitle.setTypePK(typeService.getComposedTypeForClass(TitleModel.class));
		testJobForDeliveryMode = modelService.create(ChangeDetectionJobModel.class);
		testJobForDeliveryMode.setCode("testChangeDetectionJobForDeliveryMode");
		testJobForDeliveryMode.setStreamId(STREAM_ID_DEFAULT);
		testJobForDeliveryMode.setTypePK(typeService.getComposedTypeForClass(DeliveryModeModel.class));
		testJobForCustomer = modelService.create(ChangeDetectionJobModel.class);
		testJobForCustomer.setCode("testChangeDetectionJobForCustomer");
		testJobForCustomer.setStreamId(STREAM_ID_DEFAULT);
		testJobForCustomer.setTypePK(typeService.getComposedTypeForClass(CustomerModel.class));
		modelService.saveAll(testJobForTitle, testJobForDeliveryMode, testJobForCustomer);
	}

	@Test
	public void testShouldFindChangesOnlyInRemovedItems() throws Exception
	{
		final ComposedTypeModel composedTypeForTitle = typeService.getComposedTypeForClass(TitleModel.class);
		final ComposedTypeModel composedTypeForCustomer = typeService.getComposedTypeForClass(CustomerModel.class);
		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(), "Title - " + testTitleFoo.getCode(),
				composedTypeForTitle);
		saveVersionMarker(testTitleBar.getPk(), testTitleBar.getModifiedtime(), "Title - " + testTitleBar.getCode(),
				composedTypeForTitle);
		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(), "Customer - " + testCustomerJan.getName(),
				composedTypeForCustomer);
		saveVersionMarker(testCustomerPiotr.getPk(), testCustomerPiotr.getModifiedtime(),
				"Customer - " + testCustomerPiotr.getName(), composedTypeForCustomer);

		modelService.remove(testTitleFoo);
		modelService.remove(testTitleBar);
		modelService.remove(testCustomerJan);

		final CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", testJobForTitle);
		modelService.save(myCronjobForTitle);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForTitle, true);
		checkGeneratedMedia(myCronjobForTitle, testJobForTitle);
		final CronJobModel myCronjobForCustomer = prepareCronjob("cronjobForCustomer", testJobForCustomer);
		modelService.save(myCronjobForCustomer);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForCustomer, true);
		checkGeneratedMedia(myCronjobForCustomer, testJobForCustomer);
	}

	@Test
	public void testShouldFindChangesOnlyInModifiedItems() throws Exception
	{
		final ComposedTypeModel composedTypeForTitle = typeService.getComposedTypeForClass(TitleModel.class);
		final ComposedTypeModel composedTypeForCustomer = typeService.getComposedTypeForClass(CustomerModel.class);
		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(), "Title - " + testTitleFoo.getCode(),
				composedTypeForTitle);
		saveVersionMarker(testTitleBar.getPk(), testTitleBar.getModifiedtime(), "Title - " + testTitleBar.getCode(),
				composedTypeForTitle);
		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(), "Customer - " + testCustomerJan.getName(),
				composedTypeForCustomer);
		saveVersionMarker(testCustomerPiotr.getPk(), testCustomerPiotr.getModifiedtime(),
				"Customer - " + testCustomerPiotr.getName(), composedTypeForCustomer);

		testTitleFoo.setName("title Foo - changed name");
		testCustomerJan.setName("customer Jan - changed name");
		Thread.sleep(2000L);
		modelService.saveAll(testTitleFoo, testCustomerJan);

		final CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", testJobForTitle);
		modelService.save(myCronjobForTitle);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForTitle, true);
		checkGeneratedMedia(myCronjobForTitle, testJobForTitle);
		final CronJobModel myCronjobForCustomer = prepareCronjob("cronjobForCustomer", testJobForCustomer);
		modelService.save(myCronjobForCustomer);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForCustomer, true);
		checkGeneratedMedia(myCronjobForCustomer, testJobForCustomer);
	}

	@Test
	public void testShouldFindChangesOnlyInNewItems() throws Exception
	{
		final ComposedTypeModel composedTypeForTitle = typeService.getComposedTypeForClass(TitleModel.class);
		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(), "Title - " + testTitleFoo.getCode(),
				composedTypeForTitle);

		final CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", testJobForTitle);
		modelService.save(myCronjobForTitle);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForTitle, true);
		checkGeneratedMedia(myCronjobForTitle, testJobForTitle);
		final CronJobModel myCronjobForDeliverymode = prepareCronjob("cronjobForDeliveryMode", testJobForDeliveryMode);
		modelService.save(myCronjobForDeliverymode);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForDeliverymode, true);
		checkGeneratedMedia(myCronjobForDeliverymode, testJobForDeliveryMode);
	}

	@Test
	public void testDetectDifferentKindOfChangesInDifferentItemTypes() throws Exception
	{
		final ComposedTypeModel composedTypeForTitle = typeService.getComposedTypeForClass(TitleModel.class);
		final ComposedTypeModel composedTypeForCustomer = typeService.getComposedTypeForClass(CustomerModel.class);
		final ComposedTypeModel composedTypeForDeliveryMode = typeService.getComposedTypeForClass(DeliveryModeModel.class);
		saveVersionMarker(testTitleFoo.getPk(), testTitleFoo.getModifiedtime(), "Title - " + testTitleFoo.getCode(),
				composedTypeForTitle);
		saveVersionMarker(testCustomerJan.getPk(), testCustomerJan.getModifiedtime(), "Customer - " + testCustomerJan.getName(),
				composedTypeForCustomer);
		saveVersionMarker(testCustomerPiotr.getPk(), testCustomerPiotr.getModifiedtime(),
				"Customer - " + testCustomerPiotr.getName(), composedTypeForCustomer);
		saveVersionMarker(testDeliveryModeY.getPk(), testDeliveryModeY.getModifiedtime(),
				"DeliveryMode - " + testDeliveryModeY.getCode(), composedTypeForDeliveryMode);
		modelService.remove(testCustomerPiotr);
		testDeliveryModeY.setName("deliverMode Y - changed name");
		testCustomerJan.setName("customer Jan - changed name");
		Thread.sleep(2000L);
		modelService.saveAll(testDeliveryModeY, testCustomerJan);

		// testDeliverModeX and testTitleBar are new
		// testTitleFoo is up to date
		// testCustomerPiotr is deleted
		// testDeliveryModeY and testCustomerJan are modified

		// detect changes now
		// title
		final CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", testJobForTitle);
		modelService.save(myCronjobForTitle);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForTitle, true);
		checkGeneratedMedia(myCronjobForTitle, testJobForTitle);
		// customer
		final CronJobModel myCronjobForCustomer = prepareCronjob("cronjobForCustomer", testJobForCustomer);
		modelService.save(myCronjobForCustomer);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForCustomer, true);
		checkGeneratedMedia(myCronjobForCustomer, testJobForCustomer);
		// deliverymode
		final CronJobModel myCronjobForDeliveryMode = prepareCronjob("cronjobForDeliveryMode", testJobForDeliveryMode);
		modelService.save(myCronjobForDeliveryMode);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForDeliveryMode, true);
		checkGeneratedMedia(myCronjobForDeliveryMode, testJobForDeliveryMode);
	}

	@Test
	public void testDetectChangesStreamAware() throws Exception
	{
		final ComposedTypeModel composedTypeForDeliveryMode = typeService.getComposedTypeForClass(DeliveryModeModel.class);
		saveVersionMarker(testDeliveryModeY.getPk(), testDeliveryModeY.getModifiedtime(),
				"DeliveryMode - " + testDeliveryModeY.getCode(), composedTypeForDeliveryMode);
		testDeliveryModeY.setName("deliveryMode Y - changed name");
		modelService.saveAll(testDeliveryModeY);
		// testDeliverModeX is new
		// testDeliveryModeY is modified (for StreamDefault)

		// find changes for Stream Default
		final CronJobModel myCronjobForDeliveryMode = prepareCronjob("cronjobForDeliveryMode", testJobForDeliveryMode);
		assertThat(((ChangeDetectionJobModel) myCronjobForDeliveryMode.getJob()).getStreamId()).isEqualTo(STREAM_ID_DEFAULT);
		modelService.save(myCronjobForDeliveryMode);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForDeliveryMode, true);
		checkGeneratedMedia(myCronjobForDeliveryMode, testJobForDeliveryMode);
		Thread.sleep(2000L);
		// find changes for Stream BlaBla
		testJobForDeliveryMode.setStreamId(STREAM_ID_BLABLA);
		modelService.save(testJobForDeliveryMode);
		final CronJobModel myCronjobForDeliveryModeAnotherStream = prepareCronjob("cronjobForDeliveryModeStreamBlaBla",
				testJobForDeliveryMode);
		assertThat(((ChangeDetectionJobModel) myCronjobForDeliveryModeAnotherStream.getJob()).getStreamId()).isEqualTo(
				STREAM_ID_BLABLA);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForDeliveryModeAnotherStream, true);
		checkGeneratedMedia(myCronjobForDeliveryModeAnotherStream, testJobForDeliveryMode);
	}

	private CronJobModel prepareCronjob(final String code, final JobModel job)
	{
		final CronJobModel cronjob = modelService.create(CronJobModel.class);
		cronjob.setCode(code);
		cronjob.setSingleExecutable(Boolean.TRUE);
		cronjob.setJob(job);
		modelService.save(cronjob);
		return cronjob;
	}

	private void saveVersionMarker(final PK itemPK, final Date version, final String info, final ComposedTypeModel itemComposedType)
	{
		final ItemVersionMarkerModel marker = modelService.create(ItemVersionMarkerModel.class);
		marker.setItemPK(itemPK.getLong());
		marker.setVersionTS(version);
		marker.setInfo(info);
		marker.setItemComposedType(itemComposedType);
		marker.setStreamId(STREAM_ID_DEFAULT);
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

	private void checkGeneratedMedia(final CronJobModel cronjob, final ChangeDetectionJobModel job)
	{
		final String mediaCode = "report_" + job.getStreamId() + "_" + job.getTypePK().getCode() + "_" + cronjob.getCode() + "_"
				+ job.getCode();
		final MediaModel media = mediaService.getMedia(mediaCode);
		assertThat(media).isNotNull();
		assertThat(media).isEqualTo(job.getOutput());
	}
}

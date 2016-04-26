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
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.deltadetection.model.ChangeDetectionJobModel;
import de.hybris.deltadetection.model.ScriptChangeConsumptionJobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for detecting and consuming changes with dedicated jobs.
 */
@IntegrationTest
public class DetectionAndConsumptionJobsIntegrationTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private CronJobService cronJobService;
	@Resource
	private ChangeDetectionService changeDetectionService;

	private ScriptChangeConsumptionJobModel testConsumeJobForTitle, testConsumeJobForDeliveryMode;
	private ChangeDetectionJobModel testDetectionJobForTitle, testDetectionJobForDeliveryMode;
	private TitleModel testTitleFoo, testTitleBar;
	private DeliveryModeModel testDeliveryModeX, testDeliveryModeY, testDeliveryModeZ;

	private static final String STREAM_ID_DEFAULT = "FeedDefault";
	private static final String URI_FOR_SCRIPT_CONSUME_ALL = "classpath://test/script-change-consumer-all.groovy";
	private static final String URI_FOR_SCRIPT_CONSUME_DELETED = "classpath://test/script-change-consumer-deleted.groovy";

	@Before
	public void setUp() throws Exception
	{
		testTitleFoo = modelService.create(TitleModel.class);
		testTitleFoo.setCode("Foo");
		testTitleBar = modelService.create(TitleModel.class);
		testTitleBar.setCode("Bar");
		testDeliveryModeX = modelService.create(DeliveryModeModel.class);
		testDeliveryModeX.setCode("testDeliveryModeX");
		testDeliveryModeY = modelService.create(DeliveryModeModel.class);
		testDeliveryModeY.setCode("testDeliveryModeY");
		testDeliveryModeZ = modelService.create(DeliveryModeModel.class);
		testDeliveryModeZ.setCode("testDeliveryModeZ");

		modelService.saveAll(testTitleFoo, testTitleBar, testDeliveryModeX, testDeliveryModeY, testDeliveryModeZ);

		testDetectionJobForTitle = modelService.create(ChangeDetectionJobModel.class);
		testDetectionJobForTitle.setCode("testChangeDetectionJobForTitle");
		testDetectionJobForTitle.setStreamId(STREAM_ID_DEFAULT);
		testDetectionJobForTitle.setTypePK(typeService.getComposedTypeForClass(TitleModel.class));

		testDetectionJobForDeliveryMode = modelService.create(ChangeDetectionJobModel.class);
		testDetectionJobForDeliveryMode.setCode("testChangeDetectionJobForDeliveryMode");
		testDetectionJobForDeliveryMode.setStreamId(STREAM_ID_DEFAULT);
		testDetectionJobForDeliveryMode.setTypePK(typeService.getComposedTypeForClass(DeliveryModeModel.class));

		modelService.saveAll(testDetectionJobForTitle, testDetectionJobForDeliveryMode);

		testConsumeJobForTitle = modelService.create(ScriptChangeConsumptionJobModel.class);
		testConsumeJobForTitle.setCode("testScriptChangeConsumptionJobForTitle");
		testConsumeJobForTitle.setScriptURI(URI_FOR_SCRIPT_CONSUME_ALL);

		testConsumeJobForDeliveryMode = modelService.create(ScriptChangeConsumptionJobModel.class);
		testConsumeJobForDeliveryMode.setCode("testScriptChangeConsumptionJobForDeliveryMode");
		testConsumeJobForDeliveryMode.setScriptURI(URI_FOR_SCRIPT_CONSUME_DELETED);
		// don't persist the consumer jobs here - set input parameter inside test - methods first
	}

	@Test
	public void testDetectAndConsumeAllChangesForGivenType() throws Exception
	{
		final ComposedTypeModel composedTypeForTitle = typeService.getComposedTypeForClass(TitleModel.class);
		final InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
		final CronJobModel myDetectionCronjobForTitle = prepareCronjob("cronjobForTitleChangesDetection", testDetectionJobForTitle);
		// detect
		Thread.sleep(3000L);
		cronJobService.performCronJob(myDetectionCronjobForTitle, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 2);

		final MediaModel detectionOutput = testDetectionJobForTitle.getOutput();
		assertThat(detectionOutput).isNotNull();

		testConsumeJobForTitle.setInput(detectionOutput);
		modelService.save(testConsumeJobForTitle);
		final CronJobModel myConsumptionCronjobForTitle = prepareCronjob("cronjobForTitleChangesConsumption",
				testConsumeJobForTitle);
		// consume
		Thread.sleep(3000L);
		cronJobService.performCronJob(myConsumptionCronjobForTitle, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 0);
	}

	@Test
	public void testDetectAndConsumeOnlyDeletedChangesForGivenType() throws Exception
	{
		final ComposedTypeModel composedTypeForDeliveryMode = typeService.getComposedTypeForClass(DeliveryModeModel.class);
		final InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
		final CronJobModel myDetectionCronjobForDeliveryMode = prepareCronjob("cronjobForDeliveryModeChangesDetection",
				testDetectionJobForDeliveryMode);
		// detect
		Thread.sleep(3000L);
		cronJobService.performCronJob(myDetectionCronjobForDeliveryMode, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3); // all of type new

		MediaModel detectionOutput = testDetectionJobForDeliveryMode.getOutput();
		assertThat(detectionOutput).isNotNull();
		testConsumeJobForDeliveryMode.setInput(detectionOutput);
		modelService.save(testConsumeJobForDeliveryMode);
		final CronJobModel myConsumptionCronjobForDeliveryModeFoo = prepareCronjob("cronjobForDeliveryModeChangesConsumptionFoo",
				testConsumeJobForDeliveryMode);
		// consume
		Thread.sleep(3000L);
		cronJobService.performCronJob(myConsumptionCronjobForDeliveryModeFoo, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3); // nothing consumed

		changeDetectionService.consumeChanges(inMemoryCollector.getChanges());
		modelService.remove(testDeliveryModeX);
		modelService.remove(testDeliveryModeY);
		testDeliveryModeZ.setName("blabla");
		modelService.save(testDeliveryModeZ);

		// detect again
		final CronJobModel myDetectionCronjobForDeliveryModeBar = prepareCronjob("cronjobForDeliveryModeChangesDetectionBar",
				testDetectionJobForDeliveryMode);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myDetectionCronjobForDeliveryModeBar, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3); // but this time 2 of type deleted

		modelService.refresh(testDetectionJobForDeliveryMode);

		detectionOutput = testDetectionJobForDeliveryMode.getOutput();
		assertThat(detectionOutput).isNotNull();
		testConsumeJobForDeliveryMode.setInput(detectionOutput);
		modelService.save(testConsumeJobForDeliveryMode);
		final CronJobModel myConsumptionCronjobForDeliveryModeBar = prepareCronjob("cronjobForDeliveryModeChangesConsumptionBar",
				testConsumeJobForDeliveryMode);
		// consume again
		Thread.sleep(3000L);
		cronJobService.performCronJob(myConsumptionCronjobForDeliveryModeBar, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 1); // only one left (type=modified)
	}

	private void checkCurrentAmountOfChanges(final ComposedTypeModel composedTypeForDeliveryMode,
			final InMemoryChangesCollector inMemoryCollector, final int expectedSize)
	{
		inMemoryCollector.clearChanges();
		changeDetectionService.collectChangesForType(composedTypeForDeliveryMode, STREAM_ID_DEFAULT, inMemoryCollector);
		assertThat(inMemoryCollector.getChanges()).hasSize(expectedSize);
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

}

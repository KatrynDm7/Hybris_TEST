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
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.impl.CsvReportChangesCollector;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.deltadetection.model.ScriptChangeConsumptionJobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests for consuming changes with scripts by given cronjob.
 */
@IntegrationTest
public class ScriptChangeConsumptionJobTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	@Resource
	private CronJobService cronJobService;
	@Resource
	private ChangeDetectionService changeDetectionService;
	@Resource
	private MediaService mediaService;

	private ScriptChangeConsumptionJobModel testJobForTitleWithConsumeAllScript, testJobForDeliveryModeWithConsumeDeletedScript,
			testJobForTitleConsumeNothing;
	private TitleModel testTitleFoo, testTitleBar;
	private DeliveryModeModel testDeliveryModeX, testDeliveryModeY, testDeliveryModeZ;

	private static final String STREAM_ID_DEFAULT = "FeedDefault";
	private static final String URI_FOR_SCRIPT_CONSUME_ALL = "classpath://test/script-change-consumer-all.groovy";
	private static final String URI_FOR_SCRIPT_CONSUME_DELETED = "classpath://test/script-change-consumer-deleted.groovy";
	private static final String URI_FOR_SCRIPT_CONSUME_NOTHING = "classpath://test/script-change-consumer-nothing.groovy";

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

		testJobForTitleWithConsumeAllScript = modelService.create(ScriptChangeConsumptionJobModel.class);
		testJobForTitleWithConsumeAllScript.setCode("testScriptChangeConsumptionJobForTitle");
		testJobForTitleWithConsumeAllScript.setInput(prepareMediaCSVReport(STREAM_ID_DEFAULT,
				typeService.getComposedTypeForClass(TitleModel.class), "testCronjob", "testJob"));
		testJobForTitleConsumeNothing = modelService.create(ScriptChangeConsumptionJobModel.class);
		testJobForTitleConsumeNothing.setCode("testScriptChangeConsumptionJobForTitleNothing");
		testJobForTitleConsumeNothing.setInput(prepareMediaCSVReport(STREAM_ID_DEFAULT,
				typeService.getComposedTypeForClass(TitleModel.class), "testCronjobNothing", "testJobNothing"));
		testJobForDeliveryModeWithConsumeDeletedScript = modelService.create(ScriptChangeConsumptionJobModel.class);
		testJobForDeliveryModeWithConsumeDeletedScript.setCode("testScriptChangeConsumptionJobForDeliveryMode");
		testJobForDeliveryModeWithConsumeDeletedScript.setInput(prepareMediaCSVReport(STREAM_ID_DEFAULT,
				typeService.getComposedTypeForClass(DeliveryModeModel.class), "testCronjob", "testJob"));
		testJobForDeliveryModeWithConsumeDeletedScript.setScriptURI(URI_FOR_SCRIPT_CONSUME_DELETED);

		testJobForTitleWithConsumeAllScript.setScriptURI(URI_FOR_SCRIPT_CONSUME_ALL);
		testJobForTitleConsumeNothing.setScriptURI(URI_FOR_SCRIPT_CONSUME_NOTHING);
		modelService.saveAll(testJobForTitleWithConsumeAllScript, testJobForDeliveryModeWithConsumeDeletedScript,
				testJobForTitleConsumeNothing);
	}

	@Test
	public void testConsumeAllChangesWithScript() throws Exception
	{
		final CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", testJobForTitleWithConsumeAllScript);
		final InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
		final ComposedTypeModel composedTypeForTitle = typeService.getComposedTypeForClass(TitleModel.class);
		changeDetectionService.collectChangesForType(composedTypeForTitle, STREAM_ID_DEFAULT, inMemoryCollector);
		assertThat(inMemoryCollector.getChanges()).hasSize(2);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForTitle, true);
		Thread.sleep(3000L);
		inMemoryCollector.clearChanges();
		changeDetectionService.collectChangesForType(composedTypeForTitle, STREAM_ID_DEFAULT, inMemoryCollector);
		assertThat(inMemoryCollector.getChanges()).isEmpty();
	}

	@Test
	public void testConsumeNothingWithScript() throws Exception
	{
		final CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitleNothing", testJobForTitleConsumeNothing);
		final InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
		final ComposedTypeModel composedTypeForTitle = typeService.getComposedTypeForClass(TitleModel.class);
		checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 2);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForTitle, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 2);// nothing consumed
	}

	@Test
	public void testConsumeDeletedChangesWithScript() throws Exception
	{
		final ComposedTypeModel composedTypeForDeliveryMode = typeService.getComposedTypeForClass(DeliveryModeModel.class);
		final InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);

		final CronJobModel myCronjobForDeliveryModeFoo = prepareCronjob("cronjobForDeliveryModeFoo",
				testJobForDeliveryModeWithConsumeDeletedScript);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForDeliveryModeFoo, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
		// nothing consumed - the script consumes only DELETED

		changeDetectionService.consumeChanges(inMemoryCollector.getChanges());
		modelService.remove(testDeliveryModeX);
		modelService.remove(testDeliveryModeY);
		testDeliveryModeZ.setName("blabla");
		modelService.save(testDeliveryModeZ);

		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
		// now we have 2 of type=DELETED (which will be consumed by script) and 1 of type=MODIFIED

		testJobForDeliveryModeWithConsumeDeletedScript.setInput(prepareMediaCSVReport(STREAM_ID_DEFAULT,
				typeService.getComposedTypeForClass(DeliveryModeModel.class), "testCronjobXYZ", "testJobXYZ"));
		modelService.save(testJobForDeliveryModeWithConsumeDeletedScript);

		final CronJobModel myCronjobForDeliveryModeBar = prepareCronjob("cronjobForDeliveryModeBar",
				testJobForDeliveryModeWithConsumeDeletedScript);
		Thread.sleep(3000L);
		cronJobService.performCronJob(myCronjobForDeliveryModeBar, true);
		Thread.sleep(3000L);
		checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 1);
		assertThat(inMemoryCollector.getChanges()).onProperty("changeType").containsOnly(ChangeType.MODIFIED);
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

	private MediaModel prepareMediaCSVReport(final String streamId, final ComposedTypeModel type, final String cronjobCode,
			final String jobCode)
	{
		final String mediaCode = "report_" + streamId + "_" + type.getCode() + "_" + cronjobCode + "_" + jobCode;
		final CatalogUnawareMediaModel mediaInput = prepareMedia(mediaCode, mediaCode + ".csv", "text/csv");

		String reportAbsolutePath = null;
		FileWriter csvWriter = null;
		try
		{
			final Path path = Files.createTempFile(mediaCode, ".csv");
			csvWriter = new FileWriter(path.toFile());
			changeDetectionService.collectChangesForType(type, streamId, new CsvReportChangesCollector(csvWriter));
			reportAbsolutePath = path.toAbsolutePath().toString();
		}
		catch (final Exception e)
		{
			throw new RuntimeException("Could Not prepare report csv file", e);
		}
		finally
		{
			if (csvWriter != null)
			{
				try
				{
					csvWriter.close();
					modelService.save(mediaInput);
					mediaService.setStreamForMedia(mediaInput, new FileInputStream(reportAbsolutePath));
				}
				catch (final IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}
		return mediaInput;
	}

	private CatalogUnawareMediaModel prepareMedia(final String code, final String reportFileName, final String mimeType)
	{
		final CatalogUnawareMediaModel media = modelService.create(CatalogUnawareMediaModel.class);
		media.setCode(code);
		media.setMime(mimeType);
		media.setRealFileName(reportFileName);
		return media;
	}



}

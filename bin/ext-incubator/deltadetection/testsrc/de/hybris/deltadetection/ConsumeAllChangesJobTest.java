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
import de.hybris.deltadetection.impl.CsvReportChangesCollector;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.deltadetection.model.ConsumeAllChangesJobModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
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
 * Tests for consuming changes with given cronjob
 */
@IntegrationTest
public class ConsumeAllChangesJobTest extends ServicelayerTransactionalBaseTest
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

	private ConsumeAllChangesJobModel testJobForTitle;
	private TitleModel testTitleFoo, testTitleBar;

	private static final String STREAM_ID_DEFAULT = "FeedDefault";

	@Before
	public void setUp() throws Exception
	{
		testTitleFoo = modelService.create(TitleModel.class);
		testTitleFoo.setCode("Foo");
		testTitleBar = modelService.create(TitleModel.class);
		testTitleBar.setCode("Bar");
		modelService.saveAll(testTitleFoo, testTitleBar);

		testJobForTitle = modelService.create(ConsumeAllChangesJobModel.class);
		testJobForTitle.setCode("testChangeConsumptionJobForTitle");
		testJobForTitle.setInput(prepareMediaCSVReport(STREAM_ID_DEFAULT, typeService.getComposedTypeForClass(TitleModel.class),
				"testCronjob", "testJob"));
		modelService.save(testJobForTitle);
	}

	@Test
	public void testConsumeAllChanges() throws Exception
	{
		final CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", testJobForTitle);
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
			reportAbsolutePath = path.toAbsolutePath().toString();
			csvWriter = new FileWriter(path.toFile());
			changeDetectionService.collectChangesForType(type, streamId, new CsvReportChangesCollector(csvWriter));
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

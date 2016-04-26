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

package de.hybris.y2ysync.job;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.EncodingsDataCreator;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.model.cronjob.ImpExExportJobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncConfigService;
import de.hybris.y2ysync.services.SyncExecutionService;
import de.hybris.y2ysync.services.SyncExecutionService.ExecutionMode;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@ManualTest
public class Y2YSyncMasterJobPerformableTest extends ServicelayerBaseTest
{
	private static final Logger LOG = Logger.getLogger(Y2YSyncMasterJobPerformableTest.class);

	@Resource
	private RestTemplate restTemplate;
	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private CronJobService cronJobService;
	@Resource
	private ImportService importService;
	@Resource
	private SyncConfigService syncConfigService;
	@Resource
	SyncExecutionService syncExecutionService;
	@Resource
	private TypeService typeService;

	private final PropertyConfigSwitcher dataHubUrlProperty = new PropertyConfigSwitcher("y2ysync.datahub.url");
	private Y2YStreamConfigurationContainerModel dataHubSyncContainer;
	private Y2YStreamConfigurationContainerModel zipSyncContainer;

	@Before
	public void setUp() throws Exception
	{
		new EncodingsDataCreator().populateDatabase();
		importCsv("/test/source_test_catalog.csv", "utf-8");

		final ImpExExportJobModel exportJob = modelService.create(ImpExExportJobModel.class);
		exportJob.setCode(ImpExConstants.CronJob.DEFAULT_EXPORT_JOB_CODE);
		modelService.save(exportJob);

		dataHubUrlProperty.switchToValue("/datahub-webapp/v1/data-feeds/y2ysync");

		final List<CatalogVersionModel> vers = getTestCatalogVersions();


		dataHubSyncContainer = syncConfigService.createStreamConfigurationContainer("dataHubSyncContainer",
				catalogVersionService.getCatalogVersion("CatalogA", "CatalogVersionA1"));
		modelService.save(dataHubSyncContainer);
		for (final CatalogVersionModel ctgVer : vers)
		{
			final Y2YStreamConfigurationModel ctgVerConf = syncConfigService.createStreamConfiguration(dataHubSyncContainer,
					"CatalogVersion", "{version} = ?catalogVersionCode", ctgVer, prepareCatalogVersionAttributeDescriptors(),
					Collections.emptySet());
			final Y2YStreamConfigurationModel categConf = syncConfigService.createStreamConfiguration(dataHubSyncContainer,
					"Category", ctgVer, prepareCategoryAttributeDescriptors(), Collections.emptySet());
			modelService.saveAll(ctgVerConf, categConf);
		}

		modelService.save(syncConfigService.createStreamConfiguration(dataHubSyncContainer, "Product",
				prepareProductAttributeDescriptors(), Collections.emptySet()));

		zipSyncContainer = syncConfigService.createStreamConfigurationContainer("zipSyncContainer",
				catalogVersionService.getCatalogVersion("CatalogA", "CatalogVersionA1"));
		modelService.save(dataHubSyncContainer);
		for (final CatalogVersionModel ctgVer : vers)
		{
			final Y2YStreamConfigurationModel ctgVerConf = syncConfigService.createStreamConfiguration(zipSyncContainer,
					"CatalogVersion", "{version} = ?catalogVersionCode", ctgVer, prepareCatalogVersionAttributeDescriptors(),
					Collections.emptySet());
			final Y2YStreamConfigurationModel categConf = syncConfigService.createStreamConfiguration(zipSyncContainer, "Category",
					ctgVer, prepareCategoryAttributeDescriptors(), Collections.emptySet());
			final Y2YStreamConfigurationModel mediaConf = syncConfigService.createStreamConfiguration(zipSyncContainer, "Media",
					ctgVer, prepareMediaAttributeDescriptors(), Sets.newHashSet(syncConfigService.createUntypedColumnDefinition(
							"@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator]", "media")));
			modelService.saveAll(ctgVerConf, categConf, mediaConf);
		}

		modelService.save(syncConfigService.createStreamConfiguration(zipSyncContainer, "Product",
				prepareProductAttributeDescriptors(), Collections.emptySet()));
	}

	private Set<AttributeDescriptorModel> prepareCatalogVersionAttributeDescriptors()
	{
		final Set<AttributeDescriptorModel> res = new HashSet<>();

		res.add(typeService.getAttributeDescriptor("CatalogVersion", "catalog"));
		res.add(typeService.getAttributeDescriptor("CatalogVersion", "version"));
		res.add(typeService.getAttributeDescriptor("CatalogVersion", "languages"));
		res.add(typeService.getAttributeDescriptor("CatalogVersion", "inclAssurance"));
		res.add(typeService.getAttributeDescriptor("CatalogVersion", "inclDuty"));
		res.add(typeService.getAttributeDescriptor("CatalogVersion", "inclFreight"));
		res.add(typeService.getAttributeDescriptor("CatalogVersion", "inclPacking"));
		res.add(typeService.getAttributeDescriptor("CatalogVersion", "active"));

		return res;
	}

	private Set<AttributeDescriptorModel> prepareCategoryAttributeDescriptors()
	{
		final Set<AttributeDescriptorModel> res = new HashSet<>();

		res.add(typeService.getAttributeDescriptor("Category", "code"));
		res.add(typeService.getAttributeDescriptor("Category", "name"));
		res.add(typeService.getAttributeDescriptor("Category", "catalogVersion"));

		return res;
	}

	private Set<AttributeDescriptorModel> prepareProductAttributeDescriptors()
	{
		final Set<AttributeDescriptorModel> res = new HashSet<>();

		res.add(typeService.getAttributeDescriptor("Product", "code"));
		res.add(typeService.getAttributeDescriptor("Product", "catalogVersion"));
		res.add(typeService.getAttributeDescriptor("Product", "name"));
		res.add(typeService.getAttributeDescriptor("Product", "unit"));
		res.add(typeService.getAttributeDescriptor("Product", "supercategories"));
		res.add(typeService.getAttributeDescriptor("Product", "approvalStatus"));

		return res;
	}

	private Set<AttributeDescriptorModel> prepareMediaAttributeDescriptors()
	{
		final Set<AttributeDescriptorModel> res = new HashSet<>();

		res.add(typeService.getAttributeDescriptor("Media", "code"));
		res.add(typeService.getAttributeDescriptor("Media", "catalogVersion"));
		res.add(typeService.getAttributeDescriptor("Media", "mediaFormat"));
		res.add(typeService.getAttributeDescriptor("Media", "mime"));
		res.add(typeService.getAttributeDescriptor("Media", "folder"));

		return res;
	}

	@After
	public void tearDown() throws Exception
	{
		dataHubUrlProperty.switchBackToDefault();
	}

	@Test
	public void shouldRunSyncProcessToDataHub() throws Exception
	{
		// given
		final Y2YSyncJobModel job = syncExecutionService.createSyncJobForDataHub("dataHubTestSyncJob", dataHubSyncContainer);
		modelService.save(job);

		final MockRestServiceServer server = getMockRestServer();
		server.expect(requestTo("/datahub-webapp/v1/data-feeds/y2ysync")) //
				.andExpect(method(HttpMethod.POST)) //
				.andExpect(content().contentType("application/json;charset=UTF-8")) //
				.andRespond(withSuccess()); //


		// when
		syncExecutionService.startSync(job, ExecutionMode.SYNC);
		final boolean cronJobFinished = pollForFinishedCronJob(job);

		// then
		assertThat(cronJobFinished).isTrue();
		server.verify();
	}

	private boolean pollForFinishedCronJob(final Y2YSyncJobModel job) throws InterruptedException
	{
		final Optional<CronJobModel> cronJobOpt = job.getCronJobs().stream().findFirst();

		if (cronJobOpt.isPresent())
		{
			final CronJobModel cronJob = cronJobOpt.get();
			final LocalDateTime start = LocalDateTime.now();
			do
			{
				modelService.refresh(cronJob);
				if (cronJobService.isFinished(cronJob))
				{
					return true;
				}

				Thread.sleep(1000);
			}
			while (LocalDateTime.now().isBefore(start.plusSeconds(20)));
		}

		return false;
	}

	@Test
	public void shouldRunSyncProcessToZip() throws Exception
	{
		// given
		final Y2YSyncJobModel job = syncExecutionService.createSyncJobForZip("zipTestSyncJob", zipSyncContainer);
		modelService.save(job);


		// when
		syncExecutionService.startSync(job, ExecutionMode.SYNC);

		// then
		final Optional<CronJobModel> cronJobOpt = job.getCronJobs().stream().findFirst();
		if (!cronJobOpt.isPresent())
		{
			fail("The job " + job + " has not CronJob attached");
		}
		final Y2YSyncCronJobModel cronJob = (Y2YSyncCronJobModel) cronJobOpt.get();

		final CatalogUnawareMediaModel foundMediaData = getImportMedia(cronJob, Y2YSyncCronJobModel::getImpexZip);
		final CatalogUnawareMediaModel foundMediaBinaries = getImportMedia(cronJob, Y2YSyncCronJobModel::getMediasZip);

		assertThat(foundMediaData).isNotNull();
		assertThat(foundMediaData.getCode()).isEqualTo("data-" + cronJob.getCode());

		assertThat(foundMediaBinaries).isNotNull();
		assertThat(foundMediaBinaries.getCode()).isEqualTo("media-" + cronJob.getCode());
	}

	private MockRestServiceServer getMockRestServer()
	{
		return MockRestServiceServer.createServer(restTemplate);
	}

	private List<CatalogVersionModel> getTestCatalogVersions()
	{
		return Lists.newArrayList( //
				catalogVersionService.getCatalogVersion("CatalogA", "CatalogVersionA1"), //
				catalogVersionService.getCatalogVersion("CatalogA", "CatalogVersionA2"), //
				catalogVersionService.getCatalogVersion("CatalogA", "CatalogVersionA3"), //
				catalogVersionService.getCatalogVersion("CatalogB", "CatalogVersionB1"));
	}

	private CatalogUnawareMediaModel getImportMedia(final Y2YSyncCronJobModel cronJob,
			final Function<Y2YSyncCronJobModel, CatalogUnawareMediaModel> cronJobCallback) throws InterruptedException
	{

		final LocalDateTime start = LocalDateTime.now();
		do
		{
			modelService.refresh(cronJob);
			if (cronJobCallback.apply(cronJob) != null)
			{
				return cronJobCallback.apply(cronJob);
			}

			Thread.sleep(1000);
		}
		while (LocalDateTime.now().isBefore(start.plusSeconds(20)));

		return null;
	}

	private void importCsv(final String csvFile, final String encoding) throws ImpExException
	{
		LOG.info("importing resource " + csvFile);
		final InputStream inputstream = HybrisJUnit4Test.class.getResourceAsStream(csvFile);

		final ImportConfig config = new ImportConfig();
		config.setScript(new StreamBasedImpExResource(inputstream, encoding));
		final ImportResult importResult = importService.importData(config);


		if (importResult.hasUnresolvedLines())
		{
			fail("Import has unresolved lines:\n" + importResult.getUnresolvedLines());
		}
		assertThat(importResult.isError()).isFalse();
	}

}

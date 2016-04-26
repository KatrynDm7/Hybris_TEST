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
 *
 *
 */
package de.hybris.platform.c4ccustomer.job;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.deltadetection.jalo.DeltadetectionManager;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.EncodingsDataCreator;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.model.cronjob.ImpExExportJobModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.y2ysync.jalo.Y2ysyncManager;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@ManualTest
//@IntegrationTest
public class C4CSyncJobPerformableTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(C4CSyncJobPerformableTest.class);

	public static final String DATAHUB_TEST_URL = "/datahub-webapp/v1/data-feeds/y2ysync";

	@Resource
	private RestTemplate restTemplate;
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private MediaService mediaService;
	@Resource
	private CronJobService cronJobService;

	private final PropertyConfigSwitcher dataHubUrlProperty = new PropertyConfigSwitcher("y2ysync.datahub.url");

	@Before
	public void before() throws Exception
	{
		new EncodingsDataCreator().populateDatabase();
		ServicelayerTest.createCoreData();
		DeltadetectionManager.getInstance().createEssentialData(Collections.emptyMap(), null);
		Y2ysyncManager.getInstance().createEssentialData(Collections.emptyMap(), null);
		importCsv("/impex/c4cdata-streams.impex", "UTF-8");
		final ImpExExportJobModel exportJob = modelService.create(ImpExExportJobModel.class);
		exportJob.setCode(ImpExConstants.CronJob.DEFAULT_EXPORT_JOB_CODE);
		modelService.save(exportJob);
		dataHubUrlProperty.switchToValue(DATAHUB_TEST_URL);
	}

	@Test
	public void shouldSendToDataHub() throws InterruptedException, IOException
	{
		// GIVEN Some changes have been made in customer model
		final CustomerModel customer = new CustomerModel();
		customer.setUid(UUID.randomUUID().toString());
		customer.setName("John Doe");
		modelService.save(customer);
		// WHEN The changes are sent to datahub
		final Y2YSyncCronJobModel cronJob = getCronJob("c4cSyncToDataHubCustomersCronJob");

		final MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
		server.expect(requestTo(DATAHUB_TEST_URL))
				.andExpect(method(HttpMethod.POST))
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andRespond(clientHttpRequest ->
				{
					// THEN the media url should point on CSV
					LOG.info(clientHttpRequest.getBody());

					final String mediaId = getMediaIdFromResponse(clientHttpRequest);
					final String csv = getMediaContentById(mediaId);
					assertThat(csv, containsString(customer.getUid()));
					assertThat(csv, containsString(customer.getName()));

					return withSuccess().createResponse(clientHttpRequest);
				});
		cronJobService.performCronJob(cronJob, false);
		final boolean cronJobFinished = pollForFinishedCronJob(cronJob);
		// THEN the sync job has to communicate to datahub
		assertTrue("Failed to run cron job", cronJobFinished);
		server.verify();
	}


	private String getMediaContentById(String mediaId) throws UnsupportedEncodingException
	{
		final MediaModel media = mediaService.getMedia(mediaId);
		return new String(mediaService.getDataFromMedia(media), "UTF-8");
	}

	private static String getMediaIdFromResponse(ClientHttpRequest clientHttpRequest) throws IOException
	{
        /* Example of request body:
        {
          "syncExecutionId": "dc49aa81-0eec-49ca-8c51-7438dcabe0df",
          "dataStreams" : [
            {
              "itemType": "Customer",
              "columns": "uid;customerId;firstName",
              "urls": [
                "http://localhost:9001/medias/Customer-5ffdf3cb-0272-4988-9b2a-ffe6efd1d7d5?context=anVuaXR8cm9vdHw5MHx0ZXh0L3BsYWlufGh\
                hYy9oOTUvODc5NjA5MzMxNzE1MC50eHR8MGRkMzJjMmZmNmU1MDk5Y2I5YzcxZDZjYjdlMmJlYjQ4MjllMzcxYTRhZDk3ZGI1YTcwMTkxNGU2MWZhZTI3OQ"
              ]
            }
          ]
        }
        */
		final Pattern pattern = Pattern.compile(".*medias/([^?]*)\\?.*");
		Matcher matcher = pattern.matcher(clientHttpRequest.getBody().toString());
		assertTrue("Media id was not detected", matcher.matches());
		return matcher.group(1);
	}

	private Y2YSyncCronJobModel getCronJob(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Y2YSyncCronJob} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);
		return flexibleSearchService.searchUnique(fQuery);
	}

	private boolean pollForFinishedCronJob(final CronJobModel cronJob) throws InterruptedException
	{
		final DateTime start = new DateTime().plusSeconds(10);
		while (new DateTime().isBefore(start))
		{
			modelService.refresh(cronJob);
			if (cronJobService.isFinished(cronJob))
			{
				return true;
			}
			Thread.sleep(1000);
		}

		LOG.warn("Job " + cronJob.getCode() + " was not finished");
		return false;
	}

}

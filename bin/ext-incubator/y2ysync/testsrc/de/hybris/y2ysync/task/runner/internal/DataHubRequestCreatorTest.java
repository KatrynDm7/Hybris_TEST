package de.hybris.y2ysync.task.runner.internal;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DataHubRequestCreatorTest
{
	private final static String SYNC_EXECUTION_ID = "testExecutionId";

	private DataHubRequestCreator requestCreator;
	private final RestTemplate restTemplate = getRestTemplate();

	@Mock
	private Y2YSyncDAO dao;
	@Mock
	private SyncImpExMediaModel m1, m2, m3, m4, m5;
	@Mock
	private ComposedTypeModel productType, titleType;


	@Before
	public void setUp() throws Exception
	{
		requestCreator = new DataHubRequestCreator();
		requestCreator.setY2YSyncDAO(dao);
		requestCreator.setRestTemplate(restTemplate);

		given(productType.getCode()).willReturn("Product");
		given(titleType.getCode()).willReturn("Title");

		given(m1.getImpexHeader()).willReturn("INSERT_UPDATE Product;code[unique=true];description");
		given(m1.getDataHubColumns()).willReturn("code;description");
		given(m1.getSyncType()).willReturn(productType);
		given(m1.getURL()).willReturn("/medias/m1");

		given(m2.getImpexHeader()).willReturn("INSERT_UPDATE Product;code[unique=true];description");
		given(m2.getDataHubColumns()).willReturn("code;description");
		given(m2.getSyncType()).willReturn(productType);
		given(m2.getURL()).willReturn("/medias/m2");

		given(m3.getImpexHeader()).willReturn("DELETE Product;code[unique=true]");
		given(m3.getDataHubColumns()).willReturn("code");
		given(m3.getSyncType()).willReturn(productType);
		given(m3.getURL()).willReturn("/medias/m3");

		given(m4.getImpexHeader()).willReturn("INSERT_UPDATE Title;code[unique=true];");
		given(m4.getDataHubColumns()).willReturn("code");
		given(m4.getSyncType()).willReturn(titleType);
		given(m4.getURL()).willReturn("/medias/m4");

		given(m5.getImpexHeader()).willReturn("INSERT_UPDATE Title;code[unique=true];");
		given(m5.getDataHubColumns()).willReturn("code");
		given(m5.getSyncType()).willReturn(titleType);
		given(m5.getURL()).willReturn("/medias/m5");

		given(dao.findSyncMediasBySyncCronJob(SYNC_EXECUTION_ID)).willReturn(Lists.newArrayList(m1, m2, m3, m4, m5));
	}

	@Test
	public void shouldSuccessfullySendSyncMediaUrlsGrouppedByImpExHeaderAsJSON() throws Exception
	{
		// given
		final String expectedJson = getExpectedJson();
		final MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
		server.expect(requestTo("/datahub-webapp/v1/data-feeds/y2ysync")) //
				.andExpect(method(HttpMethod.POST)) //
				.andExpect(content().contentType("application/json;charset=UTF-8")) //
				.andExpect(content().string(expectedJson)) //
				.andRespond(withSuccess()); //

		// when
		requestCreator.sendRequest(SYNC_EXECUTION_ID, "/datahub-webapp/v1/data-feeds/y2ysync");

		// then
		server.verify();
	}

	@Test
	public void shouldSuccessfullySendSyncMediaUrlsWithHomeURLGrouppedByImpExHeaderAsJSON() throws Exception
	{
		// given
		final String homeUrl = "http://server.com";
		requestCreator.setHomeUrl(homeUrl);
		final String expectedJson = getExpectedJsonWithHomeURL();
		final MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
		server.expect(requestTo("/datahub-webapp/v1/data-feeds/y2ysync")) //
				.andExpect(method(HttpMethod.POST)) //
				.andExpect(content().contentType("application/json;charset=UTF-8")) //
				.andExpect(content().string(expectedJson)) //
				.andRespond(withSuccess()); //

		// when
		requestCreator.sendRequest(SYNC_EXECUTION_ID, "/datahub-webapp/v1/data-feeds/y2ysync");

		// then
		server.verify();
	}

	@Test
	public void shouldThrowAnExceptionWhenRemoteDataHubControllerRespondsWithNot200OK() throws Exception
	{
		// given
		final String homeUrl = "http://server.com";
		requestCreator.setHomeUrl(homeUrl);

		try
		{
			// when
			final MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
			server.expect(requestTo("/datahub-webapp/v1/data-feeds/y2ysync")) //
					.andRespond(withBadRequest()); //
			requestCreator.sendRequest(SYNC_EXECUTION_ID, "/datahub-webapp/v1/data-feeds/y2ysync");
			fail("Should throw IllegalStateException");
		}
		catch (final HttpStatusCodeException e)
		{
			// fine
		}

	}

	private RestTemplate getRestTemplate()
	{
		final RestTemplate template = new RestTemplate();
		template.setMessageConverters(Lists.newArrayList(new MappingJackson2HttpMessageConverter()));

		return template;
	}

	private String getExpectedJson()
	{
		return "{\"syncExecutionId\":\""
				+ SYNC_EXECUTION_ID
				+ "\",\"dataStreams\":[{\"itemType\":\"Product\",\"columns\":\"code;description\",\"delete\":false,\"urls\":[\"/medias/m1\",\"/medias/m2\"]},"
				+ "{\"itemType\":\"Product\",\"columns\":\"code\",\"delete\":false,\"urls\":[\"/medias/m3\"]},{\"itemType\":\"Title\",\"columns\":\"code\",\"delete\":false,\"urls\":[\"/medias/m4\",\"/medias/m5\"]}]}";
	}

	private String getExpectedJsonWithHomeURL()
	{
		return "{\"syncExecutionId\":\""
				+ SYNC_EXECUTION_ID
				+ "\",\"dataStreams\":[{\"itemType\":\"Product\",\"columns\":\"code;"
				+ "description\",\"delete\":false,\"urls\":[\"http://server.com/medias/m1\",\"http://server.com/medias/m2\"]},"
				+ "{\"itemType\":\"Product\",\"columns\":\"code\",\"delete\":false,\"urls\":[\"http://server.com/medias/m3\"]},"
				+ "{\"itemType\":\"Title\",\"columns\":\"code\",\"delete\":false,\"urls\":[\"http://server.com/medias/m4\",\"http://server.com/medias/m5\"]}]}";
	}
}

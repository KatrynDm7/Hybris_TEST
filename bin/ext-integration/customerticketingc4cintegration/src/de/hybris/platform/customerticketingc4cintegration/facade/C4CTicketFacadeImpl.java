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
package de.hybris.platform.customerticketingc4cintegration.facade;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingc4cintegration.SitePropsHolder;
import de.hybris.platform.customerticketingc4cintegration.constants.Customerticketingc4cintegrationConstants;
import de.hybris.platform.customerticketingc4cintegration.data.Note;
import de.hybris.platform.customerticketingc4cintegration.data.ODataListResponseData;
import de.hybris.platform.customerticketingc4cintegration.data.ODataSingleResponseData;
import de.hybris.platform.customerticketingc4cintegration.data.ServiceRequestData;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


public class C4CTicketFacadeImpl implements TicketFacade
{
	private final static Logger LOGGER = Logger.getLogger(C4CTicketFacadeImpl.class);

	private ObjectMapper jacksonObjectMapper;

	private Converter<ServiceRequestData, TicketData> ticketConverter;
	private Converter<TicketData, ServiceRequestData> defaultC4CTicketConverter;
	private Converter<TicketData, Note> updateMessageConverter;

	private RestTemplate restTemplate;
	@Resource
	private SitePropsHolder sitePropsHolder;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "completed")
	private StatusData completedStatus;

	@Override
	public TicketData createTicket(final TicketData ticket)
	{
		Assert.isTrue(StringUtils.isNotBlank(ticket.getSubject()), "Subject can't be empty");
		Assert.isTrue(ticket.getSubject().length() <= 255, "Subject can't be longer than 255 chars");
		Assert.isTrue(StringUtils.isNotBlank(ticket.getMessage()), "Message can't be empty");

		LOGGER.info("Sending request to: " + Customerticketingc4cintegrationConstants.URL + Customerticketingc4cintegrationConstants.TICKETING_SUFFIX);

		try
		{
			//setting customerId explicitly and override the customerUid set from the addon
			ticket.setCustomerId(customerFacade.getCurrentCustomer().getCustomerId());

			final HttpHeaders headers = getEnrichedHeaders();
			final HttpEntity<String> entity = new HttpEntity<>(jacksonObjectMapper.writeValueAsString(defaultC4CTicketConverter
					.convert(ticket)), headers);

			final ResponseEntity<String> result =
					restTemplate.postForEntity(Customerticketingc4cintegrationConstants.URL
											 + Customerticketingc4cintegrationConstants.TICKETING_SUFFIX, entity, String.class);

			LOGGER.info("Response status: " + result.getStatusCode());
			LOGGER.info("Response headers: " + result.getHeaders());
			LOGGER.info("Response body: " + result.getBody());

			final ODataSingleResponseData responseData = jacksonObjectMapper.readValue(result.getBody(),
					ODataSingleResponseData.class);

			return ticketConverter.convert(responseData.getD().getResults());
		}
		catch (final IOException e)
		{
			LOGGER.warn("Can't convert ticketData: " + e);
		}
		catch (final RestClientException e)
		{
			LOGGER.warn("Can't send request " + e);
		}

		return null; // or throw
	}

	@Override
	public TicketData updateTicket(final TicketData ticket)
	{
		Assert.isTrue(StringUtils.isNotBlank(ticket.getMessage()), "Message can't be empty");

		try
		{
			//setting customerId explicitly and override the customerUid set from the addon
			ticket.setCustomerId(customerFacade.getCurrentCustomer().getCustomerId());

			final HttpHeaders updateTicketHeaders = getEnrichedHeaders();

			updateTicketHeaders.set(HttpHeaders.CONTENT_TYPE, Customerticketingc4cintegrationConstants.MULTIPART_MIXED_MODE);

			final HttpHeaders statusUpdateHeaders = addBatchHeaders("PATCH ServiceTicketCollection('" + ticket.getId()
					+ "') HTTP/1.1");

			final HttpHeaders messageUpdateHeaders = addBatchHeaders("POST ServiceTicketCollection('" + ticket.getId()
					+ "')/Notes HTTP/1.1");


			final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();

			LOGGER.info(defaultC4CTicketConverter.convert(ticket));

			final HttpEntity<String> statusEntity = new HttpEntity<>(
					jacksonObjectMapper.writeValueAsString(defaultC4CTicketConverter.convert(ticket)), statusUpdateHeaders);


			LOGGER.info(updateMessageConverter.convert(ticket));

			final HttpEntity<String> messageEntity = new HttpEntity<>(jacksonObjectMapper.writeValueAsString(updateMessageConverter
					.convert(ticket)), messageUpdateHeaders);

			if (completedStatus.getId().equalsIgnoreCase(ticket.getStatus().getId()))
			{
				if (getTicket(ticket.getId()).getStatus().getId().equals(ticket.getStatus().getId())) // so status doesn't changed
				{
					throw new IllegalArgumentException("You can not add a message to a completed ticket. Please, reopen the ticket");
				}

				parts.add("message", messageEntity);
				parts.add("status", statusEntity);
			}
			else
			{
				parts.add("status", statusEntity);
				parts.add("message", messageEntity);
			}


			final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(parts, updateTicketHeaders);

			final URI uri = UriComponentsBuilder
					.fromHttpUrl(Customerticketingc4cintegrationConstants.URL + Customerticketingc4cintegrationConstants.BATCH_SUFFIX)
					.build().encode()
					.toUri();


			LOGGER.info("Result uri for status update: " + uri);


			final ResponseEntity<MultiValueMap> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
					MultiValueMap.class);


			LOGGER.info("Response status: " + result.getStatusCode());
			LOGGER.info("Response headers: " + result.getHeaders());
			LOGGER.info("Response body: " + result.getBody());

			if (result.getBody().containsKey(Customerticketingc4cintegrationConstants.MULTIPART_HAS_ERROR))
			{
				LOGGER.error("Error happend!");
				if (null != result.getBody().get(Customerticketingc4cintegrationConstants.MULTIPART_ERROR_MESSAGE))
				{
					LOGGER.error(result.getBody().get(Customerticketingc4cintegrationConstants.MULTIPART_ERROR_MESSAGE));
				}

				return null;
			}

			return getTicket(ticket.getId());

		}
		catch (final IOException e)
		{
			LOGGER.warn("Can't convert ticketData: " + e);
		}
		catch (final RestClientException e)
		{
			LOGGER.warn("Can't send request " + e);
		}

		return null; // or throw ?
	}

	protected HttpHeaders addBatchHeaders(final String uri)
	{
		final HttpHeaders headers = new HttpHeaders();

		headers.set("", uri);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(Customerticketingc4cintegrationConstants.CONTENT_ID,
				Customerticketingc4cintegrationConstants.CONTENT_ID_VALUE_PREFIX + RandomUtils.nextInt(1000));

		return headers;
	}

	@Override
	public TicketData getTicket(final String ticketId)
	{
		validateParameterNotNullStandardMessage("ticketId", ticketId);

		final UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(Customerticketingc4cintegrationConstants.URL + Customerticketingc4cintegrationConstants.TICKETING_SUFFIX)
				.queryParam(
						Customerticketingc4cintegrationConstants.FILETR_SUFFIX,
						(sitePropsHolder.isB2C() ? String.format("ExternalCustomerID eq '%s'", customerFacade.getCurrentCustomer()
								.getCustomerId()) : String.format("ExternalContactID eq '%s'", customerFacade.getCurrentCustomer()
								.getCustomerId()))
								+ String.format("and ObjectID eq '%s'", ticketId))
				.query(Customerticketingc4cintegrationConstants.EXPAND_SUFFIX);

		LOGGER.info("Result uri: " + builder.build().encode().toUri());

		try
		{
			final HttpHeaders headers = getEnrichedHeaders();
			final HttpEntity<String> entity = new HttpEntity<>(headers);

			final ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
					String.class);

			LOGGER.info("Response status: " + result.getStatusCode());
			LOGGER.info("Response headers: " + result.getHeaders());
			LOGGER.info("Response body: " + result.getBody());

			final List<TicketData> dataList = jacksonObjectMapper.readValue(result.getBody(), ODataListResponseData.class).getD()
					.getResults().stream().map(ticketConverter::convert).collect(Collectors.toList());

			LOGGER.info(dataList);

			return dataList.isEmpty() ? null : dataList.get(0);
		}
		catch (final IOException e)
		{
			LOGGER.warn("Can't convert ticketData: " + e);
		}
		catch (final RestClientException e)
		{
			LOGGER.warn("Can't send request " + e);
		}

		return null; // or throw
	}

	@Override
	public SearchPageData<TicketData> getTickets(final PageableData o)
	{

		final UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(Customerticketingc4cintegrationConstants.URL + Customerticketingc4cintegrationConstants.TICKETING_SUFFIX)
				.queryParam(
						Customerticketingc4cintegrationConstants.FILETR_SUFFIX,
						sitePropsHolder.isB2C() ? String.format("ExternalCustomerID eq '%s'", customerFacade.getCurrentCustomer()
								.getCustomerId()) : String.format("ExternalContactID eq '%s'", customerFacade.getCurrentCustomer()
								.getCustomerId())).query(Customerticketingc4cintegrationConstants.ORDER_BY_SUFFIX)
				.query(Customerticketingc4cintegrationConstants.EXPAND_SUFFIX);
		LOGGER.info("Result uri: " + builder.build().encode().toUri());
		try
		{
			final HttpHeaders headers = getEnrichedHeaders();
			final HttpEntity<String> entity = new HttpEntity<>(headers);

			final ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
					String.class);

			LOGGER.info("Response status: " + result.getStatusCode());
			LOGGER.info("Response headers: " + result.getHeaders());
			LOGGER.info("Response body: " + result.getBody());

			final List<TicketData> dataList = jacksonObjectMapper.readValue(result.getBody(), ODataListResponseData.class).getD()
					.getResults().stream().map(ticketConverter::convert).collect(Collectors.toList());

			final SearchPageData<TicketData> results = new SearchPageData<>();
			results.setResults(dataList);
			return results;
		}
		catch (final IOException e)
		{
			LOGGER.warn("Can't convert ticketData: " + e);
		}
		catch (final RestClientException e)
		{
			LOGGER.warn("Can't send request " + e);
		}
		return new SearchPageData<>(); // or throw?
	}

	public HttpHeaders enrichHeaders(final HttpHeaders headers, final String siteId)
	{
		final String url = Customerticketingc4cintegrationConstants.URL
						 + Customerticketingc4cintegrationConstants.TICKETING_SUFFIX
						 + Customerticketingc4cintegrationConstants.TOKEN_URL_SUFFIX;
		LOGGER.info(url);

		final HttpHeaders tempHeaders = getDefaultHeaders(siteId);
		final HttpEntity<String> entity = new HttpEntity<>(tempHeaders);

		final ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class); // try - catch !

		LOGGER.info(result.getHeaders());

		if (null != result.getHeaders().get(Customerticketingc4cintegrationConstants.RESPONSE_COOKIE_NAME))
		{
			headers.put(HttpHeaders.COOKIE, result.getHeaders().get(Customerticketingc4cintegrationConstants.RESPONSE_COOKIE_NAME));
		}

		if (result.getHeaders().containsKey(Customerticketingc4cintegrationConstants.TOKEN_NAMING))
		{
			final List<String> l = result.getHeaders().get(Customerticketingc4cintegrationConstants.TOKEN_NAMING);
			headers.put(Customerticketingc4cintegrationConstants.TOKEN_NAMING, Arrays.asList(l.get(0)));
		}

		return headers;
	}

	protected HttpHeaders getEnrichedHeaders()
	{
		final String siteId = sitePropsHolder.getSiteId();
		LOGGER.info("SiteId: " + siteId);
		final HttpHeaders headers = getDefaultHeaders(siteId);
		return enrichHeaders(headers, siteId);
	}

	protected String createBasicAuthHeader(final String username, final String password)
	{
		final String auth = username + ":" + password;
		final byte[] encodedAuth = Base64Utils.encode(auth.getBytes());
		final String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}

	protected HttpHeaders getDefaultHeaders(final String siteId)
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.put(HttpHeaders.ACCEPT, Arrays.asList(Customerticketingc4cintegrationConstants.ACCEPT));

		headers.set(
				HttpHeaders.AUTHORIZATION,
				createBasicAuthHeader(Customerticketingc4cintegrationConstants.USERNAME,
						Customerticketingc4cintegrationConstants.PASSWORD));

		headers.put(Customerticketingc4cintegrationConstants.TOKEN_NAMING,
				Arrays.asList(Customerticketingc4cintegrationConstants.TOKEN_EMPTY));
		headers.put(Customerticketingc4cintegrationConstants.SITE_HEADER, Arrays.asList(siteId));

		return headers;
	}

	public RestTemplate getRestTemplate()
	{
		return restTemplate;
	}

	public void setRestTemplate(final RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	public Converter<ServiceRequestData, TicketData> getTicketConverter()
	{
		return ticketConverter;
	}

	public void setTicketConverter(final Converter<ServiceRequestData, TicketData> ticketConverter)
	{
		this.ticketConverter = ticketConverter;
	}

	public ObjectMapper getJacksonObjectMapper()
	{
		return jacksonObjectMapper;
	}

	public void setJacksonObjectMapper(final ObjectMapper jacksonObjectMapper)
	{
		this.jacksonObjectMapper = jacksonObjectMapper;
	}

	public Converter<TicketData, ServiceRequestData> getDefaultC4CTicketConverter()
	{
		return defaultC4CTicketConverter;
	}

	public void setDefaultC4CTicketConverter(final Converter<TicketData, ServiceRequestData> defaultC4CTicketConverter)
	{
		this.defaultC4CTicketConverter = defaultC4CTicketConverter;
	}

	public Converter<TicketData, Note> getUpdateMessageConverter()
	{
		return updateMessageConverter;
	}

	public void setUpdateMessageConverter(final Converter<TicketData, Note> updateMessageConverter)
	{
		this.updateMessageConverter = updateMessageConverter;
	}
}

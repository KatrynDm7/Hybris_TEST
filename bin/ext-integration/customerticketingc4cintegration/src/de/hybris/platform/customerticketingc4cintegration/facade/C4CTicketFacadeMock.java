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

import com.fasterxml.jackson.databind.ObjectMapper;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.customerticketingc4cintegration.data.ODataListResponseData;
import de.hybris.platform.customerticketingc4cintegration.data.ODataSingleResponseData;
import de.hybris.platform.customerticketingc4cintegration.data.ServiceRequestData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

public class C4CTicketFacadeMock implements TicketFacade
{
	private final static Logger LOGGER = Logger.getLogger(C4CTicketFacadeMock.class);

	private Resource createResource; // return created ticket
	private Resource updateResource; // return note
	private Resource listTicketsResource; // list of tickets
	private Resource getResource; // get Ticket

	private ObjectMapper jacksonObjectMapper;

	private Converter<ServiceRequestData, TicketData> ticketConverter;
	private Converter<TicketData, ServiceRequestData> defaultC4CTicketConverter;

	@Override
	public TicketData createTicket(final TicketData o)
	{
		Assert.isTrue(StringUtils.isNotBlank(o.getSubject()), "Subject can't be empty");
		Assert.isTrue(o.getSubject().length() <= 255, "Subject can't be longer than 255 chars");
		Assert.isTrue(StringUtils.isNotBlank(o.getMessage()), "Message can't be empty");

		final String json = loadResource(createResource);
		TicketData data = null;
		try
		{
			data = ticketConverter.convert(jacksonObjectMapper.readValue(json, ODataSingleResponseData.class).getD().getResults());
			LOGGER.info(data);
		}
		catch (final IOException e)
		{
			LOGGER.warn(e);
		}
		return data;
	}

	@Override
	public TicketData updateTicket(final TicketData o)
	{
		Assert.isTrue(StringUtils.isNotBlank(o.getMessage()), "Message can't be empty");

		final String json = loadResource(getResource); // update resource contains Note, not Ticket
		TicketData data = null;
		try
		{
			data = jacksonObjectMapper.readValue(json, ODataListResponseData.class).getD().getResults().stream()
					.map(ticketConverter::convert).collect(Collectors.toList()).get(0);
			LOGGER.info(data);
		}
		catch (final IOException e)
		{
			LOGGER.warn(e);
		}
		return data;
	}

	@Override
	public TicketData getTicket(final String ticketId)
	{
		final String json = loadResource(getResource);
		List<TicketData> data = null;
		try
		{
			data = jacksonObjectMapper.readValue(json, ODataListResponseData.class).getD().getResults().stream()
                    .map(ticketConverter::convert).collect(Collectors.toList());
			LOGGER.info(data);
		}
		catch (final IOException e)
		{
			LOGGER.warn(e);
		}
		return data.isEmpty() ? null : data.get(0);
	}

	@Override
	public SearchPageData<TicketData> getTickets(final PageableData o)
	{
		final String json = loadResource(listTicketsResource);
		final List<TicketData> data = new ArrayList<>();
		try
		{
			data.addAll(jacksonObjectMapper.readValue(json, ODataListResponseData.class).getD().getResults().stream()
					.map(ticketConverter::convert).collect(Collectors.toList()));
			LOGGER.info(data);
		}
		catch (final IOException e)
		{
			LOGGER.warn(e);
		}

		final SearchPageData<TicketData> results = new SearchPageData<>();
		results.setResults(data);

		return results;
	}

	protected String loadResource(final Resource r)
	{
		String json = "";
		try (InputStream is = r.getInputStream())
		{
			json = IOUtils.toString(is, Charset.forName("utf-8")); // IOUtils don't close stream
		}
		catch (final IOException e)
		{
			LOGGER.warn(e);
		}
		return json;
	}

	public Resource getCreateResource()
	{
		return createResource;
	}

	public void setCreateResource(final Resource createResource)
	{
		this.createResource = createResource;
	}

	public Resource getUpdateResource()
	{
		return updateResource;
	}

	public void setUpdateResource(final Resource updateResource)
	{
		this.updateResource = updateResource;
	}

	public Resource getListTicketsResource()
	{
		return listTicketsResource;
	}

	public void setListTicketsResource(final Resource listTicketsResource)
	{
		this.listTicketsResource = listTicketsResource;
	}

	public Resource getGetResource()
	{
		return getResource;
	}

	public void setGetResource(final Resource getResource)
	{
		this.getResource = getResource;
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
}

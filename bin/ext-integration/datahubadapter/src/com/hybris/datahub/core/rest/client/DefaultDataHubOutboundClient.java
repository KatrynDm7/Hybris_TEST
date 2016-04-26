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

package com.hybris.datahub.core.rest.client;

import com.hybris.datahub.core.dto.ResultData;
import com.hybris.datahub.core.rest.DataHubCommunicationException;
import com.hybris.datahub.core.rest.DataHubOutboundException;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


/**
 * Implementation of a REST client for communication with the Data Hub
 */
public class DefaultDataHubOutboundClient implements DataHubOutboundClient
{
	private static final ObjectMapper mapper = new ObjectMapper();

	private final Client client;
	private String dataHubUrl;

	/**
	 * Instantiates this client.
	 */
	public DefaultDataHubOutboundClient()
	{
		this(Client.create());
	}

	protected DefaultDataHubOutboundClient(final Client cl)
	{
		client = cl;
	}

	@Override
	public ResultData exportData(final String[] csvContent, final String dataFeed, final String rawItemType)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		final WebResource exportRequest = createRequest("/data-feeds/" + dataFeed + "/items/" + rawItemType);
		final ClientResponse response = exportRequest.type(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class,
				new ByteArrayInputStream(StringUtils.join(csvContent, System.lineSeparator()).getBytes()));

		return ResponseHander.handle(response, OutboundOperationType.EXPORT);
	}

	@Override
	public ResultData deleteItem(final String poolName, final String canonicalItemType, final Map<String, String> keyFields)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		final ClientResponse deleteResponse = sendDeleteFromPoolRequest(poolName, canonicalItemType, keyFields);

		return ResponseHander.handle(deleteResponse, OutboundOperationType.DELETE);
	}


	@Override
	public ResultData deleteByFeed(final String feedName, final String rawItemType) throws DataHubOutboundException,
			DataHubCommunicationException
	{
		return deleteByFeed(feedName, rawItemType, null);
	}

	@Override
	public ResultData deleteByFeed(final String feedName, final String rawItemType, final Map<String, Object> keyFields)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		final ClientResponse deleteResponse = sendDeleteFromFeedRequest(feedName, rawItemType, keyFields);

		return ResponseHander.handle(deleteResponse, OutboundOperationType.DELETE);
	}

	private ClientResponse sendDeleteFromPoolRequest(final String poolName, final String canonicalItemType,
			final Map<String, String> keyFields) throws DataHubCommunicationException
	{
		try
		{
			return poolRequest(poolName, canonicalItemType, keyFields).delete(ClientResponse.class);
		}
		catch (final Exception ex)
		{
			throw new DataHubCommunicationException("Unable to communicate with the Data Hub server", ex);
		}
	}

	private WebResource poolRequest(final String pool, final String type, final Map<String, String> keys)
	{
		final WebResource res = createRequest("/pools/" + pool + "/items/" + type);
		return addParam(res, "keyFields", keys);
	}

	private ClientResponse sendDeleteFromFeedRequest(final String feedName, final String rawType,
			final Map<String, Object> keyFields) throws DataHubCommunicationException
	{
		try
		{
			return feedRequest(feedName, rawType, keyFields).delete(ClientResponse.class);
		}
		catch (final Exception ex)
		{
			throw new DataHubCommunicationException("Unable to communicate with the Data Hub server", ex);
		}
	}

	private WebResource feedRequest(final String feedName, final String rawType, final Map<String, Object> attributes)
	{
		final WebResource req = createRequest("/data-feeds/" + feedName + "/types/" + rawType);
		return addParam(req, "rawFields", attributes);
	}

	private WebResource createRequest(final String url)
	{
		return client.resource(dataHubUrl + url);
	}

	private WebResource addParam(final WebResource res, final String paramName, final Map<String, ?> attributes)
	{
		if (MapUtils.isNotEmpty(attributes))
		{
			try
			{
				final String json = mapper.writeValueAsString(attributes);
				return res.queryParam(paramName, URLEncoder.encode(json, "UTF-8"));
			}
			catch (final Exception e)
			{
				throw new IllegalArgumentException("Could not convert key fields map into a JSON query string", e);
			}
		}
		return res;
	}

	/**
	 * Specifies DataHub location.
	 *
	 * @param dataHubUrl URL for the DataHub server to exchange data with.
	 */
	@Required
	public void setDataHubUrl(final String dataHubUrl)
	{
		this.dataHubUrl = dataHubUrl;
	}

	private enum OutboundOperationType
	{
		EXPORT("Export", "exporting"), DELETE("Delete", "deleting");

		private final String name;
		private final String presentTense;

		private OutboundOperationType(final String enumName, final String presentTense)
		{
			this.name = enumName;
			this.presentTense = presentTense;
		}
	}

	private static class ResponseHander
	{
		private static final int OK = 200;
		private static final int BAD_REQUEST = 400;
		private static final int NOT_FOUND = 404;
		private static final int SERVER_ERROR = 500;

		private static ResultData handle(final ClientResponse response, final OutboundOperationType operationType)
				throws DataHubOutboundException, DataHubCommunicationException
		{
			if (response.getStatus() == OK)
			{
				return response.getEntity(ResultData.class);
			}

			final String message = response.getEntity(String.class);
			switch (response.getStatus())
			{
				case BAD_REQUEST:
					try
					{
						throw new DataHubOutboundException(operationType.name + " operation was unsuccessful : " + message);
					}
					catch (ClientHandlerException | UniformInterfaceException e)
					{
						throw new DataHubOutboundException(operationType.name + " operation was unsuccessful");
					}
				case NOT_FOUND:
				case SERVER_ERROR:
					throw new DataHubCommunicationException("Communication failure with the Data Hub server: " + message);
				default:
					throw new IllegalStateException("Exception occurred while " + operationType.presentTense
							+ " item from the Data Hub. The status received was " + response.getStatus() + "; the message was '"
							+ message + "'");
			}
		}
	}
}

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

import com.hybris.datahub.core.facades.ItemImportResult;
import com.hybris.datahub.core.services.impl.DataHubFacade;

import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


/**
 * A REST client to communicate to the Data Hub. Brings data from the Data Hub into a Core system and responds to Data
 * Hub with the results of the data import
 */
public class ImpexDataImportClient implements DataHubFacade
{
	private static final Logger log = LoggerFactory.getLogger(ImpexDataImportClient.class);

	@Override
	public InputStream readData(final String url, final Map<String, ?> headers)
	{
		Preconditions.checkArgument(url != null, "url must be provided");

		log.info("Requesting data from resource " + url);
		final WebResource.Builder exportRequest = createRequest(url, headers);

		try
		{
			final ClientResponse response = exportRequest.get(ClientResponse.class);
			log.debug("Response status from " + url + ": " + response.getStatus());
			if (response.getClientResponseStatus() == ClientResponse.Status.OK)
			{
				return response.getEntity(InputStream.class);
			}
			throw new IllegalStateException(response.getClientResponseStatus() + " response from " + url);
		}
		catch (final Exception e)
		{
			throw new IllegalStateException("Failed to communicate to " + url, e);
		}
	}

	@Override
	public void returnImportResult(final String url, final ItemImportResult itemImportResult)
	{
		Preconditions.checkArgument(url != null, "url must be provided");

		log.info("Returning {} to {}", itemImportResult, url);
		final WebResource.Builder exportRequest = createRequest(url, null);

		try
		{
			final ClientResponse response = exportRequest.type("application/xml").put(ClientResponse.class, itemImportResult);
			log.info("Response status from " + url + ": " + response.getStatus());
			if (response.getClientResponseStatus() != ClientResponse.Status.OK)
			{
				throw new IllegalStateException(response.getClientResponseStatus() + " response from " + url);
			}
		}
		catch (final Exception e)
		{
			log.error("Failed to communicate to " + url, e);
			throw new IllegalStateException("Failed to communicate to " + url, e);
		}
	}

	private WebResource.Builder createRequest(final String url, final Map<String, ?> headers)
	{
		final Client client = Client.create();
		final WebResource exportResource = client.resource(url);
		final WebResource.Builder exportResourceBuilder = exportResource.getRequestBuilder();

		addHeaders(exportResourceBuilder, headers);
		return exportResourceBuilder;
	}

	private void addHeaders(final WebResource.Builder webResourceBuilder, final Map<String, ?> headers)
	{
		log.debug("Headers:");
		if (headers != null)
		{
			for (final Map.Entry<String, ?> entry : headers.entrySet())
			{
				webResourceBuilder.header(entry.getKey(), entry.getValue());
				log.debug(entry.getKey() + "=" + String.valueOf(entry.getValue()));
			}
		}
	}
}

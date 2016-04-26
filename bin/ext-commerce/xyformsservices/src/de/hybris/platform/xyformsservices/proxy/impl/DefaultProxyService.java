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
 */
package de.hybris.platform.xyformsservices.proxy.impl;

import de.hybris.platform.xyformsservices.proxy.ProxyEngine;
import de.hybris.platform.xyformsservices.proxy.ProxyException;
import de.hybris.platform.xyformsservices.proxy.ProxyService;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for proxy service.
 */
public class DefaultProxyService implements ProxyService
{
	private static final Logger LOG = Logger.getLogger(DefaultProxyService.class);

	protected static final String NEW_COMMAND = "/new";
	protected static final String EDIT_COMMAND = "/edit";
	protected static final String VIEW_COMMAND = "/view";
	protected static final String ORBEON_PREFIX = "/orbeon";

	protected String orbeonAddress;
	protected Map<String, String> extraHeaders;
	protected ProxyEngine proxyEngine;

	@Override
	public String rewriteURL(final String url, final boolean embeddable) throws MalformedURLException
	{
		LOG.debug("Got URL [" + url + "]");
		final URL u = new URL(url);

		final int index = u.getPath().indexOf(ORBEON_PREFIX);
		if (index < 0)
		{
			throw new MalformedURLException(ORBEON_PREFIX + " is not part of the URL");
		}

		// Take the URI part of the URL and remove the application context from it.
		final String path = u.getPath().substring(index);
		LOG.debug("Call Proxy Service with path [" + path + "]");

		// We are assuming that orbeon is:
		//    - Deployed as /orbeon
		//    - /orbeon is part of the request uri.
		//    - We don't need to go through an Apache Server

		URIBuilder builder = null;
		try
		{
			builder = new URIBuilder(this.orbeonAddress + path);
		}
		catch (final URISyntaxException e)
		{
			throw new MalformedURLException(e.getMessage());
		}

		// if the original URl had a QueryString...
		builder.setQuery(u.getQuery());

		// If the form should be embeddable
		if (embeddable)
		{
			builder.addParameter("orbeon-embeddable", "true");
		}

		final String newURL = builder.toString();

		LOG.debug("Rewritten URL [" + newURL + "]");
		return newURL;
	}

	@Override
	public String rewriteURL(final String applicationId, final String formId, final String formDataId, final boolean editable)
			throws MalformedURLException
	{
		String url = this.orbeonAddress + ORBEON_PREFIX + "/fr/" + applicationId + "/" + formId;

		if (formDataId == null || formDataId.isEmpty())
		{
			url = url + NEW_COMMAND;
		}
		else
		{
			if (editable)
			{
				url = url + EDIT_COMMAND + "/" + formDataId;
			}
			else
			{
				url = url + VIEW_COMMAND + "/" + formDataId;
			}
		}

		return rewriteURL(url, true);
	}

	@Override
	public String rewriteURL(final String applicationId, final String formId, final String formDataId)
			throws MalformedURLException
	{
		return rewriteURL(applicationId, formId, formDataId, false);
	}

	@Override
	public String getNextRandomNamespace()
	{
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.substring(uuid.lastIndexOf("-") + 1);
		return "uuid_" + uuid;
	}

	@Override
	public void proxy(final HttpServletRequest request, final HttpServletResponse response, final String namespace,
			final String url, final boolean forceGetMethod, final Map<String, String> headers) throws ProxyException
	{
		this.proxyEngine.proxy(request, response, namespace, url, forceGetMethod, headers);
	}

	@Override
	public String extractNamespace(final HttpServletRequest request)
	{
		return this.proxyEngine.extractNamespace(request);
	}

	@Required
	public void setOrbeonAddress(String orbeonAddress) throws MalformedURLException
	{
		if (orbeonAddress.charAt(orbeonAddress.length() - 1) == '/')
		{
			// we remove the trailing slash
			orbeonAddress = orbeonAddress.substring(0, orbeonAddress.length() - 1);
		}
		if (orbeonAddress.indexOf(ORBEON_PREFIX) < 0)
		{
			throw new MalformedURLException(ORBEON_PREFIX + " is not part of the given URL");
		}
		orbeonAddress = orbeonAddress.substring(0, orbeonAddress.indexOf(ORBEON_PREFIX));
		final URL u = new URL(orbeonAddress);
		this.orbeonAddress = u.toString();
	}

	@Override
	public Map<String, String> getExtraHeaders()
	{
		return extraHeaders;
	}

	@Required
	public void setExtraHeaders(final Map<String, String> extraHeaders)
	{
		this.extraHeaders = extraHeaders;
	}

	@Required
	public void setProxyEngine(final ProxyEngine proxyEngine)
	{
		this.proxyEngine = proxyEngine;
	}
}
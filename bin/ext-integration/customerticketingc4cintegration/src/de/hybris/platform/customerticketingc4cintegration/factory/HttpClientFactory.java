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
package de.hybris.platform.customerticketingc4cintegration.factory;


import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;


public class HttpClientFactory
{
	private static final Logger LOGGER = Logger.getLogger(HttpClientFactory.class);
	private final HttpClientBuilder builder;

	public HttpClientFactory()
	{
		builder = HttpClientBuilder.create();
	}

	public HttpClient getHttpClient()
	{
		final String proxyHost = Config.getParameter("http.proxy.host");
		final String proxyPort = Config.getParameter("http.proxy.port");
		if (StringUtils.isNotEmpty(proxyHost) && StringUtils.isNotEmpty(proxyPort))
		{
			builder.setProxy(new HttpHost(proxyHost, Integer.valueOf(proxyPort).intValue()));
		}
		return builder.build();
	}
}

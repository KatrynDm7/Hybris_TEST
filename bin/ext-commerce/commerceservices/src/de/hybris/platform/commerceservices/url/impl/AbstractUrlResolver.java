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
package de.hybris.platform.commerceservices.url.impl;

import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;
import de.hybris.platform.commerceservices.url.UrlResolver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract url resolver that provides support methods for url resolvers.
 * 
 * @param <T>
 *           the source type to resolve into a url
 */
public abstract class AbstractUrlResolver<T> implements UrlResolver<T>
{
	private static final Logger LOG = Logger.getLogger(AbstractUrlResolver.class);

	private ThreadContextService threadContextService;

	protected ThreadContextService getThreadContextService()
	{
		return threadContextService;
	}

	@Required
	public void setThreadContextService(final ThreadContextService threadContextService)
	{
		this.threadContextService = threadContextService;
	}

	/**
	 * Encode string with UTF8 encoding and then convert a string into a URL safe version of the string. Only upper and
	 * lower case letters and numbers are retained. All other characters are replaced by a hyphen (-).
	 * 
	 * @param text
	 *           the text to sanitize
	 * @return the safe version of the text
	 */
	protected String urlSafe(final String text)
	{
		if (text == null || text.isEmpty())
		{
			return "";
		}

		String encodedText;
		try
		{
			encodedText = URLEncoder.encode(text, "utf-8");
		}
		catch (final UnsupportedEncodingException encodingException)
		{
			encodedText = text;
			LOG.debug(encodingException.getMessage());
		}

		// Cleanup the text
		String cleanedText = encodedText;
		cleanedText = cleanedText.replaceAll("%2F", "/");
		cleanedText = cleanedText.replaceAll("[^%A-Za-z0-9\\-]+", "-");
		return cleanedText;
	}

	/**
	 * Url encodes a string
	 * @param source A string to encode
	 * @return A Url encoded string
	 */
	protected String urlEncode(final String source)
	{
		try
		{
			return URLEncoder.encode(source, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return source;
		}
	}

	/**
	 * Get the cache key for the source instance.
	 *
	 * @param source the source instance
	 * @return the cache key or <tt>null</tt> if caching is not supported
	 */
	protected String getKey(final T source)
	{
		// No caching by default
		return null;
	}

	@Override
	public String resolve(final T source)
	{
		final String key = getKey(source);
		if (key == null)
		{
			// No key, just return the value
			return resolveInternal(source);
		}

		// Lookup the cached value by key
		final String cachedValue = getThreadContextService().getAttribute(key);
		if (cachedValue != null)
		{
			return cachedValue;
		}

		final String url = resolveInternal(source);

		// Store the value in the thread cache
		getThreadContextService().setAttribute(key, url);

		return url;
	}

	/**
	 * Resolve the url path for the source type.
	 *
	 * @param source
	 *           the source type.
	 * @return the URL path
	 */
	protected abstract String resolveInternal(T source);
}

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
package de.hybris.platform.xyformsservices.proxy;

import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Class that implements some util methods to use the proxy engine.
 */
public interface ProxyService
{
	/**
	 * Rewrites a URL to be used for the proxy.
	 * 
	 * @param applicationId
	 *           application Id
	 * @param formId
	 *           form Id
	 * @param formDataId
	 *           form Data Id
	 * @throws MalformedURLException
	 */
	public String rewriteURL(final String applicationId, final String formId, final String formDataId)
			throws MalformedURLException;

	/**
	 * Rewrites a URL to be used for the proxy.
	 * 
	 * @param applicationId
	 *           application Id
	 * @param formId
	 *           form Id
	 * @param formDataId
	 *           form Data Id
	 * @param editable
	 *           Whether the form should be editable or not.
	 * @throws MalformedURLException
	 */
	public String rewriteURL(final String applicationId, final String formId, final String formDataId, final boolean editable)
			throws MalformedURLException;

	/**
	 * Rewrites a URL to be used for the proxy.
	 * 
	 * @param url
	 *           The url path to be rewritten
	 * @param embeddable
	 *           If the rewriter should produce a URL that produces embeddable content
	 * @throws MalformedURLException
	 */
	public String rewriteURL(final String url, final boolean embeddable) throws MalformedURLException;

	/**
	 * Generates a random namespace
	 */
	public String getNextRandomNamespace();

	/**
	 * Extracts the namespace coming from client.
	 * 
	 * @param request
	 */
	public String extractNamespace(HttpServletRequest request);

	/**
	 * Returns the extra headers configured for the application
	 * 
	 */
	public Map<String, String> getExtraHeaders();

	/**
	 * Proxies content
	 * 
	 * @param request
	 *           The {@link HttpServletRequest} associated to the call
	 * @param response
	 *           The {@link HttpServletResponse} associated to the call
	 * @param namespace
	 *           Used for HTML element's id generation
	 * @param url
	 *           The url to be called
	 * @param forceGetMethod
	 *           Useful when proxying content
	 * @param headers
	 *           Extra headers to be passed to the connection
	 * @throws ProxyException
	 */
	void proxy(HttpServletRequest request, HttpServletResponse response, String namespace, String url, boolean forceGetMethod,
			final Map<String, String> headers) throws ProxyException;
}

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
package de.hybris.platform.xyformsfacades.proxy.impl;

import de.hybris.platform.xyformsfacades.proxy.ProxyFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;
import de.hybris.platform.xyformsservices.proxy.ProxyException;
import de.hybris.platform.xyformsservices.proxy.ProxyService;
import de.hybris.platform.xyformsservices.utils.YHttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * Orchestrates calls to {@link ProxyService}
 */
public class DefaultProxyFacade implements ProxyFacade
{
	@Resource
	private ProxyService proxyService;

	@Override
	public String getInlineFormHtml(final String applicationId, final String formId, final YFormDataActionEnum action,
			final String formDataId) throws YFormServiceException
	{
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		final StringWriter out = new StringWriter();
		final OutputStream os = new WriterOutputStream(out, "UTF-8");

		try
		{
			final boolean editable = !YFormDataActionEnum.VIEW.equals(action);
			final String url = this.proxyService.rewriteURL(applicationId, formId, formDataId, editable);
			final String namespace = this.proxyService.getNextRandomNamespace();
			final Map<String, String> extraHeaders = this.proxyService.getExtraHeaders();

			// The proxy needs a Response object... we provide a mocked one.
			final HttpServletResponse response = new YHttpServletResponse(os);

			this.proxyService.proxy(request, response, namespace, url, true, extraHeaders);
			os.flush();
			return out.toString();
		}
		catch (final ProxyException | IOException e)
		{
			throw new YFormServiceException(e);
		}
		finally
		{
			IOUtils.closeQuietly(os);
		}
	}

	@Override
	public void proxy(final HttpServletRequest request, final HttpServletResponse response) throws ProxyException
	{
		try
		{
			final String url = request.getRequestURL().toString();

			final String namespace = proxyService.extractNamespace(request);
			final String newURL = proxyService.rewriteURL(url, false);
			final Map<String, String> extraHeaders = proxyService.getExtraHeaders();

			proxyService.proxy(request, response, namespace, newURL, false, extraHeaders);
		}
		catch (final MalformedURLException e)
		{
			throw new ProxyException(e);
		}
	}

	protected ProxyService getProxyService()
	{
		return this.proxyService;
	}

	public void setProxyService(final ProxyService proxyService)
	{
		this.proxyService = proxyService;
	}
}

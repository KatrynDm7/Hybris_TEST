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
package de.hybris.platform.yacceleratorstorefront.util;

import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * A Spring MVC <code>HandlerInterceptor</code> which is responsible to enforce CSRF token validity on incoming posts
 * requests. The interceptor should be registered with Spring MVC servlet using the following syntax:
 *
 * <pre>
 *   &lt;mvc:interceptors&gt;
 *        &lt;bean class="com.eyallupu.blog.springmvc.controller.csrf.CSRFHandlerInterceptor"/&gt;
 *   &lt;/mvc:interceptors&gt;
 * </pre>
 *
 * @see CSRFRequestDataValueProcessor
 */
public class CSRFHandlerInterceptor extends HandlerInterceptorAdapter
{
	private final static String CSRF_ALLOWED_URLS = "csrf.allowed.url.patterns";

	/**
	 * Set of servlet paths that should be allowed to go through without csrf validation. Usually that means those paths
	 * are already trusted (e.g. a rest call that is already validated by another token).
	 */
	private List<String> csrfAllowedUrlPatterns;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception
	{

		if (shouldCheckCSRFTokenForRequest(request))
		{
			// This is a POST request - need to check the CSRF token
			final String sessionToken = CSRFTokenManager.getTokenForSession(request.getSession());
			final String requestToken = CSRFTokenManager.getTokenFromRequest(request);
			if (sessionToken.equals(requestToken))
			{
				return true;
			}
			else
			{
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF value");
				return false;
			}
		}
		else
		{
			{
				// Not a POST - allow the request
				return true;
			}
		}
	}

	protected boolean shouldCheckCSRFTokenForRequest(final HttpServletRequest request)
	{
		return ("POST").equalsIgnoreCase(request.getMethod()) && !isCSRFExemptUrl(request.getServletPath());
	}

	protected boolean isCSRFExemptUrl(final String servletPath)
	{
		if (servletPath != null)
		{
			final String allowedUrlPatterns = Config.getParameter(CSRF_ALLOWED_URLS);
			final Set<String> allowedUrls = StringUtils.commaDelimitedListToSet(allowedUrlPatterns);
			if (CollectionUtils.isNotEmpty(csrfAllowedUrlPatterns))
			{
				allowedUrls.addAll(csrfAllowedUrlPatterns);
			}
			for (final String pattern : allowedUrls)
			{
				if (servletPath.matches(pattern))
				{
					return true;
				}
			}
		}
		return false;
	}

	public List<String> getCsrfAllowedUrlPatterns()
	{
		return csrfAllowedUrlPatterns;
	}

	public void setCsrfAllowedUrlPatterns(final List<String> csrfAllowedUrlPatterns)
	{
		this.csrfAllowedUrlPatterns = csrfAllowedUrlPatterns;
	}

}

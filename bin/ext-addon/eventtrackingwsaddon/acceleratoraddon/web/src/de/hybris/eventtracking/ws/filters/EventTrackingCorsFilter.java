/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.eventtracking.ws.filters;

import de.hybris.platform.util.Config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;


/**
 * @author stevo.slavic
 *
 */
public class EventTrackingCorsFilter extends OncePerRequestFilter
{

	private static final String FILTERED_URI = "/events";
	private static final String ACCESS_CONTROL_MAX_AGE_HEADER = "Access-Control-Max-Age";
	private static final String ACCESS_CONTROL_REQUEST_HEADERS_HEADER = "Access-Control-Request-Headers";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";
	private static final String ACCESS_CONTROL_ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
	private static final String ACCESS_CONTROL_REQUEST_METHOD_HEADER = "Access-Control-Request-Method";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS_HEADER_CONF_PROPERTY = "eventtrackingwsaddon.cors_filter.allow_request_headers_header_value";
	private static final String ACCESS_CONTROL_MAX_AGE_HEADER_CONF_PROPERTY = "eventtrackingwsaddon.cors_filter.max_age_header_value";
	private static final String DEFAULT_ACCESS_CONTROL_MAX_AGE = "3600";
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_CONF_PROPERTY = "eventtrackingwsaddon.cors_filter.allow_origin_header_value";
	private static final String DEFAULT_ALLOWED_ORIGINS = "*";

	private final UrlPathHelper urlPathHelper;

	public EventTrackingCorsFilter(final UrlPathHelper urlPathHelper)
	{
		this.urlPathHelper = urlPathHelper;
	}

	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
			throws ServletException, IOException
	{
		final String requestHeadersHeader = request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS_HEADER);
		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER,
				Config.getString(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_CONF_PROPERTY, DEFAULT_ALLOWED_ORIGINS));
		response.setHeader(ACCESS_CONTROL_ALLOW_METHODS_HEADER, HttpMethod.POST.name());
		response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_HEADER,
				Config.getString(ACCESS_CONTROL_ALLOW_HEADERS_HEADER_CONF_PROPERTY, requestHeadersHeader));
		response.setHeader(ACCESS_CONTROL_MAX_AGE_HEADER,
				Config.getString(ACCESS_CONTROL_MAX_AGE_HEADER_CONF_PROPERTY, DEFAULT_ACCESS_CONTROL_MAX_AGE));

		chain.doFilter(request, response);
	}

	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#shouldNotFilter(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException
	{
		return !shouldFilter(request);
	}

	private boolean shouldFilter(final HttpServletRequest request)
	{
		return FILTERED_URI.equals(urlPathHelper.getServletPath(request))
				&& HttpMethod.POST.name().equals(request.getHeader(ACCESS_CONTROL_REQUEST_METHOD_HEADER))
				&& HttpMethod.OPTIONS.name().equals(request.getMethod());
	}

}

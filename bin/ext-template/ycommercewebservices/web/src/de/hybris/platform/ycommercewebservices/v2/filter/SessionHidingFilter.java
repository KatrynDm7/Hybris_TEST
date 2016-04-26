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
package de.hybris.platform.ycommercewebservices.v2.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class SessionHidingFilter implements Filter
{
	@Override
	public void destroy()
	{
		//
	}

	@Override
	public void doFilter(final ServletRequest paramServletRequest, final ServletResponse paramServletResponse,
			final FilterChain paramFilterChain) throws IOException, ServletException
	{
		final HttpServletRequest req = (HttpServletRequest) paramServletRequest;
		paramFilterChain.doFilter(new SessionHidingRequestWrapper(req), paramServletResponse);
	}

	@Override
	public void init(final FilterConfig paramFilterConfig) throws ServletException
	{
		//
	}

}

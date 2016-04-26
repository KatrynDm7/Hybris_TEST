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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.NestedServletException;


/**
 * Filter that catches and resolves exceptions thrown from other filters.
 */
public class ExceptionTranslationFilter extends GenericFilterBean
{

	private HandlerExceptionResolver restHandlerExceptionResolver;

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException
	{
		try
		{
			filterChain.doFilter(servletRequest, servletResponse);
		}
		catch (final NestedServletException ex)
		{
			if (ex.getRootCause() instanceof Exception)
			{
				// exceptions thrown in other filters are wrapped in a NestedServletException by AbstractPlatformFilterChain 
				restHandlerExceptionResolver.resolveException((HttpServletRequest) servletRequest,
						(HttpServletResponse) servletResponse, null, (Exception) ex.getRootCause());
			}
			else
			{
				// other throwable error
				throw ex;
			}
		}
		catch (final Exception ex)
		{
			restHandlerExceptionResolver.resolveException((HttpServletRequest) servletRequest,
					(HttpServletResponse) servletResponse, null, ex);
		}
	}

	protected HandlerExceptionResolver getRestHandlerExceptionResolver()
	{
		return restHandlerExceptionResolver;
	}

	@Required
	public void setRestHandlerExceptionResolver(final HandlerExceptionResolver restHandlerExceptionResolver)
	{
		this.restHandlerExceptionResolver = restHandlerExceptionResolver;
	}
}

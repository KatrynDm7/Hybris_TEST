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
package com.hybris.datahub.core.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.web.filter.ShallowEtagHeaderFilter;


/**
 * Improved version of @link{org.springframework.web.filter.ShallowEtagHeaderFilter}.
 * 
 * It contains additional checks which makes ETag applicable only when all conditions are met:
 * <ul>
 * <li>response status codes in the {@code 2xx} series</li>
 * <li>request method is a GET</li>
 * <li>response Cache-Control header is not set or does not contain a "no-store" directive</li>
 * </ul>
 * 
 * FIXME: Remove it after upgrading Spring MVC to >= 4.0.2 where it's not needed anymore
 */
public class ImprovedShallowEtagHeaderFilter extends ShallowEtagHeaderFilter
{
	private static final String HEADER_CACHE_CONTROL = "Cache-Control";
	private static final String DIRECTIVE_NO_STORE = "no-store";

	@Override
	protected boolean isEligibleForEtag(final HttpServletRequest request, final HttpServletResponse response,
			final int responseStatusCode, final byte[] responseBody)
	{
		if (responseStatusCode >= 200 && responseStatusCode < 300 && HttpMethod.GET.name().equals(request.getMethod()))
		{
			final String cacheControl = response.getHeader(HEADER_CACHE_CONTROL);
			if (cacheControl == null || !cacheControl.contains(DIRECTIVE_NO_STORE))
			{
				return true;
			}
		}
		return false;
	}
}

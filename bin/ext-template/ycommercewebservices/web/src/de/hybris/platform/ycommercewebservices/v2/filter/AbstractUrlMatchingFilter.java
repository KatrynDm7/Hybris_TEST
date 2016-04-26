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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Abstract matching filter that helps parsing urls.
 */
public abstract class AbstractUrlMatchingFilter extends OncePerRequestFilter
{
	protected boolean matchesUrl(final HttpServletRequest request, final String regexp)
	{
		final Matcher matcher = getMatcher(request, regexp);
		if (matcher.find())
		{
			return true;
		}
		return false;
	}

	protected String getValue(final HttpServletRequest request, final String regexp)
	{
		final Matcher matcher = getMatcher(request, regexp);
		if (matcher.find())
		{
			return matcher.group(1);
		}
		return null;
	}

	protected String getValue(final HttpServletRequest request, final String regexp, final String groupName)
	{
		final Matcher matcher = getMatcher(request, regexp);
		if (matcher.find())
		{
			return matcher.group(groupName);
		}
		return null;
	}

	private Matcher getMatcher(final HttpServletRequest request, final String regexp)
	{
		final Pattern pattern = Pattern.compile(regexp);
		final String path = request.getPathInfo() != null ? request.getPathInfo() : "";
		return pattern.matcher(path);
	}
}

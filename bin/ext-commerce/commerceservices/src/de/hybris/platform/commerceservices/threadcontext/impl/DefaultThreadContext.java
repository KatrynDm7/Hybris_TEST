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
package de.hybris.platform.commerceservices.threadcontext.impl;

import de.hybris.platform.commerceservices.threadcontext.ThreadContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the ThreadContext
 */
public class DefaultThreadContext implements ThreadContext
{
	private final ThreadContext parent;
	private final Map<String, Object> attributes = new HashMap<String, Object>();

	public DefaultThreadContext()
	{
		this(null);
	}

	public DefaultThreadContext(final ThreadContext parent)
	{
		this.parent = parent;
	}

	@Override
	public void setAttribute(final String name, final Object value)
	{
		attributes.put(name, value);
	}

	@Override
	public <T> T getAttribute(final String name)
	{
		// Look for the value in the current map
		final T value = (T) attributes.get(name);
		if (value != null || attributes.containsKey(name))
		{
			return value;
		}

		// Delegate to parent
		if (parent != null)
		{
			parent.getAttribute(name);
		}

		// Not found
		return null;
	}

	@Override
	public void removeAttribute(final String name)
	{
		attributes.remove(name);
	}
}

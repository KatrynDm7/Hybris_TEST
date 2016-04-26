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
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the ThreadContextService
 */
public class DefaultThreadContextService implements ThreadContextService
{
	private final ThreadLocal<List<ThreadContext>> threadLocalContexts = new ThreadLocal<List<ThreadContext>>()
	{
		@Override
		protected List<ThreadContext> initialValue()
		{
			return new ArrayList<ThreadContext>(3);
		}
	};

	@Override
	public ThreadContext getCurrentContext()
	{
		final List<ThreadContext> threadContexts = threadLocalContexts.get();
		if (threadContexts != null && !threadContexts.isEmpty())
		{
			// Return the last context
			return threadContexts.get(threadContexts.size() - 1);
		}
		return null;
	}

	@Override
	public <R, T extends Throwable> R executeInContext(final Executor<R, T> wrapper) throws T
	{
		try
		{
			createLocalThreadContext();
			return wrapper.execute();
		}
		finally
		{
			removeLocalThreadContext();
		}
	}

	@Override
	public void setAttribute(final String name, final Object value)
	{
		final ThreadContext currentContext = getCurrentContext();
		if (currentContext != null)
		{
			currentContext.setAttribute(name, value);
		}
	}

	@Override
	public <T> T getAttribute(final String name)
	{
		final ThreadContext currentContext = getCurrentContext();
		if (currentContext != null)
		{
			return currentContext.getAttribute(name);
		}

		return null;
	}

	@Override
	public void removeAttribute(final String name)
	{
		final ThreadContext currentContext = getCurrentContext();
		if (currentContext != null)
		{
			currentContext.removeAttribute(name);
		}
	}

	// ------------

	protected ThreadContext createLocalThreadContext()
	{
		final List<ThreadContext> threadContexts = threadLocalContexts.get();
		if (threadContexts != null)
		{
			if (threadContexts.isEmpty())
			{
				final ThreadContext activeContext = new DefaultThreadContext();
				threadContexts.add(activeContext);
				return activeContext;
			}
			else
			{
				final ThreadContext activeContext = new DefaultThreadContext(threadContexts.get(threadContexts.size() - 1));
				threadContexts.add(activeContext);
				return activeContext;
			}
		}
		return null;
	}

	protected void removeLocalThreadContext()
	{
		final List<ThreadContext> threadContexts = threadLocalContexts.get();
		if (threadContexts != null && !threadContexts.isEmpty())
		{
			// Remove the last context
			threadContexts.remove(threadContexts.size() - 1);
		}
	}
}

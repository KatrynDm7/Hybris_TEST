/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.servicelayer.session;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MockSessionService extends DefaultSessionService
{
	private final ThreadLocal<Session> sessionTL = new ThreadLocal<Session>();
	private final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	@Override
	public synchronized Session getCurrentSession()
	{
		Session session = sessionTL.get();
		if (session != null)
		{
			return session;
		}
		else
		{
			session = createNewSession();
		}
		return session;
	}

	/**
	 * get session by Session id
	 */
	@Override
	public Session getSession(final String id)
	{
		return null;
	}


	@Override
	public Session createNewSession()
	{
		final Session session = createSession();
		sessionTL.set(session);
		sessions.put(session.getSessionId(), session);
		return session;
	}

	@Override
	public Object executeInLocalView(final SessionExecutionBody body)
	{
		return body.execute();
	}

	@Override
	public void closeSession(final Session session)
	{
		sessions.remove(session.getSessionId());
		sessionTL.set(null);
	}

	@Override
	public Session createSession()
	{
		return new MockSession();
	}

	@Override
	public Object executeInLocalView(final SessionExecutionBody body, final UserModel model)
	{
		return body.execute();
	}

}

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
package de.hybris.platform.b2b.punchout.services.impl;

import de.hybris.platform.b2b.punchout.Organization;
import de.hybris.platform.b2b.punchout.PunchOutSession;
import de.hybris.platform.b2b.punchout.PunchOutSessionExpired;
import de.hybris.platform.b2b.punchout.PunchOutSessionNotFoundException;
import de.hybris.platform.b2b.punchout.model.StoredPunchOutSessionModel;
import de.hybris.platform.b2b.punchout.services.PunchOutSessionService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link PunchOutSessionService} based on the use of {@link SessionService}.
 */
public class DefaultPunchOutSessionService implements PunchOutSessionService
{
	protected static final String PUNCHOUT_SESSION_KEY = "punchoutSession";
	protected static final String PUNCHOUT_SESSION_ID = "punchoutSessionID";

	private SessionService sessionService;

	private ConfigurationService configurationService;

	private FlexibleSearchService flexibleSearchService;

	private CartService cartService;

	private ModelService modelService;

	@Override
	public void activate(final PunchOutSession punchoutSession)
	{
		sessionService.setAttribute(PUNCHOUT_SESSION_KEY, punchoutSession);
	}

	@Override
	public void saveCurrentPunchoutSession()
	{
		final String punchoutSessionId = generatePunchoutSessionId();
		sessionService.getCurrentSession().setAttribute(PUNCHOUT_SESSION_ID, punchoutSessionId);
		final PunchOutSession punchOutSession = getCurrentPunchOutSession();
		final CartModel cart = cartService.getSessionCart();

		final StoredPunchOutSessionModel storedSession = modelService.create(StoredPunchOutSessionModel.class);
		storedSession.setSid(punchoutSessionId);
		storedSession.setCart(cart);
		storedSession.setPunchOutSession(punchOutSession);

		modelService.save(storedSession);
	}

	protected String generatePunchoutSessionId()
	{
		final StringBuffer sb = new StringBuffer();
		final PunchOutSession punchOutSession = getCurrentPunchOutSession();
		sb.append(punchOutSession.getOperation());
		final List<Organization> from = punchOutSession.getInitiatedBy();
		for (final Organization credential : from)
		{
			sb.append(credential.getDomain() + credential.getIdentity());
		}
		// no need to be save, just generating a generic string bound to the operation and credentials
		return AsymmetricManager.getHash(sb.toString(), Long.toString(System.nanoTime()));
	}

	@Override
	public StoredPunchOutSessionModel loadStoredPunchOutSessionModel(final String punchoutSessionId)
	{
		StoredPunchOutSessionModel storedSession = null;
		final SearchResult<StoredPunchOutSessionModel> result = flexibleSearchService.search(
				"SELECT {pk} FROM {StoredPunchOutSession} WHERE {sid} = ?sid", Collections.singletonMap("sid", punchoutSessionId));
		if (result.getCount() == 1)
		{
			storedSession = result.getResult().get(0);
		}
		return storedSession;
	}

	@Override
	public PunchOutSession loadPunchOutSession(final String punchoutSessionId) throws PunchOutSessionNotFoundException,
			PunchOutSessionExpired
	{
		final StoredPunchOutSessionModel storedSession = loadStoredPunchOutSessionModel(punchoutSessionId);
		if (storedSession == null)
		{
			throw new PunchOutSessionNotFoundException("Session not found");
		}

		final PunchOutSession punchoutSession = (PunchOutSession) storedSession.getPunchOutSession();

		if (punchoutSession == null)
		{
			throw new PunchOutSessionNotFoundException("PunchOut session not found");
		}

		if (new Date().after(calculateCutOutTime(punchoutSession.getTime())))
		{
			throw new PunchOutSessionExpired("PunchOut session has expired");
		}
		sessionService.getCurrentSession().setAttribute(PUNCHOUT_SESSION_KEY, punchoutSession);

		return punchoutSession;
	}

	@Override
	public void setCurrentCartFromPunchOutSetup(final String punchoutSessionId)
	{
		try
		{
			final Session currentSession = sessionService.getCurrentSession();
			final StoredPunchOutSessionModel storedSession = loadStoredPunchOutSessionModel(punchoutSessionId);
			if (storedSession == null)
			{
				throw new PunchOutSessionNotFoundException("Session not found");
			}

			final CartModel cart = storedSession.getCart();
			// final CartModel cart = session.getAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME);
			// add old cart to current session
			currentSession.setAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME, cart);
		}
		catch (final NullPointerException e)
		{
			throw new PunchOutSessionNotFoundException("Session could not be retrieved.");
		}
	}

	/**
	 * @param sessionCreationDate
	 *           the creating time of the punchout session
	 * @return the time the session should have expired
	 */
	private Date calculateCutOutTime(final Date sessionCreationDate)
	{
		final int timeoutDuration = 5;
		return DateUtils.addMilliseconds(sessionCreationDate,
				configurationService.getConfiguration().getInteger("b2bpunchout.timeout", Integer.valueOf(timeoutDuration)));
	}

	@Override
	public String getCurrentPunchOutSessionId()
	{
		return sessionService.getCurrentSession().getAttribute(PUNCHOUT_SESSION_ID);
	}

	@Override
	public PunchOutSession getCurrentPunchOutSession()
	{
		return sessionService.getCurrentSession().getAttribute(PUNCHOUT_SESSION_KEY);
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}

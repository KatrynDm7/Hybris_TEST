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
package de.hybris.platform.ycommercewebservices.filter;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.ycommercewebservices.context.ContextInformationLoader;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter sets session context basing on request parameters:<br>
 * <ul>
 * <li><b>lang</b> - set current {@link LanguageModel}</li>
 * <li><b>curr</b> - set current {@link CurrencyModel}</li>
 * </ul>
 * 
 * @author KKW
 * 
 */
public class SessionAttributesFilter extends OncePerRequestFilter
{
	private final static Logger LOG = Logger.getLogger(SessionAttributesFilter.class);

	private ContextInformationLoader contextInformationLoader;
	private BaseStoreService baseStoreService;
	private SessionService sessionService;


	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		setUserTaxGroupAttribute();
		getContextInformationLoader().setLanguageFromRequest(request);
		getContextInformationLoader().setCurrencyFromRequest(request);

		filterChain.doFilter(request, response);
	}

	protected void setUserTaxGroupAttribute()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		if (currentBaseStore != null)
		{
			final UserTaxGroup taxGroup = currentBaseStore.getTaxGroup();
			if (taxGroup != null)
			{
				getSessionService().setAttribute(Europe1Constants.PARAMS.UTG, taxGroup);
			}
		}
	}

	protected ContextInformationLoader getContextInformationLoader()
	{
		return contextInformationLoader;
	}

	@Required
	public void setContextInformationLoader(final ContextInformationLoader contextInformationLoader)
	{
		this.contextInformationLoader = contextInformationLoader;
	}

	protected BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	protected SessionService getSessionService() {
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService) {
		this.sessionService = sessionService;
	}
}

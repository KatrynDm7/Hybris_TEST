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
package de.hybris.platform.assistedservicestorefront.filter;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceFacadeException;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceUtils;
import de.hybris.platform.assistedservicestorefront.restrictions.AssistedServicePathRestrictionEvaluator;
import de.hybris.platform.assistedservicestorefront.security.impl.AssistedServiceAgentLoginStrategy;
import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Assisted Service filter. Used for applying restrictions to storefront paths for AS agent and login agent based on
 * request.
 */
public class AssistedServiceFilter extends OncePerRequestFilter
{
	private AssistedServicePathRestrictionEvaluator assistedServicePathRestrictionEvaluator;
	private AssistedServiceFacade assistedServiceFacade;
	private AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy;

	@Override
	protected void doFilterInternal(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse,
			final FilterChain filterchain) throws ServletException, IOException
	{
		if (AssistedServiceUtils.getSamlCookie(httpservletrequest) != null)
		{
			try
			{
				if (assistedServiceFacade.isAssistedServiceAgentLoggedIn())
				{
					assistedServiceFacade.logoutAssistedServiceAgent();
				}
				assistedServiceFacade.loginAssistedServiceAgent(httpservletrequest);
				final LoginToken token = new CookieBasedLoginToken(AssistedServiceUtils.getSamlCookie(httpservletrequest));
				assistedServiceAgentLoginStrategy.login(token.getUser().getUid(), httpservletrequest, httpservletresponse);
				assistedServiceFacade.emulateAfterLogin();
				AssistedServiceUtils.eraseSamlCookie(httpservletresponse);
			}
			catch (final AssistedServiceFacadeException e)
			{
				assistedServiceFacade.getAsmSession().setFlashErrorMessage(e.getMessageCode());
			}
		}

		if (getAssistedServicePathRestrictionEvaluator().evaluate(httpservletrequest, httpservletresponse))
		{
			filterchain.doFilter(httpservletrequest, httpservletresponse);
		}

	}

	/**
	 * @return the assistedServicePathRestrictionEvaluator
	 */
	protected AssistedServicePathRestrictionEvaluator getAssistedServicePathRestrictionEvaluator()
	{
		return assistedServicePathRestrictionEvaluator;
	}

	/**
	 * @param assistedServicePathRestrictionEvaluator
	 *           the assistedServicePathRestrictionEvaluator to set
	 */
	@Required
	public void setAssistedServicePathRestrictionEvaluator(
			final AssistedServicePathRestrictionEvaluator assistedServicePathRestrictionEvaluator)
	{
		this.assistedServicePathRestrictionEvaluator = assistedServicePathRestrictionEvaluator;
	}

	/**
	 * @return the assistedServiceFacade
	 */
	protected AssistedServiceFacade getAssistedServiceFacade()
	{
		return assistedServiceFacade;
	}

	/**
	 * @param assistedServiceFacade
	 *           the assistedServiceFacade to set
	 */
	@Required
	public void setAssistedServiceFacade(final AssistedServiceFacade assistedServiceFacade)
	{
		this.assistedServiceFacade = assistedServiceFacade;
	}

	/**
	 * @return the assistedServiceAgentLoginStrategy
	 */
	protected AssistedServiceAgentLoginStrategy getAssistedServiceAgentLoginStrategy()
	{
		return assistedServiceAgentLoginStrategy;
	}

	/**
	 * @param assistedServiceAgentLoginStrategy
	 *           the assistedServiceAgentLoginStrategy to set
	 */
	@Required
	public void setAssistedServiceAgentLoginStrategy(final AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy)
	{
		this.assistedServiceAgentLoginStrategy = assistedServiceAgentLoginStrategy;
	}
}
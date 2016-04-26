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
package de.hybris.platform.assistedservicestorefront.security.impl;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;


/**
 * Implementation of login strategy for assisted service agent.
 */
public class AssistedServiceAgentLoginStrategy
{
	private GUIDCookieStrategy guidCookieStrategy;
	private UserDetailsService userDetailsService;

	public void login(final String username, final HttpServletRequest request, final HttpServletResponse response)
	{
		final UserDetails userDetails = getUserDetailsService().loadUserByUsername(username);
		final AssistedServiceAuthenticationToken token = new AssistedServiceAuthenticationToken(new AssistedServiceAgentPrincipal(
				username), userDetails.getAuthorities());
		token.setDetails(new WebAuthenticationDetails(request));
		final User user = UserManager.getInstance().getUserByLogin(username);
		JaloSession.getCurrentSession().setUser(user);
		SecurityContextHolder.getContext().setAuthentication(token);
		getGuidCookieStrategy().setCookie(request, response);
	}

	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	@Required
	public void setGuidCookieStrategy(final GUIDCookieStrategy guidCookieStrategy)
	{
		this.guidCookieStrategy = guidCookieStrategy;
	}

	/**
	 * @return the userDetailsService
	 */
	protected UserDetailsService getUserDetailsService()
	{
		return userDetailsService;
	}

	/**
	 * @param userDetailsService
	 *           the userDetailsService to set
	 */
	@Required
	public void setUserDetailsService(final UserDetailsService userDetailsService)
	{
		this.userDetailsService = userDetailsService;
	}
}

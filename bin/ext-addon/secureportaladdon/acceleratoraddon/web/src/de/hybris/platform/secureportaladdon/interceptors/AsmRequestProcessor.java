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
package de.hybris.platform.secureportaladdon.interceptors;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;


public class AsmRequestProcessor implements SecurePortalRequestProcessor
{
	private String agentSessionAttribute;

	private String asmRequestParameter;

	private String quitAsmRequestUri;

	private SessionService sessionService;


	public String getOtherRequestParameters(final HttpServletRequest request)
	{
		String assistedServiceModeRequested = request.getParameter(asmRequestParameter);
		if (request.getRequestURI().endsWith(quitAsmRequestUri))
		{
			assistedServiceModeRequested = "false";
		}

		String requestParam = null;
		if (assistedServiceModeRequested != null)
		{
			requestParam = asmRequestParameter + "=" + assistedServiceModeRequested;
		}

		return requestParam;
	}

	public boolean skipSecureCheck()
	{
		// if we find asm agent id in session, that means asm agent login, so we can skip secure checking in secureportal
		final Set<String> allAttributeNames = sessionService.getCurrentSession().getAllAttributes().keySet();
		for (final String name : allAttributeNames)
		{
			if (name.contains(agentSessionAttribute))
			{
				return true;
			}
		}

		return false;
	}

	@Required
	public void setAsmRequestParameter(final String asmRequestParameter)
	{
		this.asmRequestParameter = asmRequestParameter;
	}

	@Required
	public void setQuitAsmRequestUri(final String quitAsmRequestUri)
	{
		this.quitAsmRequestUri = quitAsmRequestUri;
	}

	@Required
	public void setAgentSessionAttribute(final String agentSessionAttribute)
	{
		this.agentSessionAttribute = agentSessionAttribute;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}

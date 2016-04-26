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
package de.hybris.platform.assistedservicestorefront.restrictions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Path restriction implementation for all Assisted Service Agents. Paths from provided list are restricted.
 */
public class AssitedServiceAllAgentsPathRestriction extends AssistedServicePathRestriction
{
	@Override
	public boolean evaluate(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse)
	{
		if (pathMatches(httpservletrequest) && getAssistedServiceFacade().isAssistedServiceAgentLoggedIn())
		{
			sendRedirectToPreviousPage(httpservletrequest, httpservletresponse);
			return false;
		}
		return true;
	}
}

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

import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Path restriction implementation for Assisted Service Sales Group. Paths from provided list are restricted.
 */
public class AssistedServiceSalesGroupPathRestriction extends AssistedServicePathRestriction
{
	@Override
	public boolean evaluate(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse)
	{
		if (pathMatches(httpservletrequest))
		{
			if (getAssistedServiceFacade().isAssistedServiceAgentLoggedIn())
			{
				final UserGroupModel managerGroup = getUserService().getUserGroupForUID(
						AssistedservicefacadesConstants.AS_MANAGER_AGENT_GROUP_UID);
				final UserModel agent = getAssistedServiceFacade().getAsmSession().getAgent();
				// restrict access in case agent is not in manager group
				if (!getUserService().isMemberOfGroup(agent, managerGroup))
				{
					sendRedirectToPreviousPage(httpservletrequest, httpservletresponse);
					return false;
				}
			}
		}
		return true;
	}
}
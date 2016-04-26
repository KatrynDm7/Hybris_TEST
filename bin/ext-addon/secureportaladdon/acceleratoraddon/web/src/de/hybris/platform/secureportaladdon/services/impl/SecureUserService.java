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
package de.hybris.platform.secureportaladdon.services.impl;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class SecureUserService extends DefaultUserService
{

	public static final String SECURE_GUID_SESSION_KEY = "acceleratorSecureGUID";

	private CMSSiteService cmsSiteService;


	@Override
	public boolean isAnonymousUser(final UserModel user)
	{
		final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		final HttpSession session = attr.getRequest().getSession(true); // true == allow create
		final CMSSiteModel site = cmsSiteService.getCurrentSite();

		boolean isUserAnonymous = user == null || super.isAnonymousUser(user);
		if (site.isRequiresAuthentication())
		{
			if ((String) session.getAttribute(SECURE_GUID_SESSION_KEY) == null)
			{
				isUserAnonymous = true;
			}
		}

		return isUserAnonymous;
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	@Required
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}


}

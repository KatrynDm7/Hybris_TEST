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

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Parent abstract class for path restrictions in asm.
 */
public abstract class AssistedServicePathRestriction
{
	private UserService userService;
	private AssistedServiceFacade assistedServiceFacade;
	private List<String> paths;
	protected final static Logger LOG = Logger.getLogger(AssistedServicePathRestriction.class);

	public abstract boolean evaluate(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse);

	protected void sendRedirectToPreviousPage(final HttpServletRequest httpservletrequest,
			final HttpServletResponse httpservletresponse)
	{
		try
		{
			final String previousPage = getPreviousPage(httpservletrequest);
			httpservletresponse.sendRedirect(previousPage);
			getAssistedServiceFacade().getAsmSession().setFlashErrorMessage("asm.action.restricted");
			LOG.debug(String.format("Agent [%s] is not allowed to proceed with page [%s], has been redirected to [%s]",
					getAssistedServiceFacade().getAsmSession().getAgent().getUid(), httpservletrequest.getServletPath(), previousPage));
		}
		catch (final IOException e)
		{
			LOG.error("Failed to send redirect.");
		}
	}

	private String getPreviousPage(final HttpServletRequest httpservletrequest)
	{
		final String referer = httpservletrequest.getHeader("referer");
		if (referer != null && !referer.equals(httpservletrequest.getServletPath()))
		{
			return referer;
		}
		return httpservletrequest.getContextPath();
	}

	protected boolean pathMatches(final HttpServletRequest httpservletrequest)
	{
		for (final String restrictedPath : getPaths())
		{
			final String restrictedPattern;
			if (restrictedPath.contains(":"))
			{
				final String[] splittedPath = restrictedPath.split(":");
				final String method = splittedPath[0];
				if (!httpservletrequest.getMethod().equalsIgnoreCase(method))
				{
					continue;
				}
				restrictedPattern = splittedPath[1];
			}
			else
			{
				restrictedPattern = restrictedPath;
			}
			if (matches(httpservletrequest.getServletPath(), restrictedPattern))
			{
				return true;
			}
		}
		return false;
	}

	private boolean matches(final String url, final String pattern)
	{
		return url.matches(pattern.replace("**", ".*?"));
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
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
	 * @return the paths
	 */
	protected List<String> getPaths()
	{
		return paths;
	}

	/**
	 * @param paths
	 *           the paths to set
	 */
	@Required
	public void setPaths(final List<String> paths)
	{
		this.paths = paths;
	}
}
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
package de.hybris.platform.assistedservicestorefront.component.renderer;

import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceFacadeException;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceSession;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class AssistedServiceComponentRenderer<C extends AbstractCMSComponentModel> extends DefaultAddOnCMSComponentRenderer<C>
{

	private final static Logger LOG = Logger.getLogger(AssistedServiceComponentRenderer.class);
	private AssistedServiceFacade assistedServiceFacade;
	private static final String ASM_REQUEST_PARAM = "asm";

	@Override
	public void renderComponent(final PageContext pageContext, final C component) throws ServletException, IOException
	{
		final String asmRequestParam = pageContext.getRequest().getParameter(ASM_REQUEST_PARAM);
		boolean asmSessionStatus = assistedServiceFacade.isAssistedServiceModeLaunched();

		// Check for "asm" parameter in HTTP request
		if (asmRequestParam != null)
		{
			// change behavior only when it's 'true' or 'false' as a value
			if (asmRequestParam.equalsIgnoreCase(Boolean.TRUE.toString()))
			{
				asmSessionStatus = true;
				assistedServiceFacade.launchAssistedServiceMode();
			}
			else if (asmRequestParam.equalsIgnoreCase(Boolean.FALSE.toString()))
			{
				assistedServiceFacade.quitAssistedServiceMode();
				asmSessionStatus = false;
			}
		}

		if (!assistedServiceFacade.isAssistedServiceAgentLoggedIn())
		{
			try
			{
				assistedServiceFacade.loginAssistedServiceAgent((HttpServletRequest) pageContext.getRequest());
			}
			catch (final AssistedServiceFacadeException e)
			{
				LOG.debug("Login via request failed");
			}
		}

		if (asmSessionStatus || assistedServiceFacade.isAssistedServiceAgentLoggedIn())
		{

			final String asmModuleView = "/WEB-INF/views/addons/" + getAddonUiExtensionName(component) + "/"
					+ getUIExperienceFolder() + "/cms/asm/assistedServiceComponent.jsp";
			// render component only when it's necessary
			final Map<String, Object> exposedVariables = exposeVariables(pageContext, component);
			pageContext.include(asmModuleView);
			exposedVariables.remove(AssistedServiceSession.AGENT); // agent can be used for other jsp\tags
			unExposeVariables(pageContext, component, exposedVariables);
		}
	}

	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final Map<String, Object> exposedVariables = super.getVariablesToExpose(pageContext, component);
		exposedVariables.putAll(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return exposedVariables;
	}


	protected void handleException(final Throwable throwable, final C component)
	{
		LOG.warn("Error processing component tag. currentComponent [" + component + "] exception: " + throwable.getMessage());
	}

	/**
	 * @return the assistedServiceFacade
	 */
	public AssistedServiceFacade getAssistedServiceFacade()
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
}
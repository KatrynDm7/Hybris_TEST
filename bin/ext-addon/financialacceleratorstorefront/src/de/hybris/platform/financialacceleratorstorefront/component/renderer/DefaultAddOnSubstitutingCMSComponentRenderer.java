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
package de.hybris.platform.financialacceleratorstorefront.component.renderer;

import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.financialservices.substitute.ExtensionSubstitutionService;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;


/**
 * This is a version of the AddOnCMSComponentRenderer which allows the Extension to be intercepted and the extension
 * substituted. This is because the component definition may not be in the same extension as the renderer (e.g.
 * component defined in xxxxServices might have the renderer in xxxxStorefront)
 *
 */
public class DefaultAddOnSubstitutingCMSComponentRenderer<C extends AbstractCMSComponentModel> extends
		DefaultAddOnCMSComponentRenderer<C>
{
	private ExtensionSubstitutionService extensionSubstitutionService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer#getAddonUiExtensionName(de.hybris
	 * .platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected String getAddonUiExtensionName(final C component)
	{
		final String addonUiExtensionName = super.getAddonUiExtensionName(component);
		return extensionSubstitutionService.getSubstitutedExtension(addonUiExtensionName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer#renderComponent(javax.servlet.jsp
	 * .PageContext, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	public void renderComponent(final PageContext pageContext, final C component) throws ServletException, IOException
	{
		final Map<String, Object> exposedVariables = exposeVariables(pageContext, component);
		final String view = getView(component); // The only difference to the base class to ensure we can see the returned view
		pageContext.include(view);
		unExposeVariables(pageContext, component, exposedVariables);
	}

	/**
	 * @return the extensionSubstitutionService
	 */
	public ExtensionSubstitutionService getExtensionSubstitutionService()
	{
		return extensionSubstitutionService;
	}

	/**
	 * @param extensionSubstitutionService
	 *           the extensionSubstitutionService to set
	 */
	public void setExtensionSubstitutionService(final ExtensionSubstitutionService extensionSubstitutionService)
	{
		this.extensionSubstitutionService = extensionSubstitutionService;
	}



}

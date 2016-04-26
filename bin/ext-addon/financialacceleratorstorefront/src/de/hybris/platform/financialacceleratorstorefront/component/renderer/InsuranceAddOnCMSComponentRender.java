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

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.Map;

import javax.servlet.jsp.PageContext;


/**
 * Extends the standard <DefaultAddOnCMSComponentRenderer> so that when the standard page level variables are created
 * for a renderer, the 'extension' and 'experience' (desktop,mobile,etc) are added to the page variables. This is so
 * that the addon handling can locate additional view jsps and tags within the application as the standard renderers
 * expect everything to be in the acceleratoraddon's standard location - even though additional content is copied in
 * there from the other extensions.
 *
 * This means that any standard OOTB template can now access non-OOTB resources. The limitation is that a new Item Type
 * needs to be created per extension, with the associated rendering / mapping wiring specified.
 *
 * @param <C>
 *           The Item Type that we are handling - the item type is used to determine the correct extension so there may
 *           be a requirement to have multiple types that are the same, but in different extensions.
 */
public class InsuranceAddOnCMSComponentRender<C extends AbstractCMSComponentModel> extends
		DefaultAddOnSubstitutingCMSComponentRenderer
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer#exposeVariables(javax.servlet.jsp
	 * .PageContext, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected Map exposeVariables(final PageContext pageContext, final AbstractCMSComponentModel component)
	{
		final Map exposeVariables = super.exposeVariables(pageContext, component);

		exposeVariables.put("extension", getAddonUiExtensionName(component));
		exposeVariables.put("experience", getUIExperienceFolder());

		pageContext.setAttribute("extension", getAddonUiExtensionName(component), PageContext.REQUEST_SCOPE);
		pageContext.setAttribute("experience", getUIExperienceFolder(), PageContext.REQUEST_SCOPE);

		return exposeVariables;
	}

}

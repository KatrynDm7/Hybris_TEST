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
package de.hybris.platform.b2bacceleratoraddon.component.renderer;

import de.hybris.platform.acceleratorcms.model.components.ProductAddToCartComponentModel;
import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.b2bacceleratoraddon.constants.B2bacceleratoraddonConstants;

import java.util.Map;

import javax.servlet.jsp.PageContext;


/**
 * b2bacceleratoraddon renderer for ProductAddToCartComponents
 */
public class B2BAcceleratorProductAddToCartComponentRenderer<C extends ProductAddToCartComponentModel> extends
		DefaultAddOnCMSComponentRenderer<C>
{
	private static final String COMPONENT = "component";

	@Override
	protected Map<String, Object> getVariablesToExpose(final PageContext pageContext, final C component)
	{
		final Map<String, Object> model = super.getVariablesToExpose(pageContext, component);
		model.put(COMPONENT, component);
		return model;
	}

	@Override
	protected String getAddonUiExtensionName(final C component)
	{
		return B2bacceleratoraddonConstants.EXTENSIONNAME;
	}
}

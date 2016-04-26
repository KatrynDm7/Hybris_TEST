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
package de.hybris.platform.financialacceleratorstorefront.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.financialservices.substitute.ExtensionSubstitutionService;

import javax.annotation.Resource;


public abstract class SubstitutingCMSAddOnComponentController<T extends AbstractCMSComponentModel> extends
		AbstractCMSAddOnComponentController<T>
{

	@Resource(name = "extensionSubstitutionService")
	private ExtensionSubstitutionService extensionSubstitutionService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController#getAddonUiExtensionName(de
	 * .hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected String getAddonUiExtensionName(final T component)
	{
		final String addonUiExtensionName = super.getAddonUiExtensionName(component);
		return extensionSubstitutionService.getSubstitutedExtension(addonUiExtensionName);
	}
}

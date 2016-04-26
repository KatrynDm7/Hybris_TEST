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
package de.hybris.platform.commerceservices.jalo;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.core.Registry;

/**
 * This is the extension manager of the CommerceServices extension.
 */
public class CommerceServicesManager extends GeneratedCommerceServicesManager
{
	/**
	 * Get the valid instance of this manager.
	 *
	 * @return the current instance of this manager
	 */
	public static CommerceServicesManager getInstance()
	{
		return (CommerceServicesManager) Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
				.getExtension(CommerceServicesConstants.EXTENSIONNAME);
	}
}

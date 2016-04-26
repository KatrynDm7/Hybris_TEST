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
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.commerceservices.strategies.PickupStrategy;
import de.hybris.platform.store.services.BaseStoreService;

import org.springframework.beans.factory.annotation.Required;


public class DefaultPickupStrategy implements PickupStrategy
{
	private BaseStoreService baseStoreService;
	private PickupInStoreMode defaultPickupInStoreMode = PickupInStoreMode.DISABLED;

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected PickupInStoreMode getDefaultPickupInStoreMode()
	{
		return defaultPickupInStoreMode;
	}

	// Optional
	public void setDefaultPickupInStoreMode(final PickupInStoreMode defaultPickupInStoreMode)
	{
		this.defaultPickupInStoreMode = defaultPickupInStoreMode;
	}

	@Override
	public PickupInStoreMode getPickupInStoreMode()
	{
		if (getBaseStoreService().getCurrentBaseStore() != null
				&& getBaseStoreService().getCurrentBaseStore().getPickupInStoreMode() != null)
		{
			return getBaseStoreService().getCurrentBaseStore().getPickupInStoreMode();
		}
		else
		{
			return getDefaultPickupInStoreMode();
		}
	}

}

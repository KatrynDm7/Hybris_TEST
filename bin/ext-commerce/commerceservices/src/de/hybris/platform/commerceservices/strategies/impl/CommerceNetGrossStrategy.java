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

import de.hybris.platform.commerceservices.strategies.NetGrossStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link NetGrossStrategy} uses the current base store to determine net/gross.
 */
public class CommerceNetGrossStrategy implements NetGrossStrategy
{
	private BaseStoreService baseStoreService;
	private NetGrossStrategy defaultNetGrossStrategy;
	private CartService cartService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.strategies.NetGrossStrategy#isNet()
	 */
	@Override
	public boolean isNet()
	{
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		if (baseStore != null)
		{
			return baseStore.isNet();
		}
		if (getCartService().hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			if (cart != null && cart.getNet() != null)
			{
				return cart.getNet().booleanValue();
			}
		}
		return getDefaultNetGrossStrategy().isNet();
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @param defaultNetGrossStrategy
	 *           the defaultNetGrossStrategy to set
	 */
	@Required
	public void setDefaultNetGrossStrategy(final NetGrossStrategy defaultNetGrossStrategy)
	{
		this.defaultNetGrossStrategy = defaultNetGrossStrategy;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected NetGrossStrategy getDefaultNetGrossStrategy()
	{
		return defaultNetGrossStrategy;
	}

}

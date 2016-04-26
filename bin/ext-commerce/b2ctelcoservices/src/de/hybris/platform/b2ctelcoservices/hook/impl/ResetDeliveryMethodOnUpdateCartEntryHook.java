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
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.order.CartService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Resets delivery mode to null every time on updating cart to avoid keeping inappropriate delivery mode for the new
 * items.
 */
public class ResetDeliveryMethodOnUpdateCartEntryHook implements CommerceUpdateCartEntryHook
{

	private CartService cartService;

	@Override
	public void beforeUpdateCartEntry(final CommerceCartParameter arg0)
	{
		return; //NOPMD
	}

	@Override
	public void afterUpdateCartEntry(final CommerceCartParameter arg0, final CommerceCartModification arg1)
	{
		getCartService().getSessionCart().setDeliveryMode(null);
	}

	/**
	 * @return the cartService
	 */
	protected CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}
}

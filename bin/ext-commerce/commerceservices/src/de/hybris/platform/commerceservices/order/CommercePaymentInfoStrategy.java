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
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;

/**
 * Payment info strategy
 */
public interface CommercePaymentInfoStrategy
{
	/**
	 * Stores payment info for the passed in cart.
	 *
	 * @param parameter A parameter object for cart and payment info
	 * @return true on success.
	 */
	boolean storePaymentInfoForCart(CommerceCheckoutParameter parameter);
}

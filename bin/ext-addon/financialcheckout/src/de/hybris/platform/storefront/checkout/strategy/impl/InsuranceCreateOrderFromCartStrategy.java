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
package de.hybris.platform.storefront.checkout.strategy.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.strategies.impl.DefaultCreateOrderFromCartStrategy;


/**
 * Strategy to override the Order code generation of the DefaultCreateOrderFromCartStrategy and carry over the Cart code
 * to Order code
 * */
public class InsuranceCreateOrderFromCartStrategy extends DefaultCreateOrderFromCartStrategy
{

	@Override
	protected String generateOrderCode(final CartModel cart)
	{
		return cart.getCode();
	}
}

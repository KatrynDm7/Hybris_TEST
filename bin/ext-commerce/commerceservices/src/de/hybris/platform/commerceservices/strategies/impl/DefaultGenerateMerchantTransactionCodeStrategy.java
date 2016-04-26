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

import de.hybris.platform.commerceservices.strategies.GenerateMerchantTransactionCodeStrategy;
import de.hybris.platform.core.model.order.CartModel;

import java.util.UUID;


public class DefaultGenerateMerchantTransactionCodeStrategy implements GenerateMerchantTransactionCodeStrategy
{
	@Override
	public String generateCode(final CartModel cartModel)
	{
		return cartModel.getUser().getUid() + "-" + UUID.randomUUID();
	}

}

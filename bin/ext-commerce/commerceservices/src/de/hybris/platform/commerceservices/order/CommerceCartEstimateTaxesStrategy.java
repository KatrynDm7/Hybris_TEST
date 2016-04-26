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


import de.hybris.platform.core.model.order.CartModel;

import java.math.BigDecimal;

/**
 * Strategy to abstract the estimation of taxes on a cart.
 */
public interface CommerceCartEstimateTaxesStrategy
{
	/**
	 * Estimate taxes for the cartModel and using the deliveryZipCode as the delivery zip code.
	 *
	 * @param cartModel              cart to estimate taxes for
	 * @param deliveryZipCode        zip code to use as the delivery address
	 * @param deliveryCountryIsocode country to use for the delivery address
	 * @return total of the estimated taxes
	 */
	BigDecimal estimateTaxes(CartModel cartModel, String deliveryZipCode, String deliveryCountryIsocode);
}

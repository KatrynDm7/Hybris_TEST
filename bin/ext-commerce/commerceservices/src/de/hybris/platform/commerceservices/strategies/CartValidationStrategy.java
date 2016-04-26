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
package de.hybris.platform.commerceservices.strategies;


import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;

/**
 * A strategy for validating the cart
 */
public interface CartValidationStrategy
{
	/**
	 * @deprecated use {@link #validateCart(de.hybris.platform.commerceservices.service.data.CommerceCartParameter)} instead.
	 */
	@Deprecated
	List<CommerceCartModification> validateCart(CartModel cartModel);

	List<CommerceCartModification> validateCart(final CommerceCartParameter parameter);

}

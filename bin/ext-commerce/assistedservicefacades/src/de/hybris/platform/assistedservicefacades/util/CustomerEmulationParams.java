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
package de.hybris.platform.assistedservicefacades.util;

/**
 * Class represents deep link parameters for saving in session.
 */
public class CustomerEmulationParams
{
	private final String userId;
	private final String cartId;

	public CustomerEmulationParams(final String userId, final String cartId)
	{
		this.userId = userId;
		this.cartId = cartId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @return the cartId
	 */
	public String getCartId()
	{
		return cartId;
	}
}
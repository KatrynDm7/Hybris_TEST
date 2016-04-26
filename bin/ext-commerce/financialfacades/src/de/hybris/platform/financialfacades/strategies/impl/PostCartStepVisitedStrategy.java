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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.order.CartService;

import org.springframework.beans.factory.annotation.Required;


/**
 * PostCartStepVisitedStrategy is used to verify the Step visited based on session cart. This Strategy ensures the cart
 * existence on the session.
 * 
 */
public class PostCartStepVisitedStrategy extends AbstractStepVisitedAnalysisStrategy
{
	private CartService cartService;

	/**
	 * This method signals the caller about the step visited based on session cart.
	 * 
	 * @param progressStepId
	 * @return true if session cart exist otherwise false.
	 */
	@Override
	public boolean isVisited(final String progressStepId)
	{
		boolean isVisited = Boolean.FALSE;

		if (getCartService().hasSessionCart())
		{
			isVisited = Boolean.TRUE;
		}
		return isVisited;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

}

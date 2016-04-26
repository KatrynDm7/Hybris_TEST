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
package de.hybris.platform.assistedservicestorefront.redirect.impl;

import de.hybris.platform.assistedservicestorefront.constants.AssistedservicestorefrontConstants;
import de.hybris.platform.assistedservicestorefront.redirect.AssistedServiceRedirectStrategy;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default property based implementation for {@link AssistedServiceRedirectStrategy}
 */
public class DefaultAssistedServiceRedirectStrategy implements AssistedServiceRedirectStrategy
{
	private CartService cartService;
	private UserService userService;

	@Override
	public String getRedirectPath()
	{
		if (getCartService().getSessionCart() != null && CollectionUtils.isNotEmpty(getCartService().getSessionCart().getEntries()))
		{
			return getPathWithCart();
		}
		else if (!getUserService().isAnonymousUser(getUserService().getCurrentUser()))
		{
			return getPathCustomerOnly();
		}
		return "/";
	}

	@Override
	public String getErrorRedirectPath()
	{
		return Config.getParameter(AssistedservicestorefrontConstants.REDIRECT_ERROR);
	}

	protected String getPathWithCart()
	{
		return Config.getParameter(AssistedservicestorefrontConstants.REDIRECT_WITH_CART);
	}

	protected String getPathCustomerOnly()
	{
		return Config.getParameter(AssistedservicestorefrontConstants.REDIRECT_CUSTOMER_ONLY);
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}
}
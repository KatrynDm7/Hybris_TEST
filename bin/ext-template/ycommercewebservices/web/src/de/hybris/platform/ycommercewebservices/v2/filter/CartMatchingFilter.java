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
package de.hybris.platform.ycommercewebservices.v2.filter;

import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;


/**
 * Filter that puts cart from the requested url into the session.
 */
public class CartMatchingFilter extends AbstractUrlMatchingFilter
{
	private String regexp;
    private CartLoaderStrategy cartLoaderStrategy;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		if (matchesUrl(request, regexp))
		{
            final String cartId = getValue(request, regexp);
			cartLoaderStrategy.loadCart(cartId);
		}

		filterChain.doFilter(request, response);
	}

	protected String getRegexp()
	{
		return regexp;
	}

	@Required
	public void setRegexp(final String regexp)
	{
		this.regexp = regexp;
	}

    public CartLoaderStrategy getCartLoaderStrategy()
    {
        return cartLoaderStrategy;
    }

    @Required
    public void setCartLoaderStrategy(CartLoaderStrategy cartLoaderStrategy)
    {
        this.cartLoaderStrategy = cartLoaderStrategy;
    }
}

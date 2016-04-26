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
package de.hybris.platform.ycommercewebservices.exceptions;

import javax.servlet.ServletException;


public class BaseSiteMismatchException extends ServletException
{
	public BaseSiteMismatchException(final String baseSiteIdInRequest, final String baseSiteIdInCart)
	{
		super("Base site '" + baseSiteIdInRequest + "' from the current request does not match with base site '" + baseSiteIdInCart
				+ "' from the cart!");
	}
}

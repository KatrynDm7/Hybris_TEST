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


/**
 * Thrown when request is not supported for current configuration.
 */
public class UnsupportedRequestException extends ServletException
{
	public UnsupportedRequestException(final String message)
	{
		super(message);
	}
}

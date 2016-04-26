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

import de.hybris.platform.servicelayer.exceptions.BusinessException;


/**
 * Exception thrown if the cart cannot be saved
 */
public class CommerceSaveCartException extends BusinessException
{
	public CommerceSaveCartException(final String message)
	{
		super(message);
	}

	public CommerceSaveCartException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}

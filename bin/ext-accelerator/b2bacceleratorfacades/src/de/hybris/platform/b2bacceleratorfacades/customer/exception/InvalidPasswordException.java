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
package de.hybris.platform.b2bacceleratorfacades.customer.exception;

import de.hybris.platform.servicelayer.exceptions.SystemException;


/**
 * Exception is thrown when there is attempt to change customer password but it does not match the validation regex.
 */
public class InvalidPasswordException extends SystemException
{

	public InvalidPasswordException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public InvalidPasswordException(final String message)
	{
		super(message);
	}

	public InvalidPasswordException(final Throwable cause)
	{
		super(cause);
	}

}

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
package de.hybris.platform.timedaccesspromotionsservices.exception;

/**
 * Exception is thrown when an attempt to enqueue again for same customer same flash buy promotion
 */
public class MultipleEnqueueException extends Exception
{
	/**
	 * Default constructor
	 */
	public MultipleEnqueueException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MultipleEnqueueException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MultipleEnqueueException(final String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public MultipleEnqueueException(final Throwable cause)
	{
		super(cause);
	}
}

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
 */

package com.hybris.datahub.core.facades.impl.converter;

/**
 * An exception indicating that errors reported by the impex process could not be parsed.
 *
 * @see #getCause() to retrieve the exception, which crashed the parsing process
 */
public class ErrorParsingException extends RuntimeException
{
	private static final long serialVersionUID = -6094000799859592188L;

	/**
	 * Instantiates the exception
	 *
	 * @param cause an exception that crashed the parsing process
	 */
	public ErrorParsingException(final Throwable cause)
	{
		super(cause);
	}
}

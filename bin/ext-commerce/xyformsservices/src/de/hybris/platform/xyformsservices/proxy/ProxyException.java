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
 */
package de.hybris.platform.xyformsservices.proxy;

/**
 * Exception thrown when dealing with Content Proxy
 */
public class ProxyException extends Exception
{
	/**
	 * Message based exception.
	 * 
	 * @param message
	 */
	public ProxyException(final String message)
	{
		super(message);
	}

	/**
	 * Message and throwable based Exception
	 * 
	 * @param message
	 * @param t
	 */
	public ProxyException(final String message, final Throwable t)
	{
		super(message, t);
	}

	/**
	 * Throwable based Exception
	 * 
	 * @param t
	 */
	public ProxyException(final Throwable t)
	{
		super(t);
	}
}

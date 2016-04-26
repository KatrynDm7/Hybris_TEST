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

package com.hybris.datahub.core.rest;

public class DataHubCommunicationException extends Exception
{
	public DataHubCommunicationException()
	{
		super();
	}

	public DataHubCommunicationException(final String message)
	{
		super(message);
	}

	public DataHubCommunicationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}


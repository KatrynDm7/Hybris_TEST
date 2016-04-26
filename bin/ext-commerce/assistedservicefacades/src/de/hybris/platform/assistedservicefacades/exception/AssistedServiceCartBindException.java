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
package de.hybris.platform.assistedservicefacades.exception;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;


/**
 * Exception for the {@link AssistedServiceFacade} which is used when a cart is binded to a emulated customer.
 */
public class AssistedServiceCartBindException extends AssistedServiceFacadeException
{

	public AssistedServiceCartBindException(final String message)
	{
		super(message);
	}

	public AssistedServiceCartBindException(final String message, final Throwable t)
	{
		super(message, t);
	}

	@Override
	public String getMessageCode()
	{
		return getMessage();
	}
}
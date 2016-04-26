/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.order.exceptions;

import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;


/**
 * Throw this exception when problems in Delivery Mode methods occur.
 * 
 */
public class DeliveryModeInterceptorException extends InterceptorException
{

	public DeliveryModeInterceptorException(final String message)
	{
		super(message, null, null);
	}

	public DeliveryModeInterceptorException(final String message, final Throwable cause)
	{
		super(message, cause, null);
	}

	public DeliveryModeInterceptorException(final String message, final Interceptor inter)
	{
		super(message, null, inter);
	}

	public DeliveryModeInterceptorException(final String message, final Throwable cause, final Interceptor inter)
	{
		super(message, cause, inter);
	}

}

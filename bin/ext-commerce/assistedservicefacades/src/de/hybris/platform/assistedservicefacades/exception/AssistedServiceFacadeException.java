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
 * Parent exception type for the {@link AssistedServiceFacade}.
 */
public class AssistedServiceFacadeException extends Exception
{
	protected final static String ASM_ALERT_CART = "ASM_alert_cart";
	protected final static String ASM_ALERT_CUSTOMER = "ASM_alert_customer";
	protected final static String ASM_ALERT_CREDENTIALS = "ASM_alert_cred";

	public AssistedServiceFacadeException(final String message)
	{
		super(message);
	}

	public AssistedServiceFacadeException(final String message, final Throwable t)
	{
		super(message, t);
	}

	/**
	 * Returns message code from message properties.
	 */
	public String getMessageCode()
	{
		return getMessage();
	}

	/**
	 * Returns alert class for usage in storefront.
	 */
	public String getAlertClass()
	{
		return "";
	}
}

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
package de.hybris.platform.financialacceleratorstorefront.controllers.exceptions;

import de.hybris.platform.commerceservices.order.CommerceCartModificationException;


/**
 * The class of CommerceCartModificationUpperLimitRearchedException.
 */
public class CommerceCartModificationUpperLimitReachedException extends CommerceCartModificationException
{
	public CommerceCartModificationUpperLimitReachedException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}

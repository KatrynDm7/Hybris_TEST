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
package de.hybris.platform.commercewebservicescommons.errors.exceptions;

/**
 * 
 */
public class CartException extends WebserviceException
{
	public static final String NOT_FOUND = "notFound";
	public static final String INVALID = "invalid";
	public static final String CANNOT_RESTORE = "cannotRestore";
	public static final String CANNOT_MERGE = "cannotMerge";
	public static final String CANNOT_RECALCULATE = "cannotRecalculate";
	public static final String CANNOT_RESET_DELIVERYMODE = "cannotResetDeliveryMode";
	public static final String CANNOT_RESET_ADDRESS = "cannotResetAddress";
	public static final String EXPIRED = "expired";
	private static final String TYPE = "CartError";
	private static final String SUBJECT_TYPE = "cart";

	public CartException(final String message)
	{
		super(message);
	}

	public CartException(final String message, final String reason)
	{
		super(message, reason);
	}

	public CartException(final String message, final String reason, final Throwable cause)
	{
		super(message, reason, cause);
	}

	public CartException(final String message, final String reason, final String subject)
	{
		super(message, reason, subject);
	}

	public CartException(final String message, final String reason, final String subject, final Throwable cause)
	{
		super(message, reason, subject, cause);
	}

	@Override
	public String getType()
	{
		return TYPE;
	}

	@Override
	public String getSubjectType()
	{
		return SUBJECT_TYPE;
	}
}

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
package de.hybris.platform.acceleratorwebservicesaddon.exceptions;

import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceException;


public class PaymentProviderException extends WebserviceException
{
	private static final String TYPE = "ValidationError";
	private static final String DEFAULT_SUBJECT_TYPE = "subscription";

	private String subjectType = DEFAULT_SUBJECT_TYPE;

	public PaymentProviderException(final String message)
	{
		super(message);
	}

	public PaymentProviderException(final String message, final String reason)
	{
		super(message, reason);
	}

	public PaymentProviderException(final String message, final String reason, final Throwable cause)
	{
		super(message, reason, cause);
	}

	public PaymentProviderException(final String message, final String reason, final String subject)
	{
		super(message, reason, subject);
	}

	public PaymentProviderException(final String message, final String reason, final String subject, final Throwable cause)
	{
		super(message, reason, subject, cause);
	}

	public PaymentProviderException(final String message, final String reason, final String subject, final String subjectType)
	{
		super(message, reason, subject);
		this.subjectType = subjectType;
	}

	public PaymentProviderException(final String message, final String reason, final String subject, final String subjectType,
			final Throwable cause)
	{
		super(message, reason, subject, cause);
		this.subjectType = subjectType;
	}

	@Override
	public String getType()
	{
		return TYPE;
	}

	@Override
	public String getSubjectType()
	{
		return subjectType;
	}

	public void setSubjectType(final String subjectType)
	{
		this.subjectType = subjectType;
	}
}

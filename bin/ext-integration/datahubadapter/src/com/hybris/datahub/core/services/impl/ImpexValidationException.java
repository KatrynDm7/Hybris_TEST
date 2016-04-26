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
 */
package com.hybris.datahub.core.services.impl;

import com.hybris.datahub.core.facades.ImportError;

import java.util.List;

public class ImpexValidationException extends RuntimeException
{
	private List<ImportError> errors;

	public ImpexValidationException(final List<ImportError> errors)
	{
		super();
		this.errors = errors;
	}

	public ImpexValidationException(final String message)
	{
		super(message);
	}

	public ImpexValidationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public List<ImportError> getErrors()
	{
		return errors;
	}
}

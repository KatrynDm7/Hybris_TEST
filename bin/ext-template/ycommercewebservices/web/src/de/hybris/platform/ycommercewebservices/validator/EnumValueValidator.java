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
package de.hybris.platform.ycommercewebservices.validator;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates if provided string is valid {@link HybrisEnumValue} value;
 */
public class EnumValueValidator implements Validator
{
	private final static String MESSAGE = "enum.invalidValue";

	public final static String DEFAULT_MESSAGE = "[{0}] is not valid value for [{1}]";

	private EnumerationService enumerationService;

	private final String enumClass;

	public EnumValueValidator(final String enumClass)
	{
		this.enumClass = enumClass;
	}

	@Override
	public boolean supports(final Class<?> paramClass)
	{
		return String[].class == paramClass;
	}


	@Override
	public void validate(final Object o, final Errors errors)
	{
		Assert.notNull(errors, "Errors object must not be null");
		final String[] param = (String[]) o;

		for (final String v : param)
		{
			validate(v, errors);
		}
	}

	private void validate(final String value, final Errors errors)
	{
		Object result;
		try
		{
			result = enumerationService.getEnumerationValue(enumClass, value);
		}
		catch (final UnknownIdentifierException e)
		{
			result = null;
		}
		if (result == null)
		{
			errors.reject(MESSAGE, new String[]
			{ value, enumClass }, DEFAULT_MESSAGE);
		}

	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

}

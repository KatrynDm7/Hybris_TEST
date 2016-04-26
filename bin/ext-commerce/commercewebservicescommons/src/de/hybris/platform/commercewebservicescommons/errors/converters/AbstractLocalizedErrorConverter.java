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
package de.hybris.platform.commercewebservicescommons.errors.converters;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;


/**
 * Abstract localized error converter for converting supported objects to a list of
 * {@link de.hybris.platform.commercewebservicescommons.dto.error.ErrorWsDTO}.
 */
public abstract class AbstractLocalizedErrorConverter extends AbstractErrorConverter
{
	private MessageSource messageSource;

	protected String getMessage(final String code, final Locale locale)
	{
		return messageSource.getMessage(code, new Object[0], locale);
	}

	protected String getMessage(final String code, final Object[] args, final Locale locale)
	{
		return messageSource.getMessage(code, args, locale);
	}

	@Required
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
}

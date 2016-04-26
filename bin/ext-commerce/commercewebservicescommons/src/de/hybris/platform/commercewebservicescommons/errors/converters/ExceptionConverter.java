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

import de.hybris.platform.commercewebservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceException;

import java.util.List;


/**
 * Converts {@link Exception} to a list of {@link de.hybris.platform.commercewebservicescommons.dto.error.ErrorWsDTO}.
 */
public class ExceptionConverter extends AbstractErrorConverter
{
	@Override
	public boolean supports(Class clazz)
	{
		return Exception.class.isAssignableFrom(clazz) && !WebserviceException.class.isAssignableFrom(clazz);
	}

	@Override
	public void populate(Object o, List<ErrorWsDTO> webserviceErrorList)
	{
		final Exception ex = (Exception) o;
		final ErrorWsDTO error = new ErrorWsDTO();
		error.setType(ex.getClass().getSimpleName().replace("Exception", "Error"));
		error.setMessage(ex.getMessage());
		webserviceErrorList.add(error);
	}
}

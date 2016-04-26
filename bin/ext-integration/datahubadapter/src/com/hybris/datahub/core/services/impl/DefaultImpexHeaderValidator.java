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
 */
package com.hybris.datahub.core.services.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ScriptValidationReader;
import de.hybris.platform.util.CSVConstants;

import com.hybris.datahub.core.services.ImpexHeaderValidator;

public class DefaultImpexHeaderValidator implements ImpexHeaderValidator
{
	@Override
	public void validateImpexHeader(final String header, final String macros) throws ImpExException
	{
		final StringBuilder headerBuilder = new StringBuilder().append(macros).append(CSVConstants.DEFAULT_LINE_SEPARATOR).append(header);

		ScriptValidationReader.parseHeader(headerBuilder.toString().trim());
	}
}

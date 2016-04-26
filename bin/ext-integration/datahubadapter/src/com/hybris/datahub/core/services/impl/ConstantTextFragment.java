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

import de.hybris.platform.util.CSVConstants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


/**
 * A block of an ImpEx script containing constant content.
 */
public class ConstantTextFragment implements ImpExFragment
{
	private final StringBuilder buffer;

	/**
	 * Instantiates a script fragment with constant content.
	 */
	public ConstantTextFragment()
	{
		buffer = new StringBuilder();
	}

	@Override
	public boolean addLine(final String line)
	{
		return addLine(line, null);
	}

	@Override
	public boolean addLine(final String line, final List<ImpExFragment> fragments)
	{
		if (lineShouldBeAdded(line))
		{
			buffer.append(line).append(CSVConstants.HYBRIS_LINE_SEPARATOR);
			return true;
		}
		return false;
	}

	private boolean lineShouldBeAdded(final String line)
	{
		return line != null && lineIsNotASpecialComment(line);
	}

	private boolean lineIsNotASpecialComment(final String line)
	{
		final String trimmedAndUppered = line.trim().toUpperCase();
		return !( trimmedAndUppered.startsWith("INSERT") || trimmedAndUppered.startsWith("REMOVE") );
	}

	@Override
	public String getContent()
	{
		return buffer.toString();
	}

	@Override
	public InputStream getContentAsInputStream()
	{
		return new ByteArrayInputStream(getContent().getBytes());
	}
}

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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * A logical block of an ImpEx script, e.g. a header, data block, macros, etc.
 */
public interface ImpExFragment
{
	/**
	 * Adds a line of the script to this block.
	 *
	 * @param line a line of script text
	 * @return <code>true</code>, if the line is added; <code>false</code>, if the line does not belong to this block.
	 * @throws {@link com.hybris.datahub.core.services.impl.ImpexValidationException} when a line is invalid
	 */
	boolean addLine(String line) throws ImpexValidationException;

	/**
	 * Adds a line of the script to this block.
	 *
	 * @param line a line of script text
	 * @return <code>true</code>, if the line is added; <code>false</code>, if the line does not belong to this block.
	 * @throws {@link com.hybris.datahub.core.services.impl.ImpexValidationException} when a line is invalid
	 */
	boolean addLine(String line, List<ImpExFragment> fragments) throws ImpexValidationException;

	/**
	 * Reads back the content of this script block.
	 *
	 * @return content of this script block or an empty string, if this block is empty.
	 * @throws IOException if failed to read this fragment content.
	 */
	String getContent() throws IOException;

	/**
	 * Retrieves an input stream to read content of this fragment.
	 *
	 * @return an input stream to read content of this fragment.
	 * @throws IOException if failed to create an input stream for the fragment content.
	 */
	InputStream getContentAsInputStream() throws IOException;
}

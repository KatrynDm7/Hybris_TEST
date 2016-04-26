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

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * A base test for testing ImpEx script fragments.
 */
@SuppressWarnings("javadoc")
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractScriptFragmentTest<T extends ImpExFragment>
{
	protected T fragment;

	protected void testLineThatShouldNotBeAdded(final String line) throws IOException
	{
		boolean wasAdded = fragment.addLine(line);
		assertLineWasNotAdded(wasAdded, line);

		wasAdded = fragment.addLine(" " + line);
		assertLineWasNotAdded(wasAdded, " " + line);
	}

	protected void assertLineWasNotAdded(final boolean wasAdded, final String line) throws IOException
	{
		Assert.assertFalse("Must not report 'true' for \"" + line + "\"", wasAdded);
		Assert.assertTrue("Script is expected to be empty but it's not", fragment.getContent().isEmpty());
	}

	protected String readContentFromTheInputStream() throws IOException
	{
		return IOUtils.toString(fragment.getContentAsInputStream());
	}

	protected void testLineThatShouldBeAdded(final String line) throws IOException
	{
		final boolean wasAdded = fragment.addLine(line);

		assertLineIsInTheFragment(wasAdded, line);
	}

	protected void assertLineIsInTheFragment(final boolean wasAdded, final String exp) throws IOException
	{
		final String line = exp + CSVConstants.HYBRIS_LINE_SEPARATOR;

		Assert.assertTrue("Must return 'true' for \"" + exp + "\"", wasAdded);
		Assert.assertTrue("\"" + line + "\" not found inside the block content", fragment.getContent().contains(line));
	}
}

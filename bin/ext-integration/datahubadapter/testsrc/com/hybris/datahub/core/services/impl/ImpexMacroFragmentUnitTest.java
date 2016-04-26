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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

public class ImpexMacroFragmentUnitTest extends AbstractScriptFragmentTest<ImpexMacroFragment>
{
	@Before
	public void setup()
	{
		fragment = new ImpexMacroFragment();
	}

	@Test
	public void testScriptFragmentIsEmptyBeforeAnyLineWasAdded() throws Exception
	{
		assertEquals("", fragment.getContent());
	}

	@Test
	public void testMacroCannotBeAdded() throws IOException
	{
		final String aMacro = "$catalogVersion=catalogversion(catalog(id[default=apparelProductCatalog]),version[default='Staged'])";

		final boolean wasAdded = fragment.addLine(aMacro, new Stack<ImpExFragment>());

		assertLineIsInTheFragment(wasAdded, aMacro);
	}
}

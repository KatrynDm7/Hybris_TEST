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

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.rest.client.ImpexDataImportClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Stack;


/**
 * Default implementation of the FragmentReader interface.
 */
public class DefaultFragmentReader implements FragmentReader
{
	private DataHubFacade facade = new ImpexDataImportClient();

	@Override
	public List<ImpExFragment> readScriptFragments(final ItemImportTaskData ctx) throws ImpExException
	{
		final Stack<ImpExFragment> fragments = new Stack<>();
		assert ctx != null : "Expect context to be validated by the facade/controller";
		assert ctx.getImpexMetaData() != null : "Expect impex byte stream to be validated by the facade/controller";

		try (final LineNumberReader reader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(ctx.getImpexMetaData()))))
		{
			for (String line = reader.readLine(); line != null; line = reader.readLine())
			{
				if (!line.isEmpty())
				{
					processLine(line, fragments, ctx);
				}
			}
		}
		catch (final IOException e)
		{
			throw new ImpExException(e, "Failed to read ImpEx script", 200001);
		}
		return fragments;
	}

	private void processLine(final String line, final Stack<ImpExFragment> fragments, final ItemImportTaskData taskData) throws ImpexValidationException, ImpExException
	{
		if (!addLineToCurrentFragment(line, fragments, taskData))
		{
			addLineToNewFragment(line, fragments);
		}
	}

	private boolean addLineToCurrentFragment(final String line, final Stack<ImpExFragment> fragments, final ItemImportTaskData ctx) throws ImpexValidationException
	{
		final ImpExFragment currentFragment = fragments.isEmpty() ? null : fragments.peek();
		try
		{
			return currentFragment != null && currentFragment.addLine(line, fragments);
		}
		catch (final ImpexValidationException ex)
		{
			ctx.getHeaderErrors().addAll(ex.getErrors());
			fragments.pop();
			return false;
		}
	}

	private void addLineToNewFragment(String line, final Stack<ImpExFragment> fragments) throws ImpExException
	{
		final ImpExFragment[] fragmentsToTry = getFragmentsToTry();

		for (final ImpExFragment frag : fragmentsToTry)
		{
			if (frag.addLine(line, fragments))
			{
				fragments.push(frag);
				break;
			}
		}
	}

	protected ImpExFragment[] getFragmentsToTry()
	{
		return new ImpExFragment[]{new DataHubDataFragment(facade), new ImpexMacroFragment(), new ConstantTextFragment()};
	}

	/**
	 * Injects facade implementation into this reader.
	 *
	 * @param ilf facade implementation to use.
	 */
	public void setDataHubFacade(final DataHubFacade ilf)
	{
		facade = ilf;
	}

	public DataHubFacade getFacade()
	{
		return facade;
	}
}

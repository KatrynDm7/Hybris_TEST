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
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.util.CSVConstants;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.services.ImpExResourceFactory;

import java.io.InputStream;
import java.util.List;

import com.google.common.base.Preconditions;


/**
 * An implementation of the ImpEx resource factory for creating Data Hub specific impex resources.
 */
public class DataHubImpExResourceFactory implements ImpExResourceFactory
{
	private FragmentReader reader = new DefaultFragmentReader();

	@Override
	public ImpExResource createResource(final ItemImportTaskData ctx) throws ImpExException
	{
		assert ctx != null : "Expect input stream to be validated by the facade/controller";

		final InputStream stream = createScriptStream(ctx);
		return new StreamBasedImpExResource(stream, CSVConstants.HYBRIS_ENCODING);
	}


	/**
	 * Creates an input stream to read the final ImpEx script content, which includes the data, because the original
	 * script does not contain actual data.
	 *
	 * @throws ImpExException if the script is invalid or reading failed.
	 */
	protected InputStream createScriptStream(final ItemImportTaskData ctx) throws ImpExException
	{
		return new FragmentedImpExInputStream(extractFragments(ctx));
	}

	/**
	 * Reads ImpEx script from the input stream and splits it into the logical fragments based on the script content.
	 *
	 * @param ctx a data object containing ImpEx data and errors
	 * @return a fragments of the script in the same order as they appear in the script or an empty list, if there is
	 * nothing in the input stream
	 * @throws ImpExException if the script is invalid or reading failed.
	 */
	protected List<ImpExFragment> extractFragments(final ItemImportTaskData ctx) throws ImpExException
	{
		return reader.readScriptFragments(ctx);
	}

	/**
	 * Retrieves the fragment reader to be used by this factory for reading the ImpEx script.
	 *
	 * @return a reader to be used.
	 */
	protected FragmentReader getFragmentReader()
	{
		return reader;
	}

	/**
	 * Specifies a reader to use for reading the ImpEx script.
	 *
	 * @param r a reader to use.
	 */
	public void setFragmentReader(final FragmentReader r)
	{
		Preconditions.checkArgument(r != null, "FragmentReader cannot be null");
		reader = r;
	}
}

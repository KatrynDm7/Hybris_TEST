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
 * An input stream for reading the fragmented ImpEx file content received from DataHub.
 */
public class FragmentedImpExInputStream extends InputStream
{
	private ImpExFragment[] fragments;
	private int currentFragmentNum;
	private InputStream currentFragmentInputStream;

	/**
	 * Instantiates this input stream.
	 *
	 * @param f a list of ImpEx fragments to read content from. This stream will read the fragments in the same order
	 * they received in the list.
	 */
	public FragmentedImpExInputStream(final List<ImpExFragment> f)
	{
		fragments = f != null ? f.toArray(new ImpExFragment[f.size()]) : noFragments();
		currentFragmentNum = 0;
	}

	@Override
	public int read() throws IOException
	{
		int byteRead = -1;
		while (byteRead == -1 && currentFragmentNum < fragments.length)
		{
			byteRead = readFrom(currentInputStream());
		}
		return byteRead;
	}

	private InputStream currentInputStream() throws IOException
	{
		if (currentFragmentInputStream == null)
		{
			currentFragmentInputStream = fragments[currentFragmentNum].getContentAsInputStream();
		}
		return currentFragmentInputStream;
	}

	private int readFrom(final InputStream in) throws IOException
	{
		final int val = in.read();
		if (val == -1)
		{
			closeCurrentFragmentStream();
		}
		return val;
	}

	@Override
	public int available() throws IOException
	{
		return currentFragmentInputStream != null ? currentFragmentInputStream.available() : 0;
	}

	@Override
	public void close() throws IOException
	{
		closeCurrentFragmentStream();
		fragments = noFragments();
	}

	private void closeCurrentFragmentStream() throws IOException
	{
		currentFragmentNum++;
		if (currentFragmentInputStream != null)
		{
			currentFragmentInputStream.close();
			currentFragmentInputStream = null;
		}
	}

	private ImpExFragment[] noFragments()
	{
		return new ImpExFragment[0];
	}

	@Override
	public boolean markSupported()
	{
		return false;
	}
}

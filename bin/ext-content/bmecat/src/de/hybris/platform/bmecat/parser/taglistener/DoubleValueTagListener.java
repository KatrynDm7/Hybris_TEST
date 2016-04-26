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
 * 
 *  
 */
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.TagListener;


/**
 * Parses the &lt;DoubleValue&gt; tag
 * 
 * 
 */
public class DoubleValueTagListener extends SimpleValueTagListener
{

	/**
	 * @param parent
	 * @param tagName
	 */
	public DoubleValueTagListener(final TagListener parent, final String tagName)
	{
		super(parent, tagName);
	}

	public DoubleValueTagListener(final TagListener parent, final String tagName, final boolean typed)
	{
		super(parent, tagName, typed);
	}

	@Override
	public synchronized Object getValue()
	{
		final String chars = getCharacters();
		if (chars == null || chars.length() == 0)
		{
			return null;
		}


		Double doubleValue = null;
		synchronized (getClass())
		{
			doubleValue = new Double(chars);
		}

		return doubleValue;
	}
}

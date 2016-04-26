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
 * Parses the &lt;StringValue&gt; tag
 * 
 * 
 */
public class StringValueTagListener extends SimpleValueTagListener
{
	/**
	 * @param parent
	 * @param tagName
	 */
	public StringValueTagListener(final TagListener parent, final String tagName)
	{
		super(parent, tagName);
	}

	public StringValueTagListener(final TagListener parent, final String tagName, final boolean typed)
	{
		super(parent, tagName, typed);
	}

	@Override
	public Object getValue()
	{
		return getCharacters();
	}
}

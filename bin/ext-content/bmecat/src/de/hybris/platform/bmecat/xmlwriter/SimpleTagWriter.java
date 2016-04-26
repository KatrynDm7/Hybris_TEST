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
package de.hybris.platform.bmecat.xmlwriter;

import java.io.IOException;

import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Simple&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class SimpleTagWriter extends XMLTagWriter
{
	private final String tagName;

	/**
	 * @param parent
	 * @param tagName
	 */
	public SimpleTagWriter(final XMLTagWriter parent, final String tagName)
	{
		this(parent, tagName, false);
	}

	public SimpleTagWriter(final XMLTagWriter parent, final String tagName, final boolean mandatory)
	{
		super(parent, mandatory);
		this.tagName = tagName;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		if (object != null)
		{
			xmlOut.setLineBreak(LineBreak.NONE);
			xmlOut.pcdata(object.toString());
		}
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return tagName;
	}

}

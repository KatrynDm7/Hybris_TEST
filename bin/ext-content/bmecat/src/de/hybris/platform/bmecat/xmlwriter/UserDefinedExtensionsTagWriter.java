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

import de.hybris.platform.bmecat.constants.BMECatConstants;

import java.io.IOException;
import java.util.Iterator;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;UserDefinedExtensions&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class UserDefinedExtensionsTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public UserDefinedExtensionsTagWriter(final XMLTagWriter parent)
	{
		super(parent);
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		for (final Iterator it = getAllSubTagWriter().iterator(); it.hasNext();)
		{
			final Object xmlTagWriterObject = it.next();
			((XMLTagWriter) xmlTagWriterObject).write(xmlOut, object);
		}
	}
}

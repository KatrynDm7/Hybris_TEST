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
import java.util.Collections;
import java.util.Map;

import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;SpecialTreatmentClass&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class SpecialTreatmentClassTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public SpecialTreatmentClassTagWriter(final XMLTagWriter parent)
	{
		super(parent);
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.SPECIAL_TREATMENT_CLASS;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		if (object != null)
		{
			xmlOut.setLineBreak(LineBreak.NONE);
			xmlOut.pcdata(((Map.Entry) object).getValue().toString());
		}
	}

	@Override
	protected Map getAttributesMap(final Object object)
	{
		return Collections.singletonMap("type", ((Map.Entry) object).getKey());
	}
}

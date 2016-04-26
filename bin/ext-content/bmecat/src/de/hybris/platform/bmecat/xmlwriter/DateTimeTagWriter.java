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
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;DateTime&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class DateTimeTagWriter extends XMLTagWriter
{
	private final String type;

	/**
	 * @param parent
	 */
	public DateTimeTagWriter(final XMLTagWriter parent, final String type)
	{
		this(parent, type, false);
	}

	public DateTimeTagWriter(final XMLTagWriter parent, final String type, final boolean mandatory)
	{
		super(parent, mandatory);
		this.type = type;
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.DATE, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.TIME));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.TIMEZONE));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.DATETIME;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final Date date = (Date) object;
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		final SimpleDateFormat timeZoneFormat = new SimpleDateFormat("z");
		getSubTagWriter(BMECatConstants.XML.TAG.DATE).write(xmlOut, dateFormat.format(date));
		getSubTagWriter(BMECatConstants.XML.TAG.TIME).write(xmlOut, timeFormat.format(date));
		getSubTagWriter(BMECatConstants.XML.TAG.TIMEZONE).write(xmlOut, timeZoneFormat.format(date));
	}

	@Override
	protected Map getAttributesMap(final Object object)
	{
		return Collections.singletonMap("type", type);
	}
}

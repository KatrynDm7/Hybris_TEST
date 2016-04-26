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
import de.hybris.platform.jalo.media.Media;

import java.io.IOException;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Mime&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class MimeTagWriter extends XMLTagWriter
{
	private final String purpose;

	/**
	 * @param parent
	 */
	public MimeTagWriter(final MimeInfoTagWriter parent, final String purpose)
	{
		super(parent);

		this.purpose = purpose;
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.MIME_TYPE));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.MIME_SOURCE, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.MIME_PURPOSE));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.MIME;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final Media media = (Media) object;
		getSubTagWriter(BMECatConstants.XML.TAG.MIME_TYPE).write(xmlOut, media.getMime());
		getSubTagWriter(BMECatConstants.XML.TAG.MIME_SOURCE).write(xmlOut, MimeInfoTagWriter.getMimeID(media));
		getSubTagWriter(BMECatConstants.XML.TAG.MIME_PURPOSE).write(xmlOut, purpose);
	}
}

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
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.Mime;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;Mime&gt; tag
 * 
 * 
 * 
 */
public class MimeTagListener extends DefaultBMECatTagListener
{
	public MimeTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.MIME_TYPE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MIME_SOURCE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MIME_PURPOSE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MIME_DESCR),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MIME_ALT),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.MIME_ORDER) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		final Mime mime = new Mime();
		mime.setType((String) getSubTagValue(BMECatConstants.XML.TAG.MIME_TYPE));
		mime.setSource((String) getSubTagValue(BMECatConstants.XML.TAG.MIME_SOURCE));
		mime.setPurpose((String) getSubTagValue(BMECatConstants.XML.TAG.MIME_PURPOSE));
		mime.setDescription((String) getSubTagValue(BMECatConstants.XML.TAG.MIME_DESCR));
		mime.setAlt((String) getSubTagValue(BMECatConstants.XML.TAG.MIME_ALT));
		mime.setOrder((Integer) getSubTagValue(BMECatConstants.XML.TAG.MIME_ORDER));
		return mime;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.MIME;
	}
}

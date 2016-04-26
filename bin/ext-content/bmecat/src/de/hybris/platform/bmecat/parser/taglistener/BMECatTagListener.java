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
import de.hybris.platform.bmecat.parser.Catalog;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;


/**
 * This listener is root of a hierarchy of listeners that parse BMECat tags. Each listener may call the processor with a
 * value object of the parsed tag.
 * 
 * 
 */
public class BMECatTagListener extends DefaultBMECatTagListener
{
	static final Logger log = Logger.getLogger(BMECatTagListener.class.getName());

	public BMECatTagListener(final TagListener parent)
	{
		super(parent);
	}

	public static class ATTRIBUTES
	{
		public static final String VERSION = "version";
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#createSubTagListeners()
	 */
	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new HeaderTagListener(this), new TNewCatalogTagListener(this), new TUpdateProductsTagListener(this),
				new TUpdatePricesTagListener(this) });
	}

	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor)
	{
		final String value = getAttribute(ATTRIBUTES.VERSION);
		addResult(value);
		return value;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.BMECAT;
	}

	/**
	 * @return the parsed catalog object.
	 */
	public Catalog getCatalog()
	{
		final Catalog cat = (Catalog) getSubTagValue(BMECatConstants.XML.TAG.HEADER); // note: header won't be imported, yet
		if (cat == null)
		{
			log.warn("catalog not yet available -- creating dummy catalog object");
			return new Catalog();
			//throw new IllegalStateException("catalog not yet available");
		}
		return cat;
	}
}

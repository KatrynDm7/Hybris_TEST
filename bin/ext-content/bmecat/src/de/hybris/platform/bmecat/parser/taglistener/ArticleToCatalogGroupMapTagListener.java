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

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;ArticleToCatalogGroupMap&gt; tag
 * 
 * 
 */
public class ArticleToCatalogGroupMapTagListener extends DefaultBMECatTagListener
{
	private static final String NEW = "new";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";

	/**
	 * TagListener for BMECat Tag ARTICLE_TO_CATALOGGROUP_MAP.
	 * 
	 * @param parent
	 *           The parent tag listener of this tag listener.
	 */
	public ArticleToCatalogGroupMapTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.ART_ID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CATALOG_GROUP_ID),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.ARTICLE_TO_CATALOGGROUP_MAP_ORDER),
				new AbortTagListener(this) });
	}

	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final ArticleToCatalogGroupMap a2c = new ArticleToCatalogGroupMap();
		a2c.setArticleID((String) getSubTagValue(BMECatConstants.XML.TAG.ART_ID));
		a2c.setCatalogGroupID((String) getSubTagValue(BMECatConstants.XML.TAG.CATALOG_GROUP_ID));
		a2c.setOrder((Integer) getSubTagValue(BMECatConstants.XML.TAG.ARTICLE_TO_CATALOGGROUP_MAP_ORDER));

		try
		{
			a2c.setMode(parseMode(getAttribute(BMECatConstants.XML.ATTRIBUTE.ARTICLE_TO_CATALOGGROUP_MAP.MODE)));
			processor.process(this, a2c);
			return a2c;
		}
		catch (final ParseException exp)
		{
			return null;
		}
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_TO_CATALOGGROUP_MAP;
	}

	private int parseMode(final String modeValue) throws ParseException
	{
		if (modeValue == null)
		{
			return BMECatConstants.MODE.NEW;
		}
		else
		{
			if (NEW.equals(modeValue))
			{
				return BMECatConstants.MODE.NEW;
			}
			else if (UPDATE.equals(modeValue))
			{
				return BMECatConstants.MODE.UPDATE;
			}
			else if (DELETE.equals(modeValue))
			{
				return BMECatConstants.MODE.DELETE;
			}
			else
			{
				throw new ParseException("Unknow mode '" + modeValue + "'! Valid modes are new, update & delete.", 0);
			}
		}
	}
}

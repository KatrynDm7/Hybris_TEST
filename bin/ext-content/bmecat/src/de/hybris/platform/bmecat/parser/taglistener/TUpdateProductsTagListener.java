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
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.Catalog;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;TUpdateProducts&gt; tag
 */
public class TUpdateProductsTagListener extends DefaultBMECatTagListener
{
	/**
	 * Dummy constructor; use this one if you need the method <code>getTagName()</code> only!
	 */
	public TUpdateProductsTagListener()
	{
		super();
	}

	public TUpdateProductsTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new ArticleTagListener(this), new ArticleToCatalogGroupMapTagListener(this) });
	}

	@Override
	protected Object processStartElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final BMECatTagListener btl = (BMECatTagListener) getParent();
		final Catalog cat = btl.getCatalog();
		if (cat != null)
		{
			cat.setTransactionMode(BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS);
			cat.setPreviousVersion(getAttribute(BMECatConstants.XML.ATTRIBUTE.T_UPDATE_PRODUCTS.PREV_VERSION) != null ? Integer
					.valueOf(getAttribute(BMECatConstants.XML.ATTRIBUTE.T_UPDATE_PRODUCTS.PREV_VERSION)) : null);
		}
		/*
		 * notify processor
		 */
		processor.process(this, cat);

		return null;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.T_UPDATE_PRODUCTS;
	}

	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		return null;
	}
}

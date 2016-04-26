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
 * Parses the &lt;TNewCatalog&gt; tag
 */
public class TNewCatalogTagListener extends DefaultBMECatTagListener
{
	/**
	 * Dummy constructor; use this one if you need the method <code>getTagName()</code> only!
	 */
	public TNewCatalogTagListener()
	{
		super();
	}

	public TNewCatalogTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new ClassificationSystemTagListener(this), new ArticleTagListener(this), new CatalogGroupSystemTagListener(this),
				new FeatureSystemTagListener(this), new ArticleToCatalogGroupMapTagListener(this) });
	}

	@Override
	protected Object processStartElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final BMECatTagListener btl = (BMECatTagListener) getParent();
		final Catalog cat = btl.getCatalog();
		if (cat != null)
		{
			cat.setTransactionMode(BMECatConstants.TRANSACTION.T_NEW_CATALOG);
		}

		/*
		 * notify processor
		 */
		processor.process(this, cat);

		return null;
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		return null;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.T_NEW_CATALOG;
	}
}

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
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.product.Product;

import java.io.IOException;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;ArticleToCatalogGroupMap&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticleToCatalogGroupMapTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public ArticleToCatalogGroupMapTagWriter(final XMLTagWriter parent)
	{
		super(parent);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ART_ID, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.CATALOG_GROUP_ID, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ARTICLE_TO_CATALOGGROUP_MAP_ORDER));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_TO_CATALOGGROUP_MAP;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final Object[] link = (Object[]) object;
		getSubTagWriter(BMECatConstants.XML.TAG.ART_ID).write(xmlOut, ((Product) link[1]).getCode());
		getSubTagWriter(BMECatConstants.XML.TAG.CATALOG_GROUP_ID).write(xmlOut, ((Category) link[0]).getCode());
		getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_TO_CATALOGGROUP_MAP_ORDER).write(xmlOut, ((Integer) link[2]).toString());
	}
}

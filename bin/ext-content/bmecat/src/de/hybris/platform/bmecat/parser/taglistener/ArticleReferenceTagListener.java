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
import de.hybris.platform.bmecat.parser.ArticleReference;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;ArticleReference&gt; tag
 * 
 * 
 * 
 */
public class ArticleReferenceTagListener extends DefaultBMECatTagListener
{

	public ArticleReferenceTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.ART_ID_TO),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CATALOG_ID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CATALOG_VERSION) });
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_REFERENCE;
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		final ArticleReference articleReference = new ArticleReference();
		articleReference.setArticleReference((String) getSubTagValue(BMECatConstants.XML.TAG.ART_ID_TO));
		articleReference.setCatalogID((String) getSubTagValue(BMECatConstants.XML.TAG.CATALOG_ID));
		articleReference.setCatalogVersion((String) getSubTagValue(BMECatConstants.XML.TAG.CATALOG_VERSION));
		articleReference.setType(getAttribute(BMECatConstants.XML.ATTRIBUTE.ARTICLE_REFERENCE.TYPE));
		try
		{
			articleReference.setQuantity(Integer.valueOf((getAttribute(BMECatConstants.XML.ATTRIBUTE.ARTICLE_REFERENCE.QUANTITY))));
		}
		catch (final NumberFormatException x)
		{
			// DOCTODO Document reason, why this block is empty
		}
		return articleReference;
	}
}

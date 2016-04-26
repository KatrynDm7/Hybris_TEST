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
import de.hybris.platform.catalog.jalo.ProductReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;ArticleReference&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticleReferenceTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public ArticleReferenceTagWriter(final XMLTagWriter parent)
	{
		super(parent);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.ART_ID_TO, true));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_REFERENCE;
	}

	@Override
	protected Map getAttributesMap(final Object object)
	{
		final ProductReference productReference = (ProductReference) object;

		final Map attributesMap = new HashMap();
		final String type = productReference.getQualifier();
		if (type == null)
		{
			warn("Mandatory 'qualifier' (->type) attribute of ProductReference \"" + productReference
					+ "\" is missing, resulting bmecat file will not be valid!");
		}
		else
		{
			attributesMap.put("type", type);
		}
		final Integer quantity = productReference.getQuantity();
		if (quantity != null)
		{
			attributesMap.put("quantity", quantity.toString());
		}
		return attributesMap;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final ProductReference productReference = (ProductReference) object;
		getSubTagWriter(BMECatConstants.XML.TAG.ART_ID_TO).write(xmlOut, productReference.getTarget().getCode());
	}
}

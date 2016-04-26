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

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;ArticleDetails&gt; tag
 * 
 * 
 */
public class ArticleDetailsTagListener extends DefaultBMECatTagListener
{
	public ArticleDetailsTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	public void addSubTagValue(final String subtaglistener, final Object newvalue)
	{
		getParent().addSubTagValue(subtaglistener, newvalue);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.DESCRIPTION_SHORT),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.DESCRIPTION_LONG),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MANUFACTURER_TYPE_DESCR),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.EAN),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.SUPPLIER_ALT_AID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.BUYER_AID, true),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MANUFACTURER_AID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.MANUFACTURER_NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.ERP_GROUP_BUYER),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.ERP_GROUP_SUPPLIER),
				new DoubleValueTagListener(this, BMECatConstants.XML.TAG.DELIVERY_TIME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.SPECIAL_TREATMENT_CLASS, true),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.KEYWORD),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.REMARKS),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.SEGMENT),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.ARTICLE_ORDER),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.ARTICLE_STATUS, true) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		return null;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_DETAILS;
	}
}

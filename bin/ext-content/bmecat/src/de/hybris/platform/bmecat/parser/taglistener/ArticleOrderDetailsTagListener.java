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
 * Parses the &lt;ArticleOrderDetails&gt; tag
 * 
 * 
 * 
 */
public class ArticleOrderDetailsTagListener extends DefaultBMECatTagListener
{
	public ArticleOrderDetailsTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.ORDER_UNIT),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.PRICE_QUANTITY),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CONTENT_UNIT),
				new DoubleValueTagListener(this, BMECatConstants.XML.TAG.NO_CU_PER_OU),
				new DoubleValueTagListener(this, BMECatConstants.XML.TAG.PRICE_QUANTITY),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.QUANTITY_MIN),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.QUANTITY_INTERVAL) });
	}

	/**
	 * Passes all values to parent (ArticleTagListener).
	 */
	@Override
	protected void addSubTagValue(final String subTagListenerName, final Object newValue)
	{
		getParent().addSubTagValue(subTagListenerName, newValue);
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		return null;
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_ORDER_DETAILS;
	}
}

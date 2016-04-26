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
import de.hybris.platform.bmecat.parser.ArticlePrice;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.BMECatParser;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;


/**
 * Parses the &lt;ArticlePriceDetails&gt; tag
 * 
 * 
 * 
 */
public class ArticlePriceTagListener extends DefaultBMECatTagListener
{
	private static final Logger log = Logger.getLogger(ArticlePriceTagListener.class.getName());

	public ArticlePriceTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new DoubleValueTagListener(this, BMECatConstants.XML.TAG.PRICE_AMOUNT),
				new DoubleValueTagListener(this, BMECatConstants.XML.TAG.PRICE_FACTOR),
				new DoubleValueTagListener(this, BMECatConstants.XML.TAG.LOWER_BOUND),
				new DoubleValueTagListener(this, BMECatConstants.XML.TAG.TAX),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.PRICE_CURRENCY),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.TERRITORY) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final String type = getAttribute(BMECatConstants.XML.ATTRIBUTE.ARTICLE_PRICE.PRICE_TYPE);

		//bugfix: #4924
		if (type == null || type.trim().length() < 1)
		{
			log.error("Missing attribute: " + BMECatConstants.XML.ATTRIBUTE.ARTICLE_PRICE.PRICE_TYPE);
			throw new BMECatParser.MissingAttributeParseAbortException("Missing attribute: "
					+ BMECatConstants.XML.ATTRIBUTE.ARTICLE_PRICE.PRICE_TYPE);
		}

		final Double amount = (Double) getSubTagValue(BMECatConstants.XML.TAG.PRICE_AMOUNT);
		final Double factor = (Double) getSubTagValue(BMECatConstants.XML.TAG.PRICE_FACTOR);
		final Double lower = (Double) getSubTagValue(BMECatConstants.XML.TAG.LOWER_BOUND);
		final Double tax = (Double) getSubTagValue(BMECatConstants.XML.TAG.TAX);
		final String currency = (String) getSubTagValue(BMECatConstants.XML.TAG.PRICE_CURRENCY);
		final Collection<String> territories = getSubTagValueCollection(BMECatConstants.XML.TAG.TERRITORY);
		return new ArticlePrice(type, amount.doubleValue(), factor != null ? factor.doubleValue() : 1, lower != null ? lower
				.doubleValue() : 1, tax != null ? tax.doubleValue() : 0, currency, territories);
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_PRICE;
	}
}

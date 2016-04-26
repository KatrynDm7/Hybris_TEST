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
import de.hybris.platform.bmecat.parser.ArticlePriceDetails;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.DateTime;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


/**
 * Parses the &lt;ArticlePriceDetails&gt; tag
 * 
 * 
 * 
 */
public class ArticlePriceDetailsTagListener extends DefaultBMECatTagListener
{
	public ArticlePriceDetailsTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new ArticlePriceTagListener(this), new DateTimeValueTagListener(this),
				new BooleanValueTagListener(this, BMECatConstants.XML.TAG.DAILY_PRICE), new AbortTagListener(this) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		Date startDate = null;
		Date endDate = null;
		for (final Iterator it = getSubTagValueCollection(BMECatConstants.XML.TAG.DATETIME).iterator(); it.hasNext();)
		{
			final DateTime dateTime = (DateTime) it.next();
			if (DateTime.TYPE_START_DATE.equalsIgnoreCase(dateTime.getType()))
			{
				if (startDate != null)
				{
					throw new IllegalStateException("duplicate start dates " + startDate + " and " + dateTime);
				}
				else
				{
					startDate = dateTime;
				}
			}
			else
			{
				if (endDate != null)
				{
					throw new IllegalStateException("duplicate end dates " + endDate + " and " + dateTime);
				}
				else
				{
					endDate = dateTime;
				}
			}

		}
		return new ArticlePriceDetails(startDate, endDate,
				Boolean.TRUE.equals(getSubTagValue(BMECatConstants.XML.TAG.DAILY_PRICE)),
				getSubTagValueCollection(BMECatConstants.XML.TAG.ARTICLE_PRICE));
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_PRICE_DETAILS;
	}
}

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
package de.hybris.platform.bmecat.parser;

import de.hybris.bootstrap.xml.AbstractValueObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;


/**
 * Object which holds the value of a parsed &lt;ARTICLEPRICEDETAILS&gt; tag
 * 
 */
public class ArticlePriceDetails extends AbstractValueObject
{
	private final Date startDate;
	private final Date endDate;
	private final boolean dailyPrice;

	private final Collection prices;

	public ArticlePriceDetails(final Date startDate, final Date endDate, final boolean daily, final Collection<ArticlePrice> prices)
	{
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.dailyPrice = daily;
		this.prices = prices != null ? Collections.unmodifiableCollection(prices) : Collections.EMPTY_LIST;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.DATETIME type="valid_end_date"
	 * 
	 * @return Returns the endDate.
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.DAILY_PRICE
	 * 
	 * @return Returns the isDailyPrice.
	 */
	public boolean isDailyPrice()
	{
		return dailyPrice;
	}

	/**
	 * @return Returns the prices as collection of {@link ArticlePrice} instances.
	 */
	public Collection<ArticlePrice> getPrices()
	{
		return prices;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.DATETIME type="valid_start_date"
	 * 
	 * @return Returns the startDate.
	 */
	public Date getStartDate()
	{
		return startDate;
	}

}

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


/**
 * Object which holds the value of a parsed &lt;ARTICLEPRICE&gt; tag
 * 
 * 
 * 
 */
public class ArticlePrice extends AbstractValueObject
{
	public static final String TYPE_NET_LIST = "net_list";
	public static final String TYPE_GROS_LIST = "gros_list";
	public static final String TYPE_NET_CUSTOMER = "net_customer";
	public static final String TYPE_NET_CUSTOMER_EXP = "net_customer_exp";
	public static final String TYPE_NRP = "nrp";
	public static final String USER_TYPE_PREFIX = "udp_";


	private final String priceType;
	private final double amount;
	private final double priceFactor;
	private final double lowerBound;
	private final double tax;
	private final String currency;
	private final Collection<String> territories;

	/**
	 * 
	 * @param type
	 *           one of <li>
	 *           <ul>
	 *           {@link #TYPE_NET_LIST}
	 *           </ul>
	 *           <ul>
	 *           {@link #TYPE_GROS_LIST}
	 *           </ul>
	 *           <ul>
	 *           {@link #TYPE_NET_CUSTOMER}
	 *           </ul>
	 *           <ul>
	 *           {@link #TYPE_NRP}
	 *           </ul>
	 *           <ul>
	 *           or a user defined type string staring with {@link #USER_TYPE_PREFIX}
	 *           </ul>
	 *           </li>
	 * @param amount
	 *           the price amount
	 */
	public ArticlePrice(final String type, final double amount)
	{
		this(type, amount, 1, 1, 0, null, null);
	}

	/**
	 * 
	 * @param type
	 *           one of <li>
	 *           <ul>
	 *           {@link #TYPE_NET_LIST}
	 *           </ul>
	 *           <ul>
	 *           {@link #TYPE_GROS_LIST}
	 *           </ul>
	 *           <ul>
	 *           {@link #TYPE_NET_CUSTOMER}
	 *           </ul>
	 *           <ul>
	 *           {@link #TYPE_NRP}
	 *           </ul>
	 *           <ul>
	 *           or a user defined type string staring with {@link #USER_TYPE_PREFIX}
	 *           </ul>
	 *           </li>
	 * @param amount
	 *           the price amount
	 * @param factor
	 *           the price (discount) factor
	 * @param lowerBound
	 *           the lower bound of this price
	 * @param tax
	 *           the tax which applies to this price
	 * @param currency
	 *           the currency code of this price, may be null
	 * @param territories
	 *           the territories which this price applies to, may be null or empty
	 */
	public ArticlePrice(final String type, final double amount, final double factor, final double lowerBound, final double tax,
			final String currency, final Collection<String> territories)
	{
		super();
		this.priceType = type;
		this.amount = amount;
		this.priceFactor = factor;
		this.lowerBound = lowerBound;
		this.tax = tax;
		this.currency = currency;
		this.territories = territories != null ? Collections.unmodifiableCollection(territories) : Collections.EMPTY_LIST;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.ARTICLE_PRICE.PRICE_AMOUNT
	 * 
	 * @return Returns the amount.
	 */
	public double getAmount()
	{
		return amount;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.ARTICLE_PRICE.PRICE_CURRENCY
	 * 
	 * @return Returns the currency.
	 */
	public String getCurrency()
	{
		return currency;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.ARTICLE_PRICE price_type="..."
	 * 
	 * @return Returns the priceType.
	 */
	public String getPriceType()
	{
		return priceType;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.ARTICLE_PRICE.TERRITORY
	 * 
	 * @return Returns the territories.
	 */
	public Collection<String> getTerritories()
	{
		return territories;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.ARTICLE_PRICE.LOWER_BOUND
	 * 
	 * @return Returns the lowerBound.
	 */
	public double getLowerBound()
	{
		return lowerBound;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.ARTICLE_PRICE.PRICE_FACTOR
	 * 
	 * @return Returns the priceFactor.
	 */
	public double getPriceFactor()
	{
		return priceFactor;
	}

	/**
	 * BMECat: ARTICLE.ARTICLE_PRICE_DETAILS.ARTICLE_PRICE.TAX
	 * 
	 * @return Returns the tax.
	 */
	public double getTax()
	{
		return tax;
	}

}

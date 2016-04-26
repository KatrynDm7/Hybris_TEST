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
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.znerd.xmlenc.XMLOutputter;


/**
 * <code>
 * 	&lt;ARTICLE_PRICE price_type="net_customer"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&lt;PRICE_AMOUNT&gt;1.04&lt;/PRICE_AMOUNT&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&lt;PRICE_CURRENCY&gt;EUR&lt;/PRICE_CURRENCY&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&lt;TAX&gt;0.16&lt;/TAX&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&lt;PRICE_FACTOR&gt;0.8&lt;/PRICE_FACTOR&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&lt;LOWER_BOUND&gt;1&lt;/LOWER_BOUND&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&lt;TERRITORY&gt;DE&lt;/TERRITORY&gt;<br>
 * 	&nbsp;&nbsp;&nbsp;&lt;TERRITORY&gt;NL&lt;/TERRITORY&gt;<br>
 * 	&lt;/ARTICLE_PRICE&gt; <br>
 * </code>
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticlePriceTagWriter extends XMLTagWriter
{
	private final String priceType;

	/**
	 * @param parent
	 */
	public ArticlePriceTagWriter(final ArticlePriceDetailsTagWriter parent, final String priceType)
	{
		super(parent, true);
		this.priceType = priceType;

		addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.PRICE_AMOUNT, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.PRICE_CURRENCY));
		addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.TAX));
		addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.PRICE_FACTOR));
		addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.LOWER_BOUND));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.TERRITORY));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_PRICE;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final ArticlePriceContext ctx = (ArticlePriceContext) object;
		getSubTagWriter(BMECatConstants.XML.TAG.PRICE_AMOUNT).write(xmlOut, new Double(ctx.getPriceAmount()));
		final Currency curr = ctx.getPriceCurrency();
		if (curr != null)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.PRICE_CURRENCY).write(xmlOut, curr.getIsoCode());
		}

		if (ctx.getTax() > 0)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.TAX).write(xmlOut, new Double(ctx.getTax()));
		}

		if (ctx.getPriceFactor() > 0)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.PRICE_FACTOR).write(xmlOut, new Double(ctx.getPriceFactor()));
		}

		if (ctx.getLowerBound() > 0)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.LOWER_BOUND).write(xmlOut, new Double(ctx.getLowerBound()));
		}

		final Collection territories = ctx.getTerritories();
		if (territories != null && !territories.isEmpty())
		{
			for (final Iterator it = territories.iterator(); it.hasNext();)
			{
				final Country country = (Country) it.next();
				getSubTagWriter(BMECatConstants.XML.TAG.TERRITORY).write(xmlOut, country.getIsoCode());
			}
		}
	}

	public static class ArticlePriceContext
	{
		private final double priceAmount;
		private final String type;
		private Currency priceCurrency;
		private double tax;
		private double priceFactor;
		private double lowerBound;
		private Collection territories;

		public ArticlePriceContext(final double priceAmount, final String type)
		{
			this.priceAmount = priceAmount;
			this.type = type;
		}

		/**
		 * @return Returns the lowerBound.
		 */
		public double getLowerBound()
		{
			return lowerBound;
		}

		/**
		 * @param lowerBound
		 *           The lowerBound to set.
		 */
		public void setLowerBound(final double lowerBound)
		{
			this.lowerBound = lowerBound;
		}

		/**
		 * @return Returns the priceCurrency.
		 */
		public Currency getPriceCurrency()
		{
			return priceCurrency;
		}

		/**
		 * @param priceCurrency
		 *           The priceCurrency to set.
		 */
		public void setPriceCurrency(final Currency priceCurrency)
		{
			this.priceCurrency = priceCurrency;
		}

		/**
		 * @return Returns the priceFactor.
		 */
		public double getPriceFactor()
		{
			return priceFactor;
		}

		/**
		 * @param priceFactor
		 *           The priceFactor to set.
		 */
		public void setPriceFactor(final double priceFactor)
		{
			this.priceFactor = priceFactor;
		}

		/**
		 * @return Returns the tax.
		 */
		public double getTax()
		{
			return tax;
		}

		/**
		 * @param tax
		 *           The tax to set.
		 */
		public void setTax(final double tax)
		{
			this.tax = tax;
		}

		/**
		 * @return Returns the territories.
		 */
		public Collection getTerritories()
		{
			return territories;
		}

		/**
		 * @param territories
		 *           The territories to set.
		 */
		public void setTerritories(final Collection territories)
		{
			this.territories = territories;
		}

		/**
		 * @return Returns the priceAmount.
		 */
		public double getPriceAmount()
		{
			if (getPriceCurrency() != null)
			{
				return getPriceCurrency().round(priceAmount);
			}
			return priceAmount;
		}


		/**
		 * @return Returns the type.
		 */
		public String getType()
		{
			return type;
		}
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getAttributesMap(java.lang.Object)
	 */
	@Override
	protected Map getAttributesMap(final Object object)
	{
		return Collections.singletonMap("price_type", priceType);
	}
}

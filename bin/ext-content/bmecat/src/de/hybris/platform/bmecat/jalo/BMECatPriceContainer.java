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
package de.hybris.platform.bmecat.jalo;

import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.ArticlePrice;
import de.hybris.platform.bmecat.parser.ArticlePriceDetails;
import de.hybris.platform.europe1.jalo.TaxRow;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.TypeManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * BMECatPriceContainer
 * 
 * 
 */
public class BMECatPriceContainer
{
	final private BMECatImportCronJob cronJob;
	final private Article article;
	private Collection articlepricerows;

	private static final Logger LOG = Logger.getLogger(BMECatPriceContainer.class.getName());

	public BMECatPriceContainer(final BMECatImportCronJob cronJob, final Article article)
	{
		if (cronJob == null)
		{
			throw new JaloSystemException("cronjob couldn't be 'null'!");
		}
		if (article == null)
		{
			throw new JaloSystemException("article couldn't be 'null'!");
		}

		this.cronJob = cronJob;
		this.article = article;

		collectPriceRows();

		addTerritories();

		createTaxMappings();
	}

	public static TaxRow findTaxByValue(final double value)
	{
		final List list = JaloSession
				.getCurrentSession()
				.getFlexibleSearch()
				.search(
						"SELECT {" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(TaxRow.class).getCode() + "} "
								+ "WHERE {" + "value" + "} = ?value", Collections.singletonMap("value", new Double(value)),
						Collections.singletonList(TaxRow.class), true, true, 0, -1).getResult();
		if (list.isEmpty())
		{
			LOG.warn("no taxgroup found with value: " + value + "!");
		}
		if (list.size() > 1)
		{
			LOG.warn("multiple taxgroups (" + list + ") found with value: " + value + " - choosing first one");
		}

		return list.isEmpty() ? null : (TaxRow) list.get(0);
	}

	private void createTaxMappings()
	{
		//	 TERRITORY specific tax settings will be ignored!!!
		/*
		 * Collection territories = null; for(Iterator it = articlepricerows.iterator(); it.hasNext();) {
		 * BMECatArticlePriceRow row = (BMECatArticlePriceRow)it.next(); double tax = row.getTax(); territories =
		 * row.getTerritories();
		 * 
		 * if( territories != null && !territories.isEmpty() ) { for( Iterator it2 = territories.iterator();
		 * it2.hasNext(); ) { final String territory = (String) it2.next(); if( tax != 0 ) cronJob.addToTaxTypeMapping(
		 * getTaxMapKey( territory, tax ), findTaxByValue( tax ) ); } } else { cronJob.addToTaxTypeMapping(
		 * String.valueOf( tax ), findTaxByValue( tax ) ); } }
		 */
		final Set territoriesCodes = (Set) cronJob.getTransientObject(BMECatImportCronJob.TERRITORIES);

		if (territoriesCodes != null && territoriesCodes.size() > 1)
		{

			LOG.warn("Tax settings will be ignored!! (more than one territoy found -> there is no support for multiple country specific tax-settings, at this time)");
			return;
		}

		for (final Iterator it = articlepricerows.iterator(); it.hasNext();)
		{
			final BMECatArticlePriceRow row = (BMECatArticlePriceRow) it.next();
			final double tax = row.getTax();
			// NOTE: BMECat represents 16% as 0.16 and the [y]hybris platform as 16.00
			if (tax != 0)
			{
				cronJob.addToTaxTypeMapping(String.valueOf(tax), tax < 1 ? findTaxByValue(tax * 100) : findTaxByValue(tax));
			}
		}
	}

	// ignore TERRITORY specific tax settings now!!!!
	/*
	 * public static String getTaxMapKey( String territory, double taxvalue) { return territory +"_" + String.valueOf(
	 * taxvalue ); }
	 */

	private void collectPriceRows()
	{
		articlepricerows = new HashSet();

		final Collection articlePriceDetailsColl = article.getArticlePriceDetails();
		for (final Iterator it1 = articlePriceDetailsColl.iterator(); it1.hasNext();)
		{
			final ArticlePriceDetails articlePriceDetails = (ArticlePriceDetails) it1.next();

			final Collection articlePricesColl = articlePriceDetails.getPrices();
			for (final Iterator it2 = articlePricesColl.iterator(); it2.hasNext();)
			{
				final ArticlePrice articlePrice = (ArticlePrice) it2.next();
				articlepricerows.add(new BMECatArticlePriceRow(articlePrice));
			}
		}
	}

	private void addTerritories()
	{
		Collection territories = null;
		for (final Iterator it = articlepricerows.iterator(); it.hasNext();)
		{
			final BMECatArticlePriceRow row = (BMECatArticlePriceRow) it.next();
			territories = row.getTerritories();
		}

		if (territories != null && !territories.isEmpty())
		{
			Set territoriesCodes = (Set) cronJob.getTransientObject(BMECatImportCronJob.TERRITORIES);
			if (territoriesCodes == null)
			{
				cronJob.setTransientObject(BMECatImportCronJob.TERRITORIES, territoriesCodes = new HashSet());
			}
			boolean changed = false;
			for (final Iterator it = territories.iterator(); it.hasNext();)
			{
				final String territory = (String) it.next();
				if (!territoriesCodes.contains(territory))
				{
					changed = true;
					territoriesCodes.add(territory);
				}
			}
			if (changed)
			{
				cronJob.setTransientObject(BMECatImportCronJob.TERRITORIES, territoriesCodes);
				cronJob.setTerritories((Collection) cronJob.getTransientObject(BMECatImportCronJob.TERRITORIES));
			}
		}
	}

	public class BMECatArticlePriceRow
	{
		private final String pricetype;
		private final double tax;
		private final double price;
		private final Collection territories;

		public BMECatArticlePriceRow(final ArticlePrice articlePrice)
		{
			if (articlePrice == null)
			{
				throw new JaloSystemException("articlePrice couldn't be 'null'!");
			}

			// territories
			this.territories = articlePrice.getTerritories();

			// tax
			this.tax = articlePrice.getTax();

			//price
			this.price = articlePrice.getAmount();

			//pricetype
			this.pricetype = articlePrice.getPriceType();
		}

		public double getPrice()
		{
			return price;
		}

		public String getPricetype()
		{
			return pricetype;
		}

		public double getTax()
		{
			return tax;
		}

		public Collection getTerritories()
		{
			return territories;
		}
	}
}

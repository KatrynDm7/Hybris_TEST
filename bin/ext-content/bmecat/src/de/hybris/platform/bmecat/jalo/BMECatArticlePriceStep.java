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

import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.ParseFinishedException;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.Abort;
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.jalo.JaloInvalidParameterException;

import org.apache.log4j.Logger;


/**
 * This is an abstract Superclass for steps that imports theArticle prices. Since the platform allows different price
 * factories, you should implement your own import. For the Europe1 price factory the
 * {@link de.hybris.platform.bmecat.jalo.BMECatEurope1ArticlePriceStep} has been provided.
 * 
 * 
 */
public abstract class BMECatArticlePriceStep extends GeneratedBMECatArticlePriceStep
{
	private static final Logger LOG = Logger.getLogger(BMECatArticlePriceStep.class.getName());
	private final static String ABORTTYPE = "prices";

	/**
	 * no op
	 */
	@Override
	protected void initializeBMECatImport(final Catalog catalog, final BMECatImportCronJob cronJob)
	{
		cronJob.resetCounter();
	}

	abstract protected boolean skipArticleOnRestart(Catalog catalog, Article article, BMECatImportCronJob cronJob);

	@Override
	public int getCompletedCount(final BMECatImportCronJob cronJob)
	{
		return cronJob.getCounter();
	}

	@Override
	public int getTotalToComplete(final BMECatImportCronJob cronJob)
	{
		return cronJob.isCatalogInfoAvailableAsPrimitive() ? cronJob.getArticleCountAsPrimitive() : -1;
	}

	/**
	 * Imports article prices. This method just decides whether to call
	 * {@link #createPrices(Catalog, Article, BMECatImportCronJob)},
	 * {@link #updatePrices(Catalog, Article, BMECatImportCronJob)} or
	 * {@link #deletePrices(Catalog, Article, BMECatImportCronJob)} according to the current catalog import mode and
	 * article mode.
	 * 
	 * @param catalog
	 *           the catalog which is currently imported
	 * @param object
	 *           the article to import prices for
	 * @param cronJob
	 *           the cronJob which executes this import
	 */
	@Override
	protected void importBMECatObject(final Catalog catalog, final AbstractValueObject object, final BMECatImportCronJob cronJob)
			throws ParseAbortException
	{
		if (object instanceof Article)
		{
			final Article article = (Article) object;
			cronJob.addToCounter(1);
			if (skipArticleOnRestart(catalog, article, cronJob))
			{
				LOG.debug("price step restart - skipping article: " + article.getSupplierAID());
				return;
			}

			switch (catalog.getTransactionMode())
			{
				case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
				{
					final String mode = article.getMode();
					if (Article.MODE_NEW.equalsIgnoreCase(mode))
					{
						createPrices(catalog, article, cronJob);
					}
					else if (Article.MODE_UPDATE.equalsIgnoreCase(mode))
					{
						updatePrices(catalog, article, cronJob);
					}
					else if (Article.MODE_DELETE.equalsIgnoreCase(mode))
					{ //NOPMD
					  // don not delete prices since products are not deleted anymore (only catalogversion is set to null
					  //deletePrices(catalog,article,cronJob);
					}
					else
					{
						throw new JaloInvalidParameterException("illegal article mode " + mode + " for article " + article, 0);
					}
				}
					break;
				case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
				{
					updatePrices(catalog, article, cronJob);
				}
					break;
				case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
				{
					if (!cronJob.isLocalizationUpdateAsPrimitive())
					{
						if (cronJob.isRunningRestart())
						{
							updatePrices(catalog, article, cronJob);
						}
						else
						{
							createPrices(catalog, article, cronJob);
						}
					}
				}
					break;
			}
		}
		else if (object instanceof ArticleToCatalogGroupMap)
		{
			throw new ParseFinishedException("ArticlePriceStep: done.");
		}
		else if (object instanceof Abort)
		{
			if (ABORTTYPE.equals(((Abort) object).getType()))
			{
				throw new BMECatParser.TestParseAbortException("");
			}
		}

	}


	/**
	 * Updates the prices for the given article. As default this method just deletes and creates the article prices by
	 * calling {@link #deletePrices(Catalog, Article, BMECatImportCronJob)} before
	 * {@link #createPrices(Catalog, Article, BMECatImportCronJob)}. Feel free to override and provide a more efficient
	 * update procedure.
	 * 
	 * @param catalog
	 *           the catalog which is currently imported
	 * @param article
	 *           the article to import prices for
	 * @param cronJob
	 *           the cronJob which executes this import
	 */
	protected void updatePrices(final Catalog catalog, final Article article, final BMECatImportCronJob cronJob)
	{
		deletePrices(catalog, article, cronJob);
		createPrices(catalog, article, cronJob);
	}

	/**
	 * Override to delete all prices for the given article.
	 * 
	 * @param catalog
	 *           the catalog which is currently imported
	 * @param article
	 *           the article to import prices for
	 * @param cronJob
	 *           the cronJob which executes this import
	 */
	protected abstract void deletePrices(Catalog catalog, Article article, BMECatImportCronJob cronJob);

	/**
	 * Override to create new prices for the given article. At this time no previous prices exist. Therefore this method
	 * does not have to do cleanup.
	 * 
	 * @param catalog
	 *           the catalog which is currently imported
	 * @param article
	 *           the article to import prices for
	 * @param cronJob
	 *           the cronJob which executes this import
	 */
	protected abstract void createPrices(Catalog catalog, Article article, BMECatImportCronJob cronJob);

}

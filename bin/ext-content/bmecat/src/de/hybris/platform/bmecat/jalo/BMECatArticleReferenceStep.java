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
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.ArticleReference;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ProductReference;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.product.Product;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;


/**
 * BMECatArticleReferenceStep
 */
public class BMECatArticleReferenceStep extends GeneratedBMECatArticleReferenceStep
{
	private static final Logger LOG = Logger.getLogger(BMECatArticleReferenceStep.class.getName());


	@Override
	protected void importBMECatObject(final Catalog catalog, final AbstractValueObject object, final BMECatImportCronJob cronJob)
			throws ParseAbortException
	{
		if (object instanceof Article)
		{
			final Article article = (Article) object;

			cronJob.addToCounter(1);

			// only get active products (=catalog version is the one we search and not null)
			final CatalogManager catalogManager = getCatalogManager();
			final Product product = catalogManager.getProductByCatalogVersion(cronJob.getCatalogVersion(), article.getSupplierAID());
			if (catalog.getTransactionMode() == BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS)
			{
				// delete references before storing new
				for (final Iterator it = catalogManager.getProductReferences(product).iterator(); it.hasNext();)
				{
					getBMECatManager().removeProductReference(cronJob, (ProductReference) it.next());
				}
			}

			for (final Iterator it = article.getArticleReferences().iterator(); it.hasNext();)
			{
				final ArticleReference articleReference = (ArticleReference) it.next();
				LOG.debug("try creating ProductReference qualifier >" + articleReference.getType() + "< source >"
						+ article.getSupplierAID() + "< or target product >" + articleReference.getArticleReference() + "<");
				/**
				 * @TODO support different products per catalog version
				 */
				Product source = getProduct(cronJob.getCatalogVersion(), article.getSupplierAID());
				if (source == null)
				{
					source = getBMECatManager().getProductByPreviousCatalogVersion(cronJob.getCatalogVersion(),
							article.getSupplierAID());
				}
				Product target = getProduct(cronJob.getCatalogVersion(), articleReference.getArticleReference());
				if (target == null)
				{
					target = getBMECatManager().getProductByPreviousCatalogVersion(cronJob.getCatalogVersion(),
							articleReference.getArticleReference());
				}
				//				final Integer quantity = articleReference.getQuantity();

				if (source == null || target == null)
				{
					LOG.warn("Error creating ProductReference qualifier >" + articleReference.getType() + "< source >"
							+ article.getSupplierAID() + "< (" + source + ") or target product >"
							+ articleReference.getArticleReference() + "< (" + target + ")");
				}
				else
				{
					LOG.debug("created ProductReference qualifier >" + articleReference.getType() + "< source >"
							+ article.getSupplierAID() + "< or target product >" + articleReference.getArticleReference() + "<");
					final ProductReference productReference = getCatalogManager().createProductReference(articleReference.getType(),
							source, target, articleReference.getQuantity());
					getBMECatManager().createProductReferenceChangeDescriptor(cronJob, productReference,
							BMECatConstants.ChangeTypes.CREATE_PRODUCTREFERENCE,
							"... created ProductReference " + productReference.toString());
				}
			}
		}
		else if (object instanceof ArticleToCatalogGroupMap)
		{
			throw new ParseFinishedException("ArticleReferenceStep: done.");
		}
	}

	@Override
	protected void initializeBMECatImport(final Catalog catalog, final BMECatImportCronJob cronJob) throws ParseAbortException
	{
		// nothing to do for localization update
		if (cronJob.isLocalizationUpdateAsPrimitive())
		{
			throw new ParseFinishedException("ArticleReferenceStep: skipped in localization update mode.");
		}
		cronJob.resetCounter();
	}

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
	 * returns true
	 */
	@Override
	protected boolean canUndo(final CronJob forCronJob)
	{
		return true;
	}

	@Override
	protected void undoStep(final CronJob jobDetail)
	{
		for (Collection range = getChanges(jobDetail, BMECatConstants.ChangeTypes.CREATE_PRODUCTREFERENCE, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(jobDetail, BMECatConstants.ChangeTypes.CREATE_PRODUCTREFERENCE, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				final ProductReferenceChangeDescriptor productReferenceChangeDescriptor = (ProductReferenceChangeDescriptor) it
						.next();
				ProductReference productReference = (ProductReference) productReferenceChangeDescriptor.getChangedItem();
				try
				{
					LOG.info("undo: processing changedesc >" + productReferenceChangeDescriptor.getDescription());
					if (productReference == null)
					{
						// if product reference had been already been deleted and recreated (exists but has now a different pk)
						final CatalogVersion catalogVersion = ((BMECatImportCronJob) jobDetail).getCatalogVersion();
						Product source = getProduct(catalogVersion, productReferenceChangeDescriptor.getSource());
						if (source == null)
						{
							source = getBMECatManager().getProductByPreviousCatalogVersion(catalogVersion,
									productReferenceChangeDescriptor.getSource());
						}
						Product target = getProduct(catalogVersion, productReferenceChangeDescriptor.getTarget());
						if (target == null)
						{
							target = getBMECatManager().getProductByPreviousCatalogVersion(catalogVersion,
									productReferenceChangeDescriptor.getTarget());
						}
						final Collection productReferences = getCatalogManager().getProductReferences(
								productReferenceChangeDescriptor.getReferenceType(), source, target,
								productReferenceChangeDescriptor.getQuantity(), true);
						if (!productReferences.isEmpty())
						{
							productReference = (ProductReference) productReferences.iterator().next();
						}
					}
					if (productReference != null && productReference.isAlive())
					{
						LOG.info("undo: deleting previously created product reference " + productReference.getSource().getCode()
								+ " -> " + (productReference.getTarget()).getCode());
						productReference.remove();
					}
					else
					{
						LOG.warn("undo: could not delete previously created product reference: " + productReference);
					}
					productReferenceChangeDescriptor.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		for (Collection range = getChanges(jobDetail, BMECatConstants.ChangeTypes.DELETE_PRODUCTREFERENCE, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(jobDetail, BMECatConstants.ChangeTypes.DELETE_PRODUCTREFERENCE, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				final ProductReferenceChangeDescriptor productReferenceChangeDescriptor = (ProductReferenceChangeDescriptor) it
						.next();
				final Product source = getProduct(((BMECatImportCronJob) jobDetail).getCatalogVersion(),
						productReferenceChangeDescriptor.getSource());
				final Product target = getProduct(((BMECatImportCronJob) jobDetail).getCatalogVersion(),
						productReferenceChangeDescriptor.getTarget());

				if (source != null & target != null)
				{
					LOG.info("undo: recreating product reference " + productReferenceChangeDescriptor.getSource() + " -> "
							+ productReferenceChangeDescriptor.getTarget());
					final ProductReference productReference = getCatalogManager().createProductReference(
							productReferenceChangeDescriptor.getReferenceType(), source, target,
							productReferenceChangeDescriptor.getQuantity());
					addChange(jobDetail, BMECatConstants.ChangeTypes.CREATE_PRODUCTREFERENCE, productReference,
							"... recreated ProductReference " + productReference.toString());
				}
				else
				{
					LOG.info("undo: error recreating product reference " + productReferenceChangeDescriptor.getSource() + " ("
							+ source + ") -> " + productReferenceChangeDescriptor.getTarget() + " (" + target + ")");
				}
				try
				{
					productReferenceChangeDescriptor.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
	}
}

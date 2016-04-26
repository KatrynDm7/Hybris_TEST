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
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.Abort;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.product.Product;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.Logger;


/**
 * Step binds products to categories as defined in BMECat tag &lt;ARTICLE_TO_CATALOGGROUP_MAP&gt;
 * 
 * 
 */
public class BMECatArticleToCategoryStep extends GeneratedBMECatArticleToCategoryStep
{
	private static final Logger LOG = Logger.getLogger(BMECatArticleToCategoryStep.class.getName());
	private LRUMap categoryCache;
	private final static String ABORTTYPE = "product2category";
	protected static final String LAST_VALID_PRODUCT2CATEGORY_KEY = "lastValidProduct2CategoryKey";
	protected static final Product2CategoryPair NONE = new Product2CategoryPair("_x_none_x_", "_x_none_x_");

	/**
	 * Imports product to category binding
	 */
	@Override
	protected void importBMECatObject(final Catalog catalogValueObject, final AbstractValueObject object,
			final BMECatImportCronJob cronJob) throws ParseAbortException
	{
		if (object instanceof ArticleToCatalogGroupMap)
		{
			cronJob.addToCounter(1);
			importArticleToCategoryMapping(cronJob, catalogValueObject, (ArticleToCatalogGroupMap) object);
		}
		else if (object instanceof Abort)
		{
			LOG.debug(" abort tag found - aborting step");
			if (ABORTTYPE.equals(((Abort) object).getType()))
			{
				throw new BMECatParser.TestParseAbortException("");
			}
		}
	}

	@Override
	public int getCompletedCount(final BMECatImportCronJob cronJob)
	{
		return cronJob.getCounter();
	}

	@Override
	public int getTotalToComplete(final BMECatImportCronJob cronJob)
	{
		return cronJob.isCatalogInfoAvailableAsPrimitive() ? cronJob.getCategoryAssignmentsCountAsPrimitive() : -1;
	}


	/**
	 * Defines that this step can be undone
	 * 
	 * @return true
	 */
	@Override
	protected boolean canUndo(final CronJob cronJob)
	{
		return true;
	}

	/**
	 * Resets last imported key product2category mapping
	 * 
	 * @param cronJob
	 */
	@Override
	protected void performStep(final CronJob cronJob) throws AbortCronJobException
	{
		try
		{
			super.performStep(cronJob);
		}
		finally
		{
			cronJob.setTransientObject(LAST_VALID_PRODUCT2CATEGORY_KEY, null);
		}
	}

	/**
	 * Creates the mapping between the specified Article and Category using the <code>Language</code> from current
	 * <code>SessionContext</code>. Restart is enabled by skipping all already processed product2category items.
	 * 
	 * @param cronJob
	 * @param catalogValueObject
	 * @param articleToCategoryValueObject
	 */
	protected void importArticleToCategoryMapping(final BMECatImportCronJob cronJob, final Catalog catalogValueObject,
			final ArticleToCatalogGroupMap articleToCategoryValueObject)
	{
		final EnumerationValue taMode = cronJob.getTransactionMode();
		final String taModeCode = taMode != null ? taMode.getCode() : null;

		if (cronJob.isRunningRestart())
		{
			// manage restarts by checking last written changedescriptor
			final Product2CategoryPair lastValidCode = getLastValidProduct2CategoryCode(cronJob, catalogValueObject);
			cronJob.addLog("SECONDRUN lastValidCode: " + (lastValidCode == null ? "null" : lastValidCode.toString()), this,
					cronJob.getDebugLogLevel());
			final Product2CategoryPair currentCode = new Product2CategoryPair(articleToCategoryValueObject);
			// skip all articles up to last valid code
			if (lastValidCode != null && !NONE.equals(lastValidCode))
			{
				// reset to normal mode if the last valid article is found
				if (lastValidCode.equals(currentCode))
				{
					cronJob.addLog(
							"SECONDRUN found last valid product2category " + currentCode.toString() + " - back to normal mode ", this,
							cronJob.getDebugLogLevel());
					cronJob.setTransientObject(LAST_VALID_PRODUCT2CATEGORY_KEY, NONE);
				}
				else
				{
					cronJob.addLog("SECONDRUN waiting for last valid article " + lastValidCode.toString() + " - skipping "
							+ currentCode.toString(), this, cronJob.getDebugLogLevel());
				}
				return; // break here
			}
		}
		else
		{
			cronJob.addLog("FIRSTRUN", this, cronJob.getDebugLogLevel());
		}
		if (BMECatConstants.Enumerations.TransactionModeEnum.T_NEW_CATALOG.equals(taModeCode))
		{
			if (cronJob.isLocalizationUpdateAsPrimitive())
			{
				LOG.debug("Won't change categories on localization update");
				return;
			}
			addProductToCategoryMapping(cronJob, articleToCategoryValueObject);
		}
		else if (BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRODUCTS.equals(taModeCode))
		{
			switch (articleToCategoryValueObject.getMode())
			{
				case BMECatConstants.MODE.NEW:
				{
					addProductToCategoryMapping(cronJob, articleToCategoryValueObject);
					break;
				}
				case BMECatConstants.MODE.DELETE:
				{
					final Product product = getProduct(cronJob.getCatalogVersion(), articleToCategoryValueObject.getArticleID());
					final Category category = getCatalogCategory(cronJob, articleToCategoryValueObject.getCatalogGroupID());
					final BMECatManager bmeCatManager = getBMECatManager();
					if (product != null && category != null)
					{
						final Collection links = getCatalogManager().getCategoryProductLinks(cronJob.getCatalogVersion(), category,
								product);
						switch (links.size())
						{
							case 0:
								if (bmeCatManager.getLinkChangeDescriptors(cronJob, this,
										BMECatConstants.ChangeTypes.DELETE_CATEGORY2PRODUCT, category.getCode(), product.getCode()).size() < 1)
								{
									LOG.warn("Could not find link which should be deleted category >" + category.getCode()
											+ "< to product >" + product.getCode() + "<");
								}
								else
								{
									LOG.debug("Link has already been deleted when deleting the product - category >" + category.getCode()
											+ "< to product >" + product.getCode() + "<");
								}
								break;
							case 1:
								bmeCatManager.removeCategoryToProductLink(cronJob, (Link) links.iterator().next());
								LOG.debug("ArticleToCatalogGroupMapping: " + articleToCategoryValueObject.getCatalogGroupID() + " -> "
										+ articleToCategoryValueObject.getArticleID() + " deleted!");
								break;
							default:
								LOG.warn("Found more than one link form category >" + category.getCode() + "< to product >"
										+ product.getCode() + "<. Taking first one.");
								bmeCatManager.removeCategoryToProductLink(cronJob, (Link) links.iterator().next());
								LOG.debug("ArticleToCatalogGroupMapping: " + articleToCategoryValueObject.getCatalogGroupID() + " -> "
										+ articleToCategoryValueObject.getArticleID() + " deleted!");
								break;
						}
					}
					else
					{
						LOG.warn("ArticleToCatalogGroupMapping: " + articleToCategoryValueObject.getCatalogGroupID() + " -> "
								+ articleToCategoryValueObject.getArticleID() + " could not be found! (P: " + product + " / C: "
								+ category);
					}
					break;
				}
				default:
					LOG.warn("Invalid update mode for product2CategoryMapping >" + articleToCategoryValueObject.getMode() + "<");
			}
		}
	}

	private void addProductToCategoryMapping(final BMECatImportCronJob cronJob,
			final ArticleToCatalogGroupMap articleToCategoryValueObject)
	{
		final Product product = getProduct(cronJob.getCatalogVersion(), articleToCategoryValueObject.getArticleID());
		final Category category = getCatalogCategory(cronJob, articleToCategoryValueObject.getCatalogGroupID());
		if (product != null && category != null)
		{
			final Integer order = articleToCategoryValueObject.getOrder();
			final LinkManager linkManager = getSession().getLinkManager();
			try
			{
				final Link link = linkManager.createLink(CategoryConstants.Relations.CATEGORYPRODUCTRELATION, category, product,
						order == null ? 0 : order.intValue(), 0);
				getBMECatManager().createCategory2ProductChangeDescriptor(cronJob, link,
						BMECatConstants.ChangeTypes.CREATE_CATEGORY2PRODUCT,
						"created category2product relation. category >" + category.getCode() + "< product >" + product.getCode() + "<");
			}
			catch (final ConsistencyCheckException e)
			{
				e.printStackTrace();
			}
			LOG.debug("ArticleToCatalogGroupMapping: " + articleToCategoryValueObject.getCatalogGroupID() + " -> "
					+ articleToCategoryValueObject.getArticleID() + " done!");
		}
		else
		{
			LOG.warn("ArticleToCatalogGroupMapping: " + articleToCategoryValueObject.getArticleID() + " -> "
					+ articleToCategoryValueObject.getCatalogGroupID() + " skipped! (P: " + product + " / C: " + category);
		}
	}

	@SuppressWarnings("PMD.TooFewBranchesForASwitchStatement")
	protected Product2CategoryPair getLastValidProduct2CategoryCode(final BMECatImportCronJob cronJob,
			final Catalog catalogValueObj)
	{
		if (!cronJob.isChangeRecordingEnabledAsPrimitive())
		{
			return NONE;
		}
		Product2CategoryPair lastValidProduct2CategoryCode = (Product2CategoryPair) cronJob
				.getTransientObject(LAST_VALID_PRODUCT2CATEGORY_KEY);
		if (lastValidProduct2CategoryCode == null)
		{
			ChangeDescriptor lastOne = null;
			switch (catalogValueObj.getTransactionMode())
			{
				case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
					lastOne = getMostRecentChange(cronJob, BMECatConstants.ChangeTypes.CREATE_CATEGORY2PRODUCT);
					break;
				case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
					final ChangeDescriptor createDesc = getMostRecentChange(cronJob,
							BMECatConstants.ChangeTypes.CREATE_CATEGORY2PRODUCT);
					final ChangeDescriptor deleteDesc = getMostRecentChange(cronJob,
							BMECatConstants.ChangeTypes.DELETE_CATEGORY2PRODUCT);
					lastOne = getMostRecentChangeDescriptor(createDesc, deleteDesc);
					break;
			}

			if (lastOne != null)
			{
				final Category2ProductChangeDescriptor category2ProductChangeDescriptor = (Category2ProductChangeDescriptor) lastOne;
				final Link category2productLink = (Link) category2ProductChangeDescriptor.getChangedItem();
				Category category = (Category) category2productLink.getSource();
				final CatalogVersion catalogVersion = cronJob.getCatalogVersion();
				if (category == null)
				{
					category = catalogVersion.getCategory(category2ProductChangeDescriptor.getSource());
				}
				Product product = (Product) category2productLink.getTarget();
				if (product == null)
				{
					product = getProduct(catalogVersion, category2ProductChangeDescriptor.getTarget());
					if (product == null)
					{
						product = getBMECatManager().getProductByPreviousCatalogVersion(catalogVersion,
								category2ProductChangeDescriptor.getTarget());
					}
				}
				lastValidProduct2CategoryCode = new Product2CategoryPair(product.getCode(), category.getCode());
			}
			cronJob.setTransientObject(LAST_VALID_PRODUCT2CATEGORY_KEY,
					lastValidProduct2CategoryCode != null ? lastValidProduct2CategoryCode : NONE);
		}
		return NONE.equals(lastValidProduct2CategoryCode) ? null : lastValidProduct2CategoryCode;
	}

	/**
	 * Returns the most recent of two ChangeDescriptors / of one of the two is null returns the other
	 * 
	 * @param one
	 * @param other
	 * @return most recent changeDescriptor or null
	 */
	private ChangeDescriptor getMostRecentChangeDescriptor(final ChangeDescriptor one, final ChangeDescriptor other)
	{
		if (one == null)
		{
			return other;
		}
		if (other == null)
		{
			return one;
		}
		return one.getSequenceNumberAsPrimitive() > other.getSequenceNumberAsPrimitive() ? one : other;
	}

	/**
	 * Undo all changes done by cronjob
	 */
	@Override
	protected void undoStep(final CronJob cronJob)
	{
		LOG.debug("undo: changedescriptors CREATE_CATEGORY2PRODUCT");
		// undo removal product from catalog version
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_CATEGORY2PRODUCT, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_CATEGORY2PRODUCT, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				try
				{
					final Category2ProductChangeDescriptor category2ProductChangeDescriptor = (Category2ProductChangeDescriptor) it
							.next();
					Link category2productLink = (Link) category2ProductChangeDescriptor.getChangedItem();
					// in case it has already been recreated (different pk)
					if (category2productLink == null)
					{
						final CatalogVersion catalogVersion = ((BMECatImportCronJob) cronJob).getCatalogVersion();
						// get product by source
						Category category = null;
						if (category2ProductChangeDescriptor.getSource() != null)
						{
							category = catalogVersion.getCategory(category2ProductChangeDescriptor.getSource());
						}
						// get product by target
						Product product = null;
						if (category2ProductChangeDescriptor.getTarget() != null)
						{
							product = getProduct(catalogVersion, category2ProductChangeDescriptor.getTarget());
							if (product == null)
							{
								product = getBMECatManager().getProductByPreviousCatalogVersion(catalogVersion,
										category2ProductChangeDescriptor.getTarget());
							}
						}
						//						final Integer position = cd.getPosition();
						if (category != null && product != null)
						{
							final Collection links = getSession().getLinkManager().getLinks(
									CategoryConstants.Relations.CATEGORYPRODUCTRELATION, category, product);
							if (!links.isEmpty())
							{
								category2productLink = (Link) links.iterator().next();
							}
						}
					}
					if (category2productLink != null)
					{
						LOG.info("undo: removing previously created link. changedescriptor: "
								+ category2ProductChangeDescriptor.getDescription());
						category2productLink.remove();
					}
					category2ProductChangeDescriptor.remove();
				}
				catch (final JaloBusinessException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}


		LOG.debug("undo: changedescriptors DELETE_CATEGORY2PRODUCT");
		// undo removal product from catalog version
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_CATEGORY2PRODUCT, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_CATEGORY2PRODUCT, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				try
				{
					final Category2ProductChangeDescriptor category2ProductChangeDescriptor = (Category2ProductChangeDescriptor) it
							.next();
					//					final Link category2productLink = (Link) cd.getChangedItem();
					final CatalogVersion catalogVersion = ((BMECatImportCronJob) cronJob).getCatalogVersion();

					final Category category = catalogVersion.getCategory(category2ProductChangeDescriptor.getSource());
					Product product = getProduct(catalogVersion, category2ProductChangeDescriptor.getTarget());
					if (product == null)
					{
						product = getBMECatManager().getProductByPreviousCatalogVersion(catalogVersion,
								category2ProductChangeDescriptor.getTarget());
					}
					final Integer position = category2ProductChangeDescriptor.getPosition();

					category2ProductChangeDescriptor.remove();
					if (product == null || category == null)
					{
						LOG.warn("undo: failed to retrieve product (" + product + ") or category (" + category
								+ ") for category2product change descriptor: " + category2ProductChangeDescriptor.getDescription());
					}
					else
					{
						LOG.info("undo: recreating link from category >" + category.getCode() + "< to product >" + product.getCode());
						getSession().getLinkManager().createLink(CategoryConstants.Relations.CATEGORYPRODUCTRELATION, category,
								product, position == null ? 0 : position.intValue(), 0);
					}
				}
				catch (final JaloBusinessException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
	}

	/**
	 * Getting the catalog category
	 * 
	 * @param cronJob
	 * @param code
	 * @return the catalog category
	 */
	protected Category getCatalogCategory(final BMECatImportCronJob cronJob, final String code)
	{
		if (categoryCache == null)
		{
			categoryCache = new LRUMap();
		}
		Category category = (Category) categoryCache.get(code);
		if (category == null)
		{
			category = getCatalogManager().getCatalogCategory(cronJob.getCatalogVersion(), code);
			if (category != null)
			{
				categoryCache.put(code, category);
			}
		}
		return category;
	}

	/**
	 * Initialzes the CategoryCache map.
	 */
	@Override
	protected void initializeBMECatImport(final Catalog catalog, final BMECatImportCronJob cronJob)
	{
		categoryCache = new LRUMap(1000);
		System.out.println("resting assignmentCount = 0");
		cronJob.resetCounter();
	}

	/**
	 * BMECatArticleToCategoryStep.Product2CategoryPair
	 * 
	 */
	public static class Product2CategoryPair
	{
		private final String productCode;
		private final String categoryCode;

		/**
		 * convenience constructor assuming entry.key is ReferenceSystem, entry.value is feature
		 */
		public Product2CategoryPair(final String productCode, final String categoryCode)
		{
			this.productCode = productCode;
			this.categoryCode = categoryCode;
		}

		public Product2CategoryPair(final ArticleToCatalogGroupMap articleToCatalogGroupMap)
		{
			this.productCode = articleToCatalogGroupMap.getArticleID();
			this.categoryCode = articleToCatalogGroupMap.getCatalogGroupID();
		}

		/**
		 * @return Returns the categoryCode.
		 */
		public String getCategoryCode()
		{
			return categoryCode;
		}

		/**
		 * @return Returns the productCode.
		 */
		public String getProductCode()
		{
			return productCode;
		}


		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj)
		{
			if (!(obj instanceof Product2CategoryPair))
			{
				return false;
			}
			final Product2CategoryPair pair2 = (Product2CategoryPair) obj;
			return (getProductCode().equals(pair2.getProductCode()) && getCategoryCode().equals(pair2.getCategoryCode()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "product2category: product >" + getProductCode() + "< category >" + getCategoryCode() + "<";
		}
	}
}

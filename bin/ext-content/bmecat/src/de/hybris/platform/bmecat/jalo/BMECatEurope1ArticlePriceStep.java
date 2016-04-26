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

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.ArticlePrice;
import de.hybris.platform.bmecat.parser.ArticlePriceDetails;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PDTRow;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.jalo.TaxRow;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.DateRange;
import de.hybris.platform.util.StandardDateRange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * Imports bmecat article prices into europe1 price factory
 */
public class BMECatEurope1ArticlePriceStep extends GeneratedBMECatEurope1ArticlePriceStep
{
	private static final Logger LOGGER = Logger.getLogger(BMECatEurope1ArticlePriceStep.class.getName());
	protected static final String LAST_VALID_PRICE_ARTICLE_ID = "lastValidPriceArticleCode";
	protected static final String NONE = "__none__";

	@Override
	protected boolean canUndo(final CronJob forSchedule)
	{
		return true;
	}

	@Override
	protected void undoStep(final CronJob cronJob)
	{
		LOGGER.debug("undo: changedescriptors CREATE_PRODUCT_PRICE");
		// remove variant products
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT_PRICE, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT_PRICE, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				undoCreate((BMECatImportCronJob) cronJob, (PriceChangeDescriptor) it.next());
			}
		}
		LOGGER.debug("undo: changedescriptors DELETE_PRODUCT_PRICE");
		// remove variant products
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_PRODUCT_PRICE, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_PRODUCT_PRICE, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				undoRemove((BMECatImportCronJob) cronJob, (PriceChangeDescriptor) it.next());
			}
		}
		super.undoStep(cronJob);
	}

	@Override
	protected void performStep(final CronJob cronJob) throws AbortCronJobException
	{
		try
		{
			super.performStep(cronJob);
		}
		finally
		{
			cronJob.setTransientObject(LAST_VALID_PRICE_ARTICLE_ID, null);
		}
	}

	@Override
	protected boolean skipArticleOnRestart(final Catalog catalog, final Article article, final BMECatImportCronJob cronJob)
	{
		if (!cronJob.isRunningRestart())
		{
			return false;
		}
		final String lastValidCode = getLastValidArticleCode(cronJob, catalog);
		//log.debug( cronJob, "RESTART: lastValidCode: " + lastValidCode);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("RESTART: lastValidCode: " + lastValidCode);
		}

		// skip all articles up to last valid code
		if (lastValidCode != null && !NONE.equals(lastValidCode))
		{
			// reset to normal mode if the last valid article is found
			if (lastValidCode.equals(article.getSupplierAID()))
			{
				//log.info(cronJob, "RESTART: found last valid article "+article.getSupplierAID()+" - back to normal mode ");
				LOGGER.info("RESTART: found last valid article " + article.getSupplierAID() + " - back to normal mode ");
				cronJob.setTransientObject(LAST_VALID_PRICE_ARTICLE_ID, NONE);
				// undo Product which might have been partly imported (but not completely)
				final Product lastProduct = getProduct(cronJob.getCatalogVersion(), article.getSupplierAID());
				if (lastProduct != null)
				{
					//log.info(cronJob, "RESTART: undo prices of last imported article "+article.getSupplierAID());
					if (LOGGER.isInfoEnabled())
					{
						LOGGER.info("RESTART: undo prices of last imported article " + article.getSupplierAID());
					}
					undoProduct(cronJob, lastProduct);
				}
				return false;
			}
			else
			{
				//log.info(cronJob, "RESTART: waiting for last valid article "+lastValidCode+" - skipping "+article.getSupplierAID()); 
				if (LOGGER.isInfoEnabled())
				{
					LOGGER.info("RESTART: waiting for last valid article " + lastValidCode + " - skipping " + article.getSupplierAID());
				}
				return true;
			}
		}
		return false;
	}

	protected String getLastValidArticleCode(final BMECatImportCronJob cronJob, final Catalog catalog)
	{
		if (!cronJob.isChangeRecordingEnabledAsPrimitive())
		{
			return NONE;
		}
		String lastValidArticleCode = (String) cronJob.getTransientObject(LAST_VALID_PRICE_ARTICLE_ID);
		if (lastValidArticleCode == null)
		{
			// gets the code for last article for which all prices have been created
			final ComposedType priceChangeDesc = getSession().getTypeManager().getComposedType(PriceChangeDescriptor.class);
			final PriceChangeDescriptor lastOne = (PriceChangeDescriptor) getBMECatManager().getMostRecentChange(cronJob, this,
					null, priceChangeDesc);
			final String productCode = lastOne != null ? lastOne.getPriceCopy().getProductCode() : null;
			cronJob.setTransientObject(LAST_VALID_PRICE_ARTICLE_ID, productCode != null ? lastValidArticleCode = productCode : NONE);
		}
		return NONE.equals(lastValidArticleCode) ? null : lastValidArticleCode;
	}

	/**
	 * Deletes all price and tax rows which are directly assigned to the article.
	 * <p>
	 * <b> This includes all prices and taxes - not just the ones created by BMECat import! </b>
	 * </p>
	 */
	@Override
	protected void deletePrices(final Catalog catalog, final Article article, final BMECatImportCronJob cronJob)
	{
		Product prod = getProduct(cronJob.getCatalogVersion(), article.getSupplierAID());
		if (prod == null)
		{
			// product may have been already deleted in the article step
			if ((prod = getBMECatManager().getProductByPreviousCatalogVersion(cronJob.getCatalogVersion(), article.getSupplierAID())) == null)
			{
				throw new JaloInvalidParameterException("cannot find product for BmeCat article " + article, 0);
			}
		}
		try
		{
			/*
			 * remove price rows
			 */
			final Map values = new HashMap();
			values.put("prod", prod);
			final Collection priceRows = getSession()
					.getFlexibleSearch()
					.search(
							"SELECT {" + Item.PK + "} FROM {" + getSession().getTypeManager().getComposedType(PriceRow.class).getCode()
									+ "} " + "WHERE {" + PDTRow.PRODUCT + "}=?prod", values, Collections.singletonList(PriceRow.class),
							true, true, 0, -1).getResult();
			for (final Iterator it = priceRows.iterator(); it.hasNext();)
			{
				final PriceRow priceRow = (PriceRow) it.next();
				/* final PriceChangeDescriptor changeDesc = */getBMECatManager().createPriceChangeDescriptor(
						cronJob,
						BMECatConstants.ChangeTypes.DELETE_PRODUCT_PRICE,
						priceRow,
						"change descriptor for deleting price >" + Double.toString(priceRow.getPrice().doubleValue())
								+ "< for product >" + prod.getCode() + "<");
				priceRow.remove();
			}
			/*
			 * remove tax rows
			 */
			Item.removeItemCollection(getSession()
					.getFlexibleSearch()
					.search(
							"SELECT {" + Item.PK + "} FROM {" + getSession().getTypeManager().getComposedType(TaxRow.class).getCode()
									+ "} " + "WHERE {" + PDTRow.PRODUCT + "}=?prod", values, Collections.singletonList(TaxRow.class),
							true, true, 0, -1).getResult());
		}
		catch (final ConsistencyCheckException e)
		{
			throw new JaloSystemException(e);
		}
	}

	private static final long YEAR = 1000L * 60L * 60L * 24L * 365L;

	/**
	 * Creates a property date range object for a given article detail range. This treats missing start or end dates as
	 * follows:
	 * <ul>
	 * <li>both missing: no daterange is created</li>
	 * <li>start is missing: start = 01.01.1970</li>
	 * <li>end is missing: end = Max( start, today ) + 1 year</li>
	 * </ul>
	 * 
	 * @param details
	 *           the bmecat price range
	 * @return a europe1 compatible daterange
	 */
	protected DateRange convertDateRange(final ArticlePriceDetails details)
	{
		Date dateStart = details.getStartDate();
		Date dateEnd = details.getEndDate();
		final boolean unlimited = dateStart == null && dateEnd == null;
		if (!unlimited)
		{
			// price starts
			if (dateStart == null)
			{
				dateStart = new Date(0);
			}
			if (dateEnd == null)
			{
				dateEnd = new Date(Math.max(dateStart.getTime(), System.currentTimeMillis()) + (YEAR * 10));
			}
		}
		return unlimited ? null : new StandardDateRange(dateStart, dateEnd);
	}

	/**
	 * Determines wether a price is net or gross. Any price type other than 'gros_list' and 'nrp' is considered to be a
	 * net price.
	 * 
	 * @param price
	 *           the single price
	 */
	private boolean isNet(final ArticlePrice price)
	{
		return !ArticlePrice.TYPE_NRP.equalsIgnoreCase(price.getPriceType())
				&& !ArticlePrice.TYPE_GROS_LIST.equalsIgnoreCase(price.getPriceType());
	}

	/**
	 * Creates Europe1 prices according to the following rules.
	 * <ul>
	 * <li>daterange is defined by {@link #convertDateRange(ArticlePriceDetails)}</li>
	 * <li>net flag: determines wether a price is net or gross</li>
	 * <li>article (group): prices are assigned to the article's product directly (no groups!)</li>
	 * <li>the price type and the countries are mapped to user price groups ( see
	 * {@link #getPriceGroupKey(de.hybris.platform.bmecat.parser.Company, ArticlePrice)} )</li>
	 * <li>the price factor is calculated into the price row's value</li>
	 * <li>the article price quantity defines the unit factor</li>
	 * <li>the article min quantity or the price's lower bound define the min quantity of the row</li>
	 * <li>the currency is found or created by its iso code</li>
	 * <li>the article order unit code is used for finding or creating the price unit</li>
	 * </ul>
	 * Since all prices belong to a user price group is is necessary to assing each customer the correct price group.
	 * Otherwise no price will be found.
	 * <p>
	 * Taxes are treated somehow special:
	 * <ul>
	 * <li>if all prices have the same tax value the product is put into a (product) tax group owning this value</li>
	 * <li>if different tax values exist a own tax row for <b>each price</b> is created to make sure each separate user
	 * groups gets its own tax. this achieved by assigning user tax groups named equal to the user price groups created
	 * for the price.</li>
	 * </ul>
	 * Again it is necessary to assign the corrent tax group to all customers. Otherwise no taxes will be found.
	 * 
	 */
	@Override
	protected void createPrices(final Catalog catalog, final Article article, final BMECatImportCronJob cronJob)
	{
		final Product prod = getProduct(cronJob.getCatalogVersion(), article.getSupplierAID());
		LOGGER.debug("creating price for product: " + article.getSupplierAID());
		if (prod == null)
		{
			throw new JaloInvalidParameterException("cannot find product for article " + article, 0);
		}
		final Europe1PriceFactory europe1PriceFactory = (Europe1PriceFactory) getSession().getExtensionManager().getExtension(
				Europe1Constants.EXTENSIONNAME);

		final Map priceTypeMappings = cronJob.getAllPriceTypeMappings();
		/*
		 * calculate unit factor and price correction factor if needed
		 */
		int priceCorrectionFactor = 1;
		int unitFactor = 1;
		if (article.getPriceQuantity() != null && article.getPriceQuantity().doubleValue() != 0.0)
		{
			final BigDecimal priceQuantity = new BigDecimal(article.getPriceQuantity().doubleValue());
			priceCorrectionFactor = (int) Math.pow(10, priceQuantity.scale());
			unitFactor = priceQuantity.unscaledValue().intValue();
		}
		final long articleMinQuantity = article.getMinOrderQuantity() != null ? article.getMinOrderQuantity().longValue() : 1;
		Unit unit = cronJob.getOrCreateUnit(article.getOrderUnit());
		if (unit == null)
		{
			unit = prod.getUnit();
		}
		/*
		 * process all price (date-)ranges
		 */
		int createdPrices = 0;
		for (final Iterator it = article.getArticlePriceDetails().iterator(); it.hasNext();)
		{
			final ArticlePriceDetails priceRange = (ArticlePriceDetails) it.next();

			final DateRange dateRange = convertDateRange(priceRange);
			//final Map taxPriceMap = new HashMap();

			//			final Map predefinedTaxMappings = cronJob.getAllTaxTypeMappings();

			for (final Iterator it2 = priceRange.getPrices().iterator(); it2.hasNext();)
			{
				final ArticlePrice articlePrice = (ArticlePrice) it2.next();
				final String type = articlePrice.getPriceType();


				final EnumerationValue priceType = getPriceTypeEnum(type);
				if (priceType != null && priceTypeMappings.containsKey(priceType))
				{
					final boolean net = isNet(articlePrice);
					final double priceValue = articlePrice.getAmount() * articlePrice.getPriceFactor() * priceCorrectionFactor;
					final long minQuantity = Math.max(articleMinQuantity, (long) articlePrice.getLowerBound());
					final Currency currency = cronJob.getOrCreateCurrency(catalog, articlePrice.getCurrency());
					if (currency == null)
					{
						throw new JaloSystemException(null, "Neither a default catalog currency nor"
								+ " a currency at the price for Article >" + article.getSupplierAID()
								+ "< are defined! The catalog is not valid according to BMECat Specification.", 0);
					}
					/*
					 * convert bounds and price
					 */
					try
					{
						/*
						 * create price
						 */
						final PriceRow priceRow = europe1PriceFactory.createPriceRow(
								prod, // for this product only 
								null, // no PPG
								null, // no single customer 
								//							getOrCreateCustomerPriceGroup(e1,catalog.getBuyer(),price),
								(EnumerationValue) priceTypeMappings.get(priceType), minQuantity, currency, unit, unitFactor, net,
								dateRange, priceValue);
						createdPrices++;
						getBMECatManager().createPriceChangeDescriptor(cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT_PRICE,
								priceRow, "created price" + createdPrices + " for product: " + prod.getCode());
						LOGGER.debug("created price row: " + priceRow.getPrice() + " " + priceRow.getUnit() + " date range: "
								+ (dateRange == null ? "null" : dateRange.toString()));
						/*
						 * collect taxes for later tax row creation
						 */
						// final Double val = new Double( articlePrice.getTax() );
						// map value to all types 
						// Set prices = (Set)taxPriceMap.get( val );
						// if( prices == null ) taxPriceMap.put( val, prices = new HashSet());
						//		prices.add( articlePrice );
					}
					catch (final JaloPriceFactoryException e2)
					{
						throw new JaloSystemException(e2);
					}
				}
			}
			/*
			 * create taxes if necessary
			 */
			EnumerationValue enumerationValue = null;

			if (cronJob.getAllTaxTypeMappings() != null)
			{
				final Iterator entry = cronJob.getAllTaxTypeMappings().entrySet().iterator();
				if (entry.hasNext())
				{
					final Map.Entry mapEntry = (Map.Entry) entry.next();

					enumerationValue = (EnumerationValue) mapEntry.getValue();

					if (enumerationValue != null)
					{
						try
						{
							prod.setAttribute(Europe1Constants.PARAMS.PTG, enumerationValue);
						}
						catch (final JaloBusinessException e3)
						{
							throw new JaloSystemException(e3);
						}
					}
				}
			}
		}
	}

	private EnumerationValue getPriceTypeEnum(final String code)
	{
		final EnumerationManager enumerationManager = JaloSession.getCurrentSession().getEnumerationManager();
		final EnumerationType bmeCatPricesEnumType = enumerationManager.getEnumerationType(BMECatConstants.TC.BMECATPRICETYPEENUM);
		try
		{
			return enumerationManager.getEnumerationValue(bmeCatPricesEnumType, code);
		}
		catch (final JaloInvalidParameterException e)
		{
			e.printStackTrace();
		}
		catch (final JaloItemNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected EnumerationValue getOrCreateCustomerPriceGroup(final Europe1PriceFactory europe1PriceFactory,
			final de.hybris.platform.bmecat.parser.Company buyer, final ArticlePrice price)
	{
		try
		{
			final String key = getPriceGroupKey(buyer, price);
			final EnumerationValue upg = europe1PriceFactory.getUserPriceGroup(key);
			return upg != null ? upg : europe1PriceFactory.createUserPriceGroup(key);
		}
		catch (final ConsistencyCheckException e)
		{
			throw new JaloSystemException(e);
		}
	}

	protected static final String BMECAT_TAX_CODE = "ImportedBMECatTax";

	protected Tax getOrCreateBMECatTaxItem()
	{
		return getOrCreateBMECatTaxItem(BMECAT_TAX_CODE);
	}

	protected Tax getOrCreateBMECatTaxItem(final String code)
	{
		try
		{
			final OrderManager orderManager = getSession().getOrderManager();
			final Tax taxItem = orderManager.getTaxByCode(code);
			return taxItem != null ? taxItem : orderManager.createTax(code);
		}
		catch (final ConsistencyCheckException e)
		{
			throw new JaloSystemException(e);
		}
	}

	protected EnumerationValue getOrCreateCustomerTaxGroup(final Europe1PriceFactory europe1PriceFactory,
			final de.hybris.platform.bmecat.parser.Company buyer, final ArticlePrice price)
	{
		try
		{
			final String key = getPriceGroupKey(buyer, price);
			final EnumerationValue upg = europe1PriceFactory.getUserTaxGroup(key);
			return upg != null ? upg : europe1PriceFactory.createUserTaxGroup(key);
		}
		catch (final ConsistencyCheckException e)
		{
			throw new JaloSystemException(e);
		}
	}

	protected String getPriceGroupKey(final de.hybris.platform.bmecat.parser.Company buyer, final ArticlePrice price)
	{
		final String priceType = price.getPriceType();
		final StringBuilder key = new StringBuilder();
		// global product price groups
		if (ArticlePrice.TYPE_NET_LIST.equalsIgnoreCase(priceType) || ArticlePrice.TYPE_GROS_LIST.equalsIgnoreCase(priceType)
				|| ArticlePrice.TYPE_NRP.equalsIgnoreCase(priceType))
		{
			key.append(priceType);
		}
		// customer specific product price groups
		else
		{
			key.append(buyer != null ? buyer.getName().trim().replace(' ', '_') + "_" : "").append(priceType);
		}
		/*
		 * append countries if present
		 */
		final List countries = new ArrayList(price.getTerritories());
		if (!countries.isEmpty())
		{
			Collections.sort(countries);
			for (final Iterator it = countries.iterator(); it.hasNext();)
			{
				key.append("_").append((String) it.next());
			}
		}
		return key.toString();
	}


	protected void undoProduct(final CronJob cronJob, final Product product)
	{
		for (final Iterator it = getBMECatManager().getPriceChangeDescriptors(getSession().getSessionContext(), cronJob, this,
				BMECatConstants.ChangeTypes.CREATE_PRODUCT_PRICE, product.getCode()).iterator(); it.hasNext();)
		{
			undoCreate((BMECatImportCronJob) cronJob, (PriceChangeDescriptor) it.next());
		}
		final Collection removeChangeDescs = getBMECatManager().getPriceChangeDescriptors(getSession().getSessionContext(),
				cronJob, this, BMECatConstants.ChangeTypes.DELETE_PRODUCT_PRICE, product.getCode());
		for (final Iterator it = removeChangeDescs.iterator(); it.hasNext();)
		{
			undoRemove((BMECatImportCronJob) cronJob, (PriceChangeDescriptor) it.next());
		}
	}

	protected void undoCreate(final BMECatImportCronJob cronJob, final PriceChangeDescriptor changeDesc)
	{
		PriceRow priceRow = changeDesc.getPriceRow();
		/** if pricerow has been recreated it has a different pk but the same content - find row by content */
		if (priceRow == null)
		{
			priceRow = getBMECatManager().getPriceRowByCopy(cronJob.getCatalogVersion(), changeDesc.getPriceCopy());
		}
		String desc = "Could not find (and not delete) pricerow";
		if (priceRow != null)
		{
			try
			{
				desc = "pricerow deleted.";
				priceRow.remove();
			}
			catch (final ConsistencyCheckException x)
			{
				LOGGER.warn(x.getMessage());
			}
		}
		LOGGER.debug("UndoCreate:" + desc + " - changedescriptor >" + changeDesc.toString() + "< ");
		try
		{
			changeDesc.remove();
		}
		catch (final ConsistencyCheckException x)
		{
			LOGGER.warn(x.getMessage());
		}
	}

	protected void undoRemove(final BMECatImportCronJob cronJob, final PriceChangeDescriptor changeDesc)
	{
		if (changeDesc.getPriceCopy() == null)
		{
			LOGGER.warn("UndoRemove: PriceCopy is null. Could not process desc >" + changeDesc.toString() + "< ");
			return;
		}
		/* final PriceRow priceRow = */getBMECatManager().createPriceRow(cronJob.getCatalogVersion(), changeDesc.getPriceCopy());
		LOGGER.debug("UndoRemove: Recreated pricerow. - changedescriptor >" + changeDesc.toString() + "< ");
		try
		{
			changeDesc.remove();
		}
		catch (final ConsistencyCheckException x)
		{
			LOGGER.warn(x.getMessage());
		}
	}

	/**
	 * This message logs to both standard logging and cronjob log
	 * 
	 * @param cronJob
	 * @param message
	 */
	protected void log(final CronJob cronJob, final String message)
	{
		cronJob.addLog(message, this, cronJob.getDebugLogLevel());
		LOGGER.info(message);
	}
}

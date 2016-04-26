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
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.ArticleFeatures;
import de.hybris.platform.bmecat.parser.ArticlePrice;
import de.hybris.platform.bmecat.parser.ArticlePriceDetails;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.bmecat.parser.CatalogGroupSystem;
import de.hybris.platform.bmecat.parser.CatalogStructure;
import de.hybris.platform.bmecat.parser.Feature;
import de.hybris.platform.bmecat.parser.Mime;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.product.Unit;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * This step gathers information about a BMECat file. It stores this information to the cronjob. When completed the step
 * stops a running batch job, giving users the possibility to review the informationa and change setting / mappings
 * before conituing the BMECat import.
 * 
 * 
 */
public class BMECatInfoStep extends GeneratedBMECatInfoStep
{
	private static final Logger LOG = Logger.getLogger(BMECatInfoStep.class.getName()); //NOPMD

	@Override
	protected boolean canUndo(final CronJob forSchedule)
	{
		return true;
	}

	/**
	 * Does nothing right now.
	 */
	@Override
	protected void undoStep(final CronJob forSchedule)
	{
		// DOCTODO Document reason, why this block is empty
	}

	/**
	 * 
	 */
	@Override
	protected void performStep(final CronJob forSchedule) throws AbortCronJobException
	{
		try
		{
			this.runing = true;
			this.finished = false;
			super.performStep(forSchedule);
		}
		finally
		{
			final BMECatImportCronJob cronJob = (BMECatImportCronJob) forSchedule;
			saveArticleCount(cronJob);
			saveCategoryCount(cronJob);
			saveMimeCount(cronJob);
			saveKeywordCount(cronJob);
			saveCategoryAssignmentCount(cronJob);
			cronJob.setCatalogInfoAvailable(true);
			cronJob.setPaused();
			this.runing = false;
			this.finished = true;
		}
	}

	private boolean runing = false;

	private boolean finished = false;

	private int position = 0;

	// private static final int HEADER_DONE = 1;
	private static final int CATEGORIES_DONE = 2;

	private static final int ARTICLES_DONE = 3;

	@SuppressWarnings("PMD.TooFewBranchesForASwitchStatement")
	public int getCompletionStatus()
	{
		if (finished)
		{
			return 100;
		}
		else if (runing)
		{
			int ret = 5; // 5% for starting at all ;)
			switch (position)
			{
				case ARTICLES_DONE:
					ret += 25;
				case CATEGORIES_DONE:
					ret += 20;
					// case HEADER_DONE: ret += 10;
			}
			return ret;
		}
		else
		{
			return 0;
		}
	}

	protected void saveCategoryAssignmentCount(final BMECatImportCronJob cronJob)
	{
		cronJob.setCategoryAssignmentsCount((Integer) cronJob.getTransientObject(BMECatImportCronJob.CATEGORYASSIGNMENTSCOUNT));
		cronJob.setTransientObject(BMECatImportCronJob.CATEGORYASSIGNMENTSCOUNT, null);
	}

	protected void addToCategoryAssignmentCount(final BMECatImportCronJob cronJob, final int add)
	{
		final Integer integer = (Integer) cronJob.getTransientObject(BMECatImportCronJob.CATEGORYASSIGNMENTSCOUNT);
		cronJob.setTransientObject(BMECatImportCronJob.CATEGORYASSIGNMENTSCOUNT,
				Integer.valueOf(integer != null ? integer.intValue() + add : add));
	}

	protected void saveArticleCount(final BMECatImportCronJob cronJob)
	{
		cronJob.setArticleCount((Integer) cronJob.getTransientObject(BMECatImportCronJob.ARTICLECOUNT));
		cronJob.setTransientObject(BMECatImportCronJob.ARTICLECOUNT, null);
	}

	protected void addToArticleCount(final BMECatImportCronJob cronJob, final int add)
	{
		final Integer integer = (Integer) cronJob.getTransientObject(BMECatImportCronJob.ARTICLECOUNT);
		cronJob.setTransientObject(BMECatImportCronJob.ARTICLECOUNT,
				Integer.valueOf(integer != null ? integer.intValue() + add : add));
	}

	protected void saveKeywordCount(final BMECatImportCronJob cronJob)
	{
		cronJob.setKeywordCount((Integer) cronJob.getTransientObject(BMECatImportCronJob.KEYWORDCOUNT));
		cronJob.setTransientObject(BMECatImportCronJob.KEYWORDCOUNT, null);
		cronJob.setTransientObject("keywordCodes", null);
	}

	protected void addKeywords(final BMECatImportCronJob cronJob, final Collection keywords)
	{
		if (keywords != null && !keywords.isEmpty())
		{
			Set keyCodes = (Set) cronJob.getTransientObject("keywordCodes");
			if (keyCodes == null)
			{
				cronJob.setTransientObject("keywordCodes", keyCodes = new HashSet());
			}
			boolean changed = false;
			for (final Iterator it = keywords.iterator(); it.hasNext();)
			{
				changed |= keyCodes.add(it.next());
			}
			if (changed)
			{
				cronJob.setTransientObject(BMECatImportCronJob.KEYWORDCOUNT, Integer.valueOf(keyCodes.size()));
			}
		}
	}

	protected void saveMimeCount(final BMECatImportCronJob cronJob)
	{
		cronJob.setMimeCount((Integer) cronJob.getTransientObject(BMECatImportCronJob.MIMECOUNT));
		cronJob.setTransientObject(BMECatImportCronJob.MIMECOUNT, null);
		cronJob.setTransientObject("mimeCodes", null);
	}

	public static int getMimeCount(final BMECatImportCronJob cronJob)
	{
		final Integer integer = (Integer) cronJob.getTransientObject(BMECatImportCronJob.MIMECOUNT);
		return integer != null ? integer.intValue() : 0;
	}

	public static void addMimes(final BMECatImportCronJob cronJob, final Collection mimes)
	{
		if (mimes != null && !mimes.isEmpty())
		{
			Set mimeCodes = (Set) cronJob.getTransientObject("mimeCodes");
			if (mimeCodes == null)
			{
				cronJob.setTransientObject("mimeCodes", mimeCodes = new HashSet());
			}
			boolean changed = false;
			for (final Iterator it = mimes.iterator(); it.hasNext();)
			{
				final Mime mime = (Mime) it.next();
				changed |= mimeCodes.add(mime.getSource());
			}
			if (changed)
			{
				cronJob.setTransientObject(BMECatImportCronJob.MIMECOUNT, Integer.valueOf(mimeCodes.size()));
			}
		}
	}

	protected void saveCategoryCount(final BMECatImportCronJob cronJob)
	{
		cronJob.setCategoryCount((Integer) cronJob.getTransientObject(BMECatImportCronJob.CATEGORYCOUNT));
		cronJob.setTransientObject(BMECatImportCronJob.CATEGORYCOUNT, null);
	}

	protected void addToCategoryCount(final BMECatImportCronJob cronJob, final int add)
	{
		// this.position = Math.max( this.position, HEADER_DONE );
		final Integer integer = (Integer) cronJob.getTransientObject(BMECatImportCronJob.CATEGORYCOUNT);
		cronJob.setTransientObject(BMECatImportCronJob.CATEGORYCOUNT,
				Integer.valueOf(integer != null ? integer.intValue() + add : add));
	}

	protected Unit addToUnitMappings(final BMECatImportCronJob cronJob, final String code)
	{
		if (code == null)
		{
			return null;
		}
		Map unitMappings = cronJob.getAllUnitMappings();
		unitMappings = unitMappings == null ? new HashMap() : new HashMap(unitMappings);
		if (!unitMappings.containsKey(code))
		{
			Unit unit = null;
			final Collection coll = getSession().getProductManager().getUnitsByCode(code);
			if (!coll.isEmpty())
			{
				unit = (Unit) coll.iterator().next();
				if (coll.size() > 1)
				{
					cronJob.addLog("multiple units found for code '" + code + "' " + coll + " - choosing " + unit, this);
				}
			}
			unitMappings.put(code, unit);
			cronJob.setAllUnitMappings(unitMappings);
			return unit;
		}
		else
		{
			return (Unit) unitMappings.get(code);
		}
	}

	protected Currency addToCurrencyMappings(final BMECatImportCronJob cronJob, final String code)
	{
		if (code == null)
		{
			return null;
		}
		Map mappings = cronJob.getAllCurrencyMappings();
		mappings = mappings == null ? new HashMap() : new HashMap(mappings);
		if (!mappings.containsKey(code))
		{
			Currency currency = null;
			try
			{
				currency = getSession().getC2LManager().getCurrencyByIsoCode(code);
			}
			catch (final JaloItemNotFoundException e)
			{ /* fine here */
			}
			mappings.put(code, currency);
			cronJob.setAllCurrencyMappings(mappings);
			return currency;
		}
		else
		{
			return (Currency) mappings.get(code);
		}
	}

	protected ClassificationSystem addToClassificationMappings(final BMECatImportCronJob cronJob, final String qualifier)
	{
		if (qualifier == null)
		{
			return null;
		}
		Map mappings = cronJob.getAllClassificationMappings();
		mappings = mappings == null ? new HashMap() : new HashMap(mappings);
		if (!mappings.containsKey(qualifier))
		{
			ClassificationSystem classificationSystem = null;
			try
			{
				classificationSystem = CatalogManager.getInstance().getClassificationSystem(qualifier);
			}
			catch (final JaloItemNotFoundException e)
			{ /* fine here */
			}
			mappings.put(qualifier, classificationSystem);
			cronJob.setAllClassificationMappings(mappings);
			return classificationSystem;
		}
		else
		{
			return (ClassificationSystem) mappings.get(qualifier);
		}
	}

	// TODO -> throws: UNIQUE CONTRAINT EXCEPTION !!!!
	protected void addToCountryMappings(final BMECatImportCronJob cronJob, final Collection codes)
	{
		/*
		 * if( codes == null || codes.isEmpty() ) return; Map mappings = cronJob.getAllCountryMappings(); mappings =
		 * mappings == null ? new HashMap() : new HashMap(mappings); boolean changed = false; for( Iterator it =
		 * codes.iterator(); it.hasNext(); ) { final String code = (String)it.next(); if( !mappings.containsKey(code) ) {
		 * changed = true; Country c = null; try { c = getSession().getC2LManager().getCountryByIsoCode(code); } catch(
		 * JaloItemNotFoundException e ){ / fine here
		 */
		/*
		 * } mappings.put(code,c); } }
		 * 
		 * if( changed ) cronJob.setAllCountryMappings(mappings);
		 */
	}

	private void addToPriceTypeMappings(final BMECatImportCronJob cronJob, final String priceType)
	{
		final EnumerationManager enumerationManager = JaloSession.getCurrentSession().getEnumerationManager();
		final EnumerationType bmeCatPricesEnumType = enumerationManager.getEnumerationType(BMECatConstants.TC.BMECATPRICETYPEENUM);

		try
		{
			cronJob.addToPriceTypeMapping(enumerationManager.getEnumerationValue(bmeCatPricesEnumType, priceType), null);
		}
		catch (final JaloItemNotFoundException e)
		{
			try
			{
				cronJob.addToPriceTypeMapping(enumerationManager.createEnumerationValue(bmeCatPricesEnumType, priceType), null);
			}
			catch (final JaloInvalidParameterException e1)
			{
				e1.printStackTrace();
			}
			catch (final ConsistencyCheckException e1)
			{
				e1.printStackTrace();
			}
		}

	}

	//	private void addToTaxTypeMappings(final BMECatImportCronJob cronJob, final String taxType)
	//	{
	//		final EnumerationManager em = JaloSession.getCurrentSession().getEnumerationManager();
	//		final EnumerationType bmeCatPricesEnumType = em.getEnumerationType(BMECatConstants.TC.BMECATPRICETYPEENUM);
	//
	//		try
	//		{
	//			cronJob.addToTaxTypeMapping(taxType, null);
	//		}
	//		catch (final JaloItemNotFoundException e)
	//		{
	//			try
	//			{
	//				cronJob.addToPriceTypeMapping(em.createEnumerationValue(bmeCatPricesEnumType, taxType), null);
	//			}
	//			catch (final JaloInvalidParameterException e1)
	//			{
	//				e1.printStackTrace();
	//			}
	//			catch (final ConsistencyCheckException e1)
	//			{
	//				e1.printStackTrace();
	//			}
	//		}
	//
	//	}

	/**
	 * Analyzes articles, medias, categories, currencies, units and countries.
	 */
	@Override
	protected void importBMECatObject(final Catalog catalog, final AbstractValueObject object, final BMECatImportCronJob cronJob)
			throws ParseAbortException
	{
		if (object instanceof CatalogGroupSystem)
		{
			// this.position = Math.max( this.position, HEADER_DONE );
			final Collection coll = ((CatalogGroupSystem) object).getCategories();
			if (coll != null && !coll.isEmpty())
			{
				addToCategoryCount(cronJob, coll.size());
				for (final Iterator it = coll.iterator(); it.hasNext();)
				{
					final CatalogStructure cat = (CatalogStructure) it.next();

					// BMECAT-167
					if (cat.getParentID() == null || cat.getParentID().equals(""))
					{
						final String msg = "Missing <PARENT_ID>...</PARENT_ID> definition inside " + cat;
						cronJob.addLog(msg, this, cronJob.getErrorLogLevel());
						// error( msg );
						cronJob.setCriticalErrorMsg(msg);
						cronJob.setErrorFlag(true);
					}

					// medias
					addMimes(cronJob, cat.getMedias());
					// keywords
					addKeywords(cronJob, cat.getKeywords());
				}
			}
		}
		else if (object instanceof Article)
		{
			this.position = Math.max(this.position, CATEGORIES_DONE);
			addToArticleCount(cronJob, 1);
			final Article article = (Article) object;
			// units
			if (article.getOrderUnit() != null)
			{
				addToUnitMappings(cronJob, article.getOrderUnit());
			}
			if (article.getContentUnit() != null)
			{
				addToUnitMappings(cronJob, article.getContentUnit());
			}
			if (article.getArticleFeatures() != null)
			{
				for (final Iterator it = article.getArticleFeatures().iterator(); it.hasNext();)
				{
					final ArticleFeatures articleFeatures = (ArticleFeatures) it.next();

					addToClassificationMappings(cronJob, articleFeatures.getReferenceFeatureSystemName());

					for (final Iterator it2 = articleFeatures.getFeatures().iterator(); it2.hasNext();)
					{
						final Feature feature = (Feature) it2.next();
						if (feature.getFunit() != null && !"".equals(feature.getFunit().trim()))
						{
							addToUnitMappings(cronJob, feature.getFunit().trim());
						}
					}
				}
			}

			// stores existing territories (cronjob)
			// builds up tax mappings (cronjob)
			new BMECatPriceContainer(cronJob, article);

			// medias
			addMimes(cronJob, article.getMedias());
			// keywords
			addKeywords(cronJob, article.getKeywords());
			// currencies and countries via ArticlePrices
			final Collection priceDetails = article.getArticlePriceDetails();
			if (priceDetails != null && !priceDetails.isEmpty())
			{
				for (final Iterator it = priceDetails.iterator(); it.hasNext();)
				{
					final ArticlePriceDetails det = (ArticlePriceDetails) it.next();
					final Collection prices = det.getPrices();
					if (prices != null && !prices.isEmpty())
					{
						for (final Iterator it2 = prices.iterator(); it2.hasNext();)
						{
							final ArticlePrice articlePrice = (ArticlePrice) it2.next();
							addToCurrencyMappings(cronJob, articlePrice.getCurrency());
							addToCountryMappings(cronJob, articlePrice.getTerritories());
							addToPriceTypeMappings(cronJob, articlePrice.getPriceType());
						}
					}
				}
			}

		}
		else if (object instanceof ArticleToCatalogGroupMap)
		{
			this.position = Math.max(this.position, ARTICLES_DONE);
			addToCategoryAssignmentCount(cronJob, 1);
		}

	}

	/**
	 * Analyzes the catalog.
	 */
	@Override
	protected void initializeBMECatImport(final Catalog catalogValueObject, final BMECatImportCronJob cronJob)
	{
		final CatalogManager catalogManager = getCatalogManager();

		final int transactionMode = catalogValueObject.getTransactionMode();
		// save transaction mode
		cronJob.setTransactionMode(getBMECatManager().getTransactionModeEnum(transactionMode));
		// supplier
		/*
		 * cronJob.setSupplierName(catalogValueObject.getSupplier().getName()); final Company supplier =
		 * catalogManager.getCompanyByUID(catalogValueObject.getSupplier().getName()); cronJob.setSupplier(supplier); if(
		 * catalogValueObject.getSupplier().getAddress() != null &&
		 * catalogValueObject.getSupplier().getAddress().getCountry() != null ) addToCountryMappings(cronJob,
		 * Collections.singleton(catalogValueObject.getSupplier().getAddress().getCountry())); // buyer if(
		 * catalogValueObject.getBuyer() != null ) { cronJob.setBuyerName(catalogValueObject.getBuyer().getName()); final
		 * Company buyer = catalogManager.getCompanyByUID(catalogValueObject.getBuyer().getName());
		 * cronJob.setBuyer(buyer); if( catalogValueObject.getBuyer().getAddress() != null &&
		 * catalogValueObject.getBuyer().getAddress().getCountry() != null ) addToCountryMappings(cronJob,
		 * Collections.singleton(catalogValueObject.getBuyer().getAddress().getCountry())); } else {
		 * cronJob.setBuyerName(null); cronJob.setBuyer(null); }
		 */
		// catalog
		// final de.hybris.platform.catalog.jalo.Catalog catalog = supplier !=
		// null ? catalogManager.getCatalog(supplier, catalogValueObject.getId()):null;
		final de.hybris.platform.catalog.jalo.Catalog catalog = catalogManager.getCatalog(catalogValueObject.getID());

		cronJob.setCatalog(catalog);
		cronJob.setCatalogID(catalogValueObject.getID());
		// date
		cronJob.setProperty(BMECatImportCronJob.CATALOGDATE, catalogValueObject.getGenerationDate());
		// catalog version
		final CatalogVersion catalogVersion = catalog != null ? catalog.getCatalogVersion(catalogValueObject.getVersion()) : null;
		cronJob.setCatalogVersion(catalogVersion);
		cronJob.setCatalogVersionName(catalogValueObject.getVersion());
		cronJob.setPreviousUpdateVersion(catalogValueObject.getPreviousVersion());
		// language
		cronJob.setImportLanguageIsoCode(catalogValueObject.getLanguage());
		try
		{
			cronJob.setImportLanguage(getSession().getC2LManager().getLanguageByIsoCode(catalogValueObject.getLanguage()));
		}
		catch (final JaloItemNotFoundException e)
		{ /* fine here */
		}
		// default currency
		cronJob.setDefaultCurrencyIsoCode(catalogValueObject.getDefaultCurrency());
		addToCurrencyMappings(cronJob, catalogValueObject.getDefaultCurrency());
		if (catalogValueObject.getDefaultCurrency() != null)
		{
			try
			{
				cronJob
						.setDefaultCurrency(getSession().getC2LManager().getCurrencyByIsoCode(catalogValueObject.getDefaultCurrency()));
			}
			catch (final JaloItemNotFoundException e)
			{ /* fine here */
			}
		}
		// is localization update ?
		cronJob.setLocalizationUpdate(transactionMode == BMECatConstants.TRANSACTION.T_NEW_CATALOG && catalogVersion != null
				&& !catalogVersion.isImportedLanguage(catalogValueObject.getLanguage()));
	}
}

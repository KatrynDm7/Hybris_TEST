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
import de.hybris.platform.bmecat.FeatureContainer;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.Abort;
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.ArticleFeatures;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.bmecat.parser.Feature;
import de.hybris.platform.bmecat.parser.Variant;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.catalog.jalo.classification.util.UntypedFeature;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloDuplicateQualifierException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.variants.constants.VariantsConstants;
import de.hybris.platform.variants.jalo.VariantAttributeDescriptor;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.jalo.VariantType;
import de.hybris.platform.variants.jalo.VariantsManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.map.CaseInsensitiveMap;


/**
 * This step imports the &lt;ARTICLE&gt; tag including its sub-tags. Supports all transactions
 * <strong>T_NEW_CATALOG</strong>, <strong>T_UPDATE_PRODUCTS</strong> and <strong>T_UPDATE_PRICES</strong>. Step
 * supports restart and undo. Products are not deleted but rather removed from catalog version.
 * 
 */
public class BMECatArticleStep extends GeneratedBMECatArticleStep
{
	private Map mapping;

	//enums for value mapping
	protected EnumerationValue ARTICLE_REFERENCES;
	protected EnumerationValue BUYER_IDS;
	protected EnumerationValue CATALOGVERSION;
	protected EnumerationValue CONTENT_UNIT;
	protected EnumerationValue DELIVERY_TIME;
	protected EnumerationValue EAN;
	protected EnumerationValue ERP_GROUP_BUYER;
	protected EnumerationValue ERP_GROUP_SUPPLIER;
	protected EnumerationValue KEYWORDS;
	protected EnumerationValue LONG_DESCRIPTION;
	protected EnumerationValue MANUFACTURER_AID;
	protected EnumerationValue MANUFACTURER_NAME;
	protected EnumerationValue MANUFACTURER_TYPE_DESCRIPTION;
	protected EnumerationValue MIN_ORDER_QUANTITY;
	protected EnumerationValue NUMBER_CONTENT_UNITS;
	protected EnumerationValue ORDER;
	protected EnumerationValue ORDER_QUANTITY_INTERVAL;
	protected EnumerationValue ORDER_UNIT;
	protected EnumerationValue PRICE_QUANTITY;
	protected EnumerationValue REMARKS;
	protected EnumerationValue SEGMENT;
	protected EnumerationValue SHORT_DESCRIPTION;
	protected EnumerationValue SPECIAL_TREATMENT_CLASSES;
	protected EnumerationValue STATUS;
	protected EnumerationValue SUPPLIER_AID;
	protected EnumerationValue SUPPLIER_ALT_AID;

	protected Set unlocalizedProductAttributes;
	protected Set localizedProductAttributes;

	/**
	 * Creates and returns the default article attribute mapping. The keys are EnumerationValues of the
	 * <code>ArticleAttributeEnum</code>-EnumerationType. Values are <code>AttributeDescriptor</code>s of the specified
	 * targetType.
	 * 
	 * @param targetType
	 *           the composed type which should be used for the article import
	 * @return the default article attribute mapping
	 */
	protected Map createDefaultMapping(final ComposedType targetType)
	{
		final Map mapping = new HashMap();
		MappingUtils.addMapping(mapping, targetType, SUPPLIER_AID, Product.CODE, this);
		MappingUtils.addMapping(mapping, targetType, SUPPLIER_ALT_AID, CatalogConstants.Attributes.Product.SUPPLIERALTERNATIVEAID,
				this);
		MappingUtils.addMapping(mapping, targetType, CONTENT_UNIT, CatalogConstants.Attributes.Product.CONTENTUNIT, this);
		MappingUtils.addMapping(mapping, targetType, DELIVERY_TIME, CatalogConstants.Attributes.Product.DELIVERYTIME, this);
		MappingUtils.addMapping(mapping, targetType, EAN, CatalogConstants.Attributes.Product.EAN, this);
		MappingUtils.addMapping(mapping, targetType, ERP_GROUP_BUYER, CatalogConstants.Attributes.Product.ERPGROUPBUYER, this);
		MappingUtils
				.addMapping(mapping, targetType, ERP_GROUP_SUPPLIER, CatalogConstants.Attributes.Product.ERPGROUPSUPPLIER, this);
		MappingUtils
				.addMapping(mapping, targetType, MIN_ORDER_QUANTITY, CatalogConstants.Attributes.Product.MINORDERQUANTITY, this);
		MappingUtils.addMapping(mapping, targetType, NUMBER_CONTENT_UNITS, CatalogConstants.Attributes.Product.NUMBERCONTENTUNITS,
				this);
		MappingUtils.addMapping(mapping, targetType, ORDER, CatalogConstants.Attributes.Product.ORDER, this);
		MappingUtils.addMapping(mapping, targetType, ORDER_UNIT, Product.UNIT, this);
		MappingUtils.addMapping(mapping, targetType, ORDER_QUANTITY_INTERVAL,
				CatalogConstants.Attributes.Product.ORDERQUANTITYINTERVAL, this);
		MappingUtils.addMapping(mapping, targetType, PRICE_QUANTITY, CatalogConstants.Attributes.Product.PRICEQUANTITY, this);
		MappingUtils.addMapping(mapping, targetType, REMARKS, CatalogConstants.Attributes.Product.REMARKS, this);
		MappingUtils.addMapping(mapping, targetType, SEGMENT, CatalogConstants.Attributes.Product.SEGMENT, this);
		MappingUtils.addMapping(mapping, targetType, SHORT_DESCRIPTION, Product.NAME, this);
		MappingUtils.addMapping(mapping, targetType, LONG_DESCRIPTION, Product.DESCRIPTION, this);
		MappingUtils.addMapping(mapping, targetType, SPECIAL_TREATMENT_CLASSES,
				CatalogConstants.Attributes.Product.SPECIALTREATMENTCLASSES, this);
		MappingUtils.addMapping(mapping, targetType, STATUS, CatalogConstants.Attributes.Product.ARTICLESTATUS, this);
		MappingUtils.addMapping(mapping, targetType, BUYER_IDS, CatalogConstants.Attributes.Product.BUYERIDS, this);
		MappingUtils.addMapping(mapping, targetType, MANUFACTURER_NAME, CatalogConstants.Attributes.Product.MANUFACTURERNAME, this);
		MappingUtils.addMapping(mapping, targetType, MANUFACTURER_AID, CatalogConstants.Attributes.Product.MANUFACTURERAID, this);
		MappingUtils.addMapping(mapping, targetType, MANUFACTURER_TYPE_DESCRIPTION,
				CatalogConstants.Attributes.Product.MANUFACTURERTYPEDESCRIPTION, this);
		MappingUtils.addMapping(mapping, targetType, KEYWORDS, CatalogConstants.Attributes.Product.KEYWORDS, this);
		MappingUtils.addMapping(mapping, targetType, CATALOGVERSION, CatalogConstants.Attributes.Product.CATALOGVERSION, this);
		//MappingUtils.addMapping( mapping, targetType, , log );
		//MappingUtils.addMapping( mapping, targetType, , log );
		return mapping;
	}

	/**
	 * @see de.hybris.platform.jalo.Item#remove(de.hybris.platform.jalo.SessionContext)
	 */
	@Override
	public void remove(final SessionContext ctx) throws ConsistencyCheckException
	{
		super.remove(ctx);
	}

	/**
	 * @see de.hybris.platform.cronjob.jalo.Step#canUndo(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	protected boolean canUndo(final CronJob forSchedule)
	{
		return true;
	}

	/**
	 * Reverts any changes of this step. This includes removing newly created products and keywords.
	 * 
	 * @param forSchedule
	 */
	@Override
	protected void undoStep(final CronJob forSchedule)
	{
		undoProducts(forSchedule);
		super.undoStep(forSchedule); // removes keywords
	}

	protected void undoProducts(final CronJob cronJob)
	{
		final SessionContext ctx = getSession().createSessionContext();
		ctx.setLanguage(((BMECatImportCronJob) cronJob).getImportLanguage());
		if (isDebugEnabled())
		{
			debug("undo: changedescriptors CREATE_VARIANTPRODUCT");
		}
		// remove variant products
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_VARIANTPRODUCT, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_VARIANTPRODUCT, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
				final VariantProduct variantProduct = (VariantProduct) changeDescriptor.getChangedItem();
				final String code = variantProduct == null ? "null" : variantProduct.getCode();
				if (isDebugEnabled())
				{
					debug("undo: deleting variant product >" + code + "< changedescriptor >" + changeDescriptor.getDescription() + "<");
				}
				try
				{
					if (variantProduct != null && variantProduct.isAlive())
					{
						variantProduct.remove();
					}
					if (isDebugEnabled())
					{
						debug("undo: deleted previously created variant product >" + code + "<");
					}
					changeDescriptor.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		if (isDebugEnabled())
		{
			debug(" undo: changedescriptors CREATE_PRODUCT");
		}
		// remove products
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
				final Product product = (Product) changeDescriptor.getChangedItem();
				final String code = product == null ? "null" : product.getCode();
				if (isDebugEnabled())
				{
					debug("undo: deleting product >" + code + "< changedescriptor >" + changeDescriptor.getDescription() + "<");
				}
				try
				{
					if (product != null && product.isAlive())
					{
						product.remove();
					}
					if (isDebugEnabled())
					{
						debug("undo: deleted previously created product >" + code + "<");
					}
					changeDescriptor.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		if (isDebugEnabled())
		{
			debug("undo: changedescriptors DELETE_PRODUCT");
		}
		// undo removal product from catalog version
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_PRODUCT, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_PRODUCT, 0, 100))
		{

			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				try
				{
					final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
					final Product product = (Product) changeDescriptor.getChangedItem();
					if (isDebugEnabled())
					{
						debug("Resetting product >" + product.getCode() + "< online.");
					}
					product.setAttribute(CatalogConstants.Attributes.Product.OFFLINEDATE, null);
					changeDescriptor.remove();
				}
				catch (final JaloBusinessException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}

		if (isDebugEnabled())
		{
			debug(" undo: changedescriptors DELETE_VARIANTPRODUCT");
		}
		// undo removal of variant products from catalog version
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_VARIANTPRODUCT, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.DELETE_VARIANTPRODUCT, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				try
				{
					final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
					final Map attributes = new HashMap();
					final VariantProduct variantProduct = (VariantProduct) changeDescriptor.getChangedItem();
					// the following occures if product variant had been replaced by other variants (not connected to the product anymore)
					if (variantProduct.getBaseProduct() == null)
					{
						attributes.put(VariantProduct.BASEPRODUCT,
								variantProduct.getAttribute(BMECatConstants.Attributes.VariantProduct.PREVIOUSBASEPRODUCT));
						attributes.put(BMECatConstants.Attributes.VariantProduct.PREVIOUSBASEPRODUCT, null);
					}
					attributes.put(CatalogConstants.Attributes.Product.OFFLINEDATE, null);
					variantProduct.setAllAttributes(attributes);
					if (isInfoEnabled())
					{
						info("Resetting variant product >" + variantProduct.getCode() + "< online");
					}
					changeDescriptor.remove();
				}
				catch (final JaloBusinessException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		if (isDebugEnabled())
		{
			debug("undo: changedescriptors UPDATE_PRODUCT: "
					+ getChanges(cronJob, BMECatConstants.ChangeTypes.UPDATE_PRODUCT, 0, 100).size());
		}
		// redo product update
		for (Collection range = getChanges(cronJob, BMECatConstants.ChangeTypes.UPDATE_PRODUCT, 0, 100); range != null
				&& !range.isEmpty(); range = getChanges(cronJob, BMECatConstants.ChangeTypes.UPDATE_PRODUCT, 0, 100))
		{
			for (final Iterator it = range.iterator(); it.hasNext();)
			{
				try
				{
					final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
					final ProductUpdateVersion updateVersion = (ProductUpdateVersion) changeDescriptor.getChangedItem();
					Product product = (Product) updateVersion.getAttribute(ProductUpdateVersion.ORIGINALPRODUCT);
					if (product == null)
					{
						// in case the product had been deleted and recreated (with different pk but same code)
						final String originalProductCode = (String) updateVersion
								.getAttribute(ProductUpdateVersion.ORIGINALPRODUCTCODE);
						if (originalProductCode != null)
						{
							final CatalogVersion catalogVersion = ((BMECatImportCronJob) cronJob).getCatalogVersion();
							if (isDebugEnabled())
							{
								debug("### catalog version: " + (catalogVersion == null ? "null" : catalogVersion.getVersion())
										+ " productCode: " + originalProductCode);
							}
							product = getProduct(catalogVersion, originalProductCode);
						}
					}
					if (isDebugEnabled())
					{
						debug("got product update version for product: " + (product == null ? "null" : product.getCode()));
					}
					if (product != null && product.isAlive())
					{

						final Map oldAttributes = copyProductAttributes(((BMECatImportCronJob) cronJob), updateVersion);
						product.setAllAttributes(ctx, oldAttributes);
						final Collection featuresCollection = (Collection) updateVersion.getProperty(ProductUpdateVersion.FEATURES);
						if (featuresCollection != null)
						{
							final de.hybris.platform.catalog.jalo.classification.util.FeatureContainer untypedCont = de.hybris.platform.catalog.jalo.classification.util.FeatureContainer
									.loadUntyped(product);
							for (final Iterator f = featuresCollection.iterator(); f.hasNext();)
							{
								final FeatureContainer feature = (FeatureContainer) f.next();

								final UntypedFeature untypedFeature = (UntypedFeature) untypedCont.getOrCreateFeature(feature
										.getQualifier());
								final String[] values = feature.getValues();
								for (int i = 0; i < values.length; i++)
								{
									final FeatureValue featureValue = untypedFeature.createValue(values[i]);
									if (feature.getDescription() != null)
									{
										featureValue.setDescription(feature.getDescription());
										// TODO
										//if( feature.getValueDetails() != null ) fv.setValueDetails(feature.getValueDetails());
										// TODO use classification units
										//if( feature.getUnit() != null ) fv.setClassificationAttributeUnit( ((BMECatImportCronJob)cronJob).getOrCreateUnit( feature.getUnit() ) );
									}
								}
								if (isDebugEnabled())
								{
									debug("undo product update: recreated feature for product >" + product.getCode() + "< feature >"
											+ feature.toString() + "<");
								}
							}
							untypedCont.store();
						}
						if (isDebugEnabled())
						{
							debug("undo product update: reset product attributes >" + product.getCode() + "< changedescriptor >"
									+ changeDescriptor.getDescription() + "<");
						}
					}
					else
					{
						if (isWarnEnabled())
						{
							warn("undo product update: produc thas been null changedesc: " + changeDescriptor.getDescription());
						}
					}
					changeDescriptor.remove();
				}
				catch (final JaloSecurityException e)
				{
					throw new JaloSystemException(e);
				}
				catch (final JaloBusinessException e)
				{
					throw new JaloSystemException(e);
				}
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
		return cronJob.isCatalogInfoAvailableAsPrimitive() ? cronJob.getArticleCountAsPrimitive() : -1;
	}

	/**
	 * Initializes article step enums
	 */
	@Override
	protected void initializeBMECatImport(final Catalog catalog, final BMECatImportCronJob cronJob)
	{
		initializeArticleAttributeEnums();
		initializeCopyAttributes();
		cronJob.resetCounter();
	}

	/**
	 * Imports the &lt;ARTICLE&gt; tag
	 * 
	 * @see BMECatImportStep#importBMECatObject(Catalog, AbstractValueObject, BMECatImportCronJob)
	 */
	@Override
	@SuppressWarnings("PMD.TooFewBranchesForASwitchStatement")
	protected void importBMECatObject(final Catalog catalogValueObject, final AbstractValueObject object,
			final BMECatImportCronJob cronJob) throws ParseAbortException
	{
		if (object instanceof Article)
		{
			cronJob.addToCounter(1);
			final Article article = (Article) object;
			if (cronJob.isRunningRestart())
			{
				final String run = "SECONDRUN: ";
				final String lastValidCode = getLastValidArticleCode(cronJob, catalogValueObject);

				// skip all articles up to last valid code
				if (lastValidCode != null && !NONE.equals(lastValidCode))
				{
					// reset to normal mode if the last valid article is found
					if (lastValidCode.equals(article.getSupplierAID()))
					{
						if (isInfoEnabled())
						{
							info(run + "found last valid article " + article.getSupplierAID() + " - back to normal mode ");
						}
						lastValidArticleFound(cronJob);
					}
					else
					{
						if (isDebugEnabled())
						{
							debug(run + "waiting for last valid article " + lastValidCode + " - skipping " + article.getSupplierAID());
						}
					}
					return; // break here
				}
			}
			else
			{
				if (isDebugEnabled())
				{
					debug("FIRSTRUN");
				}
			}


			switch (catalogValueObject.getTransactionMode())
			{
				case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
				{
					final String mode = article.getMode();
					if (mode == null)
					{
						if (isErrorEnabled())
						{
							error("Mandatory attribute Mode not set! Skipping product with code " + article.getSupplierAID());
						}
					}
					else if (mode.equalsIgnoreCase(Article.MODE_NEW))
					{
						createArticle(cronJob, catalogValueObject, article);
					}
					else if (mode.equalsIgnoreCase(Article.MODE_UPDATE))
					{
						updateArticle(cronJob, catalogValueObject, article);
					}
					else if (mode.equalsIgnoreCase(Article.MODE_DELETE))
					{
						deleteArticle(cronJob, catalogValueObject, article);
					}
					else
					{
						if (isErrorEnabled())
						{
							error("Unknown Mode " + article.getMode() + "! Skipping product with code " + article.getSupplierAID());
						}
					}
					break;
				}
				case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
				{
					if (cronJob.isLocalizationUpdateAsPrimitive())
					{
						updateArticle(cronJob, catalogValueObject, article);
					}
					else
					{
						final Product product = createArticle(cronJob, catalogValueObject, article);
						if (product != null)
						{
							arrangeArticle(article, product);
						}
					}
					break;
				}
			}
		}
		else if (object instanceof ArticleToCatalogGroupMap)
		{
			throw new ParseFinishedException("ArticleStep: done.");
		}
		else if (object instanceof Abort)
		{
			if (ABORTTYPE.equals(((Abort) object).getType()))
			{
				throw new BMECatParser.TestParseAbortException("");
			}
		}
	}

	private final static String ABORTTYPE = "article";

	/**
	 * Processes the tag &lt;ARTICLE mode ="delete" &gt; tag in T_UPDATE_PRODUCT transaction. Rather than really deleting
	 * the corresponding product it is
	 * 
	 * @param cronJob
	 * @param catalog
	 * @param article
	 */
	protected void deleteArticle(final BMECatImportCronJob cronJob, final Catalog catalog, final Article article)
	{
		final Product product = getProduct(cronJob.getCatalogVersion(), article.getSupplierAID());
		if (product != null)
		{
			try
			{
				final CatalogVersion catalogVersion = (CatalogVersion) product
						.getAttribute(CatalogConstants.Attributes.Product.CATALOGVERSION);
				// variant products
				final Date offlineDate = new Date();
				for (final Iterator it = getCatalogManager().getVariants(getSession().getSessionContext(), product, catalogVersion)
						.iterator(); it.hasNext();)
				{
					final VariantProduct variantProduct = (VariantProduct) it.next();
					variantProduct.setAttribute(CatalogConstants.Attributes.Product.OFFLINEDATE, offlineDate);
					if (cronJob.isChangeRecordingEnabledAsPrimitive())
					{
						addChange(cronJob, BMECatConstants.ChangeTypes.DELETE_VARIANTPRODUCT, variantProduct,
								"... set variant product offline" + variantProduct.getCode());
					}
				}
				// product references remain intact (restriction will prevent ordinary users from seeing them)
				product.setAttribute(CatalogConstants.Attributes.Product.OFFLINEDATE, offlineDate);
				if (cronJob.isChangeRecordingEnabledAsPrimitive())
				{
					addChange(cronJob, BMECatConstants.ChangeTypes.DELETE_PRODUCT, product, " removed product >" + product.getCode()
							+ "< from catalogversion");
				}
				if (isDebugEnabled())
				{
					debug("Product with code '" + product.getCode() + " removed from catalog version!");
				}
			}
			catch (final JaloBusinessException e)
			{
				if (isErrorEnabled())
				{
					error("Product with code '" + article.getSupplierAID() + " could not be deleted! (" + e.getMessage() + ")");
				}
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param catalog
	 * @param article
	 * @param cronJob
	 */
	protected void updateArticle(final BMECatImportCronJob cronJob, final Catalog catalog, final Article article)
	{
		try
		{
			final Product product = getProduct(cronJob.getCatalogVersion(), article.getSupplierAID());
			if (product != null)
			{
				final ItemAttributeMap values = getValues(cronJob, article, true);

				/** set features, variants and attributes */
				if (cronJob.isChangeRecordingEnabledAsPrimitive())
				{
					addChange(cronJob, BMECatConstants.ChangeTypes.UPDATE_PRODUCT, createProductUpdateVersion(cronJob, product),
							"... updated Product" + product.getCode());
				}
				if (isInfoEnabled())
				{
					info("updating Product " + product.getCode() + "< pk: " + product.getPK().toString());
				}

				if (cronJob.isLocalizationUpdateAsPrimitive())
				{
					//simply set localized attributes
					product.setAllAttributes(values);
				}
				else
				{
					// set features
					setFeatures(cronJob, product, article);

					//create variants
					final List allVariantAttributeMaps = new LinkedList();
					final VariantType newVariantType = setVariantFeatures(cronJob, values, article, allVariantAttributeMaps);

					Collection deletionVariants = getCatalogManager().getVariants(getSession().getSessionContext(), product,
							(CatalogVersion) product.getAttribute(CatalogConstants.Attributes.Product.CATALOGVERSION));
					final List keptVariants = new LinkedList();
					// if variant products have the same type after update - check which variants do not need to be deleted
					if (newVariantType != null && newVariantType.equals(getVariantsManager().getVariantType(product)))
					{
						final HashMap deletionMap = new HashMap();
						for (final Iterator dIt = deletionVariants.iterator(); dIt.hasNext();)
						{
							final VariantProduct variantProduct = (VariantProduct) dIt.next();
							deletionMap.put(variantProduct.getCode(), variantProduct);
						}
						for (final Iterator it = allVariantAttributeMaps.iterator(); it.hasNext();)
						{
							final Map newVariantMap = (Map) it.next();
							// if it still exists in update catalog don't mark existing variant for deletion
							if (deletionMap.remove(newVariantMap.get(Product.CODE)) != null)
							{
								// don't create new variant
								if (isDebugEnabled())
								{
									debug("Keeping existing variant >" + newVariantMap.get(Product.CODE) + "<");
								}
								keptVariants.add(newVariantMap);
							}
						}
						allVariantAttributeMaps.removeAll(keptVariants);
						deletionVariants = deletionMap.values();
					}
					// delete previous variant products
					final Date offlineDate = new Date();
					for (final Iterator it = deletionVariants.iterator(); it.hasNext();)
					{
						final VariantProduct variantProduct = (VariantProduct) it.next();
						if (isInfoEnabled())
						{
							info("Deleting product variant >" + variantProduct.getCode() + "< because of product update");
						}
						variantProduct.setAttribute(BMECatConstants.Attributes.VariantProduct.PREVIOUSBASEPRODUCT,
								variantProduct.getBaseProduct());
						variantProduct.setAttribute(VariantProduct.BASEPRODUCT, null);
						variantProduct.setAttribute(CatalogConstants.Attributes.Product.OFFLINEDATE, offlineDate);

						if (cronJob.isChangeRecordingEnabledAsPrimitive())
						{
							addChange(cronJob, BMECatConstants.ChangeTypes.DELETE_VARIANTPRODUCT, variantProduct,
									"... removed variant product from catalog version" + variantProduct.getCode());
						}

					}
					if (newVariantType != null && !newVariantType.equals(getVariantsManager().getVariantType(product)))
					{
						// set product properties including new variant type
						values.put(VariantsConstants.Attributes.Product.VARIANTTYPE, newVariantType);
					}
					product.setAllAttributes(values);

					// create new variant products
					if (!allVariantAttributeMaps.isEmpty())
					{
						for (final Iterator it = allVariantAttributeMaps.iterator(); it.hasNext();)
						{
							final Map variantAttributes = (Map) it.next();
							variantAttributes.put(VariantProduct.BASEPRODUCT, product);
							final VariantType variantType = (VariantType) values.get(VariantsConstants.Attributes.Product.VARIANTTYPE);
							final VariantProduct variantProduct = (VariantProduct) variantType.newInstance(variantAttributes);
							if (isInfoEnabled())
							{
								info("created variant product >" + variantProduct.getCode() + "<");
							}
							if (cronJob.isChangeRecordingEnabledAsPrimitive())
							{
								addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_VARIANTPRODUCT, variantProduct,
										"... created variant product " + variantProduct.getCode());
							}
						}
					}
				}
				if (isDebugEnabled())
				{
					debug("Product with SupplierID '" + article.getSupplierAID() + "' updated!");
				}
			}
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Product with SupplierID '" + article.getSupplierAID() + "' could not be updated! " + e.getMessage());
			}
			e.printStackTrace();
		}
	}

	/**
	 * Returns Comparator which sorts variant features within one CategorySystem
	 * 
	 * @return VariantFeatureComparator
	 */
	protected Comparator getVariantFeatureComparator()
	{
		return new VariantFeatureComparator();
	}

	/**
	 * Method sets features which are not variants It is called for once for all feature groups. The implementation
	 * should be overwritten for specific needs of a project.<br/>
	 * 
	 * This default implementation stores all features in the Item ProductFeatures, these items are bound to a product by
	 * the list attribute "features" (aware of order!) This implementation ignores the fact that featurenames differ with
	 * import language.
	 * 
	 * @param cronJob
	 * @param product
	 * @param article
	 */
	protected void setFeatures(final BMECatImportCronJob cronJob, final Product product, final Article article)
	{
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			for (final Iterator it = article.getArticleFeatures().iterator(); it.hasNext();)
			{
				final ArticleFeatures featureGroup = (ArticleFeatures) it.next();
				final String systemName = featureGroup.getReferenceFeatureSystemName();
				final String className = featureGroup.getReferenceFeatureGroupId();
				if (systemName == null || systemName.length() == 0 || className == null || className.length() == 0)
				{
					if (isWarnEnabled())
					{
						warn("ReferenceFeatureSystemName or ReferenceFeatureGroupId are undefined! Skipping featuregroup!");
					}
					continue; // skip this feature group
				}

				final ClassificationSystemVersion classificationSystemVersion = cronJob.getAllClassificationMappings()
						.get(systemName);

				if (isDebugEnabled())
				{
					debug(" using classificationcystemversion: " + classificationSystemVersion.getVersion());
				}

				final ClassificationClass cclass = classificationSystemVersion != null ? classificationSystemVersion
						.getClassificationClass(className) : null;
				if (cclass != null)
				{
					final de.hybris.platform.catalog.jalo.classification.util.FeatureContainer typedCont = de.hybris.platform.catalog.jalo.classification.util.FeatureContainer
							.loadTyped(product, Collections.singleton(cclass));
					// remove all previous values
					typedCont.clearValues();
					for (final Iterator j = featureGroup.getFeatures().iterator(); j.hasNext();)
					{
						final Feature feature = (Feature) j.next();
						if (feature.getVariants() == null)
						{
							final String featureName = feature.getFname();
							// TODO fetch via category ???
							final ClassificationAttribute attr = classificationSystemVersion.getClassificationAttribute(featureName);
							final Map selectionValueMap = new CaseInsensitiveMap();
							for (final Iterator iter = cclass.getAttributeValues(attr).iterator(); iter.hasNext();)
							{
								final ClassificationAttributeValue classificationAttributeValue = (ClassificationAttributeValue) iter
										.next();
								// TODO check if something has to be mapped
								selectionValueMap.put(classificationAttributeValue.getCode(), classificationAttributeValue);
							}
							if (attr != null && importFeature(cronJob, product, featureGroup, feature))
							{
								final List convertedValues = new ArrayList();
								final String type = cclass.getAttributeType(attr) != null ? cclass.getAttributeType(attr).getCode()
										: null;
								for (final Iterator iter = Arrays.asList(feature.getFvalues()).iterator(); iter.hasNext();)
								{
									final String valueStr = (String) iter.next();
									final Object converted = convertClassificationValue(valueStr, cclass, attr, type, selectionValueMap);
									if (converted != null)
									{
										convertedValues.add(converted);
									}
								}
								typedCont.getFeature(attr).createValues(convertedValues);
							}
						}
					}
					// writre all features at once
					try
					{
						typedCont.store();
					}
					catch (final ConsistencyCheckException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected Object convertClassificationValue(final String valueStr, final ClassificationClass cat,
			final ClassificationAttribute attr, final String type, final Map valueMap)
	{
		if (valueStr.length() > 0)
		{
			if (CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN.equalsIgnoreCase(type))
			{
				// TODO: JA /NEIN/NOE/1/0/+/- ????
				return ("true".equalsIgnoreCase(valueStr) ? Boolean.TRUE : Boolean.FALSE);
			}
			else if (CatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER.equalsIgnoreCase(type))
			{
				try
				{
					return (Utilities.getNumberInstance().parse(valueStr));
				}
				catch (final ParseException e)
				{
					if (isErrorEnabled())
					{
						error(e.getMessage());
					}
					e.printStackTrace();
				}
			}
			// trying to get classification value
			else if (!valueMap.isEmpty())
			{
				final ClassificationAttributeValue classificationAttributeValue = (ClassificationAttributeValue) valueMap
						.get(valueStr);
				if (classificationAttributeValue != null)
				{
					return (classificationAttributeValue);
				}
				else
				{
					if (isErrorEnabled())
					{
						error("unknown ClassificationAttributeValue '" + valueStr + "'!");
					}
				}
			}
			// import as plain string
			else
			{
				return (valueStr);
			}
		}
		return null;
	}

	/**
	 * Builds attributemaps for the creation of variant products by building all combinations of given variant features
	 * values. Creates and returns VariantType if necessary
	 * 
	 * @param cronJob
	 * @param productAttributes
	 * @param article
	 * @param variantItemAttributes
	 * @return VariantType (created or already existing) or null if this is a localization update
	 */
	protected VariantType setVariantFeatures(final BMECatImportCronJob cronJob, final ItemAttributeMap productAttributes,
			final Article article, final List variantItemAttributes)
	{
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			final TreeMap variantTypeAttrMap = new TreeMap(getVariantFeatureComparator());
			List variantAttributeMaps = new LinkedList();
			final AtomicType atomicStringType = getSession().getTypeManager().getRootAtomicType(String.class);
			for (final Iterator it = article.getArticleFeatures().iterator(); it.hasNext();)
			{
				final ArticleFeatures featureGroup = (ArticleFeatures) it.next();
				if (!importVariantFeatures(featureGroup))
				{
					continue;
				}
				for (final Iterator features = featureGroup.getFeatures().iterator(); features.hasNext();)
				{
					final Feature feature = (Feature) features.next();
					if (feature.getVariants() != null)
					{
						// build featureMap - key contains typeinformation and responsible for sorting - value contains the feature
						final String qualifier = getVariantAttributeName(featureGroup, feature.getFname());
						final QualifierTypePair pair = new QualifierTypePair(feature.getVariants().getVorder(), qualifier,
								atomicStringType);
						variantTypeAttrMap.put(pair, new QualifierFeaturePair(qualifier, feature));
					}
				}
			}
			// create product variant from the sorted features
			for (final Iterator pairs = variantTypeAttrMap.values().iterator(); pairs.hasNext();)
			{
				final QualifierFeaturePair pair = (QualifierFeaturePair) pairs.next();
				variantAttributeMaps = multiplyVariants(cronJob, variantAttributeMaps, productAttributes, pair.getQualifier(),
						pair.getFeature());
			}
			if (!variantAttributeMaps.isEmpty())
			{
				variantItemAttributes.addAll(variantAttributeMaps);
				return getOrCreateVariantType(new HashSet(variantTypeAttrMap.keySet()));
			}
		}
		return null;
	}

	/**
	 * Gets variant type with the given attributes if not found creates a new one with these attributes
	 * 
	 * @param qualifierTypePairs
	 * @return variant type
	 */
	protected VariantType getOrCreateVariantType(final Collection qualifierTypePairs)
	{
		if (qualifierTypePairs.size() < 1)
		{
			throw new JaloSystemException(null, "Invalid number of variant attributes. Must be gerater than zero", 0);
		}
		/** query for existing variant type **/
		final VariantsManager variantManager = getVariantsManager();
		final HashMap attributeMap = new HashMap();
		for (final Iterator it = qualifierTypePairs.iterator(); it.hasNext();)
		{
			final QualifierTypePair pair = (QualifierTypePair) it.next();
			attributeMap.put(pair.getQualifier(), pair.getType());
		}
		final Collection variantTypes = variantManager.getVariantTypesByAttributes(attributeMap, false);
		switch (variantTypes.size())
		{
			case 0:
			{
				final StringBuilder code = new StringBuilder();
				for (final Iterator it = qualifierTypePairs.iterator(); it.hasNext();)
				{
					code.append(((QualifierTypePair) it.next()).getQualifier());
					if (it.hasNext())
					{
						code.append(":");
					}
				}
				if (isInfoEnabled())
				{
					info("Creating new variant type >" + code.toString() + "<");
				}
				final VariantType type = variantManager.createVariantType(code.toString());
				type.setName(code.toString());
				final int flags = AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.REMOVE_FLAG
						| AttributeDescriptor.SEARCH_FLAG;
				int position = 0;
				for (final Iterator it = qualifierTypePairs.iterator(); it.hasNext(); position++)
				{
					final QualifierTypePair attribute = (QualifierTypePair) it.next();
					try
					{
						if (isDebugEnabled())
						{
							debug("creating VariantAttributDescriptor: " + attribute.getQualifier());
						}
						final VariantAttributeDescriptor attributeDescriptor = type.createVariantAttributeDescriptor(
								attribute.getQualifier(), attribute.getType(), flags);
						attributeDescriptor.setPosition(position);
					}
					catch (final JaloDuplicateQualifierException x)
					{
						throw new JaloSystemException(x, "", 0);
					}
				}
				return type;
			}
			case 1:
			{
				final VariantType type = (VariantType) variantTypes.iterator().next();
				if (isDebugEnabled())
				{
					debug("Reusing variant type >" + type.getCode() + "<");
				}
				return type;
			}
			default:
			{
				final VariantType type = (VariantType) variantTypes.iterator().next();
				if (isWarnEnabled())
				{
					warn("More than one variant type for given attributes found. Using first one >" + type.getCode() + "<");
				}
				return type;
			}
		}
	}

	/**
	 * Decides whether variant features for the given reference system will be imported<br/>
	 * May be overwritten to ignore variant features of specified reference systems
	 * 
	 * @param features
	 * @return true to import false otherwise
	 */
	protected boolean importVariantFeatures(final ArticleFeatures features)
	{
		return true;
	}

	/**
	 * Gives subclasses the possibility to import the feature in a different way or ignore it<br/>
	 * Returns true if feature should be imported by default mechanism of BMECatArticleStep (->ProductFeature)
	 * 
	 * @param cronJob
	 * @param product
	 * @param featureGroup
	 * @param feature
	 * @return true to be imported to ProductFeature false if feature should be ignored or imported in a different way
	 */
	protected boolean importFeature(final BMECatImportCronJob cronJob, final Product product, final ArticleFeatures featureGroup,
			final Feature feature)
	{
		if (isDebugEnabled())
		{
			debug("Importing feature >" + featureGroup.getReferenceFeatureSystemName() + "_" + feature.getFname());
		}
		return true;
	}

	/**
	 * Build the name of the variant attribute from reference system and variant feature name<br/>
	 * To assure unique identifiers the name is constructed like this: name = <referencesystemname>_<variantname>
	 * 
	 * @param featureGroup
	 * @param name
	 *           Name of variant feature
	 * @return Name of variant attribut to be created
	 */
	protected String getVariantAttributeName(final ArticleFeatures featureGroup, final String name)
	{
		final StringBuilder buffer = new StringBuilder("");
		if (featureGroup.getReferenceFeatureSystemName() != null && !"".equals(featureGroup.getReferenceFeatureSystemName()))
		{
			buffer.append(featureGroup.getReferenceFeatureSystemName()).append(' ');
		}
		buffer.append(name);
		return buffer.toString();
	}


	protected String getProductCodeDelimiter()
	{
		return "-";
	}

	/**
	 * Creates new variant products (that is only their attributeMaps) by adding a feature with variants (a,b,c)*(1,2) =>
	 * a1,a2,b1,b2,c1,c2 Returns collection of attribute maps for the creation of variant products (which still have to
	 * be completed by adding the base product)
	 * 
	 * @param variantsItemAttributes
	 *           existing variant product attribute maps (e.g. a,b.c)
	 * @param productAttributes
	 *           attributeMap of the base product
	 * @param variantFeature
	 *           feature that contains variants (1,2)
	 * @return Collection of attribute maps for the creation of variant products
	 */
	protected List multiplyVariants(final BMECatImportCronJob cronJob, final List variantsItemAttributes,
			final ItemAttributeMap productAttributes, final String qualifier, final Feature variantFeature)
	{
		final LinkedList newVariantProductCollection = new LinkedList();
		if (variantsItemAttributes == null || variantsItemAttributes.isEmpty())
		{
			// first variant - seed it
			final String productCode = (String) productAttributes.get(Product.CODE);
			/** create a new variant product for each variant of this (first) feature */
			for (final Iterator it = variantFeature.getVariants().getVariants().iterator(); it.hasNext();)
			{
				final Variant variant = (Variant) it.next();
				final ItemAttributeMap attributes = new ItemAttributeMap();
				final String vCode = productCode + getProductCodeDelimiter() + variant.getSupplierAidSupplement();
				attributes.put(Product.CODE, vCode);
				attributes.put(Product.NAME, vCode);
				//	         attributes.put(CatalogConstants.Attributes.Product.CATALOG, cronJob.getCatalog());
				attributes.put(CatalogConstants.Attributes.Product.CATALOGVERSION, cronJob.getCatalogVersion());
				final StringBuilder value = new StringBuilder(variant.getFvalue());
				if (variantFeature.getFunit() != null)
				{
					value.append(" ").append(variantFeature.getFunit());
				}
				attributes.put(qualifier, value.toString());
				/** order and valueDetails are omitted, probably not relevant for variants, if not overwrite this method */
				newVariantProductCollection.add(attributes);
			}
		}
		else
		{
			// multiply each existing variant by the given variants (a,b)*(1,2) = a1,a2,b1,b2
			for (final Iterator vIt = variantsItemAttributes.iterator(); vIt.hasNext();)
			{
				final ItemAttributeMap oldVariantProduct = (ItemAttributeMap) vIt.next();
				final String productCode = (String) oldVariantProduct.get(Product.CODE);
				for (final Iterator it = variantFeature.getVariants().getVariants().iterator(); it.hasNext();)
				{
					final Variant variant = (Variant) it.next();
					final ItemAttributeMap newVariant = (ItemAttributeMap) oldVariantProduct.clone();
					final String vCode = productCode + getProductCodeDelimiter() + variant.getSupplierAidSupplement();
					newVariant.put(Product.CODE, vCode);
					newVariant.put(Product.NAME, vCode);
					final StringBuilder value = new StringBuilder(variant.getFvalue());
					if (variantFeature.getFunit() != null)
					{
						value.append(" ").append(variantFeature.getFunit());
					}
					newVariant.put(variantFeature.getFname(), value.toString());
					newVariantProductCollection.add(newVariant);
				}
			}
		}
		return newVariantProductCollection;
	}

	public VariantsManager getVariantsManager()
	{
		return VariantsManager.getInstance();
	}

	protected void initializeArticleAttributeEnums()
	{
		final EnumerationType eType = getSession().getEnumerationManager().getEnumerationType(
				BMECatConstants.TC.ARTICLEATTRIBUTEENUM);
		STATUS = getSession().getEnumerationManager().getEnumerationValue(eType, Article.STATUS);
		SUPPLIER_AID = getSession().getEnumerationManager().getEnumerationValue(eType, Article.SUPPLIER_AID);
		SUPPLIER_ALT_AID = getSession().getEnumerationManager().getEnumerationValue(eType, Article.SUPPLIER_ALT_AID);
		CONTENT_UNIT = getSession().getEnumerationManager().getEnumerationValue(eType, Article.CONTENT_UNIT);
		DELIVERY_TIME = getSession().getEnumerationManager().getEnumerationValue(eType, Article.DELIVERY_TIME);
		EAN = getSession().getEnumerationManager().getEnumerationValue(eType, Article.EAN);
		ERP_GROUP_BUYER = getSession().getEnumerationManager().getEnumerationValue(eType, Article.ERP_GROUP_BUYER);
		ERP_GROUP_SUPPLIER = getSession().getEnumerationManager().getEnumerationValue(eType, Article.ERP_GROUP_SUPPLIER);
		MIN_ORDER_QUANTITY = getSession().getEnumerationManager().getEnumerationValue(eType, Article.MIN_ORDER_QUANTITY);
		NUMBER_CONTENT_UNITS = getSession().getEnumerationManager().getEnumerationValue(eType, Article.NUMBER_CONTENT_UNITS);
		ORDER_QUANTITY_INTERVAL = getSession().getEnumerationManager().getEnumerationValue(eType, Article.ORDER_QUANTITY_INTERVAL);
		ORDER_UNIT = getSession().getEnumerationManager().getEnumerationValue(eType, Article.ORDER_UNIT);
		PRICE_QUANTITY = getSession().getEnumerationManager().getEnumerationValue(eType, Article.PRICE_QUANTITY);
		ARTICLE_REFERENCES = getSession().getEnumerationManager().getEnumerationValue(eType, Article.ARTICLE_REFERENCES);
		SPECIAL_TREATMENT_CLASSES = getSession().getEnumerationManager().getEnumerationValue(eType,
				Article.SPECIAL_TREATMENT_CLASSES);
		BUYER_IDS = getSession().getEnumerationManager().getEnumerationValue(eType, Article.BUYER_IDS);
		MANUFACTURER_NAME = getSession().getEnumerationManager().getEnumerationValue(eType, Article.MANUFACTURER_NAME);
		MANUFACTURER_AID = getSession().getEnumerationManager().getEnumerationValue(eType, Article.MANUFACTURER_AID);
		MANUFACTURER_TYPE_DESCRIPTION = getSession().getEnumerationManager().getEnumerationValue(eType,
				Article.MANUFACTURER_TYPE_DESCRIPTION);
		REMARKS = getSession().getEnumerationManager().getEnumerationValue(eType, Article.REMARKS);
		SEGMENT = getSession().getEnumerationManager().getEnumerationValue(eType, Article.SEGMENT);
		KEYWORDS = getSession().getEnumerationManager().getEnumerationValue(eType, Article.KEYWORDS);
		SHORT_DESCRIPTION = getSession().getEnumerationManager().getEnumerationValue(eType, Article.SHORT_DESCRIPTION);
		LONG_DESCRIPTION = getSession().getEnumerationManager().getEnumerationValue(eType, Article.LONG_DESCRIPTION);
		CATALOGVERSION = getSession().getEnumerationManager().getEnumerationValue(eType, "CatalogVersion");
		ORDER = getSession().getEnumerationManager().getEnumerationValue(eType, Article.ORDER);
	}

	protected String getAttributeQualifier(final EnumerationValue eValue, final Map mapping)
	{
		return ((AttributeDescriptor) mapping.get(eValue)).getQualifier();
	}

	protected ItemAttributeMap getValues(final BMECatImportCronJob cronJob, final AbstractValueObject valueObject,
			final boolean update)
	{
		final Article article = (Article) valueObject;
		final ItemAttributeMap values = new ItemAttributeMap();

		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (getMapping().containsKey(SUPPLIER_AID))
			{
				values.put(getAttributeQualifier(SUPPLIER_AID, getMapping()), article.getSupplierAID());
			}
			if (getMapping().containsKey(SUPPLIER_ALT_AID))
			{
				values.put(getAttributeQualifier(SUPPLIER_ALT_AID, getMapping()), article.getSupplierAlternativeAID());
			}
			if (getMapping().containsKey(CONTENT_UNIT))
			{
				values.put(getAttributeQualifier(CONTENT_UNIT, getMapping()), cronJob.getOrCreateUnit(article.getContentUnit()));
			}
			if (getMapping().containsKey(ORDER_UNIT))
			{
				values.put(getAttributeQualifier(ORDER_UNIT, getMapping()), cronJob.getOrCreateUnit(article.getOrderUnit()));
			}
			if (getMapping().containsKey(DELIVERY_TIME))
			{
				values.put(getAttributeQualifier(DELIVERY_TIME, getMapping()), article.getDeliveryTime());
			}
			if (getMapping().containsKey(EAN))
			{
				values.put(getAttributeQualifier(EAN, getMapping()), article.getEan());
			}
			if (getMapping().containsKey(ERP_GROUP_BUYER))
			{
				values.put(getAttributeQualifier(ERP_GROUP_BUYER, getMapping()), article.getErpGroupBuyer());
			}
			if (getMapping().containsKey(ERP_GROUP_SUPPLIER))
			{
				values.put(getAttributeQualifier(ERP_GROUP_SUPPLIER, getMapping()), article.getErpGroupSupplier());
			}
			if (getMapping().containsKey(MIN_ORDER_QUANTITY))
			{
				values.put(getAttributeQualifier(MIN_ORDER_QUANTITY, getMapping()), article.getMinOrderQuantity());
			}
			if (getMapping().containsKey(NUMBER_CONTENT_UNITS))
			{
				values.put(getAttributeQualifier(NUMBER_CONTENT_UNITS, getMapping()), article.getNumberContentUnits());
			}
			if (getMapping().containsKey(ORDER))
			{
				values.put(getAttributeQualifier(ORDER, getMapping()), article.getOrder());
			}
			if (getMapping().containsKey(ORDER_QUANTITY_INTERVAL))
			{
				values.put(getAttributeQualifier(ORDER_QUANTITY_INTERVAL, getMapping()), article.getOrderQuantityInterval());
			}
			if (getMapping().containsKey(PRICE_QUANTITY))
			{
				values.put(getAttributeQualifier(PRICE_QUANTITY, getMapping()), article.getPriceQuantity());
			}
			if (getMapping().containsKey(SPECIAL_TREATMENT_CLASSES))
			{
				values.put(getAttributeQualifier(SPECIAL_TREATMENT_CLASSES, getMapping()), article.getSpecialTreatmentClasses());
			}
			if (getMapping().containsKey(BUYER_IDS))
			{
				values.put(getAttributeQualifier(BUYER_IDS, getMapping()), getBuyerIDValues(cronJob, article.getBuyerIDs()));
			}
			if (getMapping().containsKey(MANUFACTURER_NAME))
			{
				values.put(getAttributeQualifier(MANUFACTURER_NAME, getMapping()), article.getManufacturerName());
			}
			if (getMapping().containsKey(MANUFACTURER_AID))
			{
				values.put(getAttributeQualifier(MANUFACTURER_AID, getMapping()), article.getManufacturerAID());
			}
			if (getMapping().containsKey(CATALOGVERSION))
			{
				values.put(getAttributeQualifier(CATALOGVERSION, getMapping()), cronJob.getCatalogVersion());
			}
		}

		//localized
		if (getMapping().containsKey(MANUFACTURER_TYPE_DESCRIPTION))
		{
			values.put(getAttributeQualifier(MANUFACTURER_TYPE_DESCRIPTION, getMapping()), article.getManufacturerTypeDescription());
		}
		if (getMapping().containsKey(REMARKS))
		{
			values.put(getAttributeQualifier(REMARKS, getMapping()), article.getRemarks());
		}
		if (getMapping().containsKey(SEGMENT))
		{
			values.put(getAttributeQualifier(SEGMENT, getMapping()), article.getSegment());
		}
		if (getMapping().containsKey(KEYWORDS))
		{
			values.put(getAttributeQualifier(KEYWORDS, getMapping()), getOrCreateKeywords(cronJob, article.getKeywords()));
		}
		if (getMapping().containsKey(SHORT_DESCRIPTION))
		{
			values.put(getAttributeQualifier(SHORT_DESCRIPTION, getMapping()), article.getShortDescription());
		}
		if (getMapping().containsKey(LONG_DESCRIPTION))
		{
			values.put(getAttributeQualifier(LONG_DESCRIPTION, getMapping()), article.getLongDescription());
		}
		if (getMapping().containsKey(STATUS))
		{
			values.put(getAttributeQualifier(STATUS, getMapping()), getArticleStatusMap(article));
		}

		if (article.getStartLineNumber() > -1)
		{
			values.put(GeneratedCatalogConstants.Attributes.Product.STARTLINENUMBER, Integer.valueOf(article.getStartLineNumber()));
		}

		if (article.getEndLineNumber() > -1)
		{
			values.put(GeneratedCatalogConstants.Attributes.Product.ENDLINENUMBER, Integer.valueOf(article.getEndLineNumber()));
		}

		values.put(GeneratedCatalogConstants.Attributes.Product.XMLCONTENT, article.getXML());

		return values;
	}

	private Map getArticleStatusMap(final Article article)
	{
		final Map articleStatusMap;

		if (article.getStatus() != null)
		{
			articleStatusMap = new HashMap();
			for (final Iterator it = article.getStatus().entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry entry = (Map.Entry) it.next();
				final String status = (String) entry.getKey();
				final EnumerationValue statusEnum = getSession().getEnumerationManager().getEnumerationValue(
						GeneratedCatalogConstants.TC.ARTICLESTATUS, status);
				if (statusEnum != null)
				{
					articleStatusMap.put(statusEnum, entry.getValue());
				}
				else
				{
					if (isErrorEnabled())
					{
						error("ArticleStatus EnumerationValue with code: " + status + " not found!");
					}
				}
			}
		}
		else
		{
			articleStatusMap = null;
		}

		return articleStatusMap;
	}

	protected static final String LAST_VALID_ARTICLE_KEY = "lastValidArticleCode";
	protected static final String NONE = "none";


	/**
	 * @see BMECatImportStep#performStep(CronJob)
	 */
	@Override
	protected void performStep(final CronJob forSchedule) throws AbortCronJobException
	{
		try
		{
			super.performStep(forSchedule);
		}
		finally
		{
			forSchedule.setTransientObject(LAST_VALID_ARTICLE_KEY, null);
		}
	}

	@SuppressWarnings("PMD.TooFewBranchesForASwitchStatement")
	protected String getLastValidArticleCode(final BMECatImportCronJob cronJob, final Catalog catalogValueObj)
	{
		if (!cronJob.isChangeRecordingEnabledAsPrimitive())
		{
			return NONE;
		}
		String lastValidArticleCode = (String) cronJob.getTransientObject(LAST_VALID_ARTICLE_KEY);
		if (lastValidArticleCode == null)
		{
			ChangeDescriptor lastOne = null;
			switch (catalogValueObj.getTransactionMode())
			{
				case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
					lastOne = getMostRecentChange(cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT);
					break;
				case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
					lastOne = getMostRecentChange(cronJob, BMECatConstants.ChangeTypes.UPDATE_PRODUCT);
					break;
			}
			final Product product = lastOne != null ? (Product) lastOne.getChangedItem() : null;
			cronJob.setTransientObject(LAST_VALID_ARTICLE_KEY, product != null ? lastValidArticleCode = product.getCode() : NONE);
		}
		return NONE.equals(lastValidArticleCode) ? null : lastValidArticleCode;
	}

	protected void lastValidArticleFound(final CronJob cronJob)
	{
		cronJob.setTransientObject(LAST_VALID_ARTICLE_KEY, NONE);
	}


	protected Product createArticle(final BMECatImportCronJob cronJob, final Catalog catalog, final Article article)
	{
		Product newProduct = null;
		try
		{
			final String run;
			if (cronJob.isRunningRestart())
			{
				run = "SECONDRUN: ";
			}
			else
			{
				run = "FIRSTRUN: ";
			}
			final ItemAttributeMap attributeMap = getValues(cronJob, article, false);

			final List variantAttributeMaps = new LinkedList();
			final VariantType variantType = setVariantFeatures(cronJob, attributeMap, article, variantAttributeMaps);
			attributeMap.put(VariantsConstants.Attributes.Product.VARIANTTYPE, variantType);
			newProduct = (Product) getArticleType().newInstance(attributeMap);
			if (isInfoEnabled())
			{
				info(run + " created Product " + newProduct.getCode() + "< pk: " + newProduct.getPK().toString());
			}
			if (cronJob.isChangeRecordingEnabledAsPrimitive())
			{
				addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT, newProduct,
						"... created Product " + newProduct.getCode());
			}

			// set features
			setFeatures(cronJob, newProduct, article);

			// create Variants
			for (final Iterator it = variantAttributeMaps.iterator(); it.hasNext();)
			{
				final Map variantAttributes = (Map) it.next();
				variantAttributes.put(VariantProduct.BASEPRODUCT, newProduct);
				final VariantProduct variantProduct = (VariantProduct) variantType.newInstance(variantAttributes);
				if (isInfoEnabled())
				{
					info("created variant product >" + variantProduct.getCode() + "<");
				}
				if (cronJob.isChangeRecordingEnabledAsPrimitive())
				{
					addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_VARIANTPRODUCT, variantProduct,
							"... created variant product " + variantProduct.getCode());
				}
			}
		}
		catch (final JaloGenericCreationException e)
		{
			if (isErrorEnabled())
			{
				error("Could not create Product with SupplierID '" + article.getSupplierAID() + "'! (" + e.getMessage() + ")"
						+ "due to " + e.getLocalizedMessage() + " \n " + Utilities.getStackTraceAsString(e));
			}
		}
		catch (final JaloAbstractTypeException e)
		{
			if (isErrorEnabled())
			{
				error("Could not create Product with SupplierID '" + article.getSupplierAID() + "'! (" + e.getMessage() + ")"
						+ "due to " + e.getLocalizedMessage() + " \n " + Utilities.getStackTraceAsString(e));
			}
		}
		return newProduct;
	}



	/**
	 * Implements default setting of category type and the attribute mapping if these attributes are not specified during
	 * step creation.
	 * 
	 * @param ctx
	 * @param item
	 * @param nonInitialAttributes
	 * @throws JaloBusinessException
	 */
	@Override
	public void setNonInitialAttributes(final SessionContext ctx, final Item item, final ItemAttributeMap nonInitialAttributes)
			throws JaloBusinessException
	{
		final BMECatArticleStep newStep = (BMECatArticleStep) item;

		newStep.initializeArticleAttributeEnums();
		final ItemAttributeMap myMap = new ItemAttributeMap(nonInitialAttributes);
		ComposedType articleType = (ComposedType) nonInitialAttributes.get(BMECatArticleStep.ARTICLETYPE);
		Map mapping = (Map) nonInitialAttributes.get(BMECatArticleStep.ARTICLEATTRIBUTEMAPPING);
		if (articleType == null)
		{
			if (mapping != null && !mapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define article attribute mapping without specifying the article type", 0);
			}
			else
			{
				articleType = newStep.getSession().getTypeManager().getComposedType(Product.class);
				myMap.put(BMECatArticleStep.ARTICLETYPE, articleType);
			}
		}
		if (mapping == null || mapping.isEmpty())
		{
			mapping = newStep.createDefaultMapping(articleType);
			myMap.put(BMECatArticleStep.ARTICLEATTRIBUTEMAPPING, mapping);
		}
		MappingUtils.checkAttributeMapping(Product.class, articleType, mapping);
		super.setNonInitialAttributes(ctx, item, myMap);
	}

	protected Map getBuyerIDValues(final BMECatImportCronJob cronJob, final Map buyerIDValueObject)
	{
		if (buyerIDValueObject == null)
		{
			return null;
		}

		final Map values = new HashMap();
		final EnumerationManager enumManager = cronJob.getSession().getEnumerationManager();
		final EnumerationType eType = enumManager.getEnumerationType(CatalogConstants.TC.IDTYPE);
		for (final Iterator it = buyerIDValueObject.keySet().iterator(); it.hasNext();)
		{
			final String key = (String) it.next();
			EnumerationValue eValue = null;
			try
			{
				eValue = enumManager.getEnumerationValue(eType, key);
			}
			catch (final JaloItemNotFoundException exp)
			{
				//nothing
			}
			if (eValue != null)
			{
				values.put(eValue, buyerIDValueObject.get(key));
			}
			else
			{
				if (isWarnEnabled())
				{
					warn("Could not find enumvalue for '" + key + "'!");
				}
			}
		}
		return values;
	}

	/**
	 * Returns the locale copy of the article attribute mappings.
	 * 
	 * @return the locale copy of the article attribute mapping.
	 */
	protected Map getMapping()
	{
		if (mapping == null)
		{
			mapping = getAllArticleAttributeMapping();
		}
		return mapping;
	}

	private void arrangeArticle(final Article article, final Product product)
	{
		// TODO again !!!

		//		for( final Iterator it = article.getArticleFeatures().iterator(); it.hasNext(); )
		//		{
		//			final ArticleFeatures articleFeatures = (ArticleFeatures)it.next();
		//			final String referenceFeatureSystemName = articleFeatures.getReferenceFeatureSystemName();
		//			if( referenceFeatureSystemName != null )
		//			{
		//				final String referenceFeatureGroupId = articleFeatures.getReferenceFeatureGroupId();
		//				if( referenceFeatureSystemName.toUpperCase().startsWith( EclassCategoryImportStep.CATALOG_ECLASS.toUpperCase() ) )
		//				{
		//					final EnumerationValue eClassVersion = getEclassVersion( referenceFeatureSystemName );
		//					if( eClassVersion != null )
		//					{
		//						// cut potential '-' out of the classification number
		//						final StringBuilder classificationNumber = new StringBuilder();
		//						for( final StringTokenizer t = new StringTokenizer( referenceFeatureGroupId, "-", false ); t.hasMoreTokens(); )
		//						{
		//							classificationNumber.append( t.nextToken() );
		//						}
		//						//
		//						final ClassificationSystem eclassClassSystem = getCatalogManager().getClassificationSystem( EclassCategoryImportStep.CATALOG_ECLASS );
		//						if( eclassClassSystem != null )
		//						{
		//							try
		//							{
		//								final CatalogVersion catVersion = eclassClassSystem.getCatalogVersion( (String)eClassVersion.getAttribute( GeneratedCatalogConstants.Attributes.EclassVersion.ECLASSVERSION ) );
		//								if( catVersion != null )
		//								{
		//									final EclassCategory eClassCategory = (EclassCategory)getCatalogManager().getEclassCategoryByClassificationNumber( catVersion, classificationNumber.toString() );
		//									eClassCategory.addToProducts( product );
		//
		//									if( isInfoEnabled() )
		//									{
		//										info( "added product: " + product.getCode() + " to eclass category: " + classificationNumber.toString() + "of version: " + catVersion );
		//									}
		//								}
		//							}
		//							catch( JaloInvalidParameterException e )
		//							{
		//								error( e.getLocalizedMessage() );
		//								e.printStackTrace();
		//							}
		//							catch( JaloSecurityException e )
		//							{
		//								error( e.getLocalizedMessage() );
		//								e.printStackTrace();
		//							}
		//						}
		//					}
		//				}
		//				//etim
		//				else if( referenceFeatureSystemName.toUpperCase().startsWith( AbstractEtimImportStep.ETIM.toUpperCase() ) )
		//				{
		//					final EnumerationValue etimVersion = getEtimVersion( referenceFeatureSystemName );
		//					if( etimVersion != null )
		//					{
		//						final ClassificationSystem etimClassSystem = getCatalogManager().getClassificationSystem( AbstractEtimImportStep.ETIM );
		//						if( etimClassSystem != null )
		//						{
		//							try
		//							{
		//								final CatalogVersion etimCatVersion = etimClassSystem.getCatalogVersion( (String)etimVersion.getAttribute( GeneratedCatalogConstants.Attributes.EtimVersion.ETIMVERSION ) );
		//								if( etimCatVersion != null )
		//								{
		//									if( isInfoEnabled() )
		//									{
		//										info( "searching for category: " + referenceFeatureGroupId + " to add..." );
		//									}
		//									final Category etimCategory = getCatalogManager().getCatalogCategory( etimCatVersion, referenceFeatureGroupId );
		//									if( etimCategory != null )
		//									{
		//										etimCategory.addProduct( product );
		//										if( isInfoEnabled() )
		//										{
		//											info( "added product: " + product.getCode() + " to etim category: " + referenceFeatureGroupId );
		//										}
		//									}
		//								}
		//							}
		//							catch( JaloInvalidParameterException e )
		//							{
		//								error( e.getLocalizedMessage() );
		//								e.printStackTrace();
		//							}
		//							catch( JaloSecurityException e )
		//							{
		//								error( e.getLocalizedMessage() );
		//								e.printStackTrace();
		//							}
		//						}
		//					}
		//				}
		//				//unspsc
		//				else if( referenceFeatureSystemName.toUpperCase().startsWith( UnspscImportStep.UNSPSC.toUpperCase() ) )
		//				{
		//					if( referenceFeatureSystemName.endsWith( UnspscImportStep.V5_0301 ) )
		//					{
		//						final ClassificationSystem unspscClassSystem = getCatalogManager().getClassificationSystem( UnspscImportStep.UNSPSC );
		//						if( unspscClassSystem != null )
		//						{
		//							final CatalogVersion unspscVersion = unspscClassSystem.getCatalogVersion( UnspscImportStep.V5_0301 );
		//							if( unspscVersion != null )
		//							{
		//								if( isInfoEnabled() )
		//									info( "searching for unspsc category: " + referenceFeatureGroupId + " to add..." );
		//								final Category unspscCategory = getCatalogManager().getCatalogCategory( unspscVersion, referenceFeatureGroupId );
		//								if( unspscCategory != null )
		//								{
		//									unspscCategory.addProduct( product );
		//									if( isInfoEnabled() )
		//										info( "added product: " + product.getCode() + " to unspsc category: " + referenceFeatureGroupId );
		//								}
		//							}
		//						}
		//					}
		//				}				
		//			}
		//		}	
	}

	/**
	 * BMECatArticleStep.FeatureComparator
	 * 
	 */
	public static class FeatureComparator implements Comparator
	{
		public int compare(final Object object1, final Object object2)
		{
			final QualifierFeaturePair pair1 = (QualifierFeaturePair) object1;
			final QualifierFeaturePair pair2 = (QualifierFeaturePair) object2;
			final Integer integer1 = pair1 == null ? null : pair1.getFeature().getForder();
			final int order1 = integer1 == null ? 0 : integer1.intValue();
			final Integer integer2 = pair2 == null ? null : pair2.getFeature().getForder();
			final int order2 = integer2 == null ? 0 : integer2.intValue();
			if (order1 < order2)
			{
				return -1;
			}
			else
			{
				if (order1 == order2)
				{
					return pair1.getQualifier().compareTo(pair2.getQualifier());
				}
				else
				{
					return 1;
				}
			}
		}
	}

	public Comparator getFeatureComparator()
	{
		return new FeatureComparator();
	}

	/**
	 * BMECatArticleStep.VariantFeatureComparator
	 * 
	 */
	public static class VariantFeatureComparator implements Comparator
	{
		public int compare(final Object object1, final Object object2)
		{

			final QualifierTypePair pair1 = (QualifierTypePair) object1;
			final QualifierTypePair pair2 = (QualifierTypePair) object2;
			final int order1 = pair1 == null ? 0 : pair1.getOrder().intValue();
			final int order2 = pair2 == null ? 0 : pair2.getOrder().intValue();
			return (order1 < order2 ? -1 : (order1 == order2 ? 0 : 1));
		}
	}

	/**
	 * BMECatArticleStep.QualifierTypePair
	 * 
	 */
	public static class QualifierTypePair
	{
		private final String qualifier;
		private final Type type;
		private final Integer order;

		public QualifierTypePair(final Integer order, final String qualifier, final Type type)
		{
			this.order = order == null ? Integer.valueOf(0) : order;
			this.qualifier = qualifier;
			this.type = type;
		}

		public String getQualifier()
		{
			return qualifier;
		}

		public Type getType()
		{
			return type;
		}

		public Integer getOrder()
		{
			return order;
		}

		@Override
		public String toString()
		{
			return "qualifier >" + getQualifier() + "< type >" + getType() + "< order >" + order.toString() + "<";
		}
	}

	/**
	 * Helper class for sorting in FeatureComparator
	 * 
	 * 
	 */
	public static class QualifierFeaturePair
	{
		private final String qualifier;
		private final Feature feature;

		public QualifierFeaturePair(final String qualifier, final Feature feature)
		{
			this.qualifier = qualifier;
			this.feature = feature;
		}

		/**
		 * @return Returns the feature.
		 */
		public Feature getFeature()
		{
			return feature;
		}

		/**
		 * @return Returns the group (with system info and group id) that the feature belongs to
		 */
		public String getQualifier()
		{
			return qualifier;
		}

	}

	public ProductUpdateVersion createProductUpdateVersion(final CronJob cronJob, final Product product)
	{
		try
		{
			final ComposedType type = getSession().getTypeManager().getComposedType(ProductUpdateVersion.class);
			final HashMap attributes = new HashMap();
			attributes.put(ProductUpdateVersion.ORIGINALPRODUCT, product);
			attributes.put(ProductUpdateVersion.ORIGINALPRODUCTCODE, product.getCode());
			final ProductUpdateVersion updateVersion = (ProductUpdateVersion) type.newInstance(attributes);
			updateVersion.setAllProperties(copyProductAttributes((BMECatImportCronJob) cronJob, product));
			//			final ArrayList features = new ArrayList();
			//			final int i = 0;
			// TODO re-enable feature backup 
			//	      for(Iterator it=getCatalogManager().getFeatures(product).iterator();it.hasNext();i++)
			//	      {	         
			//	         ProductFeature feature = (ProductFeature)it.next();
			//	         ClassificationAttributeUnit u = feature.getUnit(); 
			//	         final FeatureContainer container = new FeatureContainer(
			//         		feature.getQualifier(), 
			//         		(String[])feature.getValues().toArray(new String[feature.getValues().size()]),
			//               u != null ? u.getCode() : null, 
			//               new Integer(i), 
			//               feature.getValueDetails(), 
			//               feature.getDescription()
			//	         );
			//	         features.add(container);
			//	      }
			//	      updateVersion.setProperty(ProductUpdateVersion.FEATURES, features);
			return updateVersion;
		}
		catch (final JaloGenericCreationException x)
		{
			throw new JaloSystemException(x, "", 0);
		}
		catch (final JaloBusinessException x)
		{
			throw new JaloSystemException(x, "", 0);
		}
	}

	protected void initializeCopyAttributes()
	{
		unlocalizedProductAttributes = new HashSet();
		if (getMapping().containsKey(SUPPLIER_AID))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(SUPPLIER_AID, getMapping()));
		}
		if (getMapping().containsKey(SUPPLIER_ALT_AID))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(SUPPLIER_ALT_AID, getMapping()));
		}
		if (getMapping().containsKey(CONTENT_UNIT))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(CONTENT_UNIT, getMapping()));
		}
		if (getMapping().containsKey(ORDER_UNIT))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(ORDER_UNIT, getMapping()));
		}
		if (getMapping().containsKey(DELIVERY_TIME))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(DELIVERY_TIME, getMapping()));
		}
		if (getMapping().containsKey(EAN))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(EAN, getMapping()));
		}
		if (getMapping().containsKey(ERP_GROUP_BUYER))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(ERP_GROUP_BUYER, getMapping()));
		}
		if (getMapping().containsKey(ERP_GROUP_SUPPLIER))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(ERP_GROUP_SUPPLIER, getMapping()));
		}
		if (getMapping().containsKey(MIN_ORDER_QUANTITY))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(MIN_ORDER_QUANTITY, getMapping()));
		}
		if (getMapping().containsKey(NUMBER_CONTENT_UNITS))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(NUMBER_CONTENT_UNITS, getMapping()));
		}
		if (getMapping().containsKey(ORDER))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(ORDER, getMapping()));
		}
		if (getMapping().containsKey(ORDER_QUANTITY_INTERVAL))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(ORDER_QUANTITY_INTERVAL, getMapping()));
		}
		if (getMapping().containsKey(PRICE_QUANTITY))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(PRICE_QUANTITY, getMapping()));
		}
		if (getMapping().containsKey(SPECIAL_TREATMENT_CLASSES))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(SPECIAL_TREATMENT_CLASSES, getMapping()));
		}
		if (getMapping().containsKey(BUYER_IDS))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(BUYER_IDS, getMapping()));
		}
		if (getMapping().containsKey(MANUFACTURER_NAME))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(MANUFACTURER_NAME, getMapping()));
		}
		if (getMapping().containsKey(MANUFACTURER_AID))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(MANUFACTURER_AID, getMapping()));
		}
		if (getMapping().containsKey(CATALOGVERSION))
		{
			unlocalizedProductAttributes.add(getAttributeQualifier(CATALOGVERSION, getMapping()));
		}

		localizedProductAttributes = new HashSet();
		//localized
		if (getMapping().containsKey(MANUFACTURER_TYPE_DESCRIPTION))
		{
			localizedProductAttributes.add(getAttributeQualifier(MANUFACTURER_TYPE_DESCRIPTION, getMapping()));
		}
		if (getMapping().containsKey(REMARKS))
		{
			localizedProductAttributes.add(getAttributeQualifier(REMARKS, getMapping()));
		}
		if (getMapping().containsKey(SEGMENT))
		{
			localizedProductAttributes.add(getAttributeQualifier(SEGMENT, getMapping()));
		}
		if (getMapping().containsKey(KEYWORDS))
		{
			localizedProductAttributes.add(getAttributeQualifier(KEYWORDS, getMapping()));
		}
		if (getMapping().containsKey(SHORT_DESCRIPTION))
		{
			localizedProductAttributes.add(getAttributeQualifier(SHORT_DESCRIPTION, getMapping()));
		}
		if (getMapping().containsKey(LONG_DESCRIPTION))
		{
			localizedProductAttributes.add(getAttributeQualifier(LONG_DESCRIPTION, getMapping()));
		}
		if (getMapping().containsKey(STATUS))
		{
			localizedProductAttributes.add(getAttributeQualifier(STATUS, getMapping()));
		}
	}

	public JaloPropertyContainer copyProductAttributes(final BMECatImportCronJob cronJob, final Product product)
	{
		final Language lang = cronJob.getImportLanguage();
		if (isDebugEnabled())
		{
			debug("storing update version: " + product.getCode() + "lang: " + lang.getIsoCode());
		}
		final SessionContext ctx = getSession().createSessionContext();
		ctx.setLanguage(lang);
		try
		{
			final JaloPropertyContainer properties = getSession().createPropertyContainer();
			final Map attributes = product.getAllAttributes();
			if (!cronJob.isLocalizationUpdateAsPrimitive())
			{
				for (final Iterator it = unlocalizedProductAttributes.iterator(); it.hasNext();)
				{
					final String qualifier = (String) it.next();
					properties.setProperty(qualifier, attributes.get(qualifier));
				}
			}
			for (final Iterator it = localizedProductAttributes.iterator(); it.hasNext();)
			{
				final String qualifier = (String) it.next();
				properties.setLocalizedProperty(qualifier, attributes.get(qualifier));
			}
			return properties;
		}
		catch (final JaloSecurityException x)
		{
			throw new JaloSystemException(x, null, 0);
		}

	}

	protected Map copyProductAttributes(final BMECatImportCronJob cronJob, final ProductUpdateVersion updateVersion)
	{
		final Language lang = cronJob.getImportLanguage();
		if (isDebugEnabled())
		{
			debug("retrieving update version: " + updateVersion.getOriginalProductCode() + "lang: " + lang.getIsoCode());
		}
		final SessionContext ctx = getSession().createSessionContext();
		ctx.setLanguage(lang);

		final Map attributes = new HashMap();
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			for (final Iterator it = unlocalizedProductAttributes.iterator(); it.hasNext();)
			{
				final String qualifier = (String) it.next();
				attributes.put(qualifier, updateVersion.getProperty(qualifier));
			}
		}
		for (final Iterator it = localizedProductAttributes.iterator(); it.hasNext();)
		{
			final String qualifier = (String) it.next();
			attributes.put(qualifier, updateVersion.getLocalizedProperty(ctx, qualifier));
		}
		return attributes;
	}
}

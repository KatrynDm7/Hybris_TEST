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
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ProductReference;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PDTRow;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.price.DiscountInformation;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.order.price.ProductPriceInformations;
import de.hybris.platform.jalo.order.price.TaxInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.JaloTools;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.variants.jalo.VariantsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Article&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticleTagWriter extends XMLTagWriter
{
	//	private BMECatExportContext exportCtx = null;
	private static final Logger LOG = Logger.getLogger(ArticleTagWriter.class.getName());

	/**
	 * @param parent
	 */
	public ArticleTagWriter(final TNewCatalogTagWriter parent)
	{
		super(parent);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.SUPPLIER_AID, true));
		addSubTagWriter(new ArticleDetailsTagWriter(this));
		addSubTagWriter(new ArticleFeaturesTagWriter(this));
		addSubTagWriter(new ArticleOrderDetailsTagWriter(this));
		addSubTagWriter(new ArticlePriceDetailsTagWriter(this));
		addSubTagWriter(new MimeInfoTagWriter(this));
		addSubTagWriter(new ArticleReferenceTagWriter(this));
		addSubTagWriter(new UserDefinedExtensionsTagWriter(this));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE;
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final BMECatExportContext exportCtx = (BMECatExportContext) object;
		final Product product = exportCtx.getProduct();
		final Collection classificationSystemVersions = exportCtx.getClassificationSystemVersions();

		getSubTagWriter(BMECatConstants.XML.TAG.SUPPLIER_AID).write(xmlOut, product.getCode());
		getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_DETAILS).write(xmlOut, product);

		if ( /* getCatalogManager().getFeatures( product ).size() > 0 || */VariantsManager.getInstance().isBaseProduct(product))
		{
			getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_FEATURES).write(xmlOut, exportCtx);
		}

		final Map versionCategoriesMap = new HashMap();
		final CatalogManager catalogManager = CatalogManager.getInstance();
		
		/* fix for BMEC-267 
		 * 
		 * There is a strange commented out classification query above, so I am not
		 * sure if this is the right fix.
		 * The code below at least checks for classification features, and, if available
		 * it adds them to the "versionCategoriesMap", which is used to create the XML
		 * sub-document.
		 * 
		 * 
		 */
		for (final ClassificationClass cc : catalogManager.getClassificationClasses(product))
		{
			final CatalogVersion cv = catalogManager.getCatalogVersion(cc);
			if (classificationSystemVersions.contains(cv))
			{
				Collection categories = (Collection) versionCategoriesMap.get(cv);
				if (categories == null)
				{
					categories = new HashSet();
					versionCategoriesMap.put(cv, categories);
				}
				categories.add(cc);
			}
		}
		
		/* end fix BEC-267 */
		
		for (final Iterator it = getCategoryManager().getCategoriesByProduct(product).iterator(); it.hasNext();)
		{
			final Category cat = (Category) it.next();
			final CatalogVersion catVersion = catalogManager.getCatalogVersion(cat);

			if (classificationSystemVersions.contains(catVersion))
			{
				Collection categories = (Collection) versionCategoriesMap.get(catVersion);
				if (categories == null)
				{
					categories = new HashSet();
					versionCategoriesMap.put(catVersion, categories);
				}
				categories.add(cat);
			}
		}

		for (final Iterator it = classificationSystemVersions.iterator(); it.hasNext();)
		{
			final ClassificationSystemVersion classSysVersion = (ClassificationSystemVersion) it.next();
			final Collection categories = (Collection) versionCategoriesMap.get(classSysVersion);

			if (categories != null)
			{
				if (categories.isEmpty())
				{ //NOPMD
				  //do nothing
				}
				else
				{
					if (categories.size() > 1)
					{
						LOG.warn("bmecat export: product " + product.getCode()
								+ " is assigned to more than 1 category of classification system " + classSysVersion.getVersion()
								+ ". exporting first only.");
					}

					final ClassificationClass cat = (ClassificationClass) categories.iterator().next();

					exportCtx.setClassificationSystemVersion(classSysVersion);
					exportCtx.setCategory(cat);
					getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_FEATURES).write(xmlOut, exportCtx);

					/*
					 * for( final Iterator catIt = categories.iterator(); catIt.hasNext(); ) {
					 * exportCtx.setClassificationSystemVersion( classSysVersion ); exportCtx.setCategory(
					 * (Category)catIt.next() ); getSubTagWriter( BMECatConstants.XML.TAG.ARTICLE_FEATURES ).write( xmlOut,
					 * exportCtx ); }
					 */
				}
			}
		}
		getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_ORDER_DETAILS).write(xmlOut, product);

		final Collection articlePriceDetailsContexts = getArticlePriceDetailsContexts(exportCtx);
		for (final Iterator it = articlePriceDetailsContexts.iterator(); it.hasNext();)
		{
			final ArticlePriceDetailsTagWriter.ArticlePriceDetailsContext aPriceDetailsCtx = (ArticlePriceDetailsTagWriter.ArticlePriceDetailsContext) it
					.next();
			getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_PRICE_DETAILS).write(xmlOut, aPriceDetailsCtx);
		}

		final Map purposeMediaMap = new LinkedMap();

		for (final Iterator it = getProductMediaMapping().entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			final Collection medias = (Collection) product.getProperty((String) entry.getValue());
			if (medias != null && !medias.isEmpty())
			{
				purposeMediaMap.put(entry.getKey(), medias);
			}
		}

		//reading/writing hybris specific product-media-attributes ('picture', 'thumbnail')
		final Map hybris2bmecatMediaMap = exportCtx.getHybris2BmecatMediaMap();
		for (final Iterator it = hybris2bmecatMediaMap.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			final String hybris = ((EnumerationValue) entry.getKey()).getCode();
			final String bmecat = ((EnumerationValue) entry.getValue()).getCode();

			final Media media = product.getProperty(hybris) instanceof Media ? (Media) product.getProperty(hybris) : null;
			if (media != null)
			{
				final Collection collection = (Collection) purposeMediaMap.get(bmecat);
				if (collection != null)
				{
					collection.add(media);
				}
				else
				{
					purposeMediaMap.put(bmecat, Collections.singletonList(media));
				}
			}
		}

		if (!purposeMediaMap.isEmpty())
		{
			exportCtx.setStringPurpose2MediaCollectionMap(purposeMediaMap);
			getSubTagWriter(BMECatConstants.XML.TAG.MIME_INFO).write(xmlOut, exportCtx);
		}

		final Collection productReferences = getCatalogManager().getProductReferences(product);
		if (productReferences != null)
		{
			for (final Iterator it = productReferences.iterator(); it.hasNext();)
			{
				final ProductReference productReference = (ProductReference) it.next();
				getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_REFERENCE).write(xmlOut, productReference);
			}
		}

		final UserDefinedExtensionsTagWriter udxTagWriter = (UserDefinedExtensionsTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS);
		if (!udxTagWriter.getAllSubTagWriter().isEmpty())
		{
			udxTagWriter.write(xmlOut, product);
		}
	}

	private Collection priceTypes;

	private Collection getDefaultBMECatPriceTypes()
	{
		if (priceTypes == null)
		{
			priceTypes = new HashSet();
			final EnumerationManager enumerationManager = JaloSession.getCurrentSession().getEnumerationManager();
			priceTypes.add(enumerationManager.getEnumerationValue(BMECatConstants.TC.BMECATPRICETYPEENUM,
					BMECatConstants.Enumerations.BMECatPriceTypeEnum.GROS_LIST));
			priceTypes.add(enumerationManager.getEnumerationValue(BMECatConstants.TC.BMECATPRICETYPEENUM,
					BMECatConstants.Enumerations.BMECatPriceTypeEnum.NET_CUSTOMER));
			priceTypes.add(enumerationManager.getEnumerationValue(BMECatConstants.TC.BMECATPRICETYPEENUM,
					BMECatConstants.Enumerations.BMECatPriceTypeEnum.NET_CUSTOMER_EXP));
			priceTypes.add(enumerationManager.getEnumerationValue(BMECatConstants.TC.BMECATPRICETYPEENUM,
					BMECatConstants.Enumerations.BMECatPriceTypeEnum.NET_LIST));
			priceTypes.add(enumerationManager.getEnumerationValue(BMECatConstants.TC.BMECATPRICETYPEENUM,
					BMECatConstants.Enumerations.BMECatPriceTypeEnum.NRP));
		}
		return priceTypes;
	}

	public Collection getArticlePriceDetailsContexts(final BMECatExportContext exportCtx)
	{

		final Product product = exportCtx.getProduct();
		final Map priceMapping = exportCtx.getPriceMapping();
		final Map dateRange2ArticlePriceDetailsContextMap = new HashMap();
		ArticlePriceDetailsTagWriter.ArticlePriceDetailsContext ctxWithoutDateRange = null;

		final EnumerationValue actualUserPriceGroup;
		try
		{
			actualUserPriceGroup = Europe1PriceFactory.getInstance().getUPG(JaloSession.getCurrentSession().getSessionContext(),
					exportCtx.getReferenceCustomer());
		}
		catch (final JaloPriceFactoryException e1)
		{
			throw new JaloSystemException(e1);
		}

		try
		{
			final EnumerationValue GROSS_LIST = EnumerationManager.getInstance().getEnumerationValue(
					BMECatConstants.TC.BMECATPRICETYPEENUM, BMECatConstants.Enumerations.BMECatPriceTypeEnum.GROS_LIST);
			for (final Iterator it = priceMapping.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry entry = (Map.Entry) it.next();

				final EnumerationValue bmeCatPriceType = (EnumerationValue) entry.getKey();
				final EnumerationValue userPriceGroup = (EnumerationValue) entry.getValue();

				final String bmeCatPriceTypeString;
				final boolean net;

				if (bmeCatPriceType.equals(GROSS_LIST))
				{
					net = false;
					bmeCatPriceTypeString = bmeCatPriceType.getCode();
				}
				else
				{
					if (getDefaultBMECatPriceTypes().contains(bmeCatPriceType))
					{
						net = true;
						bmeCatPriceTypeString = bmeCatPriceType.getCode();
					}
					else
					{
						bmeCatPriceTypeString = "udp_" + userPriceGroup.getCode();
						net = exportCtx.isUdpNet();
					}

				}
				/*
				 * adjust date if requested
				 */
				final Date date;
				if (exportCtx.getReferenceDate() != null)
				{
					date = exportCtx.getReferenceDate();
				}
				else
				{
					date = new Date();
				}
				final SessionContext upgCtx;
				/*
				 * adjust user price group if different from reference customer
				 */
				if (actualUserPriceGroup != userPriceGroup
						&& (actualUserPriceGroup == null || !actualUserPriceGroup.equals(userPriceGroup)))
				{
					upgCtx = JaloSession.getCurrentSession().createSessionContext();
					upgCtx.setAttribute(Europe1Constants.PARAMS.UPG, userPriceGroup);
				}
				else
				{
					upgCtx = JaloSession.getCurrentSession().getSessionContext();
				}
				// get all price infos (prices, taxes, discounts)
				final ProductPriceInformations pInfos = product.getAllPriceInformations(upgCtx, date, net);

				if (pInfos.getPrices() != null && !pInfos.getPrices().isEmpty())
				{
					// calculate taxes (fixed for each possible price)
					final Collection taxValues = createTaxValues(pInfos.getTaxes());
					final double tax = TaxValue.sumRelativeTaxValues(taxValues) / 100d;
					// calculate discounts (fixed for each possible price)
					final List discounts = createDiscountValues(pInfos.getDiscounts());
					// group prices
					for (final Iterator priceIt = pInfos.getPrices().iterator(); priceIt.hasNext();)
					{
						final PriceInformation priceInfo = (PriceInformation) priceIt.next();
						final Map priceQualifiers = priceInfo.getQualifiers();
						if (product.getUnit().equals(priceQualifiers.get(PriceRow.UNIT)))
						{
							final ArticlePriceDetailsTagWriter.ArticlePriceDetailsContext ctx;
							if (priceQualifiers.containsKey(PDTRow.DATE_RANGE))
							{
								final StandardDateRange dateRange = (StandardDateRange) priceQualifiers.get(PDTRow.DATE_RANGE);
								if (dateRange2ArticlePriceDetailsContextMap.keySet().contains(dateRange))
								{
									ctx = (ArticlePriceDetailsTagWriter.ArticlePriceDetailsContext) dateRange2ArticlePriceDetailsContextMap
											.get(dateRange);
								}
								else
								{
									ctx = new ArticlePriceDetailsTagWriter.ArticlePriceDetailsContext(dateRange.getStart(),
											dateRange.getEnd());
									dateRange2ArticlePriceDetailsContextMap.put(dateRange, ctx);
								}
							}
							else
							{
								if (ctxWithoutDateRange == null)
								{
									ctxWithoutDateRange = new ArticlePriceDetailsTagWriter.ArticlePriceDetailsContext();
								}
								ctx = ctxWithoutDateRange;
							}

							final Currency exportCurrency = exportCtx.getExportCurrency();
							// convert price net/gross if necessary
							final double price = JaloTools.convertPriceIfNecessary(priceInfo.getPrice(), net, exportCurrency, taxValues)
									.getValue();
							final ArticlePriceTagWriter.ArticlePriceContext articlePriceCtx = new ArticlePriceTagWriter.ArticlePriceContext(
									price, bmeCatPriceTypeString);

							articlePriceCtx.setPriceCurrency(exportCurrency);
							articlePriceCtx.setLowerBound(priceQualifiers.get(PriceRow.MIN_QUANTITY) != null ? ((Long) priceQualifiers
									.get(PriceRow.MIN_QUANTITY)).doubleValue() : 1d);
							articlePriceCtx.setTax(tax);
							articlePriceCtx.setPriceFactor(getPriceFactor(discounts, price, exportCurrency));
							ctx.addArticlePriceContext(articlePriceCtx);
						}
					}
				}
				// suppress products without prices
				else if (exportCtx.suppressProductsWithoutPrices())
				{
					throw new JaloInvalidParameterException("product " + product + " has no price - skipped", 0);
				}
			}
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e);
		}

		final Collection articlePriceDetailsContexts = new ArrayList(dateRange2ArticlePriceDetailsContextMap.values());
		if (ctxWithoutDateRange != null)
		{
			articlePriceDetailsContexts.add(ctxWithoutDateRange);
		}
		return articlePriceDetailsContexts;
	}

	private Collection createTaxValues(final Collection taxInfos) throws JaloPriceFactoryException
	{
		final Collection taxValues = new ArrayList();
		for (final Iterator it = taxInfos.iterator(); it.hasNext();)
		{
			final TaxValue taxValue = ((TaxInformation) it.next()).getTaxValue();
			taxValues.add(taxValue);
		}
		return Collections.unmodifiableCollection(taxValues);
	}

	private List createDiscountValues(final Collection discountInfos) throws JaloPriceFactoryException
	{
		final List discountValues = new ArrayList();
		for (final Iterator it = discountInfos.iterator(); it.hasNext();)
		{
			final DiscountValue discountValue = ((DiscountInformation) it.next()).getDiscountValue();
			discountValues.add(discountValue);
		}
		return Collections.unmodifiableList(discountValues);
	}

	private double getPriceFactor(final List discountValues, final double priceAmount, final Currency currency)
			throws JaloPriceFactoryException
	{
		final double sumAppliedValues = DiscountValue.sumAppliedValues(DiscountValue.apply(1.0, priceAmount, currency.getDigits()
				.intValue(), discountValues, currency.getIsoCode()));
		//TODO: getting unit factor 
		final double unitFactor = 1;
		return (1 / unitFactor) * (1 - (sumAppliedValues / priceAmount));
	}

	private Map getProductMediaMapping()
	{
		final Map mapping = new LinkedMap();
		mapping.put("thumbnail", CatalogConstants.Attributes.Product.THUMBNAILS);
		mapping.put("detail", CatalogConstants.Attributes.Product.DETAIL);
		mapping.put("normal", CatalogConstants.Attributes.Product.NORMAL);
		mapping.put("data_sheet", CatalogConstants.Attributes.Product.DATA_SHEET);
		mapping.put("logo", CatalogConstants.Attributes.Product.LOGO);
		mapping.put("others", CatalogConstants.Attributes.Product.OTHERS);
		return mapping;
	}

	private CatalogManager getCatalogManager()
	{
		return CatalogManager.getInstance();
	}

	private CategoryManager getCategoryManager()
	{
		return CategoryManager.getInstance();
	}

	protected UserDefinedExtensionsTagWriter getUserDefinedExtensionsTagWriter()
	{
		return (UserDefinedExtensionsTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS);
	}
}

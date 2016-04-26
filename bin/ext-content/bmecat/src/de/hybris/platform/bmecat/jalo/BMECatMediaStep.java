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
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.bmecat.parser.CatalogStructure;
import de.hybris.platform.bmecat.parser.Mime;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.Company;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.JobMedia;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * BMECatMediaStep
 * 
 * 
 */
public class BMECatMediaStep extends GeneratedBMECatMediaStep
{
	private static final Logger LOG = Logger.getLogger(BMECatMediaStep.class.getName());

	public static final String MEDIA_ROOT = "mediaRoot";
	public static final String MEDIAS_ZIP_FILE = "mediasZipFile";
	public static final String MEDIAS_ZIP_FILE_CHECKED = "mediasZipFileChecked";

	public static final String SEPARATOR = "/";

	private EnumerationValue NORMAL;
	private EnumerationValue THUMBNAIL;
	private EnumerationValue LOGO;
	private EnumerationValue OTHERS;
	private EnumerationValue DATA_SHEET;
	private EnumerationValue DETAIL;

	private static final String LAST_PROCESSED_PRODUCT = "lastProcessedProduct";
	private static final String LAST_PROCESSED_CATEGORY = "lastProcessedCategory";
	private static final String PROCESSED_SUPPLIER = "processedSupplier";

	private static final Class DEFAULT_MEDIA_TYPE = Media.class;
	private static final Class DEFAULT_PRODUCT_TYPE = Product.class;
	private static final Class DEFAULT_CATEGORY_TYPE = Category.class;

	private Map productmapping;
	private Map categorymapping;

	@Override
	public void setNonInitialAttributes(final SessionContext ctx, final Item item, final ItemAttributeMap nonInitialAttributes)
			throws JaloBusinessException
	{
		final BMECatMediaStep newStep = (BMECatMediaStep) item;

		final ItemAttributeMap myMap = new ItemAttributeMap(nonInitialAttributes);
		ComposedType mediaType = (ComposedType) nonInitialAttributes.get(BMECatMediaStep.MEDIATYPE);
		ComposedType productType = (ComposedType) nonInitialAttributes.get(BMECatMediaStep.PRODUCTTYPE);
		ComposedType categoryType = (ComposedType) nonInitialAttributes.get(BMECatMediaStep.CATEGORYTYPE);
		Map productmapping = (Map) nonInitialAttributes.get(BMECatMediaStep.PRODUCTMIMEPURPOSEMAPPING);
		Map categorymapping = (Map) nonInitialAttributes.get(BMECatMediaStep.CATEGORYMIMEPURPOSEMAPPING);

		if (mediaType == null)
		{
			if ((productmapping != null && !productmapping.isEmpty()) || (categorymapping != null && !categorymapping.isEmpty()))
			{
				throw new JaloInvalidParameterException(
						"Cannot define article/category attribute mapping without specifying the media type", 0);
			}
			else
			{
				mediaType = newStep.getSession().getTypeManager().getComposedType(DEFAULT_MEDIA_TYPE);
				myMap.put(BMECatMediaStep.MEDIATYPE, mediaType);
			}
		}
		if (productType == null)
		{
			if (productmapping != null && !productmapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define article attribute mapping without specifying the product type", 0);
			}
			else
			{
				productType = newStep.getSession().getTypeManager().getComposedType(DEFAULT_PRODUCT_TYPE);
				myMap.put(BMECatMediaStep.PRODUCTTYPE, productType);
			}
		}
		if (categoryType == null)
		{
			if (categorymapping != null && !categorymapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define category attribute mapping without specifying the category type", 0);
			}
			else
			{
				categoryType = newStep.getSession().getTypeManager().getComposedType(DEFAULT_CATEGORY_TYPE);
				myMap.put(BMECatMediaStep.CATEGORYTYPE, categoryType);
			}
		}
		if (productmapping == null || productmapping.isEmpty())
		{
			productmapping = newStep.createDefaultProductMapping(newStep.getSession().getTypeManager()
					.getComposedType(DEFAULT_PRODUCT_TYPE)); // TODO
			// getProductType()
			myMap.put(BMECatMediaStep.PRODUCTMIMEPURPOSEMAPPING, productmapping);
		}
		if (categorymapping == null || categorymapping.isEmpty())
		{
			categorymapping = newStep.createDefaultCategoryMapping(newStep.getSession().getTypeManager()
					.getComposedType(DEFAULT_CATEGORY_TYPE)); // TODO
			// getCategorytype()
			myMap.put(BMECatMediaStep.CATEGORYMIMEPURPOSEMAPPING, categorymapping);
		}
		super.setNonInitialAttributes(ctx, item, myMap);
	}

	protected Map createDefaultProductMapping(final ComposedType targetType)
	{
		final Map attributeMapping = new HashMap();
		initializeMimePurposeEnums();
		MappingUtils.addMapping(attributeMapping, targetType, DATA_SHEET, CatalogConstants.Attributes.Product.DATA_SHEET, this);
		//		MappingUtils.addMapping( attributeMapping, targetType, NORMAL, CatalogConstants.Attributes.Product.NORMAL , this);
		MappingUtils.addMapping(attributeMapping, targetType, NORMAL, Product.PICTURE, this);
		//		MappingUtils.addMapping( attributeMapping, targetType, THUMBNAIL, CatalogConstants.Attributes.Product.THUMBNAILS , this);
		MappingUtils.addMapping(attributeMapping, targetType, THUMBNAIL, Product.THUMBNAIL, this);
		MappingUtils.addMapping(attributeMapping, targetType, LOGO, CatalogConstants.Attributes.Product.LOGO, this);
		MappingUtils.addMapping(attributeMapping, targetType, OTHERS, CatalogConstants.Attributes.Product.OTHERS, this);
		//		MappingUtils.addMapping( attributeMapping, targetType, DETAIL, CatalogConstants.Attributes.Product.DETAIL , this);
		MappingUtils.addMapping(attributeMapping, targetType, DETAIL, Product.PICTURE, this);
		return attributeMapping;
	}

	protected Map createDefaultCategoryMapping(final ComposedType targetType)
	{
		final Map attributeMapping = new HashMap();
		initializeMimePurposeEnums();
		MappingUtils.addMapping(attributeMapping, targetType, DATA_SHEET, CatalogConstants.Attributes.Category.DATA_SHEET, this);
		//		MappingUtils.addMapping( attributeMapping, targetType, NORMAL, CatalogConstants.Attributes.Category.NORMAL, this );
		MappingUtils.addMapping(attributeMapping, targetType, NORMAL, Category.PICTURE, this);
		//		MappingUtils.addMapping( attributeMapping, targetType, THUMBNAIL, CatalogConstants.Attributes.Category.THUMBNAILS , this);
		MappingUtils.addMapping(attributeMapping, targetType, THUMBNAIL, Category.THUMBNAIL, this);
		MappingUtils.addMapping(attributeMapping, targetType, LOGO, CatalogConstants.Attributes.Category.LOGO, this);
		MappingUtils.addMapping(attributeMapping, targetType, OTHERS, CatalogConstants.Attributes.Category.OTHERS, this);
		//		MappingUtils.addMapping( attributeMapping, targetType, DETAIL, CatalogConstants.Attributes.Category.DETAIL , this);
		MappingUtils.addMapping(attributeMapping, targetType, DETAIL, Category.PICTURE, this);

		return attributeMapping;
	}

	private void initializeMimePurposeEnums()
	{
		final EnumerationType eType = getSession().getEnumerationManager().getEnumerationType(
				BMECatConstants.TC.BMECATMIMEPURPOSEENUM);
		DATA_SHEET = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.DATA_SHEET);
		NORMAL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.NORMAL);
		LOGO = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.LOGO);
		THUMBNAIL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.THUMBNAIL);
		OTHERS = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.OTHERS);
		DETAIL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.DETAIL);
	}

	// ********************************************************************************
	// import logic
	// ********************************************************************************

	/**
	 * @return true since this step is able to remove its created medias.
	 */
	@Override
	protected boolean canUndo(final CronJob forCronJob)
	{
		return true;
	}

	/**
	 * Removes the medias create by this step.
	 */
	@Override
	protected void undoStep(final CronJob forCronJob)
	{
		super.undoStep(forCronJob);

		int index = 0;
		for (Collection changes = getChanges(forCronJob, BMECatConstants.ChangeTypes.CREATE_MEDIA, 0, 100); changes != null
				&& !changes.isEmpty(); changes = getChanges(forCronJob, BMECatConstants.ChangeTypes.CREATE_MEDIA, 0, 100))
		{
			index += changes.size();
			for (final Iterator it = changes.iterator(); it.hasNext();)
			{
				final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
				final Media media = (Media) changeDescriptor.getChangedItem();
				try
				{
					if (media != null && media.isAlive())
					{
						media.remove();
					}
					changeDescriptor.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		if (isInfoEnabled())
		{
			info("removed " + index + " medias");
		}
	}

	@Override
	protected void performStep(final CronJob cronJob) throws AbortCronJobException
	{
		final ChangeDescriptor cdProduct = getMostRecentChange(cronJob, BMECatConstants.ChangeTypes.ADD_PRODUCT_MEDIA);
		if (cdProduct != null)
		{
			cronJob.setTransientObject(LAST_PROCESSED_PRODUCT, cdProduct.getChangedItem());
		}

		final ChangeDescriptor cdCategory = getMostRecentChange(cronJob, BMECatConstants.ChangeTypes.ADD_CATEGORY_MEDIA);
		if (cdCategory != null)
		{
			cronJob.setTransientObject(LAST_PROCESSED_CATEGORY, cdCategory.getChangedItem());
		}

		final ChangeDescriptor cdSupplier = getMostRecentChange(cronJob, BMECatConstants.ChangeTypes.ADD_SUPLLIER_MEDIA);
		if (cdSupplier != null)
		{
			cronJob.setTransientObject(PROCESSED_SUPPLIER, cdSupplier.getChangedItem());
		}

		try
		{
			super.performStep(cronJob);
		}
		finally
		{
			// TODO: delete temporary file here too
			cronJob.setTransientObject(MEDIAS_ZIP_FILE, null);
			cronJob.setTransientObject(MEDIAS_ZIP_FILE_CHECKED, null);

			cronJob.setTransientObject(LAST_PROCESSED_PRODUCT, null);
			cronJob.setTransientObject(LAST_PROCESSED_CATEGORY, null);
			cronJob.setTransientObject(PROCESSED_SUPPLIER, null);
		}
	}

	private String getMediaRoot(final BMECatImportCronJob cronJob)
	{
		return (String) cronJob.getTransientObject(MEDIA_ROOT);
	}

	private void setMediaRoot(final BMECatImportCronJob cronJob, final String mediaRoot)
	{
		cronJob.setTransientObject(MEDIA_ROOT, mediaRoot);
	}

	@Override
	protected void initializeBMECatImport(final Catalog catalog, final BMECatImportCronJob cronJob)
	{
		initializeMimePurposeEnums();

		//
		// BUILD UP MEDIA-ROOT
		//
		String mediaRoot = catalog.getMimeRootDirectory();
		if (StringUtils.isNotBlank(mediaRoot))
		{
			// TODO @see UNIX.FILE.SEPARATOR, WINDOWS.FILE.SEPARATOR
			if (!mediaRoot.endsWith(SEPARATOR))
			{
				mediaRoot += SEPARATOR;
			}
		}
		else
		{
			mediaRoot = "";
		}
		setMediaRoot(cronJob, mediaRoot);
		/*
		 * final Company alreadyImportedSupllier = (Company)cronJob.getTransientObject( PROCESSED_SUPPLIER ); if(
		 * alreadyImportedSupllier == null ) { if( catalog.getSupplier() != null && catalog.getSupplier().getMedias() !=
		 * null ) { importSupplierMedias( cronJob, catalog.getSupplier().getMedias(), cronJob.getSupplier() ); } } else {
		 * if( isInfoEnabled()) info( "Supplier media already imported! Skipping..." ); }
		 */
	}

	private ZipFile createMediaZip(final String mime, final CronJob cronJob, final InputStream dis)
	{
		if (mime.equals(ZIP_MIME_TYPE))
		{
			File temporaryMediasFile = null;

			try
			{
				temporaryMediasFile = File.createTempFile("temporaryMediasFile", ".temp");
				temporaryMediasFile.deleteOnExit();
				writeToFile(temporaryMediasFile, new BufferedInputStream(dis));
				return new ZipFile(temporaryMediasFile);
			}
			catch (final Exception e) // JaloSecurityException, JaloBusinessException, IOException
			{
				e.printStackTrace();
				if (temporaryMediasFile != null)
				{
					temporaryMediasFile.delete();
				}
			}
		}
		else if (mime.equals(XML_MIME_TYPE))
		{ //NOPMD
		  // nothing to do here
		}
		else
		{
			throw new IllegalStateException("unsupported job media mimetype '" + mime + "' !!");
		}
		return null;
	}

	@Override
	public int getCompletedCount(final BMECatImportCronJob cronJob)
	{
		return BMECatInfoStep.getMimeCount(cronJob);
	}

	@Override
	public int getTotalToComplete(final BMECatImportCronJob cronJob)
	{
		return cronJob.isCatalogInfoAvailableAsPrimitive() ? cronJob.getMimeCountAsPrimitive() : -1;
	}

	protected void countMimes(final BMECatImportCronJob cronJob, final Collection mimes)
	{
		if (cronJob.isCatalogInfoAvailableAsPrimitive())
		{
			BMECatInfoStep.addMimes(cronJob, mimes);
		}
	}

	@Override
	protected void importBMECatObject(final Catalog catalog, final AbstractValueObject object, final BMECatImportCronJob cronJob)
			throws ParseAbortException
	{
		if (object instanceof Article)
		{
			final Article article = (Article) object;
			final String articleId = article.getSupplierAID();
			final Collection medias = article.getMedias();
			if (medias != null && !medias.isEmpty())
			{
				countMimes(cronJob, medias);
				if (!skipArticle(cronJob, articleId))
				{
					final Product product = cronJob.getCatalogVersion().getProduct(articleId);
					if (product != null)
					{
						importProductMedias(cronJob, medias, product);
					}
					else
					{
						if (isWarnEnabled())
						{
							warn("Warning: Skipping media import, article '" + articleId + "' not found!");
						}
					}
				}
			}
		}
		else if (object instanceof CatalogStructure)
		{
			final CatalogStructure catalogStructure = (CatalogStructure) object;
			final String catId = catalogStructure.getID();
			final Collection medias = catalogStructure.getMedias();
			if (medias != null && !medias.isEmpty())
			{
				countMimes(cronJob, medias);
				if (!skipCategory(cronJob, catId))
				{
					final Category category = cronJob.getCatalogVersion().getCategory(catId);
					if (category != null)
					{
						importCategoryMedias(cronJob, medias, category);
					}
					else
					{
						if (isWarnEnabled())
						{
							warn("Warning: Skipping media import, category '" + catId + "' not found!");
						}
					}
				}
			}
		}
		else if (object instanceof ArticleToCatalogGroupMap)
		{
			throw new ParseFinishedException("MediaStep: done.");
		}
	}

	private boolean skipArticle(final CronJob cronJob, final String actualProductCode)
	{
		final Product product = (Product) cronJob.getTransientObject(LAST_PROCESSED_PRODUCT);
		if (product != null) //skip mode
		{
			if (actualProductCode.equals(product.getCode()))
			{
				//setting id to null, next line will be imported
				cronJob.setTransientObject(LAST_PROCESSED_PRODUCT, null);
				if (isInfoEnabled())
				{
					info("Last imported article found! Now importing new lines...");
				}
			}
			else
			{
				if (isDebugEnabled())
				{
					debug("Already imported article found! Skipping...");
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean skipCategory(final CronJob cronJob, final String actualCategoryCode)
	{
		final Category cat = (Category) cronJob.getTransientObject(LAST_PROCESSED_CATEGORY);
		if (cat != null) //skip mode
		{
			if (actualCategoryCode.equals(cat.getCode()))
			{
				//setting id to null, next line will be imported
				cronJob.setTransientObject(LAST_PROCESSED_CATEGORY, null);
				if (isInfoEnabled())
				{
					info("Last imported category found! Now importing new lines...");
				}
			}
			else
			{
				if (isDebugEnabled())
				{
					debug("Already imported article found! Skipping...");
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	//	protected String getLastProcessedArticleCode( BMECatImportCronJob cronJob, Catalog catalogValueObj )
	//	{
	//		if( !cronJob.isChangeRecordingEnabledAsPrimitive() )
	//		{
	//			return NONE;
	//		}
	//		
	//		String lastProcessedArticleCode = (String)cronJob.getTransientObject( LAST_PROCESSED_ARTICLE_KEY );
	//		if( lastProcessedArticleCode == null )
	//		{
	//			ChangeDescriptor lastOne = null;
	//			switch( catalogValueObj.getTransactionMode() )
	//			{
	//				case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
	//				{
	//					lastOne = getMostRecentChange( cronJob, BMECatConstants.ChangeTypes.CREATE_PRODUCT );
	//					break;
	//				}
	//				case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
	//				{
	//					// TODO: implement
	//					break;
	//				}
	//			}
	//			final Product p = lastOne != null ? (Product)lastOne.getChangedItem() : null;
	//			cronJob.setTransientObject( LAST_PROCESSED_ARTICLE_KEY, p != null ? lastProcessedArticleCode = p.getCode() : NONE );
	//		}
	//		return NONE.equals( lastProcessedArticleCode ) ? null : lastProcessedArticleCode;
	//	}

	//	protected void lastProcessedArticleFound( CronJob cronJob )
	//	{
	//		cronJob.setTransientObject( LAST_PROCESSED_ARTICLE_KEY, NONE );
	//	}

	protected ZipFile getMediasFile(final BMECatImportCronJob cronJob)
	{
		final Boolean marker = (Boolean) cronJob.getTransientObject(MEDIAS_ZIP_FILE_CHECKED);
		ZipFile zipFile = (ZipFile) cronJob.getTransientObject(MEDIAS_ZIP_FILE);

		if (marker == null)
		{
			//
			// CREATE TEMPORARY MEDIAS AND CATALOG FILES
			//
			final JobMedia jobMedia = cronJob.getJobMedia();
			final JobMedia mediasMedia = cronJob.getMediasMedia();
			try
			{
				if (mediasMedia != null)
				{
					zipFile = createMediaZip(mediasMedia.getMime(), cronJob, mediasMedia.getDataFromStream());
				}
				else if (jobMedia != null)
				{
					zipFile = createMediaZip(jobMedia.getMime(), cronJob, jobMedia.getDataFromStream());
				}

				cronJob.setTransientObject(MEDIAS_ZIP_FILE, zipFile);
				cronJob.setTransientObject(MEDIAS_ZIP_FILE_CHECKED, Boolean.TRUE);
			}
			catch (final JaloBusinessException e)
			{
				e.printStackTrace();
			}
		}
		return zipFile;
	}

	protected void importProductMedias(final BMECatImportCronJob cronJob, final Collection mediaValueObjects, final Product product)
	{
		final Map medias = createMedias(cronJob, mediaValueObjects);
		try
		{
			final Map attribs = product.getAllAttributes(getSession().getSessionContext(), new Item.AttributeFilter()
			{
				public boolean processAttribute(final AttributeDescriptor ad)
				{
					return getProductMapping().containsValue(ad); // extract mediacollections (map: purpose -> mediacollection)
				}
			});

			final EnumerationType eType = getSession().getEnumerationManager().getEnumerationType(
					BMECatConstants.TC.BMECATMIMEPURPOSEENUM);
			for (final Iterator it = medias.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry mapEntry = (Map.Entry) it.next();
				final String key = (String) mapEntry.getKey();
				final List mediaList = (List) mapEntry.getValue();

				String purpose = null;
				Type mimePurposeAttributeType = null;

				if (key != null)
				{
					try
					{
						final EnumerationValue enumerationValue = getSession().getEnumerationManager().getEnumerationValue(eType, key);
						purpose = getMimePurposeQualifier(enumerationValue, getProductMapping());
						mimePurposeAttributeType = getMimePurposeAttributeType(enumerationValue, getProductMapping());
					}
					catch (final JaloItemNotFoundException e)
					{
						// ok, key does not match our bmecat media types , just fallback to NORMAL
						if (isWarnEnabled())
						{
							warn("unknown bmecat mime purpose '" + key + "' - falling back to normal");
						}
					}
				}

				if (purpose == null)
				{
					final EnumerationValue normalEnum = getSession().getEnumerationManager().getEnumerationValue(eType,
							BMECatConstants.Enumerations.BMECatMimePurposeEnum.NORMAL);
					purpose = getMimePurposeQualifier(normalEnum, getProductMapping()); // get mapped attribute for 'normal'
					if (purpose == null) // if no attribute is mapped just use Product.normal
					{
						purpose = CatalogConstants.Attributes.Product.NORMAL;
						mimePurposeAttributeType = TypeManager.getInstance().getComposedType(Product.class)
								.getAttributeDescriptorIncludingPrivate(purpose).getAttributeType();
					}
					else
					{
						mimePurposeAttributeType = getMimePurposeAttributeType(normalEnum, getProductMapping());
					}
				}
				final ComposedType mediaType = getSession().getTypeManager().getComposedType(Media.class);
				/*
				 * a) single valued attribute
				 */
				if (mimePurposeAttributeType.isAssignableFrom(mediaType))
				{
					if (mediaList != null && !mediaList.isEmpty())
					{
						attribs.put(purpose, mediaList.get(0));
						if (isWarnEnabled() && mediaList.size() > 1)
						{
							warn("cannot assign all medias to product " + product + " since mapped attribute " + purpose
									+ " is single valued - " + "left " + mediaList.subList(1, mediaList.size()) + " unassigned");
						}
					}
				}
				/*
				 * b) collection of medias
				 */
				else if (mimePurposeAttributeType instanceof CollectionType
						&& ((CollectionType) mimePurposeAttributeType).getElementType().isAssignableFrom(mediaType))
				{
					final Collection mediaCollection = attribs.get(purpose) != null ? new ArrayList((Collection) attribs.get(purpose))
							: new ArrayList();
					mediaCollection.addAll(mediaList);
					attribs.put(purpose, mediaCollection);
				}
				/*
				 * c) unknown: error
				 */
				else if (isErrorEnabled())
				{
					error("illegal mime attribute " + purpose + " mapped to bmecat mime type " + key
							+ " due to incompatible attribute type " + mimePurposeAttributeType
							+ " - must be media or media collection assignable");
				}
			}
			product.setAllAttributes(attribs);
			addChange(cronJob, BMECatConstants.ChangeTypes.ADD_PRODUCT_MEDIA, product, "last processed article in mediastep was: "
					+ product.getCode());

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

	protected void importSupplierMedias(final BMECatImportCronJob cronJob, final Collection mediaValueObjects,
			final Company supplier)
	{
		if (supplier != null)
		{
			final Collection medias = new HashSet();
			for (final Iterator it = createMedias(cronJob, mediaValueObjects).values().iterator(); it.hasNext();)
			{
				medias.addAll((Collection) it.next());
			}
			supplier.setMedias(medias);
			addChange(cronJob, BMECatConstants.ChangeTypes.ADD_SUPLLIER_MEDIA, supplier, null);
		}
		else
		{
			throw new RuntimeException("Supplier can not be null! Aborting import!");
		}
	}

	protected void importCategoryMedias(final BMECatImportCronJob cronJob, final Collection mediaValueObjects,
			final Category category)
	{
		final Map medias = createMedias(cronJob, mediaValueObjects);
		try
		{
			final Map attribs = category.getAllAttributes(getSession().getSessionContext(), new Item.AttributeFilter()
			{
				public boolean processAttribute(final AttributeDescriptor ad)
				{
					return getCategoryMapping().containsValue(ad); // extract mediacollections (map: purpose -> mediacollection)
				}
			});

			final EnumerationType eType = getSession().getEnumerationManager().getEnumerationType(
					BMECatConstants.TC.BMECATMIMEPURPOSEENUM);
			for (final Iterator it = medias.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry mapEntry = (Map.Entry) it.next();
				final String key = (String) mapEntry.getKey();
				final List mediaList = (List) mapEntry.getValue();

				String purpose = null;

				if (key != null)
				{
					purpose = getMimePurposeQualifier(getSession().getEnumerationManager().getEnumerationValue(eType, key),
							getCategoryMapping());
				}

				if (purpose == null)
				{
					purpose = CatalogConstants.Attributes.Category.NORMAL;
				}

				final Type mimePurposeAttributeType = getMimePurposeAttributeType(getSession().getEnumerationManager()
						.getEnumerationValue(eType, key), getCategoryMapping());

				if (mimePurposeAttributeType.isAssignableFrom(getSession().getTypeManager().getComposedType(Media.class)))
				{
					if (mediaList != null && !mediaList.isEmpty())
					{
						attribs.put(purpose, mediaList.iterator().next());
					}
				}
				else
				{
					final Collection mediaCollection = attribs.get(purpose) != null ? (Collection) attribs.get(purpose)
							: new ArrayList();
					mediaCollection.addAll(mediaList);
					attribs.put(purpose, mediaCollection);
				}
			}
			category.setAllAttributes(attribs);
			addChange(cronJob, BMECatConstants.ChangeTypes.ADD_CATEGORY_MEDIA, category,
					"last processed category in mediastep was: " + category.getCode());
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

	protected ItemAttributeMap getValues(final BMECatImportCronJob cronJob, final Mime mimeValueObject)
	{
		final ItemAttributeMap values = new ItemAttributeMap();
		values.put(CatalogConstants.Attributes.Media.CATALOGVERSION, cronJob.getCatalogVersion());
		//		values.put( CatalogConstants.Attributes.Media.CATALOG, cronJob.getCatalogVersion().getCatalog() );
		values.put(Media.CODE, mimeValueObject.getSource());
		values.put(Media.REALFILENAME, mimeValueObject.getSource());
		values.put(Media.MIME, mimeValueObject.getType());
		return values;
	}

	private Map createMedias(final BMECatImportCronJob cronJob, final Collection medias)
	{
		final ZipFile mimeZipFile = getMediasFile(cronJob);
		final Map newMedias = new HashMap();
		final List mimesSorted = new ArrayList(medias);

		Collections.sort(mimesSorted, new Comparator()
		{
			public int compare(final Object object1, final Object object2)
			{
				final int m1Order = ((Mime) object1).getOrder() != null ? ((Mime) object1).getOrder().intValue() : 0;
				final int m2Order = ((Mime) object2).getOrder() != null ? ((Mime) object2).getOrder().intValue() : 0;
				return m1Order - m2Order;
			}
		});

		for (final Iterator it = mimesSorted.iterator(); it.hasNext();)
		{
			final Mime mimeValueObject = (Mime) it.next();
			final Media media = getOrCreateMedia(cronJob, mimeValueObject, mimeZipFile);
			if (media != null)
			{
				//	build up map (purpose -> List[media])
				final Object value = newMedias.get(mimeValueObject.getPurpose());
				if (value == null)
				{
					newMedias.put(mimeValueObject.getPurpose(), new ArrayList(Collections.singletonList(media)));
				}
				else
				{
					((List) value).add(media);
				}
			}
		}
		return newMedias;
	}

	protected Media getOrCreateMedia(final BMECatImportCronJob cronJob, final Mime mimeValueObject, final ZipFile mimeZipFile)
	{
		Media media = cronJob.getCatalogVersion().getMedia(mimeValueObject.getSource());
		if (media == null)
		{
			media = createMedia(cronJob, mimeValueObject, mimeZipFile);
		}
		return media;
	}

	protected Media createMedia(final BMECatImportCronJob cronJob, final ItemAttributeMap values)
	{
		try
		{
			final Media media = (Media) getMediaType().newInstance(values);
			final String mediaCode = media.getCode();
			addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_MEDIA, media, "created new media " + mediaCode);
			if (isInfoEnabled())
			{
				info(" - created media " + mediaCode);
			}
			return media;
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Could not create media (values=" + values + ") due to " + e.getLocalizedMessage() + " : "
						+ Utilities.getStackTraceAsString(e));
			}
		}
		return null;
	}

	protected Media createMedia(final BMECatImportCronJob cronJob, final Mime mimeValueObject, final ZipFile mimeZipFile)
	{
		final Media media = createMedia(cronJob, getValues(cronJob, mimeValueObject));
		if (media != null)
		{
			final String url = getMediaRoot(cronJob) + mimeValueObject.getSource();
			if (mimeZipFile != null)
			{
				final ZipEntry zipEntry = mimeZipFile.getEntry(url);
				if (zipEntry != null) //get data from zip file
				{
					DataInputStream dis = null;
					try
					{
						media.setData(dis = new DataInputStream(mimeZipFile.getInputStream(zipEntry)), mimeValueObject.getSource(),
								mimeValueObject.getType());
					}
					catch (final Exception exp)
					{
						// DOCTODO Document reason, why this block is empty
					}
					finally
					{
						if (dis != null)
						{
							try
							{
								dis.close();
							}
							catch (final IOException exp)
							{
								if (LOG.isDebugEnabled())
								{
									LOG.debug(exp.getMessage());
								}
							}
						}
					}
				}
				else
				{
					if (isWarnEnabled())
					{
						warn("Binary data for media '" + url + "' not found in " + mimeZipFile.getName());
					}
				}
			}
			else
			//no mimeZipFile, trying to get binary data from url
			{
				try
				{
					media.setURL(url);
					//					System.out.println( "Reference Media '" + media.getCode() + "'? " + cronJob.isReferenceMediasAsPrimitive() );
					if (!cronJob.isReferenceMediasAsPrimitive())
					{
						media.setDataByURL();
					}
				}
				catch (final Exception e)
				{
					if (isWarnEnabled())
					{
						warn("Could not get binary data from url '" + url + "' due to " + e.getLocalizedMessage());
					}
				}
			}
		}
		return media;
	}

	private String getMimePurposeQualifier(final EnumerationValue eValue, final Map mapping)
	{
		final AttributeDescriptor attributeDescriptor = (AttributeDescriptor) mapping.get(eValue);
		if (attributeDescriptor != null)
		{
			return attributeDescriptor.getQualifier();
		}
		else
		{
			return null;
		}
	}

	private Type getMimePurposeAttributeType(final EnumerationValue eValue, final Map mapping)
	{
		final AttributeDescriptor attributeDescriptor = (AttributeDescriptor) mapping.get(eValue);
		if (attributeDescriptor != null)
		{
			return attributeDescriptor.getAttributeType();
		}
		else
		{
			return null;
		}

	}

	private Map getProductMapping()
	{
		if (productmapping == null)
		{
			productmapping = getAllProductMimePurposeMapping();
		}
		return productmapping;
	}

	private Map getCategoryMapping()
	{
		if (categorymapping == null)
		{
			categorymapping = getAllCategoryMimePurposeMapping();
		}
		return categorymapping;
	}

	/**
	 * Tells wether this step is able to perform for the given cronJob or not.
	 * <p>
	 * As default this method returns <code>true</code>.
	 * 
	 * @param cronJob
	 * @return true if this step is able to perform for the given cronJob
	 */
	@Override
	public boolean canPerform(final CronJob cronJob)
	{
		boolean canPerform = true;
		if (!(cronJob instanceof BMECatImportCronJob))
		{
			cronJob.addLog("Can't perform MediaStep! (used cronJob isn't assignable from 'BMECatImportjobDetail')", this);
			canPerform = false;
		}
		if (((BMECatImportCronJob) cronJob).getCatalogVersion() == null)
		{
			cronJob.addLog("Can't perform MediaStep! (missing 'CatalogVersion')", this);
			canPerform = false;
		}
		else if (((BMECatImportCronJob) cronJob).getCatalogVersion().getCatalog() == null)
		{
			cronJob.addLog("Can't perform MediaStep! (missing 'Catalog')", this);
			canPerform = false;
		}
		if (getMediaType() == null)
		{
			cronJob.addLog("Can't perform MediaStep! (missing 'MediaType' definition)", this);
			canPerform = false;
		}
		else if (!(Media.class.isAssignableFrom(getMediaType().getJaloClass())))
		{
			cronJob.addLog("Can't perform MediaStep! ( media type '" + getMediaType() + "' is no subtype of Media )", this);
			canPerform = false;
		}
		return canPerform;
	}
}

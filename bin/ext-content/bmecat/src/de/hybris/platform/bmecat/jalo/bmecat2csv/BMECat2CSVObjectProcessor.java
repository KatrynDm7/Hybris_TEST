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
package de.hybris.platform.bmecat.jalo.bmecat2csv;

import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.Abort;
import de.hybris.platform.bmecat.parser.Address;
import de.hybris.platform.bmecat.parser.Agreement;
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.ArticleFeatures;
import de.hybris.platform.bmecat.parser.ArticlePrice;
import de.hybris.platform.bmecat.parser.ArticlePriceDetails;
import de.hybris.platform.bmecat.parser.ArticleReference;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.bmecat.parser.CatalogGroupSystem;
import de.hybris.platform.bmecat.parser.CatalogStructure;
import de.hybris.platform.bmecat.parser.ClassificationGroup;
import de.hybris.platform.bmecat.parser.ClassificationSystem;
import de.hybris.platform.bmecat.parser.Company;
import de.hybris.platform.bmecat.parser.Feature;
import de.hybris.platform.bmecat.parser.FeatureSystem;
import de.hybris.platform.bmecat.parser.Mime;
import de.hybris.platform.bmecat.parser.Variant;
import de.hybris.platform.bmecat.parser.Variants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants.Enumerations.IDType;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.MediaUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;


public class BMECat2CSVObjectProcessor implements BMECatObjectProcessor
{
	private static final Logger LOG = Logger.getLogger(BMECat2CSVObjectProcessor.class);

	// stores all CSVWriter, output map
	private final Map<String, MyCSVWriter> outputWriters = new HashMap<String, MyCSVWriter>();

	// the return object, a zip file which contains all creates csv files
	private Media resultZipMedia = null;

	// the bmecat CATALOG.MIME_ROOT, will be concat with each MIME_INFO.MIME.MIME_SOURCE
	private String mimeRoot = null;

	// cache for avoiding double keywords
	private final Set<String> keywordcache = new HashSet<String>();

	// cache for avoiding double media references
	private final Set<String> mediacache = new HashSet<String>();

	// for DATETIME formatting
	private final SimpleDateFormat sdf = new SimpleDateFormat(BMECatConstants.BMECat2CSV.DATETIMETIMEZONE_FORMAT_PATTERN,
			Locale.GERMANY);

	// for formating double values
	private DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);

	// encoding constant
	private String encoding = CSVConstants.DEFAULT_ENCODING;

	// number of transformed items of bmecat.xml
	private final Long ZERO_LONG = Long.valueOf(0);
	private Long catalogSize = ZERO_LONG;
	private Long catalogVersionSize = ZERO_LONG;
	private Long classificationClassSize = ZERO_LONG;
	private Long classificationSystemSize = ZERO_LONG;
	private Long classificationSystemVersionSize = ZERO_LONG;
	private Long classificationAttributeSize = ZERO_LONG;
	private Long classificationAttributeAssignmentSize = ZERO_LONG;
	private Long articleSize = ZERO_LONG;
	private Long updateArticleSize = ZERO_LONG;
	private Long deleteArticleSize = ZERO_LONG;
	private Long articleReferenceSize = ZERO_LONG;
	private Long keywordSize = ZERO_LONG;
	private Long mimeSize = ZERO_LONG;
	private Long catalogStructureSize = ZERO_LONG;
	private Long catalogGroupNewRelationSize = ZERO_LONG;
	private Long catalogGroupDeleteRelationSize = ZERO_LONG;
	private Long companySize = ZERO_LONG;
	private Long addressSize = ZERO_LONG;
	private Long countrySize = ZERO_LONG;
	private Long regionSize = ZERO_LONG;
	private Long agreementSize = ZERO_LONG;
	private Long articlePriceSize = ZERO_LONG;
	private Long articleUpdatePriceSize = ZERO_LONG;
	private Long articleFeatureSize = ZERO_LONG;
	private Long articleFeatureDetailsSize = ZERO_LONG;
	private Long articleFeatureValueMapplingsSize = ZERO_LONG;
	private Long articleFeatureVariantsSize = ZERO_LONG;
	private Long catalogGroupSystemSize = ZERO_LONG;

	private int filesNumber = 0;
	private final StringBuilder buf = new StringBuilder();
	private String generalCatalogInfo = "";
	private final Map<String, String> bmecatHeader = new HashMap<String, String>();
	private int bmecatType = 0;
	private String previousVersion = "";

	private Date bmecatCatalogDate = null;

	private String catalogCurrency = null;

	// CSVWriter settings
	private static char commentchar = CSVConstants.DEFAULT_COMMENT_CHAR;
	private static char fieldseperator = CSVConstants.DEFAULT_FIELD_SEPARATOR;
	private static char textseperator = CSVConstants.DEFAULT_QUOTE_CHARACTER;

	private char collectionseparator = ImpExConstants.Syntax.DEFAULT_COLLECTION_VALUE_DELIMITER;

	//information for bmecat import wizard mapping
	private final Set<String> currencies = new HashSet<String>();
	private final Set<String> priceTypes = new HashSet<String>();
	private final Set<String> units = new HashSet<String>();
	private final Set<String> taxes = new HashSet<String>();
	private final Set<String> classificationSystems = new HashSet<String>();

	// record the created csv files when transforming the bmecat xml
	private final List<String> csvFileNames = new ArrayList<String>();

	/**
	 * Extended CSVWriter which keeps the given file in a link.
	 */
	protected static class MyCSVWriter extends CSVWriter
	{
		private final File file;

		MyCSVWriter(final File file, final String encoding) throws UnsupportedEncodingException, FileNotFoundException
		{
			super(file, encoding);
			super.setCommentchar(commentchar);
			super.setFieldseparator(fieldseperator);
			super.setTextseparator(textseperator);
			this.file = file;
		}

		/**
		 * @return the file
		 */
		public File getFile()
		{
			return file;
		}

		public boolean deleteFile()
		{
			return this.file.delete();
		}

	}

	public BMECat2CSVObjectProcessor(final String encoding, final String datetimetimezone, final Locale loc,
			final String numberformat, final char commentchar, final char fieldseparatorchar, final char textseparatorchar,
			final char collectionseparator)
	{
		if (encoding != null && encoding.length() > 0)
		{
			this.encoding = encoding;
		}
		if (datetimetimezone != null && datetimetimezone.length() > 0)
		{
			sdf.applyPattern(datetimetimezone);
		}
		if (loc != null)
		{
			decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(loc);
		}
		if (numberformat != null && numberformat.length() > 0)
		{
			decimalFormat.applyPattern(numberformat);
		}
		else
		{
			decimalFormat.applyPattern(BMECatConstants.BMECat2CSV.NUMBERFORMAT); // the default pattern
		}
		BMECat2CSVObjectProcessor.commentchar = commentchar;
		BMECat2CSVObjectProcessor.fieldseperator = fieldseparatorchar;
		BMECat2CSVObjectProcessor.textseperator = textseparatorchar;
		this.collectionseparator = collectionseparator;
	}

	/**
	 * The default settings for this constructor are:<br>
	 * <ul>
	 * <li>encoding: "UTF-8"</li>
	 * <li>Date, Time and Timezone format: "yyyy-MM-dd hh:mm:ss z"</li>
	 * <li>Locale: system default locale</li>
	 * <li>Number format pattern: "#,##0.000"</li>
	 * <li>CSV comment char: '#'</li>
	 * <li>CSV field separator char: ';'</li>
	 * <li>CSV text separator char: '"'</li>
	 * <li>CSV collection separator: ','</li>
	 * </ul>
	 * 
	 */
	public BMECat2CSVObjectProcessor()
	{
		decimalFormat.applyPattern(BMECatConstants.BMECat2CSV.NUMBERFORMAT);
	}


	/**
	 * This method has to be called after {@link #process(TagListener, AbstractValueObject)}! It creates a zip file of
	 * the generated csv temp files and delete all temp files after that.
	 * 
	 * @throws IOException
	 * @throws JaloBusinessException
	 */
	public void finish() throws IOException, JaloBusinessException
	{
		collectMappingInfo();
		collectStatisticsInfo();
		editStatisticsFile();
		finish_process();
		deleteTempFiles();
	}

	//collect the information of mapping for import wizard
	private void collectMappingInfo()
	{
		if (this.currencies.size() > 0)
		{
			buf.append("bmecat.currencies=" + getSetContent(this.currencies) + "\r\n");
		}
		if (this.priceTypes.size() > 0)
		{
			buf.append("bmecat.pricetypes=" + getSetContent(this.priceTypes) + "\r\n");
		}
		if (this.units.size() > 0)
		{
			buf.append("bmecat.units=" + getSetContent(this.units) + "\r\n");
		}
		if (this.taxes.size() > 0)
		{
			buf.append("bmecat.taxes=" + getSetContent(this.taxes) + "\r\n");
		}
		if (this.classificationSystems.size() > 0)
		{
			buf.append("bmecat.classificationsystems=" + getSetContent(this.classificationSystems) + "\r\n");
		}
		buf.append("\r\n");
	}

	private StringBuilder getSetContent(final Set<String> set)
	{
		final StringBuilder stringBuilder = new StringBuilder(set.toString());
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.deleteCharAt(0);
		return stringBuilder;
	}

	//collect the information of statistics of bmecat.xml
	private void collectStatisticsInfo()
	{
		if (catalogSize.longValue() > 0)
		{
			addFileEntry("catalog", catalogSize, BMECatConstants.BMECat2CSV.CATALOG_OBJECT_FILENAME);
		}
		if (classificationAttributeSize.longValue() > 0)
		{
			addFileEntry("classificationattribute", classificationAttributeSize,
					BMECatConstants.BMECat2CSV.CLASSIFICATION_ATTRIBUTE_FILENAME);
		}
		if (classificationAttributeAssignmentSize.longValue() > 0)
		{
			addFileEntry("classificationattributeassignment", classificationAttributeAssignmentSize,
					BMECatConstants.BMECat2CSV.CLASSIFICATION_ATTRIBUTE_ASSIGNMENT_FILENAME);
		}
		if (classificationSystemSize.longValue() > 0)
		{
			addFileEntry("classificationsystem", classificationSystemSize, BMECatConstants.BMECat2CSV.CLASSIFICATION_SYSTEM_FILENAME);
		}
		if (classificationSystemVersionSize.longValue() > 0)
		{
			addFileEntry("classificationsystemversion", classificationSystemVersionSize,
					BMECatConstants.BMECat2CSV.CLASSIFICATION_SYSTEM_VERSION_FILENAME);
		}
		if (classificationClassSize.longValue() > 0)
		{
			addFileEntry("classificationclass", classificationClassSize, BMECatConstants.BMECat2CSV.CLASSIFICATION_CLASS_FILENAME);
		}
		if (catalogVersionSize.longValue() > 0)
		{
			addFileEntry("catalogversion", catalogVersionSize, BMECatConstants.BMECat2CSV.CATALOG_VERSION_FILENAME);
		}
		if (articleSize.longValue() > 0)
		{
			addFileEntry("products", articleSize, BMECatConstants.BMECat2CSV.ARTICLE_OBJECT_FILENAME);
		}
		if (updateArticleSize.longValue() > 0)
		{
			addFileEntry("productsupdate", updateArticleSize, BMECatConstants.BMECat2CSV.ARTICLE_UPDATE_FILENAME);
		}
		if (deleteArticleSize.longValue() > 0)
		{
			addFileEntry("productsdelete", deleteArticleSize, BMECatConstants.BMECat2CSV.ARTICLE_DELETE_MODE);
		}
		if (articleReferenceSize.longValue() > 0)
		{
			addFileEntry("productreferences", articleReferenceSize, BMECatConstants.BMECat2CSV.ARTICLEREFERENCES_FILENAME);
		}
		if (keywordSize.longValue() > 0)
		{
			addFileEntry("keywords", keywordSize, BMECatConstants.BMECat2CSV.KEYWORD_OBJECT_FILENAME);
		}
		if (mimeSize.longValue() > 0)
		{
			addFileEntry("medias", mimeSize, BMECatConstants.BMECat2CSV.MIME_OBJECT_FILENAME);
		}
		if (catalogStructureSize.longValue() > 0)
		{
			addFileEntry("categories", catalogStructureSize, BMECatConstants.BMECat2CSV.CATALOGSTRUCTUE_OBJECT_FILENAME);
		}
		if (catalogGroupNewRelationSize.longValue() > 0)
		{
			addFileEntry("productnew2category", catalogGroupNewRelationSize,
					BMECatConstants.BMECat2CSV.ARTICLE2CATALOGGROUP_NEW_RELATION_FILENAME);
		}
		if (catalogGroupDeleteRelationSize.longValue() > 0)
		{
			addFileEntry("productdelete2category", catalogGroupDeleteRelationSize,
					BMECatConstants.BMECat2CSV.ARTICLE2CATALOGGROUP_DELETE_RELATION_FILENAME);
		}
		if (companySize.longValue() > 0)
		{
			addFileEntry("customers", companySize, BMECatConstants.BMECat2CSV.COMPANY_FILENAME);
		}
		if (addressSize.longValue() > 0)
		{
			addFileEntry("addresses", addressSize, BMECatConstants.BMECat2CSV.ADDRESS_FILENAME);
		}
		if (countrySize.longValue() > 0)
		{
			addFileEntry("territories", countrySize, BMECatConstants.BMECat2CSV.COUNTRY_FILENAME);
		}
		if (regionSize.longValue() > 0)
		{
			addFileEntry("territories", regionSize, BMECatConstants.BMECat2CSV.REGION_FILENAME);
		}
		if (agreementSize.longValue() > 0)
		{
			addFileEntry("agreement", agreementSize, BMECatConstants.BMECat2CSV.AGREEMENT_FILENAME);
		}
		if (articlePriceSize.longValue() > 0)
		{
			addFileEntry("product2productprices", articlePriceSize, BMECatConstants.BMECat2CSV.ARTICLE2ARTICLEPRICE);
		}
		if (articleUpdatePriceSize.longValue() > 0)
		{
			addFileEntry("productupdateprices", articleUpdatePriceSize, BMECatConstants.BMECat2CSV.ARTICLE_UPDATE_PRICES);
		}
		if (articleFeatureSize.longValue() > 0)
		{
			addFileEntry("articlefeatures", articleFeatureSize, BMECatConstants.BMECat2CSV.ARTICLEFEATURE_FILENAME);
		}
		if (articleFeatureDetailsSize.longValue() > 0)
		{
			addFileEntry("articlefeaturedetails", articleFeatureDetailsSize, BMECatConstants.BMECat2CSV.ARTICLEFEATURE_DETAILS);
		}
		if (articleFeatureValueMapplingsSize.longValue() > 0)
		{
			addFileEntry("articlefeaturevaluemappings", articleFeatureValueMapplingsSize,
					BMECatConstants.BMECat2CSV.ARTICLEFEATURE_VALUE_MAPPINGS);
		}
		if (articleFeatureVariantsSize.longValue() > 0)
		{
			addFileEntry("articlefeaturevariants", articleFeatureVariantsSize, BMECatConstants.BMECat2CSV.ARTICLEFEATURE_VARIANTS);
		}
		if (catalogGroupSystemSize.longValue() > 0)
		{
			addFileEntry("groupsystem", catalogGroupSystemSize, BMECatConstants.BMECat2CSV.GROUPSYSTEM_FILENAME);
		}
	}

	private void addFileEntry(final String itemPrefix, final Long itemSize, final String fileName)
	{
		buf.append("file." + itemPrefix + ".name=\"" + fileName + "\"\r\n");
		buf.append("file." + itemPrefix + ".size=" + itemSize + "\r\n");
		this.filesNumber++;
	}

	private void editStatisticsFile()
	{
		buf.append("\r\nstatistics.file.size=" + this.filesNumber + "\r\n");
		try
		{
			getOut("statistics.properties").writeSrcLine(buf.toString());
		}
		catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}

	}

	/**
	 * Deletes all temp files which are generated during {@link #process(TagListener, AbstractValueObject)}
	 * 
	 * @throws IOException
	 */
	public void deleteTempFiles() throws IOException
	{
		for (final MyCSVWriter wr : outputWriters.values())
		{
			wr.close();
			wr.deleteFile();
		}
	}

	/**
	 * Creates the zip file but do not delete the generated temp files.
	 * 
	 * @throws IOException
	 * @throws JaloBusinessException
	 */
	protected void finish_process() throws IOException, JaloBusinessException //NOPMD
	{
		if (resultZipMedia == null)
		{
			final File ziptempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), "bme2csv.zip");
			ZipOutputStream zos = null;
			try
			{
				zos = new ZipOutputStream(new FileOutputStream(ziptempFile));
				for (final Map.Entry<String, MyCSVWriter> entry : outputWriters.entrySet())
				{
					final MyCSVWriter csvWriter = entry.getValue();
					csvWriter.close();
					final File csvTmp = csvWriter.getFile();

					final ZipEntry zipEntry = new ZipEntry(entry.getKey());
					zos.putNextEntry(zipEntry);

					final FileInputStream fis = new FileInputStream(csvTmp);
					MediaUtil.copy(fis, zos, false);
					fis.close();
					zos.flush();
					zos.closeEntry();
				}
				zos.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
			resultZipMedia = MediaManager.getInstance().createMedia("bme2csv_" + System.currentTimeMillis() + ".zip");
			resultZipMedia.setFile(ziptempFile);
			if (resultZipMedia.getMime() == null)
			{
				resultZipMedia.setMime("application/zip");
				//ziptempFile.delete();
			}
		}
		else
		{
			throw new IllegalStateException();
		}
	}

	/**
	 * 
	 * @return a media which hold a zip file which contains all generated csv files.
	 */
	public Media getResultZipMedia()
	{
		if (resultZipMedia == null)
		{
			throw new IllegalStateException();
		}
		else
		{
			return resultZipMedia;
		}
	}

	/**
	 * 
	 * @return a list of the file names of all created csv files
	 */
	public List<String> getCsvFileNames()
	{
		for (final Map.Entry<String, MyCSVWriter> entry : outputWriters.entrySet())
		{
			csvFileNames.add(entry.getKey());
		}
		return this.csvFileNames;
	}

	/**
	 * 
	 * @param fileName
	 *           the used filename
	 * @return a CSVWriter (here MyCSVWriter) for the given filename. The Encoding is set to UTF-8 and the file is bound
	 *         to the CSVWriter.
	 */
	protected MyCSVWriter getOut(final String fileName)
	{
		MyCSVWriter csvWriter = outputWriters.get(fileName);
		if (csvWriter == null)
		{
			try
			{
				csvWriter = new MyCSVWriter(File.createTempFile(fileName, ".tmp"), encoding);
				outputWriters.put(fileName, csvWriter);
			}
			catch (final UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			catch (final FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
		return csvWriter;
	}


	public void process(final TagListener listener, final AbstractValueObject obj) throws ParseAbortException
	{
		try
		{
			if (obj instanceof Catalog)
			{
				final Catalog catalog = (Catalog) obj;
				//discussed with A.T, this BMECatProcessor should deal with all catalog types, 
				//namely T_NEW_CATALOG, T_UPDATE_PRODUCTS, and T_UPDATE_PRICES
				this.bmecatType = catalog.getTransactionMode();
				this.previousVersion = catalog.getPreviousVersion() == null ? "" : catalog.getPreviousVersion().toString();
				exportCatalogObject(catalog);
			}
			else if (obj instanceof ClassificationSystem)
			{
				exportClassificationSystem((ClassificationSystem) obj);
			}
			else if (obj instanceof Article)
			{
				exportArticleObject((Article) obj);
			}
			else if (obj instanceof CatalogStructure)
			{
				exportCatalogStructureObject((CatalogStructure) obj);
			}
			else if (obj instanceof ArticleToCatalogGroupMap)
			{
				if (this.bmecatType == BMECatConstants.TRANSACTION.T_NEW_CATALOG)
				{
					exportArticleNewToCatalogGroupMapObject((ArticleToCatalogGroupMap) obj);
				}
				else if (this.bmecatType == BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS)
				{
					exportArticleToCatalogGroupMapObject((ArticleToCatalogGroupMap) obj);
				}
			}
			else if (obj instanceof Abort)
			{
				//XXX debug info below, besides: what is Abort?
				final Abort abort = (Abort) obj;
				LOG.info("Abort with type [" + abort.getType() + "] in bmecat.xml was ignored.");
			}
			else if (obj instanceof FeatureSystem)
			{
				//XXX debug info below, feature system is ignored now, 
				//because such systems should have been defined in the hybris system(classification system)  
				//but how to deal with the user defined feature?
				final FeatureSystem featureSystem = (FeatureSystem) obj;
				LOG.info("FeatureSystem [" + featureSystem.getName() + "] in bmecat.xml was ignored.");
			}
			else if (obj instanceof CatalogGroupSystem)
			{
				exportCatalogGroupSystemObject((CatalogGroupSystem) obj);
			}
			else
			{
				//display the object name to see what kind of element should be parsed
				LOG.info("unknown: " + System.currentTimeMillis());
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	private void writeCsvLine(final String fileName, final Map<Integer, String> csvLine)
	{
		try
		{
			getOut(fileName).write(csvLine);
		}
		catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	private void addCatalogComment()
	{
		final Map<Integer, String> catalogline = new HashMap<Integer, String>();
		catalogline.put(Integer.valueOf(0), "##Catalog ID");
		catalogline.put(Integer.valueOf(1), "Name");
		catalogline.put(Integer.valueOf(2), "Buyer ID");
		catalogline.put(Integer.valueOf(3), "Supplier ID");
		writeCsvLine(BMECatConstants.BMECat2CSV.CATALOG_OBJECT_FILENAME, catalogline);
	}

	private void addClassificationSystemComment()
	{
		final Map<Integer, String> catalogline = new HashMap<Integer, String>();
		catalogline.put(Integer.valueOf(0), "##Catalog ID");
		catalogline.put(Integer.valueOf(1), "Name");
		writeCsvLine(BMECatConstants.BMECat2CSV.CLASSIFICATION_SYSTEM_FILENAME, catalogline);
	}

	private void addCatalogVersionComment()
	{
		final Map<Integer, String> catalogVersionLine = new HashMap<Integer, String>();
		catalogVersionLine.put(Integer.valueOf(0), "##Catalog ID");
		catalogVersionLine.put(Integer.valueOf(1), "Version");
		catalogVersionLine.put(Integer.valueOf(2), "Language");
		catalogVersionLine.put(Integer.valueOf(3), "Incl assurance");
		catalogVersionLine.put(Integer.valueOf(4), "Incl duty");
		catalogVersionLine.put(Integer.valueOf(5), "Incl freight");
		catalogVersionLine.put(Integer.valueOf(6), "Incl packing");
		catalogVersionLine.put(Integer.valueOf(7), "Default currency");
		catalogVersionLine.put(Integer.valueOf(8), "Generation date");
		writeCsvLine(BMECatConstants.BMECat2CSV.CATALOG_VERSION_FILENAME, catalogVersionLine);
	}

	private void addClassificationSystemVersionComment()
	{
		final Map<Integer, String> catalogVersionLine = new HashMap<Integer, String>();
		catalogVersionLine.put(Integer.valueOf(0), "##Catalog ID");
		catalogVersionLine.put(Integer.valueOf(1), "Version");
		catalogVersionLine.put(Integer.valueOf(2), "Language");
		writeCsvLine(BMECatConstants.BMECat2CSV.CLASSIFICATION_SYSTEM_VERSION_FILENAME, catalogVersionLine);
	}

	private void addCountryComment()
	{
		final Map<Integer, String> territoryVersionLine = new HashMap<Integer, String>();
		territoryVersionLine.put(Integer.valueOf(0), "##Country");
		writeCsvLine(BMECatConstants.BMECat2CSV.COUNTRY_FILENAME, territoryVersionLine);
	}

	private void addRegionComment()
	{
		final Map<Integer, String> territoryVersionLine = new HashMap<Integer, String>();
		territoryVersionLine.put(Integer.valueOf(0), "##Country");
		territoryVersionLine.put(Integer.valueOf(1), "Region");
		writeCsvLine(BMECatConstants.BMECat2CSV.REGION_FILENAME, territoryVersionLine);
	}

	/**
	 * Export a BMECat Catalog object into:<br>
	 * <ul>
	 * <li>- Catalog</li>
	 * </ul>
	 * 
	 * @param catalog
	 * @throws IOException
	 */
	private void exportCatalogObject(final Catalog catalog) throws IOException
	{
		if (this.catalogSize.longValue() == 0)
		{
			addCatalogComment();
			addCatalogVersionComment();
		}

		final Map<Integer, String> catalogline = new HashMap<Integer, String>();
		final Map<Integer, String> catalogVersionLine = new HashMap<Integer, String>();

		//mimeRoot is defined in the Catalog element
		mimeRoot = catalog.getMimeRootDirectory() == null ? "" : catalog.getMimeRootDirectory();

		//catalog
		catalogline.put(Integer.valueOf(0), catalog.getID());
		catalogline.put(Integer.valueOf(1), catalog.getName());

		//catalogVersion
		catalogVersionLine.put(Integer.valueOf(0), catalog.getID());
		catalogVersionLine.put(Integer.valueOf(1), catalog.getVersion());
		catalogVersionLine.put(Integer.valueOf(2), "");//catalog.getLanguage()
		catalogVersionLine.put(Integer.valueOf(3), catalog.getInclAssurance() == null ? Boolean.FALSE.toString() : catalog
				.getInclAssurance().toString());
		catalogVersionLine.put(Integer.valueOf(4), catalog.getInclDuty() == null ? Boolean.FALSE.toString() : catalog.getInclDuty()
				.toString());
		catalogVersionLine.put(Integer.valueOf(5), catalog.getInclFreight() == null ? Boolean.FALSE.toString() : catalog
				.getInclFreight().toString());
		catalogVersionLine.put(Integer.valueOf(6), catalog.getInclPacking() == null ? Boolean.FALSE.toString() : catalog
				.getInclPacking().toString());
		catalogVersionLine.put(Integer.valueOf(7), catalog.getDefaultCurrency());
		//format the Date to DateFormat, Locale is set to GERMAN
		if (catalog.getGenerationDate() != null)
		{
			catalogVersionLine.put(
					Integer.valueOf(8),
					DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN).format(
							catalog.getGenerationDate()));
		}
		else
		{
			catalogVersionLine.put(Integer.valueOf(8), "");
		}

		//HEADER.CATALOG.TERRITORY
		final Collection<String> territories = catalog.getTerritories();
		if (!territories.isEmpty())
		{
			addCountryComment();
			final Map<Integer, String> countryLine = new HashMap<Integer, String>();
			final Map<Integer, String> regionLine = new HashMap<Integer, String>();
			for (final Object o : territories)
			{
				boolean countryWithRegion = false;
				String country = "";
				String region = "";
				final String string = o.toString();
				//country has exactly the first 2 letters of territory
				country = string.substring(0, 2);
				countryLine.put(Integer.valueOf(0), country);
				getOut(BMECatConstants.BMECat2CSV.COUNTRY_FILENAME).write(countryLine);
				countrySize = Long.valueOf(countrySize.longValue() + 1);
				if (string.length() > 3)
				{
					if (!countryWithRegion)
					{
						addRegionComment();
						countryWithRegion = true;
					}
					region = string.substring(3);
					regionLine.put(Integer.valueOf(0), country);
					regionLine.put(Integer.valueOf(1), region);
					getOut(BMECatConstants.BMECat2CSV.REGION_FILENAME).write(regionLine);
					this.regionSize = Long.valueOf(regionSize.longValue() + 1);
				}
			}
		}

		//collect information for statistics
		catalogStatistics(catalog);

		//TODO in catalog.csv, observe the buyerID and the supplierID once more

		//HEADER.BUYER
		String buyerId = "";
		if (catalog.getBuyer() != null)
		{
			final Company buyer = catalog.getBuyer();
			buyerId = exportCompany(buyer);
			catalogline.put(Integer.valueOf(2), buyerId);
		}
		//HEADER.SUPPLIER
		String supplierId = "";
		if (catalog.getSupplier() != null)
		{
			final Company supplier = catalog.getSupplier();
			supplierId = exportCompany(supplier);
			catalogline.put(Integer.valueOf(3), supplierId);
		}
		//HEADER.AGREEMENT
		if (catalog.getAgreements() != null)
		{
			exportAgreements(catalog.getAgreements(), catalog.getID(), buyerId, supplierId, catalog.getDefaultCurrency());
		}
		//set the catalog currency
		if (catalog.getDefaultCurrency() != null)
		{
			this.catalogCurrency = catalog.getDefaultCurrency();
		}

		getOut(BMECatConstants.BMECat2CSV.CATALOG_OBJECT_FILENAME).write(catalogline);
		this.catalogSize = Long.valueOf(catalogSize.longValue() + 1);
		getOut(BMECatConstants.BMECat2CSV.CATALOG_VERSION_FILENAME).write(catalogVersionLine);
		this.catalogVersionSize = Long.valueOf(catalogVersionSize.longValue() + 1);
	}

	//statistics for catalog and its version
	private void catalogStatistics(final Catalog catalog)
	{
		buf.append("catalog.id=" + catalog.getID() + "\r\n");
		buf.append("catalog.version=" + catalog.getVersion() + "\r\n");
		buf.append("catalog.language=" + catalog.getLanguage() + "\r\n");
		buf.append("catalog.currency=" + catalog.getDefaultCurrency() + "\r\n");

		if (catalog.getName() != null)
		{
			buf.append("catalog.name=" + catalog.getName() + "\r\n");
		}
		if (catalog.getInclAssurance() != null)
		{
			buf.append("catalog.price.assurance=" + catalog.getInclAssurance().toString() + "\r\n");
		}
		if (catalog.getInclDuty() != null)
		{
			buf.append("catalog.price.duty=" + catalog.getInclDuty().toString() + "\r\n");
		}
		if (catalog.getInclFreight() != null)
		{
			buf.append("catalog.price.freight=" + catalog.getInclFreight().toString() + "\r\n");
		}
		if (catalog.getInclPacking() != null)
		{
			buf.append("catalog.price.packing=" + catalog.getInclPacking().toString() + "\r\n");
		}
		if (catalog.getGenerationDate() != null)
		{
			setBmecatCatalogDate(catalog.getGenerationDate());
			buf.append("catalog.price.generationdate=" + catalog.getGenerationDate().toString() + "\r\n");
		}
		setGeneralCatalogInfo(buf.toString());
		switch (this.bmecatType)
		{
			case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
				buf.append("catalog.transaction.mode=T_NEW_CATALOG\r\n");
				break;
			case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
				buf.append("catalog.transaction.mode=T_UPDATE_PRODUCTS\r\n");
				break;
			case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
				buf.append("catalog.transaction.mode=T_UPDATE_PRICES\r\n");
				break;
		}

		buf.append("\r\n");
	}

	public Date getBmecatCatalogDate()
	{
		return this.bmecatCatalogDate;
	}

	private void setBmecatCatalogDate(final Date date)
	{
		this.bmecatCatalogDate = date;
	}

	protected void setGeneralCatalogInfo(String info)
	{
		this.generalCatalogInfo = "";
		int endPos = info.indexOf('\n');
		//make the statistics more readable in HTML
		while (endPos != -1)
		{
			final String lineOutput = info.substring(0, endPos);
			final int charIndex = lineOutput.indexOf('=');
			final String bmecatHeaderVarName = lineOutput.substring(0, charIndex);
			final String bmecatHeaderVarValue = lineOutput.substring(charIndex + 1);
			this.generalCatalogInfo += lineOutput + "<br/>";
			info = info.substring(endPos + 1);
			endPos = info.indexOf('\n');
			this.bmecatHeader.put(bmecatHeaderVarName, bmecatHeaderVarValue);
			this.bmecatHeader.get("");
		}
	}

	//the values from the "HEADER" element of bmecat.xml
	public Map<String, String> getBmecatHeader()
	{
		return this.bmecatHeader;
	}

	//general information of the catalog in the bmecat.xml
	public String getGeneralCatalogInfo()
	{
		return this.generalCatalogInfo;
	}

	public StringBuilder getStatistics()
	{
		return this.buf;
	}

	private void exportAgreements(final Collection<Agreement> agr_coll, final String catalogid, final String buyer,
			final String supplier, final String curr) throws IOException
	{
		if (!agr_coll.isEmpty())
		{
			addAgreementComment();
		}

		for (final Agreement agr : agr_coll)
		{
			final Map<Integer, String> agr_line = new HashMap<Integer, String>();
			agr_line.put(Integer.valueOf(0), catalogid);
			agr_line.put(Integer.valueOf(1), agr.getID());
			if (agr.getStartDate() != null)
			{
				agr_line.put(Integer.valueOf(2), DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN)
						.format(agr.getStartDate()));
			}
			else
			{
				agr_line.put(Integer.valueOf(2), "");
			}
			if (agr.getEndDate() != null)
			{
				agr_line.put(Integer.valueOf(3), DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN)
						.format(agr.getEndDate()));
			}
			else
			{
				agr_line.put(Integer.valueOf(3), "");
			}
			agr_line.put(Integer.valueOf(4), buyer);
			agr_line.put(Integer.valueOf(5), supplier);
			agr_line.put(Integer.valueOf(6), curr);
			getOut(BMECatConstants.BMECat2CSV.AGREEMENT_FILENAME).write(agr_line);
			this.agreementSize = Long.valueOf(agreementSize.longValue() + 1);
		}
	}

	private void addAgreementComment()
	{
		final Map<Integer, String> agreementLine = new HashMap<Integer, String>();
		agreementLine.put(Integer.valueOf(0), "##Catalog ID");
		agreementLine.put(Integer.valueOf(1), "Agreement ID");
		agreementLine.put(Integer.valueOf(2), "Start date");
		agreementLine.put(Integer.valueOf(3), "End date");
		agreementLine.put(Integer.valueOf(4), "Buyer");
		agreementLine.put(Integer.valueOf(5), "Supplier");
		agreementLine.put(Integer.valueOf(6), "Currency");
		writeCsvLine(BMECatConstants.BMECat2CSV.AGREEMENT_FILENAME, agreementLine);
	}

	private String getCompanyID(final Company company)
	{
		String companyId = company.getId();
		//if company id is not specific, and if all 4 types of id are not specific, 
		//then take the company name as id
		if (companyId == null || companyId.length() == 0)
		{
			if (company.getDunsID() != null && company.getDunsID().length() > 0)
			{
				companyId = company.getDunsID();
			}
			else if (company.getIlnID() != null && company.getIlnID().length() > 0)
			{
				companyId = company.getIlnID();
			}
			else if (company.getBuyerSpecificID() != null && company.getBuyerSpecificID().length() > 0)
			{
				companyId = company.getBuyerSpecificID();
			}
			else if (company.getSupplierSpecificID() != null && company.getSupplierSpecificID().length() > 0)
			{
				companyId = company.getSupplierSpecificID();
			}
			else
			{
				companyId = company.getName();
			}
		}
		return companyId;
	}

	private String exportCompany(final Company company) throws IOException
	{
		if (companySize.longValue() == 0)
		{
			addCompanyComment();
		}

		final Map<Integer, String> customerline = new HashMap<Integer, String>();
		customerline.put(Integer.valueOf(0), company.getName());
		final String companyId = getCompanyID(company);

		customerline.put(Integer.valueOf(1), companyId);
		customerline.put(Integer.valueOf(2), company.getDunsID());
		customerline.put(Integer.valueOf(3), company.getIlnID());
		customerline.put(Integer.valueOf(4), company.getBuyerSpecificID());
		customerline.put(Integer.valueOf(5), company.getSupplierSpecificID());
		//MIME for BUYER/SUPPLIER
		String medias = "";
		for (final Map.Entry<String, List<Mime>> e : groupMimes(company.getMedias()).entrySet())
		{
			for (final Mime m : e.getValue())
			{
				medias = medias + m.getSource() + collectionseparator;
			}
		}
		if (medias.length() > 0)
		{
			medias = medias.substring(0, medias.length() - 1);
		}

		customerline.put(Integer.valueOf(6), medias);
		getOut(BMECatConstants.BMECat2CSV.COMPANY_FILENAME).write(customerline);
		this.companySize = Long.valueOf(this.companySize.longValue() + 1);

		final Address adr = company.getAddress();
		if (adr != null)
		{
			if (addressSize.longValue() == 0)
			{
				addAddressComment();
			}

			final Map<Integer, String> adrline = new HashMap<Integer, String>();
			adrline.put(Integer.valueOf(0), companyId);
			adrline.put(Integer.valueOf(1), adr.getName());
			adrline.put(Integer.valueOf(2), adr.getName2());
			adrline.put(Integer.valueOf(3), adr.getName3());
			adrline.put(Integer.valueOf(4), adr.getContact());
			adrline.put(Integer.valueOf(5), adr.getStreet());
			adrline.put(Integer.valueOf(6), adr.getZip());
			adrline.put(Integer.valueOf(7), adr.getBoxno());
			adrline.put(Integer.valueOf(8), adr.getZipbox());
			adrline.put(Integer.valueOf(9), adr.getCity());
			adrline.put(Integer.valueOf(10), adr.getState());
			adrline.put(Integer.valueOf(11), adr.getCountry());
			adrline.put(Integer.valueOf(12), adr.getPhone());
			adrline.put(Integer.valueOf(13), adr.getFax());
			adrline.put(Integer.valueOf(14), adr.getEmail());
			adrline.put(Integer.valueOf(15), adr.getPublicKey());
			adrline.put(Integer.valueOf(16), adr.getUrl());
			adrline.put(Integer.valueOf(17), adr.getRemarks());
			getOut(BMECatConstants.BMECat2CSV.ADDRESS_FILENAME).write(adrline);
			this.addressSize = Long.valueOf(addressSize.longValue() + 1);
		}
		return companyId;
	}

	private void addCompanyComment()
	{
		final Map<Integer, String> customerLine = new HashMap<Integer, String>();
		customerLine.put(Integer.valueOf(0), "##Company name");
		customerLine.put(Integer.valueOf(1), "Company ID");
		customerLine.put(Integer.valueOf(2), "Duns ID");
		customerLine.put(Integer.valueOf(3), "Iln ID");
		customerLine.put(Integer.valueOf(4), "Buyer specific ID");
		customerLine.put(Integer.valueOf(5), "Supplier specific ID");
		customerLine.put(Integer.valueOf(6), "Medias");
		writeCsvLine(BMECatConstants.BMECat2CSV.COMPANY_FILENAME, customerLine);
	}

	private void addAddressComment()
	{
		final Map<Integer, String> addressLine = new HashMap<Integer, String>();
		addressLine.put(Integer.valueOf(0), "##Company ID");
		addressLine.put(Integer.valueOf(1), "Address name");
		addressLine.put(Integer.valueOf(2), "Address name2");
		addressLine.put(Integer.valueOf(3), "Address name3");
		addressLine.put(Integer.valueOf(4), "Contact");
		addressLine.put(Integer.valueOf(5), "Street");
		addressLine.put(Integer.valueOf(6), "Zip");
		addressLine.put(Integer.valueOf(7), "Boxno");
		addressLine.put(Integer.valueOf(8), "Zipbox");
		addressLine.put(Integer.valueOf(9), "City");
		addressLine.put(Integer.valueOf(10), "State");
		addressLine.put(Integer.valueOf(11), "Country");
		addressLine.put(Integer.valueOf(12), "Phone");
		addressLine.put(Integer.valueOf(13), "Fax");
		addressLine.put(Integer.valueOf(14), "Email");
		addressLine.put(Integer.valueOf(15), "Public key");
		addressLine.put(Integer.valueOf(16), "Url");
		addressLine.put(Integer.valueOf(17), "Remarks");
		writeCsvLine(BMECatConstants.BMECat2CSV.ADDRESS_FILENAME, addressLine);
	}

	private void exportMime(final String identification, final Mime mime) throws IOException
	{
		if (!mediacache.contains(identification))
		{
			if (mimeSize.longValue() == 0)
			{
				addMimeComment();
			}

			mediacache.add(identification);

			final Map<Integer, String> mediamapline = new HashMap<Integer, String>();
			final int lastSlashOfSource = mime.getSource().lastIndexOf("/");
			mediamapline.put(Integer.valueOf(0), mime.getSource().substring(lastSlashOfSource + 1));
			mediamapline.put(Integer.valueOf(1), mime.getSource());
			mediamapline.put(Integer.valueOf(2), mimeRoot + mime.getSource());
			mediamapline.put(Integer.valueOf(3), mime.getType());
			mediamapline.put(Integer.valueOf(4), mime.getDescription());
			mediamapline.put(Integer.valueOf(5), mime.getAlt());
			//there is no "Order" attribute in hybris system
			mediamapline.put(Integer.valueOf(6), mime.getOrder() == null ? "" : mime.getOrder().toString());
			//there is no "Purpose" attribute for Media in hybris system, but should be specified in ARTICLE 
			mediamapline.put(Integer.valueOf(7), mime.getPurpose());
			getOut(BMECatConstants.BMECat2CSV.MIME_OBJECT_FILENAME).write(mediamapline);
			this.mimeSize = Long.valueOf(mimeSize.longValue() + 1);
		}
	}

	private void addMimeComment()
	{
		final Map<Integer, String> mimeLine = new HashMap<Integer, String>();
		mimeLine.put(Integer.valueOf(0), "##Code");
		mimeLine.put(Integer.valueOf(1), "Source");
		mimeLine.put(Integer.valueOf(2), "Root source");
		mimeLine.put(Integer.valueOf(3), "Type");
		mimeLine.put(Integer.valueOf(4), "Description");
		mimeLine.put(Integer.valueOf(5), "Alt text");
		mimeLine.put(Integer.valueOf(6), "Order");
		mimeLine.put(Integer.valueOf(7), "Purpose");
		writeCsvLine(BMECatConstants.BMECat2CSV.MIME_OBJECT_FILENAME, mimeLine);
	}

	private Map<String, List<Mime>> groupMimes(final Collection<Mime> coll) throws IOException
	{
		final Map<String, List<Mime>> ret = new HashMap<String, List<Mime>>();
		if (coll != null)
		{
			//grouping the mimes
			for (final Mime m : coll)
			{
				final String u_id = mimeRoot + m.getSource();
				exportMime(u_id, m); //here write to medias.csv
				final String purpose = m.getPurpose() == null ? "" : m.getPurpose().toLowerCase();
				List list = ret.get(purpose);
				if (list == null)
				{
					ret.put(purpose, list = new ArrayList());
				}
				list.add(m);
			}
			//here sorting the mimes
			for (final Map.Entry<String, List<Mime>> e : ret.entrySet())
			{
				final List<Mime> mimes = e.getValue();
				Collections.sort(mimes, new Comparator()
				{
					public int compare(final Object object1, final Object object2)
					{
						final Integer mimeOrder1 = ((Mime) object1).getOrder();
						final Integer mimeOrder2 = ((Mime) object2).getOrder();
						return (mimeOrder1 != null ? mimeOrder1.intValue() : Integer.MAX_VALUE)
								- (mimeOrder2 != null ? mimeOrder2.intValue() : Integer.MAX_VALUE);
					}
				});
			}
		}
		return ret;
	}


	/**
	 * Export a BMECat Article object into:<br>
	 * <ul>
	 * <li>- Product</li>
	 * <li>- Keyword</li>
	 * <li>- Media</li>
	 * </ul>
	 * 
	 * @param article
	 * @throws IOException
	 */
	private void exportArticleObject(final Article article) throws IOException
	{
		switch (this.bmecatType)
		{
			case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
				//article mode must be "new" if it is specified in T_NEW_CATALOG
				if (article.getMode() == null || article.getMode().trim().length() == 0
						|| "new".equals(article.getMode().toLowerCase()))
				{
					exportArticleInNewCatalogObject(article);
				}
				else
				{
					LOG.info("Mode [" + article.getMode() + "] is not allowed in T_NEW_CATALOG.");
				}
				break;
			case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
				//all modes are possible in T_UPDATE_PRODUCTS, 
				//but exactly one mode must be specified
				if ("new".equals(article.getMode().toLowerCase()) || "update".equals(article.getMode().toLowerCase()))
				{
					exportArticleInUpdateProductsObject(article);
				}
				else if ("delete".equals(article.getMode().toLowerCase()))
				{
					exportDeleteArticleInUpdateProductsObject(article);
				}
				else
				{
					LOG.info("Mode [" + article.getMode() + "] is not valid for article [" + article.getSupplierAID() + "].");
				}
				break;
			case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
				//article mode must be specified in T_UPDATE_PRICES and must be "update"
				if ("update".equals(article.getMode().toLowerCase().trim()))
				{
					exportArticleInUpdatePricesObject(article);
				}
				else
				{
					LOG.info("Mode [" + article.getMode() + "] is not valid for article [" + article.getSupplierAID() + "].");
				}
				break;
			default:
				throw new IllegalStateException("Such type not supported.");
		}
	}

	private void exportArticleInNewCatalogObject(final Article article) throws IOException
	{
		if (this.articleSize.longValue() == 0)
		{
			addArticleInNewCatalogComment();
		}

		exportGeneralArticle(article);
	}

	private void exportArticleInUpdateProductsObject(final Article article) throws IOException
	{
		//parse ARTICLE in T_UPDATE_PRODUCTS 
		if (this.updateArticleSize.longValue() == 0)
		{
			addArticleInUpdateProductsComment();
		}

		exportGeneralArticle(article);
	}

	private void exportDeleteArticleInUpdateProductsObject(final Article article) throws IOException
	{
		//since the mode is "delete", no properties need to be transformed to csv files
		if (this.deleteArticleSize.longValue() == 0)
		{
			addArticleDeleteInUpdateProductsComment(article);
		}

		final Map<Integer, String> articleDeleteLine = new HashMap<Integer, String>();
		articleDeleteLine.put(Integer.valueOf(0), article.getMode());
		articleDeleteLine.put(Integer.valueOf(1), article.getSupplierAID());

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE_DELETE_MODE, articleDeleteLine);
		this.deleteArticleSize = Long.valueOf(deleteArticleSize.longValue() + 1);
	}

	private void addArticleDeleteInUpdateProductsComment(final Article article) throws IOException
	{
		final Map<Integer, String> articleDeleteLine = new HashMap<Integer, String>();
		articleDeleteLine.put(Integer.valueOf(0), "##Mode");
		articleDeleteLine.put(Integer.valueOf(1), "Supplier AID");

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE_DELETE_MODE, articleDeleteLine);
	}

	private void exportArticleInUpdatePricesObject(final Article article) throws IOException
	{
		if (this.previousVersion.trim().isEmpty())
		{
			LOG.info("Previous version is not valid for article [" + article.getSupplierAID() + "].");
			return;
		}

		if (this.articleUpdatePriceSize.longValue() == 0)
		{
			addArticleUpdatePriceComment();
		}

		if (this.articlePriceSize.longValue() == 0)
		{
			addArticlePriceComment();
		}

		final Map<Integer, String> articleLine = new HashMap<Integer, String>();
		articleLine.put(Integer.valueOf(0), article.getSupplierAID());
		articleLine.put(Integer.valueOf(1), updateArticlePrices(article));

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE_UPDATE_PRICES, articleLine);
		this.articleUpdatePriceSize = Long.valueOf(articleUpdatePriceSize.longValue() + 1);
	}

	private void exportClassificationSystem(final ClassificationSystem system) throws IOException
	{
		if (this.classificationSystemSize.longValue() == 0)
		{
			addClassificationSystemComment();
		}

		if (this.classificationSystemVersionSize.longValue() == 0)
		{
			addClassificationSystemVersionComment();
		}

		final Map<Integer, String> classificationSystemLine = new HashMap<Integer, String>();
		final Map<Integer, String> classificationVersionLine = new HashMap<Integer, String>();

		//catalog
		classificationSystemLine.put(Integer.valueOf(0), system.getName());
		classificationSystemLine.put(Integer.valueOf(1), system.getName());

		//catalogVersion
		classificationVersionLine.put(Integer.valueOf(0), system.getName());
		classificationVersionLine.put(Integer.valueOf(1), system.getVersion());
		classificationVersionLine.put(Integer.valueOf(2), "");//catalog.getLanguage()

		getOut(BMECatConstants.BMECat2CSV.CLASSIFICATION_SYSTEM_FILENAME).write(classificationSystemLine);
		this.classificationSystemSize = Long.valueOf(classificationSystemSize.longValue() + 1);

		getOut(BMECatConstants.BMECat2CSV.CLASSIFICATION_SYSTEM_VERSION_FILENAME).write(classificationVersionLine);
		this.classificationSystemVersionSize = Long.valueOf(classificationSystemVersionSize.longValue() + 1);

		for (final ClassificationGroup group : system.getGroups())
		{
			exportClassificationGroup(group, system);
		}
	}

	private void exportClassificationGroup(final ClassificationGroup group, final ClassificationSystem system) throws IOException
	{
		if (this.classificationClassSize.longValue() == 0)
		{
			addClassificationClassComment();
		}

		final Map<Integer, String> classificationclassline = new HashMap<Integer, String>();
		classificationclassline.put(Integer.valueOf(0), group.getID());
		classificationclassline.put(Integer.valueOf(1), group.getName());
		classificationclassline.put(Integer.valueOf(2), group.getDescription());
		classificationclassline.put(Integer.valueOf(3), (group.getParentID() == null || group.getParentID().equals("0")) ? ""
				: group.getParentID() + ":" + system.getName() + ":" + system.getVersion());
		classificationclassline.put(Integer.valueOf(4), system.getName() + ":" + system.getVersion());

		getOut(BMECatConstants.BMECat2CSV.CLASSIFICATION_CLASS_FILENAME).write(classificationclassline);
		this.classificationClassSize = Long.valueOf(classificationClassSize.longValue() + 1);
	}

	private final Map<String, Map<String, List<String>>> exportedAssignments = new HashMap<String, Map<String, List<String>>>();
	private final Map<String, List<String>> exportedAttributes = new HashMap<String, List<String>>();

	private void exportGeneralArticle(final Article article) throws IOException
	{
		final Map<Integer, String> articleLine = new HashMap<Integer, String>();
		articleLine.put(Integer.valueOf(0), article.getMode());
		articleLine.put(Integer.valueOf(1), article.getSupplierAID());
		/* <ARTICLE_DETAILS> */
		articleLine.put(Integer.valueOf(2), article.getShortDescription());
		articleLine.put(Integer.valueOf(3), article.getLongDescription());
		articleLine.put(Integer.valueOf(4), article.getEan());
		articleLine.put(Integer.valueOf(5), article.getSupplierAlternativeAID());
		articleLine.put(Integer.valueOf(6), article.getManufacturerAID());
		articleLine.put(Integer.valueOf(7), article.getManufacturerName());
		articleLine.put(Integer.valueOf(8), article.getManufacturerTypeDescription());
		articleLine.put(Integer.valueOf(9), article.getErpGroupBuyer());
		articleLine.put(Integer.valueOf(10), article.getErpGroupSupplier());
		articleLine.put(Integer.valueOf(11),
				article.getDeliveryTime() == null ? "" : decimalFormat.format(article.getDeliveryTime()));

		//Collection of keywords
		String keywords = "";
		for (final String keyword : article.getKeywords())
		{
			exportKeyword(keyword);
			keywords += keyword + this.collectionseparator;
		}
		if (keywords.length() > 0)
		{
			keywords = keywords.substring(0, keywords.length() - 1);
		}
		articleLine.put(Integer.valueOf(12), keywords);

		articleLine.put(Integer.valueOf(13), article.getRemarks());
		articleLine.put(Integer.valueOf(14), article.getSegment());
		articleLine.put(Integer.valueOf(15), article.getOrder() == null ? "" : article.getOrder().toString());

		//ARTICLE.ARTICLE_ORDER_DETAILS
		articleLine.put(Integer.valueOf(16), article.getOrderUnit());
		if (article.getOrderUnit() != null)
		{
			this.units.add(article.getOrderUnit());
		}
		articleLine.put(Integer.valueOf(17), article.getContentUnit());
		if (article.getContentUnit() != null)
		{
			this.units.add(article.getContentUnit());
		}

		articleLine.put(Integer.valueOf(18),
				article.getNumberContentUnits() == null ? "" : decimalFormat.format(article.getNumberContentUnits()));
		articleLine.put(Integer.valueOf(19),
				article.getPriceQuantity() == null ? "" : decimalFormat.format(article.getPriceQuantity()));
		articleLine.put(Integer.valueOf(20), article.getMinOrderQuantity() == null ? "" : article.getMinOrderQuantity().toString());
		articleLine.put(Integer.valueOf(21), article.getOrderQuantityInterval() == null ? "" : article.getOrderQuantityInterval()
				.toString());

		String mimeOthers = "";
		/* <MIME_INFO> */
		for (final Map.Entry<String, List<Mime>> e : groupMimes(article.getMedias()).entrySet())
		{
			if (e.getKey().equals(Mime.PURPOSE.NORMAL))
			{
				if (e.getValue().size() > 0)
				{
					articleLine.put(Integer.valueOf(22), getMimeNames(e.getValue()));
				}
			}
			else if (e.getKey().equals(Mime.PURPOSE.THUMBNAIL))
			{
				if (e.getValue().size() > 0)
				{
					articleLine.put(Integer.valueOf(23), getMimeNames(e.getValue()));
				}
			}
			else if (e.getKey().equals(Mime.PURPOSE.DATA_SHEET))
			{
				if (e.getValue().size() > 0)
				{
					articleLine.put(Integer.valueOf(24), getMimeNames(e.getValue()));
				}
			}
			else if (e.getKey().equals(Mime.PURPOSE.DETAIL))
			{
				if (e.getValue().size() > 0)
				{
					articleLine.put(Integer.valueOf(25), getMimeNames(e.getValue()));
				}
			}
			else if (e.getKey().equals(Mime.PURPOSE.LOGO))
			{
				if (e.getValue().size() > 0)
				{
					articleLine.put(Integer.valueOf(26), getMimeNames(e.getValue()));
				}
			}
			else if (e.getKey().equals(Mime.PURPOSE.OTHERS))
			{
				if (e.getValue().size() > 0)
				{
					if (mimeOthers.length() == 0)
					{
						mimeOthers += getMimeNames(e.getValue());
					}
					else
					{
						mimeOthers += this.collectionseparator + getMimeNames(e.getValue());
					}
				}
			}
			else if (e.getKey().length() == 0)
			{
				if (e.getValue().size() > 0)
				{
					final String notDefinedMime = getMimeNames(e.getValue());
					articleLine.put(Integer.valueOf(28), notDefinedMime);
					if (mimeOthers.length() == 0)
					{
						mimeOthers += notDefinedMime;
					}
					else
					{
						mimeOthers += this.collectionseparator + notDefinedMime;
					}
				}
			}
			articleLine.put(Integer.valueOf(27), mimeOthers);
		}

		//ARTICLE.ARTICLE_FEATURES
		if (article.getArticleFeatures().size() > 0)
		{
			for (final ArticleFeatures af : article.getArticleFeatures())
			{
				if (articleFeatureSize.longValue() == 0
						&& (af.getReferenceFeatureSystemName() != null || af.getReferenceFeatureGroupId() != null || af
								.getReferenceFeatureGroupName() != null))
				{
					addArticleFeatureComment();
					break;
				}
			}

			for (final ArticleFeatures articleFeature : article.getArticleFeatures())
			{
				if (articleFeature.getReferenceFeatureSystemName() != null || articleFeature.getReferenceFeatureGroupId() != null
						|| articleFeature.getReferenceFeatureGroupName() != null)
				{
					final Map<Integer, String> afLine = new HashMap<Integer, String>();
					afLine.put(Integer.valueOf(0), article.getSupplierAID());
					afLine.put(Integer.valueOf(1), articleFeature.getReferenceFeatureSystemName());
					if (articleFeature.getReferenceFeatureSystemName() != null)
					{
						this.classificationSystems.add(articleFeature.getReferenceFeatureSystemName());
					}
					//either groupId or groupName or neither 
					if (articleFeature.getReferenceFeatureGroupId() != null
							&& articleFeature.getReferenceFeatureGroupId().trim().length() > 0)
					{
						afLine.put(Integer.valueOf(2), articleFeature.getReferenceFeatureGroupId());
					}
					else
					{
						afLine.put(Integer.valueOf(3), articleFeature.getReferenceFeatureGroupName());
					}
					getOut(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_FILENAME).write(afLine);
					articleFeatureSize = Long.valueOf(articleFeatureSize.longValue() + 1);
				}

				Map<String, List<String>> featureSystem = exportedAssignments.get(articleFeature.getReferenceFeatureSystemName());
				if (featureSystem == null)
				{
					featureSystem = new HashMap<String, List<String>>();
					exportedAssignments.put(articleFeature.getReferenceFeatureSystemName(), featureSystem);
				}
				List<String> assignments = featureSystem.get(articleFeature.getReferenceFeatureGroupId());
				if (assignments == null)
				{
					assignments = new ArrayList<String>();
					featureSystem.put(articleFeature.getReferenceFeatureGroupId(), assignments);
				}

				List<String> attributes = exportedAttributes.get(articleFeature.getReferenceFeatureSystemName());
				if (attributes == null)
				{
					attributes = new ArrayList<String>();
					exportedAttributes.put(articleFeature.getReferenceFeatureSystemName(), attributes);
				}

				for (final Feature f : articleFeature.getFeatures())
				{
					if (!assignments.contains(f.getFname()))
					{
						assignments.add(f.getFname());

						if (this.classificationAttributeAssignmentSize.longValue() == 0)
						{
							addClassificationAttributeAssignmentComment();
						}

						final Map<Integer, String> classificationAssignmentLine = new HashMap<Integer, String>();
						classificationAssignmentLine.put(Integer.valueOf(0), articleFeature.getReferenceFeatureGroupId() + ":"
								+ articleFeature.getReferenceFeatureSystemName());
						classificationAssignmentLine.put(Integer.valueOf(1),
								f.getFname() + ":" + articleFeature.getReferenceFeatureSystemName());
						classificationAssignmentLine.put(Integer.valueOf(2), articleFeature.getReferenceFeatureSystemName());

						getOut(BMECatConstants.BMECat2CSV.CLASSIFICATION_ATTRIBUTE_ASSIGNMENT_FILENAME).write(
								classificationAssignmentLine);
						this.classificationAttributeAssignmentSize = Long
								.valueOf(classificationAttributeAssignmentSize.longValue() + 1);
					}

					if (!attributes.contains(f.getFname()))
					{
						attributes.add(f.getFname());

						if (this.classificationAttributeSize.longValue() == 0)
						{
							addClassificationAttributeComment();
						}

						final Map<Integer, String> classificationAttributLine = new HashMap<Integer, String>();
						classificationAttributLine.put(Integer.valueOf(0), f.getFname());
						classificationAttributLine.put(Integer.valueOf(1), f.getFname());
						classificationAttributLine.put(Integer.valueOf(2), articleFeature.getReferenceFeatureSystemName());

						getOut(BMECatConstants.BMECat2CSV.CLASSIFICATION_ATTRIBUTE_FILENAME).write(classificationAttributLine);
						this.classificationAttributeSize = Long.valueOf(classificationAttributeSize.longValue() + 1);
					}
				}

				final Map<Integer, String> afLine = new HashMap<Integer, String>();
				afLine.put(Integer.valueOf(0), article.getSupplierAID());
				afLine.put(Integer.valueOf(1), articleFeature.getReferenceFeatureSystemName());

				//ARTICLE.ARTICLE_FEATURES.FEATURE
				if (articleFeature.getFeatures() != null && articleFeature.getFeatures().size() > 0)
				{
					if (articleFeatureDetailsSize.longValue() == 0)
					{
						addArticleFeatureDetailsComment();
						addArticleFeatureValueMappingsHeader();
					}

					//TODO attribute distribution in 3 files or 2 files?
					//productfeaturedetails.csv, productfeatures.csv, and productfeaturevariants.csv
					final StringBuilder featureLine = new StringBuilder(
							"UPDATE PRODUCT; catalogVersion(catalog(id),version)[default=$catalog_id:$catalog_version, unique=true]; code[unique=true]; ");
					final int orignalLength = featureLine.length();
					final StringBuilder valueLine = new StringBuilder("; ; " + article.getSupplierAID() + "; ");
					for (final Feature f : articleFeature.getFeatures())
					{
						final Map<Integer, String> fLine = new HashMap<Integer, String>();
						fLine.put(Integer.valueOf(0), article.getSupplierAID());
						fLine.put(Integer.valueOf(1), f.getFname());
						//either VARIANTS or FVALUE must be specified
						if (f.getVariants() != null)
						{
							fLine.put(Integer.valueOf(2), "");
							//ARTICLE.ARTICLE_FEATURES.FEATURE.VARIANTS
							if (articleFeatureVariantsSize.longValue() == 0)
							{
								addArticleFeatureVariantsComment();
							}

							final Variants variants = f.getVariants();
							final String valueOrder = variants.getVorder() == null ? "" : variants.getVorder().toString();
							for (final Variant v : variants.getVariants())
							{
								final Map<Integer, String> articleFeatureVariantLine = new HashMap<Integer, String>();
								articleFeatureVariantLine.put(Integer.valueOf(0), article.getSupplierAID());
								articleFeatureVariantLine.put(Integer.valueOf(1), f.getFname());
								articleFeatureVariantLine.put(Integer.valueOf(2), v.getFvalue());
								articleFeatureVariantLine.put(Integer.valueOf(3), v.getSupplierAidSupplement());
								articleFeatureVariantLine.put(Integer.valueOf(4), valueOrder);
								getOut(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_VARIANTS).write(articleFeatureVariantLine);
								articleFeatureVariantsSize = Long.valueOf(articleFeatureVariantsSize.longValue() + 1);
							}
						}
						else
						//(f.getFvalues() != null && f.getFvalues().length > 0)
						{
							String fvalues = "";
							for (final String fvalue : f.getFvalues())
							{
								fvalues += fvalue + this.collectionseparator;
							}
							final int fvaluesLenth = fvalues.length();
							fLine.put(Integer.valueOf(2), fvalues.substring(0, fvaluesLenth - 1));
							if (fvalues.length() > 0)
							{
								featureLine.append("@"
										+ f.getFname()
										+ "[$YC"
										+ (articleFeature.getReferenceFeatureGroupId() == null ? "" : ",classificationClass='"
												+ articleFeature.getReferenceFeatureGroupId() + "'") + "]; ");
								valueLine.append(fvalues.substring(0, fvaluesLenth - 1) + "; ");
							}
						}

						fLine.put(Integer.valueOf(3), f.getFunit());
						if (f.getFunit() != null)
						{
							this.units.add(f.getFunit());
						}
						fLine.put(Integer.valueOf(4), f.getForder() == null ? "" : f.getForder().toString());
						fLine.put(Integer.valueOf(5), f.getDescription());
						fLine.put(Integer.valueOf(6), f.getValueDetails());
						getOut(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_DETAILS).write(fLine);
						articleFeatureDetailsSize = Long.valueOf(articleFeatureDetailsSize.longValue() + 1);
					}
					if (featureLine.length() > orignalLength)
					{
						final Map<Integer, String> featureValueMappingsLine = new HashMap<Integer, String>();
						featureValueMappingsLine.put(Integer.valueOf(0), featureLine.toString());
						getOut(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_VALUE_MAPPINGS).writeSrcLine(featureLine.toString());
						featureValueMappingsLine.clear();
						featureValueMappingsLine.put(Integer.valueOf(0), valueLine.toString());
						getOut(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_VALUE_MAPPINGS).writeSrcLine(valueLine.toString());
						this.articleFeatureValueMapplingsSize = Long.valueOf(articleFeatureValueMapplingsSize.longValue() + 1);
					}
				}
			}
		}

		//ARTICLE.ARTICLE_PRICE_DETAILS
		if (article.getArticlePriceDetails().size() > 0)
		{
			if (articlePriceSize.longValue() == 0)
			{
				addArticlePriceComment();
			}

			articleLine.put(Integer.valueOf(32), updateArticlePrices(article));
		}

		//ARTICLE.ARTICLE_DETAILS.BUYER_AID
		if (article.getBuyerIDs().entrySet().size() > 0)
		{
			int undefinedType = 0;
			String buyerIDS = "";
			if (article.getBuyerIDs().size() > 0)
			{
				for (final Map.Entry<String, String> me : article.getBuyerIDs().entrySet())
				{
					if (IDType.DUNS.equals(me.getKey()) || IDType.ILN.equals(me.getKey()) || IDType.BUYER_SPECIFIC.equals(me.getKey())
							|| IDType.SUPPLIER_SPECIFIC.equals(me.getKey()))
					{
						buyerIDS += me.getKey() + "->" + me.getValue() + ";";
					}
					else if (undefinedType == 0)
					{
						buyerIDS += IDType.UNSPECIFIED + "->" + me.getValue() + ";";
						undefinedType++;
					}
					//undefined type can appear no more than once
					else
					{
						LOG.error("Buyer AID [" + me.getValue() + "] is ignored, " + "because its type is not correct.");
					}
				}
				final int buyerIDSLength = buyerIDS.length();
				buyerIDS = buyerIDS.substring(0, buyerIDSLength - 1);
			}

			articleLine.put(Integer.valueOf(29), buyerIDS);
		}

		//ARTICLE.ARTICLE_REFERENCE
		if (article.getArticleReferences().size() > 0)
		{
			if (articleReferenceSize.longValue() == 0)
			{
				addArticleReferenceComment();
			}

			for (final ArticleReference articlereference : article.getArticleReferences())
			{
				final Map<Integer, String> articlereferenceline = new HashMap<Integer, String>();
				articlereferenceline.put(Integer.valueOf(0), article.getSupplierAID());
				articlereferenceline.put(Integer.valueOf(1), articlereference.getArticleReference());
				articlereferenceline.put(Integer.valueOf(2), articlereference.getType());
				articlereferenceline.put(Integer.valueOf(3), articlereference.getQuantity() == null ? "" : articlereference
						.getQuantity().toString());
				articlereferenceline.put(Integer.valueOf(4), articlereference.getCatalogID());
				articlereferenceline.put(Integer.valueOf(5), articlereference.getCatalogVersion());
				getOut(BMECatConstants.BMECat2CSV.ARTICLEREFERENCES_FILENAME).write(articlereferenceline);
				this.articleReferenceSize = Long.valueOf(articleReferenceSize.longValue() + 1);
			}
		}

		// ARTICLE.ARTICLE_DETAILS.SPECIAL_TREATMENT_CLASS
		if (article.getSpecialTreatmentClasses().entrySet().size() > 0)
		{
			String specialTreatmentClasses = "";
			if (article.getSpecialTreatmentClasses().size() > 0)
			{
				for (final Map.Entry<String, String> stc : article.getSpecialTreatmentClasses().entrySet())
				{
					specialTreatmentClasses += stc.getKey() + "->" + stc.getValue() + ";";
				}
				final int stcLength = specialTreatmentClasses.length();
				specialTreatmentClasses = specialTreatmentClasses.substring(0, stcLength - 1);
			}
			articleLine.put(Integer.valueOf(30), specialTreatmentClasses);
		}

		//ARTICLE.ARTICLE_DETAILS.ARTICLE_STATUS
		if (article.getStatus().entrySet().size() > 0)
		{
			String status = "";
			for (final Map.Entry<String, String> me : article.getStatus().entrySet())
			{
				status += me.getKey() + "->" + me.getValue() + ";";
			}
			final int statusLength = status.length();
			status = status.substring(0, statusLength - 1);
			articleLine.put(Integer.valueOf(31), status);
		}

		if (this.bmecatType == BMECatConstants.TRANSACTION.T_NEW_CATALOG)
		{
			getOut(BMECatConstants.BMECat2CSV.ARTICLE_OBJECT_FILENAME).write(articleLine);
			this.articleSize = Long.valueOf(articleSize.longValue() + 1);
		}
		else if (this.bmecatType == BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS)
		{
			articleLine.put(Integer.valueOf(33), this.previousVersion);
			getOut(BMECatConstants.BMECat2CSV.ARTICLE_UPDATE_FILENAME).write(articleLine);
			this.updateArticleSize = Long.valueOf(updateArticleSize.longValue() + 1);
		}
	}

	private String getMimeRealName(final String mimeSource)
	{
		final int lastSlashOfMimeSource = mimeSource.lastIndexOf('/');
		return mimeSource.substring(lastSlashOfMimeSource + 1);
	}

	private String getMimeNames(final List<Mime> mimes)
	{
		StringBuilder mimeName = new StringBuilder("");
		if (!mimes.isEmpty())
		{
			for (final Mime m : mimes)
			{
				mimeName.append(getMimeRealName(m.getSource()) + this.collectionseparator);
			}
			final int mimeNameLength = mimeName.length();
			mimeName = mimeName.delete(mimeNameLength - 1, mimeNameLength);
		}
		return mimeName.toString();
	}

	private void addArticleInNewCatalogComment()
	{
		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE_OBJECT_FILENAME, addArticleComment());
	}

	private void addArticleInUpdateProductsComment()
	{
		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE_UPDATE_FILENAME, addArticleComment());
	}

	private Map<Integer, String> addArticleComment()
	{
		final Map<Integer, String> articleLine = new HashMap<Integer, String>();
		articleLine.put(Integer.valueOf(0), "##Article mode");
		articleLine.put(Integer.valueOf(1), "Supplier AID");
		articleLine.put(Integer.valueOf(2), "Short description");
		articleLine.put(Integer.valueOf(3), "Long description");
		articleLine.put(Integer.valueOf(4), "Ean");
		articleLine.put(Integer.valueOf(5), "Supplier alternative AID");
		articleLine.put(Integer.valueOf(6), "Manufacturer AID");
		articleLine.put(Integer.valueOf(7), "Manufacturer name");
		articleLine.put(Integer.valueOf(8), "Manufacturer type description");
		articleLine.put(Integer.valueOf(9), "Erp group buyer");
		articleLine.put(Integer.valueOf(10), "Erp group supplier");
		articleLine.put(Integer.valueOf(11), "Delivery time");
		articleLine.put(Integer.valueOf(12), "Keywords");
		articleLine.put(Integer.valueOf(13), "Remarks");
		articleLine.put(Integer.valueOf(14), "Segment");
		articleLine.put(Integer.valueOf(15), "Order");
		articleLine.put(Integer.valueOf(16), "Order unit");
		articleLine.put(Integer.valueOf(17), "Content unit");
		articleLine.put(Integer.valueOf(18), "Number of content units");
		articleLine.put(Integer.valueOf(19), "Price quantity");
		articleLine.put(Integer.valueOf(20), "Min order quantity");
		articleLine.put(Integer.valueOf(21), "Interval order quantity");
		articleLine.put(Integer.valueOf(22), "Purpose normal");
		articleLine.put(Integer.valueOf(23), "Purpose thumbnail");
		articleLine.put(Integer.valueOf(24), "Purpose data_sheet");
		articleLine.put(Integer.valueOf(25), "Purpose detail");
		articleLine.put(Integer.valueOf(26), "Purpose logo");
		articleLine.put(Integer.valueOf(27), "Purpose others");
		articleLine.put(Integer.valueOf(28), "Purpose not defined");
		articleLine.put(Integer.valueOf(29), "Buyer AID type and value");
		articleLine.put(Integer.valueOf(30), "Special treatment class type and value");
		articleLine.put(Integer.valueOf(31), "Article status type and value");
		articleLine.put(Integer.valueOf(32), "Price collection");
		if (this.bmecatType == BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS)
		{
			articleLine.put(Integer.valueOf(33), "Previous version");
		}
		return articleLine;
	}

	private void addArticleFeatureComment()
	{
		final Map<Integer, String> articleFeatureLine = new HashMap<Integer, String>();
		articleFeatureLine.put(Integer.valueOf(0), "##Supplier AID");
		articleFeatureLine.put(Integer.valueOf(1), "Feature system name");
		articleFeatureLine.put(Integer.valueOf(2), "Feature group id");
		articleFeatureLine.put(Integer.valueOf(3), "Feature group name");

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_FILENAME, articleFeatureLine);
	}

	private void addArticleFeatureDetailsComment()
	{
		final Map<Integer, String> featureDeatilLine = new HashMap<Integer, String>();
		featureDeatilLine.put(Integer.valueOf(0), "##Supplier AID");
		featureDeatilLine.put(Integer.valueOf(1), "Feature name");
		featureDeatilLine.put(Integer.valueOf(2), "Feature values");
		featureDeatilLine.put(Integer.valueOf(3), "Feature unit");
		featureDeatilLine.put(Integer.valueOf(4), "Feature order");
		featureDeatilLine.put(Integer.valueOf(5), "Feature description");
		featureDeatilLine.put(Integer.valueOf(6), "Feature value details");

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_DETAILS, featureDeatilLine);
	}

	private void addArticleFeatureValueMappingsHeader()
	{
		try
		{
			getOut(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_VALUE_MAPPINGS).writeSrcLine(
					"$YC=class_system_versions='$class_system_versions', "
							+ "lang_iso='$lang_iso', translator=de.hybris.platform.bmecat.jalo."
							+ "bmecat2csv.BMECatClassificationAttributeTranslator;");
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	private void addArticleFeatureVariantsComment()
	{
		final Map<Integer, String> articleFeatureVariantLine = new HashMap<Integer, String>();
		articleFeatureVariantLine.put(Integer.valueOf(0), "##Supplier AID");
		articleFeatureVariantLine.put(Integer.valueOf(1), "Feature name");
		articleFeatureVariantLine.put(Integer.valueOf(2), "Feature value");
		articleFeatureVariantLine.put(Integer.valueOf(3), "Supplier AID supplement");
		articleFeatureVariantLine.put(Integer.valueOf(4), "Variant order");

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLEFEATURE_VARIANTS, articleFeatureVariantLine);
	}

	private void addArticleUpdatePriceComment()
	{
		final Map<Integer, String> articleUpdatePriceLine = new HashMap<Integer, String>();
		articleUpdatePriceLine.put(Integer.valueOf(0), "##Supplier AID");
		articleUpdatePriceLine.put(Integer.valueOf(1), "Price collection");

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE_UPDATE_PRICES, articleUpdatePriceLine);
	}

	private void addArticlePriceComment()
	{
		final Map<Integer, String> articlePriceLine = new HashMap<Integer, String>();
		articlePriceLine.put(Integer.valueOf(0), "##Supplier AID");
		articlePriceLine.put(Integer.valueOf(1), "Start date");
		articlePriceLine.put(Integer.valueOf(2), "End date");
		articlePriceLine.put(Integer.valueOf(3), "Daily price");
		articlePriceLine.put(Integer.valueOf(4), "Price type");
		articlePriceLine.put(Integer.valueOf(5), "Amount");
		articlePriceLine.put(Integer.valueOf(6), "Currency");
		articlePriceLine.put(Integer.valueOf(7), "Tax");
		articlePriceLine.put(Integer.valueOf(8), "Price factor");
		articlePriceLine.put(Integer.valueOf(9), "Lower bound");
		articlePriceLine.put(Integer.valueOf(10), "Territory");

		if (this.bmecatType == BMECatConstants.TRANSACTION.T_UPDATE_PRICES)
		{
			articlePriceLine.put(Integer.valueOf(11), "Previous version");
		}

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE2ARTICLEPRICE, articlePriceLine);
	}

	private String updateArticlePrices(final Article article)
	{
		StringBuilder articlePrices = new StringBuilder("");
		try
		{
			for (final ArticlePriceDetails apd : article.getArticlePriceDetails())
			{
				final Map<Integer, String> articlePriceLine = new HashMap<Integer, String>();

				for (final ArticlePrice ap : apd.getPrices())
				{
					articlePriceLine.put(Integer.valueOf(0), article.getSupplierAID());
					final SimpleDateFormat articlePriceDate = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
					articlePriceLine.put(Integer.valueOf(1),
							apd.getStartDate() == null ? "" : articlePriceDate.format(apd.getStartDate()));
					articlePriceLine
							.put(Integer.valueOf(2), apd.getEndDate() == null ? "" : articlePriceDate.format(apd.getEndDate()));
					articlePriceLine.put(Integer.valueOf(3), Boolean.toString(apd.isDailyPrice()));
					articlePriceLine.put(Integer.valueOf(4), ap.getPriceType());
					if (ap.getPriceType() != null)
					{
						this.priceTypes.add(ap.getPriceType());
					}
					//temporarily change the Locale, making the Double separated with "," instead of "."
					final Locale locale = Locale.getDefault();
					Locale.setDefault(Locale.GERMANY);
					final NumberFormat numberFormat = NumberFormat.getInstance();
					articlePriceLine.put(Integer.valueOf(5), numberFormat.format(ap.getAmount()));
					Locale.setDefault(locale);
					if (ap.getCurrency() != null)
					{
						articlePriceLine.put(Integer.valueOf(6), ap.getCurrency() + " ");
						this.currencies.add(ap.getCurrency());
					}
					else
					{
						articlePriceLine.put(Integer.valueOf(6), this.catalogCurrency + " ");
					}

					articlePriceLine.put(Integer.valueOf(7), Double.toString(ap.getTax()));
					if (Double.toString(ap.getTax()) != null)
					{
						this.taxes.add(Double.toString(ap.getTax()));
					}
					articlePriceLine.put(Integer.valueOf(8), Double.toString(ap.getPriceFactor()));
					//lowerBound must be an Integer
					articlePriceLine.put(Integer.valueOf(9), Integer.toString((int) ap.getLowerBound()));

					//collecting all information for the products.csv
					articlePrices.append(addArticlePriceCollection(articlePriceLine));

					//in which country and/or region is the article available 
					//no such implementation in hybris system, just to avoid losing the information
					if (ap.getTerritories().size() > 0)
					{
						String territory = "";
						for (final String t : ap.getTerritories())
						{
							territory += t + this.collectionseparator;
						}
						final int territoryLength = territory.length();
						articlePriceLine.put(Integer.valueOf(10), territory.substring(0, territoryLength - 1));
					}
					else
					{
						articlePriceLine.put(Integer.valueOf(10), "");
					}

					if (this.bmecatType == BMECatConstants.TRANSACTION.T_UPDATE_PRICES)
					{
						articlePriceLine.put(Integer.valueOf(11), this.previousVersion);
					}

					getOut(BMECatConstants.BMECat2CSV.ARTICLE2ARTICLEPRICE).write(articlePriceLine);
					this.articlePriceSize = Long.valueOf(articlePriceSize.longValue() + 1);
				}
			}
		}
		catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}
		if (articlePrices.length() > 0)
		{
			final int articlePricesLength = articlePrices.length();
			articlePrices = articlePrices.delete(articlePricesLength - 2, articlePricesLength);
		}
		return articlePrices.toString();
	}

	private String addArticlePriceCollection(final Map<Integer, String> articlePrices)
	{
		//Syntax: ( ({user}|{userPriceGroup})? {quantity} {unitCode} '=')? {price} ({currencyISO}|{currencySign}) ('B'|'N'|'G')? ('['{from}','{to}']')? 
		//Example: "1 pieces=10,90 EUR B [01.01.2005,31.12.2005], 2 pieces=11,90 EUR B [01.01.2006,31.12.2006]";
		final StringBuilder articlePrice = new StringBuilder();
		//quantity
		articlePrice.append(articlePrices.get(Integer.valueOf(9)) + " ");
		//price, default unit is "PK"
		articlePrice.append("PK=" + articlePrices.get(Integer.valueOf(5)) + " ");
		//currencyISO
		articlePrice.append(articlePrices.get(Integer.valueOf(6)) + " ");
		//start time and end time
		if (articlePrices.get(Integer.valueOf(1)).trim().length() == 10
				&& articlePrices.get(Integer.valueOf(2)).trim().length() == 10)
		{
			articlePrice.append("[" + articlePrices.get(Integer.valueOf(1)) + "," + articlePrices.get(Integer.valueOf(2)) + "], ");
		}
		return articlePrice.toString();
	}

	private void addArticleReferenceComment()
	{
		final Map<Integer, String> articleReferenceLine = new HashMap<Integer, String>();
		articleReferenceLine.put(Integer.valueOf(0), "##Supplier AID");
		articleReferenceLine.put(Integer.valueOf(1), "Article reference");
		articleReferenceLine.put(Integer.valueOf(2), "Type");
		articleReferenceLine.put(Integer.valueOf(3), "Quantity");
		articleReferenceLine.put(Integer.valueOf(4), "Catalog ID");
		articleReferenceLine.put(Integer.valueOf(5), "Catalog version");

		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLEREFERENCES_FILENAME, articleReferenceLine);
	}

	private void exportCatalogGroupSystemObject(final CatalogGroupSystem cataloggroupsystem)
	{
		//only one catalog group system is allowed in one catalog
		addCatalogGroupSystemComment();

		final Map<Integer, String> catalogGroupSystemLine = new HashMap<Integer, String>();
		catalogGroupSystemLine.put(Integer.valueOf(0), cataloggroupsystem.getID());
		catalogGroupSystemLine.put(Integer.valueOf(1), cataloggroupsystem.getName());
		catalogGroupSystemLine.put(Integer.valueOf(2), cataloggroupsystem.getDescription());
		writeCsvLine(BMECatConstants.BMECat2CSV.GROUPSYSTEM_FILENAME, catalogGroupSystemLine);
		catalogGroupSystemSize = Long.valueOf(catalogGroupSystemSize.longValue() + 1);
	}

	private void addCatalogGroupSystemComment()
	{
		final Map<Integer, String> catalogGroupSystemLine = new HashMap<Integer, String>();
		catalogGroupSystemLine.put(Integer.valueOf(0), "##Group system ID");
		catalogGroupSystemLine.put(Integer.valueOf(1), "Group system name");
		catalogGroupSystemLine.put(Integer.valueOf(2), "Group system description");
		writeCsvLine(BMECatConstants.BMECat2CSV.GROUPSYSTEM_FILENAME, catalogGroupSystemLine);
	}

	private void exportCatalogStructureObject(final CatalogStructure catalogstructure) throws IOException
	{
		if (catalogStructureSize.longValue() == 0)
		{
			addCatalogStructureComment();
		}

		final Map<Integer, String> catalogstructureline = new HashMap<Integer, String>();
		catalogstructureline.put(Integer.valueOf(0), catalogstructure.getID());
		catalogstructureline.put(Integer.valueOf(1), catalogstructure.getName());
		catalogstructureline.put(Integer.valueOf(2), catalogstructure.getDescription());
		catalogstructureline.put(Integer.valueOf(3),
				catalogstructure.getParentID().equals("0") ? "" : catalogstructure.getParentID());
		catalogstructureline.put(Integer.valueOf(4), catalogstructure.getOrder() == null ? "" : catalogstructure.getOrder()
				.toString());

		//mime_info
		String coll = "";
		for (final Map.Entry<String, List<Mime>> e : groupMimes(catalogstructure.getMedias()).entrySet())
		{
			if (e.getValue().size() > 0)
			{
				coll = getMimeNames(e.getValue());
			}
		}
		catalogstructureline.put(Integer.valueOf(5), coll);

		//Collection of Keywords
		String keywords = "";
		for (final String keyword : catalogstructure.getKeywords())
		{
			exportKeyword(keyword);
			keywords += keyword + this.collectionseparator;
		}
		if (keywords.length() > 0)
		{
			keywords = keywords.substring(0, keywords.length() - 1);
		}
		catalogstructureline.put(Integer.valueOf(6), keywords);

		//"Type" is not a supported type in the platform, ignored by ImpEx importing
		catalogstructureline.put(Integer.valueOf(7), catalogstructure.getType());
		getOut(BMECatConstants.BMECat2CSV.CATALOGSTRUCTUE_OBJECT_FILENAME).write(catalogstructureline);
		this.catalogStructureSize = Long.valueOf(catalogStructureSize.longValue() + 1);
	}

	private void addCatalogStructureComment()
	{
		final Map<Integer, String> catalogStructureLine = new HashMap<Integer, String>();
		catalogStructureLine.put(Integer.valueOf(0), "##Group ID(code)");
		catalogStructureLine.put(Integer.valueOf(1), "Group name");
		catalogStructureLine.put(Integer.valueOf(2), "Description");
		catalogStructureLine.put(Integer.valueOf(3), "Parent ID");
		catalogStructureLine.put(Integer.valueOf(4), "Order");
		catalogStructureLine.put(Integer.valueOf(5), "Mime filename");
		catalogStructureLine.put(Integer.valueOf(6), "Keywords");
		catalogStructureLine.put(Integer.valueOf(7), "Type");
		writeCsvLine(BMECatConstants.BMECat2CSV.CATALOGSTRUCTUE_OBJECT_FILENAME, catalogStructureLine);
	}

	private void addClassificationClassComment()
	{
		final Map<Integer, String> classificationClassLine = new HashMap<Integer, String>();
		classificationClassLine.put(Integer.valueOf(0), "##Group ID(code)");
		classificationClassLine.put(Integer.valueOf(1), "Group name");
		classificationClassLine.put(Integer.valueOf(2), "Description");
		classificationClassLine.put(Integer.valueOf(3), "Parent ID");
		classificationClassLine.put(Integer.valueOf(4), "catalogVersion");
		writeCsvLine(BMECatConstants.BMECat2CSV.CLASSIFICATION_CLASS_FILENAME, classificationClassLine);
	}

	private void addClassificationAttributeComment()
	{
		final Map<Integer, String> classificationAttributeLine = new HashMap<Integer, String>();
		classificationAttributeLine.put(Integer.valueOf(0), "##code");
		classificationAttributeLine.put(Integer.valueOf(1), "name");
		classificationAttributeLine.put(Integer.valueOf(2), "system version");
		writeCsvLine(BMECatConstants.BMECat2CSV.CLASSIFICATION_ATTRIBUTE_FILENAME, classificationAttributeLine);
	}

	private void addClassificationAttributeAssignmentComment()
	{
		final Map<Integer, String> classificationAttributeAssignmentLine = new HashMap<Integer, String>();
		classificationAttributeAssignmentLine.put(Integer.valueOf(0), "##classificationClass");
		classificationAttributeAssignmentLine.put(Integer.valueOf(1), "classificationAttribute");
		classificationAttributeAssignmentLine.put(Integer.valueOf(2), "system version");
		writeCsvLine(BMECatConstants.BMECat2CSV.CLASSIFICATION_ATTRIBUTE_ASSIGNMENT_FILENAME, classificationAttributeAssignmentLine);
	}

	private void exportKeyword(final String keyword) throws IOException
	{
		if (!keywordcache.contains(keyword))
		{
			if (keywordSize.longValue() == 0)
			{
				addKeywordComment();
			}
			keywordcache.add(keyword);
			final Map<Integer, String> keywordline = new HashMap<Integer, String>();
			keywordline.put(Integer.valueOf(0), keyword);
			getOut(BMECatConstants.BMECat2CSV.KEYWORD_OBJECT_FILENAME).write(keywordline);
			this.keywordSize = Long.valueOf(keywordSize.longValue() + 1);
		}
	}

	private void addKeywordComment()
	{
		final Map<Integer, String> keywordLine = new HashMap<Integer, String>();
		keywordLine.put(Integer.valueOf(0), "##Keyword");
		writeCsvLine(BMECatConstants.BMECat2CSV.KEYWORD_OBJECT_FILENAME, keywordLine);
	}

	private void exportArticleToCatalogGroupMapObject(final ArticleToCatalogGroupMap articletocataloggroupmap) throws IOException
	{
		if (articletocataloggroupmap.getMode() == BMECatConstants.MODE.NEW)
		{
			exportArticleNewToCatalogGroupMapObject(articletocataloggroupmap);
		}
		else if (articletocataloggroupmap.getMode() == BMECatConstants.MODE.DELETE)
		{
			exportArticleDeleteToCatalogGroupMapObject(articletocataloggroupmap);
		}
		else
		{
			LOG.error("The mode was not imported. It must be either [new] or [delete].");
		}
	}

	private void exportArticleNewToCatalogGroupMapObject(final ArticleToCatalogGroupMap articletocataloggroupmap)
			throws IOException
	{
		if (catalogGroupNewRelationSize.longValue() == 0)
		{
			addCatalogGroupNewRelationComment();
		}

		final Map<Integer, String> articletocataloggroupmapline = new HashMap<Integer, String>();
		articletocataloggroupmapline.put(Integer.valueOf(0), articletocataloggroupmap.getArticleID());
		articletocataloggroupmapline.put(Integer.valueOf(1), articletocataloggroupmap.getCatalogGroupID());
		articletocataloggroupmapline.put(Integer.valueOf(2), articletocataloggroupmap.getOrder() == null ? ""
				: articletocataloggroupmap.getOrder().toString());
		getOut(BMECatConstants.BMECat2CSV.ARTICLE2CATALOGGROUP_NEW_RELATION_FILENAME).write(articletocataloggroupmapline);
		this.catalogGroupNewRelationSize = Long.valueOf(catalogGroupNewRelationSize.longValue() + 1);
	}

	private void addCatalogGroupNewRelationComment()
	{
		final Map<Integer, String> articleNewCatalogGroupLine = new HashMap<Integer, String>();
		articleNewCatalogGroupLine.put(Integer.valueOf(0), "##Article ID");
		articleNewCatalogGroupLine.put(Integer.valueOf(1), "Catalog group ID");
		articleNewCatalogGroupLine.put(Integer.valueOf(2), "Article to catalog group order");
		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE2CATALOGGROUP_NEW_RELATION_FILENAME, articleNewCatalogGroupLine);
	}

	private void exportArticleDeleteToCatalogGroupMapObject(final ArticleToCatalogGroupMap articletocataloggroupmap)
			throws IOException
	{
		if (catalogGroupDeleteRelationSize.longValue() == 0)
		{
			addCatalogGroupDeleteRelationComment();
		}

		final Map<Integer, String> articletocataloggroupmapline = new HashMap<Integer, String>();
		articletocataloggroupmapline.put(Integer.valueOf(0), articletocataloggroupmap.getArticleID());
		articletocataloggroupmapline.put(Integer.valueOf(1), articletocataloggroupmap.getCatalogGroupID());
		articletocataloggroupmapline.put(Integer.valueOf(2), articletocataloggroupmap.getOrder() == null ? ""
				: articletocataloggroupmap.getOrder().toString());
		getOut(BMECatConstants.BMECat2CSV.ARTICLE2CATALOGGROUP_DELETE_RELATION_FILENAME).write(articletocataloggroupmapline);
		this.catalogGroupDeleteRelationSize = Long.valueOf(catalogGroupDeleteRelationSize.longValue() + 1);
	}

	private void addCatalogGroupDeleteRelationComment()
	{
		final Map<Integer, String> articleDeleteCatalogGroupLine = new HashMap<Integer, String>();
		articleDeleteCatalogGroupLine.put(Integer.valueOf(0), "##Article ID");
		articleDeleteCatalogGroupLine.put(Integer.valueOf(1), "Catalog group ID");
		articleDeleteCatalogGroupLine.put(Integer.valueOf(2), "Article to catalog group order");
		writeCsvLine(BMECatConstants.BMECat2CSV.ARTICLE2CATALOGGROUP_DELETE_RELATION_FILENAME, articleDeleteCatalogGroupLine);
	}

}

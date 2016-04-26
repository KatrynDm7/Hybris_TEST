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
import de.hybris.platform.bmecat.jalo.bmecat2csv.BMECat2CSVObjectProcessor;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJob.CronJobResult;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.MediaUtil;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;


public class BMECatImportWithCsvTransformJob extends GeneratedBMECatImportWithCsvTransformJob
{
	private static final Logger LOG = Logger.getLogger(BMECatImportWithCsvTransformJob.class.getName()); //NOPMD

	private BMECat2CSVObjectProcessor proc;
	private BMECatParser bmecatParser;



	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	/**
	 * transform the bmecat.xml to csv files
	 */
	@Override
	protected CronJobResult performCronJob(final CronJob cronJob) throws AbortCronJobException
	{
		final BMECatImportWithCsvTransformCronJob catTransformCronJob = (BMECatImportWithCsvTransformCronJob) cronJob;
		final Media bmecatFile = catTransformCronJob.getJobMedia();
		//LOG.info("bmecat file: " + bmecatFile.getRealFileName());
		InputStream bmecatXml = null;
		try
		{
			bmecatXml = getBmecatXmlFromSource(bmecatFile);
		}
		catch (final IllegalStateException e)
		{
			catTransformCronJob.setParserError(true);
			//set all attribute values in tab2[catalog information] as null
			catTransformCronJob.getBmecatImportWithCsvCronJob().setCatalogDate(null);
			catTransformCronJob.getBmecatImportWithCsvCronJob().setTransactionMode(null);
			catTransformCronJob.getBmecatImportWithCsvCronJob().setCategoryCount(null);
			catTransformCronJob.getBmecatImportWithCsvCronJob().setCategoryAssignmentsCount(null);
			catTransformCronJob.getBmecatImportWithCsvCronJob().setArticleCount(null);
			catTransformCronJob.getBmecatImportWithCsvCronJob().setMimeCount(null);
			catTransformCronJob.getBmecatImportWithCsvCronJob().setKeywordCount(null);
			e.printStackTrace();
			return getCurrentCronJobResult(cronJob, false);
		}

		try
		{
			//parse the bmecat.xml
			proc = new BMECat2CSVObjectProcessor();
			bmecatParser = new BMECatParser(proc);
			bmecatParser.parse(new InputSource(bmecatXml));
			proc.finish();

			//parser is finished, now read the head information
			final Media csvZipMedia = proc.getResultZipMedia();
			final ZipInputStream csvZipStream = new ZipInputStream(csvZipMedia.getDataFromStreamSure());
			ZipEntry entry;
			final Collection<ImpExMedia> externalCsvData = new ArrayList<ImpExMedia>();
			while ((entry = csvZipStream.getNextEntry()) != null)
			{
				if (entry.getName().toLowerCase().indexOf("statistics.properties") != -1)
				{
					setBmecatStatistics((BMECatImportWithCsvTransformCronJob) cronJob, csvZipStream);
				}
				else
				{
					//collect all csv files
					final File temp = File.createTempFile("zipentry", ".csv");
					MediaUtil.copy(csvZipStream, new FileOutputStream(temp));
					final ImpExMedia externalCsvMedia = ImpExManager.getInstance().createImpExMedia(entry.getName());
					externalCsvMedia.setData(new DataInputStream(new FileInputStream(temp)), entry.getName(),
							ImpExConstants.File.MIME_TYPE_CSV);
					externalCsvMedia.setRemoveOnSuccess(true);
					externalCsvMedia.setLinesToSkip(1);
					externalCsvData.add(externalCsvMedia);
					if (!temp.delete())
					{
						System.err.println("Can not delete temporary file " + temp.getAbsolutePath());
					}
				}
			}
			csvZipStream.close();
			//all csv files are available now
			catTransformCronJob.getBmecatImportWithCsvCronJob().setExternalDataCollection(externalCsvData);
		}
		catch (final JaloBusinessException e)
		{
			e.printStackTrace();
			return getCurrentCronJobResult(cronJob, false);
		}
		catch (final AbortCronJobException e)
		{
			e.printStackTrace();
			return getCurrentCronJobResult(cronJob, false);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			return getCurrentCronJobResult(cronJob, false);
		}
		return getCurrentCronJobResult(cronJob, true);
	}

	private void setBmecatStatistics(final BMECatImportWithCsvTransformCronJob tCronJob, final ZipInputStream csvZipStream)
			throws IOException
	{
		final File temp = File.createTempFile("statistics", ".properties");
		MediaUtil.copy(csvZipStream, new FileOutputStream(temp));
		final InputStream statistics = new FileInputStream(temp);
		final Properties props = new Properties();
		props.load(statistics);
		statistics.close();

		//information in the bmecat xml file
		final BMECatImportWithCsvCronJob catCronJob = tCronJob.getBmecatImportWithCsvCronJob();
		try
		{
			//tab2: catalog information
			setCatalogInfo(catCronJob, props);

			//tab3: targets and mode
			setTargetAndMode(catCronJob, props);

			//language setting: try to find the language in the system first
			try
			{
				String catalogLanguage = props.getProperty("catalog.language", "");
				if ("eng".equals(catalogLanguage))
				{
					catalogLanguage = "en";
				}
				else if ("deu".equals(catalogLanguage))
				{
					catalogLanguage = "de";
				}
				catCronJob.setImportLanguage(C2LManager.getInstance().getLanguageByIsoCode(catalogLanguage));
			}
			catch (final JaloItemNotFoundException e)
			{
				//it is all right, no default language was found, a new language will be created 
			}

			//catalog currency setting: try to find the currency in the system first
			try
			{
				catCronJob.setDefaultCurrencyIsoCode(props.getProperty("catalog.currency", ""));
				catCronJob.setDefaultCurrency(C2LManager.getInstance()
						.getCurrencyByIsoCode(props.getProperty("catalog.currency", "")));
			}
			catch (final JaloItemNotFoundException e)
			{
				//it is all right, no default currency was found, a new currency will be created
			}

			//tab4: mapping between bmecat.xml and system
			setMappings(catCronJob, props);
		}
		catch (final NumberFormatException e)
		{
			e.printStackTrace();
		}
	}

	private void setCatalogInfo(final BMECatImportWithCsvCronJob catCronJob, final Properties props) throws NumberFormatException
	{
		if (props.getProperty("catalog.price.generationdate", "").length() != 0)
		{
			catCronJob.setCatalogDate(proc.getBmecatCatalogDate());
		}
		catCronJob.setTransactionMode(EnumerationManager.getInstance().getEnumerationValue(BMECatConstants.TC.TRANSACTIONMODEENUM,
				props.getProperty("catalog.transaction.mode", "")));
		catCronJob.setCategoryCount(Integer.parseInt(props.getProperty("file.categories.size", "0")));
		catCronJob.setCategoryAssignmentsCount(Integer.parseInt(props.getProperty("file.productnew2category.size", "0")));
		catCronJob.setArticleCount(Integer.parseInt(props.getProperty("file.products.size", "0")));
		catCronJob.setMimeCount(Integer.parseInt(props.getProperty("file.medias.size", "0")));
		catCronJob.setKeywordCount(Integer.parseInt(props.getProperty("file.keywords.size", "0")));
		if (props.getProperty("catalog.name", "").trim().length() > 0)
		{
			catCronJob.getBmecatInfo().put("catalog.name", props.getProperty("catalog.name").trim());
		}
	}

	private void setTargetAndMode(final BMECatImportWithCsvCronJob catCronJob, final Properties props)
	{
		catCronJob.setCatalogID(props.getProperty("catalog.id", ""));
		catCronJob.setCatalogVersionName(props.getProperty("catalog.version", ""));
		catCronJob.setImportLanguageIsoCode(props.getProperty("catalog.language", ""));

		//if the import catalog exists in the system, set it as the default catalog
		if (CatalogManager.getInstance().getCatalog(props.getProperty("catalog.id", "")) != null)
		{
			catCronJob.setCatalog(CatalogManager.getInstance().getCatalog(props.getProperty("catalog.id", "")));
			//if the catalog is found, then look for the catalog version
			if (CatalogManager.getInstance().getCatalog(props.getProperty("catalog.id", ""))
					.getCatalogVersion(props.getProperty("catalog.version", "")) != null)
			{
				catCronJob.setCatalogVersion(CatalogManager.getInstance().getCatalog(props.getProperty("catalog.id", ""))
						.getCatalogVersion(props.getProperty("catalog.version", "")));
			}
		}
	}

	private void setMappings(final BMECatImportWithCsvCronJob catCronJob, final Properties props)
	{
		if (props.getProperty("bmecat.currencies") != null)
		{
			final String[] currencies = props.getProperty("bmecat.currencies").split(",");
			final Iterator<String> iterator = Arrays.asList(currencies).iterator();
			final Map<String, Currency> values = new HashMap<String, Currency>();
			while (iterator.hasNext())
			{
				final String currency = iterator.next().trim();
				try
				{
					values.put(currency, C2LManager.getInstance().getCurrencyByIsoCode(currency));
				}
				catch (final JaloItemNotFoundException e)
				{
					//no currency for the product found, and the user can choose one from system 
					values.put(currency, null);
				}
			}
			catCronJob.setAllCurrencyMappings(values);
		}

		if (props.getProperty("bmecat.pricetypes") != null)
		{
			final String[] priceTypes = props.getProperty("bmecat.pricetypes").split(",");
			final Iterator<String> iterator = Arrays.asList(priceTypes).iterator();
			final Map<String, EnumerationValue> values = new HashMap<String, EnumerationValue>();
			while (iterator.hasNext())
			{
				final String priceType = iterator.next().trim();
				try
				{
					values.put(priceType,
							EnumerationManager.getInstance().getEnumerationValue(BMECatConstants.TC.BMECATPRICETYPEENUM, priceType));
				}
				catch (final JaloItemNotFoundException e)
				{
					//it is all right, no price type was found
					values.put(priceType, null);
				}
			}
			catCronJob.setAllPriceTypeMappings(values);
		}

		if (props.getProperty("bmecat.units") != null)
		{
			final String[] units = props.getProperty("bmecat.units").split(",");
			final Iterator<String> iterator = Arrays.asList(units).iterator();
			final Map<String, Unit> values = new HashMap<String, Unit>();
			while (iterator.hasNext())
			{
				final String unit = iterator.next().trim();
				values.put(unit, ProductManager.getInstance().getUnit(unit));
			}
			catCronJob.setAllUnitMappings(values);
		}

		if (props.getProperty("bmecat.taxes") != null)
		{
			final Europe1PriceFactory europe1PriceFactory = (Europe1PriceFactory) getSession().getExtensionManager().getExtension(
					Europe1Constants.EXTENSIONNAME);
			final String[] taxes = props.getProperty("bmecat.taxes").split(",");
			final Iterator<String> iterator = Arrays.asList(taxes).iterator();
			final Map<String, EnumerationValue> values = new HashMap<String, EnumerationValue>();
			while (iterator.hasNext())
			{
				final String tax = iterator.next().trim();
				values.put(tax, europe1PriceFactory.getProductTaxGroup(tax));
			}
			catCronJob.setAllTaxTypeMappings(values);
		}

		if (props.getProperty("bmecat.classificationsystems") != null)
		{
			final String[] systems = props.getProperty("bmecat.classificationsystems").split(",");
			final Iterator<String> iterator = Arrays.asList(systems).iterator();
			final Map<String, ClassificationSystemVersion> values = new HashMap<String, ClassificationSystemVersion>();
			while (iterator.hasNext())
			{
				final String system = iterator.next().trim();
				final int lastMinusPos = system.lastIndexOf('-');
				if (lastMinusPos != -1)
				{
					final String systemCode = system.substring(0, lastMinusPos);
					final String systemVersion = system.substring(lastMinusPos + 1);
					if (CatalogManager.getInstance().getClassificationSystem(systemCode) != null)
					{
						values.put(system,
								CatalogManager.getInstance().getClassificationSystem(systemCode).getSystemVersion(systemVersion));
					}
					else
					{
						values.put(system, null);
					}
				}
				else
				{
					values.put(system, null);
				}
			}
			catCronJob.setAllClassificationMappings(values);
		}
	}

	private CronJobResult getCurrentCronJobResult(final CronJob cronJob, final boolean result)
	{
		cronJob.setCronJobResult(cronJob.getFinishedResult(result));
		return cronJob.getFinishedResult(result);
	}

	private InputStream getBmecatXmlFromSource(final Media bmecatFile)
	{
		try
		{
			if (bmecatFile.getFileName().toLowerCase().endsWith(".xml"))
			{
				return bmecatFile.getDataFromStream();
			}
			else if (bmecatFile.getFileName().toLowerCase().endsWith(".zip"))
			{
				//go through the zip file and look for the first xml file
				try
				{
					final File tempCatalogZipFile = File.createTempFile("bmecatzip", ".temp");
					tempCatalogZipFile.deleteOnExit();
					writeToFile(tempCatalogZipFile, new BufferedInputStream(bmecatFile.getDataFromStream()));
					final ZipFile zipFile = new ZipFile(tempCatalogZipFile);

					final Enumeration enumeration = zipFile.entries();
					while (enumeration.hasMoreElements())
					{
						final ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
						if (zipEntry.getName().toLowerCase().endsWith(".xml"))
						{
							return zipFile.getInputStream(zipEntry);
						}
					}
					//if no xml file was found, throw an exception
					throw new IllegalStateException("no catalog file found in the zip file!");
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				//unsupported file type: neither xml nor zip file
				throw new IllegalStateException("unsupported mime type!!");
			}
		}
		catch (final JaloBusinessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Write an input stream to a file
	 * 
	 * @param file
	 *           file that the content is written to
	 * @param inputStream
	 *           content that will be written
	 */
	protected void writeToFile(final File file, final InputStream inputStream)
	{
		final int byteBuffer = 4096;
		try
		{
			final FileOutputStream fos = new FileOutputStream(file);
			final byte byteArray[] = new byte[byteBuffer];
			int singleByte = inputStream.read(byteArray, 0, byteBuffer);

			while (singleByte != -1)
			{
				fos.write(byteArray, 0, singleByte);
				singleByte = inputStream.read(byteArray, 0, byteBuffer);
			}

			inputStream.close();
			fos.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

}

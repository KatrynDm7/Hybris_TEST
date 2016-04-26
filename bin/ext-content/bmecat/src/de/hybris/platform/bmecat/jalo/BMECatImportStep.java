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
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.ArticleToCatalogGroupMap;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.JobMedia;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.xml.sax.InputSource;


/**
 * Base class for all import step providing essential functionality such as parsing the BMECat file and calling
 * processing methods. A {@link BMECatObjectProcessor} instance processes the xml file and calls
 * {@link #initializeBMECatImport(de.hybris.platform.bmecat.parser.Catalog, BMECatImportCronJob)} before any data object
 * is read and
 * {@link #importBMECatObject(de.hybris.platform.bmecat.parser.Catalog, AbstractValueObject, BMECatImportCronJob)} for
 * each BMECat value object provided by the actual parser.
 * 
 * 
 */
public abstract class BMECatImportStep extends GeneratedBMECatImportStep
{
	public static final String XML_MIME_TYPE = "text/xml";
	public static final String ZIP_MIME_TYPE = "application/zip";

	@Override
	public void remove(final SessionContext ctx) throws ConsistencyCheckException
	{
		super.remove(ctx);
	}

	/**
	 * Gets the BMECat xml file which will be parsed
	 * 
	 * @param jobMedia
	 *           BMECat xml file
	 * @return xml data as input stream
	 */
	protected InputStream getData(final JobMedia jobMedia)
	{
		try
		{
			final String mimetype = jobMedia.getMime();

			//@TODO hot fix for unknown mime type problem has to be fixed
			if (XML_MIME_TYPE.equals(mimetype) || mimetype == null)
			{
				// nothig to do here
				return jobMedia.getDataFromStream();
			}
			else if (ZIP_MIME_TYPE.equals(mimetype))
			{
				File tempCatalogZipFile = null;
				try
				{
					tempCatalogZipFile = File.createTempFile("catlogzip", ".temp");
					tempCatalogZipFile.deleteOnExit();
					writeToFile(tempCatalogZipFile, new BufferedInputStream(jobMedia.getDataFromStream()));

					final ZipFile zipFile = new ZipFile(tempCatalogZipFile);

					final Enumeration enumeration = zipFile.entries();
					while (enumeration.hasMoreElements())
					{
						final ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
						if (zipEntry.getName().toLowerCase().endsWith(".xml"))
						{
							if (isDebugEnabled())
							{
								debug(" found catalog with name '" + zipEntry.getName() + "' in zip file!");
							}
							return zipFile.getInputStream(zipEntry);
						}
					}
					throw new IllegalStateException("no catalogfile found!");
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				throw new IllegalStateException("unsupported mimetype '" + mimetype + "' !!");
			}
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e);
		}
		return null;
	}

	/**
	 * Writes an input stream to a file
	 * 
	 * @param file
	 *           file that the content is written to
	 * @param inputStream
	 *           content that will is written
	 */
	protected void writeToFile(final File file, final InputStream inputStream)
	{
		try
		{
			final FileOutputStream fos = new FileOutputStream(file);
			final byte byteArray[] = new byte[4096];
			//System.out.println("Available = " + inputStream.available());
			int singleByte = inputStream.read(byteArray, 0, 4096);
			//System.out.println("Read in " + singleByte);
			while (singleByte != -1)
			{
				fos.write(byteArray, 0, singleByte);
				singleByte = inputStream.read(byteArray, 0, 4096);
				//System.out.println("Read in " + singleByte);
			}
			// Finish it up
			inputStream.close();
			fos.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Performs {@link de.hybris.platform.bmecat.jalo.BMECatImportCronJob} by creating a BMECat 1.2 Sax parser around the
	 * media data attached to the cronJob.
	 * <p>
	 * First {@link #initializeBMECatImport(de.hybris.platform.bmecat.parser.Catalog, BMECatImportCronJob)}is called to
	 * pass all informations about the BMECat catalog of the imported document. Afterwards
	 * {@link #importBMECatObject(de.hybris.platform.bmecat.parser.Catalog, AbstractValueObject, BMECatImportCronJob)} is
	 * called for any other BMECat data object.
	 */
	@Override
	protected void performStep(final CronJob cronJob) throws AbortCronJobException
	{
		final Language sessionLanguage = getSession().getSessionContext().getLanguage();
		try
		{
			final BMECatImportCronJob bmeCatCronJob = (BMECatImportCronJob) cronJob;
			if (bmeCatCronJob.getImportLanguage() != null)
			{
				getSession().getSessionContext().setLanguage(bmeCatCronJob.getImportLanguage());
			}

			final BMECatParser parser = new BMECatParser(createProcessor(bmeCatCronJob));
			customizeImport(parser);
			parser.parse(new InputSource(getData(bmeCatCronJob.getJobMedia())));
		}
		catch (final AbortCronJobException e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new JaloSystemException(e);
		}
		finally
		{
			finalizeStep(cronJob);
			getSession().getSessionContext().setLanguage(sessionLanguage);
		}
	}

	/**
	 * Methods gives step the possibility to act before the step is finished. (e.g. after the parser has parsed the
	 * catalog file)
	 */
	protected void finalizeStep(final CronJob cronJob)
	{
		// DOCTODO Document reason, why this block is empty
	}

	/**
	 * Creates the {@link BMECatObjectProcessor} instance which should call
	 * {@link #initializeBMECatImport(de.hybris.platform.bmecat.parser.Catalog, BMECatImportCronJob)} before any data
	 * object is read and
	 * {@link #importBMECatObject(de.hybris.platform.bmecat.parser.Catalog, AbstractValueObject, BMECatImportCronJob)}
	 * for each BMECat value object provided by the actual parser.
	 * 
	 * @param cronJob
	 *           the cronJob which is currently executed
	 * @return the new <code>BMECatProcessor instance</code>
	 */
	protected BMECatObjectProcessor createProcessor(final BMECatImportCronJob cronJob)
	{
		return new DefaultBMECatObjectProcessor(cronJob);
	}

	/**
	 * Default implementation of the BMECat object processor.
	 */
	protected class DefaultBMECatObjectProcessor implements BMECatObjectProcessor
	{
		protected final BMECatImportCronJob cronJob;
		protected de.hybris.platform.bmecat.parser.Catalog parsedCatalog;

		DefaultBMECatObjectProcessor(final BMECatImportCronJob cronJob)
		{
			this.cronJob = cronJob;
		}

		public void process(final TagListener listener, final AbstractValueObject obj) throws ParseAbortException
		{
			if (cronJob.isRequestAbortAsPrimitive())
			{
				throw new BMECatParser.AbortRequestedException("abort requested");
			}
			else if (cronJob.isRequestAbortStepAsPrimitive())
			{
				cronJob.setRequestAbortStep(null); // reset abort flag
				if (isWarnEnabled())
				{
					warn("abort step requested");
				}
				throw new ParseFinishedException("abort step requested - aborting " + BMECatImportStep.this.getCode());
			}

			if (obj instanceof de.hybris.platform.bmecat.parser.Catalog)
			{
				if (this.parsedCatalog != null)
				{
					throw new IllegalStateException("catalog is already initialized - got second one " + obj);
				}
				this.parsedCatalog = (de.hybris.platform.bmecat.parser.Catalog) obj;

				initializeBMECatImport(parsedCatalog, cronJob);
			}
			else if (obj instanceof ArticleToCatalogGroupMap)
			{
				importBMECatObject(parsedCatalog, obj, cronJob);
			}
			else if (obj != null)
			{
				importBMECatObject(parsedCatalog, obj, cronJob);
			}
			else
			// obj is null
			{
				throw new IllegalArgumentException("unexpected value object: object is null");
			}
		}
	}

	/**
	 * Overwrite to provide a completion status for this currently processed step.
	 * 
	 * @return [0,100] if {@link #getCompletedCount(BMECatImportCronJob)} and
	 *         {@link #getTotalToComplete(BMECatImportCronJob)} are implemented, -1 otherwise
	 */
	public int getCompletionStatus(final BMECatImportCronJob cronJob)
	{
		final int completed = getCompletedCount(cronJob);
		final int total = getTotalToComplete(cronJob);
		return completed >= 0 && total > 0 ? (completed * 100) / total : -1;
	}

	public int getCompletedCount(final BMECatImportCronJob cronJob)
	{
		return -1;
	}

	public int getTotalToComplete(final BMECatImportCronJob cronJob)
	{
		return -1;
	}

	/**
	 * Called once per step to pass information about the importing catalog before all object will be processed
	 * 
	 * @param catalog
	 *           object which holds information about the BMECat catalog tag
	 * @param cronJob
	 *           context under which this step is
	 * @throws ParseAbortException
	 */
	protected abstract void initializeBMECatImport(de.hybris.platform.bmecat.parser.Catalog catalog, BMECatImportCronJob cronJob)
			throws ParseAbortException;

	/**
	 * Called once for each parsed BMECat value object except the {@link de.hybris.platform.bmecat.parser.Catalog}
	 * itself, which is passed to
	 * {@link #initializeBMECatImport(de.hybris.platform.bmecat.parser.Catalog, BMECatImportCronJob)}instead.
	 * 
	 * @param catalog
	 *           the parsed catalog data
	 * @param object
	 *           the parsed value object
	 * @param cronJob
	 *           the cronJob which executes the current import
	 */
	protected abstract void importBMECatObject(de.hybris.platform.bmecat.parser.Catalog catalog, AbstractValueObject object,
			BMECatImportCronJob cronJob) throws ParseAbortException;

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
		return (cronJob instanceof BMECatImportCronJob) && ((BMECatImportCronJob) cronJob).getJobMedia() != null;
	}

	protected BMECatManager getBMECatManager()
	{
		return BMECatManager.getInstance();
	}

	protected CatalogManager getCatalogManager()
	{
		return CatalogManager.getInstance();
	}

	/**
	 * Returns a product for a specified catalag version and id
	 * 
	 * @param catalogVersion
	 * @param articleID
	 * @return the Product
	 */
	protected Product getProduct(final CatalogVersion catalogVersion, final String articleID)
	{
		final Collection products = catalogVersion.getProducts(articleID);
		if (products.isEmpty())
		{
			return null;
		}
		else if (products.size() == 1)
		{
			return (Product) products.iterator().next();
		}
		else
		{
			//TODO: How to handle inconsistency?!?
			if (isErrorEnabled())
			{
				error("More than one product found for article ID '" + articleID + "' (found " + products + ")");
			}
			return (Product) products.iterator().next();
		}
	}

	/**
	 * Gets or creates {@link Keyword}s for every keyword value that is passed as parameter
	 * 
	 * @param cronJob
	 *           context of search/creation which provides catalogversion and keyword type
	 * @param keywordValues
	 *           collection of keyword names (Strings)
	 * @return Collections of {@link Keyword}s
	 */
	protected Collection getOrCreateKeywords(final BMECatImportCronJob cronJob, final Collection keywordValues)
	{
		Collection keywords = null;
		if (keywordValues != null)
		{
			final Language lang = getSession().getSessionContext().getLanguage();
			final String iso = lang.getIsoCode();
			keywords = new ArrayList();
			for (final Iterator it = keywordValues.iterator(); it.hasNext();)
			{
				final String keywordValue = (String) it.next();
				final CatalogVersion catalogVersion = cronJob.getCatalogVersion();
				Keyword keyword = getCatalogManager().getKeyword(cronJob.getKeywordType().getCode(), catalogVersion, keywordValue);
				if (keyword == null)
				{
					keyword = createKeyword(cronJob, keywordValue, lang);
					addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_KEYWORD, keyword, " - Keyword '" + keywordValue + "' ("
							+ iso + ") created!");
					//log.info( " - Keyword '" + keywordValue + "' (" + iso + ") created!" );
				}
				keywords.add(keyword);
			}
		}
		return keywords;
	}

	/**
	 * Creates an new <code>Keyword</code> for the given <code>CatalogVersion and keyword value.
	 * 
	 * @param cronJob
	 *           The <code>BMECatImportCronJob</code> in which this step is executed
	 * @param keyword
	 *           The keyword value.
	 * @return The new <code>Keyword</code> for the given CatalogVersion and keyword value.
	 */
	public Keyword createKeyword(final BMECatImportCronJob cronJob, final String keyword, final Language lang)
	{
		try
		{
			final Map values = new ItemAttributeMap();
			values.put(Keyword.KEYWORD, keyword);
			values.put(Keyword.LANGUAGE, lang);
			if (cronJob.getCatalogVersion() != null)
			{
				values.put(Keyword.CATALOGVERSION, cronJob.getCatalogVersion());
			}
			return (de.hybris.platform.catalog.jalo.Keyword) cronJob.getKeywordType().newInstance(getSession().getSessionContext(),
					values);
		}
		catch (final Exception e)
		{
			throw new JaloSystemException(e, "Could not create new Keyword instance. ErrorMessage: " + e.getMessage(), 0);
		}
	}

	/**
	 * Removes all keywords created by this step for the specified cronJob.
	 * 
	 * @param cronJob
	 *           context of step execution
	 */
	@Override
	protected void undoStep(final CronJob cronJob)
	{
		int index = 0;
		/*
		 * fetch keyword in 100 - entry - ranges
		 */
		for (Collection changes = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_KEYWORD, 0, 100); changes != null
				&& !changes.isEmpty(); changes = getChanges(cronJob, BMECatConstants.ChangeTypes.CREATE_KEYWORD, 0, 100))
		{
			index += changes.size();
			for (final Iterator it = changes.iterator(); it.hasNext();)
			{
				try
				{
					final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
					final Keyword keyword = (Keyword) changeDescriptor.getChangedItem();
					if (keyword != null && keyword.isAlive())
					{
						keyword.remove();
					}
					changeDescriptor.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		if (index > 0 && isInfoEnabled())
		{
			info("removed " + index + " keywords");
		}
	}

	/**
	 * Overwrite this method to add own <CODE>TagListener</CODE> to <CODE>HEADER</CODE>, <CODE>ARTICLE</CODE> or
	 * <CODE>CATALOG_STRUCTURE</CODE>.
	 * 
	 * @param parser
	 *           bmecat parser which process the tags
	 */
	protected void customizeImport(final BMECatParser parser)
	{
		// DOCTODO Document reason, why this block is empty
	}

	/**
	 * Identifies bmecat import steps as abortable by default
	 * 
	 * @see de.hybris.platform.cronjob.jalo.Step#isAbortable()
	 */
	@Override
	public boolean isAbortable()
	{
		return true;
	}
}

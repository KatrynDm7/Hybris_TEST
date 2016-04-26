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

import de.hybris.platform.bmecat.xmlwriter.BMECatWriter;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cronjob.constants.CronJobConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJob.CronJobResult;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;


/**
 * The <code>Job</code> for BMECat export.
 * 
 * 
 */
public class BMECatExportJob extends GeneratedBMECatExportJob
{
	private static final Logger LOG = Logger.getLogger(BMECatExportJob.class.getName());

	/**
	 * Calls {@link #exportBmeCat(CronJob )}.
	 * 
	 * @see de.hybris.platform.cronjob.jalo.Job#perform(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	public CronJobResult performCronJob(final CronJob cronJob)
	{
		final Language sessionLanguage = getSession().getSessionContext().getLanguage();
		try
		{
			return cronJob.getFinishedResult(exportBmeCat(cronJob));
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			return cronJob.getAbortResult();
		}
		finally
		{
			getSession().getSessionContext().setLanguage(sessionLanguage);
		}
	}

	/**
	 * @see de.hybris.platform.cronjob.jalo.Job#isPerformable(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	public boolean isPerformable(final CronJob cronJob)
	{
		if (super.isPerformable(cronJob))
		{
			return cronJob instanceof BMECatExportCronJob;
		}
		return false;
	}

	/**
	 * @see de.hybris.platform.cronjob.jalo.Job#canUndo(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	protected boolean canUndo(final CronJob cronJob)
	{
		return true;
	}

	/**
	 * @see de.hybris.platform.cronjob.jalo.Job#undo(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	public CronJobResult undoCronJob(final CronJob cronJob)
	{
		final BMECatJobMedia media = (BMECatJobMedia) ((BMECatMediaProcessCronJob) cronJob).getJobMedia();
		if (media != null)
		{
			try
			{
				media.remove();
			}
			catch (final ConsistencyCheckException e)
			{
				throw new JaloSystemException(e);
			}
		}
		return cronJob.getUndoFinishedResult(true);
	}

	protected String createFileNameBase(final BMECatExportCronJob jobDetail)
	{
		String catalog = jobDetail.getExportCatalogVersion().getCatalog().getName();
		// fallback to catalog ID if no name is provided
		if (catalog == null)
		{
			catalog = jobDetail.getExportCatalogVersion().getCatalog().getId();
		}

		final String version = jobDetail.getExportCatalogVersion().getVersion();

		String ret = catalog + "_" + version + "_" + new SimpleDateFormat("yyyyMMdd_hhmmss").format(jobDetail.getCreationTime());
		ret = ret.trim().replace(' ', '_');
		return ret;
	}

	protected BMECatJobMedia createBmeCatExportMedia(final String fileName, final BMECatExportCronJob jobDetail)
	{
		final ComposedType batchMediaType = getSession().getTypeManager().getComposedType(BMECatJobMedia.class);

		final BMECatJobMedia target = (BMECatJobMedia) getSession().getMediaManager().createMedia(fileName, batchMediaType);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("created new result media '" + target.getCode() + "'");
		}

		return target;
	}

	/**
	 * Overwritten to redirect any logging inside the bmecat writer to this job - this way it will be written in database
	 * and/or LOG file.
	 */
	protected class MyBMECatWriter extends BMECatWriter
	{

		public MyBMECatWriter(final CatalogVersion catVersion, final EnumerationValue transactionMode,
				final Collection classificationSystemVersions, final Language exportLang, final Map hybris2bmecatMediaMap,
				final Media exportedMedias, final Map userPriceGroups2BMECatPriceTypesMap, final boolean udpNet, final Date refDate,
				final Customer refCustomer, final Currency exportCurrency, final boolean ignoreErrors,
				final boolean suppressEmptyCategories, final boolean suppressProductsWithoutPrice,
				final String classificationNumberFormat)
		{
			super(catVersion, transactionMode, classificationSystemVersions, exportLang, hybris2bmecatMediaMap, exportedMedias,
					userPriceGroups2BMECatPriceTypesMap, udpNet, refDate, refCustomer, exportCurrency, ignoreErrors,
					suppressEmptyCategories, suppressProductsWithoutPrice, classificationNumberFormat);
		}
	}

	/**
	 * Creates a new {@link MyBMECatWriter} instance for each exported language.
	 * 
	 * @param exportCronJob
	 *           the exporting cronjob
	 * @param exportLang
	 *           the language to export a catalog in
	 */
	protected BMECatWriter createWriter(final BMECatExportCronJob exportCronJob, final Language exportLang)
	{
		return new MyBMECatWriter(exportCronJob.getExportCatalogVersion(), exportCronJob.getTransactionMode(),
				exportCronJob.getClassificationSystemVersions(), exportLang, exportCronJob.getAllHybris2BMECatMimePurposeMapping(),
				exportCronJob.getExportedMedias(), exportCronJob.getAllPriceTypeMapping(), exportCronJob.isUdpNetAsPrimitive(),
				exportCronJob.getReferenceDate(), exportCronJob.getReferenceCustomer(), exportCronJob.getSessionCurrency(),
				// ignore all errors if error mode != FAIL
				exportCronJob.getErrorMode() != null
						&& !CronJobConstants.Enumerations.ErrorMode.FAIL.equals(exportCronJob.getErrorMode().getCode()),
				exportCronJob.isSuppressEmptyCategoriesAsPrimitive(), exportCronJob.isSuppressProductsWithoutPriceAsPrimitive(),
				exportCronJob.getClassificationSystemNumberFormat());
	}

	/**
	 * Provides the functionality to start a BMECatExport for each selected export language. Stores each generated xml
	 * file in a zip file.
	 * 
	 * @param cronJob
	 *           the performing <code>CronJob</code>
	 * 
	 * @return true if all exports have been successful, false otherwise
	 * 
	 * @throws JaloBusinessException
	 * @throws IOException
	 */
	protected boolean exportBmeCat(final CronJob cronJob) throws JaloBusinessException, IOException
	{
		ZipOutputStream zos = null;
		Writer writer = null;
		boolean errorsOccured = false;
		try
		{
			final BMECatExportCronJob exportCronJob = (BMECatExportCronJob) cronJob;

			final String fileNameBase = createFileNameBase(exportCronJob);

			final File tempFile = File.createTempFile(fileNameBase, ".zip");
			if (!tempFile.canWrite())
			{
				throw new JaloSystemException("cannot write to " + tempFile, 0);
			}

			zos = new ZipOutputStream(new FileOutputStream(tempFile));
			writer = new BufferedWriter(new OutputStreamWriter(zos, "UTF-8"));

			//final Media mex = prepareMedia();
			final BMECatJobMedia mex = prepareMedia(fileNameBase);
			exportCronJob.setExportedMedias(mex);

			int languageCount = 0;

			for (final Iterator it = exportCronJob.getExportLanguages().iterator(); it.hasNext(); languageCount++)
			{
				final Language exportLang = (Language) it.next();
				zos.putNextEntry(new ZipEntry(fileNameBase + "_" + exportLang.getIsoCode() + ".xml"));

				final BMECatWriter bmeCatWriter = createWriter(exportCronJob, exportLang);
				customizeExport(exportCronJob, bmeCatWriter);

				exportCronJob.registerWriterForInfo(bmeCatWriter, languageCount);

				bmeCatWriter.write(writer);

				exportCronJob.unsetWriterForInfo(bmeCatWriter);

				errorsOccured |= bmeCatWriter.errorsOccured();

				zos.closeEntry();
			}

			writer.close();
			writer = null;
			zos.close();
			zos = null;
			final BMECatJobMedia target = createBmeCatExportMedia(fileNameBase + ".zip", exportCronJob);
			target.setFile(tempFile);
			tempFile.delete();
			exportCronJob.setJobMedia(target);

			return !errorsOccured;
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (final IOException e)
				{
					// DOCTODO Document reason, why this block is empty
				}
			}
			if (zos != null)
			{
				try
				{
					zos.close();
				}
				catch (final IOException e)
				{
					// DOCTODO Document reason, why this block is empty
				}
			}
		}
	}

	private BMECatJobMedia prepareMedia(final String fileNameBase)
	{
		try
		{
			final ComposedType mediaType = getSession().getTypeManager().getComposedType(BMECatJobMedia.class);
			final BMECatJobMedia target = (BMECatJobMedia) getSession().getMediaManager().createMedia(fileNameBase + "_medias.zip",
					mediaType);
			target.setMime("application/zip");
			target.setRealFileName(fileNameBase + "_medias.zip");
			return target;
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e);
		}
	}

	/**
	 * Overwrite this method to customize your BMECat export.
	 * 
	 * @param bmeCatWriter
	 */
	protected void customizeExport(final BMECatExportCronJob cronJob, final BMECatWriter bmeCatWriter)
	{
		// DOCTODO Document reason, why this block is empty
	}
}

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
package de.hybris.platform.classificationsystems.jalo;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationImportCronJob;
import de.hybris.platform.catalog.jalo.classification.ClassificationImportJob;
import de.hybris.platform.classificationsystems.constants.ClassificationsystemsConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.cronjob.ImpExImportCronJob;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.Utilities;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class ClassificationsystemsManager extends GeneratedClassificationsystemsManager
{
	private static Logger LOG = Logger.getLogger(ClassificationsystemsManager.class.getName());

	public static final String DEFAULT_CLASS_IMPORT_JOB = "ClassificationImportJob";

	public static final ClassificationsystemsManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ClassificationsystemsManager) em.getExtension(ClassificationsystemsConstants.EXTENSIONNAME);
	}

	/**
	 * returns or - if not found with the default jobcode <code>ClassificationImportJob</code> - creates a
	 * {@link ClassificationImportJob}
	 * 
	 * @return the {@link ClassificationImportJob}
	 */
	public ClassificationImportJob getOrCreateDefaultClassificationImportJob()
	{
		ClassificationImportJob classificationIJ = (ClassificationImportJob) CronJobManager.getInstance().getJob(
				DEFAULT_CLASS_IMPORT_JOB);
		if (classificationIJ == null)
		{
			classificationIJ = createClassificationImportJob(Collections.singletonMap(Job.CODE, DEFAULT_CLASS_IMPORT_JOB));
		}
		return classificationIJ;
	}


	private ImpExMedia getBatchMedia(final String fileName, final String fileExt) throws IOException, JaloBusinessException
	{
		final String adjustedFileName = fileName.indexOf('/') == 0 ? fileName : ("/" + fileName);
		final InputStream istr = CatalogManager.class.getResourceAsStream(adjustedFileName);

		if (istr == null)
		{
			throw new IllegalArgumentException("Missing classification data file: " + adjustedFileName
					+ "!\nPlease be sure that the 'classificationsystems' extension is installed.");
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Loading file: " + fileName);
			}
			return createBatchMedia(fileName, fileExt, istr);
		}
	}

	private ImpExMedia createBatchMedia(final String fileName, final String mime, final InputStream istr)
	{
		ImpExMedia batchMedia = null;
		try
		{
			final Map values = new HashMap();
			values.put(ImpExMedia.CODE, fileName);
			values.put(ImpExMedia.REMOVABLE, Boolean.TRUE);
			values.put(ImpExMedia.ENCODING, Utilities.resolveEncoding("windows-1252"));
			batchMedia = ImpExManager.getInstance().createImpExMedia(values);
			if (istr == null)
			{
				batchMedia.setMime(mime);
				batchMedia.setRealFileName(fileName);
			}
			else
			{
				batchMedia.setData(new DataInputStream(istr), fileName, mime);
			}
			return batchMedia;
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			if (cause == null)
			{
				throw new JaloSystemException(e);
			}
			else
			{
				if (cause instanceof RuntimeException)
				{
					throw (RuntimeException) cause;
				}
				else
				{
					throw new JaloSystemException(cause);
				}
			}
		}
		catch (final JaloAbstractTypeException e)
		{
			throw new JaloSystemException(e);
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e);
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new JaloSystemException(e);
		}
	}



	public ClassificationImportCronJob createClassificationImportCronJob(final String cronjobCode) throws Exception
	{
		// EClass ///////////////////////////////////

		// 4.0 DE
		if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_4_0_DE))
		{
			return createEClass_4_0_DE_Import(null);
		}
		// 4.1 DE, EN, FR
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_4_1_DE))
		{
			return createEClass_4_1_DE_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_4_1_EN))
		{
			return createEClass_4_1_EN_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_4_1_FR)
				&& CatalogManager.getInstance().getLanguageIfExists("fr") != null)
		{
			return createEClass_4_1_FR_Import(null);
		}
		// 5.0 SP1 DE, EN, FR
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_5_0_DE))
		{
			return createEClass_5_0_SP1_DE_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_5_0_EN))
		{
			return createEClass_5_0_SP1_EN_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_5_0_FR)
				&& CatalogManager.getInstance().getLanguageIfExists("fr") != null)
		{
			return createEClass_5_0_SP1_FR_Import(null);
		}
		// 5.1  DE, EN, FR
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_5_1_DE))
		{
			return createEClass_5_1_DE_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_5_1_EN))
		{
			return createEClass_5_1_EN_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.ECLASS_5_1_FR)
				&& CatalogManager.getInstance().getLanguageIfExists("fr") != null)
		{
			return createEClass_5_1_FR_Import(null);
		}
		// ETIM ///////////////////////////////////
		else if (cronjobCode.equals(ClassificationsystemsConstants.ETIM_2_0_DE))
		{
			return createETIM_2_0_DE_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.ETIM_3_0_DE))
		{
			return createETIM_3_0_DE_Import(null);
		}
		// ProfiClass ///////////////////////////////////
		else if (cronjobCode.equals(ClassificationsystemsConstants.PROFICLASS_3_0_DE))
		{
			return createProfiClass_3_0_DE_Import(null);
		}
		else if (cronjobCode.equals(ClassificationsystemsConstants.PROFICLASS_4_0_DE))
		{
			return createProfiClass_4_0_DE_Import(null);
		}
		// UNSPSC ///////////////////////////////////
		else if (cronjobCode.equals(ClassificationsystemsConstants.UNSPSC_5_DE_EN))
		{
			return createUnspscImportItems(null);
		}
		else
		{
			throw new JaloInvalidParameterException("Unable to create classification cronjob!", 0);
		}
	}

	private InputStream getZipEntryInputStream(final ZipFile zipfile, final String entryName) throws IOException
	{
		final ZipEntry zentry = zipfile.getEntry(entryName);
		if (zentry == null)
		{
			LOG.error("Missing zip entry '" + entryName + "' in file " + zipfile.getName());
			return null;
		}
		else
		{
			return zipfile.getInputStream(zentry);
		}
	}

	private ClassificationImportCronJob createUnspscImportItems(@SuppressWarnings("unused") final JspContext jspc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/UNSPSC_v5_0301_German.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "UNSPSC");
			attributes.put(ClassificationImportCronJob.VERSION, "5.0301");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.UNSPSC_5_DE_EN);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia unspsc_v5_0301GermanZip = getBatchMedia("/catalog/UNSPSC_v5_0301_German.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("UNSPSC_v5_0301_German", "zipped");
			MediaUtil.copy(unspsc_v5_0301GermanZip.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{ createBatchMedia("UNSPSC_v5_0301_German.csv", ClassificationsystemsConstants.MIME_CSV,
					getZipEntryInputStream(zipfile, "UNSPSC_v5_0301_German.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createProfiClass_3_0_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/proficlass3_0.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "proficlass");
			attributes.put(ClassificationImportCronJob.VERSION, "3.0");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.PROFICLASS_3_0_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia zipmedia = getBatchMedia("/catalog/proficlass_3_0_csv.zipped", ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("proficlass_3_0_csv", "zipped");
			MediaUtil.copy(zipmedia.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("Klassen.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Klassen.csv")),
					createBatchMedia("Schlagworte.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Schlagworte.csv")),
					createBatchMedia("KlassenSchlagworte_rel.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "KlassenSchlagworte_rel.csv")),
					createBatchMedia("Merkmale.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Merkmale.csv")),
					createBatchMedia("Werte.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Werte.csv")),
					createBatchMedia("Einheiten.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Einheiten.csv")),
					createBatchMedia("KlassenMerkmale_rel.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "KlassenMerkmale_rel.csv")),
					createBatchMedia("KlassenMerkmaleWerte_rel.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "KlassenMerkmaleWerte_rel.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createProfiClass_4_0_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/proficlass4_0.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "proficlass");
			attributes.put(ClassificationImportCronJob.VERSION, "4.0");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.PROFICLASS_4_0_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia zipmedia = getBatchMedia("/catalog/proficlass_4_0_csv.zipped", ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("proficlass_4_0_csv", "zipped");
			MediaUtil.copy(zipmedia.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("Klassen.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Klassen.csv")),
					createBatchMedia("Schlagworte.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Schlagworte.csv")),
					createBatchMedia("KlassenSchlagworte_rel.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "KlassenSchlagworte_rel.csv")),
					createBatchMedia("Merkmale.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Merkmale.csv")),
					createBatchMedia("Werte.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Werte.csv")),
					createBatchMedia("Einheiten.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "Einheiten.csv")),
					createBatchMedia("KlassenMerkmale_rel.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "KlassenMerkmale_rel.csv")),
					createBatchMedia("KlassenMerkmaleWerte_rel.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "KlassenMerkmaleWerte_rel.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_4_1_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass4_1_80c.impex.csv", ClassificationsystemsConstants.MIME_CSV);
		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "4.1.80c");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_4_1_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eclass4_1_80c_de.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass4_1_80c_de", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eclass4_1_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_80c_de.csv")),
					createBatchMedia("eclass4_1_mlmm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mlmm_de.csv")),
					createBatchMedia("eclass4_1_mm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mm_de.csv")),
					createBatchMedia("eclass4_1_mw.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mw_de.csv")),
					createBatchMedia("eclass4_1_sw_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_sw_80c_de.csv")),
					createBatchMedia("eclass4_1_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_we_de.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_4_1_EN_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass4_1_80c.impex.csv", ClassificationsystemsConstants.MIME_CSV);
		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("en"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("en"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "4.1.80c");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_4_1_EN);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eclass4_1_80c_en.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass4_1_80c_en", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eclass4_1_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_80c_en.csv")),
					createBatchMedia("eclass4_1_mlmm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mlmm_en.csv")),
					createBatchMedia("eclass4_1_mm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mm_en.csv")),
					createBatchMedia("eclass4_1_mw.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mw_en.csv")),
					createBatchMedia("eclass4_1_sw_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_sw_80c_en.csv")),
					createBatchMedia("eclass4_1_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_we_en.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_4_1_FR_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass4_1_80c.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("fr"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("fr"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "4.1.80c");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_4_1_FR);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eclass4_1_80c_fr.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass4_1_80c_fr", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eclass4_1_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_80c_fr.csv")),
					createBatchMedia("eclass4_1_mlmm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mlmm_fr.csv")),
					createBatchMedia("eclass4_1_mm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mm_fr.csv")),
					createBatchMedia("eclass4_1_mw.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_mw_fr.csv")),
					createBatchMedia("eclass4_1_sw_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_sw_80c_fr.csv")),
					createBatchMedia("eclass4_1_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_1_we_fr.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_4_0_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass4_0_80c.impex.csv", ClassificationsystemsConstants.MIME_CSV);
		if (bm != null)
		{

			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "4.0.80c");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_4_0_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eclass4_0_80c_de.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass4_0_80c_de", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eclass4_0_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_0_80c_de.csv")),
					createBatchMedia("eclass4_0_mlmm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_0_mlmm_de.csv")),
					createBatchMedia("eclass4_0_mm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_0_mm_de.csv")),
					createBatchMedia("eclass4_0_mw.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_0_mw_de.csv")),
					createBatchMedia("eclass4_0_sw_80c.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_0_sw_80c_de.csv")),
					createBatchMedia("eclass4_0_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eclass4_0_we_de.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_5_0_SP1_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass5_0_sp1.impex.csv", ClassificationsystemsConstants.MIME_CSV);
		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "5.0 SP1");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_5_0_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eClass5_0_de_SP1.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eClass5_0_de_SP1", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eClass5_0_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_de_SP1.csv")),
					createBatchMedia("eClass5_0_ml_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_ml_de_SP1.csv")),
					createBatchMedia("eClass5_0_mm_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_mm_de_SP1.csv")),
					createBatchMedia("eClass5_0_mm_we_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_mm_we_de_SP1.csv")),
					createBatchMedia("eClass5_0_sw_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_sw_de_SP1.csv")),
					createBatchMedia("eClass5_0_we_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_we_de_SP1.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_5_0_SP1_EN_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass5_0_sp1.impex.csv", ClassificationsystemsConstants.MIME_CSV);
		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("en"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("en"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "5.0 SP1");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_5_0_EN);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eClass5_0_en_SP1.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass5_0_en_SP1", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eClass5_0_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_en_SP1.csv")),
					createBatchMedia("eClass5_0_ml_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_ml_en_SP1.csv")),
					createBatchMedia("eClass5_0_mm_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_mm_en_SP1.csv")),
					createBatchMedia("eClass5_0_mm_we_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_mm_we_en_SP1.csv")),
					createBatchMedia("eClass5_0_sw_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_sw_en_SP1.csv")),
					createBatchMedia("eClass5_0_we_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_we_en_SP1.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_5_0_SP1_FR_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass5_0_sp1.impex.csv", ClassificationsystemsConstants.MIME_CSV);
		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("fr"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("fr"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "5.0 SP1");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_5_0_FR);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eClass5_0_fr_SP1.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass5_0_fr_SP1", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eClass5_0_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_fr_SP1.csv")),
					createBatchMedia("eClass5_0_ml_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_ml_fr_SP1.csv")),
					createBatchMedia("eClass5_0_mm_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_mm_fr_SP1.csv")),
					createBatchMedia("eClass5_0_mm_we_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_mm_we_fr_SP1.csv")),
					createBatchMedia("eClass5_0_sw_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_sw_fr_SP1.csv")),
					createBatchMedia("eClass5_0_we_SP1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_0_we_fr_SP1.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_5_1_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass5_1.impex.csv", ClassificationsystemsConstants.MIME_CSV);
		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "5.1");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_5_1_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eClass5_1_de.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass5_1_de", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eClass5_1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_de.csv")),
					createBatchMedia("eClass5_1_ml.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_ml_de.csv")),
					createBatchMedia("eClass5_1_mm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_mm_de.csv")),
					createBatchMedia("eClass5_1_mm_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_mm_we_de.csv")),
					createBatchMedia("eClass5_1_sw.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_sw_de.csv")),
					createBatchMedia("eClass5_1_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_we_de.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_5_1_EN_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass5_1.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("en"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("en"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "5.1");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_5_1_EN);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eClass5_1_en.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass5_1_en", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eClass5_1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_en.csv")),
					createBatchMedia("eClass5_1_ml.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_ml_en.csv")),
					createBatchMedia("eClass5_1_mm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_mm_en.csv")),
					createBatchMedia("eClass5_1_mm_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_mm_we_en.csv")),
					createBatchMedia("eClass5_1_sw.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_sw_en.csv")),
					createBatchMedia("eClass5_1_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_we_en.csv")), }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createEClass_5_1_FR_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/eclass5_1.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("fr"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("fr"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "eclass");
			attributes.put(ClassificationImportCronJob.VERSION, "5.1");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ECLASS_5_1_FR);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia eclass4_1_80c_zipped = getBatchMedia("/catalog/eClass5_1_fr.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("eclass5_1_fr", "zipped");
			MediaUtil.copy(eclass4_1_80c_zipped.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("eClass5_1.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_fr.csv")),
					createBatchMedia("eClass5_1_ml.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_ml_fr.csv")),
					createBatchMedia("eClass5_1_mm.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_mm_fr.csv")),
					createBatchMedia("eClass5_1_mm_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_mm_we_fr.csv")),
					createBatchMedia("eClass5_1_sw.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_sw_fr.csv")),
					createBatchMedia("eClass5_1_we.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "eClass5_1_we_fr.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createETIM_3_0_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/etim3_0.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "etim");
			attributes.put(ClassificationImportCronJob.VERSION, "3.0");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ETIM_3_0_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia zipmedia = getBatchMedia("/catalog/ETIM30csv.zipped", ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("ETIM30csv", "zipped");
			MediaUtil.copy(zipmedia.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("ETIMARTCLASS.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTCLASS.csv")),
					createBatchMedia("ETIMARTCLASSFEATUREMAP.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTCLASSFEATUREMAP.csv")),
					createBatchMedia("ETIMARTCLASSFEATUREVALUEMAP.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTCLASSFEATUREVALUEMAP.csv")),
					createBatchMedia("ETIMARTGROUP.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTGROUP.csv")),
					createBatchMedia("ETIMFEATURE.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMFEATURE.csv")),
					createBatchMedia("ETIMSYNONYM_DE.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMSYNONYM_DE.csv")),
					createBatchMedia("ETIMUNIT.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMUNIT.csv")),
					createBatchMedia("ETIMVALUE.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMVALUE.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

	private ClassificationImportCronJob createETIM_2_0_DE_Import(@SuppressWarnings("unused") final JspContext jscpc)
			throws Exception
	{
		ClassificationImportCronJob cicj = null;
		final ImpExMedia bm = getBatchMedia("/catalog/etim2_0.impex.csv", ClassificationsystemsConstants.MIME_CSV);

		if (bm != null)
		{
			final Map attributes = new HashMap();
			attributes.put(CronJob.ACTIVE, Boolean.TRUE);
			attributes.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			attributes.put(CronJob.LOGTODATABASE, Boolean.TRUE);
			attributes.put(CronJob.LOGTOFILE, Boolean.TRUE);
			attributes.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			attributes.put(ImpExImportCronJob.ENABLECODEEXECUTION, Boolean.TRUE);
			attributes.put(ImpExImportCronJob.SESSIONUSER, UserManager.getInstance().getAdminEmployee());
			attributes.put(ImpExImportCronJob.SESSIONLANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.LANGUAGE, CatalogManager.getInstance().getLanguageIfExists("de"));
			attributes.put(ClassificationImportCronJob.CLASSIFICATIONSYSTEM, "etim");
			attributes.put(ClassificationImportCronJob.VERSION, "2.0");
			attributes.put(ImpExImportCronJob.CODE, ClassificationsystemsConstants.ETIM_2_0_DE);

			cicj = createClassificationImportCronJob(attributes);

			cicj.setJobMedia(bm);

			final ImpExMedia zipmedia = getBatchMedia("/catalog/ETIM_Datenmodell_2_0_csv.zipped",
					ClassificationsystemsConstants.MIME_ZIP);
			final File tmp = File.createTempFile("ETIM_Datenmodell_2_0_csv", "zipped");
			MediaUtil.copy(zipmedia.getDataFromStream(), new FileOutputStream(tmp));
			final ZipFile zipfile = new ZipFile(tmp);

			cicj.setExternalDataCollection(Arrays.asList(new ImpExMedia[]
			{
					createBatchMedia("ETIMARTCLASS.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTCLASS.csv")),
					createBatchMedia("ETIMARTCLASSFEATUREMAP.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTCLASSFEATUREMAP.csv")),
					createBatchMedia("ETIMARTCLASSFEATUREVALUEMAP.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTCLASSFEATUREVALUEMAP.csv")),
					createBatchMedia("ETIMARTGROUP.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMARTGROUP.csv")),
					createBatchMedia("ETIMFEATURE.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMFEATURE.csv")),
					createBatchMedia("ETIMSYNONYM_DE.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMSYNONYM_DE.csv")),
					createBatchMedia("ETIMUNIT.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMUNIT.csv")),
					createBatchMedia("ETIMVALUE.csv", ClassificationsystemsConstants.MIME_CSV,
							getZipEntryInputStream(zipfile, "ETIMVALUE.csv")) }));
			zipfile.close();
			tmp.delete();
		}
		return cicj;
	}

}

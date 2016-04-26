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
package de.hybris.platform.bmecat.hmc;

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.jalo.BMECatImpExScriptMedia;
import de.hybris.platform.bmecat.jalo.BMECatImportWithCsvCronJob;
import de.hybris.platform.bmecat.jalo.BMECatImportWithCsvJob;
import de.hybris.platform.bmecat.jalo.BMECatImportWithCsvTransformCronJob;
import de.hybris.platform.bmecat.jalo.BMECatJobMedia;
import de.hybris.platform.bmecat.jalo.BMECatManager;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.cronjob.jalo.JobMedia;
import de.hybris.platform.hmc.jalo.ValidationException;
import de.hybris.platform.hmc.jalo.VetoException;
import de.hybris.platform.hmc.jalo.WizardEditorContext;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.util.localization.Localization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class BMECatImportWithCsvWizard extends GeneratedBMECatImportWithCsvWizard
{
	private static final Logger LOG = Logger.getLogger(BMECatImportWithCsvWizard.class.getName());

	//cronjob through the wizard, and job for importing impex script
	private BMECatImportWithCsvCronJob cronJob = null;
	private BMECatImportWithCsvJob job = null;

	private boolean targetsFinished = false;
	private boolean mappingFinished = false;
	private boolean impexFinished = false;

	//cronjob only for transforming bmecat.xml to csv files
	private BMECatImportWithCsvTransformCronJob tCronJob = null;

	public final static List<String> tabSequence = Arrays.asList(new String[]
	{ Tabs.TAB0_JOB, Tabs.TAB1_FILES, Tabs.TAB2_CATALOG_INFORMATIONS, Tabs.TAB3_TARGETS_AND_MODE, Tabs.TAB4_MAPPINGS,
			Tabs.TAB5_IMPEX_SCRIPT, Tabs.TAB6_FURTHER_SETTINGS, Tabs.TAB7_ERROR });

	private boolean doingCsvImport = false;

	public interface Tabs
	{
		String TAB0_JOB = "tab0_job";
		String TAB1_FILES = "tab1_files";
		String TAB2_CATALOG_INFORMATIONS = "tab2_catalog_informations";
		String TAB3_TARGETS_AND_MODE = "tab3_targets_and_mode";
		String TAB4_MAPPINGS = "tab4_mappings";
		String TAB5_IMPEX_SCRIPT = "tab5_impex_script";
		String TAB6_FURTHER_SETTINGS = "tab6_further_settings";
		String TAB7_ERROR = "tab7_error";
	}

	public interface Errors
	{
		int MISSING_FILE = 33101;
		int NO_DATA = 33102;
		int FILE_LOCKED = 33103;
		int MISSING_TARGET = 33201;
		int CATALOG_VERSION_MISMATCH = 33202;
		int MISSING_LANGUAGE = 33204;
		int LANGUAGE_ALREADY_EXISTS = 33205;
		int MISSING_IMPORTJOB = 33104;
		int WRONG_IMPEX_SCRIPT = 33105;
		int MEDIA_FILE_NOT_NEEDED = 33106;
		int WRONG_MEDIA_TYPE = 33107;
	}


	@Override
	public void initialize(final WizardEditorContext ctx)
	{
		super.initialize(ctx);
		//is this parserError tab necessary? or when will this tab be displayed? thinking...
		ctx.hideTab(Tabs.TAB7_ERROR);
	}

	@Override
	public void validate(final WizardEditorContext ctx, final String fromTab, final String toTab) throws ValidationException
	{
		super.validate(ctx, fromTab, toTab);

		// only validate in forward direction
		if (tabSequence.indexOf(toTab) < tabSequence.indexOf(fromTab))
		{
			return;
		}

		final String currentTab = ctx.getCurrentTab();
		if (Tabs.TAB0_JOB.equals(currentTab))
		{
			validateJobTab(ctx);
		}
		else if (Tabs.TAB1_FILES.equals(currentTab))
		{
			if (Tabs.TAB2_CATALOG_INFORMATIONS.equals(toTab))
			{
				validateImportFiles(ctx);
			}
		}
		else if (Tabs.TAB3_TARGETS_AND_MODE.equals(currentTab))
		{
			validateImportTargets(ctx);
		}
		else if (Tabs.TAB4_MAPPINGS.equals(currentTab))
		{
			validateMappings(ctx);
		}
		else if (Tabs.TAB5_IMPEX_SCRIPT.equals(currentTab))
		{
			validateImpexFile(ctx);
		}
	}

	private void validateJobTab(final WizardEditorContext ctx) throws ValidationException
	{
		final BMECatImportWithCsvJob job = (BMECatImportWithCsvJob) ctx.getCurrentValue(BMECatImportWithCsvWizard.IMPORTJOB);
		if (job == null)
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.missing_importjob"),
					Errors.MISSING_IMPORTJOB);
		}
	}

	private void validateImportFiles(final WizardEditorContext ctx) throws ValidationException
	{
		final Media bmecatFile = (Media) ctx.getCurrentValue(BMECatImportWithCsvWizard.FILE);
		final Media mediaFile = (Media) ctx.getCurrentValue(BMECatImportWithCsvWizard.MEDIAFILE);

		/*
		 * definition: mediasnotincluded.true=no media file mediasnotincluded.false=media files in bmecat zip file
		 * mediasnotincluded.null=media files in separate zip file
		 */
		final boolean mediasnotincluded;
		if (ctx.getCurrentValue(BMECatImportWithCsvWizard.MEDIASNOTINCLUDED) != null)
		{
			mediasnotincluded = ((Boolean) ctx.getCurrentValue(BMECatImportWithCsvWizard.MEDIASNOTINCLUDED)).booleanValue();
			if (mediasnotincluded)
			{
				if (mediaFile != null)
				{
					throw new ValidationException(
							Localization.getLocalizedString("exception.bmecat.import.wizard.media_file_not_needed"),
							Errors.MEDIA_FILE_NOT_NEEDED);
				}
				this.cronJob.setMediasMedia(null);
			}
			else
			{
				if (!bmecatFile.getFileName().toLowerCase().endsWith(".zip"))
				{
					throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.wrong_media_type"),
							Errors.WRONG_MEDIA_TYPE);
				}
				else if (mediaFile != null)
				{
					throw new ValidationException(
							Localization.getLocalizedString("exception.bmecat.import.wizard.media_file_not_needed"),
							Errors.MEDIA_FILE_NOT_NEEDED);
				}
				this.cronJob.setMediasMedia(bmecatFile);
			}
		}
		else
		{
			if (mediaFile == null)
			{
				throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.media_file_missing"),
						Errors.MISSING_FILE);
			}
			this.cronJob.setMediasMedia(mediaFile);
		}

		if (bmecatFile == null)
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.catalog_file_no_data"),
					Errors.NO_DATA);
		}

		if (bmecatFile instanceof JobMedia)
		{
			if (((BMECatJobMedia) bmecatFile).isLockedAsPrimitive())
			{
				throw new ValidationException(
						Localization.getLocalizedString("exception.bmecat.import.wizard.catalog_file_being_processed"),
						Errors.FILE_LOCKED);
			}
		}
		//final Media prev = getFile();
		//fileChanged = prev != bmecatFile && (prev == null || !prev.equals(bmecatFile));

		startProcessing(ctx);
		final Map<String, String> values = new HashMap<String, String>();
		values.put("filename", bmecatFile.getRealFileName());
		values.put("filecode", bmecatFile.getCode());
		final StringBuilder stringBuilder = new StringBuilder();
		appendLocalizedTemplateString(stringBuilder, "bmecat.infostep.processing", values);
		ctx.setStatus(stringBuilder.toString(), 0, false);

		//parse the bmecat.xml, show info screen during the process
		this.tCronJob.getJob().perform(tCronJob, false);
	}

	@Override
	public void pollStatus(final WizardEditorContext ctx)
	{
		if (this.tCronJob.isRunning())
		{
			final StringBuilder stringBuilder = new StringBuilder("</br>");
			stringBuilder.append(tCronJob.getLogText());
			String status = stringBuilder.toString();
			status = status.replace("\n", "<br>");
			ctx.setStatus(status, 0, false);
		}
		else if (cronJob.isRunning())
		{
			final StringBuilder stringBuilder = new StringBuilder("</br>");
			stringBuilder.append(Localization.getLocalizedString("bmecat.statistics.impex.import") + "\n");
			stringBuilder.append(cronJob.getLogText());
			String status = stringBuilder.toString();
			status = status.replace("\n", "<br>");
			ctx.setStatus(status, 0, false);
		}
		else
		{
			endProcessing(ctx);
			//after impex script import
			if (this.doingCsvImport)
			{
				final StringBuilder stringBuilder = new StringBuilder("</br>");
				stringBuilder.append(cronJob.getLogText());
				String status = stringBuilder.toString();
				status = status.replace("\n", "<br>");
				ctx.showSummaryTab(status);
			}
			//after bmecat.xml transform to csv files
			else
			{
				ctx.reloadAllFields();
			}
		}
	}

	private void validateImportTargets(final WizardEditorContext ctx) throws ValidationException
	{
		final Catalog catalog = (Catalog) ctx.getCurrentValue(CATALOG);
		final CatalogVersion catalogVersion = (CatalogVersion) ctx.getCurrentValue(CATALOGVERSION);
		final Language catalogLanguage = (Language) ctx.getCurrentValue(CATALOGLANGUAGE);
		final Currency currency = (Currency) ctx.getCurrentValue(DEFAULTCURRENCY);
		final String catalogName = (String) ctx.getCurrentValue(CATALOGNAME);
		final String catalogVersionName = (String) ctx.getCurrentValue(CATALOGVERSIONNAME);
		final String catalogLanguageISO = (String) ctx.getCurrentValue(CATALOGLANGUAGEISO);
		final String currencyISO = (String) ctx.getCurrentValue(DEFAULTCURRENCYISO);

		if (catalog != null)
		{
			this.cronJob.getBmecatInfo().put("catalog.code", catalog.getId());
		}
		else
		{
			this.cronJob.getBmecatInfo().put("catalog.code", catalogName);
		}
		if (catalogVersion != null)
		{
			this.cronJob.getBmecatInfo().put("catalogversion.version", catalogVersion.getVersion());
		}
		else
		{
			this.cronJob.getBmecatInfo().put("catalogversion.version", catalogVersionName);
		}
		if (catalogLanguage != null)
		{
			this.cronJob.getBmecatInfo().put("catalog.language", catalogLanguage.getIsoCode());
		}
		else
		{
			this.cronJob.getBmecatInfo().put("catalog.language", catalogLanguageISO);
		}
		if (currency != null)
		{
			this.cronJob.getBmecatInfo().put("catalog.currency", currency.getIsoCode());
		}
		else
		{
			this.cronJob.getBmecatInfo().put("catalog.currency", currencyISO);
		}

		//TODO l10n update, should be dealt with
		this.cronJob.setLocalizationUpdate((Boolean) ctx.getCurrentValue(LOCALIZATIONUPDATE));
		this.targetsFinished = true;
	}

	private void validateMappings(final WizardEditorContext ctx) throws ValidationException
	{
		final Map<String, Object> units = (Map<String, Object>) ctx.getCurrentValue(UNITMAPPINGS);
		final Map<String, Object> cSystemVersions = (Map<String, Object>) ctx.getCurrentValue(CLASSIFICATIONMAPPINGS);
		final Map<String, Object> currencies = (Map<String, Object>) ctx.getCurrentValue(CURRENCYMAPPINGS);
		final Map<String, Object> priceTypes = (Map<String, Object>) ctx.getCurrentValue(PRICETYPEMAPPINGS);
		final Map<String, Object> taxTypes = (Map<String, Object>) ctx.getCurrentValue(TAXTYPEMAPPINGS);


		validateMappingValues(priceTypes, "exception.bmecat.import.wizard.unmapped_pricetypes", Errors.MISSING_TARGET);
		//validateMappingValues(cSystemVersions, "exception.bmecat.import.wizard.unmapped_systemversions", 
		//		Errors.MISSING_TARGET);

		//the user does not need to specify the following values, because such values can be created 
		//		validateMappingValues(currencies, "exception.bmecat.import.wizard.unmapped_currencies", 
		//				Errors.MISSING_TARGET);
		//		validateMappingValues(units, "exception.bmecat.import.wizard.unmapped_units", 
		//				Errors.MISSING_TARGET);
		//		validateMappingValues(taxTypes, "exception.bmecat.import.wizard.unmapped_taxtypes", 
		//				Errors.MISSING_TARGET);

		//save all mapping information after validation
		final Iterator<Map.Entry<String, Object>> itUnits = units.entrySet().iterator();
		while (itUnits.hasNext())
		{
			final Map.Entry<String, Object> entry = itUnits.next();
			this.cronJob.getBmecatMappings().put(entry.getKey(),
					entry.getValue() == null ? null : ((Unit) entry.getValue()).getCode());
		}

		final Iterator<Map.Entry<String, Object>> itCurrencies = currencies.entrySet().iterator();
		while (itCurrencies.hasNext())
		{
			final Map.Entry<String, Object> entry = itCurrencies.next();
			this.cronJob.getBmecatMappings().put(entry.getKey(),
					entry.getValue() == null ? null : ((Currency) entry.getValue()).getIsoCode());
		}

		final Iterator<Map.Entry<String, Object>> itPriceTypes = priceTypes.entrySet().iterator();
		while (itPriceTypes.hasNext())
		{
			final Map.Entry<String, Object> entry = itPriceTypes.next();
			this.cronJob.getBmecatMappings().put(entry.getKey(), ((EnumerationValue) entry.getValue()).getCode());
		}

		final Iterator<Map.Entry<String, Object>> itTaxTypes = taxTypes.entrySet().iterator();
		while (itTaxTypes.hasNext())
		{
			final Map.Entry<String, Object> entry = itTaxTypes.next();
			this.cronJob.getBmecatMappings().put(entry.getKey(),
					entry.getValue() == null ? null : ((EnumerationValue) entry.getValue()).getCode());
		}

		final Iterator<Map.Entry<String, Object>> itCSystemVersions = cSystemVersions.entrySet().iterator();
		boolean createClassSystem = false;
		if (!cSystemVersions.isEmpty())
		{
			final StringBuilder fullNames = new StringBuilder();
			while (itCSystemVersions.hasNext())
			{
				final StringBuilder fullName = new StringBuilder();
				final Map.Entry<String, Object> entry = itCSystemVersions.next();
				if (entry.getValue() != null)
				{
					LOG.info("full name: " + ((ClassificationSystemVersion) entry.getValue()).getFullVersionName());
					fullName.append(((ClassificationSystemVersion) entry.getValue()).getFullVersionName());
					final int pos = fullName.indexOf("/");
					fullName.setCharAt(pos, '-');
				}
				else
				{
					fullName.append(entry.getKey());
					createClassSystem = true;
				}
				this.cronJob.getBmecatMappings().put(entry.getKey(), fullName.toString());
				fullNames.append(fullName + ",");
			}
			final int length = fullNames.length();
			fullNames.deleteCharAt(length - 1);
			this.cronJob.getBmecatInfo().put("classification.system.versions", fullNames.toString());
			this.cronJob.getBmecatInfo().put("classification.system.create", Boolean.toString(createClassSystem));
			LOG.debug("all versions: " + fullNames);
		}

		this.mappingFinished = true;
	}

	private void validateMappingValues(final Map<String, Object> mappingValues, final String error, final int errorCode)
			throws ValidationException
	{
		final Iterator<Map.Entry<String, Object>> iterator = mappingValues.entrySet().iterator();
		final Set<String> unmappedValues = new HashSet<String>();
		while (iterator.hasNext())
		{
			final Map.Entry<String, Object> entry = iterator.next();
			if (entry.getValue() == null)
			{
				unmappedValues.add(entry.getKey());
			}
		}
		if (!unmappedValues.isEmpty())
		{
			throw new ValidationException(Localization.getLocalizedString(error, new Object[]
			{ unmappedValues }), errorCode);
		}
	}

	private void validateImpexFile(final WizardEditorContext ctx) throws ValidationException
	{
		//TODO it is perhaps a good idea to get the default impex file from the class path directory 
		//why not get the default impex file from the system?
		//because the names of the files as Media can be duplicated, 
		//that means, two files with the same name(code) are correct in the system
		//and we do not know which one is the correct default file
		final Media impexFile = (Media) ctx.getCurrentValue(BMECatImportWithCsvWizard.IMPEXSCRIPTFILE);

		String impexFileName;
		if (impexFile == null)
		{
			//default impex script will be executed if it is not specified
			//three different types, namely T_NEW_CATALOG, T_UPDATE_PRODUCTS, and T_UPDATE_PRICES
			if (BMECatConstants.XML.TAG.T_NEW_CATALOG.equals(this.cronJob.getTransactionMode().getCode()))
			{
				impexFileName = "default_t_new_catalog.impex";
			}
			else if (BMECatConstants.XML.TAG.T_UPDATE_PRODUCTS.equals(this.cronJob.getTransactionMode().getCode()))
			{
				impexFileName = "default_t_update_products.impex";
			}
			else if (BMECatConstants.XML.TAG.T_UPDATE_PRICES.equals(this.cronJob.getTransactionMode().getCode()))
			{
				impexFileName = "default_t_update_prices.impex";
			}
			else
			{
				LOG.info("no default impex file found");
				return;
			}
			try
			{
				final InputStream inputStream = BMECatImportWithCsvWizard.class.getResourceAsStream("/bmecat/" + impexFileName);
				this.cronJob.setDefaultImpExContent(getImpexContent(inputStream));
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (!impexFile.getRealFileName().toLowerCase().endsWith(".impex"))
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.wrong_impex_file_type"),
					Errors.WRONG_IMPEX_SCRIPT);
		}
		this.impexFinished = true;
	}

	private String getImpexContent(final InputStream impexInputStream)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		final int byteBufferSize = 4096;
		final byte byteArray[] = new byte[byteBufferSize];
		try
		{
			int len = impexInputStream.read(byteArray, 0, byteBufferSize);
			while (len != -1)
			{
				stringBuilder.append(new StringBuilder(new String(byteArray, 0, len)));
				len = impexInputStream.read(byteArray, 0, byteBufferSize);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	@Override
	public void tabChanges(final WizardEditorContext ctx, final String fromTabName, final String toTabName) throws VetoException
	{
		//only tab0-->tab1 allowed
		if (Tabs.TAB0_JOB.equals(fromTabName))
		{
			if (!Tabs.TAB1_FILES.equals(toTabName))
			{
				throw new VetoException(Localization.getLocalizedString("exception.bmecat.import.wizard.go_to_file"), 555);
			}
		}
		else if (Tabs.TAB1_FILES.equals(toTabName))
		{
			if (!Tabs.TAB0_JOB.equals(fromTabName))
			{
				throw new VetoException(Localization.getLocalizedString("exception.bmecat.import.wizard.back_to_file"), 555);
			}
		}
		//cannot go back to tab0
		if (Tabs.TAB0_JOB.equals(toTabName))
		{
			throw new VetoException(Localization.getLocalizedString("exception.bmecat.import.wizard.back_to_job"), 555);
		}
		//only tab1-->tab2 allowed
		else if (Tabs.TAB1_FILES.equals(fromTabName))
		{
			if (!Tabs.TAB2_CATALOG_INFORMATIONS.equals(toTabName))
			{
				throw new VetoException(Localization.getLocalizedString("exception.bmecat.import.wizard.parse_bmecat"), 555);
			}
		}
		else if (Tabs.TAB2_CATALOG_INFORMATIONS.equals(fromTabName))
		{
			if (getBmecatImportWithCsvTransformCronJob().isParserError())
			{
				throw new VetoException("some critical error occured - " + "see LOG entries or console LOG and restart the wizard",
						555);
			}
		}
		else if (Tabs.TAB6_FURTHER_SETTINGS.equalsIgnoreCase(toTabName))
		{
			ctx.disableButton(NEXT_BUTTON);
			if (this.targetsFinished && this.mappingFinished && this.impexFinished)
			{
				ctx.enableButton(START_BUTTON);
			}
		}
		else
		{
			ctx.disableButton(START_BUTTON);
			ctx.enableButton(NEXT_BUTTON);
		}

		//disable back button on tab0, tab1, and tab2
		if (Tabs.TAB1_FILES.equals(toTabName) || Tabs.TAB2_CATALOG_INFORMATIONS.equals(toTabName))
		{
			ctx.disableButton(BACK_BUTTON);
		}
		else
		{
			ctx.enableButton(BACK_BUTTON);
		}
	}

	/**
	 * all information available, and the transformed csv files can be imported immediately
	 */
	@Override
	public void start(final WizardEditorContext ctx) throws VetoException
	{
		validateFurtherInfos(ctx);
		this.cronJob.setEnableCodeExecution(true);
		this.cronJob.setEnableExternalSyntaxParsing(true);
		this.cronJob.setEnableExternalCodeExecution(true);

		this.doingCsvImport = true;
		startImport(ctx);
	}

	private void validateFurtherInfos(final WizardEditorContext ctx) throws VetoException
	{
		final boolean sendEmail = ((Boolean) ctx.getCurrentValue(SENDEMAIL)).booleanValue();
		final String address = (String) ctx.getCurrentValue(EMAILADDRESS);
		if (sendEmail && (address == null || "".equals(address.trim())))
		{
			throw new VetoException("missing email address for enabled email notification "
					+ "-please provide one or disable notification", 33301);
		}
	}

	/*
	 * csv files are collected in the transform job process, media files (jpg, pdf, and so on) are collected in file
	 * validation process, impex file are generated in the import job process,
	 */
	private void startImport(final WizardEditorContext ctx)
	{
		startProcessing(ctx);
		ctx.showButton(CANCEL_BUTTON);
		ctx.enableButton(CANCEL_BUTTON);
		ctx.disableButton(START_BUTTON);
		ctx.setStatus(Localization.getLocalizedString("bmecat.statistics.impex.import"), 0, false);
		this.cronJob.getJob().perform(this.cronJob);
	}

	protected void removeCronjob() throws ConsistencyCheckException
	{
		if (this.cronJob != null)
		{
			if (this.cronJob.isRunning())
			{
				LOG.error("cannot remove wizard cronjob " + this.cronJob.getCode() + "(" + this.cronJob.getPK().toString()
						+ ") since it is still running " + "- please remove manually when finished");
			}
			else
			{
				this.cronJob.remove();
				this.cronJob = null;
			}
		}
	}

	@Override
	public Map<String, ClassificationSystemVersion> getAllClassificationMappings(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getAllClassificationMappings(ctx);
		}
	}

	@Override
	public Map<String, Currency> getAllCurrencyMappings(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getAllCurrencyMappings(ctx);
		}
	}

	@Override
	public Map<String, EnumerationValue> getAllPriceTypeMappings(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getAllPriceTypeMappings(ctx);
		}
	}

	@Override
	public Map<String, EnumerationValue> getAllTaxTypeMappings(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getAllTaxTypeMappings(ctx);
		}
	}

	@Override
	public Map<String, Unit> getAllUnitMappings(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			final Map<String, Unit> current = getBmecatImportWithCsvCronJob().getAllUnitMappings(ctx);
			final Map<String, Unit> ret = new HashMap<String, Unit>();
			if (current != null)
			{
				Map<String, Unit> def = ((BMECatImportWithCsvJob) getBmecatImportWithCsvCronJob().getJob(ctx))
						.getAllDefaultUnitMappings(ctx);
				if (def != null)
				{
					def = new HashMap<String, Unit>(def);
				}
				else
				{
					def = new HashMap<String, Unit>();
				}

				boolean saveDefaults = false;
				final Iterator<Map.Entry<String, Unit>> iter = current.entrySet().iterator();
				while (iter.hasNext())
				{
					final Map.Entry<String, Unit> mapEntry = iter.next();
					Unit unit = def.get(mapEntry.getKey());
					if (unit == null && mapEntry.getValue() != null)
					{
						saveDefaults = true;
						def.put(mapEntry.getKey(), unit = mapEntry.getValue());
					}
					ret.put(mapEntry.getKey(), unit);
				}
				if (!ret.equals(current))
				{
					getBmecatImportWithCsvCronJob().setAllUnitMappings(ctx, ret);
				}
				if (saveDefaults)
				{
					((BMECatImportWithCsvJob) getBmecatImportWithCsvCronJob().getJob(ctx)).setAllDefaultUnitMappings(ctx, ret);
				}

				return ret;
			}
			else
			{
				return Collections.EMPTY_MAP;
			}
		}

	}

	@Override
	public Integer getArticleCount(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getArticleCount();
		}
	}

	@Override
	public BMECatImportWithCsvCronJob getBmecatImportWithCsvCronJob(final SessionContext ctx)
	{
		if (this.cronJob == null && getImportJob() == null)
		{
			this.cronJob = BMECatManager.getInstance().createDefaultBMECatImportWithCsvCronJob(null, getImportJob());
			if (this.cronJob.getJobMedia() != null)
			{
				final ImpExMedia media = this.cronJob.getJobMedia();
				this.cronJob.setJobMedia(null);
				try
				{
					media.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		return this.cronJob;
	}

	@Override
	public BMECatImportWithCsvTransformCronJob getBmecatImportWithCsvTransformCronJob(final SessionContext ctx)
	{
		if (this.tCronJob == null)
		{
			this.tCronJob = BMECatManager.getInstance().createDefaultBMECatImportWithCsvTransformCronJob(null, null);
			this.tCronJob.setSingleExecutable(false);
			if (this.tCronJob.getJobMedia() != null)
			{
				final JobMedia media = this.tCronJob.getJobMedia();
				this.tCronJob.setJobMedia(null);
				try
				{
					media.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
			//create a reference to import cronjob
			this.tCronJob.setBmecatImportWithCsvCronJob(this.cronJob);
		}
		return this.tCronJob;
	}

	@Override
	public Catalog getCatalog(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getCatalog(ctx);
		}
	}

	@Override
	public Date getCatalogDate(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getCatalogDate(ctx);
		}
	}

	@Override
	public Language getCatalogLanguage(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getImportLanguage(ctx);
		}
	}

	@Override
	public String getCatalogLanguageISO(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getImportLanguageIsoCode(ctx);
		}
	}

	@Override
	public String getCatalogName(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getCatalogID(ctx);
		}
	}

	@Override
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getCatalogVersion(ctx);
		}
	}

	@Override
	public String getCatalogVersionName(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getCatalogVersionName(ctx);
		}
	}

	@Override
	public Integer getCategoryAssignmentsCount(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getCategoryAssignmentsCount(ctx);
		}
	}

	@Override
	public Integer getCategoryCount(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getCategoryCount(ctx);
		}
	}

	@Override
	public Currency getDefaultCurrency(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getDefaultCurrency(ctx);
		}
	}

	@Override
	public String getDefaultCurrencyISO(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getDefaultCurrencyIsoCode(ctx);
		}
	}

	@Override
	public String getEmailAddress(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getEmailAddress(ctx);
		}
	}

	@Override
	public BMECatJobMedia getFile(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvTransformCronJob() == null)
		{
			return null;
		}
		else
		{
			return (BMECatJobMedia) getBmecatImportWithCsvTransformCronJob().getJobMedia(ctx);
		}
	}

	@Override
	public BMECatImpExScriptMedia getImpexScriptFile(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getImpexScriptMedia(ctx);
		}
	}

	@Override
	public BMECatImportWithCsvJob getImportJob(final SessionContext ctx)
	{
		return this.job;
	}

	@Override
	public Integer getKeywordCount(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getKeywordCount(ctx);
		}
	}

	@Override
	public Integer getMediaCount(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getMimeCount(ctx);
		}
	}

	@Override
	public BMECatJobMedia getMediaFile(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return (BMECatJobMedia) getBmecatImportWithCsvCronJob().getMediasMedia(ctx);
		}
	}

	@Override
	public Collection<String> getTerritories(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getTerritories(ctx);
		}
	}

	@Override
	public EnumerationValue getTransactionMode(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().getTransactionMode(ctx);
		}
	}

	@Override
	public Boolean isEnableUndo(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().isChangeRecordingEnabled(ctx);
		}
	}

	@Override
	public Boolean isLocalizationUpdate(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().isLocalizationUpdate(ctx);
		}
	}

	@Override
	public Boolean isMediasNotIncluded(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvTransformCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvTransformCronJob().isReferenceMedias(ctx);
		}
	}

	@Override
	public Boolean isSendEmail(final SessionContext ctx)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return null;
		}
		else
		{
			return getBmecatImportWithCsvCronJob().isSendEmail(ctx);
		}
	}

	@Override
	public void setAllClassificationMappings(final SessionContext ctx, final Map<String, ClassificationSystemVersion> value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setAllClassificationMappings(ctx, value);
		}
	}

	@Override
	public void setAllCurrencyMappings(final SessionContext ctx, final Map<String, Currency> value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setAllCurrencyMappings(ctx, value);
		}
	}

	@Override
	public void setAllPriceTypeMappings(final SessionContext ctx, final Map<String, EnumerationValue> value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setAllPriceTypeMappings(ctx, value);
		}
	}

	@Override
	public void setAllTaxTypeMappings(final SessionContext ctx, final Map<String, EnumerationValue> value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setAllTaxTypeMappings(ctx, value);
		}
	}

	@Override
	public void setAllUnitMappings(final SessionContext ctx, final Map<String, Unit> value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}

		if (value != null)
		{
			Map defaults = ((BMECatImportWithCsvJob) getBmecatImportWithCsvCronJob().getJob(ctx)).getAllDefaultUnitMappings(ctx);
			if (defaults == null)
			{
				defaults = new HashMap();
			}
			else
			{
				defaults = new HashMap(defaults);
			}
			boolean save = false;
			final Iterator iterator = value.entrySet().iterator();
			while (iterator.hasNext())
			{
				final Map.Entry mapEntry = (Map.Entry) iterator.next();
				if (mapEntry.getValue() != null)
				{
					save |= !mapEntry.getValue().equals(defaults.put(mapEntry.getKey(), mapEntry.getValue()));
				}
			}
			if (save)
			{
				((BMECatImportWithCsvJob) getBmecatImportWithCsvCronJob().getJob(ctx)).setAllDefaultUnitMappings(ctx, defaults);
			}
		}
		getBmecatImportWithCsvCronJob().setAllUnitMappings(ctx, value);
	}

	@Override
	public void setCatalog(final SessionContext ctx, final Catalog value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setCatalog(ctx, value);
		}
	}

	@Override
	public void setCatalogLanguage(final SessionContext ctx, final Language value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setImportLanguage(ctx, value);
		}
	}

	@Override
	public void setCatalogVersion(final SessionContext ctx, final CatalogVersion value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setCatalogVersion(ctx, value);
		}
	}

	@Override
	public void setDefaultCurrency(final SessionContext ctx, final Currency value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setDefaultCurrency(ctx, value);
		}
	}

	@Override
	public void setEmailAddress(final SessionContext ctx, final String value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setEmailAddress(ctx, value);
		}
	}

	@Override
	public void setEnableUndo(final SessionContext ctx, final Boolean value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setChangeRecordingEnabled(ctx, value);
		}
	}

	@Override
	public void setFile(final SessionContext ctx, final BMECatJobMedia value)
	{
		if (getBmecatImportWithCsvTransformCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvTransformCronJob().setJobMedia(ctx, value);
		}
	}

	@Override
	public void setImpexScriptFile(final SessionContext ctx, final BMECatImpExScriptMedia value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setImpexScriptMedia(ctx, value);
		}
	}

	@Override
	public void setImportJob(final SessionContext ctx, final BMECatImportWithCsvJob value)
	{
		this.job = value;
	}

	@Override
	public void setLocalizationUpdate(final SessionContext ctx, final Boolean value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setLocalizationUpdate(ctx, value);
		}
	}

	@Override
	public void setMediaFile(final SessionContext ctx, final BMECatJobMedia value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setMediasMedia(ctx, value);
		}
	}

	@Override
	public void setMediasNotIncluded(final SessionContext ctx, final Boolean value)
	{
		if (getBmecatImportWithCsvTransformCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvTransformCronJob().setReferenceMedias(ctx, value);
		}
	}

	@Override
	public void setSendEmail(final SessionContext ctx, final Boolean value)
	{
		if (getBmecatImportWithCsvCronJob() == null)
		{
			return;
		}
		else
		{
			getBmecatImportWithCsvCronJob().setSendEmail(ctx, value);
		}
	}

}

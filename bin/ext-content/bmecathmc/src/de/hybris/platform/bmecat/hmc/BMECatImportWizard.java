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
import de.hybris.platform.bmecat.jalo.BMECatImportBatchJob;
import de.hybris.platform.bmecat.jalo.BMECatImportCronJob;
import de.hybris.platform.bmecat.jalo.BMECatImportStep;
import de.hybris.platform.bmecat.jalo.BMECatInfoStep;
import de.hybris.platform.bmecat.jalo.BMECatJobMedia;
import de.hybris.platform.bmecat.jalo.BMECatManager;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.cronjob.jalo.JobMedia;
import de.hybris.platform.cronjob.jalo.Step;
import de.hybris.platform.hmc.jalo.ValidationException;
import de.hybris.platform.hmc.jalo.VetoException;
import de.hybris.platform.hmc.jalo.WizardEditorContext;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
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


/**
 * 
 */
public class BMECatImportWizard extends GeneratedBMECatImportWizard
{
	private static final Logger LOG = Logger.getLogger(BMECatImportWizard.class.getName());

	public final static List tabSequence = Arrays.asList(new String[]
	{ Tabs.TAB0_JOB, Tabs.TAB1_FILES, Tabs.TAB2_CATALOG_INFORMATIONS, Tabs.TAB3_TARGETS_AND_MODE, Tabs.TAB4_MAPPINGS,
			Tabs.TAB5_FURTHER_SETTINGS, Tabs.TAB6_ERROR });

	public interface Tabs
	{
		String TAB0_JOB = "tab0_job";

		String TAB1_FILES = "tab1_files";

		String TAB2_CATALOG_INFORMATIONS = "tab2_catalog_informations";

		String TAB3_TARGETS_AND_MODE = "tab3_targets_and_mode";

		String TAB4_MAPPINGS = "tab4_mappings";

		String TAB5_FURTHER_SETTINGS = "tab5_further_settings";

		String TAB6_ERROR = "tab6_error";
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
	}

	private BMECatImportBatchJob bmeCatImportBatchJob = null;

	private BMECatImportCronJob cronJob = null;

	private BMECatInfoStep infoStep = null;

	private boolean doingInfoStep = false;

	private boolean doingImport = false;

	private boolean done = false;

	private boolean error = false;

	private int transactionMode = -1;

	private boolean fileChanged = false;

	protected void removeCronjob() throws ConsistencyCheckException
	{
		if (this.cronJob != null)
		{
			// try to abort import if wizard is closed to early
			if (this.cronJob.isRunning())
			{
				LOG.error("cannot remove wizard cronjob " + this.cronJob.getCode() + "(" + this.cronJob.getPK().toString()
						+ ") since it is still running " + "- please remove manually when finished");
			}
			else
			{
				this.cronJob.remove();
				this.cronJob = null;
				this.infoStep = null;
			}
		}
	}

	/**
	 * Starts the import. This requires that the user processed the 3rd tab!
	 * 
	 * @see de.hybris.platform.hmc.jalo.WizardBusinessItem#start(de.hybris.platform.hmc.jalo.WizardEditorContext)
	 */
	@Override
	public void start(final WizardEditorContext ctx) throws VetoException
	{
		validateFurtherInfos(ctx);
		startImport(ctx);
	}

	/**
	 * Closes this wizard and removes the underlying {@link BMECatImportCronJob} unless the
	 * {@link BMECatImportWizard#KEEPLOGS} field is <code> true </code> which does not remove the cronjob for later
	 * review or undo of the import.
	 * 
	 * @see de.hybris.platform.hmc.jalo.WizardBusinessItem#close(de.hybris.platform.hmc.jalo.WizardEditorContext)
	 */
	@Override
	public void close(final WizardEditorContext ctx) throws VetoException
	{
		if (doingImport && getBmeCatImportCronJob().isRunning())
		{
			getBmeCatImportCronJob().setRequestAbort(true);
			final int maxWait = 60 * 1000;
			int time = 0;
			while (getBmeCatImportCronJob().isRunning() && time < maxWait)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (final InterruptedException e)
				{
					// DOCTODO Document reason, why this block is empty
				}
				time += 1000;
			}
		}
		// remove cronjob if either it has not been started and/or finished or if
		// removal was requested
		if (!(done || doingImport || error) || !isKeepLogsAsPrimitive())
		{
			try
			{
				// TODO: remove cronjob asynchronously since it may take some time
				// !!!
				removeCronjob();
			}
			catch (final ConsistencyCheckException e)
			{
				throw new VetoException(e.getLocalizedMessage(), e.getErrorCode());
			}
		}
		super.close(ctx);
	}

	@Override
	public void tabChanges(final WizardEditorContext ctx, final String fromTabName, final String toTabName) throws VetoException
	{
		if (cronJob.getErrorFlag())
		{
			throw new VetoException(
					(cronJob.getCriticalErrorMsg() != null && cronJob.getCriticalErrorMsg().length() > 0) ? cronJob.getCriticalErrorMsg()
							: "some critical error occured - see LOG entries or console LOG and restart the wizard", 555);
		}

		// consistency check
		checkTab(ctx, fromTabName, toTabName);

		// processing
		if (Tabs.TAB1_FILES.equalsIgnoreCase(fromTabName) && Tabs.TAB2_CATALOG_INFORMATIONS.equalsIgnoreCase(toTabName))
		{
			if (!Tabs.TAB0_JOB.equalsIgnoreCase(toTabName))
			{
				processFilesTab(ctx);
			}
		}
		//		else if (Tabs.TAB2_CATALOG_INFORMATIONS.equalsIgnoreCase(fromTabName))
		//		{
		// processCatalogInfoTab( ctx );
		//		}
		else if (Tabs.TAB3_TARGETS_AND_MODE.equalsIgnoreCase(fromTabName) && Tabs.TAB4_MAPPINGS.equalsIgnoreCase(toTabName))
		{
			processTargetsAndModeTab(ctx);
		}
		//		else if (Tabs.TAB4_MAPPINGS.equalsIgnoreCase(fromTabName))
		//		{
		// processMappingsTab(ctx,toTabName);
		//		}

		/*
		 * button settings
		 */
		if (Tabs.TAB5_FURTHER_SETTINGS.equalsIgnoreCase(toTabName))
		{
			ctx.disableButton(NEXT_BUTTON);
			ctx.enableButton(START_BUTTON);
		}
		else
		{
			ctx.disableButton(START_BUTTON);
			ctx.enableButton(NEXT_BUTTON);
		}
	}

	private void processFilesTab(final WizardEditorContext ctx) throws VetoException
	{
		final boolean alreadyGotInfo = this.infoStep != null;
		// reset steps queue if this wizard has already run the info step
		if (fileChanged && alreadyGotInfo)
		{
			final List pending = new ArrayList(getBmeCatImportCronJob().getPendingSteps());
			final List processed = new ArrayList(getBmeCatImportCronJob().getProcessedSteps());
			pending.add(0, this.infoStep);
			getBmeCatImportCronJob().setPendingSteps(pending);
			processed.remove(this.infoStep);
			getBmeCatImportCronJob().setProcessedSteps(processed);

			// reset all info fields
			getBmeCatImportCronJob().setArticleCount(null);
			getBmeCatImportCronJob().setCategoryCount(null);
			getBmeCatImportCronJob().setMimeCount(null);
			getBmeCatImportCronJob().setKeywordCount(null);
			getBmeCatImportCronJob().setCatalog(null);
			getBmeCatImportCronJob().setCatalogID(null);
			getBmeCatImportCronJob().setCatalogVersion(null);
			getBmeCatImportCronJob().setCatalogVersionName(null);
			getBmeCatImportCronJob().setImportLanguage(null);
			getBmeCatImportCronJob().setImportLanguageIsoCode(null);
			getBmeCatImportCronJob().setDefaultCurrency(null);
			getBmeCatImportCronJob().setDefaultCurrencyIsoCode(null);
			// getBmeCatImportCronJob().setBuyer(null);
			// getBmeCatImportCronJob().setBuyerName(null);
			// getBmeCatImportCronJob().setSupplier(null);
			// getBmeCatImportCronJob().setSupplierName(null);
			getBmeCatImportCronJob().setAllCountryMappings(null);
			getBmeCatImportCronJob().setAllClassificationMappings(null);
			getBmeCatImportCronJob().setAllClassificationMappings(null);
			getBmeCatImportCronJob().setAllUnitMappings(null);
			getBmeCatImportCronJob().setTransactionMode(null);
			getBmeCatImportCronJob().setCatalogInfoAvailable(null);
			// TODO: reload gui ???
		}
		if (!alreadyGotInfo || fileChanged)
		{
			startInfoStep(ctx);
		}
	}

	private void processTargetsAndModeTab(final WizardEditorContext ctx)
	{
		//		if (getTransactionModeAsInt() == BMECatConstants.TRANSACTION.T_NEW_CATALOG)
		//		{
		// getCronJob().setLocalizationUpdate( getCatalog() != null &&
		// getCatalogVersion() != null );
		//		}
	}

	@Override
	public void pollStatus(final WizardEditorContext ctx)
	{
		if (doingInfoStep)
		{
			pollStatusInfoStep(ctx);
		}
		else if (doingImport)
		{
			pollStatusImport(ctx);
		}
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
			validateImportFiles(ctx);
		}
		else if (Tabs.TAB3_TARGETS_AND_MODE.equals(currentTab))
		{
			validateImportTargets(ctx);
		}
		else if (Tabs.TAB4_MAPPINGS.equals(currentTab))
		{
			validateMappings(ctx);
		}
	}

	private void validateJobTab(final WizardEditorContext ctx) throws ValidationException
	{
		final BMECatImportBatchJob importJob = (BMECatImportBatchJob) ctx.getCurrentValue(BMECatImportWizard.IMPORTJOB);
		if (importJob == null)
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.missing_importjob"),
					Errors.MISSING_IMPORTJOB);
		}
	}

	private void validateImportFiles(final WizardEditorContext ctx) throws ValidationException
	{
		final Media src = (Media) ctx.getCurrentValue(BMECatImportWizard.FILE);

		if (src == null)
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.missing_catalog_file"),
					Errors.MISSING_FILE);
		}
		else if (!src.hasData())
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.catalog_file_no_data"),
					Errors.NO_DATA);
		}

		if (src instanceof JobMedia)
		{
			if (((BMECatJobMedia) src).isLockedAsPrimitive())
			{
				throw new ValidationException(
						Localization.getLocalizedString("exception.bmecat.import.wizard.catalog_file_being_processed"),
						Errors.FILE_LOCKED);
			}
		}
		final Media prev = getFile();
		fileChanged = prev == null || !prev.equals(src);
	}

	private void validateImportTargets(final WizardEditorContext ctx) throws ValidationException
	{
		final Catalog catalog;
		final CatalogVersion catalogVersion = (CatalogVersion) ctx.getCurrentValue(CATALOGVERSION);
		final Language catalogLanguage = (Language) ctx.getCurrentValue(CATALOGLANGUAGE);

		switch (getTransactionModeAsInt())
		{
			case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
				if (catalogLanguage == null)
				{
					throw new ValidationException(
							Localization.getLocalizedString("exception.bmecat.import.wizard.missing_import_language"),
							Errors.MISSING_LANGUAGE);
				}

				if (ctx.getCurrentValue(CATALOG) == null && catalogVersion != null)
				{
					catalog = catalogVersion.getCatalog();
					setCatalog(catalog);
					ctx.reloadField(CATALOG);
				}
				else
				{
					catalog = (Catalog) ctx.getCurrentValue(CATALOG);
				}

				if (catalog != null && catalogVersion != null)
				{
					if (!catalog.equals(catalogVersion.getCatalog()))
					{
						throw new ValidationException(Localization.getLocalizedString(
								"exception.bmecat.import.wizard.catalog_version_mismatch", new Object[]
								{ catalogVersion.getVersion(), catalog.getId(), catalogVersion.getCatalog().getId() }),
								Errors.CATALOG_VERSION_MISMATCH);
					}
					/*
					 * if( catalogVersion.isImportedLanguage( catalogLanguage.getIsoCode() ) ) { // ctx.setCurrentTab(
					 * Tabs.TAB1_FILES ); ctx.showError( BMECatImportWizard.FILE, Localization.getLocalizedString(
					 * "exception.bmecat.import.wizard.already_imported" ) ); throw new ValidationException(
					 * Localization.getLocalizedString( "exception.bmecat.import.wizard.language_already_exists", new
					 * Object[]{ catalogLanguage.getIsoCode(), catalogVersion.getVersion() } ),
					 * Errors.LANGUAGE_ALREADY_EXISTS ); }
					 */
					ctx.postInfoMessage(Localization.getLocalizedString(
							"infomessage.bmecat.import.wizard.language_already_exists_info", new Object[]
							{ catalogLanguage.getIsoCode() }));
				}
				break;

			case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
				if (catalogLanguage == null)
				{
					throw new ValidationException(
							Localization.getLocalizedString("exception.bmecat.import.wizard.missing_update_language"),
							Errors.MISSING_LANGUAGE);
				}
				break;

			case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:

				if (catalogVersion == null)
				{
					throw new ValidationException(
							Localization.getLocalizedString("exception.bmecat.import.wizard.missing_target_catalog_version"),
							Errors.MISSING_TARGET);
				}
				if (ctx.getCurrentValue(CATALOG) == null)
				{
					setCatalog(catalog = catalogVersion.getCatalog());
					ctx.reloadField(BMECatImportWizard.CATALOG);
				}
				else
				{
					catalog = (Catalog) ctx.getCurrentValue(CATALOG);
				}

				if (!catalog.equals(catalogVersion.getCatalog()))
				{
					throw new ValidationException(Localization.getLocalizedString(
							"exception.bmecat.import.wizard.catalog_version_mismatch", new Object[]
							{ catalogVersion.getVersion(), catalog.getId(), catalogVersion.getCatalog().getId() }),
							Errors.CATALOG_VERSION_MISMATCH);
				}

				break;
		}
	}

	private void validateMappings(final WizardEditorContext ctx) throws ValidationException
	{
		switch (getTransactionModeAsInt())
		{
			case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
				/*
				 * final Company supplier = (Company)ctx.getCurrentValue( SUPPLIER ); if( ctx.getCurrentValue( BUYER ) ==
				 * null ) { if( supplier == null ) { ctx.postInfoMessage( Localization.getLocalizedString(
				 * "infomessage.bmecat.import.wizard.buyer_supplier_not_specified" ) ); } else { ctx.postInfoMessage(
				 * Localization.getLocalizedString( "infomessage.bmecat.import.wizard.buyer_not_specified" ) ); } } else if(
				 * supplier == null ) { ctx.postInfoMessage( Localization.getLocalizedString(
				 * "infomessage.bmecat.import.wizard.supplier_not_specified" ) ); }
				 */
				break;

			case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
				break;

			case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
				break;
		}

		final Map currencyMap = (Map) ctx.getCurrentValue(CURRENCYMAPPINGS);
		final Set unmappedCurrencies = new HashSet();
		for (final Iterator it = currencyMap.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue() == null)
			{
				unmappedCurrencies.add(entry.getKey());
			}
		}
		if (!unmappedCurrencies.isEmpty())
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.unmapped_currencies",
					new Object[]
					{ unmappedCurrencies }), 33302);
		}

		final Map classificationMap = (Map) ctx.getCurrentValue(CLASSIFICATIONMAPPINGS);
		final Set unmappedClassification = new HashSet();
		for (final Iterator it = classificationMap.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue() == null)
			{
				unmappedClassification.add(entry.getKey());
			}
		}

		// BMECAT-189
		/*
		 * if (!unmappedClassification.isEmpty()) { throw new ValidationException(Localization.getLocalizedString(
		 * "exception.bmecat.import.wizard.unmapped_classification", new Object[] { unmappedClassification }), 33302); }
		 */
		final Map unitMap = (Map) ctx.getCurrentValue(UNITMAPPINGS);
		final Set unmappedUnits = new HashSet();
		for (final Iterator it = unitMap.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue() == null)
			{
				unmappedUnits.add(entry.getKey());
			}
		}
		if (!unmappedUnits.isEmpty())
		{
			throw new ValidationException(Localization.getLocalizedString("exception.bmecat.import.wizard.unmapped_units",
					new Object[]
					{ unmappedUnits }), 33302);
		}
	}

	private void validateFurtherInfos(final WizardEditorContext ctx) throws VetoException
	{
		final Boolean sendEmail = (Boolean) ctx.getCurrentValue(SENDEMAIL);
		final String address = (String) ctx.getCurrentValue(EMAILADDRESS);
		if (Boolean.TRUE.equals(sendEmail) && (address == null || "".equals(address)))
		{
			throw new VetoException(
					"missing email address for enabled email notification -please provide one or disable notification", 33301);
		}
	}

	private void checkTab(@SuppressWarnings("unused") final WizardEditorContext ctx,
			@SuppressWarnings("unused") final String tabFrom, @SuppressWarnings("unused") final String tabTo) throws VetoException
	{
		if (error)
		{
			throw new VetoException("some error occured - see console LOG and restart the wizard", 555);
		}
		if (done)
		{
			throw new VetoException("import is finished - please close this wizard", 666);
		}

		if (getBmeCatImportCronJob() != null && getBmeCatImportCronJob().isRunning())
		{
			throw new IllegalStateException("job is running - tabs should not be changeable at all");
		}

		// if( Tabs.TAB2_CATALOG_INFORMATIONS.equalsIgnoreCase(from) &&
		// Tabs.TAB5_FURTHER_SETTINGS.equalsIgnoreCase(to) ) checkTab2(ctx );
	}

	private void startInfoStep(final WizardEditorContext ctx)
	{
		final JobMedia src = getFile();
		// fetch info step
		final Step step = getBmeCatImportCronJob().getPendingSteps().iterator().next();
		if (!(step instanceof BMECatInfoStep))
		{
			throw new IllegalStateException("first step of bmecat import job " + getBmeCatImportCronJob().getJob()
					+ " is no BMECatInfoStep but " + step.getClass().getName());
		}
		this.infoStep = (BMECatInfoStep) step;

		// set inital status
		startProcessing(ctx);

		final Map values = new HashMap();
		values.put("filename", src.getRealFileName());
		values.put("filecode", src.getCode());
		final StringBuilder stringBuilder = new StringBuilder();
		appendLocalizedTemplateString(stringBuilder, "bmecat.infostep.processing", values);
		ctx.setStatus(stringBuilder.toString(), 0, false);

		// start it
		this.doingInfoStep = true;
		getBmeCatImportCronJob().getJob().perform(getBmeCatImportCronJob(), false);
	}

	private void pollStatusInfoStep(final WizardEditorContext ctx)
	{
		// TODO: how to find out if an error occured
		if (getBmeCatImportCronJob().isRunning())
		{
			// 1. runing info step ?
			if (getBmeCatImportCronJob().getCurrentStep() != null && getBmeCatImportCronJob().getCurrentStep().equals(this.infoStep))
			{
				final StringBuilder stringBuilder = new StringBuilder();
				final JobMedia file = getFile();
				final Map values = new HashMap();
				values.put("filename", file.getRealFileName());
				values.put("filecode", file.getCode());
				appendLocalizedTemplateString(stringBuilder, "bmecat.infostep.processing", values);
				ctx.setStatus(stringBuilder.toString(), this.infoStep.getCompletionStatus(), false);
			}
		}
		else
		{
			finishedInfoStep(ctx);
		}
	}

	private int getTransactionModeAsInt()
	{
		if (transactionMode == -1)
		{
			throw new IllegalStateException("transaction mode not set - need to run info step first");
		}
		return transactionMode;
	}

	// /**
	// * Tries to fill missing currency and unit mappings with default mappings
	// from
	// * the {@link BMECatImportBatchJob}.
	// */
	// private void applyDefaultMappings()
	// {
	// final BMECatImportBatchJob myJob =
	// (BMECatImportBatchJob)getCronJob().getJob();
	// /*
	// * fill currency mapping with default mappings
	// */
	// final Map currencyMappings = getAllCurrencyMappings();
	// if( currencyMappings != null && !currencyMappings.isEmpty() )
	// {
	// final Map newMappings = new HashMap( currencyMappings );
	// final Map defMappings = myJob.getAllDefaultCurrencyMappings();
	// if( defMappings != null && !defMappings.isEmpty() )
	// {
	// for (Iterator it = currencyMappings.entrySet().iterator(); it.hasNext(); )
	// {
	// final Map.Entry e = (Map.Entry) it.next();
	// final String iso = (String)e.getKey();
	// if( e.getValue() == null && defMappings.get(iso) != null )
	// {
	// newMappings.put(iso,defMappings.get(iso));
	// }
	// }
	// setAllCurrencyMappings(newMappings);
	// }
	// }
	// /*
	// * fill unit mapping with default mappings
	// */
	// final Map unitMappings = getAllUnitMappings();
	// if( unitMappings != null || !unitMappings.isEmpty())
	// {
	// final Map newMappings = new HashMap( unitMappings );
	// final Map defMappings = myJob.getAllDefaultUnitMappings();
	// if( defMappings != null )
	// {
	// for (Iterator it = unitMappings.entrySet().iterator(); it.hasNext(); )
	// {
	// final Map.Entry e = (Map.Entry) it.next();
	// final String code = (String)e.getKey();
	// if( e.getValue() == null && defMappings.get(code) != null )
	// {
	// newMappings.put(code, defMappings.get(code));
	// }
	// }
	// setAllUnitMappings(newMappings);
	// }
	// }
	// }

	protected void finishedInfoStep(final WizardEditorContext ctx)
	{

		doingInfoStep = false;
		endProcessing(ctx);
		ctx.reloadAllFields(); // update GUI with info data
		// report errors if there are any
		if (!getBmeCatImportCronJob().getPausedStatus().equals(getBmeCatImportCronJob().getStatus()))
		{
			System.err.println("got unexpected status " + getBmeCatImportCronJob().getStatus() + ", expected "
					+ getBmeCatImportCronJob().getPausedStatus());
			this.error = true;
			ctx.setCurrentTab(Tabs.TAB1_FILES); // back to tab 1 !!!
			LOG.error("something went wrong analyzing bmecat file " + getBmeCatImportCronJob().getJobMedia() + " : '"
					+ getBmeCatImportCronJob().getLogText() + "'");
		}
		else
		{
			// mode
			final EnumerationValue mode = getTransactionMode();

			/*
			 * checks
			 */
			if (mode == null || BMECatConstants.Enumerations.TransactionModeEnum.T_NEW_CATALOG.equalsIgnoreCase(mode.getCode()))
			{
				transactionMode = BMECatConstants.TRANSACTION.T_NEW_CATALOG;
			}
			else if (BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRICES.equalsIgnoreCase(mode.getCode()))
			{
				transactionMode = BMECatConstants.TRANSACTION.T_UPDATE_PRICES;
				// dont show:
				// language

				// CAUTION
				// -------
				// ctx.hideField throws JaloInvalidParameterException("hideField()
				// not yet implemented", 0);

				// ctx.hideField(BMECatImportWizard.CATALOGLANGUAGEISO);
				// ctx.hideField(BMECatImportWizard.CATALOGLANGUAGE);

				// buyer
				// ctx.hideField(BMECatImportWizard.BUYERNAME);
				// ctx.hideField(BMECatImportWizard.BUYER);

				// supplier
				// ctx.hideField(BMECatImportWizard.SUPPLIERNAME);
				// ctx.hideField(BMECatImportWizard.SUPPLIER);

				// counts
				// ctx.hideField(BMECatImportWizard.CATEGORYCOUNT);
				// ctx.hideField(BMECatImportWizard.MEDIACOUNT);
				// ctx.hideField(BMECatImportWizard.KEYWORDCOUNT);
			}
			else if (BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRODUCTS.equalsIgnoreCase(mode.getCode()))
			{
				transactionMode = BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS;
			}
		}
	}

	// private void checkTab2(WizardEditorContext ctx ) throws VetoException
	// {
	// Catalog c = getCatalog();
	// CatalogVersion cv = getCatalogVersion();
	// final Language l = getCatalogLanguage();
	// final Company buyer = getBuyer();
	// final Company supplier = getSupplier();
	//
	// switch( getTransactionModeAsInt())
	// {
	// case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
	// if( l == null ) throw new VetoException("missing import language - please
	// select or create one", Errors.MISSING_LANGUAGE );
	// if( c == null && cv != null )
	// {
	// setCatalog( c = cv.getCatalog());
	// ctx.reloadField(BMECatImportWizard.CATALOG);
	// }
	//
	// if( c != null && cv != null )
	// {
	// if( !c.equals( cv.getCatalog() ) )
	// throw new VetoException("catalog version "+cv.getVersion()+" does not
	// belong to catalog "+c.getId()+" but to "+cv.getCatalog().getId(),
	// Errors.CATALOG_VERSION_MISMATCH );
	// if( cv.isImportedLanguage(l.getIsoCode()))
	// {
	// ctx.setCurrentTab(Tabs.TAB1_FILES);
	// ctx.showError(BMECatImportWizard.FILE,"catalog(version) is already
	// imported - choose another file");
	// throw new VetoException(
	// "language version "+l.getIsoCode()+" is already imported in existing
	// catalog version "+cv.getVersion()+" - cannot run",
	// Errors.LANGUAGE_ALREADY_EXISTS
	// );
	// }
	// ctx.postInfoMessage("since catalog version already exists the import will
	// update '"+l.getIsoCode()+"' localized article values only");
	// }
	//
	// if( buyer == null )
	// {
	// if( supplier == null )
	// ctx.postInfoMessage("warning: buyer and supplier not specified - import
	// will create new companies" );
	// else
	// ctx.postInfoMessage("warning: buyer not specified - import create a new
	// company" );
	// }
	// else if( supplier == null )
	// ctx.postInfoMessage("warning: supplier not specified - import will create
	// a new company" );
	//
	// break;
	//
	// case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
	// if( l == null ) throw new VetoException("missing update language",
	// Errors.MISSING_LANGUAGE );
	//
	// // TODO: check update numbers !!!
	//
	// case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
	// if( cv == null )
	// throw new VetoException(
	// "missing target catalog version - please select catalog version to
	// update",
	// Errors.MISSING_TARGET
	// );
	// if( c == null )
	// {
	// if( cv != null )
	// {
	// setCatalog( c = cv.getCatalog());
	// ctx.reloadField(BMECatImportWizard.CATALOG );
	// }
	// else
	// throw new VetoException( "missing target catalog for update" ,
	// Errors.MISSING_TARGET );
	// }
	// if( !c.equals( cv.getCatalog() ) )
	// throw new VetoException(
	// "catalog version "+cv.getVersion()+" does not belong to catalog
	// "+c.getId()+" but to "+cv.getCatalog().getId(),
	// Errors.CATALOG_VERSION_MISMATCH
	// );
	//
	// break;
	// }
	// }

	// protected void checkTab3(WizardEditorContext ctx ) throws VetoException
	// {
	// Boolean sendEmail = isSendEmail();
	// String address = getEmailAddress();
	// if( Boolean.TRUE.equals(sendEmail) && ( address == null ||
	// "".equals(address)))
	// throw new VetoException("missing email address for enabled email
	// notification -please provide one or disable notification", 33301 );
	//
	// final Map currencyMap = getAllCurrencyMappings();
	// Set unmappedCurrencies = new HashSet();
	// for( Iterator it = currencyMap.entrySet().iterator(); it.hasNext(); )
	// {
	// Map.Entry entry = (Map.Entry)it.next();
	// if( entry.getValue() == null ) unmappedCurrencies.add(entry.getKey());
	// }
	// if( !unmappedCurrencies.isEmpty() )
	// throw new VetoException("got unmapped currencies "+unmappedCurrencies+" -
	// please map to existing currencies", 33302 );
	//
	// final Map unitMap = getAllUnitMappings();
	// Set unmappedUnits = new HashSet();
	// for( Iterator it = unitMap.entrySet().iterator(); it.hasNext(); )
	// {
	// Map.Entry entry = (Map.Entry)it.next();
	// if( entry.getValue() == null ) unmappedUnits.add(entry.getKey());
	// }
	// if( !unmappedUnits.isEmpty() )
	// throw new VetoException("got unmapped units "+unmappedUnits+" - please map
	// to existing units", 33303 );
	// }

	/**
	 * Saves all current unit and currency mappings to the default mappings
	 * 
	 */
	private final void saveMappingsAsDefault()
	{
		final BMECatImportBatchJob job = (BMECatImportBatchJob) getBmeCatImportCronJob().getJob();

		final Map unitMappings = getAllUnitMappings();
		Map defaultUnits = job.getAllDefaultUnitMappings();
		defaultUnits = defaultUnits != null ? new HashMap(defaultUnits) : new HashMap();
		if (unitMappings != null)
		{
			for (final Iterator it = unitMappings.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry mapEntry = (Map.Entry) it.next();
				final String code = (String) mapEntry.getKey();
				final Unit unit = (Unit) mapEntry.getValue();
				if (unit != null && !unit.equals(defaultUnits.get(code)))
				{
					defaultUnits.put(code, unit);
				}
			}
			job.setAllDefaultUnitMappings(defaultUnits);
		}

		final Map currencyMappings = getAllCurrencyMappings();
		Map defaultCurrencies = job.getAllDefaultCurrencyMappings();
		defaultCurrencies = defaultCurrencies != null ? new HashMap(defaultCurrencies) : new HashMap();
		if (currencyMappings != null)
		{
			for (final Iterator it = currencyMappings.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry mapEntry = (Map.Entry) it.next();
				final String iso = (String) mapEntry.getKey();
				final Currency currency = (Currency) mapEntry.getValue();
				if (currency != null && !currency.equals(defaultCurrencies.get(iso)))
				{
					defaultCurrencies.put(iso, currency);
				}
			}
			job.setAllDefaultCurrencyMappings(defaultCurrencies);
		}

		// TODO: save country mappings too
	}

	private void startImport(final WizardEditorContext ctx)
	{
		saveMappingsAsDefault();
		doingImport = true;
		startProcessing(ctx);
		ctx.showButton(CANCEL_BUTTON);
		ctx.enableButton(CANCEL_BUTTON);
		ctx.setStatus(Localization.getLocalizedString("bmecat.import.start", new Object[]
		{ getFile().getRealFileName(), getFile().getCode() }), 0, false);
		getBmeCatImportCronJob().getJob().perform(getBmeCatImportCronJob(), false);
	}

	private void pollStatusImport(final WizardEditorContext ctx)
	{
		final BMECatImportCronJob importCronJob = getBmeCatImportCronJob();
		if (importCronJob.isRunning())
		{
			final Map values = new HashMap();

			final Collection pending = importCronJob.getPendingSteps();
			final Collection done = importCronJob.getProcessedSteps();
			int percentage = (100 * done.size()) / (done.size() + pending.size() + 1);
			final StringBuilder stringBuilder = new StringBuilder("</br>");
			// show processed steps
			for (final Iterator it = done.iterator(); it.hasNext();)
			{
				final BMECatImportStep importStep = (BMECatImportStep) it.next();
				values.put("step", importStep.getCode());
				appendLocalizedTemplateString(stringBuilder, "bmecat.step.done", values);
				stringBuilder.append("</br>");
			}
			final BMECatImportStep current = (BMECatImportStep) importCronJob.getCurrentStep();
			if (current != null)
			{
				stringBuilder.append("</br><b>");
				final int stepPercentage = current.getCompletionStatus(importCronJob);
				final int count = current.getCompletedCount(importCronJob);
				final int total = current.getTotalToComplete(importCronJob);
				values.put("step", current.getCode());
				values.put("percentage", stepPercentage > -1 ? String.valueOf(stepPercentage) : "?");
				values.put("count", count > -1 ? String.valueOf(count) : "?");
				values.put("total", total > -1 ? String.valueOf(total) : "?");
				appendLocalizedTemplateString(stringBuilder, "bmecat.step.processing", values);
				stringBuilder.append("</b></br>");

				percentage += (double) stepPercentage / (double) (done.size() + pending.size() + 1);
			}
			values.clear();
			for (final Iterator it = pending.iterator(); it.hasNext();)
			{
				final Step step = (Step) it.next();
				values.put("step", step.getCode());
				appendLocalizedTemplateString(stringBuilder, "bmecat.step.pending", values);
				stringBuilder.append("</br>");
			}
			// sb.append("</pre>");
			ctx.setStatus(stringBuilder.toString(), percentage, false);
		}
		else
		{
			finishedImport(ctx);
		}
	}

	protected void finishedImport(final WizardEditorContext ctx)
	{
		doingImport = false;
		done = true;
		ctx.setProcessingMode(false); // we dont show buttons again since we're
		// done right now
		ctx.showButton(CANCEL_BUTTON);
		ctx.setButtonLabel(CANCEL_BUTTON, DONE_BUTTON_NAME); // will enable
		// closing this
		// wizard
		final EnumerationValue res = getBmeCatImportCronJob().getResult();

		if (getBmeCatImportCronJob().getSuccessResult().equals(res))
		{
			final Map values = new HashMap();
			values.put("file", getBmeCatImportCronJob().getJobMedia().getRealFileName() != null ? getBmeCatImportCronJob()
					.getJobMedia().getRealFileName() : getBmeCatImportCronJob().getJobMedia().getCode());

			values.put("cronjob", getBmeCatImportCronJob().getCode() + "(PK " + getBmeCatImportCronJob().getPK().toString() + ")");
			ctx.showSummaryTab(getLocalizedTemplateString("bmecat.import.done.success", values));
		}
		else
		{
			ctx.hideTab(Tabs.TAB0_JOB);
			ctx.hideTab(Tabs.TAB1_FILES);
			ctx.hideTab(Tabs.TAB2_CATALOG_INFORMATIONS);
			ctx.hideTab(Tabs.TAB3_TARGETS_AND_MODE);
			ctx.hideTab(Tabs.TAB4_MAPPINGS);
			ctx.hideTab(Tabs.TAB5_FURTHER_SETTINGS);

			ctx.showTab(Tabs.TAB6_ERROR);
			ctx.reloadAllFields();
		}
		// TODO: check for errors
	}

	/*
	 * ------------------------------------------------------------------------------ Delegates to cronjob
	 * ------------------------------------------------------------------------------
	 */

	@Override
	public BMECatJobMedia getFile(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return (BMECatJobMedia) getBmeCatImportCronJob().getJobMedia(ctx);
	}

	@Override
	public BMECatJobMedia getMediaFile(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getMediasMedia(ctx);
	}

	@Override
	public Boolean isMediasNotIncluded(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().isReferenceMedias(ctx);
	}

	@Override
	public boolean isMediasNotIncludedAsPrimitive(final SessionContext ctx)
	{
		return Boolean.TRUE.equals(isMediasNotIncluded(ctx));
	}

	@Override
	public EnumerationValue getTransactionMode(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getTransactionMode(ctx);
	}

	@Override
	public String getCatalogName(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getCatalogID(ctx);
	}

	@Override
	public Catalog getCatalog(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getCatalog(ctx);
	}

	@Override
	public Date getCatalogDate(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getCatalogDate(ctx);
	}

	@Override
	public String getCatalogVersionName(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getCatalogVersionName(ctx);
	}

	@Override
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getCatalogVersion(ctx);
	}

	@Override
	public String getCatalogLanguageISO(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getImportLanguageIsoCode(ctx);
	}

	@Override
	public Language getCatalogLanguage(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getImportLanguage(ctx);
	}

	@Override
	public String getDefaultCurrencyISO(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getDefaultCurrencyIsoCode(ctx);
	}

	@Override
	public Currency getDefaultCurrency(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getDefaultCurrency(ctx);
	}

	/*
	 * public String getSupplierName( SessionContext ctx ) { if( getBmeCatImportCronJob() == null ) { return null; }
	 * return getBmeCatImportCronJob().getSupplierName(ctx); }
	 * 
	 * public Company getSupplier( SessionContext ctx ) { if( getBmeCatImportCronJob() == null ) { return null; } return
	 * getBmeCatImportCronJob().getSupplier(ctx); }
	 * 
	 * public Address getSupplierAddress( SessionContext ctx ) { // TODO return null; }
	 * 
	 * public String getBuyerName( SessionContext ctx ) { if( getBmeCatImportCronJob() == null ) { return null; } return
	 * getBmeCatImportCronJob().getBuyerName(ctx); }
	 * 
	 * public Company getBuyer( SessionContext ctx ) { if( getBmeCatImportCronJob() == null ) { return null; } return
	 * getBmeCatImportCronJob().getBuyer(ctx); }
	 * 
	 * public Address getBuyerAddress( SessionContext ctx ) { return null; }
	 */

	@Override
	public Integer getArticleCount(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getArticleCount(ctx);
	}

	@Override
	public int getArticleCountAsPrimitive(final SessionContext ctx)
	{
		return getBmeCatImportCronJob().getArticleCountAsPrimitive(ctx);
	}

	@Override
	public Integer getCategoryCount(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getCategoryCount(ctx);
	}

	@Override
	public int getCategoryCountAsPrimitive(final SessionContext ctx)
	{
		return getBmeCatImportCronJob().getCategoryCountAsPrimitive(ctx);
	}

	@Override
	public Integer getMediaCount(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getMimeCount(ctx);
	}

	@Override
	public int getMediaCountAsPrimitive(final SessionContext ctx)
	{
		return getBmeCatImportCronJob().getMimeCountAsPrimitive(ctx);
	}

	@Override
	public Integer getKeywordCount(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getKeywordCount(ctx);
	}

	@Override
	public int getKeywordCountAsPrimitive(final SessionContext ctx)
	{
		return getBmeCatImportCronJob().getKeywordCountAsPrimitive(ctx);
	}

	@Override
	public Boolean isEnableUndo(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().isChangeRecordingEnabled(ctx);
	}

	@Override
	public boolean isEnableUndoAsPrimitive(final SessionContext ctx)
	{
		return getBmeCatImportCronJob().isChangeRecordingEnabledAsPrimitive(ctx);
	}

	@Override
	public Boolean isSendEmail(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().isSendEmail(ctx);
	}

	@Override
	public boolean isSendEmailAsPrimitive(final SessionContext ctx)
	{
		return getBmeCatImportCronJob().isSendEmailAsPrimitive(ctx);
	}

	@Override
	public String getEmailAddress(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getEmailAddress(ctx);
	}

	@Override
	public Map<String, Currency> getAllCurrencyMappings(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getAllCurrencyMappings(ctx);
	}

	@Override
	public Map<String, ClassificationSystemVersion> getAllClassificationMappings(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getAllClassificationMappings(ctx);
	}


	@Override
	public Map<String, Unit> getAllUnitMappings(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		final Map current = getBmeCatImportCronJob().getAllUnitMappings(ctx);
		final HashMap ret = new HashMap();
		if (current != null)
		{
			Map def = ((BMECatImportBatchJob) getBmeCatImportCronJob().getJob(ctx)).getAllDefaultUnitMappings(ctx);
			if (def != null)
			{
				def = new HashMap(def);
			}
			else
			{
				def = new HashMap();
			}
			boolean saveDefaults = false;
			for (final Iterator iter = current.entrySet().iterator(); iter.hasNext();)
			{
				final Map.Entry mapEntry = (Map.Entry) iter.next();
				Unit unit = (Unit) def.get(mapEntry.getKey());
				if (unit == null && mapEntry.getValue() != null)
				{
					saveDefaults = true;
					def.put(mapEntry.getKey(), unit = (Unit) mapEntry.getValue());
				}
				ret.put(mapEntry.getKey(), unit);
			}
			if (!ret.equals(current))
			{
				getBmeCatImportCronJob().setAllUnitMappings(ctx, ret);
			}
			if (saveDefaults)
			{
				((BMECatImportBatchJob) getBmeCatImportCronJob().getJob(ctx)).setAllDefaultUnitMappings(ctx, def);
			}
			return ret;
		}
		else
		{
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	public void setFile(final SessionContext ctx, final BMECatJobMedia param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setJobMedia(ctx, param);
	}

	@Override
	public void setMediaFile(final SessionContext ctx, final BMECatJobMedia param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setMediasMedia(ctx, param);
	}

	@Override
	public void setMediasNotIncluded(final SessionContext ctx, final Boolean param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}

		final Boolean mediasNotIncluded;
		if (param == null)
		{
			mediasNotIncluded = Boolean.FALSE;
		}
		else
		{
			mediasNotIncluded = param;
		}
		getBmeCatImportCronJob().setReferenceMedias(ctx, mediasNotIncluded);
	}

	@Override
	public void setCatalog(final SessionContext ctx, final Catalog param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setCatalog(ctx, param);
	}

	@Override
	public void setCatalogVersion(final SessionContext ctx, final CatalogVersion param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setCatalogVersion(ctx, param);
	}

	@Override
	public void setCatalogLanguage(final SessionContext ctx, final Language param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setImportLanguage(ctx, param);
	}

	@Override
	public void setDefaultCurrency(final SessionContext ctx, final Currency param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setDefaultCurrency(ctx, param);
	}

	/*
	 * public void setSupplier( SessionContext ctx, Company param ) { if( getBmeCatImportCronJob() == null ) return;
	 * getBmeCatImportCronJob().setSupplier(ctx,param); }
	 * 
	 * public void setSupplierAddress( SessionContext ctx, Address param ) { if( getBmeCatImportCronJob() == null )
	 * return; }
	 * 
	 * public void setBuyer( SessionContext ctx, Company param ) { if( getBmeCatImportCronJob() == null ) return;
	 * getBmeCatImportCronJob().setBuyer(ctx,param); }
	 * 
	 * public void setBuyerAddress( SessionContext ctx, Address param ) { if( getBmeCatImportCronJob() == null ) return;
	 * }
	 */
	@Override
	public void setEnableUndo(final SessionContext ctx, final Boolean param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setChangeRecordingEnabled(ctx, param);
	}

	@Override
	public void setEnableUndo(final SessionContext ctx, final boolean param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setChangeRecordingEnabled(ctx, param);
	}

	@Override
	public void setSendEmail(final SessionContext ctx, final Boolean param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setSendEmail(ctx, param);
	}

	@Override
	public void setSendEmail(final SessionContext ctx, final boolean param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setSendEmail(ctx, param);
	}

	@Override
	public void setEmailAddress(final SessionContext ctx, final String param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setEmailAddress(ctx, param);
	}

	@Override
	public void setAllCurrencyMappings(final SessionContext ctx, final Map<String, Currency> param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setAllCurrencyMappings(ctx, param);
	}

	@Override
	public void setAllClassificationMappings(final SessionContext ctx, final Map<String, ClassificationSystemVersion> param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setAllClassificationMappings(ctx, param);
	}

	@Override
	public void setAllUnitMappings(final SessionContext ctx, final Map<String, Unit> param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		if (param != null)
		{
			Map defaults = ((BMECatImportBatchJob) getBmeCatImportCronJob().getJob(ctx)).getAllDefaultUnitMappings(ctx);
			if (defaults == null)
			{
				defaults = new HashMap();
			}
			else
			{
				defaults = new HashMap(defaults);
			}
			boolean save = false;
			for (final Iterator it = param.entrySet().iterator(); it.hasNext();)
			{
				final Map.Entry mapEntry = (Map.Entry) it.next();
				if (mapEntry.getValue() != null)
				{
					save |= !mapEntry.getValue().equals(defaults.put(mapEntry.getKey(), mapEntry.getValue()));
				}
			}
			if (save)
			{
				((BMECatImportBatchJob) getBmeCatImportCronJob().getJob(ctx)).setAllDefaultUnitMappings(ctx, defaults);
			}
		}
		getBmeCatImportCronJob().setAllUnitMappings(ctx, param);
	}

	@Override
	public Integer getCategoryAssignmentsCount(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getCategoryAssignmentsCount(ctx);
	}

	@Override
	public int getCategoryAssignmentsCountAsPrimitive(final SessionContext ctx)
	{
		return getBmeCatImportCronJob().getCategoryAssignmentsCountAsPrimitive(ctx);
	}

	@Override
	public BMECatImportBatchJob getImportJob(final SessionContext ctx)
	{
		return bmeCatImportBatchJob;
	}

	@Override
	public void setImportJob(final SessionContext ctx, final BMECatImportBatchJob bmeCatImportBatchJob)
	{
		this.bmeCatImportBatchJob = bmeCatImportBatchJob;
	}

	@Override
	public Boolean isLocalizationUpdate(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().isLocalizationUpdate(ctx);
	}

	@Override
	public void setLocalizationUpdate(final SessionContext ctx, final Boolean param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}

		getBmeCatImportCronJob().setLocalizationUpdate(ctx, param);
	}

	@Override
	public BMECatImportCronJob getBmeCatImportCronJob(final SessionContext ctx)
	{
		if (this.cronJob == null && getImportJob() != null)
		{
			this.cronJob = BMECatManager.getInstance().createDefaultBMECatImportCronJob(null, getImportJob());
			/*
			 * XXX: hack to remove generated job media
			 */
			if (this.cronJob.getJobMedia() != null)
			{
				final JobMedia media = this.cronJob.getJobMedia();
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

	/**
	 * @see de.hybris.platform.hmc.jalo.WizardBusinessItem#initialize(de.hybris.platform.hmc.jalo.WizardEditorContext)
	 */
	@Override
	public void initialize(final WizardEditorContext ctx)
	{
		super.initialize(ctx);
		ctx.hideTab(Tabs.TAB6_ERROR);
	}

	@Override
	public Map<EnumerationValue, EnumerationValue> getAllPriceTypeMappings(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getAllPriceTypeMappings(ctx);
	}

	@Override
	public Map<String, EnumerationValue> getAllTaxTypeMappings(final SessionContext ctx)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return null;
		}
		return getBmeCatImportCronJob().getAllTaxTypeMappings(ctx);
	}

	@Override
	public void setAllTaxTypeMappings(final SessionContext ctx, final Map<String, EnumerationValue> param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setAllTaxTypeMappings(ctx, param);
	}

	@Override
	public void setAllPriceTypeMappings(final SessionContext ctx, final Map<EnumerationValue, EnumerationValue> param)
	{
		if (getBmeCatImportCronJob() == null)
		{
			return;
		}
		getBmeCatImportCronJob().setAllPriceTypeMappings(ctx, param);
	}

	@Override
	public Collection<String> getTerritories(final SessionContext ctx)
	{
		return getBmeCatImportCronJob() != null ? getBmeCatImportCronJob().getTerritories(ctx) : null;
	}
}

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
import de.hybris.platform.bmecat.jalo.BMECatExportCronJob;
import de.hybris.platform.bmecat.jalo.BMECatJobMedia;
import de.hybris.platform.bmecat.jalo.BMECatManager;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.JobMedia;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.hmc.jalo.ValidationException;
import de.hybris.platform.hmc.jalo.VetoException;
import de.hybris.platform.hmc.jalo.WizardEditorContext;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * 
 */
public class BMECatExportWizard extends GeneratedBMECatExportWizard
{
	private final static String TAB_ESSENTIALS = "tab.bmecatexportwizard.essentials";
	private final static String TAB_EXPORTED_PRODUCT_SIZE = "tab.bmecatexportwizard.exportedproduct_size";
	private final static String TAB_CLASSIFICATIONSYSTEMS = "tab.bmecatexportwizard.classificationsystems";
	private final static String TAB_MEDIAS_PRICES = "tab.bmecatexportwizard.medias_prices";
	private final static String TAB_SUMMARY = "tab.bmecatexportwizard.summary";
	private final static String TAB_ERROR = "tab.bmecatexportwizard.error";

	private EnumerationValue BMECAT_NORMAL;
	private EnumerationValue BMECAT_THUMBNAIL;
	private EnumerationValue BMECAT_LOGO;
	private EnumerationValue BMECAT_OTHERS;
	private EnumerationValue BMECAT_DATA_SHEET;
	private EnumerationValue BMECAT_DETAIL;

	private EnumerationValue HYBRIS_PICTURE;
	private EnumerationValue HYBRIS_THUMBNAIL;
	private EnumerationValue HYBRIS_LOGO;
	private EnumerationValue HYBRIS_OTHERS;
	private EnumerationValue HYBRIS_DATA_SHEET;
	private EnumerationValue HYBRIS_DETAIL;

	private static final Logger LOG = Logger.getLogger(BMECatExportWizard.class.getName());

	private BMECatExportCronJob exportCronJob;
	private boolean started = false;
	private Currency exportCurrency = null;

	@Override
	public CatalogVersion getExportCatalogVersion(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).getExportCatalogVersion(ctx);
	}

	public EnumerationValue getTransactionMode(final SessionContext ctx)
	{
		return getBMECatManager().getTransactionModeEnum(BMECatConstants.TRANSACTION.T_NEW_CATALOG);
	}

	@Override
	public Collection<ClassificationSystemVersion> getClassificationSystemVersions(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).getClassificationSystemVersions(ctx);
	}

	@Override
	public String getClassificationSystemNumberFormat(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).getClassificationSystemNumberFormat(ctx);
	}

	@Override
	public Map<EnumerationValue, EnumerationValue> getAllHybris2BMECatMimePurposeMapping(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).getAllHybris2BMECatMimePurposeMapping(ctx);
	}

	@Override
	public BMECatJobMedia getExportedMedias(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).getExportedMedias(ctx);
	}

	@Override
	public void setExportCatalogVersion(final SessionContext ctx, final CatalogVersion version)
	{
		final CatalogVersion old = getExportCatalogVersion(ctx);
		if (old != version && (old == null || !old.equals(version)))
		{
			getBmeCatExportCronJob(ctx).setExportCatalogVersion(ctx, version);
			// set currency to default catalog currency if version changes
			if (version != null && version.getCatalog(ctx).getDefaultCurrency(ctx) != null)
			{
				setExportCurrency(ctx, version.getCatalog(ctx).getDefaultCurrency(ctx));
			}
		}
	}

	@Override
	public Integer getExportedProductSize(final SessionContext ctx)
	{
		if (getExportCatalogVersion() != null)
		{
			final User currentUser = ctx.getUser();
			ctx.setUser(getReferenceCustomer());
			final List<CatalogVersion> versions = new ArrayList<CatalogVersion>();
			versions.add(getExportCatalogVersion());
			CatalogManager.getInstance().setSessionCatalogVersions(ctx, versions);
			final String query = "SELECT {PK} FROM {PRODUCT}";
			final List result = FlexibleSearch.getInstance().search(ctx, query, null, Product.class).getResult();
			ctx.setUser(currentUser);
			return Integer.valueOf(result.size());
		}
		else
		{
			return Integer.valueOf(0);
		}
	}

	@Override
	public Integer getExistedProductSize(final SessionContext ctx)
	{
		if (getExportCatalogVersion() != null)
		{
			return Integer.valueOf(getExportCatalogVersion().getAllProducts().size());
		}
		else
		{
			return Integer.valueOf(0);
		}
	}

	public void setTransactionMode(final SessionContext ctx, final EnumerationValue mode)
	{
		getBmeCatExportCronJob(ctx).setTransactionMode(ctx, mode);
	}

	@Override
	public void setClassificationSystemVersions(final SessionContext ctx, final Collection param)
	{
		getBmeCatExportCronJob(ctx).setClassificationSystemVersions(ctx, param);
	}

	@Override
	public void setAllHybris2BMECatMimePurposeMapping(final SessionContext ctx, final Map<EnumerationValue, EnumerationValue> param)
	{
		getBmeCatExportCronJob(ctx).setAllHybris2BMECatMimePurposeMapping(ctx, param);
	}

	@Override
	public void setExportedMedias(final SessionContext ctx, final BMECatJobMedia medias)
	{
		getBmeCatExportCronJob(ctx).setExportedMedias(ctx, medias);
	}

	private BMECatManager getBMECatManager()
	{
		return BMECatManager.getInstance();
	}

	/**
	 * Remove the underlying export cronjob if it was ended successfully. If the export cronjob is still running it tries
	 * to stop it and removes it too.
	 */
	@Override
	public void remove(final SessionContext ctx) throws ConsistencyCheckException
	{
		if (exportCronJob != null)
		{
			if (!started || exportCronJob.isRunning())
			{
				try
				{
					exportCronJob.tryToStop(45 * 1000);
					if (!exportCronJob.isRunning())
					{
						exportCronJob.remove(ctx);
						exportCronJob = null;
					}
					else
					{
						LOG.warn("export cronjob " + exportCronJob + " still running after 45s - cannot remove");
					}
				}
				catch (final JaloInvalidParameterException e)
				{
					// cronjob does not support abort!
					LOG.warn("export cronjob " + exportCronJob + " does not support aborting : " + e);
				}
			}
			else
			{
				// only remove successfully finished cronjobs, keep errors
				if (exportCronJob.getSuccessResult().equals(exportCronJob.getResult()))
				{
					exportCronJob.remove(ctx);
					exportCronJob = null;
				}
				else
				{
					LOG.info("export cronjob " + exportCronJob + " got errors - will not remove");
				}
			}
		}
		super.remove(ctx);
	}

	protected void checkBeforeStart(final WizardEditorContext ctx) throws VetoException
	{
		final Map priceMappings = getAllPriceTypeMapping();
		if (priceMappings == null || priceMappings.isEmpty())
		{
			ctx.showError(PRICETYPEMAPPING, "at least one price type must be specified");
			throw new VetoException("at least one price type must be secified", 0);
		}
		else
		{
			ctx.clearError(PRICETYPEMAPPING);
		}
		if (getExportCurrency() == null)
		{
			ctx.showError(EXPORTCURRENCY, "export currency must be specified");
			throw new VetoException("export currency must be specified", 0);
		}
		else
		{
			ctx.clearError(EXPORTCURRENCY);
		}
	}

	protected void checkAfterEssentials(final WizardEditorContext ctx) throws VetoException
	{
		if (getExportCatalogVersion() == null)
		{
			ctx.showError(EXPORTCATALOGVERSION, "export catalog version must be specified");
			throw new VetoException("export catalog version must be specified", 0);
		}
		else
		{
			ctx.clearError(EXPORTCATALOGVERSION);
		}
		final List selectedLanguages = ctx.getSelectedValues(EXPORTLANGUAGES);
		if (selectedLanguages == null || selectedLanguages.isEmpty())
		{
			ctx.showError(EXPORTLANGUAGES, "at least one export language must be selected");
			throw new VetoException("at least one export language must be selected", 0);
		}
		else
		{
			ctx.clearError(EXPORTLANGUAGES);
		}
	}

	@Override
	public void start(final WizardEditorContext ctx) throws VetoException
	{
		checkBeforeStart(ctx);

		startProcessing(ctx);
		ctx.showButton(CANCEL_BUTTON);
		ctx.enableButton(CANCEL_BUTTON);
		ctx.setStatus("starting export of catalog version: " + getExportCatalogVersion().getVersion() + "...", 0, false);
		getBmeCatExportCronJob().getJob().perform(getBmeCatExportCronJob(), false);
		started = true;
	}

	/**
	 * @see de.hybris.platform.hmc.jalo.WizardBusinessItem#pollStatus(de.hybris.platform.hmc.jalo.WizardEditorContext)
	 */
	@Override
	public void pollStatus(final WizardEditorContext ctx)
	{
		if (getBmeCatExportCronJob().isRunning())
		{
			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(getBmeCatExportCronJob().getCompletedLanguageCount()).append(" von ")
					.append(getBmeCatExportCronJob().getTotalLanguagesCount()).append(" Sprachen komplett.<br/>");
			stringBuilder.append(getBmeCatExportCronJob().getCurrentItemsCount()).append(" von ")
					.append(getBmeCatExportCronJob().getTotalItemsCount()).append(" Items f&uuml;r aktuelle Sprache exportiert.");

			ctx.setStatus(stringBuilder.toString(), getBmeCatExportCronJob().getPercentage(), false);
		}
		else if (getBmeCatExportCronJob().isFinished()
				|| getBmeCatExportCronJob().getAbortedStatus().equals(getBmeCatExportCronJob().getStatus()))
		{
			ctx.setProcessingMode(false); // we dont show buttons again since we're done right now
			ctx.showButton(CANCEL_BUTTON);
			ctx.setButtonLabel(CANCEL_BUTTON, DONE_BUTTON_NAME); // will enable closing this wizard

			final EnumerationValue result = getBmeCatExportCronJob().getResult();

			if (getBmeCatExportCronJob().getSuccessResult().equals(result))
			{
				ctx.hideTab(TAB_ESSENTIALS);
				ctx.hideTab(TAB_EXPORTED_PRODUCT_SIZE);
				ctx.hideTab(TAB_CLASSIFICATIONSYSTEMS);
				ctx.hideTab(TAB_MEDIAS_PRICES);
				ctx.showTab(TAB_SUMMARY);
				ctx.reloadAllFields();
			}
			else
			{
				final Map values = new HashMap();
				values.put("version", getExportCatalogVersion().getVersion());
				values.put("cronjob", getBmeCatExportCronJob().getCode());

				//ctx.showSummaryTab( getLocalizedTemplateString( "bmecat.export.done.error", values ) );

				ctx.hideTab(TAB_ESSENTIALS);
				ctx.hideTab(TAB_CLASSIFICATIONSYSTEMS);
				ctx.hideTab(TAB_MEDIAS_PRICES);

				ctx.showTab(TAB_ERROR);
				ctx.reloadAllFields();

			}

		}
	}

	/**
	 * @see de.hybris.platform.hmc.jalo.WizardBusinessItem#tabChanges(de.hybris.platform.hmc.jalo.WizardEditorContext,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void tabChanges(final WizardEditorContext ctx, final String fromTabName, final String toTabName) throws VetoException
	{
		if (TAB_MEDIAS_PRICES.equals(toTabName))
		{
			ctx.enableButton(START_BUTTON);
		}
		else if (TAB_ESSENTIALS.equals(fromTabName))
		{
			checkAfterEssentials(ctx);
			getBmeCatExportCronJob().setExportLanguages(new HashSet(ctx.getSelectedValues(EXPORTLANGUAGES)));
			presetCurrency(ctx); // preset currency if not already set
		}
	}

	protected void presetCurrency(final WizardEditorContext ctx)
	{
		if (getExportCurrency() == null)
		{
			final C2LManager c2LManager = C2LManager.getInstance();
			Currency curr = c2LManager.getBaseCurrency();
			if (curr == null)
			{
				try
				{
					curr = c2LManager.getCurrencyByIsoCode("EUR");
				}
				catch (final JaloItemNotFoundException e)
				{
					// DOCTODO Document reason, why this block is empty
				}
			}
			if (curr == null)
			{
				curr = c2LManager.getAllCurrencies().iterator().next();
			}
			setExportCurrency(curr);
			ctx.reloadField(EXPORTCURRENCY);
		}
	}

	/*
	 * @see de.hybris.platform.hmc.jalo.WizardBusinessItem#initialize(de.hybris.platform.hmc.jalo.WizardEditorContext)
	 */
	@Override
	public void initialize(final WizardEditorContext ctx)
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		ctx.hideTab(TAB_SUMMARY);
		ctx.hideTab(TAB_ERROR);
		setExportLanguages(C2LManager.getInstance().getAllLanguages());
		ctx.reloadField(EXPORTLANGUAGES);
		ctx.setSelectedValues(EXPORTLANGUAGES, Collections.singletonList(jSession.getSessionContext().getLanguage()));

		//price mapping
		final EnumerationValue netListPriceType = jSession.getEnumerationManager().getEnumerationValue(
				BMECatConstants.TC.BMECATPRICETYPEENUM, BMECatConstants.Enumerations.BMECatPriceTypeEnum.NET_LIST);
		final EnumerationValue referenceCustomerPriceGroup;
		try
		{
			referenceCustomerPriceGroup = Europe1PriceFactory.getInstance().getUPG(jSession.getSessionContext(),
					getReferenceCustomer());
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new JaloSystemException(e);
		}
		final Map priceMapping = Collections.singletonMap(netListPriceType, referenceCustomerPriceGroup);
		setAllPriceTypeMapping(priceMapping);
		ctx.reloadField(PRICETYPEMAPPING);
	}

	protected Map createHybris2BMECatDefaultMediaMapping(final ComposedType targetType)
	{
		final Map attributeMapping = new HashMap();
		initializeMimePurposeEnums();
		attributeMapping.put(HYBRIS_DATA_SHEET, BMECAT_DATA_SHEET);
		attributeMapping.put(HYBRIS_PICTURE, BMECAT_NORMAL);
		attributeMapping.put(HYBRIS_THUMBNAIL, BMECAT_THUMBNAIL);
		attributeMapping.put(HYBRIS_LOGO, BMECAT_LOGO);
		attributeMapping.put(HYBRIS_OTHERS, BMECAT_OTHERS);
		attributeMapping.put(HYBRIS_DETAIL, BMECAT_DETAIL);
		return attributeMapping;
	}

	private void initializeMimePurposeEnums()
	{
		EnumerationType eType = getSession().getEnumerationManager().getEnumerationType(BMECatConstants.TC.BMECATMIMEPURPOSEENUM);
		BMECAT_DATA_SHEET = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.DATA_SHEET);
		BMECAT_NORMAL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.NORMAL);
		BMECAT_LOGO = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.LOGO);
		BMECAT_THUMBNAIL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.THUMBNAIL);
		BMECAT_OTHERS = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.OTHERS);
		BMECAT_DETAIL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.BMECatMimePurposeEnum.DETAIL);

		eType = getSession().getEnumerationManager().getEnumerationType(BMECatConstants.TC.HYBRISMIMEPURPOSEENUM);
		HYBRIS_DATA_SHEET = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.HybrisMimePurposeEnum.DATA_SHEET);
		HYBRIS_PICTURE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.HybrisMimePurposeEnum.PICTURE);
		HYBRIS_LOGO = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.HybrisMimePurposeEnum.LOGO);
		HYBRIS_THUMBNAIL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.HybrisMimePurposeEnum.THUMBNAIL);
		HYBRIS_OTHERS = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.HybrisMimePurposeEnum.OTHERS);
		HYBRIS_DETAIL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.HybrisMimePurposeEnum.DETAIL);

	}

	@Override
	public BMECatExportCronJob getBmeCatExportCronJob(final SessionContext ctx)
	{
		if (exportCronJob == null)
		{
			final Map hybris2bmecat = createHybris2BMECatDefaultMediaMapping(TypeManager.getInstance()
					.getComposedType(Product.class));

			final Map params = new HashMap();
			params.put(CronJob.JOB, getBMECatManager().getOrCreateBMECatExportJob());
			params.put(CronJob.ACTIVE, Boolean.TRUE);
			params.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE);
			params.put(BMECatExportCronJob.HYBRIS2BMECATMIMEPURPOSEMAPPING, hybris2bmecat);
			exportCronJob = getBMECatManager().createBMECatExportCronJob(ctx, params);
		}
		return exportCronJob;
	}

	@Override
	public JobMedia getJobMedia(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).getJobMedia(ctx);
	}

	@Override
	public Map<EnumerationValue, EnumerationValue> getAllPriceTypeMapping(final SessionContext ctx)
	{
		final Map ret = getBmeCatExportCronJob(ctx).getAllPriceTypeMapping(ctx);
		return ret;
	}

	@Override
	public void setAllPriceTypeMapping(final SessionContext ctx, final Map<EnumerationValue, EnumerationValue> param)
	{
		getBmeCatExportCronJob(ctx).setAllPriceTypeMapping(ctx, param);
	}

	@Override
	public Boolean isUdpNet(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).isUdpNet(ctx);
	}

	@Override
	public Date getReferenceDate(final SessionContext ctx)
	{
		Date date = getBmeCatExportCronJob(ctx).getReferenceDate(ctx);
		if (date == null)
		{
			getBmeCatExportCronJob(ctx).setReferenceDate(ctx, date = new Date());
		}
		return date;
	}

	/**
	 * Always return (and write back) <i> anonymous customer </i>m if value was null.
	 */
	@Override
	public Customer getReferenceCustomer(final SessionContext ctx)
	{
		Customer customer = getBmeCatExportCronJob(ctx).getReferenceCustomer(ctx);
		if (customer == null)
		{
			getBmeCatExportCronJob(ctx).setReferenceCustomer(ctx, customer = UserManager.getInstance().getAnonymousCustomer());
		}
		return customer;
	}

	@Override
	public void setUdpNet(final SessionContext ctx, final Boolean param)
	{
		getBmeCatExportCronJob(ctx).setUdpNet(ctx, param);
	}

	@Override
	public void setReferenceDate(final SessionContext ctx, final Date date)
	{
		getBmeCatExportCronJob(ctx).setReferenceDate(ctx, date == null ? new Date() : date);
	}

	/**
	 * Always set <i> anonymous customer </i>m if value is null.
	 */
	@Override
	public void setReferenceCustomer(final SessionContext ctx, Customer customer)
	{
		if (customer == null)
		{
			customer = UserManager.getInstance().getAnonymousCustomer();
		}
		getBmeCatExportCronJob(ctx).setReferenceCustomer(ctx, customer);
	}

	@Override
	public void setExportCurrency(final SessionContext ctx, final Currency param)
	{
		getBmeCatExportCronJob(ctx).setSessionCurrency(ctx, this.exportCurrency = param);
	}

	@Override
	public void setClassificationSystemNumberFormat(final SessionContext ctx, final String param)
	{
		getBmeCatExportCronJob(ctx).setClassificationSystemNumberFormat(ctx, param);
	}

	@Override
	public Currency getExportCurrency(final SessionContext ctx)
	{
		return this.exportCurrency;
	}

	@Override
	public Boolean isSuppressEmptyCategories(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).isSuppressEmptyCategories(ctx);
	}

	@Override
	public void setSuppressEmptyCategories(final SessionContext ctx, final Boolean param)
	{
		getBmeCatExportCronJob(ctx).setSuppressEmptyCategories(ctx, param);
	}

	@Override
	public Boolean isSuppressProductsWithoutPrice(final SessionContext ctx)
	{
		return getBmeCatExportCronJob(ctx).isSuppressProductsWithoutPrice(ctx);
	}

	@Override
	public void setSuppressProductsWithoutPrice(final SessionContext ctx, final Boolean param)
	{
		getBmeCatExportCronJob(ctx).setSuppressProductsWithoutPrice(ctx, param);
	}

	/**
	 * @see de.hybris.platform.hmc.jalo.WizardBusinessItem#validate(de.hybris.platform.hmc.jalo.WizardEditorContext,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void validate(final WizardEditorContext ctx, final String fromTab, final String toTab) throws ValidationException
	{
		super.validate(ctx, fromTab, toTab);
		if (TAB_ESSENTIALS.equals(ctx.getCurrentTab()))
		{
			final CatalogVersion version = (CatalogVersion) ctx.getCurrentValue(EXPORTCATALOGVERSION);
			if (version == null)
			{
				throw new ValidationException(
						Localization.getLocalizedString("exception.bmecat.export.wizard.version_not_specified"), 25);
			}

			/*
			 * Company supplier = version.getCatalog().getSupplier(); if( supplier == null ) { throw new
			 * ValidationException(
			 * Localization.getLocalizedString("exception.bmecat.export.wizard.supplier_not_specified"), 25 ); //
			 * CompanyTagWriter exports a default 'Supplier', yet }
			 */
		}
	}
}

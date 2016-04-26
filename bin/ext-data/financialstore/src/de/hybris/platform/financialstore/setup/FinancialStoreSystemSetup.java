/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.financialstore.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.financialstore.constants.FinancialStoreConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


@SystemSetup(extension = FinancialStoreConstants.EXTENSIONNAME)
public class FinancialStoreSystemSetup extends AbstractSystemSetup
{
	public static final String INSURANCE = "insurance";

	public static final String FINANCIAL = "financial";

	public static final String IMPORT_CORE_DATA = "importCoreData";
	public static final String IMPORT_SAMPLE_DATA = "importSampleData";
	public static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;

	@SystemSetupParameterMethod
	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}

	/**
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/financialstore/import/coredata/productCatalogs/financialProductCatalog/billingevent.impex", true);
		importImpexFile(context, "/financialstore/import/coredata/productCatalogs/financialProductCatalog/billingevent_en.impex",
				true);
		importImpexFile(context, "/financialstore/import/coredata/productCatalogs/financialProductCatalog/billingfrequency.impex",
				true);
		importImpexFile(context,
				"/financialstore/import/coredata/productCatalogs/financialProductCatalog/billingfrequency_en.impex", true);
		importImpexFile(context,
				"/financialstore/import/coredata/productCatalogs/financialProductCatalog/classificationsystem.impex", true);
		importImpexFile(context,
				"/financialstore/import/coredata/productCatalogs/financialProductCatalog/classificationsystem_en.impex", true);
		importImpexFile(context, "/financialstore/import/coredata/common/searchrestriction.impex", true);
		importImpexFile(context, "/financialstore/import/coredata/common/delivery-modes.impex", true);
	}


	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData insuranceImportData = new ImportData();
		insuranceImportData.setProductCatalogName(FINANCIAL);
		insuranceImportData.setContentCatalogNames(Arrays.asList(FINANCIAL));
		insuranceImportData.setStoreNames(Arrays.asList(INSURANCE));
		importData.add(insuranceImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

		getSampleDataImportService().execute(this, context, importData);

		importImpexFile(context, "/financialstore/import/sampledata/productCatalogs/financialProductCatalog/catalog-sync.impex",
				true);

		importImpexFile(context, "/financialstore/import/sampledata/productCatalogs/financialProductCatalog/billingplans.impex",
				true);
		importImpexFile(context, "/financialstore/import/sampledata/productCatalogs/financialProductCatalog/billingplans_en.impex",
				true);

		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionterms.impex", true);
		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionterms_en.impex", true);

		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionproducts.impex", true);
		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionproducts_en.impex", true);

		importImpexFile(context, "/financialstore/import/sampledata/productCatalogs/financialProductCatalog/bundletemplates.impex",
				true);
		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/bundletemplates_en.impex", true);
		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/bundletemplates-selectioncriteria.impex",
				true);

		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/bundletemplates-disablerules.impex", true);
		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/bundletemplates-pricerules.impex", true);

		importImpexFile(
				context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionproducts-classifications_en.impex",
				true);

		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionproducts-prices.impex", true);

		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionproducts-prices_en.impex",
				true);

		importImpexFile(context,
				"/financialstore/import/sampledata/productCatalogs/financialProductCatalog/subscriptionproducts-media.impex", true);

		importImpexFile(context, "/financialstore/import/sampledata/contentCatalogs/financialContentCatalog/forms-content.impex",
				true);

		importImpexFile(context,
				"/financialstore/import/sampledata/contentCatalogs/financialContentCatalog/insurance-cms-content.impex", true);

		importImpexFile(context,
				"/financialstore/import/sampledata/contentCatalogs/financialContentCatalog/insurance-cms-content_en.impex", true);

        importImpexFile(context, "/financialstore/import/sampledata/stores/insurance/agent.impex", true);

		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		if (this.getBooleanSystemSetupParameter(context, ACTIVATE_SOLR_CRON_JOBS))
		{
			this.logInfo(context, String.format("Activating solr index for [%s]", INSURANCE));
            getSampleDataImportService().runSolrIndex(context.getExtensionName(), INSURANCE);
		}
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}

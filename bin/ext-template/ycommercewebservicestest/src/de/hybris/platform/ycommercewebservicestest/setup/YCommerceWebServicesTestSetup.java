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
package de.hybris.platform.ycommercewebservicestest.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.ycommercewebservicestest.constants.YcommercewebservicestestConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SystemSetup(extension = YcommercewebservicestestConstants.EXTENSIONNAME)
public class YCommerceWebServicesTestSetup extends AbstractSystemSetup
{
	public static final String WS_INTEGRATION = "wsIntegrationTest";
	public static final String WS_TEST = "wsTest";
	private static final Logger LOG = Logger.getLogger(YCommerceWebServicesTestSetup.class);
	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;

	@Override
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(CoreDataImportService.IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(SampleDataImportService.IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(CoreDataImportService.ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));

		return params;
	}

	@SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<ImportData>();

		final ImportData wsIntegrationImportData = new ImportData();
		wsIntegrationImportData.setProductCatalogName(WS_TEST);
		wsIntegrationImportData.setContentCatalogNames(Arrays.asList(WS_TEST, WS_INTEGRATION));
		wsIntegrationImportData.setStoreNames(Arrays.asList(WS_TEST, WS_INTEGRATION));
		importData.add(wsIntegrationImportData);

		getCoreDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new CoreDataImportedEvent(context, importData));

		loadOptionalSampleData();

		getSampleDataImportService().execute(this, context, importData);
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));

		getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/sampledata/user-orders.impex", true, false);
		loadIntegrationData();
        loadMultiDimensionalData();
	}

	protected void loadOptionalSampleData()
	{
		final List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
		if (extensionNames.contains("acceleratorwebservicesaddon"))
		{
			getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/acceleratorwebservicesaddon/solr.impex", true,
					true);
		}
	}

	protected void loadIntegrationData()
	{
		final List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();
		if (extensionNames.contains("cissampledata"))
		{
			getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/integration/cis-integration-data.impex", true);
		}

		if (extensionNames.contains("omssampledata"))
		{
			getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/integration/oms-integration-data.impex", true);
		}
	}

    protected void loadMultiDimensionalData()
    {
        getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-categories.impex", true, false);
        getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-products.impex", true, false);
        getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-products-prices.impex", true, false);
        getSetupImpexService().importImpexFile("/ycommercewebservicestest/import/sampledata/productCatalogs/wsTestProductCatalog/dimension-products-stock-levels.impex", true, false);

        getSetupSyncJobService().executeCatalogSyncJob(String.format("%sProductCatalog", WS_TEST));
        getSetupSolrIndexerService().executeSolrIndexerCronJob(String.format("%sIndex", WS_TEST), true);
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

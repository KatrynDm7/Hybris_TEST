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
package de.hybris.platform.commerceservices.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.AbstractDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;

import java.util.List;


public class SampleDataImportService extends AbstractDataImportService
{
	public static final String IMPORT_SAMPLE_DATA = "importSampleData";

	public static final String BTG_EXTENSION_NAME = "btg";
	public static final String CMS_COCKPIT_EXTENSION_NAME = "cmscockpit";
	public static final String PRODUCT_COCKPIT_EXTENSION_NAME = "productcockpit";
	public static final String CS_COCKPIT_EXTENSION_NAME = "cscockpit";
	public static final String REPORT_COCKPIT_EXTENSION_NAME = "reportcockpit";
	public static final String MCC_EXTENSION_NAME = "mcc";

	@Override
	public void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context, final List<ImportData> importData)
	{
		final boolean importSampleData = systemSetup.getBooleanSystemSetupParameter(context, IMPORT_SAMPLE_DATA);

		if (importSampleData)
		{
			for (final ImportData data : importData)
			{
				importAllData(systemSetup, context, data, true);
			}
		}
	}

	@Override
	protected void importCommonData(final String extensionName)
	{
		if (isExtensionLoaded(CMS_COCKPIT_EXTENSION_NAME))
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/sampledata/cockpits/cmscockpit/cmscockpit-users.impex", extensionName), false);
		}

		if (isExtensionLoaded(PRODUCT_COCKPIT_EXTENSION_NAME))
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/sampledata/cockpits/productcockpit/productcockpit-users.impex", extensionName), false);
		}

		if (isExtensionLoaded(CS_COCKPIT_EXTENSION_NAME))
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/sampledata/cockpits/cscockpit/cscockpit-users.impex", extensionName), false);
		}

		if (isExtensionLoaded(REPORT_COCKPIT_EXTENSION_NAME))
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/sampledata/cockpits/reportcockpit/reportcockpit-users.impex", extensionName), false);

			if (isExtensionLoaded(MCC_EXTENSION_NAME))
			{
				getSetupImpexService().importImpexFile(
						String.format("/%s/import/sampledata/cockpits/reportcockpit/reportcockpit-mcc-links.impex", extensionName),
						false);
			}
		}
	}

	@Override
	protected void importProductCatalog(final String extensionName, final String productCatalogName)
	{
		// Load Units
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/classifications-units.impex", extensionName,
						productCatalogName), false);

		// Load Categories
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories.impex", extensionName,
						productCatalogName), false);

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-classifications.impex",
						extensionName, productCatalogName), false);

		// Load Suppliers
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/suppliers.impex", extensionName,
						productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/suppliers-media.impex", extensionName,
						productCatalogName), false);

		// Load medias for Categories as Suppliers loads some new Categories
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-media.impex", extensionName,
						productCatalogName), false);

		// Load Products
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products.impex", extensionName,
						productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-media.impex", extensionName,
						productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-classifications.impex", extensionName,
						productCatalogName), false);

		// Load Products Relations
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-relations.impex", extensionName,
						productCatalogName), false);

		// Load Products Fixes
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-fixup.impex", extensionName,
						productCatalogName), false);

		// Load Prices
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-prices.impex", extensionName,
						productCatalogName), false);

		// Load Stock Levels
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-stocklevels.impex", extensionName,
						productCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-pos-stocklevels.impex", extensionName,
						productCatalogName), false);

		// Load Taxes
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-tax.impex", extensionName,
						productCatalogName), false);
	}

	@Override
	protected void importContentCatalog(final String extensionName, final String contentCatalogName)
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/cms-content.impex", extensionName,
						contentCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/cms-mobile-content.impex", extensionName,
						contentCatalogName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/email-content.impex", extensionName,
						contentCatalogName), false);
		if (ResponsiveUtils.isResponsive())
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/cms-responsive-content.impex",
							extensionName, contentCatalogName), false);
		}
	}

	@Override
	protected void importStore(final String extensionName, final String storeName, final String productCatalogName)
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/stores/%s/points-of-service-media.impex", extensionName, storeName), false);
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/stores/%s/points-of-service.impex", extensionName, storeName), false);

		if (isExtensionLoaded(BTG_EXTENSION_NAME))
		{
			getSetupImpexService().importImpexFile(
					String.format("/%s/import/sampledata/stores/%s/btg.impex", extensionName, storeName), false);
		}

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/stores/%s/warehouses.impex", extensionName, storeName), false);

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/reviews.impex", extensionName,
						productCatalogName), false);

		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/stores/%s/promotions.impex", extensionName, storeName), false);
	}

	@Override
	protected void importJobs(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/stores/%s/jobs.impex", extensionName, storeName), false);
	}

	@Override
	protected void importSolrIndex(final String extensionName, final String storeName)
	{
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/stores/%s/solr.impex", extensionName, storeName), false);

		getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));
	}
}

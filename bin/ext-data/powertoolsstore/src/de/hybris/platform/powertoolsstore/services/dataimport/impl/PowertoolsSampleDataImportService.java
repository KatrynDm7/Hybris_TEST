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
package de.hybris.platform.powertoolsstore.services.dataimport.impl;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.core.initialization.SystemSetupContext;


public class PowertoolsSampleDataImportService extends SampleDataImportService
{
	public static final String COMMERCEORG_ADDON_EXTENSION_NAME = "commerceorgaddon";

	public void importCommerceOrgData(final SystemSetupContext context)
	{
		if (isExtensionLoaded(COMMERCEORG_ADDON_EXTENSION_NAME))
		{
			final String extensionName = context.getExtensionName();

			getSetupImpexService().importImpexFile(
					String.format("/%s/import/sampledata/commerceorg/user-groups.impex", extensionName), false);
		}
	}

	@Override
	protected void importProductCatalog(final String extensionName, final String productCatalogName)
	{
		super.importProductCatalog(extensionName, productCatalogName);

		// Load Multi-Dimension Categories
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/multi-d/dimension-categories.impex",
						extensionName, productCatalogName), false);
		// Load Multi-Dimension Products
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/multi-d/dimension-products.impex",
						extensionName, productCatalogName), false);
		// Load Multi-Dimension Products-Media
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/multi-d/dimension-products-media.impex",
						extensionName, productCatalogName), false);
		// Load Multi-Dimension Products-Prices
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/multi-d/dimension-products-prices.impex",
						extensionName, productCatalogName), false);
		// Load Multi-Dimension Products-Stocklevels
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/multi-d/dimension-products-stock-levels.impex",
						extensionName, productCatalogName), false);
		// Load future stock for multi -D products
		getSetupImpexService().importImpexFile(
				String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-futurestock.impex",
						extensionName, productCatalogName), false);

	}

}

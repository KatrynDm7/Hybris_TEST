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
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Some helper methods for the catalog service.
 */
public final class CommerceCatalogUtils
{
	/**
	 * Private constructor for avoiding instantiation.
	 */
	private CommerceCatalogUtils()
	{
		//empty
	}

	/**
	 * Filters out {@link ClassificationSystemVersionModel} and ContentCatalogModel catalogs
	 *
	 * @param catalogVersions A collection of {@link CatalogVersionModel}s
	 * @return A collection of product catalog versions
	 */
	public static Collection<CatalogVersionModel> findProductCatalogVersions(final Collection<CatalogVersionModel> catalogVersions)
	{
		final List<CatalogVersionModel> result = new ArrayList<CatalogVersionModel>(catalogVersions.size());

		for (final CatalogVersionModel catalogVersion : catalogVersions)
		{
			//TODO: find a better way to test for ContentCatalogModel without depending on cms2
			if (!(catalogVersion instanceof ClassificationSystemVersionModel)
					&& !(catalogVersion.getCatalog().getClass().getName().equals("ContentCatalogModel")))
			{
				result.add(catalogVersion);
			}
		}

		return result;
	}

	/**
	 * Gets the active Product Catalog
	 *
	 * @param catalogVersions A collection of {@link CatalogVersionModel}s
	 * @return An active product catalog
	 */
	public static CatalogVersionModel getActiveProductCatalogVersion(final Collection<CatalogVersionModel> catalogVersions)
	{

		for (final CatalogVersionModel cvm : catalogVersions)
		{
			if (cvm.getCatalog().getClass().isAssignableFrom(CatalogModel.class))
			{
				return cvm;
			}
		}

		return null;
	}

}

/*
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


public class CatalogVersionAssert extends GenericAssert<CatalogVersionAssert, CatalogVersionModel>
{
	private final ModelService modelService;

	protected CatalogVersionAssert(final CatalogVersionModel actual)
	{
		super(CatalogVersionAssert.class, actual);
		modelService = Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}

	public static CatalogVersionAssert assertThat(final CatalogVersionModel catalog)
	{
		return new CatalogVersionAssert(catalog);
	}

	public CatalogVersionAssert hasNumOfProducts(final int numOfProducts)
	{
		final CatalogVersion catalog = modelService.getSource(actual);

		Assertions.assertThat(catalog.getAllProductCount()).isEqualTo(numOfProducts);

		return this;
	}

	public CatalogVersionAssert hasAllProductsWithPropertiesAs(final Map<String, String> values)
	{
		final CatalogVersion catalog = modelService.getSource(actual);
		final Collection<Product> allProducts = catalog.getAllProducts();

		for (final Product product : allProducts)
		{
			final ProductModel productModel = modelService.get(product.getPK());
			ProductAssert.assertThat(productModel).hasPropertyValuesAs(values);
		}

		return this;
	}
}

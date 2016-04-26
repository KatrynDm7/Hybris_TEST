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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.strategies.QuotePolicyStrategy;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Mock Quote Policy Strategy to simulate the retrieval of the relevant information for display.
 */
public class MockQuotePolicyStrategy implements QuotePolicyStrategy
{
	private CatalogVersionService catalogVersionService;

	private ProductService productService;

	private Converter<ProductModel, ProductData> productConverter;

	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	static final String CATALOG_ID = "financialProductCatalog";
	static final String CATALOG_VERSION_ID = "Online";

	static final String[] POLICIES_PRODUCT_IDS = new String[]
	{ "WED_2STAR", "WED_3STAR", "TRA_SINGLE_BUDGET", "TRA_ANNUAL_GOLD" };
	static final String[] QUOTE_PRODUCT_IDS = new String[]
	{ "TRA_SINGLE_GOLD", "WED_5STAR" };


	/**
	 * Obtain a list of policies with product information.
	 */
	@Override
	public List<ProductData> getPoliciesProducts()
	{
		final List<ProductData> productDataList = new ArrayList<>();

		final CatalogVersionModel catalogVersionModel = getCatalogVersionService()
				.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_ID);
		for (final String productId : POLICIES_PRODUCT_IDS)
		{
			final ProductModel product = getProductService().getProductForCode(catalogVersionModel, productId);
			final ProductData data = getProductConverter().convert(product);

			getProductConfiguredPopulator().populate(product, data, Arrays.asList(ProductOption.THUMBNAIL));

			productDataList.add(data);
		}

		return productDataList;
	}

	/**
	 * Obtain a list of quotes with product information.
	 */
	@Override
	public List<ProductData> getQuotesProducts()
	{
		final List<ProductData> productDataList = new ArrayList<>();

		final CatalogVersionModel catalogVersionModel = getCatalogVersionService()
				.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_ID);
		for (final String productId : QUOTE_PRODUCT_IDS)
		{
			final ProductModel product = getProductService().getProductForCode(catalogVersionModel, productId);
			final ProductData data = getProductConverter().convert(product);

			getProductConfiguredPopulator().populate(product, data, Arrays.asList(ProductOption.THUMBNAIL));

			productDataList.add(data);
		}

		return productDataList;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
	{
		return productConfiguredPopulator;
	}

	@Required
	public void setProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConfiguredPopulator = productConfiguredPopulator;
	}
}

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
package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


public class DefaultIndexerServiceIntegrationTest extends AbstractIntegrationTest
{
	private static final String PRICE_INDEXED_PROPERTY = "price";

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private ProductService productService;

	@Test
	public void performFullIndex() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		// when
		indexerService.performFullIndex(facetSearchConfig);
	}

	@Test
	public void updateIndex() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		indexerService.performFullIndex(facetSearchConfig);

		// when
		indexerService.updateIndex(facetSearchConfig);
	}

	@Test
	public void updateTypeIndex() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(facetSearchConfig);

		// when
		indexerService.updateTypeIndex(facetSearchConfig, indexedType);
	}

	@Test
	public void updateTypeIndexWithPks() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final List<PK> pks = new ArrayList<PK>();

		for (final ProductModel product : getExistingProducts())
		{
			pks.add(product.getPk());
		}

		indexerService.performFullIndex(facetSearchConfig);

		// when
		indexerService.updateTypeIndex(facetSearchConfig, indexedType, pks);
	}

	@Test
	public void updatePartialTypeIndexWithPks() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final Collection<IndexedProperty> indexedProperties = Collections.singletonList(indexedType.getIndexedProperties().get(
				PRICE_INDEXED_PROPERTY));
		final List<PK> pks = new ArrayList<PK>();

		for (final ProductModel product : getExistingProducts())
		{
			pks.add(product.getPk());
		}

		indexerService.performFullIndex(facetSearchConfig);

		// when
		indexerService.updatePartialTypeIndex(facetSearchConfig, indexedType, indexedProperties, pks);
	}

	@Test
	public void deleteFromIndex() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		indexerService.performFullIndex(facetSearchConfig);

		// when
		indexerService.deleteFromIndex(facetSearchConfig);
	}

	@Test
	public void deleteTypeIndex() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(facetSearchConfig);

		// when
		indexerService.deleteTypeIndex(facetSearchConfig, indexedType);
	}

	@Test
	public void deleteTypeIndexWithPks() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final List<PK> pks = new ArrayList<PK>();

		for (final ProductModel product : getExistingProducts())
		{
			pks.add(product.getPk());
		}

		indexerService.performFullIndex(facetSearchConfig);

		// when
		indexerService.deleteTypeIndex(facetSearchConfig, indexedType, pks);
	}

	protected List<ProductModel> getExistingProducts()
	{
		final List<ProductModel> products = new ArrayList<ProductModel>();

		final CatalogVersionModel catalogVersionStaged = catalogVersionService
				.getCatalogVersion(HW_CATALOG, STAGED_CATALOG_VERSION);
		final ProductModel productStaged = productService.getProductForCode(catalogVersionStaged, getProductCode());

		final CatalogVersionModel catalogVersionOnline = catalogVersionService
				.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION);
		final ProductModel productOnline = productService.getProductForCode(catalogVersionOnline, getProductCode());

		products.add(productStaged);
		products.add(productOnline);

		return products;
	}
}

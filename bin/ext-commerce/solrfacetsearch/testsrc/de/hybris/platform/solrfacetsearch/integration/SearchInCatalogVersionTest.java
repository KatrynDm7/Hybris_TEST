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
package de.hybris.platform.solrfacetsearch.integration;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


public class SearchInCatalogVersionTest extends AbstractIntegrationTest
{
	@Resource
	private ProductService productService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/SearchInCatalogVersionTest.csv");
	}

	/**
	 * Test case for issue SNA-264
	 */
	@Test
	public void testSpaceInCatalogId() throws Exception
	{
		//myCatalogVersion -> my catalog:Online
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel myCatalogOnline = catalogVersionService.getCatalogVersion("my Catalog", ONLINE_CATALOG_VERSION);
		final ProductModel testProduct0 = productService.getProductForCode(myCatalogOnline, "testProduct0");
		final ProductModel testProduct1 = productService.getProductForCode(myCatalogOnline, "testProduct1");

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(myCatalogOnline));
		final SearchResult result = facetSearchService.search(query);
		Assert.assertTrue(result.getResults().contains(testProduct0));
		Assert.assertTrue(result.getResults().contains(testProduct1));
	}
}

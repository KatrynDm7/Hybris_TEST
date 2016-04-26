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


/**
 * Tests escaping of special characters when building a search query.
 */
public class EscapeQueryFieldsTest extends AbstractIntegrationTest
{
	private final static String TARGET_FIELD_NAME = "Colour of product, 1766";

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/EscapeQueryFieldsTest.csv");
	}

	@Test
	public void testQueryEscapedField() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());
		final CatalogVersionModel hwStagedCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				STAGED_CATALOG_VERSION + getTestId());

		indexerService.performFullIndex(facetSearchConfig);

		// when
		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);
		searchQuery.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion, hwStagedCatalogVersion));
		searchQuery.searchInField(TARGET_FIELD_NAME, "test");

		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		Assert.assertEquals(2, searchResult.getTotalNumberOfResults());
	}
}

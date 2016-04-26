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

import static org.junit.Assert.assertEquals;

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

import org.junit.Test;


/**
 * Tests the Solr search with multi-categories.
 *
 */
public class SearchInMultiCategoriesTest extends AbstractIntegrationTest
{
	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importCsv("/test/solrClassificationSystemOnline.csv", "utf-8");
		importCsv("/test/solrClassificationSystemStaged.csv", "utf-8");
	}

	/**
	 * Creates the index for the hwcatalog_online, searches for products that belong to category with code "HW1200", and
	 * searches for the products that belong to both "HW1200" and "HW1210".
	 *
	 * @throws Exception
	 */
	@Test
	public void testMultiCategoryCodes() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION);

		indexerService.performFullIndex(facetSearchConfig);

		final String queryField = "categoryCode";
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion));
		query.addFacetValue(queryField, "HW1200");
		SearchResult result = facetSearchService.search(query);
		assertEquals(9, result.getTotalNumberOfResults());
		query.addFacetValue(queryField, "HW1210");
		result = facetSearchService.search(query);
		assertEquals(4, result.getTotalNumberOfResults());
	}

	/**
	 * Creates the index for the hwcatalog_online, sets the language to German, searches for products that belong to
	 * category with name "Hardware", searches for the products that belong to both "Hardware" and
	 * "Elektronische Geräte", and at last searches for products that belong to "Hardware", "Elektronische Geräte", and
	 * "Topseller_online_de".
	 *
	 * @throws Exception
	 */
	@Test
	public void testMultiCategoryNames() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION);

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnline));
		query.setLanguage("de");
		final String queryField = "categoryName";
		query.addFacetValue(queryField, "Hardware");
		SearchResult result = facetSearchService.search(query);
		assertEquals(33, result.getTotalNumberOfResults());
		query.addFacetValue(queryField, "Elektronische Ger\u00E4te");
		result = facetSearchService.search(query);
		assertEquals(33, result.getTotalNumberOfResults());
		query.addFacetValue(queryField, "Topseller_online_de");
		query.addRawParam("timeAllowed", "5000");
		result = facetSearchService.search(query);
		assertEquals(6, result.getTotalNumberOfResults());
	}
}

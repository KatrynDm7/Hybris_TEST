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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;


/**
 * Tests restricting the fields included in the search response from Solr.
 */
public class SolrSearchResponseRestrictFieldsTest extends AbstractIntegrationTest
{
	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/SolrSearchResponseRestrictFieldsTest.csv");
	}

	@Test
	public void testRestrictNotSelected() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setLanguage("en");
		searchQuery.setCurrency("EUR");
		searchQuery.setCatalogVersions(Collections.singletonList(catalogVersion));

		// when
		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		final QueryResponse solrObject = searchResult.getSolrObject();
		assertTrue(solrObject.getResults().get(0).containsKey("description_text_de"));
		assertTrue(solrObject.getResults().get(0).containsKey("description_text_en"));
		assertTrue(solrObject.getResults().get(0).containsKey("description_text_fr"));
	}

	@Test
	public void testRestrictSelected() throws Exception
	{
		// given
		importConfig("/test/integration/SolrSearchResponseRestrictFieldsTest_restrictionSelected.csv");

		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setLanguage("en");
		searchQuery.setCurrency("EUR");
		searchQuery.setCatalogVersions(Collections.singletonList(catalogVersion));

		// when
		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		final QueryResponse solrObject = searchResult.getSolrObject();
		assertFalse(solrObject.getResults().get(0).containsKey("description_text_de"));
		assertTrue(solrObject.getResults().get(0).containsKey("description_text_en"));
		assertFalse(solrObject.getResults().get(0).containsKey("description_text_fr"));
	}

	@Test
	public void testRestrictDescriptionField() throws Exception
	{
		// given
		importConfig("/test/integration/SolrSearchResponseRestrictFieldsTest_restrictDescriptionField.csv");

		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setLanguage("en");
		searchQuery.setCurrency("EUR");
		searchQuery.setCatalogVersions(Collections.singletonList(catalogVersion));

		// when
		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		final QueryResponse solrObject = searchResult.getSolrObject();
		assertFalse(solrObject.getResults().get(0).containsKey("description_text_de"));
		assertFalse(solrObject.getResults().get(0).containsKey("description_text_en"));
		assertFalse(solrObject.getResults().get(0).containsKey("description_text_fr"));
	}
}

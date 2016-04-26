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
package de.hybris.platform.solrfacetsearch.config;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.Collections;

import javax.annotation.Resource;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;


public class RangedIndexedPropertyTest extends AbstractIntegrationTest
{
	private static final String PRICE_RANGE_EUR = "1-100";
	private static final String PRICE_RANGE_USD = "$50-$199.99";
	private static final String PRICE_RANGE_CHF = "1,000-100,000SFr.";
	private static final String PRODUCT_CODE = "testProduct1";

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Override
	protected void loadInitialData() throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException,
			SolrServerException
	{
		importConfig("/test/integration/RangedIndexedPropertyTest.csv");
	}

	@Test
	public void testPriceRangeSetEUR() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		// when
		indexerService.performFullIndex(getFacetSearchConfig());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(hwOnlineCatalogVersion));
		query.searchInField("code", PRODUCT_CODE);
		final SearchResult searchResult = facetSearchService.search(query);

		//then
		final Facet facet = searchResult.getFacet("price");
		assertEquals(1, facet.getFacetValues().size());

		assertEquals(PRICE_RANGE_EUR, facet.getFacetValues().get(0).getName());
	}

	@Test
	public void testPriceRangeSetUSD() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		// when
		indexerService.performFullIndex(getFacetSearchConfig());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(hwOnlineCatalogVersion));
		query.searchInField("code", PRODUCT_CODE);
		query.setCurrency("USD");
		final SearchResult searchResult = facetSearchService.search(query);

		//then
		final Facet facet = searchResult.getFacet("price");
		assertEquals(1, facet.getFacetValues().size());

		assertEquals(PRICE_RANGE_USD, facet.getFacetValues().get(0).getName());
	}

	@Test
	public void testPriceRangeSetCHF() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		// when
		indexerService.performFullIndex(getFacetSearchConfig());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(hwOnlineCatalogVersion));
		query.searchInField("code", PRODUCT_CODE);
		query.setCurrency("CHF");
		final SearchResult searchResult = facetSearchService.search(query);

		//then
		final Facet facet = searchResult.getFacet("price");
		assertEquals(1, facet.getFacetValues().size());

		assertEquals(PRICE_RANGE_CHF, facet.getFacetValues().get(0).getName());
	}
}

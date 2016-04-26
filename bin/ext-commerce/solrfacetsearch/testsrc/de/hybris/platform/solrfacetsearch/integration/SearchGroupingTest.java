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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroup;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroupCommand;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.hamcrest.Matchers;
import org.junit.Test;


@IntegrationTest
public class SearchGroupingTest extends AbstractIntegrationTest
{
	public static final String GROUP_FIELD = "manufacturerName";
	public static final String MANUFACTURER_A = "manA";
	public static final String MANUFACTURER_B = "manB";

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Override
	protected void loadInitialData()
			throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException, SolrServerException
	{
		importConfig("/test/integration/SearchGroupingTest.csv");
	}

	@Test
	public void testWithoutGrouping() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setCatalogVersions(Collections.singletonList(hwOnlineCatalogVersion));
		searchQuery.addSort(GROUP_FIELD);
		searchQuery.addFacet(GROUP_FIELD);

		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		assertEquals(5, searchResult.getNumberOfResults());

		final Facet facet = searchResult.getFacet(GROUP_FIELD);
		assertNotNull("Facet not found: " + GROUP_FIELD, facet);
		assertThat(facet.getFacetValues(),
				Matchers.contains(new FacetValue(MANUFACTURER_A, 3, false), new FacetValue(MANUFACTURER_B, 2, false)));
	}

	@Test
	public void testWithGrouping() throws Exception
	{
		// given
		importConfig("/test/integration/SearchGroupingTest_enableGrouping.csv");

		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setCatalogVersions(Collections.singletonList(hwOnlineCatalogVersion));
		searchQuery.addSort(GROUP_FIELD);

		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		assertEquals(2, searchResult.getNumberOfResults());
		assertThat(searchResult.getGroupCommands(), Matchers.hasSize(1));

		final SearchResultGroupCommand groupCommand = searchResult.getGroupCommands().get(0);
		assertEquals(groupCommand.getName(), GROUP_FIELD);
		assertEquals(groupCommand.getNumberOfGroups(), 2);
		assertEquals(groupCommand.getNumberOfMatches(), 5);
		assertThat(groupCommand.getGroups(), Matchers.hasSize(2));

		final SearchResultGroup group1 = groupCommand.getGroups().get(0);
		assertEquals(group1.getGroupValue(), MANUFACTURER_A);
		assertEquals(3, group1.getNumberOfResults());
		assertThat(group1.getDocuments(), Matchers.hasSize(3));

		final SearchResultGroup group2 = groupCommand.getGroups().get(1);
		assertEquals(group2.getGroupValue(), MANUFACTURER_B);
		assertEquals(2, group2.getNumberOfResults());
		assertThat(group2.getDocuments(), Matchers.hasSize(2));
	}

	@Test
	public void testWithGroupingAndGroupLimit() throws Exception
	{
		// given
		importConfig("/test/integration/SearchGroupingTest_enableGrouping.csv");
		importConfig("/test/integration/SearchGroupingTest_setGroupLimitTo1.csv");

		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setCatalogVersions(Collections.singletonList(hwOnlineCatalogVersion));
		searchQuery.addSort(GROUP_FIELD);

		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		assertEquals(2, searchResult.getNumberOfResults());
		assertThat(searchResult.getGroupCommands(), Matchers.hasSize(1));

		final SearchResultGroupCommand groupCommand = searchResult.getGroupCommands().get(0);
		assertEquals(groupCommand.getName(), GROUP_FIELD);
		assertEquals(groupCommand.getNumberOfGroups(), 2);
		assertEquals(groupCommand.getNumberOfMatches(), 5);
		assertThat(groupCommand.getGroups(), Matchers.hasSize(2));

		final SearchResultGroup group1 = groupCommand.getGroups().get(0);
		assertEquals(group1.getGroupValue(), MANUFACTURER_A);
		assertEquals(3, group1.getNumberOfResults());
		assertThat(group1.getDocuments(), Matchers.hasSize(1));

		final SearchResultGroup group2 = groupCommand.getGroups().get(1);
		assertEquals(group2.getGroupValue(), MANUFACTURER_B);
		assertEquals(2, group2.getNumberOfResults());
		assertThat(group2.getDocuments(), Matchers.hasSize(1));
	}

	@Test
	public void testWithGroupingAndGroupFacets() throws Exception
	{
		// given
		importConfig("/test/integration/SearchGroupingTest_enableGrouping.csv");
		importConfig("/test/integration/SearchGroupingTest_enableGroupFacets.csv");

		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setCatalogVersions(Collections.singletonList(hwOnlineCatalogVersion));
		searchQuery.addSort(GROUP_FIELD);
		searchQuery.addFacet(GROUP_FIELD);

		final SearchResult searchResult = facetSearchService.search(searchQuery);

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Facet facet = searchResult.getFacet(GROUP_FIELD);
		assertNotNull("Facet not found: " + GROUP_FIELD, facet);
		assertThat(facet.getFacetValues(),
				Matchers.contains(new FacetValue(MANUFACTURER_A, 1, false), new FacetValue(MANUFACTURER_B, 1, false)));
	}
}

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
package de.hybris.platform.solrfacetsearch.search.impl.populators;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class FacetSearchQueryPagingPopulatorTest
{
	private FacetSearchQueryPagingPopulator facetSearchQueryPagingPopulator;
	private SearchQueryConverterData searchQueryConverterData;

	@Before
	public void setUp()
	{
		final SearchConfig searchConfig = new SearchConfig();
		searchConfig.setPageSize(10);

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		facetSearchConfig.setSearchConfig(searchConfig);

		final IndexedType indexedType = new IndexedType();

		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);

		facetSearchQueryPagingPopulator = new FacetSearchQueryPagingPopulator();

		searchQueryConverterData = new SearchQueryConverterData();
		searchQueryConverterData.setSearchQuery(searchQuery);
	}

	@Test
	public void populateFirstPage()
	{
		// given
		searchQueryConverterData.getSearchQuery().setOffset(0);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryPagingPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		assertEquals(solrQuery.getStart(), Integer.valueOf(0));
		assertEquals(solrQuery.getRows(), Integer.valueOf(10));
	}

	@Test
	public void populateSecondPage()
	{
		// given
		searchQueryConverterData.getSearchQuery().setOffset(1);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryPagingPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		assertEquals(solrQuery.getStart(), Integer.valueOf(10));
		assertEquals(solrQuery.getRows(), Integer.valueOf(10));
	}
}

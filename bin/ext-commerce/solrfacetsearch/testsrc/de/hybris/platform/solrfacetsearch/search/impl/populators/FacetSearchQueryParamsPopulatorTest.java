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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

import java.util.Arrays;

import org.apache.solr.client.solrj.SolrQuery;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class FacetSearchQueryParamsPopulatorTest
{
	private FacetSearchQueryParamsPopulator facetSearchQueryParamsPopulator;
	private SearchQueryConverterData searchQueryConverterData;

	@Before
	public void setUp()
	{
		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexedType indexedType = new IndexedType();
		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);

		facetSearchQueryParamsPopulator = new FacetSearchQueryParamsPopulator();

		searchQueryConverterData = new SearchQueryConverterData();
		searchQueryConverterData.setSearchQuery(searchQuery);
	}

	@Test
	public void populateWithEmptyParams()
	{
		// given
		final String param = "paramName";
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryParamsPopulator.populate(searchQueryConverterData, solrQuery);

		//then
		assertNull(solrQuery.get(param));
	}

	@Test
	public void populateWithSingleParam()
	{
		// given
		final String param = "paramName";
		searchQueryConverterData.getSearchQuery().addRawParam(param, "testing");
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryParamsPopulator.populate(searchQueryConverterData, solrQuery);

		//then
		assertTrue(solrQuery.get(param).equals("testing"));
	}

	@Test
	public void populateWithMultipleParams()
	{
		// given
		final String param = "paramName";
		final String value1 = "value1";
		final String value2 = "value2";
		searchQueryConverterData.getSearchQuery().addRawParam(param, value1, value2);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryParamsPopulator.populate(searchQueryConverterData, solrQuery);

		//then
		assertThat(Arrays.asList(solrQuery.getParams(param)), CoreMatchers.hasItems(value1, value2));
	}
}

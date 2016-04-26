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

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.OrderField.SortOrder;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class FacetSearchQuerySortsPopulatorTest
{
	public static final String FIELD1 = "field1";
	public static final String TRANSLATED_FIELD1 = "translatedField1";

	public static final String FIELD2 = "field2";
	public static final String TRANSLATED_FIELD2 = "translatedField2";

	@Mock
	private FieldNameTranslator fieldNameTranslator;

	private FacetSearchQuerySortsPopulator facetSearchQuerySortsPopulator;
	private SearchQueryConverterData searchQueryConverterData;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexedType indexedType = new IndexedType();
		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);

		facetSearchQuerySortsPopulator = new FacetSearchQuerySortsPopulator();
		facetSearchQuerySortsPopulator.setFieldNameTranslator(fieldNameTranslator);

		searchQueryConverterData = new SearchQueryConverterData();
		searchQueryConverterData.setSearchQuery(searchQuery);

		given(fieldNameTranslator.translate(searchQuery, FIELD1, FieldType.SORT)).willReturn(TRANSLATED_FIELD1);
		given(fieldNameTranslator.translate(searchQuery, FIELD2, FieldType.SORT)).willReturn(TRANSLATED_FIELD2);
	}

	@Test
	public void populateWithEmptySorts()
	{
		// given
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQuerySortsPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		assertThat(solrQuery.getSorts(), empty());
	}

	@Test
	public void populateWithSingleSort()
	{
		// given
		final SearchQuery searchQuery = searchQueryConverterData.getSearchQuery();
		searchQuery.addSort(FIELD1, SortOrder.ASCENDING);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQuerySortsPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		final SortClause sortClause = new SortClause(TRANSLATED_FIELD1, ORDER.asc);
		assertThat(solrQuery.getSorts(), CoreMatchers.hasItem(sortClause));
	}

	@Test
	public void populateWithMultipleSorts()
	{
		// given
		final SearchQuery searchQuery = searchQueryConverterData.getSearchQuery();
		searchQuery.addSort(FIELD1, SortOrder.ASCENDING);
		searchQuery.addSort(FIELD2, SortOrder.DESCENDING);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQuerySortsPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		final SortClause sortClause1 = new SortClause(TRANSLATED_FIELD1, ORDER.asc);
		final SortClause sortClause2 = new SortClause(TRANSLATED_FIELD2, ORDER.desc);

		final List<SortClause> sorts = solrQuery.getSorts();
		assertThat(sorts, Matchers.hasSize(2));
		assertEquals(sorts.get(0), sortClause1);
		assertEquals(sorts.get(1), sortClause2);
	}
}

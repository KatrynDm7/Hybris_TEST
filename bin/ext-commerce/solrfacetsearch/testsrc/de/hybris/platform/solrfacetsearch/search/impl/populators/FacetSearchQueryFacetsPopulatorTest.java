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
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultSolrQueryConverter;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

import java.util.Arrays;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@Ignore
@UnitTest
public class FacetSearchQueryFacetsPopulatorTest
{
	public static final String FIELD1 = "field1";
	public static final String TRANSLATED_FIELD1 = "translatedField1";

	public static final String FIELD2 = "field2";
	public static final String TRANSLATED_FIELD2 = "translatedField2";

	@Mock
	private FieldNameTranslator fieldNameTranslator;

	private FacetSearchQueryFacetsPopulator facetSearchQueryFacetsPopulator;
	private SearchQueryConverterData searchQueryConverterData;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexedType indexedType = new IndexedType();
		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);

		facetSearchQueryFacetsPopulator = new FacetSearchQueryFacetsPopulator();
		facetSearchQueryFacetsPopulator.setFieldNameTranslator(fieldNameTranslator);

		searchQueryConverterData = new SearchQueryConverterData();
		searchQueryConverterData.setSearchQuery(searchQuery);

		given(fieldNameTranslator.translate(searchQuery, FIELD1, FieldType.SORT)).willReturn(TRANSLATED_FIELD1);
		given(fieldNameTranslator.translate(searchQuery, FIELD2, FieldType.SORT)).willReturn(TRANSLATED_FIELD2);
	}

	@Test
	public void populateWithEmptyFacets()
	{
		// given
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryFacetsPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		assertThat(Arrays.asList(solrQuery.getFacetQuery()), empty());
	}

	@Test
	public void populateWithRefineFacet()
	{
		// given
		final SearchQuery searchQuery = searchQueryConverterData.getSearchQuery();
		searchQuery.addFacet(FIELD1, FacetType.REFINE);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryFacetsPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		final String[] expectedFacetNames = new String[]
		{ "{!ex=fk0}translatedTest" };

		Assert.assertArrayEquals(expectedFacetNames, solrQuery.getFacetFields());
		Assert.assertEquals(DefaultSolrQueryConverter.FacetSort.COUNT.getName(), solrQuery.getFacetSortString());
	}

	@Test
	public void populateWithMultiSelectAndFacet()
	{
		// given
		final SearchQuery searchQuery = searchQueryConverterData.getSearchQuery();
		searchQuery.addFacet(FIELD1, FacetType.MULTISELECTAND);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryFacetsPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		final String[] expectedFacetNames = new String[]
		{ "{!ex=fk0}translatedTest" };

		Assert.assertArrayEquals(expectedFacetNames, solrQuery.getFacetFields());
		Assert.assertEquals(DefaultSolrQueryConverter.FacetSort.COUNT.getName(), solrQuery.getFacetSortString());
	}

	@Test
	public void populateWithMultiSelectOrFacet()
	{
		// given
		final SearchQuery searchQuery = searchQueryConverterData.getSearchQuery();
		searchQuery.addFacet(FIELD1, FacetType.MULTISELECTOR);
		final SolrQuery solrQuery = new SolrQuery();

		// when
		facetSearchQueryFacetsPopulator.populate(searchQueryConverterData, solrQuery);

		// then
		final String[] expectedFacetNames = new String[]
		{ "translatedTest" };

		Assert.assertArrayEquals(expectedFacetNames, solrQuery.getFacetFields());
		Assert.assertEquals(DefaultSolrQueryConverter.FacetSort.COUNT.getName(), solrQuery.getFacetSortString());
	}
}

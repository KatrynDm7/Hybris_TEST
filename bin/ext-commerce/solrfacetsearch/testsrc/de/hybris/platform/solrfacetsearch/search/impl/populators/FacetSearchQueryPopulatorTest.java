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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.BoostField;
import de.hybris.platform.solrfacetsearch.search.FacetSearchQueryOperatorTranslator;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.FreeTextQueryBuilderFactory;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFreeTextQueryBuilderFactory;
import de.hybris.platform.solrfacetsearch.search.impl.DisMaxFreeTextQueryBuilder;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class FacetSearchQueryPopulatorTest
{
	public static final String FIELD1 = "field1";
	public static final String TRANSLATED_FIELD1 = "translatedField1";

	public static final String FIELD2 = "field2";
	public static final String TRANSLATED_FIELD2 = "translatedField2";

	@Mock
	private FieldNameTranslator fieldNameTranslator;
	@Mock
	private FreeTextQueryBuilderFactory freeTextQueryBuilderFactory;
	@Mock
	private FacetSearchQueryOperatorTranslator facetSearchQueryOperatorTranslator;

	private FacetSearchQueryBasicPopulator facetSearchQueryBasicPopulator;
	private SearchQueryConverterData searchQueryConverterData;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexedType indexedType = new IndexedType();
		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);

		facetSearchQueryBasicPopulator = new FacetSearchQueryBasicPopulator();
		facetSearchQueryBasicPopulator.setFieldNameTranslator(fieldNameTranslator);
		facetSearchQueryBasicPopulator.setFreeTextQueryBuilderFactory(freeTextQueryBuilderFactory);
		facetSearchQueryBasicPopulator.setFacetSearchQueryOperatorTranslator(facetSearchQueryOperatorTranslator);

		searchQueryConverterData = new SearchQueryConverterData();
		searchQueryConverterData.setSearchQuery(searchQuery);

		given(fieldNameTranslator.translate(searchQuery, FIELD1, FieldNameProvider.FieldType.INDEX)).willReturn(TRANSLATED_FIELD1);
		given(fieldNameTranslator.translate(searchQuery, FIELD2, FieldNameProvider.FieldType.INDEX)).willReturn(TRANSLATED_FIELD2);

		DisMaxFreeTextQueryBuilder disMaxFreeTextQueryBuilder = new DisMaxFreeTextQueryBuilder();
		given(freeTextQueryBuilderFactory.createQueryBuilder(searchQuery)).willReturn(disMaxFreeTextQueryBuilder);

		given(facetSearchQueryOperatorTranslator.translate(anyString(), any())).willReturn("test");
	}

	@Test
	public void testPopulateWithQueryBoosts()
	{
		// given
		final SolrQuery solrQuery = new SolrQuery();
		searchQueryConverterData.getSearchQuery().addBoost(FIELD1, SearchQuery.QueryOperator.EQUAL_TO, "test1", 10f, BoostField.BoostType.MULTIPLICATIVE);
		searchQueryConverterData.getSearchQuery().addBoost(FIELD2, SearchQuery.QueryOperator.EQUAL_TO, "test2", 11f, BoostField.BoostType.MULTIPLICATIVE);
		searchQueryConverterData.getSearchQuery().addBoost(FIELD1, SearchQuery.QueryOperator.EQUAL_TO, "test1", 12f, BoostField.BoostType.ADDITIVE);
		searchQueryConverterData.getSearchQuery().addBoost(FIELD2, SearchQuery.QueryOperator.EQUAL_TO, "test2", 13f, BoostField.BoostType.ADDITIVE);

		// when
		facetSearchQueryBasicPopulator.populate(searchQueryConverterData, solrQuery);

		//then
		assertEquals(solrQuery.getQuery(),
				"{!boost b=\"product(map(query({!v=translatedField1:test}),0,0,1,10.0), map(query({!v=translatedField2:test}),0,0,1,11.0))\"} "
						+ "*:* "
						+ "AND "
						+ "({!func v=\"sum(map(query({!v=translatedField1:test}),0,0,0,12.0), map(query({!v=translatedField2:test}),0,0,0,13.0))\"})");
	}
}

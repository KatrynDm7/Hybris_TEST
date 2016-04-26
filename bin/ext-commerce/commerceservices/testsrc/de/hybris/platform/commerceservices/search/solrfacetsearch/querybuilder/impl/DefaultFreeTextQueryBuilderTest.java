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
package de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.search.RawQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultFreeTextQueryBuilderTest
{
	private DefaultFreeTextQueryBuilder defaultFreeTextQueryBuilder;

	@Mock
	private FacetSearchConfig facetSearchConfig;
	@Mock
	private SearchConfig searchConfig;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		defaultFreeTextQueryBuilder = new DefaultFreeTextQueryBuilder();

		given(facetSearchConfig.getSearchConfig()).willReturn(searchConfig);
		given(searchConfig.getDefaultSortOrder()).willReturn(Collections.<String> emptyList());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testConvert()
	{
		final IndexedType indexedType = mock(IndexedType.class);
		final SearchQuery searchQuery = spy(new SearchQuery(facetSearchConfig, indexedType));
		searchQuery.setLanguage("en");
		searchQuery.setCurrency("GBP");

		final IndexedProperty codeProperty = new IndexedProperty();
		codeProperty.setName("code");
		codeProperty.setType("string");
		given(indexedType.getIndexedProperties()).willReturn(Collections.singletonMap("code", codeProperty));

		defaultFreeTextQueryBuilder.setPropertyName("code");
		defaultFreeTextQueryBuilder.setBoost(10);
		defaultFreeTextQueryBuilder.addFreeTextQuery(searchQuery, "full text string", new String[]
		{ "full", "text", "string" });

		verify(searchQuery).addRawQuery(new RawQuery("code", "full\\ text\\ string^20.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("code", "full\\ text\\ string*^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("code", "full^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("code", "full*^5.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("code", "text^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("code", "text*^5.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("code", "string^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("code", "string*^5.0", SearchQuery.Operator.OR));
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testConvert2()
	{
		final IndexedType indexedType = mock(IndexedType.class);
		final SearchQuery searchQuery = spy(new SearchQuery(facetSearchConfig, indexedType));
		searchQuery.setLanguage("en");
		searchQuery.setCurrency("GBP");

		final IndexedProperty codeProperty = new IndexedProperty();
		codeProperty.setName("name");
		codeProperty.setType("text");
		given(indexedType.getIndexedProperties()).willReturn(Collections.singletonMap("name", codeProperty));

		defaultFreeTextQueryBuilder.setPropertyName("name");
		defaultFreeTextQueryBuilder.setBoost(10);
		defaultFreeTextQueryBuilder.addFreeTextQuery(searchQuery, "full text string", new String[]
		{ "full", "text", "string" });

		verify(searchQuery).addRawQuery(new RawQuery("name", "full\\ text\\ string^20.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "full\\ text\\ string*^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "full\\ text\\ string~^5.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "full^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "full*^5.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "full~^2.5", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "text^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "text*^5.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "text~^2.5", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "string^10.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "string*^5.0", SearchQuery.Operator.OR));
		verify(searchQuery).addRawQuery(new RawQuery("name", "string~^2.5", SearchQuery.Operator.OR));
	}
}

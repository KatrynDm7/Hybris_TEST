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
package de.hybris.platform.solrfacetsearch.search.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.CoupledQueryField;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.Operator;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultSolrQueryConverterTest
{
	private static final String TEST_EN_LANG_CODE = "en";
	private static final String TEST_EN_CURRENCY_CODE = "GBP";

	// Categories
	private static final String TEST_FACET_CATEGORY_NAME = "manufacturer";
	private static final String TEST_FACET_CATEGORY_TRANSLATED_NAME = "manufacturer_string_mv";
	private static final String TEST_FACET_CATEGORY_VALUE1 = "Dell";
	private static final String TEST_FACET_CATEGORY_VALUE2 = "Maxtor";

	// Catalog Version
	private static final String TEST_CATALOG_VERSION_VER = "Online";
	private static final String TEST_CATALOG_VERSION_ID = "apparelProductCatalog";

	private static final int TEST_QUERY_PG_SIZE = 5;
	private static final int TEST_QUERY_OFFSET = 0;

	@Mock
	private FieldNameTranslator fieldNameTranslator;

	@Mock
	private CatalogModel catalog;

	@Mock
	private CatalogVersionModel catalogVersion;

	private DefaultSolrQueryConverter defaultSolrQueryConverter;
	private SearchQuery searchQuery;
	private IndexedProperty indexedProperty;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		defaultSolrQueryConverter = new DefaultSolrQueryConverter();
		defaultSolrQueryConverter.setFieldNameTranslator(fieldNameTranslator);

		indexedProperty = new IndexedProperty();
		indexedProperty.setName(TEST_FACET_CATEGORY_NAME);
		indexedProperty.setLocalized(false);
		indexedProperty.setCurrency(false);

		final IndexedType indexedType = new IndexedType();
		indexedType.setIndexedProperties(Collections.singletonMap(indexedProperty.getName(), indexedProperty));

		final Set<String> facets = new HashSet<String>(3);
		facets.add(TEST_FACET_CATEGORY_NAME);
		indexedType.setTypeFacets(facets);

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();

		searchQuery = new SearchQuery(facetSearchConfig, indexedType);
		searchQuery.setLanguage(TEST_EN_LANG_CODE);
		searchQuery.setCurrency(TEST_EN_CURRENCY_CODE);
		searchQuery.setCatalogVersions(Collections.singletonList(catalogVersion));
		searchQuery.setPageSize(Integer.valueOf(TEST_QUERY_PG_SIZE));
		searchQuery.setOffset(Integer.valueOf(TEST_QUERY_OFFSET));

		searchQuery.addQuery(TEST_FACET_CATEGORY_NAME, SearchQuery.Operator.AND, TEST_FACET_CATEGORY_VALUE1,
				TEST_FACET_CATEGORY_VALUE2);

		given(catalog.getId()).willReturn(TEST_CATALOG_VERSION_ID);
		given(catalogVersion.getVersion()).willReturn(TEST_CATALOG_VERSION_VER);
		given(catalogVersion.getCatalog()).willReturn(catalog);

		given(fieldNameTranslator.translate(searchQuery, TEST_FACET_CATEGORY_NAME, FieldNameProvider.FieldType.INDEX)).willReturn(
				TEST_FACET_CATEGORY_TRANSLATED_NAME);
	}

	@Test
	public void testConvertQueryForFacetTypeMultiSelectOr() throws Exception
	{
		indexedProperty.setFacetType(FacetType.MULTISELECTOR);

		String result = defaultSolrQueryConverter.convertSolrQuery(searchQuery).toString();
		result = URLDecoder.decode(result, "UTF-8");

		assertTrue("fq not as expected", result.contains("fq={!tag=fk0}(manufacturer_string_mv:(Dell OR Maxtor))"));
		assertTrue("facet.field not as expected", result.contains("facet.field={!ex=fk0}manufacturer_string_mv"));
	}

	@Test
	public void testConvertQueryForFacetTypeMultiSelectAnd() throws Exception
	{
		indexedProperty.setFacetType(FacetType.MULTISELECTAND);

		String result = defaultSolrQueryConverter.convertSolrQuery(searchQuery).toString();
		result = URLDecoder.decode(result, "UTF-8");

		assertTrue("fq not as expected", result.contains("fq={!tag=fk0}(manufacturer_string_mv:(Dell AND Maxtor))"));
		assertTrue("facet.field not as expected", result.contains("facet.field={!ex=fk0}manufacturer_string_mv"));
	}

	@Test
	public void testConvertQueryForFacetTypeRefine() throws Exception
	{
		indexedProperty.setFacetType(FacetType.REFINE);

		String result = defaultSolrQueryConverter.convertSolrQuery(searchQuery).toString();
		result = URLDecoder.decode(result, "UTF-8");

		assertTrue("fq not as expected", result.contains("fq=(manufacturer_string_mv:(Dell AND Maxtor))"));
		assertTrue("facet.field not as expected", result.contains("facet.field=manufacturer_string_mv"));
	}

	@Test
	public void testConvertQueryWithSimpleCoupledFields() throws Exception
	{
		indexedProperty.setFacetType(FacetType.REFINE);

		final QueryField queryField1 = new QueryField("catalogId", "TestCatalog1");
		final QueryField queryField2 = new QueryField("version", "Online");

		final QueryField queryField3 = new QueryField("catalogId", "TestCatalog2");
		final QueryField queryField4 = new QueryField("version", "Staged");

		final CoupledQueryField couple1 = new CoupledQueryField("testCouple", queryField1, queryField2, Operator.AND, Operator.OR);
		final CoupledQueryField couple2 = new CoupledQueryField("testCouple", queryField3, queryField4, Operator.AND, Operator.OR);

		searchQuery.addCoupledFields(couple1);
		searchQuery.addCoupledFields(couple2);

		String result = defaultSolrQueryConverter.convertSolrQuery(searchQuery).toString();
		result = URLDecoder.decode(result, "UTF-8");

		assertTrue(
				"fq not as expected",
				result.contains("q=(((catalogId:TestCatalog1) AND (version:Online)) OR ((catalogId:TestCatalog2) AND (version:Staged)))"));
	}

	@Test
	public void testConvertQueryWithComplicatedCoupledFields() throws Exception
	{
		indexedProperty.setFacetType(FacetType.REFINE);

		final Set<String> catalog1Values = new LinkedHashSet<String>(3);
		catalog1Values.add("TestCatalog1A");
		catalog1Values.add("TestCatalog1B");
		catalog1Values.add("TestCatalog1C");
		final QueryField queryField1 = new QueryField("catalogId", Operator.AND, catalog1Values);

		final Set<String> versions1 = new LinkedHashSet<String>(3);
		versions1.add("version1");
		versions1.add("version2");
		versions1.add("version3");
		final QueryField queryField2 = new QueryField("version", Operator.OR, versions1);

		final Set<String> catalog2Values = new LinkedHashSet<String>(3);
		catalog2Values.add("TestCatalog2A");
		catalog2Values.add("TestCatalog2B");
		catalog2Values.add("TestCatalog2C");
		final QueryField queryField3 = new QueryField("catalogId", Operator.OR, catalog2Values);

		final Set<String> versions2 = new LinkedHashSet<String>(3);
		versions2.add("versionA");
		versions2.add("versionB");
		versions2.add("versionC");
		final QueryField queryField4 = new QueryField("version", Operator.AND, versions2);

		final CoupledQueryField couple1 = new CoupledQueryField("testCouple", queryField1, queryField2, Operator.AND, Operator.AND);
		final CoupledQueryField couple2 = new CoupledQueryField("testCouple", queryField3, queryField4, Operator.OR, Operator.AND);

		searchQuery.addCoupledFields(couple1);
		searchQuery.addCoupledFields(couple2);

		String result = defaultSolrQueryConverter.convertSolrQuery(searchQuery).toString();
		result = URLDecoder.decode(result, "UTF-8");

		assertTrue(
				"q not as expected",
				result.contains("q=(((catalogId:(TestCatalog1A AND TestCatalog1B AND TestCatalog1C)) AND (version:(version1 OR version2 OR version3))) AND ((catalogId:(TestCatalog2A OR TestCatalog2B OR TestCatalog2C)) OR (version:(versionA AND versionB AND versionC))))"));
	}
}

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import de.hybris.platform.solrfacetsearch.provider.QualifierProviderAware;
import de.hybris.platform.solrfacetsearch.provider.ValueProviderSelectionStrategy;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;


@UnitTest
public class DefaultFieldNameTranslatorTest
{
	protected static final String FIELD_NAME = "field";
	protected static final String FIELD_QUALIFIER = "qual";
	protected static final String FIELD_TYPE = "string";
	protected static final String FIELD_SEPARATOR = "_";

	protected static final String VALUE_PROVIDER_ID = "valueProvider";
	protected static final String TYPE_VALUE_PROVIDER_ID = "typeValueProvider";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private FieldNameProvider fieldNameProvider;

	@Mock
	private ValueProviderSelectionStrategy valueProviderSelectionStrategy;

	@Mock
	private QualifierProviderAware valueProvider;

	@Mock
	private QualifierProvider qualifierProvider;

	@Mock
	private Qualifier qualifier;

	@Mock
	private BeanFactory beanFactory;

	private IndexedType indexedType;
	private SearchQuery searchQuery;

	private DefaultFieldNameTranslator defaultFieldNameTranslator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();

		indexedType = new IndexedType();
		indexedType.setIndexedProperties(new HashMap<String, IndexedProperty>());

		searchQuery = new SearchQuery(facetSearchConfig, indexedType);

		defaultFieldNameTranslator = new DefaultFieldNameTranslator();
		defaultFieldNameTranslator.setFieldNameProvider(fieldNameProvider);
		defaultFieldNameTranslator.setValueProviderSelectionStrategy(valueProviderSelectionStrategy);
		defaultFieldNameTranslator.setBeanFactory(beanFactory);
	}

	@Test
	public void translateFromPropertyWithQualifierProvider() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setExportId(FIELD_NAME);
		indexedProperty.setName(FIELD_NAME);
		indexedProperty.setType(FIELD_TYPE);
		indexedProperty.setFieldValueProvider(VALUE_PROVIDER_ID);

		indexedType.getIndexedProperties().put(FIELD_NAME, indexedProperty);

		when(valueProviderSelectionStrategy.resolveValueProvider(indexedType, indexedProperty)).thenReturn(VALUE_PROVIDER_ID);
		when(valueProviderSelectionStrategy.getValueProvider(VALUE_PROVIDER_ID)).thenReturn(valueProvider);
		when(valueProvider.getQualifierProvider()).thenReturn(qualifierProvider);
		when(qualifierProvider.getCurrentQualifier()).thenReturn(qualifier);
		when(qualifierProvider.canApply(indexedProperty)).thenReturn(true);
		when(qualifier.toFieldQualifier()).thenReturn(FIELD_QUALIFIER);

		final String expectedFieldName = FIELD_NAME + FIELD_SEPARATOR + FIELD_QUALIFIER + FIELD_SEPARATOR + FIELD_TYPE;
		when(fieldNameProvider.getFieldName(indexedProperty, FIELD_QUALIFIER, FieldType.INDEX)).thenReturn(expectedFieldName);

		// when
		final String translatedFieldName = defaultFieldNameTranslator.translate(searchQuery, FIELD_NAME, FieldType.INDEX);

		// then
		assertEquals(expectedFieldName, translatedFieldName);
	}

	@Test
	public void translateFromLocalizedPropertyWithoutQualifierProvider() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setExportId(FIELD_NAME);
		indexedProperty.setName(FIELD_NAME);
		indexedProperty.setType(FIELD_TYPE);
		indexedProperty.setLocalized(true);

		indexedType.getIndexedProperties().put(FIELD_NAME, indexedProperty);

		final String expectedFieldName = FIELD_NAME + FIELD_SEPARATOR + FIELD_QUALIFIER + FIELD_SEPARATOR + FIELD_TYPE;
		when(fieldNameProvider.getFieldName(indexedProperty, FIELD_QUALIFIER, FieldType.INDEX)).thenReturn(expectedFieldName);

		searchQuery.setLanguage(FIELD_QUALIFIER);

		// when
		final String translatedFieldName = defaultFieldNameTranslator.translate(searchQuery, FIELD_NAME, FieldType.INDEX);

		// then
		assertEquals(expectedFieldName, translatedFieldName);
	}

	@Test
	public void translateFromCurrencyPropertyWithoutQualifierProvider() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setExportId(FIELD_NAME);
		indexedProperty.setName(FIELD_NAME);
		indexedProperty.setType(FIELD_TYPE);
		indexedProperty.setCurrency(true);

		indexedType.getIndexedProperties().put(FIELD_NAME, indexedProperty);

		final String expectedFieldName = FIELD_NAME + FIELD_SEPARATOR + FIELD_QUALIFIER + FIELD_SEPARATOR + FIELD_TYPE;
		when(fieldNameProvider.getFieldName(indexedProperty, FIELD_QUALIFIER, FieldType.INDEX)).thenReturn(expectedFieldName);

		searchQuery.setCurrency(FIELD_QUALIFIER);

		// when
		final String translatedFieldName = defaultFieldNameTranslator.translate(searchQuery, FIELD_NAME, FieldType.INDEX);

		// then
		assertEquals(expectedFieldName, translatedFieldName);
	}

	@Test
	public void translateFromPropertyWithoutQualifierProvider() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setExportId(FIELD_NAME);
		indexedProperty.setName(FIELD_NAME);
		indexedProperty.setType(FIELD_TYPE);

		indexedType.getIndexedProperties().put(FIELD_NAME, indexedProperty);

		final String expectedFieldName = FIELD_NAME + FIELD_SEPARATOR + FIELD_TYPE;
		when(fieldNameProvider.getFieldName(indexedProperty, null, FieldType.INDEX)).thenReturn(expectedFieldName);

		// when
		final String translatedFieldName = defaultFieldNameTranslator.translate(searchQuery, FIELD_NAME, FieldType.INDEX);

		// then
		assertEquals(expectedFieldName, translatedFieldName);
	}

	@Test
	public void translateFromTypeWithMapping() throws Exception
	{
		// given
		indexedType.setFieldsValuesProvider(TYPE_VALUE_PROVIDER_ID);

		final String expectedFieldName = FIELD_NAME + FIELD_SEPARATOR + FIELD_TYPE;

		final IndexedTypeFieldsValuesProvider typeValueProvider = mock(IndexedTypeFieldsValuesProvider.class);

		when(beanFactory.getBean(TYPE_VALUE_PROVIDER_ID)).thenReturn(typeValueProvider);
		when(typeValueProvider.getFieldNamesMapping()).thenReturn(Collections.singletonMap(FIELD_NAME, expectedFieldName));

		// when
		final String translatedFieldName = defaultFieldNameTranslator.translate(searchQuery, FIELD_NAME, FieldType.INDEX);

		// then
		assertEquals(expectedFieldName, translatedFieldName);
	}

	@Test
	public void translateFromTypeWithoutMapping() throws Exception
	{
		// given
		final String expectedFieldName = FIELD_NAME;

		// when
		final String translatedFieldName = defaultFieldNameTranslator.translate(searchQuery, FIELD_NAME, FieldType.INDEX);

		// then
		assertEquals(expectedFieldName, translatedFieldName);
	}
}

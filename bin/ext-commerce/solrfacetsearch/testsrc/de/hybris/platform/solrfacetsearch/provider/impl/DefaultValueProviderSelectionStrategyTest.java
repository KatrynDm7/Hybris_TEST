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
package de.hybris.platform.solrfacetsearch.provider.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


@UnitTest
public class DefaultValueProviderSelectionStrategyTest
{
	private static final String DEFAULT_VALUE_PROVIDER_ID = "defaulrValueProviderId";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private ApplicationContext applicationContext;

	private DefaultValueProviderSelectionStrategy valueProviderSelectionStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		valueProviderSelectionStrategy = new DefaultValueProviderSelectionStrategy();
		valueProviderSelectionStrategy.setDefaultValueProviderId(DEFAULT_VALUE_PROVIDER_ID);
		valueProviderSelectionStrategy.setApplicationContext(applicationContext);
	}

	@Test
	public void getValueProvider() throws Exception
	{
		// given
		final String valueProviderId = "valueProviderId";
		final Object expectedValueProvider = new Object();

		Mockito.when(valueProviderSelectionStrategy.getValueProvider(valueProviderId)).thenReturn(expectedValueProvider);

		// when
		final Object valueProvider = valueProviderSelectionStrategy.getValueProvider(valueProviderId);

		// then
		assertSame(expectedValueProvider, valueProvider);
	}

	@Test
	public void resolveValueProviderFromIndexedProperty() throws Exception
	{
		// given
		final String indexedPropertyValueProviderId = "indexedPropertyValueProviderId";
		final String indexedTypeValueProviderId = "indexedPropertyValueProviderId";

		final IndexedType indexedType = new IndexedType();
		indexedType.setDefaultFieldValueProvider(indexedTypeValueProviderId);

		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setFieldValueProvider(indexedPropertyValueProviderId);

		// when
		final String valueProviderId = valueProviderSelectionStrategy.resolveValueProvider(indexedType, indexedProperty);

		// then
		assertSame(indexedTypeValueProviderId, valueProviderId);
	}

	@Test
	public void resolveValueProviderFromIndexedType() throws Exception
	{
		// given
		final String indexedTypeValueProviderId = "indexedPropertyValueProviderId";

		final IndexedType indexedType = new IndexedType();
		indexedType.setDefaultFieldValueProvider(indexedTypeValueProviderId);

		final IndexedProperty indexedProperty = new IndexedProperty();

		// when
		final String valueProviderId = valueProviderSelectionStrategy.resolveValueProvider(indexedType, indexedProperty);

		// then
		assertSame(indexedTypeValueProviderId, valueProviderId);
	}

	@Test
	public void resolveDefaultValueProvider() throws Exception
	{
		// given
		final IndexedType indexedType = new IndexedType();
		final IndexedProperty indexedProperty = new IndexedProperty();

		// when
		final String valueProviderId = valueProviderSelectionStrategy.resolveValueProvider(indexedType, indexedProperty);

		// then
		assertSame(DEFAULT_VALUE_PROVIDER_ID, valueProviderId);
	}

	@Test
	public void resolveValueProvidersWithSameId() throws Exception
	{
		// given
		final String valueProviderId = "valueProviderId";

		final IndexedType indexedType = new IndexedType();

		final IndexedProperty indexedProperty1 = new IndexedProperty();
		indexedProperty1.setFieldValueProvider(valueProviderId);

		final IndexedProperty indexedProperty2 = new IndexedProperty();
		indexedProperty2.setFieldValueProvider(valueProviderId);

		final Collection<IndexedProperty> indexedProperties = Arrays.asList(indexedProperty1, indexedProperty2);

		// when
		final Map<String, Collection<IndexedProperty>> valueProviders = valueProviderSelectionStrategy.resolveValueProviders(
				indexedType, indexedProperties);

		// then
		assertEquals(1, valueProviders.size());
		assertEquals(2, valueProviders.get(valueProviderId).size());
		assertThat(valueProviders.get(valueProviderId), hasItems(indexedProperty1, indexedProperty2));
	}

	@Test
	public void resolveValueProvidersWithDifferentIds() throws Exception
	{
		// given
		final String valueProviderId1 = "valueProviderId1";
		final String valueProviderId2 = "valueProviderId2";

		final IndexedType indexedType = new IndexedType();

		final IndexedProperty indexedProperty1 = new IndexedProperty();
		indexedProperty1.setFieldValueProvider(valueProviderId1);

		final IndexedProperty indexedProperty2 = new IndexedProperty();
		indexedProperty2.setFieldValueProvider(valueProviderId2);

		final Collection<IndexedProperty> indexedProperties = Arrays.asList(indexedProperty1, indexedProperty2);

		// when
		final Map<String, Collection<IndexedProperty>> valueProviders = valueProviderSelectionStrategy.resolveValueProviders(
				indexedType, indexedProperties);

		// then
		assertEquals(2, valueProviders.size());
		assertEquals(1, valueProviders.get(valueProviderId1).size());
		assertEquals(1, valueProviders.get(valueProviderId2).size());
		assertThat(valueProviders.get(valueProviderId1), hasItem(indexedProperty1));
		assertThat(valueProviders.get(valueProviderId2), hasItem(indexedProperty2));
	}
}

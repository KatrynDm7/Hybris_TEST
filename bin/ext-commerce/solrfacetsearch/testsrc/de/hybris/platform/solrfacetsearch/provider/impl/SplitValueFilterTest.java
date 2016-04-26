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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class SplitValueFilterTest
{
	protected static final String INDEXED_PROPERTY_NAME = "indexedProperty";

	@Mock
	private IndexerBatchContext batchContext;

	private SplitValueFilter splitValueFilter;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		splitValueFilter = new SplitValueFilter();
	}

	protected IndexedProperty getIndexedProperty()
	{
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setName(INDEXED_PROPERTY_NAME);
		indexedProperty.setValueProviderParameters(new HashMap<String, String>());
		indexedProperty.setLocalized(false);

		return indexedProperty;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void resolveWithSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Object attributeValue = "a b";

		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());

		// when
		final Object splittedValue = splitValueFilter.doFilter(batchContext, indexedProperty, attributeValue);

		// then
		Assert.assertTrue(splittedValue instanceof Collection);

		final Collection<String> splittedValues = (Collection<String>) splittedValue;

		assertEquals(2, splittedValues.size());
		assertThat(splittedValues, hasItems("a", "b"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void resolveWithSplitRegex() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Object attributeValue = "a/b";

		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());
		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_REGEX_PARAM, "/");

		// when
		final Object splittedValue = splitValueFilter.doFilter(batchContext, indexedProperty, attributeValue);

		// then
		Assert.assertTrue(splittedValue instanceof Collection);

		final List<String> splittedValues = (List<String>) splittedValue;

		assertEquals(2, splittedValues.size());
		assertThat(splittedValues, hasItems("a", "b"));
	}

	@Test
	public void resolveNonStringAttributeWithSplit() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = getIndexedProperty();
		final Object attributeValue = new Object();

		indexedProperty.getValueProviderParameters().put(SplitValueFilter.SPLIT_PARAM, Boolean.TRUE.toString());

		// when
		final Object splittedValue = splitValueFilter.doFilter(batchContext, indexedProperty, attributeValue);

		// then
		Assert.assertEquals(splittedValue, attributeValue);
	}
}

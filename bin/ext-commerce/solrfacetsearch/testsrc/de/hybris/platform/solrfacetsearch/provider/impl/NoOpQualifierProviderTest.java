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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;

import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@UnitTest
public class NoOpQualifierProviderTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	private NoOpQualifierProvider qualifierProvider;

	@Before
	public void setUp()
	{
		qualifierProvider = new NoOpQualifierProvider();
	}

	@Test
	public void emptyAvailableQualifiers() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexConfig indexConfig = new IndexConfig();
		final IndexedType indexedType = new IndexedType();

		facetSearchConfig.setIndexConfig(indexConfig);

		// when
		final Collection<Qualifier> qualifiers = qualifierProvider.getAvailableQualifiers(facetSearchConfig, indexedType);

		// then
		assertTrue(qualifiers.isEmpty());
	}

	@Test
	public void cannotApply() throws Exception
	{
		// given
		final IndexedProperty indexedProperty = new IndexedProperty();

		// when
		final boolean canApply = qualifierProvider.canApply(indexedProperty);

		// then
		assertFalse(canApply);
	}

	@Test
	public void applyQualifierDoesNotThrowException() throws Exception
	{
		// given
		final Qualifier qualifier = mock(Qualifier.class);

		// when
		qualifierProvider.applyQualifier(qualifier);
	}

	@Test
	public void getCurrentQualifierReturnsNull() throws Exception
	{
		// when
		final Qualifier qualifier = qualifierProvider.getCurrentQualifier();

		// then
		assertNull(qualifier);
	}
}

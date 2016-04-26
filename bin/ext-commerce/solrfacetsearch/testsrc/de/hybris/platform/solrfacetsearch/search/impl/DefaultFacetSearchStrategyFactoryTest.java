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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategyFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultFacetSearchStrategyFactoryTest
{
	@Mock
	private FacetSearchConfig facetSearchConfig;

	@Mock
	private SearchConfig searchConfig;

	@Mock
	private IndexedType indexedType;

	@Mock
	private DefaultFacetSearchStrategy defaultFacetSearchStrategy;

	@Mock
	private LegacyFacetSearchStrategy legacyFacetSearchStrategy;

	private FacetSearchStrategyFactory facetSearchStrategyFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(facetSearchConfig.getSearchConfig()).thenReturn(searchConfig);

		facetSearchStrategyFactory = new DefaultFacetSearchStrategyFactory();
		((DefaultFacetSearchStrategyFactory) facetSearchStrategyFactory).setDefaultFacetSearchStrategy(defaultFacetSearchStrategy);
		((DefaultFacetSearchStrategyFactory) facetSearchStrategyFactory).setLegacyFacetSearchStrategy(legacyFacetSearchStrategy);
	}

	@Test
	public void createDefaultSearchFactory()
	{
		// given
		when(facetSearchConfig.getSearchConfig().isLegacyMode()).thenReturn(false);

		// when
		final FacetSearchStrategy defualtFacetSearchStrategy = facetSearchStrategyFactory.createStrategy(facetSearchConfig,
				indexedType);

		// then
		assertTrue(defualtFacetSearchStrategy instanceof DefaultFacetSearchStrategy);
	}

	@Test
	public void createLegacySearchFactory()
	{
		// given
		when(facetSearchConfig.getSearchConfig().isLegacyMode()).thenReturn(true);

		// when
		final FacetSearchStrategy searchStrategy = facetSearchStrategyFactory.createStrategy(facetSearchConfig, indexedType);

		// then
		assertSame(legacyFacetSearchStrategy, searchStrategy);
	}
}

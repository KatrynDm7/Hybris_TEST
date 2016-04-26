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
package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;


@UnitTest
public class DefaultIndexerStrategyFactoryTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private ApplicationContext applicationContext;

	private FacetSearchConfig facetSearchConfig;

	private DefaultIndexerStrategyFactory indexerStrategyFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		facetSearchConfig = new FacetSearchConfig();

		indexerStrategyFactory = new DefaultIndexerStrategyFactory();
		indexerStrategyFactory.setApplicationContext(applicationContext);
	}

	@Test
	public void createIndexerStrategy() throws Exception
	{
		// given
		final String indexerStrategyBeanId = "indexerStrategy";
		final DefaultIndexerStrategy expectedIndexerStrategy = mock(DefaultIndexerStrategy.class);

		indexerStrategyFactory.setIndexerStrategyBeanId(indexerStrategyBeanId);
		when(applicationContext.getBean(indexerStrategyBeanId, IndexerStrategy.class)).thenReturn(expectedIndexerStrategy);

		// when
		final IndexerStrategy indexerStrategy = indexerStrategyFactory.createIndexerStrategy(facetSearchConfig);

		// then
		assertSame(expectedIndexerStrategy, indexerStrategy);
	}

	@Test
	public void shouldFailToCreateIndexerStrategy() throws Exception
	{
		// given
		final String indexerStrategyBeanId = "indexerStrategy";
		final BeansException exception = new NoSuchBeanDefinitionException(indexerStrategyBeanId);

		indexerStrategyFactory.setIndexerStrategyBeanId(indexerStrategyBeanId);
		when(applicationContext.getBean(indexerStrategyBeanId, IndexerStrategy.class)).thenThrow(exception);

		// expect
		expectedException.expect(IndexerException.class);

		// when
		indexerStrategyFactory.createIndexerStrategy(facetSearchConfig);
	}
}

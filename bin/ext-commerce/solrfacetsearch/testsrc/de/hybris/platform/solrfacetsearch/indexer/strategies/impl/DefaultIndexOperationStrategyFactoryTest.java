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
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.enums.IndexMode;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexOperationStrategy;

import java.util.Collections;
import java.util.Map;

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
public class DefaultIndexOperationStrategyFactoryTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private ApplicationContext applicationContext;

	private IndexConfig indexConfig;
	private FacetSearchConfig facetSearchConfig;

	private DefaultIndexOperationStrategyFactory indexOperationStrategyFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		indexConfig = new IndexConfig();
		indexConfig.setIndexMode(IndexMode.DIRECT);

		facetSearchConfig = new FacetSearchConfig();
		facetSearchConfig.setIndexConfig(indexConfig);

		indexOperationStrategyFactory = new DefaultIndexOperationStrategyFactory();
		indexOperationStrategyFactory.setApplicationContext(applicationContext);
	}

	@Test
	public void createIndexerStrategy() throws Exception
	{
		// given
		final IndexOperation indexOperation = IndexOperation.UPDATE;
		final String strategyKey = indexOperation.name();
		final String strategyBeanId = "indexOperationStrategy";
		final Map<String, String> indexOperationStrategiesMapping = Collections.singletonMap(strategyKey, strategyBeanId);
		final IndexOperationStrategy expectedIndexOperationStrategy = mock(AbstractIndexOperationStrategy.class);

		indexOperationStrategyFactory.setIndexOperationStrategiesMapping(indexOperationStrategiesMapping);
		when(applicationContext.getBean(strategyBeanId, IndexOperationStrategy.class)).thenReturn(expectedIndexOperationStrategy);

		// when
		final IndexOperationStrategy indexOperationStrategy = indexOperationStrategyFactory.createIndexOperationStrategy(
				indexOperation, facetSearchConfig);

		// then
		assertSame(expectedIndexOperationStrategy, indexOperationStrategy);
	}

	@Test
	public void createIndexerStrategyForFullOperation() throws Exception
	{
		// given
		final IndexOperation indexOperation = IndexOperation.FULL;
		final String strategyKey = indexOperation.name() + DefaultIndexOperationStrategyFactory.SEPARATOR
				+ indexConfig.getIndexMode().name();
		final String strategyBeanId = "indexOperationStrategy";
		final Map<String, String> indexOperationStrategiesMapping = Collections.singletonMap(strategyKey, strategyBeanId);
		final IndexOperationStrategy expectedIndexOperationStrategy = mock(AbstractIndexOperationStrategy.class);

		indexOperationStrategyFactory.setIndexOperationStrategiesMapping(indexOperationStrategiesMapping);
		when(applicationContext.getBean(strategyBeanId, IndexOperationStrategy.class)).thenReturn(expectedIndexOperationStrategy);

		// when
		final IndexOperationStrategy indexOperationStrategy = indexOperationStrategyFactory.createIndexOperationStrategy(
				indexOperation, facetSearchConfig);

		// then
		assertSame(expectedIndexOperationStrategy, indexOperationStrategy);
	}

	@Test
	public void shouldFailToCreateIndexOperationStrategy() throws Exception
	{
		// given
		final IndexOperation indexOperation = IndexOperation.UPDATE;
		final String strategyKey = indexOperation.name();
		final String strategyBeanId = "indexOperationStrategy";
		final Map<String, String> indexOperationStrategiesMapping = Collections.singletonMap(strategyKey, strategyBeanId);
		final BeansException exception = new NoSuchBeanDefinitionException(strategyBeanId);

		indexOperationStrategyFactory.setIndexOperationStrategiesMapping(indexOperationStrategiesMapping);
		when(applicationContext.getBean(strategyBeanId, IndexOperationStrategy.class)).thenThrow(exception);

		// expect
		expectedException.expect(IndexerException.class);

		// when
		indexOperationStrategyFactory.createIndexOperationStrategy(indexOperation, facetSearchConfig);
	}

	@Test
	public void shouldFailBecauseNoBeanIdWasFound() throws Exception
	{
		// given
		final IndexOperation indexOperation = IndexOperation.FULL;
		final Map<String, String> indexOperationStrategiesMapping = Collections.emptyMap();

		indexOperationStrategyFactory.setIndexOperationStrategiesMapping(indexOperationStrategiesMapping);

		// expect
		expectedException.expect(IndexerException.class);

		// when
		indexOperationStrategyFactory.createIndexOperationStrategy(indexOperation, facetSearchConfig);
	}
}

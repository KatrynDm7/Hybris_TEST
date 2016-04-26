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
package de.hybris.platform.solrfacetsearch.indexer.workers.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorker;

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
public class DefaultIndexerWorkerFactoryTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private ApplicationContext applicationContext;

	private FacetSearchConfig facetSearchConfig;

	private DefaultIndexerWorkerFactory indexerWorkerFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		facetSearchConfig = new FacetSearchConfig();

		indexerWorkerFactory = new DefaultIndexerWorkerFactory();
		indexerWorkerFactory.setApplicationContext(applicationContext);
	}

	@Test
	public void createIndexerWorker() throws Exception
	{
		// given
		final String workerBeanId = "indexerWorker";
		final DefaultIndexerWorker expectedIndexerWorker = mock(DefaultIndexerWorker.class);

		indexerWorkerFactory.setWorkerBeanId(workerBeanId);
		when(applicationContext.getBean(workerBeanId, IndexerWorker.class)).thenReturn(expectedIndexerWorker);

		// when
		final IndexerWorker indexerWorker = indexerWorkerFactory.createIndexerWorker(facetSearchConfig);

		// then
		assertSame(expectedIndexerWorker, indexerWorker);
	}

	@Test
	public void shouldFailToCreateIndexerWorker() throws Exception
	{
		// given
		final String workerBeanId = "indexerWorker";
		final BeansException exception = new NoSuchBeanDefinitionException(workerBeanId);

		indexerWorkerFactory.setWorkerBeanId(workerBeanId);
		when(applicationContext.getBean(workerBeanId, IndexerWorker.class)).thenThrow(exception);

		// expect
		expectedException.expect(IndexerException.class);

		// when
		indexerWorkerFactory.createIndexerWorker(facetSearchConfig);
	}
}

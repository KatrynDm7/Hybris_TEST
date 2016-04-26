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
package de.hybris.platform.solrfacetsearch.indexer.cron;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerJobException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrExtIndexerCronJobModel;
import de.hybris.platform.solrfacetsearch.provider.ContextAwareParameterProvider;
import de.hybris.platform.solrfacetsearch.provider.CronJobAwareParameterProvider;
import de.hybris.platform.solrfacetsearch.provider.ParameterProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;


@UnitTest
public class SolrExtIndexerJobTest
{
	private static final String FACET_SEARCH_CONFIG_NAME = "testConfig";
	private static final String INDEXED_TYPE_NAME = "Product";
	private static final String QUERY = "SELECT {pk} FROM {Product}";
	private static final String QUERY_PARAMETER_NAME = "parameter";
	private static final String PARAMETER_PROVIDER_NAME = "parameterProvider";

	@Mock
	private FacetSearchConfigService facetSearchConfigService;

	@Mock
	private IndexerService indexerService;

	@Mock
	private IndexerQueriesExecutor indexerQueriesExecutor;

	@Mock
	private BeanFactory beanFactory;

	@Mock
	private SolrFacetSearchConfigModel solrFacetSearchConfig;

	@Mock
	private SolrExtIndexerCronJobModel solrExtIndexerCronJob;

	@Mock
	private CatalogVersionModel catalogVersion;

	private FacetSearchConfig facetSearchConfig;
	private IndexConfig indexConfig;
	private IndexedType indexedType;

	private SolrExtIndexerJob<SolrExtIndexerCronJobModel> solrExtIndexerJob;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		indexedType = new IndexedType();

		final Map<String, IndexedType> indexedTypes = new HashMap<>();
		indexedTypes.put(INDEXED_TYPE_NAME, indexedType);

		indexConfig = new IndexConfig();
		indexConfig.setIndexedTypes(indexedTypes);
		indexConfig.setCatalogVersions(Collections.singletonList(catalogVersion));

		facetSearchConfig = new FacetSearchConfig();
		facetSearchConfig.setIndexConfig(indexConfig);

		when(solrFacetSearchConfig.getName()).thenReturn(FACET_SEARCH_CONFIG_NAME);
		when(facetSearchConfigService.getConfiguration(FACET_SEARCH_CONFIG_NAME)).thenReturn(facetSearchConfig);

		when(solrExtIndexerCronJob.getFacetSearchConfig()).thenReturn(solrFacetSearchConfig);
		when(solrExtIndexerCronJob.getIndexedType()).thenReturn(INDEXED_TYPE_NAME);
		when(solrExtIndexerCronJob.getQuery()).thenReturn(QUERY);
		when(solrExtIndexerCronJob.getIndexerOperation()).thenReturn(IndexerOperationValues.UPDATE);

		solrExtIndexerJob = new SolrExtIndexerJob<SolrExtIndexerCronJobModel>();
		solrExtIndexerJob.setFacetSearchConfigService(facetSearchConfigService);
		solrExtIndexerJob.setIndexerService(indexerService);
		solrExtIndexerJob.setIndexerQueriesExecutor(indexerQueriesExecutor);
		solrExtIndexerJob.setBeanFactory(beanFactory);
	}

	@Test
	public void executeQueryWithParameters() throws FacetConfigServiceException, IndexerJobException, IndexerException
	{
		// given
		final String query = QUERY;
		final Map<String, Object> queryParameters = Collections.singletonMap(QUERY_PARAMETER_NAME, new Object());
		final ParameterProvider parameterProvider = mock(ParameterProvider.class);
		when(solrExtIndexerCronJob.getQueryParameterProvider()).thenReturn(PARAMETER_PROVIDER_NAME);
		when(beanFactory.getBean(PARAMETER_PROVIDER_NAME)).thenReturn(parameterProvider);

		when(parameterProvider.createParameters()).thenReturn(queryParameters);

		// when
		solrExtIndexerJob.perform(solrExtIndexerCronJob);

		// then
		verify(indexerQueriesExecutor).getPks(facetSearchConfig, indexedType, query, queryParameters);
	}

	@Test
	public void executeQueryWithContextAwareParameters() throws FacetConfigServiceException, IndexerJobException, IndexerException
	{
		// given
		final String query = QUERY;
		final Map<String, Object> queryParameters = Collections.singletonMap(QUERY_PARAMETER_NAME, new Object());
		final ContextAwareParameterProvider parameterProvider = mock(ContextAwareParameterProvider.class);
		when(solrExtIndexerCronJob.getQueryParameterProvider()).thenReturn(PARAMETER_PROVIDER_NAME);
		when(beanFactory.getBean(PARAMETER_PROVIDER_NAME)).thenReturn(parameterProvider);

		when(parameterProvider.createParameters(indexConfig, indexedType)).thenReturn(queryParameters);

		// when
		solrExtIndexerJob.perform(solrExtIndexerCronJob);

		// then
		verify(indexerQueriesExecutor).getPks(facetSearchConfig, indexedType, query, queryParameters);
	}

	@Test
	public void executeQueryWithCronJobAwareParameters() throws FacetConfigServiceException, IndexerJobException, IndexerException
	{
		// given
		final String query = QUERY;
		final Map<String, Object> queryParameters = Collections.singletonMap(QUERY_PARAMETER_NAME, new Object());
		final CronJobAwareParameterProvider parameterProvider = mock(CronJobAwareParameterProvider.class);
		when(solrExtIndexerCronJob.getQueryParameterProvider()).thenReturn(PARAMETER_PROVIDER_NAME);
		when(beanFactory.getBean(PARAMETER_PROVIDER_NAME)).thenReturn(parameterProvider);

		when(parameterProvider.createParameters(solrExtIndexerCronJob, indexConfig, indexedType)).thenReturn(queryParameters);

		// when
		solrExtIndexerJob.perform(solrExtIndexerCronJob);

		// then
		verify(indexerQueriesExecutor).getPks(facetSearchConfig, indexedType, query, queryParameters);
	}
}

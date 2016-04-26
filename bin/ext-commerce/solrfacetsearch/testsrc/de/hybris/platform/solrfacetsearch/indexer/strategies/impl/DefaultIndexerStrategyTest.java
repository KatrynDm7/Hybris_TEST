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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.factories.FlexibleSearchQuerySpecFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerRuntimeException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexOperationIdGenerator;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorker;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorkerFactory;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.IndexNameResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultIndexerStrategyTest
{
	private static final String TEST_TENANT = "testTenant";

	private static final String INDEXED_PROPERTY_NAME = "testName";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private SessionService sessionService;

	@Mock
	private UserService userService;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private FlexibleSearchQuerySpecFactory flexibleSearchQuerySpecFactory;

	@Mock
	private IndexerQueriesExecutor indexerQueriesExecutor;

	@Mock
	private IndexerContextFactory<IndexerContext> indexerContextFactory;

	@Mock
	private IndexerWorkerFactory indexerWorkerFactory;

	@Mock
	private IndexOperationIdGenerator indexOperationIdGenerator;

	@Mock
	private IndexNameResolver indexNameResolver;

	@Mock
	private IndexerContext indexerContext;

	@Mock
	private TenantService tenantService;

	@Mock
	private JaloSession jaloSession;

	@Mock
	private IndexerWorker indexerWorker;

	@Mock
	private IndexerWorker failingIndexerWorker;

	private FacetSearchConfig facetSearchConfig;
	private IndexConfig indexConfig;

	private DefaultIndexerStrategy defaultIndexerStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		facetSearchConfig = new FacetSearchConfig();

		final IndexOperation indexOperation = IndexOperation.FULL;
		final long indexOperationId = 1;

		final IndexedType indexedType = new IndexedType();
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setName(INDEXED_PROPERTY_NAME);
		indexedType.setIndexedProperties(Collections.singletonMap(INDEXED_PROPERTY_NAME, indexedProperty));
		final List<IndexedProperty> indexedProperties = Collections.singletonList(indexedProperty);

		indexConfig = new IndexConfig();
		indexConfig.setBatchSize(5);
		indexConfig.setNumberOfThreads(1);
		facetSearchConfig.setIndexConfig(indexConfig);

		defaultIndexerStrategy = new DefaultIndexerStrategy();
		defaultIndexerStrategy.setSessionService(sessionService);
		defaultIndexerStrategy.setUserService(userService);
		defaultIndexerStrategy.setCommonI18NService(commonI18NService);
		defaultIndexerStrategy.setFlexibleSearchQuerySpecFactory(flexibleSearchQuerySpecFactory);
		defaultIndexerStrategy.setIndexerQueriesExecutor(indexerQueriesExecutor);
		defaultIndexerStrategy.setIndexerWorkerFactory(indexerWorkerFactory);
		defaultIndexerStrategy.setIndexOperationIdGenerator(indexOperationIdGenerator);
		defaultIndexerStrategy.setIndexNameResolver(indexNameResolver);
		defaultIndexerStrategy.setTenantService(tenantService);
		defaultIndexerStrategy.setIndexOperation(indexOperation);
		defaultIndexerStrategy.setIndexedType(indexedType);
		defaultIndexerStrategy.setFacetSearchConfig(facetSearchConfig);
		defaultIndexerStrategy.setIndexerContextFactory(indexerContextFactory);
		defaultIndexerStrategy.setIndexedProperties(indexedProperties);
		defaultIndexerStrategy.setFacetSearchConfig(facetSearchConfig);

		final List<PK> pks = new ArrayList<PK>(10);
		for (int i = 0; i < 10; i++)
		{
			pks.add(PK.createFixedCounterPK(1, i + 1));
		}

		defaultIndexerStrategy.setPks(pks);

		when(
				indexOperationIdGenerator.generate(Mockito.any(FacetSearchConfig.class), Mockito.any(IndexedType.class),
						Mockito.any(Index.class))).thenReturn(indexOperationId);
		when(
				indexerContextFactory.createContext(indexOperationId, indexOperation, true, facetSearchConfig, indexedType,
						indexedProperties)).thenReturn(indexerContext);
		when(indexerContext.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		when(userService.getCurrentUser()).thenReturn(mock(UserModel.class));
		when(commonI18NService.getCurrentLanguage()).thenReturn(mock(LanguageModel.class));
		when(commonI18NService.getCurrentCurrency()).thenReturn(mock(CurrencyModel.class));
		when(tenantService.getCurrentTenantId()).thenReturn(TEST_TENANT);
		when(indexerContext.getIndexedType()).thenReturn(indexedType);
		when(indexerContext.getIndex()).thenReturn(mock(Index.class));
		when(indexerContext.getPks()).thenReturn(pks);
		when(sessionService.getRawSession(any())).thenReturn(jaloSession);

		doThrow(new IndexerRuntimeException()).when(failingIndexerWorker).run();
	}

	@Test
	public void testExecuteIndexerWorkersNoRetries() throws Exception
	{
		// given
		indexConfig.setMaxRetries(0);
		indexConfig.setMaxBatchRetries(2);

		when(indexerWorkerFactory.createIndexerWorker(facetSearchConfig)).thenReturn(failingIndexerWorker)
				.thenReturn(indexerWorker).thenReturn(indexerWorker);

		// expect
		expectedException.expect(IndexerException.class);

		// when
		defaultIndexerStrategy.execute();
	}

	@Test
	public void testExecuteIndexerWorkersMaxRetries() throws Exception
	{
		// given
		indexConfig.setMaxRetries(1);
		indexConfig.setMaxBatchRetries(1);

		when(indexerWorkerFactory.createIndexerWorker(facetSearchConfig)).thenReturn(failingIndexerWorker)
				.thenReturn(indexerWorker).thenReturn(indexerWorker);

		// when
		defaultIndexerStrategy.execute();
	}

	@Test
	public void testExecuteIndexerWorkersMaxBatchRetries() throws Exception
	{
		// given
		indexConfig.setMaxRetries(3);
		indexConfig.setMaxBatchRetries(1);

		when(indexerWorkerFactory.createIndexerWorker(facetSearchConfig)).thenReturn(failingIndexerWorker)
				.thenReturn(indexerWorker).thenReturn(failingIndexerWorker).thenReturn(indexerWorker);

		// expect
		expectedException.expect(IndexerException.class);

		// when
		defaultIndexerStrategy.execute();
	}

	@Test
	public void testExecuteIndexerWorkersRetries() throws Exception
	{
		// given
		indexConfig.setMaxRetries(3);
		indexConfig.setMaxBatchRetries(2);

		when(indexerWorkerFactory.createIndexerWorker(facetSearchConfig)).thenReturn(failingIndexerWorker)
				.thenReturn(indexerWorker).thenReturn(failingIndexerWorker).thenReturn(indexerWorker);

		// when
		defaultIndexerStrategy.execute();
	}
}

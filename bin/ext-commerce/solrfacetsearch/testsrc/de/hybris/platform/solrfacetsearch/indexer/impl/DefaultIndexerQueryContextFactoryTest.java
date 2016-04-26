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
package de.hybris.platform.solrfacetsearch.indexer.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.common.ListenersFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContext.Status;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultIndexerQueryContextFactoryTest
{
	private static final String QUERY = "SELECT {pk} FROM {Product}";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private SessionService sessionService;

	@Mock
	private JaloSession rawSession;

	@Mock
	private ListenersFactory listenersFactory;

	@Mock
	private IndexerQueryListener listener1;

	@Mock
	private IndexerQueryListener listener2;

	private FacetSearchConfig facetSearchConfig;
	private IndexedType indexedType;
	private String query;
	private Map<String, Object> queryParameters;

	private DefaultIndexerQueryContextFactory indexerQueryContextFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(sessionService.getRawSession(any(Session.class))).thenReturn(rawSession);

		facetSearchConfig = new FacetSearchConfig();
		indexedType = new IndexedType();
		query = QUERY;
		queryParameters = new HashMap<String, Object>();

		indexerQueryContextFactory = new DefaultIndexerQueryContextFactory();
		indexerQueryContextFactory.setSessionService(sessionService);
		indexerQueryContextFactory.setListenersFactory(listenersFactory);
	}

	@Test
	public void createContext() throws Exception
	{
		// when
		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// then
		assertNotNull(queryContext);
		assertEquals(Status.CREATED, queryContext.getStatus());
	}

	@Test
	public void initializeContext() throws Exception
	{
		// given
		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.initializeContext();

		// then
		assertEquals(Status.EXECUTING, queryContext.getStatus());
	}

	@Test
	public void doesNotHaveCurrentContext() throws Exception
	{
		// expect
		expectedException.expect(IllegalStateException.class);

		// when
		indexerQueryContextFactory.getContext();
	}

	@Test
	public void hasCurrentContext() throws Exception
	{
		// given
		final IndexerQueryContext expectedQueryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType,
				query, queryParameters);

		// when
		final IndexerQueryContext queryContext = indexerQueryContextFactory.getContext();

		// then
		assertNotNull(queryContext);
		assertSame(expectedQueryContext, queryContext);
	}

	@Test
	public void destroyContext() throws Exception
	{
		// given
		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.destroyContext();

		// then
		assertEquals(Status.COMPLETED, queryContext.getStatus());
	}

	@Test
	public void destroyContextAfterException() throws Exception
	{
		// given
		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.destroyContext(new RuntimeException()); // NOPMD

		// then
		assertEquals(Status.FAILED, queryContext.getStatus());
	}

	@Test
	public void noListenerConfigured() throws Exception
	{
		// given
		final List<IndexerQueryListener> listeners = Collections.emptyList();

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerQueryListener.class)).thenReturn(listeners);

		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.initializeContext();
		indexerQueryContextFactory.destroyContext();

		// then
		verify(listener1, never()).beforeQuery(queryContext);
		verify(listener2, never()).beforeQuery(queryContext);
		verify(listener2, never()).afterQuery(queryContext);
		verify(listener1, never()).afterQuery(queryContext);
	}

	@Test
	public void runBeforeBatchListener() throws Exception
	{
		// given
		final List<IndexerQueryListener> listeners = Arrays.asList(listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerQueryListener.class)).thenReturn(listeners);

		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.initializeContext();

		// then
		final InOrder inOrder = inOrder(listener1);
		inOrder.verify(listener1).beforeQuery(queryContext);
		inOrder.verify(listener1, never()).afterQuery(queryContext);
	}

	@Test
	public void runListeners() throws Exception
	{
		// given
		final List<IndexerQueryListener> listeners = Arrays.asList(listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerQueryListener.class)).thenReturn(listeners);

		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.initializeContext();
		indexerQueryContextFactory.destroyContext();

		// then
		final InOrder inOrder = inOrder(listener1);
		inOrder.verify(listener1).beforeQuery(queryContext);
		inOrder.verify(listener1).afterQuery(queryContext);
	}

	@Test
	public void runAfterBatchErrorListener() throws Exception
	{
		// given
		final List<IndexerQueryListener> listeners = Arrays.asList(listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerQueryListener.class)).thenReturn(listeners);

		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.initializeContext();
		indexerQueryContextFactory.destroyContext(new Exception());

		// then
		final InOrder inOrder = inOrder(listener1);
		inOrder.verify(listener1).beforeQuery(queryContext);
		inOrder.verify(listener1).afterQueryError(queryContext);
	}

	@Test
	public void listenersRunInCorrectOrder() throws Exception
	{
		// given
		final List<IndexerQueryListener> listeners = Arrays.asList(listener2, listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerQueryListener.class)).thenReturn(listeners);

		final IndexerQueryContext queryContext = indexerQueryContextFactory.createContext(facetSearchConfig, indexedType, query,
				queryParameters);

		// when
		indexerQueryContextFactory.initializeContext();
		indexerQueryContextFactory.destroyContext();

		// then
		final InOrder inOrder = inOrder(listener1, listener2);
		inOrder.verify(listener2).beforeQuery(queryContext);
		inOrder.verify(listener1).beforeQuery(queryContext);
		inOrder.verify(listener1).afterQuery(queryContext);
		inOrder.verify(listener2).afterQuery(queryContext);
	}
}

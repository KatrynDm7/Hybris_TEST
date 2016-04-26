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
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext.Status;
import de.hybris.platform.solrfacetsearch.indexer.IndexerListener;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultIndexerContextFactoryTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private SessionService sessionService;

	@Mock
	private JaloSession rawSession;

	@Mock
	private ListenersFactory listenersFactory;

	@Mock
	private IndexerListener listener1;

	@Mock
	private IndexerListener listener2;

	private long indexOperationId;
	private IndexOperation indexOperation;
	private boolean externalIndexOperation;
	private FacetSearchConfig facetSearchConfig;
	private IndexedType indexedType;
	private Collection<IndexedProperty> indexedProperties;

	private DefaultIndexerContextFactory indexerContextFactory;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(sessionService.getRawSession(any(Session.class))).thenReturn(rawSession);

		indexOperationId = 1;
		indexOperation = IndexOperation.FULL;
		externalIndexOperation = false;
		facetSearchConfig = new FacetSearchConfig();
		indexedType = new IndexedType();
		indexedProperties = Collections.emptyList();

		indexerContextFactory = new DefaultIndexerContextFactory();
		indexerContextFactory.setSessionService(sessionService);
		indexerContextFactory.setListenersFactory(listenersFactory);
	}

	@Test
	public void createContext() throws Exception
	{
		// when
		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// then
		assertNotNull(context);
		assertEquals(Status.CREATED, context.getStatus());
	}

	@Test
	public void initializeContext() throws Exception
	{
		// given
		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.initializeContext();

		// then
		assertEquals(Status.EXECUTING, context.getStatus());
	}

	@Test
	public void doesNotHaveCurrentContext() throws Exception
	{
		// expect
		expectedException.expect(IllegalStateException.class);

		// when
		indexerContextFactory.getContext();
	}

	@Test
	public void hasCurrentContext() throws Exception
	{
		// given
		final IndexerContext expectedContext = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		final IndexerContext context = indexerContextFactory.getContext();

		// then
		assertNotNull(context);
		assertSame(expectedContext, context);
	}

	@Test
	public void destroyContext() throws Exception
	{
		// given
		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.destroyContext();

		// then
		assertEquals(Status.COMPLETED, context.getStatus());
	}

	@Test
	public void destroyContextAfterException() throws Exception
	{
		// given
		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.destroyContext(new RuntimeException()); // NOPMD

		// then
		assertEquals(Status.FAILED, context.getStatus());
	}

	@Test
	public void noListenerConfigured() throws Exception
	{
		// given
		final List<IndexerListener> listeners = Collections.emptyList();

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerListener.class)).thenReturn(listeners);

		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.initializeContext();
		indexerContextFactory.destroyContext();

		// then
		verify(listener1, never()).beforeIndex(context);
		verify(listener2, never()).beforeIndex(context);
		verify(listener2, never()).afterIndex(context);
		verify(listener1, never()).afterIndex(context);
	}

	@Test
	public void runBeforeIndexListener() throws Exception
	{
		// given
		final List<IndexerListener> listeners = Arrays.asList(listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerListener.class)).thenReturn(listeners);

		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.initializeContext();

		// then
		final InOrder inOrder = inOrder(listener1);
		inOrder.verify(listener1).beforeIndex(context);
		inOrder.verify(listener1, never()).afterIndex(context);
	}

	@Test
	public void runListeners() throws Exception
	{
		// given
		final List<IndexerListener> listeners = Arrays.asList(listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerListener.class)).thenReturn(listeners);

		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.initializeContext();
		indexerContextFactory.destroyContext();

		// then
		final InOrder inOrder = inOrder(listener1);
		inOrder.verify(listener1).beforeIndex(context);
		inOrder.verify(listener1).afterIndex(context);
	}

	@Test
	public void runAfterIndexErrorListener() throws Exception
	{
		// given
		final List<IndexerListener> listeners = Arrays.asList(listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerListener.class)).thenReturn(listeners);

		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.initializeContext();
		indexerContextFactory.destroyContext(new Exception());

		// then
		final InOrder inOrder = inOrder(listener1);
		inOrder.verify(listener1).beforeIndex(context);
		inOrder.verify(listener1).afterIndexError(context);
	}

	@Test
	public void listenersRunInCorrectOrder() throws Exception
	{
		// given
		final List<IndexerListener> listeners = Arrays.asList(listener2, listener1);

		when(listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerListener.class)).thenReturn(listeners);

		final IndexerContext context = indexerContextFactory.createContext(indexOperationId, indexOperation,
				externalIndexOperation, facetSearchConfig, indexedType, indexedProperties);

		// when
		indexerContextFactory.initializeContext();
		indexerContextFactory.destroyContext();

		// then
		final InOrder inOrder = inOrder(listener1, listener2);
		inOrder.verify(listener2).beforeIndex(context);
		inOrder.verify(listener1).beforeIndex(context);
		inOrder.verify(listener1).afterIndex(context);
		inOrder.verify(listener2).afterIndex(context);
	}
}

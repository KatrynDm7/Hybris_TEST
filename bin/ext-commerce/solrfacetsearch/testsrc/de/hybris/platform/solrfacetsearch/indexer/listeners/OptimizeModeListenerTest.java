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
package de.hybris.platform.solrfacetsearch.indexer.listeners;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.OptimizeMode;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.Indexer;
import de.hybris.platform.solrfacetsearch.solr.Index;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class OptimizeModeListenerTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private Index index;

	@Mock
	private IndexerContext indexerContext;

	@Mock
	private Indexer indexer;

	private IndexConfig indexConfig;

	private OptimizeModeListener optimizeModeListener;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final IndexOperation indexOperation = IndexOperation.FULL;
		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexedType indexedType = new IndexedType();
		final Collection<IndexedProperty> indexedProperties = Collections.emptyList();
		final List<PK> pks = Collections.emptyList();
		final Map<String, String> indexerHints = new HashMap<String, String>();

		indexConfig = new IndexConfig();
		facetSearchConfig.setIndexConfig(indexConfig);

		when(indexerContext.getIndex()).thenReturn(index);
		when(indexerContext.getIndexOperation()).thenReturn(indexOperation);
		when(indexerContext.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		when(indexerContext.getIndexedType()).thenReturn(indexedType);
		when(indexerContext.getIndexedProperties()).thenReturn(indexedProperties);
		when(indexerContext.getPks()).thenReturn(pks);
		when(indexerContext.getIndexerHints()).thenReturn(indexerHints);

		optimizeModeListener = new OptimizeModeListener();
		optimizeModeListener.setIndexer(indexer);
	}

	private void runListeners() throws Exception
	{
		optimizeModeListener.beforeIndex(indexerContext);
		optimizeModeListener.afterIndex(indexerContext);
	}

	@Test
	public void shouldNeverOptimize() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(OptimizeMode.NEVER);

		// when
		runListeners();

		// then
		verify(indexer, never()).optimize(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class));
	}

	@Test
	public void shouldOptimizeAfterIndex() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(OptimizeMode.AFTER_INDEX);

		// when
		runListeners();

		// then
		verify(indexer, times(1)).optimize(indexerContext.getFacetSearchConfig(), indexerContext.getIndexedType(),
				indexerContext.getIndex());
	}

	@Test
	public void shouldOptimizeAfterFullIndex() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(OptimizeMode.AFTER_FULL_INDEX);
		when(indexerContext.getIndexOperation()).thenReturn(IndexOperation.FULL);

		// when
		runListeners();

		// then
		verify(indexer, times(1)).optimize(indexerContext.getFacetSearchConfig(), indexerContext.getIndexedType(),
				indexerContext.getIndex());
	}

	@Test
	public void shouldNotOptimizeAfterIndexUpdate() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(OptimizeMode.AFTER_FULL_INDEX);
		when(indexerContext.getIndexOperation()).thenReturn(IndexOperation.UPDATE);

		// when
		runListeners();

		// then
		verify(indexer, never()).optimize(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class));
	}

	@Test
	public void defaultModeShouldBeNeverOptimize() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(null);

		// when
		runListeners();

		// then
		verify(indexer, never()).optimize(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class));
	}

	@Test
	public void shouldNeverOptimizeOnBeforeAndErrorListeners() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(OptimizeMode.AFTER_INDEX);

		// when
		optimizeModeListener.beforeIndex(indexerContext);
		optimizeModeListener.afterIndexError(indexerContext);

		// then
		verify(indexer, never()).optimize(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class));
	}

	@Test
	public void indexerHintShouldBeOptimizedAfterIndex() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(OptimizeMode.NEVER);
		indexerContext.getIndexerHints().put(OptimizeModeListener.OPTIMIZE_MODE_HINT, "AFTER_INDEX");

		// when
		runListeners();

		// then
		verify(indexer, times(1)).optimize(indexerContext.getFacetSearchConfig(), indexerContext.getIndexedType(),
				indexerContext.getIndex());
	}

	@Test
	public void indexerHintIsNotValid() throws Exception
	{
		// given
		indexConfig.setOptimizeMode(OptimizeMode.NEVER);
		indexerContext.getIndexerHints().put(OptimizeModeListener.OPTIMIZE_MODE_HINT, "after_index");

		// when
		runListeners();

		// then
		verify(indexer, never()).optimize(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class));
	}
}

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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.CommitMode;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.Indexer;
import de.hybris.platform.solrfacetsearch.indexer.spi.Indexer.CommitType;
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
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CommitModeListenerTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private Index index;

	@Mock
	private IndexerContext indexerContext;

	@Mock
	private IndexerBatchContext indexerBatchContext;

	@Mock
	private Indexer indexer;

	private IndexConfig indexConfig;

	private CommitModeListener commitModeListener;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final IndexOperation indexOperation = IndexOperation.FULL;
		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		final IndexedType indexedType = new IndexedType();
		final Collection<IndexedProperty> indexedProperties = Collections.emptyList();
		final List<PK> pks = Collections.emptyList();
		final List<ItemModel> items = Collections.emptyList();
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

		when(indexerBatchContext.getIndex()).thenReturn(index);
		when(indexerBatchContext.getIndexOperation()).thenReturn(indexOperation);
		when(indexerBatchContext.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		when(indexerBatchContext.getIndexedType()).thenReturn(indexedType);
		when(indexerBatchContext.getIndexedProperties()).thenReturn(indexedProperties);
		when(indexerBatchContext.getItems()).thenReturn(items);
		when(indexerBatchContext.getIndexerHints()).thenReturn(indexerHints);

		commitModeListener = new CommitModeListener();
		commitModeListener.setIndexer(indexer);
	}

	private void runListeners() throws Exception
	{
		commitModeListener.beforeIndex(indexerContext);
		commitModeListener.beforeBatch(indexerBatchContext);
		commitModeListener.afterBatch(indexerBatchContext);
		commitModeListener.beforeBatch(indexerBatchContext);
		commitModeListener.afterBatch(indexerBatchContext);
		commitModeListener.afterIndex(indexerContext);
	}

	@Test
	public void shouldNeverCommit() throws Exception
	{
		// given
		indexConfig.setCommitMode(CommitMode.NEVER);

		// when
		runListeners();

		// then
		verify(indexer, never()).commit(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class),
				any(CommitType.class));
	}

	@Test
	public void shouldCommitAfterIndex() throws Exception
	{
		// given
		indexConfig.setCommitMode(CommitMode.AFTER_INDEX);

		// when
		runListeners();

		// then
		verify(indexer, times(1)).commit(indexerContext.getFacetSearchConfig(), indexerContext.getIndexedType(),
				indexerContext.getIndex(), CommitType.HARD);
	}

	@Test
	public void shouldCommitAfterBatch() throws Exception
	{
		// given
		indexConfig.setCommitMode(CommitMode.AFTER_BATCH);

		// when
		runListeners();

		// then
		verify(indexer, times(2)).commit(indexerBatchContext.getFacetSearchConfig(), indexerBatchContext.getIndexedType(),
				indexerBatchContext.getIndex(), CommitType.HARD);
	}

	@Test
	public void shouldCommitMixed() throws Exception
	{
		// given
		indexConfig.setCommitMode(CommitMode.MIXED);

		// when
		runListeners();

		// then
		final InOrder inOrder = inOrder(indexer);
		inOrder.verify(indexer, times(2)).commit(indexerBatchContext.getFacetSearchConfig(), indexerBatchContext.getIndexedType(),
				indexerBatchContext.getIndex(), CommitType.SOFT);
		inOrder.verify(indexer, times(1)).commit(indexerContext.getFacetSearchConfig(), indexerContext.getIndexedType(),
				indexerContext.getIndex(), CommitType.HARD);
	}

	@Test
	public void defaultModeShouldBeCommitAfterIndex() throws Exception
	{
		// given
		indexConfig.setCommitMode(null);

		// when
		runListeners();

		// then
		verify(indexer, times(1)).commit(indexerContext.getFacetSearchConfig(), indexerContext.getIndexedType(),
				indexerContext.getIndex(), CommitType.HARD);
	}

	@Test
	public void shouldNeverCommitOnBeforeAndErrorListeners() throws Exception
	{
		// given
		indexConfig.setCommitMode(CommitMode.MIXED);

		// when
		commitModeListener.beforeIndex(indexerContext);
		commitModeListener.beforeBatch(indexerBatchContext);
		commitModeListener.afterBatchError(indexerBatchContext);
		commitModeListener.afterIndexError(indexerContext);

		// then
		verify(indexer, never()).commit(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class),
				any(CommitType.class));
	}

	@Test
	public void indexerHintShouldBeCommitModeMixed() throws Exception
	{
		// given
		indexConfig.setCommitMode(CommitMode.NEVER);
		indexerContext.getIndexerHints().put(CommitModeListener.COMMIT_MODE_HINT, "MIXED");
		indexerBatchContext.getIndexerHints().put(CommitModeListener.COMMIT_MODE_HINT, "MIXED");

		// when
		runListeners();

		// then
		final InOrder inOrder = inOrder(indexer);
		inOrder.verify(indexer, times(2)).commit(indexerBatchContext.getFacetSearchConfig(), indexerBatchContext.getIndexedType(),
				indexerBatchContext.getIndex(), CommitType.SOFT);
		inOrder.verify(indexer, times(1)).commit(indexerContext.getFacetSearchConfig(), indexerContext.getIndexedType(),
				indexerContext.getIndex(), CommitType.HARD);
	}

	@Test
	public void indexerHintIsInvalid() throws Exception
	{
		// given
		indexConfig.setCommitMode(CommitMode.NEVER);
		indexerContext.getIndexerHints().put(CommitModeListener.COMMIT_MODE_HINT, "mixed");
		indexerBatchContext.getIndexerHints().put(CommitModeListener.COMMIT_MODE_HINT, "mixed");

		// when
		runListeners();

		// then
		verify(indexer, never()).commit(any(FacetSearchConfig.class), any(IndexedType.class), any(Index.class),
				any(CommitType.class));
	}
}

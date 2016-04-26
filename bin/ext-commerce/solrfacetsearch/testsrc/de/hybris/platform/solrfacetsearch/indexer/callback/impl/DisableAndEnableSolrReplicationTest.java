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
package de.hybris.platform.solrfacetsearch.indexer.callback.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import de.hybris.platform.solrfacetsearch.indexer.callback.replication.IndexOperationCallbackParams;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.solr.client.solrj.SolrClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test Full Index Operation Callbacks: {@link EnableReplicationForFullIndex} and {@link DisableReplicationForFullIndex}
 */
public class DisableAndEnableSolrReplicationTest
{
	/**
	 * counterpart of replication flag on Solr Server
	 */
	private final AtomicBoolean isEnabledReplication = new AtomicBoolean(false);
	private EnableReplicationForFullIndex enableReplicationForFullIndex;
	private DisableReplicationForFullIndex disableReplicationForFullIndex;

	@Before
	public void before()
	{
		enableReplicationForFullIndex = spy(new MockedEnableReplicationForFullIndex(isEnabledReplication));
		disableReplicationForFullIndex = spy(new MockedDisableReplicationForFullIndex(isEnabledReplication));
		final Map<IndexOperationCallbackParams, Integer> disabledReplicationConfigMap = new HashMap<IndexOperationCallbackParams, Integer>();
		enableReplicationForFullIndex.setDisabledReplicationConfigMap(disabledReplicationConfigMap);
		disableReplicationForFullIndex.setDisabledReplicationConfigMap(disabledReplicationConfigMap);
	}

	@Test
	public void testThatDisablingAndEnablingReplicationShouldPertainOnlyToStandaloneSolr()
	{
		isEnabledReplication.set(false);
		//given
		final SolrFacetSearchConfigModel facetSearchConfig = createSolrConfigModel(SolrServerModes.EMBEDDED);
		//when
		disableReplicationForFullIndex.invoke(facetSearchConfig, null);
		enableReplicationForFullIndex.invoke(facetSearchConfig, null);
		//then
		Assert.assertFalse(isEnabledReplication.get());
		verify(enableReplicationForFullIndex, never()).enableReplicationOnMaster(any(SolrClient.class));
		verify(disableReplicationForFullIndex, never()).disableReplicationOnMaster(any(SolrClient.class));
	}

	@Test
	public void testThatStandaloneServerWithDisabledReplicationDoesNotEnableReplication()
	{
		//given
		isEnabledReplication.set(false);
		final SolrFacetSearchConfigModel facetSearchConfig = createSolrConfigModel(SolrServerModes.STANDALONE);
		//when-then

		// disable - enable
		disableReplicationForFullIndex.invoke(facetSearchConfig, null);
		enableReplicationForFullIndex.invoke(facetSearchConfig, null);
		Assert.assertFalse(isEnabledReplication.get());

		// disable disable enable enable
		disableReplicationForFullIndex.invoke(facetSearchConfig, null);
		disableReplicationForFullIndex.invoke(facetSearchConfig, null);
		Assert.assertFalse(isEnabledReplication.get());

		enableReplicationForFullIndex.invoke(facetSearchConfig, null);
		enableReplicationForFullIndex.invoke(facetSearchConfig, null);
		Assert.assertFalse(isEnabledReplication.get());

		verify(enableReplicationForFullIndex, never()).enableReplicationOnMaster(any(SolrClient.class));
		verify(disableReplicationForFullIndex, never()).disableReplicationOnMaster(any(SolrClient.class));
	}

	@Test
	public void testDisableReplicationForStandaloneServer()
	{
		//given
		isEnabledReplication.set(true);
		final SolrFacetSearchConfigModel facetSearchConfig = createSolrConfigModel(SolrServerModes.STANDALONE);
		//when-then
		disableReplicationForFullIndex.invoke(facetSearchConfig, null);
		Assert.assertFalse(isEnabledReplication.get());
		enableReplicationForFullIndex.invoke(facetSearchConfig, null);
		Assert.assertTrue(isEnabledReplication.get());
		verify(disableReplicationForFullIndex, times(1)).disableReplicationOnMaster(any(SolrClient.class));
		verify(enableReplicationForFullIndex, times(1)).enableReplicationOnMaster(any(SolrClient.class));

		disableReplicationForFullIndex.invoke(facetSearchConfig, null);
		disableReplicationForFullIndex.invoke(facetSearchConfig, null);
		Assert.assertFalse(isEnabledReplication.get());
		enableReplicationForFullIndex.invoke(facetSearchConfig, null);
		enableReplicationForFullIndex.invoke(facetSearchConfig, null);
		Assert.assertTrue(isEnabledReplication.get());

		verify(disableReplicationForFullIndex, times(2)).disableReplicationOnMaster(any(SolrClient.class));
		verify(enableReplicationForFullIndex, times(2)).enableReplicationOnMaster(any(SolrClient.class));
	}

	protected SolrFacetSearchConfigModel createSolrConfigModel(final SolrServerModes mode)
	{
		final SolrFacetSearchConfigModel facetSearchConfig = new SolrFacetSearchConfigModel();
		final SolrServerConfigModel solrServerConfigModel = new SolrServerConfigModel();
		solrServerConfigModel.setMode(mode);
		facetSearchConfig.setSolrServerConfig(solrServerConfigModel);
		return facetSearchConfig;
	}

	/**
	 * "Mocked" version of class DisableReplicationForFullIndex, disabling/enabling/reading replication flag work on
	 * {@link DisableAndEnableSolrReplicationTest#isEnabledReplication}
	 */
	protected static class MockedDisableReplicationForFullIndex extends DisableReplicationForFullIndex
	{
		private final AtomicBoolean isEnabledReplication;

		public MockedDisableReplicationForFullIndex(final AtomicBoolean isEnabledReplication)
		{
			this.isEnabledReplication = isEnabledReplication;
		}

		@Override
		protected SolrClient getSolrClient(final SolrFacetSearchConfigModel facetSearchConfig,
				final SolrIndexedTypeModel indexedType) throws SolrServiceException
		{
			return null;
		}

		@Override
		protected boolean isReplicationEnabledOnMaster(final SolrClient solrClient)
		{
			return isEnabledReplication.get();
		}

		@Override
		protected boolean disableReplicationOnMaster(final SolrClient solrClient)
		{
			isEnabledReplication.set(false);
			return true;
		}

		@Override
		protected boolean enableReplicationOnMaster(final SolrClient solrClient)
		{
			isEnabledReplication.set(true);
			return true;
		}
	}

	/**
	 * "Mocked" version of class EnableReplicationForFullIndex, disabling/enabling/reading replication flag work on
	 * {@link DisableAndEnableSolrReplicationTest#isEnabledReplication}
	 */
	protected static class MockedEnableReplicationForFullIndex extends EnableReplicationForFullIndex
	{
		private final AtomicBoolean isEnabledReplication;

		public MockedEnableReplicationForFullIndex(final AtomicBoolean isEnabledReplication)
		{
			this.isEnabledReplication = isEnabledReplication;
		}

		@Override
		protected SolrClient getSolrClient(final SolrFacetSearchConfigModel facetSearchConfig,
				final SolrIndexedTypeModel indexedType) throws SolrServiceException
		{
			return null;
		}

		@Override
		protected boolean isReplicationEnabledOnMaster(final SolrClient masterClient)
		{
			return isEnabledReplication.get();
		}

		@Override
		protected boolean disableReplicationOnMaster(final SolrClient masterClient)
		{
			isEnabledReplication.set(false);
			return true;
		}

		@Override
		protected boolean enableReplicationOnMaster(final SolrClient masterClient)
		{
			isEnabledReplication.set(true);
			return true;
		}
	}
}

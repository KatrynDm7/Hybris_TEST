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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.test.RunnerCreator;
import de.hybris.platform.test.TestThreadsHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * This Test checks the scenario of multi-threaded indexation.
 */
@IntegrationTest
public class MultithreadedIndexerServiceAccessTest extends AbstractIntegrationTest
{
	private static final int TIMEOUT = 3600;
	private static final int NO_OF_THREADS = 10;

	@Resource
	private SessionService sessionService;

	@Resource
	private TenantService tenantService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private FacetSearchConfigService facetSearchConfigService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws Exception
	{
		for (int i = 0; i < NO_OF_THREADS; i++)
		{
			importConfig("/test/integration/MultithreadedIndexerServiceAccessTest.csv",
					Collections.singletonMap("threadId", String.valueOf(i)));
		}
	}

	protected FacetSearchConfig getFacetSearchConfig(final int threadNumber) throws FacetConfigServiceException
	{
		final String name = FACET_SEARCH_CONFIG_NAME + getTestId() + "_" + threadNumber;
		return facetSearchConfigService.getConfiguration(name);
	}

	@Test
	public void testMultithreadedAccessNoErrors() throws Exception
	{
		// given
		final RunnerCreator<IndexerServiceRunner> runnerCreator = new IndexerServiceRunnerCreator();
		final TestThreadsHolder<IndexerServiceRunner> threadsHolder = new TestThreadsHolder<MultithreadedIndexerServiceAccessTest.IndexerServiceRunner>(
				NO_OF_THREADS, runnerCreator);

		// when
		threadsHolder.startAll();
		final boolean allFinished = threadsHolder.waitForAll(TIMEOUT, TimeUnit.SECONDS);

		// then
		assertTrue("Some threads didn't finish execution", allFinished);
		assertFalse("Some threads finished execution with errors", threadsHolder.hasErrors());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testMultithreadedAccess() throws Exception
	{
		// given
		final RunnerCreator<IndexerServiceRunner> runnerCreator = new IndexerServiceRunnerCreator();
		final TestThreadsHolder<IndexerServiceRunner> threadsHolder = new TestThreadsHolder<MultithreadedIndexerServiceAccessTest.IndexerServiceRunner>(
				NO_OF_THREADS, runnerCreator);

		// when
		threadsHolder.startAll();
		final boolean allFinished = threadsHolder.waitForAll(TIMEOUT, TimeUnit.SECONDS);

		// then
		assertTrue("Some threads didn't finish execution", allFinished);
		assertFalse("Some threads finished execution with errors", threadsHolder.hasErrors());

		// each thread is indexing products from different catalog version
		for (int i = 0; i < NO_OF_THREADS; i++)
		{
			final FacetSearchConfig facetSearchConfig = getFacetSearchConfig(i);
			final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
			final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);

			final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
					ONLINE_CATALOG_VERSION + getTestId() + "_" + i);
			searchQuery.setCatalogVersions(Arrays.asList(catalogVersion));

			final SearchResult searchResult = facetSearchService.search(searchQuery);
			final List<ProductModel> products = (List<ProductModel>) searchResult.getResults();

			Assert.assertEquals(3, products.size());
			for (final ProductModel product : products)
			{
				Assert.assertEquals(catalogVersion, product.getCatalogVersion());
			}
		}
	}

	private class IndexerServiceRunner implements Runnable
	{
		private final String tenantId;
		private final int threadNumber;

		public IndexerServiceRunner(final String tenantId, final int threadNumber)
		{
			super();
			this.tenantId = tenantId;
			this.threadNumber = threadNumber;
		}

		@Override
		public void run()
		{
			try
			{
				final Tenant tenant = Registry.getTenantByID(tenantId);
				Registry.setCurrentTenant(tenant);

				sessionService.createNewSession();

				final FacetSearchConfig facetSearchConfig = getFacetSearchConfig(threadNumber);
				indexerService.performFullIndex(facetSearchConfig);
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e); // NOPMD
			}
			finally
			{
				sessionService.closeCurrentSession();
				Registry.unsetCurrentTenant();
			}
		}
	}

	private class IndexerServiceRunnerCreator implements RunnerCreator<IndexerServiceRunner>
	{
		@Override
		public IndexerServiceRunner newRunner(final int threadNumber)
		{
			return new IndexerServiceRunner(tenantService.getCurrentTenantId(), threadNumber);
		}
	}
}

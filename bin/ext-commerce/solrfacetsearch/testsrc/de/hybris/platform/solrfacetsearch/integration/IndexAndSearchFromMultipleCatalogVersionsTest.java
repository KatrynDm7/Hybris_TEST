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
package de.hybris.platform.solrfacetsearch.integration;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;


@IntegrationTest
public class IndexAndSearchFromMultipleCatalogVersionsTest extends AbstractIntegrationTest
{
	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	private static final String PRODUCT_1 = "product1";
	private static final String PRODUCT_2 = "product2";

	private static final String CATALOG_A_ID = "testCatalogA";
	private static final String CATALOG_B_ID = "testCatalogB";
	private static final String CATALOG_VERSION_ONLINE = "Online";
	private static final String CATALOG_VERSION_STAGED = "Staged";

	@Override
	protected void loadInitialData() throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException,
			SolrServerException
	{
		importConfig("/test/integration/IndexAndSearchFromMultipleCatalogVersionsTest.csv");
	}

	@Test
	public void testIndexTwoCatalogVersionsSearchInTwoCatalogVersions() throws IndexerException, FacetConfigServiceException,
			FacetSearchException
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionAOnline = catalogVersionService.getCatalogVersion(CATALOG_A_ID,
				CATALOG_VERSION_ONLINE);
		final CatalogVersionModel catalogVersionBStaged = catalogVersionService.getCatalogVersion(CATALOG_B_ID,
				CATALOG_VERSION_STAGED);

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(catalogVersionAOnline, catalogVersionBStaged));
		query.setLanguage("en");
		query.searchInField("code", PRODUCT_1);

		final SearchResult searchResult = facetSearchService.search(query);

		// then
		assertEquals(2, searchResult.getTotalNumberOfResults());
	}

	@Test
	public void testIndexTwoCatalogVersionsSearchInOneCatalogVersion() throws FacetConfigServiceException, IndexerException,
			FacetSearchException
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionAOnline = catalogVersionService.getCatalogVersion(CATALOG_A_ID,
				CATALOG_VERSION_ONLINE);

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(catalogVersionAOnline));
		query.setLanguage("en");
		query.searchInField("code", PRODUCT_2);

		final SearchResult searchResult = facetSearchService.search(query);

		// then
		assertEquals(1, searchResult.getTotalNumberOfResults());
	}

	@Test
	public void testIndexTwoCatalogVersionsSearchInTwoSessionCatalogVersions() throws FacetSearchException,
			FacetConfigServiceException, IndexerException
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionAOnline = catalogVersionService.getCatalogVersion(CATALOG_A_ID,
				CATALOG_VERSION_ONLINE);
		final CatalogVersionModel catalogVersionBStaged = catalogVersionService.getCatalogVersion(CATALOG_B_ID,
				CATALOG_VERSION_STAGED);

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(null);
		query.setLanguage("en");
		query.searchInField("code", PRODUCT_1);

		catalogVersionService.setSessionCatalogVersions(Arrays.asList(catalogVersionAOnline, catalogVersionBStaged));

		final SearchResult searchResult = facetSearchService.search(query);

		// then
		assertEquals(2, searchResult.getTotalNumberOfResults());
	}

	@Test
	public void testIndexTwoCatalogVersionsSearchInOneSessionCatalogVersion() throws FacetSearchException,
			FacetConfigServiceException, IndexerException
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionBStaged = catalogVersionService.getCatalogVersion(CATALOG_B_ID,
				CATALOG_VERSION_STAGED);

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(null);
		query.setLanguage("en");
		query.searchInField("code", PRODUCT_2);

		catalogVersionService.setSessionCatalogVersions(Arrays.asList(catalogVersionBStaged));

		final SearchResult searchResult = facetSearchService.search(query);

		// then
		assertEquals(1, searchResult.getTotalNumberOfResults());
	}

	@Test
	public void testIndexTwoCatalogVersionsSearchInThird() throws FacetSearchException, FacetConfigServiceException,
			IndexerException
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionAOnline = catalogVersionService.getCatalogVersion(CATALOG_A_ID,
				CATALOG_VERSION_STAGED);

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(catalogVersionAOnline));
		query.setLanguage("en");
		query.searchInField("code", PRODUCT_1);

		final SearchResult searchResult = facetSearchService.search(query);

		// then
		assertEquals(0, searchResult.getTotalNumberOfResults());
	}

	@Test
	public void testIndexTwoCatalogVersionsSearchInTwoOthers() throws FacetSearchException, FacetConfigServiceException,
			IndexerException
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionAOnline = catalogVersionService.getCatalogVersion(CATALOG_A_ID,
				CATALOG_VERSION_STAGED);
		final CatalogVersionModel catalogVersionBOnline = catalogVersionService.getCatalogVersion(CATALOG_B_ID,
				CATALOG_VERSION_ONLINE);

		indexerService.performFullIndex(getFacetSearchConfig());

		// when
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(catalogVersionAOnline, catalogVersionBOnline));
		query.setLanguage("en");
		query.searchInField("code", PRODUCT_1);

		final SearchResult searchResult = facetSearchService.search(query);

		// then
		assertEquals(0, searchResult.getTotalNumberOfResults());
	}
}

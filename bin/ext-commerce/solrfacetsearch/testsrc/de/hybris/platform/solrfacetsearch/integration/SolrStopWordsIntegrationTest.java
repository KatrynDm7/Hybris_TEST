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
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;


/**
 * Test for update StopWords feature
 */
@IntegrationTest
public class SolrStopWordsIntegrationTest extends AbstractIntegrationTest
{
	private static final String STOP_WORD = "abcdef";

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException,
			SolrServerException
	{
		importConfig("/test/integration/SolrStopWordsIntegrationTest.csv");
	}

	@Test
	public void testSearchForStopWordBeforeUpdate() throws IndexerException, FacetConfigServiceException, FacetSearchException
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		// when
		indexerService.performFullIndex(getFacetSearchConfig());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersionOnline));
		query.setLanguage("en");
		query.searchInField("description", STOP_WORD);

		final SearchResult searchResult = facetSearchService.search(query);

		// then
		assertEquals(2, searchResult.getTotalNumberOfResults());
	}

	@Test
	public void testSearchForStopWordAfterUpdate() throws FacetConfigServiceException, ImpExException, IOException,
			FacetSearchException, IndexerException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		//given
		importConfig("/test/integration/SolrStopWordsIntegrationTest_addStopWord.csv");

		//when
		indexerService.performFullIndex(getFacetSearchConfig());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersionOnline));
		query.setLanguage("en");
		query.searchInField("description", STOP_WORD);

		final SearchResult searchResult = facetSearchService.search(query);

		//then
		assertEquals(0, searchResult.getTotalNumberOfResults());
	}

	@Test
	public void testSearchForStopWordInOtherLanguage() throws FacetConfigServiceException, ImpExException, IOException,
			FacetSearchException, IndexerException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		//given
		importConfig("/test/integration/SolrStopWordsIntegrationTest_addStopWord.csv");

		//when
		indexerService.performFullIndex(getFacetSearchConfig());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersionOnline));
		query.setLanguage("de");
		query.searchInField("description", STOP_WORD);

		final SearchResult searchResult = facetSearchService.search(query);

		//then
		assertEquals(1, searchResult.getTotalNumberOfResults());
	}
}

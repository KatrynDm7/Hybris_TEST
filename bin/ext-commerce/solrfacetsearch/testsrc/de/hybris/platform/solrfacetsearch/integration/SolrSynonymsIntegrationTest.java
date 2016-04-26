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


public class SolrSynonymsIntegrationTest extends AbstractIntegrationTest
{
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
		importConfig("/test/integration/DefaultSolrSynonymServiceIntegrationTest.csv");
	}

	@Test
	public void testSearchForSynonymBeforeUpdate() throws IndexerException, FacetConfigServiceException, FacetSearchException
	{
		indexerService.performFullIndex(getFacetSearchConfig());

		//given
		final SearchQuery queryForBank = getQueryForBank();
		final SearchQuery queryForFolks = getQueryForFolks();
		final SearchQuery queryForBasic = getQueryForBasic();

		//when
		final SearchResult searchResultBank = facetSearchService.search(queryForBank);
		final SearchResult searchResultFolks = facetSearchService.search(queryForFolks);
		final SearchResult searchResultBasic = facetSearchService.search(queryForBasic);

		//then
		assertEquals(1, searchResultBank.getTotalNumberOfResults());
		assertEquals(1, searchResultFolks.getTotalNumberOfResults());
		assertEquals(0, searchResultBasic.getTotalNumberOfResults());
	}

	@Test
	public void testAddingSynonyms() throws FacetConfigServiceException, FacetSearchException, ImpExException, IOException,
			IndexerException
	{
		//given
		importConfig("/test/integration/DefaultSolrSynonymServiceIntegrationTest_addSynonyms.csv");
		indexerService.performFullIndex(getFacetSearchConfig());

		//when
		final SearchQuery queryForBank = getQueryForBank();
		final SearchQuery queryForFolks = getQueryForFolks();
		final SearchQuery queryForBasic = getQueryForBasic();

		final SearchResult searchResultBank = facetSearchService.search(queryForBank);
		final SearchResult searchResultFolks = facetSearchService.search(queryForFolks);
		final SearchResult searchResultBasic = facetSearchService.search(queryForBasic);

		//then
		assertEquals(2, searchResultBank.getTotalNumberOfResults());
		assertEquals(1, searchResultFolks.getTotalNumberOfResults());
		assertEquals(1, searchResultBasic.getTotalNumberOfResults());
	}

	@Test
	public void testUpdateSynonyms() throws FacetConfigServiceException, FacetSearchException, ImpExException, IOException,
			IndexerException
	{
		//given
		importConfig("/test/integration/DefaultSolrSynonymServiceIntegrationTest_updateSynonyms.csv");
		indexerService.performFullIndex(getFacetSearchConfig());

		//when
		final SearchQuery queryForBank = getQueryForBank();
		final SearchQuery queryForFolks = getQueryForFolks();
		final SearchQuery queryForBasic = getQueryForBasic();

		final SearchResult searchResultBank = facetSearchService.search(queryForBank);
		final SearchResult searchResultFolks = facetSearchService.search(queryForFolks);
		final SearchResult searchResultBasic = facetSearchService.search(queryForBasic);

		//then
		assertEquals(1, searchResultBank.getTotalNumberOfResults());
		assertEquals(3, searchResultFolks.getTotalNumberOfResults());
		assertEquals(1, searchResultBasic.getTotalNumberOfResults());
	}

	@Test
	public void testRemoveSynonyms() throws FacetConfigServiceException, FacetSearchException, ImpExException, IOException,
			IndexerException
	{
		//given
		importConfig("/test/integration/DefaultSolrSynonymServiceIntegrationTest_deleteSynonyms.csv");
		indexerService.performFullIndex(getFacetSearchConfig());

		//when
		final SearchQuery queryForBank = getQueryForBank();
		final SearchQuery queryForFolks = getQueryForFolks();
		final SearchQuery queryForBasic = getQueryForBasic();

		final SearchResult searchResultBank = facetSearchService.search(queryForBank);
		final SearchResult searchResultFolks = facetSearchService.search(queryForFolks);
		final SearchResult searchResultBasic = facetSearchService.search(queryForBasic);

		//then
		assertEquals(1, searchResultBank.getTotalNumberOfResults());
		assertEquals(0, searchResultFolks.getTotalNumberOfResults());
		assertEquals(0, searchResultBasic.getTotalNumberOfResults());
	}

	private SearchQuery getQueryForBank() throws FacetConfigServiceException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersionOnline));
		query.setLanguage("de");
		query.searchInField("description", "bank");

		return query;
	}

	private SearchQuery getQueryForFolks() throws FacetConfigServiceException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersionOnline));
		query.setLanguage("en");
		query.searchInField("description", "folks");

		return query;
	}

	private SearchQuery getQueryForBasic() throws FacetConfigServiceException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel catalogVersionOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Collections.singletonList(catalogVersionOnline));
		query.setLanguage("en");
		query.searchInField("description", "basic");

		return query;
	}
}

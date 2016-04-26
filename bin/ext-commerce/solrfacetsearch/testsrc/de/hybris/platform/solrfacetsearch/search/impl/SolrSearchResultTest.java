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
package de.hybris.platform.solrfacetsearch.search.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.enums.ConverterType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.SolrResultPostProcessor;
import de.hybris.platform.solrfacetsearch.search.product.SolrProductData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * @author wojciech.gruszczyk
 */
public class SolrSearchResultTest extends AbstractIntegrationTest
{
	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Resource
	private SearchResultConverters searchResultConverters;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/SolrSearchResultTest.csv");
	}

	@Test
	public void testGetResultData() throws FacetSearchException, FacetConfigServiceException, IndexerException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION);

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);
		searchQuery.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion));

		final SearchResult searchResult = facetSearchService.search(searchQuery);
		final List<SolrProductData> resultData = searchResult.<SolrProductData> getResultData(ConverterType.DEFAULT);

		assertFalse("Empty set not expected", resultData.isEmpty());

		final List<Long> resPk = new ArrayList<Long>();
		for (final SolrProductData data : resultData)
		{
			assertNotNull("Code required", data.getCode());
			assertNotNull("PK required", data.getPk());
			assertFalse("All PK's should be unique in the search", resPk.contains(data.getPk()));
			resPk.add(data.getPk());
			assertFalse(data.getCategories().isEmpty());
			//not Indexed
			assertNull(data.getCatalog());
		}
	}

	@Test
	public void testGetResultDataWithPostProcessors() throws FacetSearchException, IndexerException, FacetConfigServiceException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		indexerService.performFullIndex(facetSearchConfig);

		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION);
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion));


		final DefaultFacetSearchService dfss = (DefaultFacetSearchService) facetSearchService;

		final LegacyFacetSearchStrategy facetSearchStrategy = (LegacyFacetSearchStrategy) dfss
				.getFacetSearchStrategy(facetSearchConfig, indexedType);
		final List<SolrResultPostProcessor> initialPostProcessors = facetSearchStrategy.getResultPostProcessors();
		try
		{
			final SolrResultPostProcessor processor1 = new SolrResultPostProcessor()
			{
				@Override
				public SearchResult process(final SearchResult searchResult)
				{
					//sample post processor which returns empty result data list
					final SolrSearchResult input = (SolrSearchResult) searchResult;

					final SolrSearchResult solrSearchResult = new SolrSearchResult();
					solrSearchResult.setSearchQuery(input.getSearchQuery());
					solrSearchResult.setQueryResponse(input.getQueryResponse());
					solrSearchResult.setNumberOfResults(input.getNumberOfResults());
					solrSearchResult.setFacetsMap(input.getFacetsMap());
					solrSearchResult.setBreadcrumbs(input.getBreadcrumbs());
					solrSearchResult.setDocuments(Collections.emptyList());
					solrSearchResult.setSolrDocuments(Collections.emptyList());
					if (searchResultConverters != null)
					{
						solrSearchResult.setConvertersMapping(searchResultConverters.getConverterMapping(indexedType.getCode()));
					}

					return solrSearchResult;
				}
			};

			SearchResult res = dfss.search(query);
			List<SolrProductData> resultData = res.<SolrProductData> getResultData(ConverterType.DEFAULT);
			assertFalse("Empty set not expected", resultData.isEmpty());

			facetSearchStrategy.setResultPostProcessors(Collections.singletonList(processor1));
			res = dfss.search(query);
			resultData = res.<SolrProductData> getResultData(ConverterType.DEFAULT);
			assertTrue("Empty set expected - forced by post processor", resultData.isEmpty());

		}
		finally
		{
			facetSearchStrategy.setResultPostProcessors(initialPostProcessors);
		}
	}
}

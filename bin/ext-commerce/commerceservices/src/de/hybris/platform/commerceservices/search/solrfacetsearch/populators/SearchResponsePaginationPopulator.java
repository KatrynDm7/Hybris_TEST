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
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.SearchResult;


/**
 */
public class SearchResponsePaginationPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, IndexedTypeSort, SearchResult>, SearchPageData<ITEM>>
{
	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, IndexedTypeSort, SearchResult> source,
			final SearchPageData<ITEM> target)
	{
		target.setPagination(buildPaginationData(source, source.getSearchResult()));
	}

	protected PaginationData buildPaginationData(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, IndexedTypeSort, SearchResult> source,
			final SearchResult solrSearchResult)
	{
		final PaginationData paginationData = createPaginationData();

		if (solrSearchResult != null)
		{
			paginationData.setTotalNumberOfResults(solrSearchResult.getTotalNumberOfResults());
			paginationData.setPageSize(solrSearchResult.getPageSize());
			paginationData.setCurrentPage(solrSearchResult.getOffset());
			paginationData.setNumberOfPages((int) solrSearchResult.getNumberOfPages());
		}

		// Set the current sort if there is one
		final IndexedTypeSort currentSort = source.getRequest().getCurrentSort();
		if (currentSort != null)
		{
			paginationData.setSort(currentSort.getCode());
		}

		return paginationData;
	}

	protected PaginationData createPaginationData()
	{
		return new PaginationData();
	}
}

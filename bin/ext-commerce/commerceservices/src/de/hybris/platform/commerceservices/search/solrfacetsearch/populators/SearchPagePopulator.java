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

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

/**
 */
public class SearchPagePopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> implements Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	// Spring config
	private int maximumPageSize = 100;

	protected int getMaximumPageSize()
	{
		return maximumPageSize;
	}

	// Optional
	public void setMaximumPageSize(final int maximumPageSize)
	{
		this.maximumPageSize = maximumPageSize;
	}

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source, final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		if (target.getPageableData() != null)
		{
			// Set the page size
			final int pageSize = Math.min(getMaximumPageSize(), target.getPageableData().getPageSize());
			if (pageSize > 0)
			{
				target.getSearchQuery().setPageSize(pageSize);
			}

			final int currentPage = target.getPageableData().getCurrentPage();
			if (currentPage >= 0)
			{
				target.getSearchQuery().setOffset(currentPage);
			}
		}
		else
		{
			// We want the first page of results
			target.getSearchQuery().setOffset(0);
		}
	}
}

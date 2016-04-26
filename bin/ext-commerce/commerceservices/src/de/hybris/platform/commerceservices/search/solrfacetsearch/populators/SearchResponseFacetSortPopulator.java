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

import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 */
public class SearchResponseFacetSortPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult>, FacetSearchPageData<SolrSearchQueryData, ITEM>>
{

	protected static class FacetDataComparator extends AbstractComparator<FacetData<?>>
	{
		protected static final FacetDataComparator INSTANCE = new FacetDataComparator();

		@Override
		protected int compareInstances(final FacetData<?> facet1, final FacetData<?> facet2)
		{
			// Compare based on priority - higher priority comes first (descending so reverse facets)
			int result = compareValues(facet2.getPriority(), facet1.getPriority());
			if (EQUAL == result)
			{
				// If priority is equal then compare display names (ascending)
				result = facet1.getName().compareToIgnoreCase(facet2.getName());
				if (EQUAL == result)
				{
					// Same priority, same display name, try the code
					result = facet1.getCode().compareTo(facet2.getCode());
				}
			}
			return result;
		}
	}


	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult> source,
			final FacetSearchPageData<SolrSearchQueryData, ITEM> target)
	{
		final List<FacetData<SolrSearchQueryData>> facets = target.getFacets();
		if (CollectionUtils.isNotEmpty(facets))
		{
			Collections.sort(facets, FacetDataComparator.INSTANCE);
		}
	}

}

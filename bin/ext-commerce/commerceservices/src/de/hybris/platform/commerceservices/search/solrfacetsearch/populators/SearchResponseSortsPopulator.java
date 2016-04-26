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


import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class SearchResponseSortsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_RESULT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, IndexedTypeSort, SEARCH_RESULT_TYPE>, SearchPageData<ITEM>>
{
	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, IndexedTypeSort, SEARCH_RESULT_TYPE> source,
			final SearchPageData<ITEM> target)
	{
		target.setSorts(buildSorts(source));
	}

	protected List<SortData> buildSorts(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, IndexedTypeSort, SEARCH_RESULT_TYPE> source)
	{
		final List<SortData> result = new ArrayList<SortData>();

		final IndexedType indexedType = source.getRequest().getSearchQuery().getIndexedType();

		final IndexedTypeSort currentSort = source.getRequest().getCurrentSort();
		final String currentSortCode = currentSort != null ? currentSort.getCode() : null;

		final List<IndexedTypeSort> sorts = indexedType.getSorts();
		if (sorts != null && !sorts.isEmpty())
		{
			for (final IndexedTypeSort sort : sorts)
			{
				final SortData sortData = createSortData();
				sortData.setCode(sort.getCode());
				sortData.setName(sort.getName());

				if (currentSortCode != null && currentSortCode.equals(sort.getCode()))
				{
					sortData.setSelected(true);
				}

				result.add(sortData);
			}
		}


		return result;
	}

	protected SortData createSortData()
	{
		return new SortData();
	}
}

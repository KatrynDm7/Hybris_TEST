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

import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.IndexedPropertyValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class SearchResponseQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, SEARCH_QUERY_TYPE, SEARCH_RESULT_TYPE, ITEM> implements Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SEARCH_QUERY_TYPE, IndexedTypeSort, SEARCH_RESULT_TYPE>, FacetSearchPageData<SolrSearchQueryData, ITEM>>
{
	@Override
	public void populate(final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SEARCH_QUERY_TYPE, IndexedTypeSort, SEARCH_RESULT_TYPE> source, final FacetSearchPageData<SolrSearchQueryData, ITEM> target)
	{
		target.setCurrentQuery(buildSearchQueryData(source));
	}

	protected SolrSearchQueryData buildSearchQueryData(final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SEARCH_QUERY_TYPE, IndexedTypeSort, SEARCH_RESULT_TYPE> source)
	{
		return buildSearchQueryData(source.getRequest().getSearchText(), source.getRequest().getSearchQueryData().getCategoryCode(),
				source.getRequest().getCurrentSort(), source.getRequest().getIndexedPropertyValues());
	}

	protected SolrSearchQueryData buildSearchQueryData(final String searchText, final String categoryCode,
	                                                   final IndexedTypeSort currentSort, final List<IndexedPropertyValueData<IndexedProperty>> indexedPropertyValues)
	{
		final SolrSearchQueryData result = createSearchQueryData();
		result.setFreeTextSearch(searchText);
		result.setCategoryCode(categoryCode);
		if (currentSort != null)
		{
			result.setSort(currentSort.getCode());
		}

		final List<SolrSearchQueryTermData> terms = new ArrayList<SolrSearchQueryTermData>();

		if (indexedPropertyValues != null && !indexedPropertyValues.isEmpty())
		{
			for (final IndexedPropertyValueData<IndexedProperty> indexedPropertyValue : indexedPropertyValues)
			{
				final SolrSearchQueryTermData term = createSearchQueryTermData();
				term.setKey(indexedPropertyValue.getIndexedProperty().getName());
				term.setValue(indexedPropertyValue.getValue());
				terms.add(term);
			}
		}

		result.setFilterTerms(terms);

		return result;
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}

	protected SolrSearchQueryTermData createSearchQueryTermData()
	{
		return new SolrSearchQueryTermData();
	}
}

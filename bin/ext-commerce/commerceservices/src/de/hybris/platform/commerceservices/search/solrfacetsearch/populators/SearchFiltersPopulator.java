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

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.IndexedPropertyValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class SearchFiltersPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		// Convert the facet filters into IndexedPropertyValueData
		final List<IndexedPropertyValueData<IndexedProperty>> indexedPropertyValues = new ArrayList<IndexedPropertyValueData<IndexedProperty>>();
		final List<SolrSearchQueryTermData> terms = target.getSearchQueryData().getFilterTerms();
		if (terms != null && !terms.isEmpty())
		{
			for (final SolrSearchQueryTermData term : terms)
			{
				final IndexedProperty indexedProperty = target.getIndexedType().getIndexedProperties().get(term.getKey());
				if (indexedProperty != null)
				{
					final IndexedPropertyValueData<IndexedProperty> indexedPropertyValue = new IndexedPropertyValueData<IndexedProperty>();
					indexedPropertyValue.setIndexedProperty(indexedProperty);
					indexedPropertyValue.setValue(term.getValue());
					indexedPropertyValues.add(indexedPropertyValue);
				}
			}
		}
		target.setIndexedPropertyValues(indexedPropertyValues);

		// Add the facet filters
		for (final IndexedPropertyValueData<IndexedProperty> indexedPropertyValue : target.getIndexedPropertyValues())
		{
			target.getSearchQuery().addFacetValue(indexedPropertyValue.getIndexedProperty().getName(),
					indexedPropertyValue.getValue());
		}

		// Add category restriction
		if (target.getSearchQueryData().getCategoryCode() != null)
		{
			// allCategories field indexes all the separate category hierarchies
			target.getSearchQuery().addFacetValue("allCategories", target.getSearchQueryData().getCategoryCode());
		}
	}
}

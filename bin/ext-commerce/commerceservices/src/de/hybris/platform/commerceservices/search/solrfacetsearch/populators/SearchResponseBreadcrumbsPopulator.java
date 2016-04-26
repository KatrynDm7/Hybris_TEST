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

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.IndexedPropertyValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 */
public class SearchResponseBreadcrumbsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult>, FacetSearchPageData<SolrSearchQueryData, ITEM>>
{
	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult> source,
			final FacetSearchPageData<SolrSearchQueryData, ITEM> target)
	{
		target.setBreadcrumbs(buildBreadcrumbs(target.getCurrentQuery(), source.getRequest().getIndexedPropertyValues(), source
				.getRequest().getSearchQuery()));
	}

	protected List<BreadcrumbData<SolrSearchQueryData>> buildBreadcrumbs(final SolrSearchQueryData currentSearchQuery,
			final List<IndexedPropertyValueData<IndexedProperty>> indexedPropertyValues, final SearchQuery searchQuery)
	{
		final List<BreadcrumbData<SolrSearchQueryData>> result = new ArrayList<BreadcrumbData<SolrSearchQueryData>>();

		SolrSearchQueryData truncateQuery = cloneSearchQueryDataText(currentSearchQuery);

		if (indexedPropertyValues != null && !indexedPropertyValues.isEmpty())
		{
			final int breadcrumbsSize = indexedPropertyValues.size();
			for (int i = 0; i < breadcrumbsSize; i++)
			{
				final IndexedPropertyValueData<IndexedProperty> indexedPropertyValue = indexedPropertyValues.get(i);
				final IndexedProperty indexedProperty = indexedPropertyValue.getIndexedProperty();

				final String facet = indexedProperty.getName();
				String facetDisplayName = facet;
				final String displayName = indexedProperty.getDisplayName();
				if (displayName != null && !displayName.isEmpty())
				{
					facetDisplayName = displayName;
				}

				final String facetValue = indexedPropertyValue.getValue();
				String facetValueDisplayName = facetValue;
				final FacetDisplayNameProvider facetDisplayNameProvider = getFacetDisplayNameProvider(indexedProperty);
				if (facetDisplayNameProvider != null)
				{
					if (facetDisplayNameProvider instanceof FacetValueDisplayNameProvider)
					{
						facetValueDisplayName = ((FacetValueDisplayNameProvider) facetDisplayNameProvider).getDisplayName(
								searchQuery, indexedProperty, facetValue);
					}
					else
					{
						facetValueDisplayName = facetDisplayNameProvider.getDisplayName(searchQuery, facetValue);
					}
				}

				final BreadcrumbData<SolrSearchQueryData> breadcrumbData = createBreadcrumbData();

				breadcrumbData.setFacetCode(indexedProperty.getName());
				breadcrumbData.setFacetName(facetDisplayName);

				breadcrumbData.setFacetValueCode(facetValue);
				breadcrumbData.setFacetValueName(facetValueDisplayName);

				// Build the truncate query, which is the search up to this point in the breadcrumb trail
				// If this is the last breadcrumb then we don't need a truncate query as that is the same as the current query
				if (i + 1 < breadcrumbsSize)
				{
					truncateQuery = refineQueryAddFacet(truncateQuery, facet, facetValue);
					breadcrumbData.setTruncateQuery(truncateQuery);
				}

				breadcrumbData.setRemoveQuery(refineQueryRemoveFacet(currentSearchQuery, facet, facetValue));
				result.add(breadcrumbData);
			}
		}

		return result;
	}

	protected SolrSearchQueryData cloneSearchQueryDataText(final SolrSearchQueryData currentSearchQuery)
	{
		final SolrSearchQueryData result = cloneSearchQueryData(currentSearchQuery);
		result.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());
		return result;
	}

	/**
	 * Shallow clone of the source SearchQueryData
	 * 
	 * @param source
	 *            the instance to clone
	 * @return the shallow clone
	 */
	protected SolrSearchQueryData cloneSearchQueryData(final SolrSearchQueryData source)
	{
		final SolrSearchQueryData target = createSearchQueryData();
		target.setFreeTextSearch(source.getFreeTextSearch());
		target.setCategoryCode(source.getCategoryCode());
		target.setSort(source.getSort());
		target.setFilterTerms(source.getFilterTerms());
		return target;
	}

	protected SolrSearchQueryData refineQueryAddFacet(final SolrSearchQueryData searchQueryData, final String facet,
			final String facetValue)
	{
		final SolrSearchQueryTermData newTerm = createSearchQueryTermData();
		newTerm.setKey(facet);
		newTerm.setValue(facetValue);

		final List<SolrSearchQueryTermData> newTerms = new ArrayList<SolrSearchQueryTermData>(searchQueryData.getFilterTerms());
		newTerms.add(newTerm);

		// Build the new query data
		final SolrSearchQueryData result = cloneSearchQueryData(searchQueryData);
		result.setFilterTerms(newTerms);
		return result;
	}

	protected SolrSearchQueryData refineQueryRemoveFacet(final SolrSearchQueryData searchQueryData, final String facet,
			final String facetValue)
	{
		final List<SolrSearchQueryTermData> newTerms = new ArrayList<SolrSearchQueryTermData>(searchQueryData.getFilterTerms());

		// Remove the term for the specified facet
		final Iterator<SolrSearchQueryTermData> iterator = newTerms.iterator();
		while (iterator.hasNext())
		{
			final SolrSearchQueryTermData term = iterator.next();
			if (facet.equals(term.getKey()) && facetValue.equals(term.getValue()))
			{
				iterator.remove();
			}
		}

		// Build the new query data
		final SolrSearchQueryData result = cloneSearchQueryData(searchQueryData);
		result.setFilterTerms(newTerms);
		return result;
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}

	protected BreadcrumbData<SolrSearchQueryData> createBreadcrumbData()
	{
		return new BreadcrumbData<SolrSearchQueryData>();
	}

	protected SolrSearchQueryTermData createSearchQueryTermData()
	{
		return new SolrSearchQueryTermData();
	}

	public static FacetDisplayNameProvider getFacetDisplayNameProvider(final IndexedProperty property)
	{
		final String name = property.getFacetDisplayNameProvider();
		return name == null ? null : Registry.getGlobalApplicationContext().getBean(name, FacetDisplayNameProvider.class);
	}
}

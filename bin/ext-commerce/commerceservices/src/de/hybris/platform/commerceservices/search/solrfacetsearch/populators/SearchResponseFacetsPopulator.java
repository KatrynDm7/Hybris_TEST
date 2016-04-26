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
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.TopValuesProvider;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.CollectionUtils;


public class SearchResponseFacetsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult>, FacetSearchPageData<SolrSearchQueryData, ITEM>>,
		BeanFactoryAware
{

	private BeanFactory beanFactory;

	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult> source,
			final FacetSearchPageData<SolrSearchQueryData, ITEM> target)
	{
		final SearchResult solrSearchResult = source.getSearchResult();
		final SolrSearchQueryData solrSearchQueryData = target.getCurrentQuery();
		final IndexedType indexedType = source.getRequest().getSearchQuery().getIndexedType();

		if (solrSearchResult != null)
		{
			target.setFacets(buildFacets(solrSearchResult, solrSearchQueryData, indexedType));
		}
	}

	protected List<FacetData<SolrSearchQueryData>> buildFacets(final SearchResult solrSearchResult,
			final SolrSearchQueryData searchQueryData, final IndexedType indexedType)
	{
		final List<Facet> solrSearchResultFacets = solrSearchResult.getFacets();
		final List<FacetData<SolrSearchQueryData>> result = new ArrayList<FacetData<SolrSearchQueryData>>(
				solrSearchResultFacets.size());

		for (final Facet facet : solrSearchResultFacets)
		{
			final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get(facet.getName());

			// Ignore any facets with a priority less than or equal to 0 as they are for internal use only
			final FacetData<SolrSearchQueryData> facetData = createFacetData();
			facetData.setCode(facet.getName());
			facetData.setCategory(indexedProperty.isCategoryField());
			final String displayName = indexedProperty.getDisplayName();
			facetData.setName(displayName == null ? facet.getName() : displayName);
			facetData.setMultiSelect(indexedProperty.isMultiSelect());
			facetData.setPriority(indexedProperty.getPriority());
			facetData.setVisible(indexedProperty.isVisible());

			buildFacetValues(facetData, facet, indexedProperty, solrSearchResult, searchQueryData);

			// Only add the facet if there are values
			if (facetData.getValues() != null && !facetData.getValues().isEmpty())
			{
				result.add(facetData);
			}
		}

		return result;
	}

	protected void buildFacetValues(final FacetData<SolrSearchQueryData> facetData, final Facet facet,
			final IndexedProperty indexedProperty, final SearchResult solrSearchResult, final SolrSearchQueryData searchQueryData)
	{
		final List<FacetValue> facetValues = facet.getFacetValues();
		if (facetValues != null && !facetValues.isEmpty())
		{
			final List<FacetValueData<SolrSearchQueryData>> allFacetValues = new ArrayList<FacetValueData<SolrSearchQueryData>>(
					facetValues.size());

			for (final FacetValue facetValue : facetValues)
			{
				final FacetValueData<SolrSearchQueryData> facetValueData = buildFacetValue(facetData, facet, facetValue,
						solrSearchResult, searchQueryData);
				if (facetValueData != null)
				{
					allFacetValues.add(facetValueData);
				}
			}

			facetData.setValues(allFacetValues);

			final TopValuesProvider topValuesProvider = getTopValuesProvider(indexedProperty);
			if (!isRanged(indexedProperty) && topValuesProvider != null)
			{
				final List<FacetValue> topFacetValues = topValuesProvider.getTopValues(indexedProperty, facetValues);
				if (topFacetValues != null)
				{
					final List<FacetValueData<SolrSearchQueryData>> topFacetValuesData = new ArrayList<FacetValueData<SolrSearchQueryData>>();
					for (final FacetValue facetValue : topFacetValues)
					{
						final FacetValueData<SolrSearchQueryData> topFacetValueData = buildFacetValue(facetData, facet, facetValue,
								solrSearchResult, searchQueryData);
						if (topFacetValueData != null)
						{
							topFacetValuesData.add(topFacetValueData);
						}
					}
					facetData.setTopValues(topFacetValuesData);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	protected FacetValueData<SolrSearchQueryData> buildFacetValue(final FacetData<SolrSearchQueryData> facetData,
			final Facet facet, final FacetValue facetValue, final SearchResult searchResult,
			final SolrSearchQueryData searchQueryData)
	{
		final FacetValueData<SolrSearchQueryData> facetValueData = createFacetValueData();
		facetValueData.setCode(facetValue.getName());
		facetValueData.setName(facetValue.getDisplayName());
		facetValueData.setCount(facetValue.getCount());

		// Check if the facet is selected
		facetValueData.setSelected(isFacetSelected(searchQueryData, facet.getName(), facetValue.getName()));

		if (facetValueData.isSelected())
		{
			// Query to remove, rather than add facet
			facetValueData.setQuery(refineQueryRemoveFacet(searchQueryData, facet.getName(), facetValue.getName()));
		}
		else
		{
			// Query to add the facet
			facetValueData.setQuery(refineQueryAddFacet(searchQueryData, facet.getName(), facetValue.getName()));
		}

		return facetValueData;
	}

	protected boolean isFacetSelected(final SolrSearchQueryData searchQueryData, final String facetName, final String facetValue)
	{
		for (final SolrSearchQueryTermData termData : searchQueryData.getFilterTerms())
		{
			if (termData.getKey().equals(facetName) && termData.getValue().equals(facetValue))
			{
				return true;
			}
		}
		return false;
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

	/**
	 * Shallow clone of the source SearchQueryData
	 *
	 * @param source
	 *           the instance to clone
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

	protected FacetData<SolrSearchQueryData> createFacetData()
	{
		return new FacetData<SolrSearchQueryData>();
	}

	protected FacetValueData<SolrSearchQueryData> createFacetValueData()
	{
		return new FacetValueData<SolrSearchQueryData>();
	}

	protected SolrSearchQueryTermData createSearchQueryTermData()
	{
		return new SolrSearchQueryTermData();
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}

	protected TopValuesProvider getTopValuesProvider(final IndexedProperty property)
	{
		final String name = property.getTopValuesProvider();
		return name == null ? null : beanFactory.getBean(name, TopValuesProvider.class);
	}

	protected boolean isRanged(final IndexedProperty property)
	{
		return !CollectionUtils.isEmpty(property.getValueRangeSets());
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		this.beanFactory = beanFactory;
	}
}

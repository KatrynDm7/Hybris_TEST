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
import de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.FreeTextQueryBuilder;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class SearchTextPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private List<FreeTextQueryBuilder> freeTextQueryBuilders;

	public List<FreeTextQueryBuilder> getFreeTextQueryBuilders()
	{
		return freeTextQueryBuilders;
	}

	@Required
	public void setFreeTextQueryBuilders(final List<FreeTextQueryBuilder> freeTextQueryBuilders)
	{
		this.freeTextQueryBuilders = freeTextQueryBuilders;
	}

	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		// cleanup the search text
		final String cleanedFreeTextSearch = cleanupSolrSearchText(target.getSearchQueryData().getFreeTextSearch());
		target.setSearchText(cleanedFreeTextSearch);

		final SearchConfig searchConfig = target.getFacetSearchConfig().getSearchConfig();
		if (searchConfig != null && searchConfig.isLegacyMode() && StringUtils.isNotBlank(cleanedFreeTextSearch))
		{
			addFreeTextQuery(target.getSearchQuery(), cleanedFreeTextSearch);
		}
	}

	protected String cleanupSolrSearchText(final String text)
	{
		String cleanedText = text;
		if (text == null)
		{
			cleanedText = "";
		}
		else
		{
			// Replace ':' special character in solr text with whitespace
			cleanedText = cleanedText.replace(':', ' ').trim();

			// Replace SOLR keywords with lowercased
			cleanedText = cleanedText.replaceAll("AND", "and");
			cleanedText = cleanedText.replaceAll("OR", "or");
		}

		return cleanedText;
	}

	protected void addFreeTextQuery(final SearchQuery searchQuery, final String cleanedFreeTextSearch)
	{
		// Split the full text string to separate words
		final String[] words = cleanedFreeTextSearch.split("\\s+");

		for (final FreeTextQueryBuilder freeTextQueryBuilder : getFreeTextQueryBuilders())
		{
			freeTextQueryBuilder.addFreeTextQuery(searchQuery, cleanedFreeTextSearch, words);
		}
	}
}

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
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.DocumentData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class SearchResponseResultsPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult>, SearchPageData<ITEM>>
{
	private Converter<DocumentData, ITEM> searchResultConverter;

	protected Converter<DocumentData, ITEM> getSearchResultConverter()
	{
		return searchResultConverter;
	}

	@Required
	public void setSearchResultConverter(final Converter<DocumentData, ITEM> searchResultConverter)
	{
		this.searchResultConverter = searchResultConverter;
	}

	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, de.hybris.platform.solrfacetsearch.search.SearchResult> source,
			final SearchPageData<ITEM> target)
	{
		target.setResults(buildResults(source.getSearchResult(), source.getRequest().getSearchQuery()));
	}

	protected List<ITEM> buildResults(final SearchResult searchResult, final SearchQuery searchQuery)
	{
		if (searchResult == null)
		{
			return Collections.emptyList();
		}

		final List<ITEM> result = new ArrayList<ITEM>(searchResult.getPageSize());

		for (final Document document : searchResult.getDocuments())
		{
			result.add(convertResultDocument(searchQuery, document));
		}

		return result;
	}

	protected ITEM convertResultDocument(final SearchQuery searchQuery, final Document document)
	{
		final DocumentData<SearchQuery, Document> documentData = createDocumentData();
		documentData.setSearchQuery(searchQuery);
		documentData.setDocument(document);
		return getSearchResultConverter().convert(documentData);
	}

	protected DocumentData<SearchQuery, Document> createDocumentData()
	{
		return new DocumentData<SearchQuery, Document>();
	}
}

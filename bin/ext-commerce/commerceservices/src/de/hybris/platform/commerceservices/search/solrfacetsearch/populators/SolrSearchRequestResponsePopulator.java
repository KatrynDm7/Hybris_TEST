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


import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.SolrKeywordRedirectService;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrException;
import org.springframework.beans.factory.annotation.Required;


/**
 * Convert the SolrSearchRequest into a SolrSearchResponse by executing the SOLR search.
 */
public class SolrSearchRequestResponsePopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>, SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult>>
{
	private static final Logger LOG = Logger.getLogger(SolrSearchRequestResponsePopulator.class);

	private FacetSearchService solrFacetSearchService;
	private SolrKeywordRedirectService solrKeywordRedirectService;

	protected FacetSearchService getSolrFacetSearchService()
	{
		return solrFacetSearchService;
	}

	@Required
	public void setSolrFacetSearchService(final FacetSearchService solrFacetSearchService)
	{
		this.solrFacetSearchService = solrFacetSearchService;
	}

	public SolrKeywordRedirectService getSolrKeywordRedirectService()
	{
		return solrKeywordRedirectService;
	}

	@Required
	public void setSolrKeywordRedirectService(final SolrKeywordRedirectService solrKeywordRedirectService)
	{
		this.solrKeywordRedirectService = solrKeywordRedirectService;
	}

	@Override
	public void populate(
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> source,
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE, SearchResult> target)
	{
		try
		{
			target.setRequest(source);
			final SearchResult searchResult = getSolrFacetSearchService().search(source.getSearchQuery());
			if (searchResult instanceof SolrSearchResult)
			{
				getSolrKeywordRedirectService().attachKeywordRedirect((SolrSearchResult) searchResult);
			}
			target.setSearchResult(searchResult);
		}
		catch (final FacetSearchException | SolrException ex)
		{
			LOG.error("Exception while executing SOLR search", ex);
		}
	}
}

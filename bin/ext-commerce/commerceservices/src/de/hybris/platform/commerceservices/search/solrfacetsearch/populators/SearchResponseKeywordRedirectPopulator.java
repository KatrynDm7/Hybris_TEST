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

import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect.KeywordRedirectUrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;
import de.hybris.platform.solrfacetsearch.search.KeywordRedirectValue;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator for keyword redirects retrieved from solrfacetsearch
 */
public class SearchResponseKeywordRedirectPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, STATE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SearchResult>, ProductSearchPageData<STATE, ITEM>>
{
	private TypeService typeService;
	private Map<String, KeywordRedirectUrlResolver> keywordRedirectUrlResolvers;

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected Map<String, KeywordRedirectUrlResolver> getKeywordRedirectUrlResolvers()
	{
		return keywordRedirectUrlResolvers;
	}

	@Required
	public void setKeywordRedirectUrlResolvers(final Map<String, KeywordRedirectUrlResolver> keywordRedirectUrlResolvers)
	{
		this.keywordRedirectUrlResolvers = keywordRedirectUrlResolvers;
	}

	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SearchResult> source,
			final ProductSearchPageData<STATE, ITEM> target)
	{
		final String keywordRedirectUrl = buildKeywordRedirectUrl(source.getSearchResult());
		if (keywordRedirectUrl != null && !keywordRedirectUrl.isEmpty())
		{
			target.setKeywordRedirectUrl(keywordRedirectUrl);
		}
	}

	protected String buildKeywordRedirectUrl(final SearchResult solrSearchResult)
	{
		if (solrSearchResult != null && solrSearchResult.getKeywordRedirects() != null)
		{
			for (final KeywordRedirectValue redirect : solrSearchResult.getKeywordRedirects())
			{
				final String url = getKeywordRedirectUrl(redirect.getRedirect());
				if (url != null && !url.isEmpty())
				{
					return url;
				}
			}
		}

		return null;
	}

	protected <T extends SolrAbstractKeywordRedirectModel> String getKeywordRedirectUrl(final T redirect)
	{
		final KeywordRedirectUrlResolver<T> urlBuilder = getKeywordRedirectUrlResolver(redirect);
		if (urlBuilder != null)
		{
			final String url = urlBuilder.resolve(redirect);
			if (url != null && !url.isEmpty())
			{
				return url;
			}
		}
		return null;
	}

	protected <T extends SolrAbstractKeywordRedirectModel> KeywordRedirectUrlResolver<T> getKeywordRedirectUrlResolver(
			final SolrAbstractKeywordRedirectModel keywordRedirect)
	{
		final Map<String, KeywordRedirectUrlResolver> map = getKeywordRedirectUrlResolvers();

		final ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(keywordRedirect.getClass());

		for (ComposedTypeModel ct = composedType; ct != null; ct = ct.getSuperType())
		{
			final KeywordRedirectUrlResolver<T> keywordRedirectURLBuilder = map.get(ct.getCode());
			if (keywordRedirectURLBuilder != null)
			{
				return keywordRedirectURLBuilder;
			}
		}
		return null;
	}
}

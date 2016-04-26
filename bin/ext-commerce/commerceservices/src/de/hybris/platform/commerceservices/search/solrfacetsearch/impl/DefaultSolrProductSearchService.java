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
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.ProductSearchService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.convert.converter.Converter;


/**
 * Default implementation of the ProductSearchService
 */
public class DefaultSolrProductSearchService<ITEM> implements
		ProductSearchService<SolrSearchQueryData, ITEM, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultSolrProductSearchService.class);

	private Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter;
	private Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter;
	private Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResponseConverter;

	protected Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> getSearchQueryPageableConverter()
	{
		return searchQueryPageableConverter;
	}

	@Required
	public void setSearchQueryPageableConverter(
			final Converter<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest> searchQueryPageableConverter)
	{
		this.searchQueryPageableConverter = searchQueryPageableConverter;
	}

	protected Converter<SolrSearchRequest, SolrSearchResponse> getSearchRequestConverter()
	{
		return searchRequestConverter;
	}

	@Required
	public void setSearchRequestConverter(final Converter<SolrSearchRequest, SolrSearchResponse> searchRequestConverter)
	{
		this.searchRequestConverter = searchRequestConverter;
	}

	protected Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> getSearchResponseConverter()
	{
		return searchResponseConverter;
	}

	@Required
	public void setSearchResponseConverter(
			final Converter<SolrSearchResponse, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>> searchResponseConverter)
	{
		this.searchResponseConverter = searchResponseConverter;
	}

	// End spring inject methods

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> textSearch(final String text,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setFreeTextSearch(text);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> categorySearch(final String categoryCode,
			final PageableData pageableData)
	{
		final SolrSearchQueryData searchQueryData = createSearchQueryData();
		searchQueryData.setCategoryCode(categoryCode);
		searchQueryData.setFilterTerms(Collections.<SolrSearchQueryTermData> emptyList());

		return doSearch(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> searchAgain(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		return doSearch(searchQueryData, pageableData);
	}

	protected ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> doSearch(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		validateParameterNotNull(searchQueryData, "SearchQueryData cannot be null");

		// Create the SearchQueryPageableData that contains our parameters
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = buildSearchQueryPageableData(searchQueryData,
				pageableData);

		// Build up the search request
		final SolrSearchRequest solrSearchRequest = getSearchQueryPageableConverter().convert(searchQueryPageableData);

		// Execute the search
		final SolrSearchResponse solrSearchResponse = getSearchRequestConverter().convert(solrSearchRequest);

		// Convert the response
		return getSearchResponseConverter().convert(solrSearchResponse);
	}

	protected SearchQueryPageableData<SolrSearchQueryData> buildSearchQueryPageableData(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		final SearchQueryPageableData<SolrSearchQueryData> searchQueryPageableData = createSearchQueryPageableData();
		searchQueryPageableData.setSearchQueryData(searchQueryData);
		searchQueryPageableData.setPageableData(pageableData);
		return searchQueryPageableData;
	}

	// Create methods for data object - can be overridden in spring config

	protected SearchQueryPageableData<SolrSearchQueryData> createSearchQueryPageableData()
	{
		return new SearchQueryPageableData<SolrSearchQueryData>();
	}

	protected SolrSearchQueryData createSearchQueryData()
	{
		return new SolrSearchQueryData();
	}
}

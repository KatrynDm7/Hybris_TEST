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
package de.hybris.platform.commerceservices.search.dao.impl;


import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Required;


public class DefaultPagedGenericDao<M extends ItemModel> implements PagedGenericDao<M>
{
	private final String typeCode;
	private PagedFlexibleSearchService pagedFlexibleSearchService;


	public DefaultPagedGenericDao(final String typeCode)
	{
		this.typeCode = typeCode;
	}

	@Required
	public void setPagedFlexibleSearchService(final PagedFlexibleSearchService pagedFlexibleSearchService)
	{
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}

	/**
	 * Accessor for a {@link PagedFlexibleSearchService} which might be helpful on higher levels of the class hierarchy.
	 * 
	 * @return the PagedFlexibleSearchService
	 */
	protected PagedFlexibleSearchService getPagedFlexibleSearchService()
	{
		return pagedFlexibleSearchService;
	}


	@Override
	public SearchPageData<M> find(final PageableData pageableData)
	{
		return this.find(new HashMap<String, Object>(), pageableData);
	}

	@Override
	public SearchPageData<M> find(final Map<String, ?> params, final PageableData pageableData)
	{
		final FlexibleSearchQuery query = createFlexibleSearchQuery(params, pageableData);
		return getPagedFlexibleSearchService().search(query, pageableData);
	}

	@Override
	public SearchPageData<M> find(final SortParameters sortParameters, final PageableData pageableData)
	{
		final FlexibleSearchQuery query = createFlexibleSearchQuery(sortParameters, pageableData);
		final SearchPageData<M> searchPageData = getPagedFlexibleSearchService().search(query, pageableData);
		return searchPageData;
	}

	@Override
	public SearchPageData<M> find(final Map<String, ?> params, final SortParameters sortParameters, final PageableData pageableData)
	{
		final FlexibleSearchQuery query = createFlexibleSearchQuery(params, sortParameters, pageableData);
		return getPagedFlexibleSearchService().search(query, pageableData);
	}


	protected FlexibleSearchQuery createFlexibleSearchQuery(final Map<String, ?> params, final PageableData pageableData)
	{
		final StringBuilder builder = createQueryString();
		appendWhereClausesToBuilder(builder, params);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
		query.setCount(pageableData.getPageSize());
		if (params != null && !params.isEmpty())
		{
			query.addQueryParameters(params);
		}
		return query;
	}

	protected FlexibleSearchQuery createFlexibleSearchQuery(final SortParameters sortParameters, final PageableData pageableData)
	{
		final StringBuilder builder = createQueryString();
		appendOrderByClausesToBuilder(builder, sortParameters);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
		query.setCount(pageableData.getPageSize());
		return query;
	}


	protected FlexibleSearchQuery createFlexibleSearchQuery(final Map<String, ?> params, final SortParameters sortParameters,
			final PageableData pageableData)
	{
		final StringBuilder builder = createQueryString();
		appendWhereClausesToBuilder(builder, params);
		appendOrderByClausesToBuilder(builder, sortParameters);
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(builder.toString());
		searchQuery.setNeedTotal(true);
		searchQuery.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());
		searchQuery.setCount(pageableData.getPageSize());
		if (params != null && !params.isEmpty())
		{
			searchQuery.addQueryParameters(params);
		}
		return searchQuery;
	}

	protected StringBuilder createQueryString()
	{
		final StringBuilder builder = new StringBuilder(25);
		builder.append("SELECT {c:").append(ItemModel.PK).append("} ");
		builder.append("FROM {").append(typeCode).append(" AS c} ");
		return builder;
	}

	protected void appendWhereClausesToBuilder(final StringBuilder builder, final Map<String, ?> params)
	{
		if (params != null && !params.isEmpty())
		{
			builder.append("WHERE ");
			boolean firstParam = true;
			for (final String paramName : params.keySet())
			{
				if (!firstParam)
				{
					builder.append("AND ");
				}
				builder.append("{c:").append(paramName).append("}=?").append(paramName).append(' ');
				firstParam = false;
			}
		}
	}

	protected void appendOrderByClausesToBuilder(final StringBuilder builder, final SortParameters sortParameters)
	{
		if (sortParameters != null && !sortParameters.isEmpty())
		{
			builder.append("ORDER BY ");
			boolean firstParam = true;
			final Map<String, SortParameters.SortOrder> sortParams = sortParameters.getSortParameters();
			for (final Entry<String, SortParameters.SortOrder> entry : sortParams.entrySet())
			{
				if (!firstParam)
				{
					builder.append(", ");
				}
				builder.append("{c:").append(entry.getKey()).append("} ").append(entry.getValue());
				firstParam = false;
			}
		}
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}


	protected SortQueryData createSortQueryData(final String sortCode, final Map<String, ?> params,
			final SortParameters sortParameters, final PageableData pageableData)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(this.createFlexibleSearchQuery(params, sortParameters, pageableData).getQuery());
		return result;
	}
}

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
package de.hybris.platform.commerceservices.search.flexiblesearch;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;
import java.util.Map;

/**
 * The PagedFlexibleSearchService interface supports executing flexible search queries returning paginated results.
 * The current page, page size, and sort are specified using the {@link PageableData} parameter. The results
 * are returned in a parametrized {@link SearchPageData} result which includes the search results for the requested
 * page, the pagination data, and the available sort options.
 */
public interface PagedFlexibleSearchService
{
	<T> SearchPageData<T> search(FlexibleSearchQuery searchQuery, PageableData pageableData);

	<T> SearchPageData<T> search(String query, Map<String, ?> queryParams, PageableData pageableData);

	<T> SearchPageData<T> search(List<SortQueryData> sortQueries, String defaultSortCode, Map<String, ?> queryParams, PageableData pageableData);
}

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
package de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;

/**
 * Interface used by the DefaultSearchFacade to allow the free text query to be built from
 * a number of beans.
 */
public interface FreeTextQueryBuilder
{
	/**
	 * Add a free text query to the search query.
	 *
	 * @param searchQuery The search query to add search terms to
	 * @param fullText    The full text of the query
	 * @param textWords   The full text query split into words
	 */
	void addFreeTextQuery(final SearchQuery searchQuery, final String fullText, final String[] textWords);
}

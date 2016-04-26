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
package de.hybris.platform.b2bacceleratorfacades.api.search;

import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;


/**
 * Defines the search funtionality for different types of data objects, using the concept of pagination and states.
 * 
 * @param <ITEM>
 *           Type of item to be searched.
 * @param <STATE>
 *           Type of search state depending containing the information about the query.
 */
public interface SearchFacade<ITEM, STATE extends SearchStateData>
{

	/**
	 * Searches for the ITEM's based on the search state. The query object allows more complex queries using facet
	 * selection. The SearchStateData must have been obtained from the results of a call to {@link #textSearch(String)}.
	 * 
	 * @param searchState
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	public SearchPageData<ITEM> search(final STATE searchState, final PageableData pageableData);

	/**
	 * Get the auto complete suggestions for the provided input.
	 * 
	 * @param searchState
	 *           the search query object
	 * @return a list of suggested search terms
	 */
	public List<AutocompleteSuggestionData> autocomplete(final STATE searchState);

}

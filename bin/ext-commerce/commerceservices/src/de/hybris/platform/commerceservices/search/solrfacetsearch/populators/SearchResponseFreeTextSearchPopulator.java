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
import de.hybris.platform.converters.Populator;

/**
 */
public class SearchResponseFreeTextSearchPopulator<STATE, ITEM> implements Populator<SolrSearchResponse, ProductSearchPageData<STATE, ITEM>>
{
	@Override
	public void populate(final SolrSearchResponse source, final ProductSearchPageData<STATE, ITEM> target)
	{
		target.setFreeTextSearch(source.getRequest().getSearchText());
	}
}

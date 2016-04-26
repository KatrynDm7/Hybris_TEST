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
package de.hybris.platform.commerceservices.search.solrfacetsearch.strategies;

import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;



/**
 * Resolves suitable {@link SolrFacetSearchConfigModel} that should be used for searching in the current session
 * context.<br>
 * 
 * @author krzysztof.kwiatosz
 * 
 */
public interface SolrFacetSearchConfigSelectionStrategy
{

	/**
	 * Resolves suitable {@link SolrFacetSearchConfigModel} that should be used for searching in the current session
	 * 
	 * @return {@link SolrFacetSearchConfigModel}
	 * @throws NoValidSolrConfigException
	 */
	SolrFacetSearchConfigModel getCurrentSolrFacetSearchConfig() throws NoValidSolrConfigException;

}

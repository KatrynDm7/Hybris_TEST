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
package de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions;

import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;


/**
 * Throws when {@link SolrFacetSearchConfigSelectionStrategy} cannot resolve suitable solr index config for the current
 * context.
 * 
 * @author krzysztof.kwiatosz
 * 
 */
public class NoValidSolrConfigException extends Exception
{

	/**
	 * @param message
	 */
	public NoValidSolrConfigException(final String message)
	{
		super(message);
	}

}

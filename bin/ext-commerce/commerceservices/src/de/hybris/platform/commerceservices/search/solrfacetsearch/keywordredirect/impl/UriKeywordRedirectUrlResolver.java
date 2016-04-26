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
package de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect.KeywordRedirectUrlResolver;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrURIRedirectModel;

/**
 * trivial KeywordRedirectUrlResolver for Redirects to static URLs
 */
public class UriKeywordRedirectUrlResolver implements KeywordRedirectUrlResolver<SolrURIRedirectModel>
{
	@Override
	public String resolve(final SolrURIRedirectModel redirect)
	{
		return redirect.getUrl();
	}
}

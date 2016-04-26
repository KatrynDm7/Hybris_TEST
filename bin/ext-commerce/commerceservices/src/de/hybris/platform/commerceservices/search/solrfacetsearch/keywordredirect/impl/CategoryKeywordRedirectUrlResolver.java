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

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.keywordredirect.KeywordRedirectUrlResolver;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrCategoryRedirectModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * KeywordRedirectUrlResolver for redirects to product pages
 */
public class CategoryKeywordRedirectUrlResolver implements KeywordRedirectUrlResolver<SolrCategoryRedirectModel>
{
	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	protected UrlResolver<CategoryModel> getCategoryModelUrlResolver()
	{
		return categoryModelUrlResolver;
	}

	@Required
	public void setCategoryModelUrlResolver(final UrlResolver<CategoryModel> categoryModelUrlResolver)
	{
		this.categoryModelUrlResolver = categoryModelUrlResolver;
	}

	@Override
	public String resolve(final SolrCategoryRedirectModel redirect)
	{
		final CategoryModel category = redirect.getRedirectItem();
		if (category != null)
		{
			// todo: check if product is actually from a catalog version that belongs to this base site
			final String url = getCategoryModelUrlResolver().resolve(category);

			if (url != null && !url.isEmpty())
			{
				return url;
			}
		}

		return null;
	}
}

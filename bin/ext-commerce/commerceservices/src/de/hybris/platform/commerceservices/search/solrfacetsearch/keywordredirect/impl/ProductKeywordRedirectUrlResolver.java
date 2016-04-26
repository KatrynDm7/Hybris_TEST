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
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrProductRedirectModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * KeywordRedirectUrlResolver for redirects to product pages
 */
public class ProductKeywordRedirectUrlResolver implements KeywordRedirectUrlResolver<SolrProductRedirectModel>
{
	private UrlResolver<ProductModel> productModelUrlResolver;

	protected UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	@Required
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}

	@Override
	public String resolve(final SolrProductRedirectModel redirect)
	{
		final ProductModel product = redirect.getRedirectItem();
		if (product != null)
		{
			// todo: check if product is actually from a catalog version that belongs to this base site
			final String url = getProductModelUrlResolver().resolve(product);

			if (url != null && !url.isEmpty())
			{
				return url;
			}
		}

		return null;
	}
}

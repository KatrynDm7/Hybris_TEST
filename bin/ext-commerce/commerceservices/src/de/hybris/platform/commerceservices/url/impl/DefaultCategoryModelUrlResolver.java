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
package de.hybris.platform.commerceservices.url.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * URL resolver for CategoryModel instances. The pattern could be of the form: /{category-path}/c/{category-code}
 */
public class DefaultCategoryModelUrlResolver extends AbstractUrlResolver<CategoryModel>
{
	private final String CACHE_KEY = DefaultCategoryModelUrlResolver.class.getName();

	private CommerceCategoryService commerceCategoryService;
	private BaseSiteService baseSiteService;
	private String pattern;

	protected CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	@Required
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected String getPattern()
	{
		return pattern;
	}

	@Required
	public void setPattern(final String pattern)
	{
		this.pattern = pattern;
	}

	@Override
	protected String getKey(final CategoryModel source)
	{
		return CACHE_KEY + "." + source.getPk().toString();
	}

	@Override
	protected String resolveInternal(final CategoryModel source)
	{
		// Work out values

		// Replace pattern values
		String url = getPattern();
		if (url.contains("{baseSite-uid}"))
		{
			url = url.replace("{baseSite-uid}", getBaseSiteUid());
		}
		if (url.contains("{category-path}"))
		{
			final String categoryPath = buildPathString(getCategoryPath(source));
			url = url.replace("{category-path}", categoryPath);
		}
		if (url.contains("{category-code}"))
		{
			final String categoryCode = urlEncode(source.getCode()).replaceAll("\\+", "%20");
			url = url.replace("{category-code}", categoryCode);
		}
		if (url.contains("{catalog-id}"))
		{
			url = url.replace("{catalog-id}", source.getCatalogVersion().getCatalog().getId());
		}
		if (url.contains("{catalogVersion}"))
		{
			url = url.replace("{catalogVersion}", source.getCatalogVersion().getVersion());
		}

		return url;

	}

	protected CharSequence getBaseSiteUid()
	{
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
		if (currentBaseSite == null)
		{
			return "{baseSiteUid}";
		}
		else
		{
			return currentBaseSite.getUid();
		}
	}

	protected String buildPathString(final List<CategoryModel> path)
	{
		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); i++)
		{
			if (i != 0)
			{
				result.append('/');
			}
			result.append(urlSafe(path.get(i).getName()));
		}

		return result.toString();
	}

	protected List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection<List<CategoryModel>> paths = getCommerceCategoryService().getPathsForCategory(category);
		// Return first - there will always be at least 1
		return paths.iterator().next();
	}
}

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
package de.hybris.platform.commerceservices.category.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.impl.CatalogUtils;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * A category service that lookup up a category by code, but only includes product catalogs.
 */
public class DefaultCommerceCategoryService implements CommerceCategoryService
{
	private static final Logger LOG = Logger.getLogger(DefaultCommerceCategoryService.class);

	private CategoryService categoryService;
	private CatalogVersionService catalogVersionService;


	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	@Override
	public CategoryModel getCategoryForCode(final String code)
	{
		Assert.notNull(code);

		// Get all the categories for the specified code
		final Collection<CategoryModel> categoriesForCode = getCategoryService().getCategoriesForCode(code);

		// Filter the categories and return the first not in a classification system
		for (final CategoryModel categoryModel : categoriesForCode)
		{
			if (isSupportedCategory(categoryModel))
			{
				return categoryModel;
			}
		}

		throw new UnknownIdentifierException("Category with code '" + code + "' not found! (Active session catalogversions: " + getCatalogVersionsString() + ")");
	}

	private String getCatalogVersionsString()
	{
		return CatalogUtils.getCatalogVersionsString(getCatalogVersionService().getSessionCatalogVersions());
	}

	@Override
	public Collection<List<CategoryModel>> getPathsForCategory(final CategoryModel category)
	{
		validateParameterNotNull(category, "Parameter 'category' was null.");
		return getPathsInternal(category, new HashSet<CategoryModel>(Collections.singleton(category)));
	}

	protected Collection<List<CategoryModel>> getPathsInternal(final CategoryModel category, final Set<CategoryModel> controlSet)
	{
		Collection<List<CategoryModel>> result = null;

		final Collection<CategoryModel> superCategories = category.getSupercategories();
		if (CollectionUtils.isNotEmpty(superCategories))
		{
			for (final CategoryModel parent : superCategories)
			{
				if (isSupportedCategory(parent))
				{
					if (notVisited(parent, controlSet))
					{
						if (result == null)
						{
							result = new LinkedList<List<CategoryModel>>();
						}
						visitSuperCategory(category, parent, controlSet, result);
						markNotVisited(parent, controlSet);
					}
					else
					{
						LOG.warn("path cycle found for category: [" + category.getCode() + "]");
					}
				}
			}
		}

		return result == null ? Collections.singletonList(Collections.singletonList(category)) : result;
	}

	protected void markNotVisited(final CategoryModel cat, final Set<CategoryModel> visitedCategories)
	{
		visitedCategories.remove(cat);
	}

	protected boolean notVisited(final CategoryModel cat, final Set<CategoryModel> visitedCategories)
	{
		return visitedCategories.add(cat);
	}

	protected void visitSuperCategory(final CategoryModel category, final CategoryModel parent, final Set<CategoryModel> controlSet,
	                                final Collection<List<CategoryModel>> result)
	{
		for (List<CategoryModel> parentPath : getPathsInternal(parent, controlSet))
		{
			if (!(parentPath instanceof LinkedList))
			{
				parentPath = new LinkedList<CategoryModel>(parentPath);
			}
			parentPath.add(category);
			result.add(parentPath);
		}
	}

	protected boolean isSupportedCategory(final CategoryModel categoryModel)
	{
		return (!(categoryModel.getCatalogVersion() instanceof ClassificationSystemVersionModel));
	}
}

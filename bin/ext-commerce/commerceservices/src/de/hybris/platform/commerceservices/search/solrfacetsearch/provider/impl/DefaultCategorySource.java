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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Source of categories for CategoryCodeValueProvider. Does not include ClassificationClasses by default. Optional root
 * category is used to restrict categories found to categories that have a path to the root.
 */
public class DefaultCategorySource implements CategorySource
{
	private static final Logger LOG = Logger.getLogger(DefaultCategorySource.class);

	private ModelService modelService;
	private String categoriesQualifier;
	private boolean includeClassificationClasses;
	private String rootCategory;
	private CategoryService categoryService;

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected String getCategoriesQualifier()
	{
		return categoriesQualifier;
	}

	@Required
	public void setCategoriesQualifier(final String categoriesQualifier)
	{
		this.categoriesQualifier = categoriesQualifier;
	}

	public boolean isIncludeClassificationClasses()
	{
		return includeClassificationClasses;
	}

	// Optional
	public void setIncludeClassificationClasses(final boolean includeClassificationClasses)
	{
		this.includeClassificationClasses = includeClassificationClasses;
	}

	protected String getRootCategory()
	{
		return rootCategory;
	}

	// Optional
	public void setRootCategory(final String rootCategory)
	{
		this.rootCategory = rootCategory;
	}

	protected CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	@Override
	public Collection<CategoryModel> getCategoriesForConfigAndProperty(final IndexConfig indexConfig,
			final IndexedProperty indexedProperty, final Object model)
	{
		final Set<ProductModel> products = getProducts(model);
		final Set<CategoryModel> directCategories = getDirectSuperCategories(products);

		if (directCategories != null && !directCategories.isEmpty())
		{
			// Lookup the root categories - null if no root categories
			//	final Set<CategoryModel> rootCategories = lookupRootCategories(indexConfig.getCatalogVersions());
			final Collection<CatalogVersionModel> catalogVersions = Collections.singletonList(((ProductModel) model)
					.getCatalogVersion());
			final Set<CategoryModel> rootCategories = lookupRootCategories(catalogVersions);

			final Set<CategoryModel> allCategories = new HashSet<CategoryModel>();
			for (final CategoryModel category : directCategories)
			{
				allCategories.addAll(getAllCategories(category, rootCategories));
			}
			return allCategories;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected Set<ProductModel> getProducts(final Object model)
	{
		if (model instanceof VariantProductModel)
		{
			// Collect all the variant products and all their super variants, until the final base product is hit
			final Set<ProductModel> products = new HashSet<ProductModel>();

			ProductModel currentProduct = (ProductModel) model;
			while (currentProduct instanceof VariantProductModel)
			{
				products.add(currentProduct);
				currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();
			}

			products.add(currentProduct);
			return products;
		}
		else if (model instanceof ProductModel)
		{
			return Collections.singleton((ProductModel) model);
		}
		return Collections.emptySet();
	}

	protected Set<CategoryModel> getDirectSuperCategories(final Set<ProductModel> products)
	{
		final Set<CategoryModel> categories = new HashSet<CategoryModel>();

		for (final ProductModel product : products)
		{
			final Collection<CategoryModel> directCategories = getModelService()
					.getAttributeValue(product, getCategoriesQualifier());
			if (directCategories != null && !directCategories.isEmpty())
			{
				categories.addAll(directCategories);
			}
		}

		return categories;
	}

	protected Collection<CategoryModel> getAllCategories(final CategoryModel directCategory,
			final Set<CategoryModel> rootCategories)
	{
		if (isBlockedCategory(directCategory))
		{
			// This category is blocked - ignore it and all super categories
			return Collections.emptyList();
		}

		if (rootCategories != null && !rootCategories.isEmpty())
		{
			// We have root categories - use collect
			return collectSuperCategories(directCategory, rootCategories, new HashSet<CategoryModel>(3));
		}
		else
		{
			// Traverse all the super-categories
			final Collection<CategoryModel> categories = new ArrayList<CategoryModel>();
			categories.add(directCategory);
			for (final CategoryModel superCategory : directCategory.getAllSupercategories())
			{
				if (!isBlockedCategory(superCategory))
				{
					categories.add(superCategory);
				}
			}
			return categories;
		}
	}

	protected boolean isBlockedCategory(final CategoryModel category)
	{
		return category instanceof ClassificationClassModel && !isIncludeClassificationClasses();
	}

	protected Set<CategoryModel> collectSuperCategories(final CategoryModel category, final Set<CategoryModel> rootCategories,
			final Set<CategoryModel> path)
	{
		if (category == null || isBlockedCategory(category))
		{
			// If this category is blocked or null then return empty set as this whole branch is not viable
			return Collections.emptySet();
		}

		if (path.contains(category))
		{
			// Loop detected, category has already been seen. this whole branch is not viable
			return Collections.emptySet();
		}

		// This category is ok, so add it to our path
		path.add(category);

		if (rootCategories.contains(category))
		{
			// We have found the root, so that is the end of this path
			return path;
		}
		else
		{
			final List<CategoryModel> superCategories = category.getSupercategories();
			if (superCategories == null || superCategories.isEmpty())
			{
				// No super categories, and we haven't found the root yet, so this whole branch is not viable
				return Collections.emptySet();
			}

			if (superCategories.size() == 1)
			{
				// Optimization for 1 super-category we can reuse our 'path' set
				return collectSuperCategories(superCategories.iterator().next(), rootCategories, path);
			}
			else
			{
				final HashSet<CategoryModel> result = new HashSet<CategoryModel>();

				for (final CategoryModel superCategory : superCategories)
				{
					if (!isBlockedCategory(superCategory))
					{
						// Collect the super category branch for each super-category with a copy of the path so far
						// Combine together the results
						result.addAll(collectSuperCategories(superCategory, rootCategories, new HashSet<CategoryModel>(path)));
					}
				}

				return result;
			}
		}
	}

	protected Set<CategoryModel> lookupRootCategories(final Collection<CatalogVersionModel> catalogVersions)
	{
		final String categoryCode = getRootCategory();
		if (categoryCode != null && !categoryCode.isEmpty())
		{
			final Set<CategoryModel> result = new HashSet<CategoryModel>(1);

			for (final CatalogVersionModel catalogVersion : catalogVersions)
			{
				try
				{
					result.add(getCategoryService().getCategoryForCode(catalogVersion, categoryCode));
				}
				catch (final UnknownIdentifierException ignore)
				{
					LOG.warn("Failed to load category [" + categoryCode + "] from catalog version ["
							+ catalogVersionToString(catalogVersion) + "]");
				}
			}

			if (result.isEmpty())
			{
				LOG.error("Failed to find Category with code [" + categoryCode + "] in catalog versions ["
						+ catalogVersionsToString(catalogVersions) + "]");
			}
			else
			{
				return result;
			}
		}

		return null;
	}

	protected String catalogVersionToString(final CatalogVersionModel catalogVersion)
	{
		if (catalogVersion == null)
		{
			return "<null>";
		}
		else if (catalogVersion.getCatalog() == null)
		{
			return "<null>:" + catalogVersion.getVersion();
		}
		return catalogVersion.getCatalog().getId() + ":" + catalogVersion.getVersion();
	}

	protected String catalogVersionsToString(final Collection<CatalogVersionModel> catalogVersions)
	{
		final StringBuilder buf = new StringBuilder();
		for (final CatalogVersionModel catalogVersion : catalogVersions)
		{
			if (buf.length() > 0)
			{
				buf.append(", ");
			}

			buf.append(catalogVersionToString(catalogVersion));
		}
		return buf.toString();
	}
}

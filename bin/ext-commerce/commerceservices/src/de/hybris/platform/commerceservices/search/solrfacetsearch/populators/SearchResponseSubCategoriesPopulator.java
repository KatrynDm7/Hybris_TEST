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

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 */
public class SearchResponseSubCategoriesPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>>
{
	private CommerceCategoryService commerceCategoryService;

	protected CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	@Required
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}


	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> target)
	{
		final String categoryCode = source.getRequest().getSearchQueryData().getCategoryCode();
		target.setSubCategories(buildSubCategories(categoryCode, source.getSearchResult()));
	}


	protected List<CategoryModel> buildSubCategories(final String categoryCode, final SearchResult solrSearchResult)
	{
		if (solrSearchResult != null && solrSearchResult.getTotalNumberOfResults() > 0)
		{
			final Facet categoryPathFacet = solrSearchResult.getFacet("categoryPath");
			if (categoryPathFacet != null && categoryCode != null && !categoryCode.isEmpty())
			{
				final CategoryModel currentCategory = getCommerceCategoryService().getCategoryForCode(categoryCode);
				if (currentCategory != null)
				{
					final Set<String> prefixFilters = getPathsForCategory(currentCategory);
					// Take all category path facets
					// Filter paths by the current selected path
					final Set<String> subCategoryCodes = new HashSet<String>();
					// Extract direct children

					for (final FacetValue facetValue : categoryPathFacet.getFacetValues())
					{
						final String subCategoryPath = extractCategorySubPath(prefixFilters, facetValue.getName());
						if (subCategoryPath != null && !subCategoryPath.isEmpty())
						{
							subCategoryCodes.add(subCategoryPath);
						}
					}

					// Build SubCategories
					final List<CategoryModel> subCategories = new ArrayList<CategoryModel>();
					for (final String subCategoryCode : subCategoryCodes)
					{
						subCategories.add(getCommerceCategoryService().getCategoryForCode(subCategoryCode));
					}

					// Sort the sub-categories by name
					Collections.sort(subCategories, CategoryComparator.INSTANCE);

					return subCategories;
				}
			}
		}
		return null;
	}

	protected Set<String> getPathsForCategory(final CategoryModel category)
	{
		final Set<String> allPaths = new HashSet<String>();

		final Collection<List<CategoryModel>> pathsForCategory = getCommerceCategoryService().getPathsForCategory(category);
		if (pathsForCategory != null)
		{
			for (final List<CategoryModel> categoryPath : pathsForCategory)
			{
				allPaths.add(buildCategoryPath(categoryPath));
			}
		}

		return allPaths;
	}

	protected String buildCategoryPath(final List<CategoryModel> categoryPath)
	{
		final StringBuilder accumulator = new StringBuilder();
		for (final CategoryModel category : categoryPath)
		{
			if (category instanceof ClassificationClassModel)
			{
				return null;
			}
			accumulator.append('/').append(category.getCode());
		}
		return accumulator.toString();
	}

	protected String extractCategorySubPath(final Set<String> prefixFilters, final String path)
	{
		for (final String prefixFilter : prefixFilters)
		{
			if (path.startsWith(prefixFilter + "/"))
			{
				final String subPath = path.substring(prefixFilter.length() + 1);
				final String[] element = subPath.split("/", 2);
				return element[0];
			}
		}
		return null;
	}

	public static class CategoryComparator extends AbstractComparator<CategoryModel>
	{
		public static final CategoryComparator INSTANCE = new CategoryComparator();

		@Override
		protected int compareInstances(final CategoryModel category1, final CategoryModel category2)
		{
			int result = compareValues(category1.getName(), category2.getName(), false);
			if (EQUAL == result)
			{
				result = compareValues(category1.getCode(), category2.getCode(), true);
			}
			return result;
		}
	}
}

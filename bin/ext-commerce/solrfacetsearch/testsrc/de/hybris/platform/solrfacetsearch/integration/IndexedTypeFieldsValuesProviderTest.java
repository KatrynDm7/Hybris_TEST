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
package de.hybris.platform.solrfacetsearch.integration;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.provider.impl.MockupIndexTypeValuesProvider;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class IndexedTypeFieldsValuesProviderTest extends AbstractIntegrationTest
{
	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/IndexedTypeFieldsValuesProviderTest.csv");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testFieldsFromItemValueProvider() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		indexerService.performFullIndex(facetSearchConfig);

		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION);
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnline));

		//here: the mockup provider returns field 'arbitraryField1_string' = 'top' if product is a topseller
		query.searchInField(MockupIndexTypeValuesProvider.name + "_string", "TOP");

		final SearchResult result = facetSearchService.search(query);
		final Collection<ProductModel> products = (Collection<ProductModel>) result.getResults();
		checkProductsCategoryByCode(products, "topseller");
	}

	protected void checkProductsCategoryByCode(final Collection<ProductModel> products, final String categoryCode)
	{
		boolean catFound = false;
		for (final ProductModel product : products)
		{
			final Collection<CategoryModel> categories = product.getSupercategories();
			for (final CategoryModel category : categories)
			{
				if (categoryCode.equals(category.getCode()))
				{
					catFound = true;
					break;
				}
			}
		}
		assertTrue("Category not found", catFound);
	}
}

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperties;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


public class AllAsItemsTest extends AbstractIntegrationTest
{
	private final static String CATALOG_ID = "hwcatalog";
	private final static String VERSION_ONLINE = "Online";

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private FacetSearchService facetSearchService;

	@Resource
	private DefaultIndexerService indexerService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/AllAsItemsTest.csv");
	}

	/**
	 * This Test performs similar actions as FacetDrillDown.testFacetLocalized, however it uses configuration based upon
	 * items - not xml configuration file.
	 */
	@Test
	public void testFacetLocalized() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(getFacetSearchConfig());

		final SearchQuery query = new SearchQuery(getFacetSearchConfig(), indexedType);

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		query.setCatalogVersions(Collections.singletonList(catalogVersion));
		query.setLanguage("de");
		query.clearOrderFields();
		query.addFacetValue("manufacturerName", "Intel");

		final SearchResult result1 = facetSearchService.search(query);
		final Collection<ProductModel> products1 = checkProductCollection(catalogVersion, result1.getResults());
		checkProductsManufacturer(products1, "Intel");

		query.addFacetValue("categoryName", "Motherboards_online_de");
		final SearchResult result2 = facetSearchService.search(query);
		final Collection<ProductModel> products2 = checkProductCollection(catalogVersion, result2.getResults());
		checkProductsCategory(products2, "Motherboards_online_de");
	}

	@Test
	public void testFacetSearchConfigQualifiedRanges() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(getFacetSearchConfig());

		assertNotNull("Config must not be null", facetSearchConfig);
		assertNotNull("IndexedType must not be null", indexedType);
		final IndexedProperty indexedProperty = indexedType.getIndexedProperties().get("priceWithCurrency");
		assertNotNull("Indexed Property must not be null", indexedProperty);
		final List<ValueRange> eurValueRanges = IndexedProperties.getValueRanges(indexedProperty, "EUR");
		assertNotNull("EUR value ranges must not be null", eurValueRanges);
		assertEquals("Number of ranges for EUR", 2, eurValueRanges.size());
		final ValueRange eurValueRange1 = eurValueRanges.get(0);
		assertEquals("Name of EUR range", "1-2000", eurValueRange1.getName());
		assertEquals("Start of EUR range", Double.valueOf(1.0), eurValueRange1.getFrom());
		assertEquals("End of EUR range", Double.valueOf(2000), eurValueRange1.getTo());
		final ValueRange eurValueRange2 = eurValueRanges.get(1);
		assertEquals("Name of EUR range", "2001-INF", eurValueRange2.getName());
		assertEquals("Start of EUR range", Double.valueOf(2001), eurValueRange2.getFrom());
		assertEquals("End of EUR range", null, eurValueRange2.getTo());
		final List<ValueRange> usdValueRanges = IndexedProperties.getValueRanges(indexedProperty, "USD");
		assertNotNull("USD value ranges must not be null", usdValueRanges);
		assertEquals("Number of ranges for USD", 2, usdValueRanges.size());
		final ValueRange usdValueRange1 = usdValueRanges.get(0);
		assertEquals("Name of USD range", "1-3000", usdValueRange1.getName());
		assertEquals("Start of USD range", Double.valueOf(1.0), usdValueRange1.getFrom());
		assertEquals("End of USD range", Double.valueOf(3000.0), usdValueRange1.getTo());
		final ValueRange usdValueRange2 = usdValueRanges.get(1);
		assertEquals("Name of USD range", "3001-INF", usdValueRange2.getName());
		assertEquals("Start of USD range", Double.valueOf(3001), usdValueRange2.getFrom());
		assertEquals("End of USD range", null, usdValueRange2.getTo());

	}

	protected Collection<ProductModel> checkProductCollection(final CatalogVersionModel catalogVersion,
			final Collection<? extends ItemModel> items)
	{
		assertNotNull("Items collection must not be null", items);
		assertFalse("Items collection must not be empty", items.isEmpty());
		for (final ItemModel item : items)
		{
			assertTrue("Result item must be of type " + ProductModel.class, item instanceof ProductModel);
			final ProductModel product = (ProductModel) item;
			assertEquals("Catalog version of product", catalogVersion, product.getCatalogVersion());
		}
		return (Collection<ProductModel>) items;
	}

	protected void checkProductsManufacturer(final Collection<ProductModel> products, final String manufacturerName)
	{
		for (final ProductModel product : products)
		{
			assertEquals(manufacturerName, product.getManufacturerName());
		}
	}

	protected void checkProductsCategory(final Collection<ProductModel> products, final String categoryName)
	{
		for (final ProductModel product : products)
		{
			final Collection<CategoryModel> categories = product.getSupercategories();
			boolean catFound = false;
			for (final CategoryModel category : categories)
			{
				if (categoryName.equals(category.getName()))
				{
					catFound = true;
					break;
				}
			}
			assertTrue("Category not found", catFound);
		}
	}
}

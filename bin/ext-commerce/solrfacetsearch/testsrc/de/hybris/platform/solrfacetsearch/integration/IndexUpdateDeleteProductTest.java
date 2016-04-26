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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexStatisticsProvider;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.Operator;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Test for adding, modifying and deleting products in the catalog. After updating the index the added products should
 * appear in the index and the index should reflect the modifications on the products. When delete query select some
 * products for delete from index, these products shouldn't appear in indexer.
 */
public class IndexUpdateDeleteProductTest extends AbstractIntegrationTest
{
	private final static Logger LOG = Logger.getLogger(IndexUpdateDeleteProductTest.class);

	private static final String EXISTING_TEST_PRODUCT_CODE = "productindexupdatedeleteproducttest";
	private static final String EXISTING_TEST_PRODUCT_NAME = "nametestone";
	private static final String EXISTING_TEST_PRODUCT_DESCRIPTION = "descriptiontestone";
	private static final String NEW_TEST_PRODUCT_CODE = "producttesttwo";
	private static final String NEW_TEST_PRODUCT_NAME = "nametesttwo";
	private static final String NEW_TEST_PRODUCT_DESCRIPTION = "descriptiontesttwo";

	@Resource
	private ProductService productService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Resource
	private SolrIndexStatisticsProvider indexStatisticsProvider;

	@Resource
	private ModelService modelService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/IndexUpdateDeleteProductTest.csv");
	}

	protected void sleep() throws InterruptedException
	{
		Thread.sleep(1500);
	}

	@Test
	public void testCreateProduct() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(facetSearchConfig);
		sleep();

		importConfig("/test/integration/IndexUpdateDeleteProductTest_createProduct.csv");

		final ProductModel testProduct = productService.getProductForCode(hwOnline, NEW_TEST_PRODUCT_CODE);
		assertEquals(testProduct.getCode(), NEW_TEST_PRODUCT_CODE);

		logAndAssertIndexTimeBeforeProductUpdate(NEW_TEST_PRODUCT_CODE, facetSearchConfig, indexedType, testProduct);

		indexerService.updateIndex(facetSearchConfig);
		sleep();

		// waits some time to make sure the test passes
		Thread.sleep(1000);

		logAndAssertIndexTimeAfterProductUpdate(NEW_TEST_PRODUCT_CODE, facetSearchConfig, indexedType, testProduct);

		final SearchResult result = queryIndexForProduct(NEW_TEST_PRODUCT_CODE, NEW_TEST_PRODUCT_NAME, NEW_TEST_PRODUCT_DESCRIPTION,
				facetSearchConfig, hwOnline, indexedType);

		assertEquals("New product " + NEW_TEST_PRODUCT_CODE + " was not transfered to indexer!", 1,
				result.getTotalNumberOfResults());
	}

	@Test
	public void testUpdateProduct() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(facetSearchConfig);
		sleep();

		final ProductModel testProduct = productService.getProductForCode(hwOnline, EXISTING_TEST_PRODUCT_CODE);
		assertNotNull("Failed test data! Product with code " + EXISTING_TEST_PRODUCT_CODE + " does not exist.", testProduct);

		SearchResult result = queryIndexForProduct(EXISTING_TEST_PRODUCT_CODE, EXISTING_TEST_PRODUCT_NAME,
				EXISTING_TEST_PRODUCT_DESCRIPTION, facetSearchConfig, hwOnline, indexedType);

		assertEquals("Test product was not indexed. Missed solr document " + EXISTING_TEST_PRODUCT_CODE, 1,
				result.getTotalNumberOfResults());

		importConfig("/test/integration/IndexUpdateDeleteProductTest_updateProduct.csv");

		modelService.refresh(testProduct);

		logAndAssertIndexTimeBeforeProductUpdate(EXISTING_TEST_PRODUCT_CODE, facetSearchConfig, indexedType, testProduct);

		indexerService.updateIndex(facetSearchConfig);
		sleep();

		logAndAssertIndexTimeAfterProductUpdate(EXISTING_TEST_PRODUCT_CODE, facetSearchConfig, indexedType, testProduct);

		result = queryIndexForProduct(EXISTING_TEST_PRODUCT_CODE, EXISTING_TEST_PRODUCT_NAME, EXISTING_TEST_PRODUCT_DESCRIPTION,
				facetSearchConfig, hwOnline, indexedType);

		assertEquals("Test product is still in old version in the indexer. Product id: " + EXISTING_TEST_PRODUCT_CODE, 0,
				result.getTotalNumberOfResults());

		result = queryIndexForProduct(EXISTING_TEST_PRODUCT_CODE, NEW_TEST_PRODUCT_NAME, NEW_TEST_PRODUCT_DESCRIPTION,
				facetSearchConfig, hwOnline, indexedType);

		assertEquals("Changed test product was not indexed. Missed solr document " + EXISTING_TEST_PRODUCT_CODE, 1,
				result.getTotalNumberOfResults());
	}

	@Test
	public void testDeleteProduct() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(facetSearchConfig);
		sleep();

		SearchResult result = queryIndexForProduct(EXISTING_TEST_PRODUCT_CODE, null, null, facetSearchConfig, hwOnline,
				indexedType);

		assertEquals("Test product was not indexed. Missed solr document " + EXISTING_TEST_PRODUCT_CODE, 1,
				result.getTotalNumberOfResults());

		indexerService.deleteFromIndex(facetSearchConfig);
		sleep();

		result = queryIndexForProduct(EXISTING_TEST_PRODUCT_CODE, null, null, facetSearchConfig, hwOnline, indexedType);

		assertEquals("Product with code= " + EXISTING_TEST_PRODUCT_CODE + "wasn't properly deleted from indexer.", 0,
				result.getTotalNumberOfResults());
	}

	private void logAndAssertIndexTimeBeforeProductUpdate(final String productCode, final FacetSearchConfig facetSearchConfig,
			final IndexedType indexedType, final ProductModel testProduct)
	{
		final Date indexTime = indexStatisticsProvider.getLastIndexTime(facetSearchConfig, indexedType);
		final Date productDate = testProduct.getModifiedtime();
		final DateFormat dateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.FULL);
		LOG.info(productCode + " modify time : " + dateFormat.format(productDate) + " milis : " + productDate.getTime());
		LOG.info("Solr last index time : " + dateFormat.format(indexTime) + " milis : " + indexTime.getTime());
		assertTrue("LastIndexTime is not before new product modification time", indexTime.before(productDate));
	}

	private void logAndAssertIndexTimeAfterProductUpdate(final String productCode, final FacetSearchConfig facetSearchConfig,
			final IndexedType indexedType, final ProductModel testProduct)
	{
		final Date indexTime = indexStatisticsProvider.getLastIndexTime(facetSearchConfig, indexedType);
		final Date productDate = testProduct.getModifiedtime();
		final DateFormat dateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.FULL);
		LOG.info(productCode + " modify time : " + dateFormat.format(productDate) + " milis : " + productDate.getTime());
		LOG.info("Solr last index time : " + dateFormat.format(indexTime) + " milis : " + indexTime.getTime());
		assertTrue("LastIndexTime is not after new product modification time", indexTime.after(productDate));
	}

	private SearchResult queryIndexForProduct(final String productCode, final String productName, final String productDescription,
			final FacetSearchConfig facetSearchConfig, final CatalogVersionModel hwOnline, final IndexedType indexedType)
					throws FacetSearchException
	{
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setDefaultOperator(Operator.AND);

		query.setCatalogVersions(Arrays.asList(hwOnline));
		if (productCode != null)
		{
			query.searchInField("code", productCode);
		}

		if (productName != null)
		{
			query.searchInField("name", productName);
		}

		if (productDescription != null)
		{
			query.searchInField("description", productDescription);
		}

		query.setLanguage("en");
		return facetSearchService.search(query);
	}
}

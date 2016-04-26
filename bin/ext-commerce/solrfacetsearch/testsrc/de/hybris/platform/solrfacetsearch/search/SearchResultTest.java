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
package de.hybris.platform.solrfacetsearch.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


public class SearchResultTest extends AbstractIntegrationTest
{
	@Resource
	private ProductService productService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	/**
	 * This case tests {@link SearchResult}.getResultPKs() method
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetResultPK() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		indexerService.performFullIndex(facetSearchConfig);
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION);
		final PK expectedPK = productService.getProductForCode(hwOnlineCatalogVersion, "HW2300-2356").getPk();
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion));
		query.searchInField("code", "HW2300-2356");
		final SearchResult result = facetSearchService.search(query);
		final List<PK> resultPK = result.getResultPKs();
		assertNotNull("Resulting PK cannot be null", resultPK);
		assertEquals("Resulting PK list should be of size 1", resultPK.size(), 1);
		assertEquals("Resulting Pk not as expected", expectedPK, resultPK.get(0));
	}

	/**
	 * This case tests {@link SearchResult}.getResultCodes() method
	 *
	 * @throws Exception
	 */
	@Test
	public void testGetResultCode() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		indexerService.performFullIndex(facetSearchConfig);
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION);
		final String expectedCode = productService.getProductForCode(hwOnlineCatalogVersion, "HW2300-2356").getCode();
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion));
		query.searchInField("code", "HW2300-2356");
		final SearchResult result = facetSearchService.search(query);
		final List<String> resutCodes = result.getResultCodes();
		assertNotNull("Resulting code cannot be null", resutCodes);
		assertEquals("Resulting code list should be of size 1", resutCodes.size(), 1);
		assertEquals("Resulting code not as expected", expectedCode, resutCodes.get(0));
	}

	@Test
	public void testSearchResultSerializable() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		indexerService.performFullIndex(facetSearchConfig);
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION);
		query.setCatalogVersions(Arrays.asList(hwOnline));

		final SearchResult resultIn = facetSearchService.search(query);

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(resultIn);
		out.close();

		final ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		final SearchResult resultOut = (SearchResult) input.readObject();
		input.close();
		assertNotNull(resultOut);
	}

}

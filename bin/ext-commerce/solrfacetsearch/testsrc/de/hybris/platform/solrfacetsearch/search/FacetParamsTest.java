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

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.common.params.FacetParams;
import org.junit.Assert;
import org.junit.Test;


public class FacetParamsTest extends AbstractIntegrationTest
{
	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Test
	public void testFacetLimitParam() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		indexerService.performFullIndex(facetSearchConfig);
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION);
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnline));
		query.addSolrParams(FacetParams.FACET_LIMIT, "2");
		final Map<String, String[]> params = query.getSolrParams();
		final String[] strParams = params.get(FacetParams.FACET_LIMIT);
		Assert.assertEquals(1, strParams.length);
		Assert.assertEquals("2", strParams[0]);
		final SearchResult result = facetSearchService.search(query);
		for (final Facet facet : result.getFacets())
		{
			Assert.assertTrue("Number of facet values should be less than or equal 2 due to applied limitation", facet
					.getFacetValues().size() <= 2);
		}
	}

	@Test
	public void testFacetMinCountParam() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		indexerService.performFullIndex(facetSearchConfig);
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		final CatalogVersionModel hwOnline = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION);
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.searchInField("manufacturerName", "EIZO");
		query.setCatalogVersions(Arrays.asList(hwOnline));
		query.addSolrParams(FacetParams.FACET_MINCOUNT, "0");
		final Map<String, String[]> params = query.getSolrParams();
		final String[] strParams = params.get(FacetParams.FACET_MINCOUNT);
		Assert.assertEquals(1, strParams.length);
		Assert.assertEquals("0", strParams[0]);
		final SearchResult result = facetSearchService.search(query);
		final Facet facet = result.getFacet("manufacturerName");
		final FacetValue facetValue = facet.getFacetValue("Hewlett-Packard");
		Assert.assertNotNull(facetValue);
		Assert.assertEquals(0, facetValue.getCount());
	}

}

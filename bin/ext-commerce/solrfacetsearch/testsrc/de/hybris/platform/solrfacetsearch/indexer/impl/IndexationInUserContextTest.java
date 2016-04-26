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
package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class IndexationInUserContextTest extends AbstractIntegrationTest
{
	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private ProductService productService;

	@Resource
	private UserService userService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws ImpExException, IOException, FacetConfigServiceException
	{
		importConfig("/test/integration/IndexationInUserContextTest.csv");
	}

	@Test
	public void testQueryNotPermittedAttribute() throws FacetSearchException, IndexerException, FacetConfigServiceException
	{
		final UserModel admin = userService.getAdminUser();
		userService.setCurrentUser(admin);
		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage("en"));

		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		final ProductModel indexedProduct1 = productService.getProductForCode("product1" + getTestId());

		Assert.assertEquals(admin, userService.getCurrentUser());
		query.setCurrency("EUR");
		query.searchInField("priceValue", "1");
		SearchResult searchResult = facetSearchService.search(query);
		org.fest.assertions.Assertions.assertThat(searchResult.getResults()).containsOnly(indexedProduct1);


		query.searchInField("priceValue", "10");
		searchResult = facetSearchService.search(query);
		org.fest.assertions.Assertions.assertThat(searchResult.getResults()).isEmpty();

		Assert.assertEquals(admin, userService.getCurrentUser());
	}
}

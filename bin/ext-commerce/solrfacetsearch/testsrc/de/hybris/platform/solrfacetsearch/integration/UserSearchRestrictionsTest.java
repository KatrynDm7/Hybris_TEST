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

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


public class UserSearchRestrictionsTest extends AbstractIntegrationTest
{
	private static final String APPROVED_CODE = "approvedProduct";
	private static final String UNAPPROVED_CODE = "approvedProduct";

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private EnumerationService enumerationService;

	@Resource
	private TypeService typeService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/UserSearchRestrictionsTest.csv");
	}

	@Test
	public void testIndexingWithoutUserRestriction() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		// when
		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion));

		final SearchResult result = facetSearchService.search(query);
		final List<String> resultCodes = result.getResultCodes();

		// then
		assertEquals(2, resultCodes.size());
		assertThat(resultCodes, hasItems(APPROVED_CODE, UNAPPROVED_CODE));
	}

	@Test
	public void testIndexingWithUserRestriction() throws Exception
	{
		// given
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final CatalogVersionModel hwOnlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());

		// when
		final EnumerationValueModel approved = typeService.getEnumerationValue(enumerationService.getEnumerationValue(
				ArticleApprovalStatus._TYPECODE, ArticleApprovalStatus.APPROVED.getCode()));
		importConfig("/test/integration/UserSearchRestrictionsTest_createRestrictions.csv",
				Collections.singletonMap("approvedPk", approved.getPk().toString()));

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);
		query.setCatalogVersions(Arrays.asList(hwOnlineCatalogVersion));

		final SearchResult result = facetSearchService.search(query);
		final List<String> resultCodes = result.getResultCodes();

		// then
		assertEquals(1, resultCodes.size());
		assertThat(resultCodes, hasItems(APPROVED_CODE));
	}
}

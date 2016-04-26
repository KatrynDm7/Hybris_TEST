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
package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class CachedFacetSearchConfigServiceTest extends AbstractIntegrationTest
{
	@Resource
	private FacetSearchConfigService cachedFacetSearchConfigService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Override
	protected void loadInitialData() throws ImpExException, IOException, FacetConfigServiceException
	{
		importConfig("/test/integration/CachedFacetSearchConfigServiceTest.csv");
	}

	@Test
	public void testGetConfigurationByName() throws Exception
	{
		// when
		final FacetSearchConfig config = cachedFacetSearchConfigService.getConfiguration(this.getFacetSearchConfigName());

		// then
		verifyConfigData(config);
	}

	@Test(expected = FacetConfigServiceException.class)
	public void testGetNotExistingConfigurationByName() throws Exception
	{
		cachedFacetSearchConfigService.getConfiguration("dummy");
	}

	@Test
	public void testGetConfigurationByCatalogVersion() throws Exception
	{
		// given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG, ONLINE_CATALOG_VERSION
				+ getTestId());

		// when
		final FacetSearchConfig config = cachedFacetSearchConfigService.getConfiguration(catalogVersion);

		// then
		verifyConfigData(config);
	}

	@Test
	public void testNoConfigurationForCatalogVersion() throws Exception
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("dummyCatalogVersion",
				ONLINE_CATALOG_VERSION + getTestId());

		//when
		final FacetSearchConfig config = cachedFacetSearchConfigService.getConfiguration(catalogVersion);

		//then
		Assert.assertNull(config);
	}

	protected void verifyConfigData(final FacetSearchConfig config)
	{
		final String indexedTypeName = "Product_testIndexedType" + getTestId();
		final String defaulrPriceRangeSetQualifier = "default";

		Assert.assertEquals(getFacetSearchConfigName(), config.getName());

		final SearchConfig searchConfig = config.getSearchConfig();
		Assert.assertEquals(20, searchConfig.getPageSize());

		final SolrConfig solrConfig = config.getSolrConfig();
		Assert.assertEquals(SolrServerMode.STANDALONE, solrConfig.getMode());

		final IndexConfig indexConfig = config.getIndexConfig();
		Assert.assertEquals(100, indexConfig.getBatchSize());
		Assert.assertEquals(2, indexConfig.getLanguages().size());
		Assert.assertEquals(2, indexConfig.getCurrencies().size());
		Assert.assertEquals(1, indexConfig.getCatalogVersions().size());
		Assert.assertEquals(1, indexConfig.getIndexedTypes().size());
		final IndexedType indexedType = indexConfig.getIndexedTypes().get(indexedTypeName);
		Assert.assertEquals(8, indexedType.getIndexedProperties().size());
		Assert.assertEquals(5, indexedType.getTypeFacets().size());

		final Map<String, IndexedProperty> indexedPropertyMap = indexedType.getIndexedProperties();

		IndexedProperty indexedProperty = indexedPropertyMap.get("code");
		Assert.assertEquals(Boolean.FALSE, indexedProperty.isFacet());
		Assert.assertEquals(Boolean.FALSE, indexedProperty.isLocalized());
		Assert.assertEquals(Boolean.FALSE, indexedProperty.isCurrency());
		Assert.assertEquals(Boolean.FALSE, indexedProperty.isMultiValue());

		indexedProperty = indexedPropertyMap.get("price");
		Assert.assertEquals(Boolean.TRUE, indexedProperty.isFacet());
		Assert.assertEquals(Boolean.FALSE, indexedProperty.isLocalized());
		Assert.assertEquals(Boolean.TRUE, indexedProperty.isCurrency());
		Assert.assertEquals(Boolean.FALSE, indexedProperty.isMultiValue());
		Assert.assertEquals("productPriceValueProvider", indexedProperty.getFieldValueProvider());
		Assert.assertEquals(1, indexedProperty.getValueRangeSets().size());
		final ValueRangeSet valueRangeSet = indexedProperty.getValueRangeSets().get(defaulrPriceRangeSetQualifier);
		Assert.assertEquals(11, valueRangeSet.getValueRanges().size());
	}
}

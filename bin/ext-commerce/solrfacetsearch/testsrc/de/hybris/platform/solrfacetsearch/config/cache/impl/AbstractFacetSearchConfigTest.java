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
package de.hybris.platform.solrfacetsearch.config.cache.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerServiceIntegrationTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;


@Ignore
public class AbstractFacetSearchConfigTest extends ServicelayerTest
{
	protected static final String CATALOG = "hwcatalog";
	protected static final String ONLINE_CATALOG_VERSION = "Online";
	protected static final String FACET_SEARCH_CONFIG_NAME = "testFacetSearchConfig";
	protected static final String LANG_EN = "en";
	protected static final String LANG_DE = "de";


	protected void createBasicData() throws Exception
	{
		createCoreData();
		importCsv("/test/solrBasics.csv", "windows-1252");
		importCsv("/test/solrHwcatalogStaged.csv", "utf-8");
		importCsv("/test/solrHwcatalogOnline.csv", "utf-8");
	}

	protected void importConfig(final String resource, final String encoding, final String testId) throws IOException,
			ImpExException
	{
		final InputStream inputStream = DefaultIndexerServiceIntegrationTest.class.getResourceAsStream(resource);

		final String impexContent = IOUtils.toString(inputStream, encoding);
		final String newImpexContent = impexContent.replaceAll("\\$\\{testId\\}", testId);

		final InputStream newInputStream = IOUtils.toInputStream(newImpexContent, encoding);

		importStream(newInputStream, encoding, resource);
	}

	protected void verifyConfigData(final FacetSearchConfig config, final String testId)
	{
		final String configName = FACET_SEARCH_CONFIG_NAME + testId;
		final String indexedTypeName = "Product_testIndexedType" + testId;
		final String defaulrPriceRangeSetQualifier = "default";

		Assert.assertEquals(configName, config.getName());

		final SearchConfig searchConfig = config.getSearchConfig();
		Assert.assertEquals(20, searchConfig.getPageSize());

		final SolrConfig solrConfig = config.getSolrConfig();
		Assert.assertEquals(SolrServerMode.STANDALONE, solrConfig.getMode());

		final IndexConfig indexConfig = config.getIndexConfig();
		Assert.assertEquals(100, indexConfig.getBatchSize());
		Assert.assertEquals(2, indexConfig.getLanguages().size());
		Assert.assertEquals(2, indexConfig.getCurrencies().size());
		Assert.assertEquals(2, indexConfig.getCatalogVersions().size());
		Assert.assertEquals(1, indexConfig.getIndexedTypes().size());
		final IndexedType indexedType = indexConfig.getIndexedTypes().get(indexedTypeName);
		Assert.assertEquals(9, indexedType.getIndexedProperties().size());
		Assert.assertEquals(6, indexedType.getTypeFacets().size());

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

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

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.test.TestCacheKey;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.impl.FacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultFacetSearchConfigCacheValueLoaderTest
{
	private static final String FACET_SEARCH_CONFIG_NAME = "testFacetSearchConfig";
	private static final String NOT_EXISTING_CONFIG = "notExistingConfig";

	private static final String LANG_EN = "en";
	private static final String TENANT_ID = "junit";

	private DefaultFacetSearchConfigCacheValueLoader facetSearchConfigCacheValueLoader;
	private SolrFacetSearchConfigModel solrFacetSearchConfigModel;
	private FacetSearchConfig facetSearchConfig;
	@Mock
	private FacetSearchConfigDao facetSearchConfigDao;
	@Mock
	private Converter<SolrFacetSearchConfigModel, FacetSearchConfig> facetSearchConfigConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		facetSearchConfigCacheValueLoader = new DefaultFacetSearchConfigCacheValueLoader();
		facetSearchConfigCacheValueLoader.setFacetSearchConfigConverter(facetSearchConfigConverter);
		facetSearchConfigCacheValueLoader.setFacetSearchConfigDao(facetSearchConfigDao);

		facetSearchConfig = new FacetSearchConfig();
		facetSearchConfig.setName(FACET_SEARCH_CONFIG_NAME);
		solrFacetSearchConfigModel = new SolrFacetSearchConfigModel();
		solrFacetSearchConfigModel.setName(FACET_SEARCH_CONFIG_NAME);
	}

	@Test
	public void testLoad()
	{
		//given
		final FacetSearchConfigCacheKey key = new FacetSearchConfigCacheKey(FACET_SEARCH_CONFIG_NAME, LANG_EN, TENANT_ID);
		given(facetSearchConfigDao.findSolrFacetSearchConfigByName(FACET_SEARCH_CONFIG_NAME))
				.willReturn(solrFacetSearchConfigModel);
		given(facetSearchConfigConverter.convert(solrFacetSearchConfigModel)).willReturn(facetSearchConfig);

		//when
		final FacetSearchConfig config = facetSearchConfigCacheValueLoader.load(key);

		//then
		Assert.assertEquals(facetSearchConfig, config);
	}

	@Test(expected = CacheValueLoadException.class)
	public void testLoadNotExistingObject()
	{
		//given
		final FacetSearchConfigCacheKey key = new FacetSearchConfigCacheKey(NOT_EXISTING_CONFIG, LANG_EN, TENANT_ID);
		given(facetSearchConfigDao.findSolrFacetSearchConfigByName(NOT_EXISTING_CONFIG)).willReturn(null);

		//when
		facetSearchConfigCacheValueLoader.load(key);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLoadWithIncorectKeyType()
	{
		//given
		final CacheKey key = new TestCacheKey("test");

		//when
		facetSearchConfigCacheValueLoader.load(key);
	}
}

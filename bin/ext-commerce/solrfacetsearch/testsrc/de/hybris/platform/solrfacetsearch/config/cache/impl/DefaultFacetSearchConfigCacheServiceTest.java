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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.cache.FacetSearchConfigCacheService;
import de.hybris.platform.solrfacetsearch.config.impl.FacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultFacetSearchConfigCacheServiceTest extends AbstractIntegrationTest
{
	@Resource
	private CacheRegion facetSearchConfigCacheRegion;

	@Resource
	private FacetSearchConfigDao facetSearchConfigDao;

	@Resource
	private ModelService modelService;

	@Resource(name = "defaultFacetSearchConfigCacheService")
	private FacetSearchConfigCacheService facetSearchConfigCacheService;

	@Resource
	private CommonI18NService commonI18NService;

	protected static final String FACET_SEARCH_CONFIG_NAME = "testFacetSearchConfig";
	protected static final String LANG_EN = "en";
	protected static final String LANG_DE = "de";

	@Test
	public void testPutOrGetFromCache() throws Exception
	{
		// given
		importConfig("/test/solrConfigBase.csv");

		// when
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());

		// then
		final CacheStatistics statistics = facetSearchConfigCacheRegion.getCacheRegionStatistics();
		Assert.assertEquals(1, statistics.getMissCount());
		Assert.assertEquals(2, statistics.getHitCount());
		Assert.assertEquals(1, statistics.getInstanceCount());
	}

	@Test(expected = CacheValueLoadException.class)
	public void testPutOrGetNotExisting() throws Exception
	{
		facetSearchConfigCacheService.putOrGetFromCache(FACET_SEARCH_CONFIG_NAME);
	}

	@Test
	public void testPutOrGetForDifferentLaguages() throws Exception
	{
		// given
		importConfig("/test/solrConfigBase.csv");
		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage(LANG_EN));

		// when
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage(LANG_DE));
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());

		// then
		final CacheStatistics statistics = facetSearchConfigCacheRegion.getCacheRegionStatistics();
		Assert.assertEquals(2, statistics.getMissCount());
		Assert.assertEquals(3, statistics.getHitCount());
		Assert.assertEquals(2, statistics.getInstanceCount());
	}

	@Test
	public void testInvalidate() throws Exception
	{
		// given
		importConfig("/test/solrConfigBase.csv");

		// when
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.invalidate(getFacetSearchConfigName());

		// then
		final CacheStatistics statistics = facetSearchConfigCacheRegion.getCacheRegionStatistics();
		Assert.assertEquals(1, statistics.getMissCount());
		Assert.assertEquals(1, statistics.getHitCount());
		Assert.assertEquals(1, statistics.getInvalidations());
		Assert.assertEquals(0, statistics.getInstanceCount());
	}

	@Test
	public void testInvalidateForDiffrentLanguages() throws Exception
	{
		// given
		importConfig("/test/solrConfigBase.csv");
		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage(LANG_EN));

		// when
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		commonI18NService.setCurrentLanguage(commonI18NService.getLanguage(LANG_DE));
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.invalidate(getFacetSearchConfigName());

		// then
		final CacheStatistics statistics = facetSearchConfigCacheRegion.getCacheRegionStatistics();
		Assert.assertEquals(2, statistics.getMissCount());
		Assert.assertEquals(2, statistics.getHitCount());
		Assert.assertEquals(2, statistics.getInvalidations());
		Assert.assertEquals(0, statistics.getInstanceCount());
	}

	@Test
	public void testInvalidateNotCached() throws Exception
	{
		// given
		importConfig("/test/solrConfigBase.csv");

		// when
		facetSearchConfigCacheService.invalidate(getFacetSearchConfigName());

		// then
		final CacheStatistics statistics = facetSearchConfigCacheRegion.getCacheRegionStatistics();
		Assert.assertEquals(0, statistics.getInvalidations());
	}

	@Test
	public void testInvalidateNotExisting() throws Exception
	{
		// given
		facetSearchConfigCacheRegion.clearCache();

		// when
		facetSearchConfigCacheService.invalidate(FACET_SEARCH_CONFIG_NAME);

		// then
		final CacheStatistics statistics = facetSearchConfigCacheRegion.getCacheRegionStatistics();
		Assert.assertEquals(0, statistics.getInvalidations());
	}

	@Test
	public void testInvalidationListener() throws Exception
	{
		// given
		importConfig("/test/solrConfigBase.csv");

		// when
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());
		final SolrFacetSearchConfigModel configModel = facetSearchConfigDao
				.findSolrFacetSearchConfigByName(getFacetSearchConfigName());
		configModel.setDescription("new description");
		modelService.save(configModel);
		facetSearchConfigCacheService.putOrGetFromCache(getFacetSearchConfigName());

		// then
		//currently changing model cause that clearCache() method is called so also statistics will be cleared
		final CacheStatistics statistics = facetSearchConfigCacheRegion.getCacheRegionStatistics();
		Assert.assertEquals(1, statistics.getMissCount());
		Assert.assertEquals(0, statistics.getHitCount());
		Assert.assertEquals(0, statistics.getInvalidations());
	}
}

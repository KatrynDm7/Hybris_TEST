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
package de.hybris.platform.acceleratorcms.component.cache.impl;

import de.hybris.platform.acceleratorcms.component.cache.CmsCacheKeyProvider;
import de.hybris.platform.acceleratorcms.component.cache.CmsCacheService;
import de.hybris.platform.acceleratorcms.data.CmsPageRequestContextData;
import de.hybris.platform.acceleratorcms.services.CMSPageContextService;
import de.hybris.platform.acceleratorcms.utils.SpringHelper;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;


public class DefaultCmsCacheService implements CmsCacheService
{
	private static final String CMS_CACHE_ENABLED_KEY = "cms.cache.enabled";
	private static final String CMS_CACHE_USE_DEFAULT_FALLBACK_KEY = "cms.cache.use.default.fallback";

	private Map<String, CmsCacheKeyProvider<? extends AbstractCMSComponentModel>> cacheKeyProviders;
	private ConfigurationService configurationService;
	private CacheController cacheController;

	private final Supplier<Boolean> useCache = Suppliers.memoizeWithExpiration(new Supplier<Boolean>()
	{
		@Override
		public Boolean get()
		{
			return Boolean.valueOf(getConfigurationService().getConfiguration().getBoolean(CMS_CACHE_ENABLED_KEY, false));
		}
	}, 1, TimeUnit.MINUTES);

	@Override
	public CacheKey getKey(final HttpServletRequest request, final AbstractCMSComponentModel component)
	{
		CmsCacheKeyProvider provider = getCacheKeyProviders().get(component.getItemtype());
		if (provider == null && getConfigurationService().getConfiguration().getBoolean(CMS_CACHE_USE_DEFAULT_FALLBACK_KEY, false))
		{
			provider = getCacheKeyProviders().get(AbstractCMSComponentModel._TYPECODE);
		}
		if (provider != null)
		{
			return provider.getKey(request, component);
		}
		else
		{
			return null;
		}
	}

	@Override
	public String get(final CacheKey key)
	{
		return getCacheController().get(key);
	}

	@Override
	public void put(final CacheKey key, final String content)
	{
		getCacheController().getWithLoader(key, new CmsCacheValueLoader(content));
	}

	@Override
	public boolean useCache(final HttpServletRequest request, final AbstractCMSComponentModel component)
	{
		return request != null && component != null && useCacheInternal() && !isPreviewOrLiveEditEnabled(request);
	}

	protected Supplier<Boolean> getUseCache()
	{
		return useCache;
	}

	protected boolean useCacheInternal()
	{
		return Boolean.TRUE.equals(getUseCache().get());
	}

	protected Map<String, CmsCacheKeyProvider<? extends AbstractCMSComponentModel>> getCacheKeyProviders()
	{
		return cacheKeyProviders;
	}

	@Required
	public void setCacheKeyProviders(final Map<String, CmsCacheKeyProvider<? extends AbstractCMSComponentModel>> cacheKeyProviders)
	{
		this.cacheKeyProviders = cacheKeyProviders;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected CacheController getCacheController()
	{
		return cacheController;
	}

	@Required
	public void setCacheController(final CacheController cacheController)
	{
		this.cacheController = cacheController;
	}

	protected boolean isPreviewOrLiveEditEnabled(final HttpServletRequest request)
	{
		final CmsPageRequestContextData cmsPageRequestContextData = SpringHelper.getSpringBean(request, "cmsPageContextService",
				CMSPageContextService.class, true).getCmsPageRequestContextData(request);
		return cmsPageRequestContextData.isLiveEdit() || cmsPageRequestContextData.isPreview();
	}

	static class CmsCacheValueLoader implements CacheValueLoader<String>
	{
		final private String content;

		public CmsCacheValueLoader(final String content)
		{
			this.content = content;
		}

		@Override
		public String load(final CacheKey paramCacheKey) throws CacheValueLoadException
		{
			return content;
		}
	}
}

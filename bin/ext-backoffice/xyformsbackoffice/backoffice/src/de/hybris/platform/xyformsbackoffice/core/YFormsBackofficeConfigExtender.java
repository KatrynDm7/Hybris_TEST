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
 */
package de.hybris.platform.xyformsbackoffice.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.core.config.CockpitConfigurationFallbackStrategy;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;


/**
 * Allows for extending cockpit configuration service functionality by adding additional strategies and adapters. Should
 * be used as a spring bean to add additional properties to existing cockpit configuration service without creating a
 * new cockpit configuration service bean. It adds the specified properties to the injected configuration service after
 * the application context was initialized. It removes these properties before the application context is destroyed.
 */
public class YFormsBackofficeConfigExtender
{
	private DefaultCockpitConfigurationService configurationService;
	private List<WidgetConfigurationContextDecorator> configContextDecoratorList;
	private Map<String, CockpitConfigurationContextStrategy> contextStrategies = Collections
			.<String, CockpitConfigurationContextStrategy> emptyMap();
	private Map<String, List<CockpitConfigurationFallbackStrategy>> fallbackStrategies = Collections
			.<String, List<CockpitConfigurationFallbackStrategy>> emptyMap();
	private Map<Map<String, String>, CockpitConfigurationAdapter<?>> adapters;


	private Map<Map<String, String>, CockpitConfigurationAdapter<?>> adaptersToRemove;
	private Map<String, CockpitConfigurationContextStrategy> originalContextStrategies = Collections
			.<String, CockpitConfigurationContextStrategy> emptyMap();
	private Map<String, List<CockpitConfigurationFallbackStrategy>> originalFallbackStrategies = Collections
			.<String, List<CockpitConfigurationFallbackStrategy>> emptyMap();
	private Map<Map<String, String>, CockpitConfigurationAdapter<?>> originalAdapters;
	private List<WidgetConfigurationContextDecorator> contextDecorators;
	private List<WidgetConfigurationContextDecorator> originalContextDecorators;

	/**
	 * Executed after application context was initialized. Adds specified properties to the cockpit configuration
	 * service.
	 */
	@PostConstruct
	public void addAll()
	{
		if (MapUtils.isNotEmpty(this.contextStrategies))
		{
			this.originalContextStrategies = this.configurationService.getContextStrategies();
			this.configurationService.setContextStrategies(add(this.originalContextStrategies, this.contextStrategies));
		}

		if (MapUtils.isNotEmpty(this.fallbackStrategies))
		{
			this.originalFallbackStrategies = this.configurationService.getFallbackStrategies();
			this.configurationService.setFallbackStrategies(add(this.originalFallbackStrategies, this.fallbackStrategies));
		}

		if (MapUtils.isNotEmpty(this.adaptersToRemove))
		{
			this.configurationService.setAdapters(remove(this.configurationService.getAdapters(), this.adaptersToRemove));
		}


		if (MapUtils.isNotEmpty(this.adapters))
		{
			this.originalAdapters = this.configurationService.getAdapters();
			this.configurationService.setAdapters(add(this.originalAdapters, this.adapters));
		}


		if (this.configContextDecoratorList != null && CollectionUtils.isNotEmpty(this.contextDecorators))
		{
			this.originalContextDecorators = new ArrayList<WidgetConfigurationContextDecorator>(this.configContextDecoratorList);
			add(this.configContextDecoratorList, this.contextDecorators);
		}
	}

	private <K, V> Map<K, V> add(final Map<K, V> original, final Map<K, V> toAdd)
	{
		final Map<K, V> result = new HashMap<K, V>();
		if (MapUtils.isNotEmpty(original))
		{
			result.putAll(original);
		}
		result.putAll(toAdd);
		return result;
	}

	private <E> void add(final List<E> original, final List<E> toAdd)
	{
		for (final E element : toAdd)
		{
			if (!original.contains(element))
			{
				original.add(element);
			}
		}
	}

	/**
	 * Executed before application context is destroyed. Removes specified properties from the cockpit configuration
	 * service.
	 */
	@PreDestroy
	public void removeAll()
	{
		if (MapUtils.isNotEmpty(this.contextStrategies))
		{
			this.configurationService.setContextStrategies(remove(this.configurationService.getContextStrategies(),
					this.contextStrategies, this.originalContextStrategies));
		}

		if (MapUtils.isNotEmpty(this.fallbackStrategies))
		{
			this.configurationService.setFallbackStrategies(remove(this.configurationService.getFallbackStrategies(),
					this.fallbackStrategies, this.originalFallbackStrategies));
		}

		if (MapUtils.isNotEmpty(this.adapters))
		{
			this.configurationService.setAdapters(remove(this.configurationService.getAdapters(), this.adapters,
					this.originalAdapters));
		}

		if (this.configContextDecoratorList != null && CollectionUtils.isNotEmpty(this.contextDecorators))
		{
			remove(this.configContextDecoratorList, this.contextDecorators, this.originalContextDecorators);
		}

	}

	private <K, V> Map<K, V> remove(final Map<K, V> original, final Map<K, V> toRemove)
	{

		final Map<K, V> result = new HashMap<K, V>();
		if (MapUtils.isNotEmpty(original))
		{
			result.putAll(original);
		}
		for (final Map.Entry<K, V> entry : toRemove.entrySet())
		{
			if (MapUtils.isNotEmpty(original) && original.containsKey(entry.getKey()))
			{
				result.remove(entry.getKey());
			}
		}
		return result;


	}

	private <K, V> Map<K, V> remove(final Map<K, V> original, final Map<K, V> toRemove, final Map<K, V> saved)
	{
		final Map<K, V> result = new HashMap<K, V>();
		if (MapUtils.isNotEmpty(original))
		{
			result.putAll(original);
		}
		for (final Map.Entry<K, V> entry : toRemove.entrySet())
		{
			if (MapUtils.isNotEmpty(saved) && saved.containsKey(entry.getKey()))
			{
				result.put(entry.getKey(), saved.get(entry.getKey()));
			}
			else
			{
				result.remove(entry.getKey());
			}
		}
		return result;
	}

	private <E> void remove(final List<E> original, final List<E> toRemove, final List<E> saved)
	{
		for (final E element : toRemove)
		{
			if (!saved.contains(element))
			{
				original.remove(element);
			}
		}
	}

	/**
	 * Context strategies to be added.
	 * 
	 * @return context strategies to be added
	 */
	public Map<String, CockpitConfigurationContextStrategy> getContextStrategies()
	{
		return contextStrategies;
	}

	/**
	 * Sets context strategies to be added.
	 * 
	 * @param contextStrategies
	 *           context strategies to be added
	 */
	public void setContextStrategies(final Map<String, CockpitConfigurationContextStrategy> contextStrategies)
	{
		this.contextStrategies = contextStrategies;
	}

	/**
	 * Fallback strategies to be added.
	 * 
	 * @return fallback strategies to be added
	 */
	public Map<String, List<CockpitConfigurationFallbackStrategy>> getFallbackStrategies()
	{
		return fallbackStrategies;
	}

	/**
	 * Sets fallback strategies to be added.
	 * 
	 * @param fallbackStrategies
	 *           fallback strategies to be added
	 */
	public void setFallbackStrategies(final Map<String, List<CockpitConfigurationFallbackStrategy>> fallbackStrategies)
	{
		this.fallbackStrategies = fallbackStrategies;
	}

	/**
	 * Adapters to be added.
	 * 
	 * @return adapters to be added
	 */
	public Map<Map<String, String>, CockpitConfigurationAdapter<?>> getAdapters()
	{
		return adapters;
	}

	/**
	 * Sets adapters to be added.
	 * 
	 * @param adapters
	 *           adapters to be added
	 */
	public void setAdapters(final Map<Map<String, String>, CockpitConfigurationAdapter<?>> adapters)
	{
		this.adapters = adapters;
	}

	/**
	 * Context decorators to be added.
	 * 
	 * @return contextDecorators to be added
	 */
	public List<WidgetConfigurationContextDecorator> getContextDecorators()
	{
		return contextDecorators;
	}

	/**
	 * Set context decorators to be added.
	 * 
	 * @param contextDecorators
	 *           context decorators to be added
	 */
	public void setContextDecorators(final List<WidgetConfigurationContextDecorator> contextDecorators)
	{
		this.contextDecorators = contextDecorators;
	}

	/**
	 * Configuration service to add additional properties to.
	 * 
	 * @return configuration service to extend
	 */
	public DefaultCockpitConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * Sets configuration service to add additional properties to.
	 * 
	 * @param configurationService
	 *           configuration service to extend
	 */
	@Required
	public void setConfigurationService(final DefaultCockpitConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * Sets context decorator list to extend.
	 * 
	 * @param configContextDecoratorList
	 *           context decorator list to be extended
	 */
	public void setWidgetConfigurationContextDecoratorList(
			final List<WidgetConfigurationContextDecorator> configContextDecoratorList)
	{
		this.configContextDecoratorList = configContextDecoratorList;
	}

	public void setAdaptersToRemove(final Map<Map<String, String>, CockpitConfigurationAdapter<?>> adaptersToRemove)
	{
		this.adaptersToRemove = adaptersToRemove;
	}
}

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
package de.hybris.platform.commerceservices.spring.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;


public class MapMergeDirectiveBeanPostProcessor implements InitializingBean, ApplicationContextAware, BeanFactoryAware,
		BeanPostProcessor
{

	private static final Logger LOG = Logger.getLogger(MapMergeDirectiveBeanPostProcessor.class.getName());

	private ConfigurableListableBeanFactory beanFactory;
	private ApplicationContext applicationContext;

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String arg1) throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String arg1) throws BeansException
	{
		return bean;
	}

	protected ConfigurableListableBeanFactory getBeanFactory()
	{
		return beanFactory;
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		if (!(beanFactory instanceof ConfigurableListableBeanFactory))
		{
			throw new IllegalStateException(
					"ListMergeDirectiveBeanPostProcessor doesn't work with a BeanFactory which does not implement ConfigurableListableBeanFactory: "
							+ beanFactory.getClass());
		}
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	protected ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		final Map<String, MapMergeDirective> beans = getApplicationContext().getBeansOfType(MapMergeDirective.class);
		final Stopwatch stopWatch = Stopwatch.createUnstarted(Ticker.systemTicker());
		stopWatch.start();
		try
		{
			processAll(beans);
		}
		finally
		{
			stopWatch.stop();
			LOG.info("[" + this.applicationContext.getDisplayName() + "] " + this + " Finished Processing [" + beans.size()
					+ "] directives in " + stopWatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
		}


	}

	protected void processAll(final Map<String, MapMergeDirective> beans)
	{
		// pass 1 - add
		process(beans, new MapMergeDirectiveProcessor()
		{
			@Override
			public String getName()
			{
				return "Add";
			}

			@Override
			public void processDirective(final MapMergeDirective directive, final Map<Object, Object> mapBean,
					final String directiveBeanName, final String mapBeanName)
			{
				if (directive.getKey() != null && mapBean.containsKey(directive.getKey()))
				{
					final Object value = mapBean.get(directive.getKey());
					if (value instanceof Collection)
					{
						if (directive.getValue() instanceof Collection)
						{
							((Collection) value).addAll((Collection) directive.getValue());
						}
						else
						{
							((Collection) value).add(directive.getValue());
						}
					}
					else
					{
						mapBean.put(directive.getKey(), directive.getValue());
					}
				}
				else
				{
					if (directive.getSourceMap() != null)
					{
						mapBean.putAll(directive.getSourceMap());
					}
					else
					{
						mapBean.put(directive.getKey(), directive.getValue());
					}

				}
			}
		});
	}

	protected static interface MapMergeDirectiveProcessor
	{
		String getName();

		void processDirective(MapMergeDirective directive, final Map<Object, Object> mapBean, final String directiveBeanName,
				final String mapBeanName);
	}

	protected void process(final Map<String, MapMergeDirective> beans, final MapMergeDirectiveProcessor processor)
	{
		for (final Entry<String, MapMergeDirective> entry : beans.entrySet())
		{
			final String directiveBeanName = entry.getKey();
			final MapMergeDirective mmd = entry.getValue();
			final String[] dependentBeans = getBeanFactory().getBeanDefinition(directiveBeanName).getDependsOn();
			if (dependentBeans == null)
			{
				LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], missing depends-on");
				continue;
			}
			for (final String dependency : dependentBeans)
			{
				if (LOG.isInfoEnabled())
				{
					LOG.info(processor.getName() + " Post Processing MapMergeDirective [" + directiveBeanName + "] on Bean ["
							+ dependency + "]");
				}
				final Map<Object, Object> mapBean = resolveMapBean(mmd, directiveBeanName, dependency);
				if (mapBean != null)
				{
					processor.processDirective(mmd, mapBean, directiveBeanName, dependency);
				}
			}
		}
	}

	protected Map<Object, Object> resolveMapBean(final MapMergeDirective mmd, final String directiveBeanName,
			final String dependency)
	{
		if (mmd.getMapPropertyDescriptor() == null && mmd.getFieldName() == null)
		{
			try
			{
				return getApplicationContext().getBean(dependency, Map.class);
			}
			catch (final BeanNotOfRequiredTypeException e)
			{
				LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency
						+ "] is not of type java.util.Map");
				return null;
			}

		}
		else
		{
			final Object bean = getApplicationContext().getBean(dependency);

			if (mmd.getMapPropertyDescriptor() != null)
			{
				return getMapByPropertyDescriptor(bean, mmd.getMapPropertyDescriptor(), directiveBeanName, dependency);
			}
			else
			{
				return getMapByReflection(bean, mmd.getFieldName(), directiveBeanName, dependency);
			}
		}
	}

	protected Map<Object, Object> getMapByPropertyDescriptor(final Object bean, final String propertyDescriptor,
			final String directiveBeanName, final String dependency)
	{
		try
		{
			final Object map = PropertyUtils.getProperty(bean, propertyDescriptor);
			if (map == null)
			{
				LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency
						+ "] propertyDescriptor [" + propertyDescriptor + "] map is null");
				return null;
			}
			else if (!(map instanceof Map))
			{
				LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency
						+ "] propertyDescriptor [" + propertyDescriptor + "] is not of type java.util.Map");
				return null;
			}
			return (Map<Object, Object>) map;
		}
		catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
		{
			LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] propertyDescriptor ["
					+ propertyDescriptor + "] did not resolve a property");
			return null;
		}
	}

	protected Map<Object, Object> getMapByReflection(final Object bean, final String fieldName, final String directiveBeanName,
			final String dependency)
	{
		try
		{
			final Field field = ReflectionUtils.findField(bean.getClass(), fieldName);
			if (field == null)
			{
				LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
						+ fieldName + "] did not resolve a field");
				return null;
			}
			field.setAccessible(true);
			final Object map = field.get(bean);
			if (map == null)
			{
				LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
						+ fieldName + "] map is null");
				return null;
			}
			else if (!(map instanceof Map))
			{
				LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
						+ fieldName + "] is not of type java.util.Map");
				return null;
			}
			return (Map<Object, Object>) map;
		}
		catch (SecurityException | IllegalAccessException e)
		{
			LOG.error("invalid MapMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
					+ fieldName + "] did not resolve a field");
			return null;
		}

	}

}

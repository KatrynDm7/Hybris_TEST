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
import java.util.List;
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
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;



/**
 * Processes all List Merge Directives in the visible application context. This bean is a bean post processor to ensure
 * it is initialised by the container prior to bean
 * 
 */
public class ListMergeDirectiveBeanPostProcessor implements InitializingBean, ApplicationContextAware, BeanFactoryAware,
		BeanPostProcessor
{
	private static final Logger LOG = Logger.getLogger(ListMergeDirectiveBeanPostProcessor.class.getName());

	private ConfigurableListableBeanFactory beanFactory;
	private ApplicationContext applicationContext;

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

	protected void processAll(final Map<String, ListMergeDirective> beans)
	{
		// pass 1 - add
		process(beans, new ListMergeDirectiveProcessor()
		{
			@Override
			public String getName()
			{
				return "Add";
			}

			@Override
			public void processDirective(final ListMergeDirective directive, final List<Object> listBean,
					final String directiveBeanName, final String listBeanName)
			{
				listBean.add(directive.getAdd());
			}
		});

		// pass 2 - sort 
		process(beans, new ListMergeDirectiveProcessor()
		{
			@Override
			public String getName()
			{
				return "Sort";
			}

			protected boolean requiresSorting(final ListMergeDirective directive)
			{
				return hasAfterDirectives(directive) || hasBeforeDirectives(directive);
			}

			protected boolean hasAfterDirectives(final ListMergeDirective directive)
			{
				return directive.getAfterBeanNames() != null || directive.getAfterClasses() != null
						|| directive.getAfterValues() != null;
			}

			protected boolean hasBeforeDirectives(final ListMergeDirective directive)
			{
				return directive.getBeforeBeanNames() != null || directive.getBeforeClasses() != null
						|| directive.getBeforeValues() != null;
			}

			protected void processAfterDirectives(final ListMergeDirective directive, final List<Object> listBean,
					final String directiveBeanName, final String listBeanName, final int index)
			{
				if (hasAfterDirectives(directive))
				{
					int highestIndex = -1;

					if (directive.getAfterBeanNames() != null)
					{
						for (final String beanName : directive.getAfterBeanNames())
						{
							try
							{
								final Object bean = getApplicationContext().getBean(beanName);
								final int listBeanIndex = listBean.indexOf(bean);
								if (listBeanIndex > highestIndex)
								{
									highestIndex = listBeanIndex;

								}
							}
							catch (final NoSuchBeanDefinitionException e)
							{
								LOG.warn("list merge directive[" + directiveBeanName + "] afterBeanName [" + beanName
										+ "] does not exist");
							}
						}
					}
					if (directive.getAfterClasses() != null)
					{
						for (int i = (listBean.size() - 1); i >= 0 || i >= index; i--)
						{
							boolean done = false;
							for (final Class clazz : directive.getAfterClasses())
							{
								final Object element = listBean.get(i);

								if (element != null && clazz.isAssignableFrom(element.getClass()))
								{
									highestIndex = i;
									done = true;
									break;
								}
							}


							if (done)
							{
								break;
							}
						}
					}
					if (directive.getAfterValues() != null)
					{
						for (final Object value : directive.getAfterValues())
						{
							final int listBeanIndex = listBean.indexOf(value);
							if (listBeanIndex > highestIndex)
							{
								highestIndex = listBeanIndex;

							}
						}
					}

					if (highestIndex > index)
					{
						final int newIndex = highestIndex + 1;
						if ((newIndex) >= listBean.size())
						{
							listBean.add(directive.getAdd());

						}
						else
						{
							listBean.add(newIndex, directive.getAdd());
						}
						listBean.remove(index);
					}
				}
			}

			protected void processBeforeDirectives(final ListMergeDirective directive, final List<Object> listBean,
					final String directiveBeanName, final String listBeanName, final int index)
			{
				if (hasBeforeDirectives(directive))
				{
					int lowestIndex = -1;

					if (directive.getBeforeBeanNames() != null)
					{
						for (final String beanName : directive.getBeforeBeanNames())
						{
							try
							{
								final Object bean = getApplicationContext().getBean(beanName);
								final int listBeanIndex = listBean.indexOf(bean);
								if (listBeanIndex >= 0)
								{
									if (lowestIndex == -1 || listBeanIndex < lowestIndex)
									{
										lowestIndex = listBeanIndex;
									}
								}
							}
							catch (final NoSuchBeanDefinitionException e)
							{
								LOG.warn("list merge directive[" + directiveBeanName + "] beforeBeanName [" + beanName
										+ "] does not exist");
							}
						}
					}

					if (directive.getBeforeClasses() != null)
					{
						for (int i = 0; i < listBean.size() || index < i || (lowestIndex >= 0 && i > lowestIndex); i++)
						{
							boolean done = false;
							for (final Class clazz : directive.getBeforeClasses())
							{
								final Object element = listBean.get(i);

								if (element != null && clazz.isAssignableFrom(element.getClass()))
								{
									lowestIndex = i;
									done = true;
									break;
								}
							}


							if (done)
							{
								break;
							}
						}
					}

					if (directive.getBeforeValues() != null)
					{
						for (final Object value : directive.getBeforeValues())
						{

							final int listBeanIndex = listBean.indexOf(value);
							if (listBeanIndex >= 0)
							{
								if (lowestIndex == -1 || listBeanIndex < lowestIndex)
								{
									lowestIndex = listBeanIndex;
								}
							}
						}
					}

					if (lowestIndex < index)
					{
						final int newIndex = lowestIndex;
						Preconditions.checkArgument(listBean.remove(index) == directive.getAdd(), "Software Error Directive ["
								+ directiveBeanName + "]. Tried to remove wrong bean ");
						if (newIndex < 0)
						{
							listBean.add(0, directive.getAdd());
						}
						else
						{
							listBean.add(newIndex, directive.getAdd());
						}
					}
				}

			}


			@Override
			public void processDirective(final ListMergeDirective directive, final List<Object> listBean,
					final String directiveBeanName, final String listBeanName)
			{

				if (requiresSorting(directive))
				{
					final int index = listBean.indexOf(directive.getAdd());
					if (index != -1)
					{
						processAfterDirectives(directive, listBean, directiveBeanName, listBeanName, index);
						processBeforeDirectives(directive, listBean, directiveBeanName, listBeanName, index);
					}
				}

			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{

		final Map<String, ListMergeDirective> beans = getApplicationContext().getBeansOfType(ListMergeDirective.class);
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

	protected static interface ListMergeDirectiveProcessor
	{
		String getName();

		void processDirective(ListMergeDirective directive, final List<Object> listBean, final String directiveBeanName,
				final String listBeanName);
	}

	protected void process(final Map<String, ListMergeDirective> beans, final ListMergeDirectiveProcessor processor)
	{

		for (final Entry<String, ListMergeDirective> entry : beans.entrySet())
		{
			final String directiveBeanName = entry.getKey();
			final ListMergeDirective lmd = entry.getValue();
			final String[] dependentBeans = getBeanFactory().getBeanDefinition(directiveBeanName).getDependsOn();
			if (dependentBeans == null)
			{
				LOG.error("invalid list merge directive [" + directiveBeanName + "], missing depends-on");
				continue;
			}
			for (final String dependency : dependentBeans)
			{
				if (LOG.isInfoEnabled())
				{
					LOG.info(processor.getName() + " Post Processing ListMergeDirective [" + directiveBeanName + "] on Bean ["
							+ dependency + "]");
				}
				final List<Object> listBean = resolveListBean(lmd, directiveBeanName, dependency);
				if (listBean != null)
				{
					processor.processDirective(lmd, listBean, directiveBeanName, dependency);
				}
			}
		}

	}

	protected List<Object> resolveListBean(final ListMergeDirective lmd, final String directiveBeanName, final String dependency)
	{
		if (lmd.getListPropertyDescriptor() == null && lmd.getFieldName() == null)
		{
			try
			{
				return getApplicationContext().getBean(dependency, List.class);
			}
			catch (final BeanNotOfRequiredTypeException e)
			{
				LOG.error("invalid list merge directive[" + directiveBeanName + "], depends-on [" + dependency
						+ "] is not of type java.util.List");
				return null;
			}

		}
		else
		{
			final Object bean = getApplicationContext().getBean(dependency);

			if (lmd.getListPropertyDescriptor() != null)
			{
				return getListByPropertyDescriptor(bean, lmd.getListPropertyDescriptor(), directiveBeanName, dependency);
			}
			else
			{
				return getListByReflection(bean, lmd.getFieldName(), directiveBeanName, dependency);
			}
		}
	}

	protected List<Object> getListByPropertyDescriptor(final Object bean, final String propertyDescriptor,
			final String directiveBeanName, final String dependency)
	{
		try
		{
			final Object list = PropertyUtils.getProperty(bean, propertyDescriptor);
			if (list == null)
			{
				LOG.error("invalid ListMergeDirective [" + directiveBeanName + "], depends-on [" + dependency
						+ "] propertyDescriptor [" + propertyDescriptor + "] list is null");
				return null;
			}
			else if (!(list instanceof List))
			{
				LOG.error("invalid ListMergeDirective [" + directiveBeanName + "], depends-on [" + dependency
						+ "] propertyDescriptor [" + propertyDescriptor + "] is not of type java.util.List");
				return null;
			}
			return (List<Object>) list;

		}
		catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
		{
			LOG.error("invalid ListMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] propertyDescriptor ["
					+ propertyDescriptor + "] did not resolve a property");
			return null;
		}
	}

	protected List<Object> getListByReflection(final Object bean, final String fieldName, final String directiveBeanName,
			final String dependency)
	{
		try
		{
			final Field field = ReflectionUtils.findField(bean.getClass(), fieldName);
			if (field == null)
			{
				LOG.error("invalid ListMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
						+ fieldName + "] did not resolve a field");
				return null;
			}
			field.setAccessible(true);
			final Object list = field.get(bean);
			if (list == null)
			{
				LOG.error("invalid ListMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
						+ fieldName + "] list is null");
				return null;
			}
			else if (!(list instanceof List))
			{
				LOG.error("invalid ListMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
						+ fieldName + "] is not of type java.util.List");
				return null;
			}
			return (List<Object>) list;
		}
		catch (SecurityException | IllegalAccessException e)
		{
			LOG.error("invalid ListMergeDirective [" + directiveBeanName + "], depends-on [" + dependency + "] fieldName ["
					+ fieldName + "] did not resolve a field");
			return null;
		}

	}


	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException
	{
		return bean;
	}

}

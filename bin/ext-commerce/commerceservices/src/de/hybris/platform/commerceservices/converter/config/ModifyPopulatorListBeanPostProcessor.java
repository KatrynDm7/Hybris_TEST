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
package de.hybris.platform.commerceservices.converter.config;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.PopulatorList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * BeanPostProcessor that looks for beans of type ModifyPopulatorList and applies the modifications to the target list.
 */
public class ModifyPopulatorListBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware, BeanFactoryAware
{
	private static final Logger LOG = Logger.getLogger(ModifyPopulatorListBeanPostProcessor.class);

	protected static final String LIST_PROPERTY = "list";
	protected static final String ADD_PROPERTY = "add";
	protected static final String REMOVE_PROPERTY = "remove";

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
					"ModifyPopulatorListBeanPostProcessor doesn't work with a BeanFactory which does not implement ConfigurableListableBeanFactory: "
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
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException
	{
		if (bean instanceof PopulatorList)
		{
			addPopulators((PopulatorList) bean, beanName);
		}
		return bean;
	}

	protected void addPopulators(final PopulatorList populatorList, final String populatorListBeanName)
	{
		// Get all the ModifyPopulatorList bean names
		final String[] modifyPopulatorListBeanNames = getApplicationContext().getBeanNamesForType(ModifyPopulatorList.class);
		if (modifyPopulatorListBeanNames != null && modifyPopulatorListBeanNames.length > 0)
		{
			// Get a modifiable list of the existing populators in the list
			final List<Populator> populators = getModifiablePopulatorProperty(populatorList);

			//
			modifyPopulatorsList(populatorListBeanName, populators, modifyPopulatorListBeanNames);

			// Finally store the populators back onto the list
			populatorList.setPopulators(populators);
		}
	}

	protected void modifyPopulatorsList(final String populatorListBeanName, final List<Populator> populators,
			final String[] modifyPopulatorListBeanNames)
	{
		// Have a look and see if there is a parent bean defined, process the parent's modifications first
		final BeanDefinition populatorListBeanDefinition = getBeanFactory().getBeanDefinition(populatorListBeanName);
		final String parentPopulatorListBeanName = populatorListBeanDefinition.getParentName();
		if (parentPopulatorListBeanName != null)
		{
			// Apply modifications for the parent bean first
			modifyPopulatorsList(parentPopulatorListBeanName, populators, modifyPopulatorListBeanNames);
		}

		// Get the aliases for the list
		final String[] populatorListBeanNameAndAliases = getBeanNameAndAliases(populatorListBeanName);

		// Check each modification bean
		for (final String modifyPopulatorListBeanName : modifyPopulatorListBeanNames)
		{
			final BeanDefinition modifyPopulatorListBeanDefinition = getBeanFactory().getMergedBeanDefinition(
					modifyPopulatorListBeanName);
			if (!modifyPopulatorListBeanDefinition.isAbstract())
			{
				final String targetListRefName = getBeanPropertyRefName(modifyPopulatorListBeanDefinition, LIST_PROPERTY);

				// Check if the populator applies to the current list
				if (targetListRefName != null
						&& populatorAppliesTo(targetListRefName, populatorListBeanName, populatorListBeanNameAndAliases))
				{
					removeBeanFromList(populators, targetListRefName, modifyPopulatorListBeanDefinition);
					addBeanToList(populators, targetListRefName, modifyPopulatorListBeanDefinition);
				}
			}
		}
	}

	protected void addBeanToList(final List<Populator> populators, final String targetListRefName,
			final BeanDefinition modifyPopulatorListBeanDefinition)
	{
		// Lookup the add property
		final String addRefName = getBeanPropertyRefName(modifyPopulatorListBeanDefinition, ADD_PROPERTY);
		if (addRefName != null && !addRefName.isEmpty())
		{
			try
			{
				final Populator populatorToAdd = applicationContext.getBean(addRefName, Populator.class);
				if (populatorToAdd != null)
				{
					LOG.debug("Adding populator [" + addRefName + "] to list [" + targetListRefName + "]");
					populators.add(populatorToAdd);
				}
			}
			catch (final Exception ex)
			{
				LOG.error("Failed to lookup bean with name [" + addRefName + "] trying to add it to populator list ["
						+ targetListRefName + "]", ex);
			}
		}
	}

	protected void removeBeanFromList(final List<Populator> populators, final String targetListRefName,
			final BeanDefinition modifyPopulatorListBeanDefinition)
	{
		// Lookup the remove property
		final String removeRefName = getBeanPropertyRefName(modifyPopulatorListBeanDefinition, REMOVE_PROPERTY);
		if (removeRefName != null && !removeRefName.isEmpty())
		{
			try
			{
				final Populator populatorToRemove = applicationContext.getBean(removeRefName, Populator.class);
				if (populatorToRemove != null)
				{
					LOG.debug("Removing populator [" + removeRefName + "] from list [" + targetListRefName + "]");
					populators.remove(populatorToRemove);
				}
			}
			catch (final Exception ex)
			{
				LOG.error("Failed to lookup bean with name [" + removeRefName + "] trying to remove it from populator list ["
						+ targetListRefName + "]", ex);
			}
		}
	}

	protected String getBeanPropertyRefName(final BeanDefinition beanDefinition, final String propertyName)
	{
		final PropertyValue propertyValue = beanDefinition.getPropertyValues().getPropertyValue(propertyName);
		if (propertyValue != null)
		{
			final Object value = propertyValue.getValue();
			if (value instanceof BeanReference)
			{
				return ((BeanReference) value).getBeanName();
			}
		}
		return null;
	}

	protected boolean populatorAppliesTo(final String targetListRefName, final String populatorListBeanName,
			final String[] populatorListBeanNameAndAliases)
	{
		for (final String converterBeanNameOrAlias : populatorListBeanNameAndAliases)
		{
			if (targetListRefName.equals(converterBeanNameOrAlias))
			{
				LOG.debug("Found populator list [" + populatorListBeanName + "]. Converter alias [" + converterBeanNameOrAlias + "].");
				return true;
			}
		}
		return false;
	}

	protected String[] getBeanNameAndAliases(final String beanName)
	{
		final String[] aliases = getApplicationContext().getAliases(beanName);
		if (Arrays.binarySearch(aliases, beanName) >= 0)
		{
			// Contains the bean name
			return aliases;
		}
		else
		{
			final String[] result = new String[aliases.length + 1];
			result[0] = beanName;
			System.arraycopy(aliases, 0, result, 1, aliases.length);
			return result;
		}
	}

	protected List<Populator> getModifiablePopulatorProperty(final PopulatorList populatorList)
	{
		final List<Populator> existingPopulators = populatorList.getPopulators();
		if (existingPopulators != null && !existingPopulators.isEmpty())
		{
			return new ArrayList<Populator>(existingPopulators);
		}
		return new ArrayList<Populator>();
	}
}

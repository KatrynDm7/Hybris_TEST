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
package de.hybris.platform.commerceservices.impex.impl;

import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.impex.jalo.imp.ImportProcessor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.util.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

/**
 * Impex ImportProcessor that injects all config properties as impex definitions.
 * All defined configuration properties are added as impex macro definitions with
 * the prefix of "config-". For example the config key <tt>mail.smtp.server</tt>
 * can be accessed via the macro <tt>$config-mail.smtp.server</tt>.
 * In order to use this import processor and to load the configuration properties
 * the following must be added to the top of the impex file:
 *
 * <tt>UPDATE GenericItem[processor=de.hybris.platform.commerceservices.impex.impl.ConfigPropertyImportProcessor];pk[unique=true]</tt>
 */
public class ConfigPropertyImportProcessor implements ImportProcessor
{
	private static final Logger LOG = Logger.getLogger(ConfigPropertyImportProcessor.class);

	@Override
	public void init(final ImpExImportReader reader)
	{
		final Method addDefinitionMethod = findAddDefinitionMethod();

		Config.getAllParameters();
		for (final Map.Entry<String, String> paramEntry : Config.getAllParameters().entrySet())
		{
			addDefinition(reader, addDefinitionMethod, "config-" + paramEntry.getKey(), paramEntry.getValue());
		}
	}

	@Override
	public Item processItemData(final ValueLine valueLine) throws ImpExException
	{
		throw new IllegalStateException("ConfigPropertyImportProcessor cannot be used to process any lines");
	}

	protected Method findAddDefinitionMethod()
	{
		final Method addDefinitionMethod = BeanUtils.findMethod(ImpExReader.class, "addDefinition", String.class);
		addDefinitionMethod.setAccessible(true);
		return addDefinitionMethod;
	}

	protected void addDefinition(final ImpExReader reader, final Method addDefinitionMethod, final String key, final String value)
	{
		final String definition = ImpExConstants.Syntax.DEFINITION_PREFIX + key + ImpExConstants.Syntax.MODIFIER_EQUAL + value;

		try
		{
			addDefinitionMethod.invoke(reader, definition);
		}
		catch (final IllegalAccessException e)
		{
			LOG.error("Failed to call ImpExReader.addDefinition", e);
		}
		catch (final InvocationTargetException e)
		{
			LOG.error("Failed to call ImpExReader.addDefinition", e);
		}
	}
}

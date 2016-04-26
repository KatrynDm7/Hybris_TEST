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
package de.hybris.platform.commercefacades.converter.impl;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.converter.config.ConfigurablePopulatorModification;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;


/**
 * Default implementation of the {@link ConfigurablePopulator} extending {@link AbstractModifiableConfigurablePopulator}
 */
public class DefaultConfigurablePopulator<SOURCE, TARGET, OPTION> extends
		AbstractModifiableConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	private static final Logger LOG = Logger.getLogger(DefaultConfigurablePopulator.class);

	private Map<OPTION, Populator<SOURCE, TARGET>> populators;

	private final Object modificationLock = new Object();

	@Override
	public void applyModification(final ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification)
	{
		if (modification.getAdd() != null)
		{
			getPopulators().put((OPTION) modification.getResolvedKey(), modification.getAdd());
		}
		else if (modification.getRemove() != null)
		{
			getPopulators().remove(modification.getResolvedKey(), modification.getRemove());
		}
	}

	@Override
	public void populate(final SOURCE source, final TARGET target, final Collection<OPTION> options) throws ConversionException
	{
		Assert.notNull(source, "Parameter [source] must not be null");
		Assert.notNull(target, "Parameter [target] must not be null");
		Assert.notEmpty(options, "Parameter [options] must not be empty");

		// make sure the first thread accessing the method applies the modifications
		// afterwards isModified() will return true
		synchronized (modificationLock)
		{
			if (!isModified())
			{
				applyModifications();
			}
		}

		if (!CollectionUtils.isEmpty(getPopulators()))
		{
			for (final OPTION option : options)
			{
				final Populator<SOURCE, TARGET> populator = getPopulators().get(option);
				if (populator == null)
				{
					LOG.warn("No populator configured for option [" + option + "]");
				}
				else
				{
					populator.populate(source, target);
				}
			}
		}
	}

	protected Map<OPTION, Populator<SOURCE, TARGET>> getPopulators()
	{
		return populators;
	}

	@Required
	public void setPopulators(final Map<OPTION, Populator<SOURCE, TARGET>> populators)
	{
		this.populators = populators;
	}
}

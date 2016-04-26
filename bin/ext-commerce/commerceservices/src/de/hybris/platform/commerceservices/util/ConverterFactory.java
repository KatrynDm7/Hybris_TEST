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
package de.hybris.platform.commerceservices.util;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import org.apache.log4j.Logger;


public class ConverterFactory<SOURCE, TARGET, P extends Populator>
{
	//Logger for anonymous inner class
	private static final Logger LOG = Logger.getLogger(AbstractPopulatingConverter.class);

	public AbstractPopulatingConverter<SOURCE, TARGET> create(final Class<TARGET> targetClass, final P... populators)
	{
		return new AbstractPopulatingConverter<SOURCE, TARGET>()
		{
			@Override
			protected TARGET createTarget()
			{
				try
				{
					return targetClass.newInstance();
				}
				catch (final InstantiationException | IllegalAccessException e)
				{
					LOG.fatal(e);
				}
				return null;
			}

			@Override
			public void populate(final SOURCE source, final TARGET target)
			{
				for (final Populator populator : populators)
				{
					populator.populate(source, target);
				}
			}
		};
	}
}

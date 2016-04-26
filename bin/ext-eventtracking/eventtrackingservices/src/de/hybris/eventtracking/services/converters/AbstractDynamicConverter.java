/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.eventtracking.services.converters;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * @author stevo.slavic
 *
 */
public abstract class AbstractDynamicConverter<SOURCE, TARGET> implements Converter<SOURCE, TARGET>, Populator<SOURCE, TARGET>
{

	private final TypeResolver<SOURCE, TARGET> typeResolver;

	public AbstractDynamicConverter(final TypeResolver<SOURCE, TARGET> typeResolver)
	{
		this.typeResolver = typeResolver;
	}

	@Override
	public TARGET convert(final SOURCE source) throws ConversionException
	{
		final TARGET target = createTargetFromSource(source);
		if (target != null)
		{
			populate(source, target);
		}
		return target;
	}

	/**
	 * @deprecated Do not call this method - only call the single argument {@link #convert(Object)} method.
	 */
	@Override
	@Deprecated
	public TARGET convert(final SOURCE source, final TARGET prototype) throws ConversionException
	{
		return convert(source);
	}

	protected TARGET createTargetFromSource(final SOURCE source)
	{
		final Class<? extends TARGET> targetClass = typeResolver.resolveType(source);

		return createTarget(targetClass);
	}

	protected TARGET createTarget(final Class<? extends TARGET> targetClass)
	{
		try
		{
			return targetClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}

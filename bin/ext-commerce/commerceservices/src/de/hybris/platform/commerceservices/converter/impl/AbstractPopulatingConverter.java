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
package de.hybris.platform.commerceservices.converter.impl;


import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.lang.reflect.Method;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.util.ReflectionUtils;


/**
 * @deprecated please use {@link de.hybris.platform.converters.impl.AbstractPopulatingConverter}
 * 
 *             Populating converter that uses a list of configured populators to populate the target during conversion.
 */
@Deprecated
public class AbstractPopulatingConverter<SOURCE, TARGET> extends
		de.hybris.platform.converters.impl.AbstractPopulatingConverter<SOURCE, TARGET>
{
	private Class<TARGET> targetClass;

	@Override
	public TARGET convert(final SOURCE source) throws ConversionException
	{
		final TARGET target = targetClass == null ? createTarget() : createFromClass();
		populate(source, target);
		return target;
	}

	/**
	 * Allows to specify the target object class directly.
	 * 
	 * Please use that instead of the deprecated <lookup-method name="createTarget" ref="bean"> approach, as it's way
	 * faster.
	 */
	@Override
	public void setTargetClass(final Class<TARGET> targetClass)
	{
		this.targetClass = targetClass;

		// sanity check - can we instantiate that class ?
		if (targetClass != null)
		{
			createFromClass();
		}
	}

	@Override
	protected TARGET createFromClass()
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

	/**
	 * @deprecated please inject the target class directly, since it's way faster than the Spring <lookup-method>
	 *             approach!
	 */
	@Override
	@Deprecated
	protected TARGET createTarget()
	{
		// optional - no longer requiring sub classes to implement this method
		throw new NotImplementedException();
	}

	// -------------------------------------------------------------------
	// --- Sanity check for the two different converter setups
	// -------------------------------------------------------------------

	private String myBeanName;

	/*
	 * for sanity checks only
	 */
	@Override
	public void setBeanName(final String name)
	{
		this.myBeanName = name;
	}

	/*
	 * Ensures that either a class has been set or createTarget() has been overridden
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (targetClass == null)
		{
			final Class<? extends de.hybris.platform.converters.impl.AbstractConverter> cl = this.getClass();
			final Method createTargetMethod = ReflectionUtils.findMethod(cl, "createTarget");
			if (AbstractConverter.class.equals(createTargetMethod.getDeclaringClass()))
			{
				throw new IllegalStateException("Converter '" + myBeanName
						+ "' doesn't have a targetClass property nor does it override createTarget()!");
			}
		}
	}
}

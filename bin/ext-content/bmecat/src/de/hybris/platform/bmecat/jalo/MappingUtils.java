/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.jalo;

import de.hybris.platform.cronjob.jalo.Step;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * MappingUtils
 * 
 * 
 */
public class MappingUtils
{
	private static final Logger LOG = Logger.getLogger(MappingUtils.class.getName());

	public static void checkAttributeMapping(final Class baseJaloClass, final ComposedType targetType, final Map mapping)
	{
		if (!(baseJaloClass.isAssignableFrom(targetType.getJaloClass())))
		{
			throw new JaloInvalidParameterException("TargetType " + targetType.getCode() + " is no subtype of "
					+ baseJaloClass.getName() + ".", 0);
		}
		for (final Iterator it = mapping.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry mapEntry = (Map.Entry) it.next();
			final EnumerationValue bmeCatField = (EnumerationValue) mapEntry.getKey();
			final AttributeDescriptor attributeDescriptor = (AttributeDescriptor) mapEntry.getValue();
			if (bmeCatField == null)
			{
				throw new JaloInvalidParameterException("Found NULL key in attribute mapping " + mapping, 0);
			}
			if (attributeDescriptor == null)
			{
				throw new JaloInvalidParameterException("Found NULL attribute in attribute mapping " + mapping, 0);
			}
			if (!attributeDescriptor.getEnclosingType().equals(targetType))
			{
				throw new JaloInvalidParameterException("Attribute " + attributeDescriptor + " does not belong to target type "
						+ targetType.getCode() + " ( Mapping: " + mapping + " )", 0);
			}
		}
	}

	public static void addMapping(final Map mapping, final EnumerationValue eValue, final ComposedType targetType,
			final String attributeQualifier, final Step step)
	{
		AttributeDescriptor attributeDescriptor = null;
		try
		{
			attributeDescriptor = targetType.getAttributeDescriptor(attributeQualifier);
			if (attributeDescriptor != null)
			{
				mapping.put(attributeDescriptor, eValue);
				if (step != null && step.isDebugEnabled())
				{
					step.debug("Successfully added mapping for '" + attributeQualifier + " -> " + eValue.getCode() + "'!");
				}
				LOG.debug("Successfully added mapping for '" + attributeQualifier + " -> " + eValue.getCode() + "'!");
			}
			else
			{
				if (step != null && step.isErrorEnabled())
				{
					step.error("Failed to add mapping for '" + attributeQualifier + " -> " + eValue + "'!");
				}
				LOG.error("Failed to add mapping for '" + attributeQualifier + " -> " + eValue + "'!");
			}
		}
		catch (final JaloItemNotFoundException e)
		{
			if (step != null && step.isErrorEnabled())
			{
				step.error("Failed to add mapping for '" + attributeQualifier + " -> " + eValue + "'!");
			}
			LOG.error("Failed to add mapping for '" + attributeQualifier + " -> " + eValue + "'!");
			e.printStackTrace();
		}
	}

	public static void addMapping(final Map mapping, final ComposedType targetType, final EnumerationValue eValue,
			final String attributeQualifier, final Step step)
	{
		AttributeDescriptor attributeDescriptor = null;
		try
		{
			attributeDescriptor = targetType.getAttributeDescriptor(attributeQualifier);
			if (eValue != null)
			{
				mapping.put(eValue, attributeDescriptor);
				if (step != null && step.isDebugEnabled())
				{
					step.debug("Successfully added mapping for '" + eValue.getCode() + " -> " + attributeQualifier + "'!");
				}
				LOG.debug("Successfully added mapping for '" + eValue.getCode() + " -> " + attributeQualifier + "'!");
			}
			else
			{
				if (step != null && step.isErrorEnabled())
				{
					step.error("Failed to add mapping for '" + eValue + " -> " + attributeQualifier + "'!");
				}
				LOG.error("Failed to add mapping for '" + eValue + " -> " + attributeQualifier + "'!");
			}
		}
		catch (final JaloItemNotFoundException e)
		{
			if (step != null && step.isErrorEnabled())
			{
				step.error("Failed to add mapping for '" + eValue + " -> " + attributeQualifier + "'!");
			}
			LOG.error("Failed to add mapping for '" + eValue + " -> " + attributeQualifier + "'!");
			e.printStackTrace();
		}
	}
}

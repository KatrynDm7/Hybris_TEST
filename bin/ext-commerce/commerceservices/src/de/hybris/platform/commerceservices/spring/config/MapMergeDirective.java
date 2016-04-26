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

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;


public class MapMergeDirective implements InitializingBean
{

	private Object key;
	private Object value;
	private String mapPropertyDescriptor;
	private String fieldName;
	private Map<Object, Object> sourceMap;

	public Object getKey()
	{
		return key;
	}

	public void setKey(final Object key)
	{
		this.key = key;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(final Object value)
	{
		this.value = value;
	}

	public String getMapPropertyDescriptor()
	{
		return mapPropertyDescriptor;
	}

	public void setMapPropertyDescriptor(final String mapPropertyDescriptor)
	{
		this.mapPropertyDescriptor = mapPropertyDescriptor;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(final String fieldName)
	{
		this.fieldName = fieldName;
	}

	public Map<Object, Object> getSourceMap()
	{
		return sourceMap;
	}

	public void setSourceMap(final Map<Object, Object> sourceMap)
	{
		this.sourceMap = sourceMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		// sanaty check that either key and value was set or sourceMap
		if (getSourceMap() == null && getKey() == null && getValue() == null)
		{
			throw new IllegalStateException("Either key/value or sourceMap parameters should be injected into MapMergeDirective.");
		}
	}
}

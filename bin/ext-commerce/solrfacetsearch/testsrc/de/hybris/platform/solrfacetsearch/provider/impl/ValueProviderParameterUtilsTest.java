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
package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ValueProviderParameterUtilsTest
{
	private static final String INDEXED_PROPERTY_NAME = "test";
	private static final double DOUBLE_DELTA = 0.001;

	private Map<String, String> valueProviderParameters;

	private IndexedProperty indexedProperty;

	@Before
	public void setUp()
	{
		valueProviderParameters = new HashMap<String, String>();

		indexedProperty = new IndexedProperty();
		indexedProperty.setName(INDEXED_PROPERTY_NAME);
		indexedProperty.setValueProviderParameters(valueProviderParameters);
	}

	@Test
	public void testGetInt() throws Exception
	{
		// given
		final String paramKey = "INT_VALUE";
		final String paramValue = "15";
		final int expectedValue = 15;
		final int defaultValue = 25;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final int value = ValueProviderParameterUtils.getInt(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(expectedValue, value);
	}

	@Test
	public void testGetIntDefaultValue() throws Exception
	{
		// given
		final String paramKey = "INT_VALUE";
		final String paramValue = null;
		final int defaultValue = 25;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final int value = ValueProviderParameterUtils.getInt(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(defaultValue, value);
	}

	@Test
	public void testGetLong() throws Exception
	{
		// given
		final String paramKey = "LONG_VALUE";
		final String paramValue = "456";
		final long expectedValue = 456l;
		final long defaultValue = 14;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final long value = ValueProviderParameterUtils.getLong(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(expectedValue, value);
	}

	@Test
	public void testGetLongDefaultValue() throws Exception
	{
		// given
		final String paramKey = "LONG_VALUE";
		final String paramValue = null;
		final long defaultValue = 14;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final long value = ValueProviderParameterUtils.getLong(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(defaultValue, value);
	}

	@Test
	public void testGetDouble() throws Exception
	{
		// given
		final String paramKey = "DOUBLE_VALUE";
		final String paramValue = "78.67";
		final double expectedValue = 78.67d;
		final double defaultValue = 14.89;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final double value = ValueProviderParameterUtils.getDouble(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(expectedValue, value, DOUBLE_DELTA);
	}

	@Test
	public void testGetDoubleDefaultValue() throws Exception
	{
		// given
		final String paramKey = "DOUBLE_VALUE";
		final String paramValue = null;
		final double defaultValue = 14;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final double value = ValueProviderParameterUtils.getDouble(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(defaultValue, value, DOUBLE_DELTA);
	}

	@Test
	public void testGetBoolean() throws Exception
	{
		// given
		final String paramKey = "BOOLEAN_VALUE";
		final String paramValue = "false";
		final boolean expectedValue = false;
		final boolean defaultValue = true;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final boolean value = ValueProviderParameterUtils.getBoolean(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(Boolean.valueOf(expectedValue), Boolean.valueOf(value));
	}

	@Test
	public void testGetBooleanDefaultValue() throws Exception
	{
		// given
		final String paramKey = "BOOLEAN_VALUE";
		final String paramValue = null;
		final boolean defaultValue = true;

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final boolean value = ValueProviderParameterUtils.getBoolean(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(Boolean.valueOf(defaultValue), Boolean.valueOf(value));
	}

	@Test
	public void testGetString() throws Exception
	{
		// given
		final String paramKey = "STRING_VALUE";
		final String paramValue = "value";
		final String defaultValue = "defaultValue";

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final String value = ValueProviderParameterUtils.getString(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(paramValue, value);
	}

	@Test
	public void testGetStringDefaultValue() throws Exception
	{
		// given
		final String paramKey = "STRING_VALUE";
		final String paramValue = null;
		final String defaultValue = "defaultValue";

		valueProviderParameters.put(paramKey, paramValue);

		// when
		final String value = ValueProviderParameterUtils.getString(indexedProperty, paramKey, defaultValue);

		// then
		Assert.assertEquals(defaultValue, value);
	}
}

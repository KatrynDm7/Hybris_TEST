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
 */

package com.hybris.datahub.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class provides utility methods for converting data into CSV format that can be sent to a running Data Hub
 * through the DataHubOutboundService
 */
public class OutboundServiceCsvUtils
{
	private String datePattern;

	/**
	 * The date pattern to use when converting Date objects to Strings in UTC timezone This value is read from a property
	 * 'datahubadapter.datahuboutbound.date.pattern'
	 *
	 * @param datePattern the date pattern
	 */
	@Required
	public void setDatePattern(final String datePattern)
	{
		this.datePattern = datePattern;
	}

	/**
	 * Converts a single <code>Object</code> into CSV formatted data.
	 * <p/>
	 * <p>
	 * The Object's property names are appended to the CSV header. The string value of the object's properties will be
	 * appended to the CSV body.
	 * </p>
	 *
	 * @param obj the object to convert to CSV format
	 * @return an array of CSV data
	 */
	public String[] convertObjectToCsv(final Object obj)
	{
		final Map<String, Object> csvMap = convertObjectToMap(obj);
		return convertMapToCsv(csvMap);
	}

	/**
	 * Converts a single <code>Map</code> into CSV formatted data.
	 * <p/>
	 * <p>
	 * The <code>Map</code> keys are used as values in the CSV header. The string value of <code>Map.Entry</code> values
	 * will be used in the CSV body.
	 * </p>
	 *
	 * @param map the map to convert into CSV formatted data
	 * @return an array of CSV data
	 */
	public String[] convertMapToCsv(final Map<String, Object> map)
	{
		final String[] csvData = new String[2];

		final Set<String> csvHeaderValues = map.keySet();

		csvData[0] = createCsvHeader(csvHeaderValues, map);
		csvData[1] = createCsvBody(csvHeaderValues, map);
		return csvData;
	}

	/**
	 * Converts a <code>List</code> of Maps into CSV formatted data CSV header is populated using the keys from the first
	 * Map in the List
	 * <p/>
	 * <p>
	 * Each Map in the List should contain the same keys
	 * </p>
	 *
	 * @param objList the List of Maps to convert into CSV formatted data
	 * @return an array of CSV data
	 */
	public String[] convertListToCsv(final List<Map<String, Object>> objList)
	{
		// the array will be the size of the List + 1 row for the csv headers
		final String[] csvArray = new String[objList.size() + 1];

		final Set<String> csvHeaderValues = objList.get(0).keySet();

		int csvIndex = 0;
		csvArray[csvIndex] = createCsvHeader(csvHeaderValues, objList.get(0));

		for (final Map<String, Object> map : objList)
		{
			csvIndex++;
			csvArray[csvIndex] = createCsvBody(csvHeaderValues, map);
		}
		return csvArray;
	}

	/**
	 * Converts values of a map to be safe for a data transmission meaning the data will be consistently reproduced at
	 * both ends of the communication.
	 *
	 * @param original a map to make transmission safe.
	 * @return a map safe to transmit.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Map<String, Object>> T transmissionSafe(final T original)
	{
		final Map<String, Object> safe = new HashMap<>(original.size());
		for (final Map.Entry<String, Object> entry : original.entrySet())
		{
			final Object value = transmissionSafeValue(entry.getValue());
			if (value != null)
			{
				safe.put(entry.getKey(), value);
			}
		}
		return (T) safe;
	}

	protected Map<String, Object> convertObjectToMap(final Object obj)
	{
		final Map<String, Object> result = new HashMap<>();
		try
		{
			final BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());

			// loop through the properties of the bean
			for (final PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors())
			{

				// skip if the property name is "class"
				if ("class".equals(propertyDesc.getName()))
				{
					continue;
				}

				final Method getter = propertyDesc.getReadMethod();

				// skip if there is no getter
				if (getter == null)
				{
					continue;
				}
				final Object value = getter.invoke(obj);

				// don't add null values and collections to the Map
				if ((value != null) && !(value instanceof Collection<?>))
				{
					result.put(propertyDesc.getName(), value);
				}
			}
		}
		catch (final Exception e)
		{
			throw new IllegalArgumentException("Unable to introspect bean");
		}
		return result;
	}

	private String createCsvHeader(final Set<String> csvHeaderValues, final Map<String, Object> map)
	{
		final StringBuilder csvHeader = new StringBuilder();
		for (final String header : csvHeaderValues)
		{
			// skip the header if it's corresponding value is a collection
			if (map.get(header) instanceof Collection)
			{
				continue;
			}
			if (csvHeader.length() > 0)
			{
				csvHeader.append(",");
			}
			csvHeader.append(StringEscapeUtils.escapeCsv(header));
		}
		return csvHeader.toString();
	}

	private String createCsvBody(final Set<String> csvHeaderValues, final Map<String, Object> map)
	{
		final StringBuilder csvBody = new StringBuilder();
		int counter = 0;

		for (final String header : csvHeaderValues)
		{
			// skip the header if it's corresponding value is a collection
			if (map.get(header) instanceof Collection)
			{
				continue;
			}
			if (counter > 0)
			{
				csvBody.append(",");
			}
			final Object obj = map.get(header);
			csvBody.append(toCsvValue(obj));
			counter++;
		}
		return csvBody.toString();
	}

	protected String toCsvValue(final Object obj)
	{
		final String safeValue = transmissionSafeValue(obj);
		return safeValue != null ? StringEscapeUtils.escapeCsv(safeValue) : "";
	}

	/**
	 * Handles certain type conversion for unsafe types, e.g. Date
	 *
	 * @param obj a value to convert to another type, if necessary.
	 * @return the converted value.
	 */
	private String transmissionSafeValue(final Object obj)
	{
		return obj == null ? null : obj instanceof Date ? DateFormatUtils.formatUTC((Date) obj, datePattern) : String.valueOf(obj);
	}
}

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
package de.hybris.platform.ycommercewebservices.jaxb;

import de.hybris.platform.ycommercewebservices.jaxb.StringMapAdapter.MapContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;


public class StringMapAdapter extends XmlAdapter<MapContainer, Map<String, String>>
{

	@Override
	public Map<String, String> unmarshal(final MapContainer v) throws Exception
	{
		if (v == null)
		{
			return null;
		}

		final Map<String, String> result = new HashMap<String, String>();
		for (final MapElement element : v.getEntry())
		{
			result.put(element.key, element.value);
		}
		return result;
	}

	@Override
	public MapContainer marshal(final Map<String, String> v) throws Exception
	{
		if (v == null)
		{
			return null;
		}

		final MapContainer result = new MapContainer();

		for (final Entry<String, String> entry : v.entrySet())
		{
			result.getEntry().add(new MapElement(entry.getKey(), entry.getValue()));
		}
		return result;
	}

	public static class MapContainer
	{
		@XmlElement
		private List<MapElement> entry;

		public List<MapElement> getEntry()
		{
			if (entry == null)
			{
				entry = new ArrayList<StringMapAdapter.MapElement>();
			}
			return entry;
		}

	}

	public static class MapElement
	{
		@XmlElement
		private String key;
		@XmlElement
		private String value;

		public MapElement()
		{

		}

		public MapElement(final String key, final String value)
		{
			this.key = key;
			this.value = value;
		}
	}
}

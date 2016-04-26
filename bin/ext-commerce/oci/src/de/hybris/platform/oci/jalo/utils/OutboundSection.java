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
package de.hybris.platform.oci.jalo.utils;

import de.hybris.platform.oci.jalo.exception.OciException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * This class stores the given parameters from the SAP SRM server.<br>
 * The OutboundSection is then bounded as object to a jaloSession.
 * 
 * 
 */
public class OutboundSection implements Serializable
{

	public static class Fields
	{
		/**
		 * name of field from SRM server
		 */
		public final static String OK_CODE = "~OkCode";

		/**
		 * name of field from SRM server
		 */
		public final static String TARGET = "~Target";

		/**
		 * name of field from SRM server
		 */
		public final static String CALLER = "~Caller";
	}

	//wird alles in einer hashmap gespeichert
	private final Map fields;
	private String hook_url_fieldname;

	/**
	 * Constructor which stores the ParameterMap from the HTTP request.<br>
	 * Use the method <code>getHookURLFieldName()</code> from Interface <code>CatalogLoginPerformer</code> for accessing
	 * the value of field HOOK_URL.
	 * 
	 * @param map
	 *           ParameterMap from HTTP request
	 * @param hookurlFieldName
	 *           The field name for the HOOK_URL (default name); the field name is in SAP SRM server variable.
	 * 
	 * @see de.hybris.platform.oci.jalo.interfaces.CatalogLoginPerformer#getHookURLFieldName()
	 */
	public OutboundSection(final Map map, final String hookurlFieldName) throws OciException
	{
		this.fields = new HashMap();
		setAllFields(map, hookurlFieldName);
	}

	/**
	 * Set a key/value pair into the map if key is not null
	 * 
	 * @param name
	 *           the key as String
	 * @param value
	 *           the value as String
	 */
	protected void setField(final String name, final String value)
	{
		if (name != null)
		{
			this.fields.put(name.toUpperCase(), value);
		}
	}

	/**
	 * This method returns the value from the map for a given key.
	 * 
	 * @param name
	 *           the key as String
	 * @return the value as String if key/value pair exists, null otherwise
	 */
	public String getField(final String name)
	{
		return name != null ? (String) this.fields.get(name.toUpperCase()) : null;
	}

	/**
	 * The return URL (standart name: HOOK_URL) in SAP SRM server is variable and have to be specified in method
	 * <code>getHookURLFieldName()</code> in Interface <code>CatalogLoginPerformer</code>. This method returns the name
	 * of the field.
	 * 
	 * @return the field name where the HOOK_URL is stored
	 * 
	 * @see de.hybris.platform.oci.jalo.interfaces.CatalogLoginPerformer#getHookURLFieldName()
	 * 
	 */
	public String getHookURLFieldName()
	{
		return this.hook_url_fieldname;
	}

	/**
	 * 
	 * Set the OutboundSection with the given map and the HOOK_URL field name, same as Constructor from this class.
	 * 
	 * @param map
	 *           given map, could be request.getParameterMap()
	 * @param hookurlFieldName
	 *           The field name for the HOOK_URL (default name); the field name is in SAP SRM server variable.
	 */
	protected void setAllFields(final Map map, final String hookurlFieldName) throws OciException
	{
		for (final Iterator it = map.entrySet().iterator(); it.hasNext();)
		{
			final Map.Entry mapEntry = (Map.Entry) it.next();
			this.setField((String) mapEntry.getKey(), (String) mapEntry.getValue());
		}

		if (Utils.isEmpty(hookurlFieldName))
		{
			throw new OciException("Parameter for hook_url field name is null or empty", OciException.NO_HOOK_URL);
		}
		else
		{
			this.hook_url_fieldname = hookurlFieldName.toUpperCase();
		}
	}
}

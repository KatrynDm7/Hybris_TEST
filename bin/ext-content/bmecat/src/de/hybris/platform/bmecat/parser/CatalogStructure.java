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
package de.hybris.platform.bmecat.parser;

import java.util.Collection;


/**
 * Object which holds the value of a parsed &lt;CATALOGSTRUCTURE&gt; tag
 * 
 * 
 */
public class CatalogStructure extends CatalogGroupSystem
{
	private String parentID;
	private String type;
	private Integer order;
	private Collection medias;
	private Collection keywords;
	private Object udxValue;

	/**
	 * Collection of Keywords (String)<br>
	 * BMECat: CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE.KEYWORD
	 * 
	 * @return Returns the keywords.
	 */
	public Collection<String> getKeywords()
	{
		return keywords;
	}

	/**
	 * @param keywords
	 *           The keywords to set.
	 */
	public void setKeywords(final Collection<String> keywords)
	{
		this.keywords = keywords;
	}

	/**
	 * BMECat: CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE.MIME_INFO
	 * 
	 * @return Returns a Collection of {@link Mime}
	 */
	public Collection<Mime> getMedias()
	{
		return medias;
	}

	/**
	 * @param medias
	 *           The medias to set.
	 */
	public void setMedias(final Collection<Mime> medias)
	{
		this.medias = medias;
	}

	/**
	 * BMECat: CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE.GROUP_ORDER
	 * 
	 * @return Returns the order.
	 */
	public Integer getOrder()
	{
		return order;
	}

	/**
	 * @param order
	 *           The order to set.
	 */
	public void setOrder(final Integer order)
	{
		this.order = order;
	}

	/**
	 * BMECat: CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE.PARENT_ID
	 * 
	 * @return Returns the parentID.
	 */
	public String getParentID()
	{
		return parentID;
	}

	/**
	 * @param parentID
	 *           The parentID to set.
	 */
	public void setParentID(final String parentID)
	{
		this.parentID = parentID;
	}

	/**
	 * BMECat: CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE type="..."
	 * 
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *           The type to set.
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * BMECat: CATALOG_STRUCTURE.USER_DEFINED_EXTENSIONS
	 * 
	 * @return Returns the object representing the user defined extensions.
	 */
	public Object getUDXValue()
	{
		return udxValue;
	}

	/**
	 * BMECat: CATALOG_STRUCTURE.USER_DEFINED_EXTENSIONS
	 * 
	 * @param value
	 *           The object representing the user defined extensions.
	 */
	public void setUDXValue(final Object value)
	{
		this.udxValue = value;
	}
}

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

import de.hybris.bootstrap.xml.AbstractValueObject;

import java.util.Collection;


/**
 * Object which holds the value of a parsed &lt;CATALOGGROUPSYSTEM&gt; tag
 * 
 * 
 */
public class CatalogGroupSystem extends AbstractValueObject
{
	private String id;
	private String name;
	private String description;
	private Collection categories;

	/**
	 * BMECat: CATALOG_GROUP_SYSTEM.GROUP_SYSTEM_DESCRIPTION or CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE.GROUP_DESCRIPTION
	 * 
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           The description to set.
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * BMECat: CATALOG_GROUP_SYSTEM.GROUP_SYSTEM_ID or CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE.GROUP_ID
	 * 
	 * @return Returns the id.
	 */
	public String getID()
	{
		return id;
	}

	/**
	 * @param id
	 *           The id to set.
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * BMECat: CATALOG_GROUP_SYSTEM.GROUP_SYSTEM_NAME or CATALOG_GROUP_SYSTEM.CATALOG_STRUCTURE.GROUP_NAME
	 * 
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           The name to set.
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * A Collection of {@link CatalogStructure}
	 * 
	 * @return Returns the categories.
	 */
	public Collection<CatalogStructure> getCategories()
	{
		return categories;
	}

	/**
	 * @param categories
	 *           The categories to set.
	 */
	public void setCategories(final Collection<CatalogStructure> categories)
	{
		this.categories = categories;
	}
}

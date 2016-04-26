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


/**
 * 
 * 
 */
public class ClassificationGroup extends AbstractValueObject
{
	private String id;
	private String name;
	private String description;
	private String parentID;

	public String getID()
	{
		return id;
	}

	public void setID(final String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getParentID()
	{
		return parentID;
	}

	public void setParentID(final String parentID)
	{
		this.parentID = parentID;
	}
}

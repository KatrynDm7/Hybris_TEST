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
 * Object which holds the value of a parsed &lt;Abort&gt; tag
 * 
 * 
 */
public class Abort extends AbstractValueObject
{
	private final String type;

	public Abort(final String type)
	{
		super();
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}

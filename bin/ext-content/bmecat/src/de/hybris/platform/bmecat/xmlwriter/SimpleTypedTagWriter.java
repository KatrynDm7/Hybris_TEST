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
package de.hybris.platform.bmecat.xmlwriter;

import java.util.Collections;
import java.util.Map;


/**
 * Writes the &lt;SimpleTyped&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class SimpleTypedTagWriter extends SimpleTagWriter
{
	private final String typeValue;

	public SimpleTypedTagWriter(final XMLTagWriter parent, final String tagName, final String typeValue)
	{
		super(parent, tagName);
		this.typeValue = typeValue;
	}

	@Override
	protected Map getAttributesMap(final Object object)
	{
		return Collections.singletonMap("type", typeValue);
	}
}

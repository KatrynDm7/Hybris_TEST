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
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.util.HashMap;
import java.util.Map;


/**
 * Parses the &lt;SimpleValue&gt; tag
 * 
 * 
 */
public abstract class SimpleValueTagListener extends DefaultBMECatTagListener
{
	public final static String UNSPECIFIED_TYPE = "unspecified";
	private final String tagName;
	private final boolean typed;

	public SimpleValueTagListener(final TagListener parent, final String tagName)
	{
		this(parent, tagName, false);
	}

	public SimpleValueTagListener(final TagListener parent, final String tagName, final boolean typed)
	{
		super(parent);
		if (tagName == null)
		{
			throw new IllegalArgumentException("TagName can not be null!");
		}
		this.tagName = tagName;
		this.typed = typed;
	}

	public String getTagName()
	{
		return tagName;
	}

	/**
	 * @return Returns the typed.
	 */
	public boolean isTyped()
	{
		return typed;
	}

	public abstract Object getValue();

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		if (isTyped())
		{
			final Map values = new HashMap();
			//TODO: hack!
			values.put(getAttribute("type") == null ? UNSPECIFIED_TYPE : getAttribute("type"), getValue());
			return values;
		}
		else
		{
			return getValue();
		}
	}
}

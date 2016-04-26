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

import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;


/**
 * Parses the &lt;Dummy&gt; tag
 * 
 * 
 * 
 */
public class DummyTagListener extends DefaultBMECatTagListener
{
	public static final String TAGNAME = "DUMMY-TAG";

	public DummyTagListener()
	{
		super(null);
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		return null;
	}

	public String getTagName()
	{
		return TAGNAME;
	}
}

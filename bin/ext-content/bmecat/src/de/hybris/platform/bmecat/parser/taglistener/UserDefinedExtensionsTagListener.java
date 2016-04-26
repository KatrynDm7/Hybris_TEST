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

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;


/**
 * Parses the &lt;UserDefinedExtensions&gt; tag
 * 
 */
public abstract class UserDefinedExtensionsTagListener extends DefaultBMECatTagListener
{

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#processEndElement(de.hybris.platform.bmecat.parser.BMECatObjectProcessor)
	 */
	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		return null;
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS;
	}
}

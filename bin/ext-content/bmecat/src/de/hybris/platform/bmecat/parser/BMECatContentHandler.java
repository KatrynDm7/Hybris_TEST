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

import de.hybris.bootstrap.xml.XMLContentHandler;
import de.hybris.platform.bmecat.parser.taglistener.BMECatTagListener;

import org.apache.log4j.Logger;


/**
 * This class handles the parsed BMECat xml elements. Its implements basic tag processing and binds the root element to
 * the {@link de.hybris.platform.bmecat.parser.taglistener.BMECatTagListener}. This listener is root of a hierarchy of
 * listeners each of them parsing a BMECat tag. Each listener may call the processor with a values object of the parsed
 * tag.
 * 
 * 
 */
public class BMECatContentHandler extends XMLContentHandler
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(BMECatContentHandler.class.getName());

	protected final BMECatObjectProcessor processor;

	public BMECatContentHandler(final BMECatObjectProcessor processor)
	{
		super(new BMECatTagListener(null), processor);
		this.processor = processor;
	}

}

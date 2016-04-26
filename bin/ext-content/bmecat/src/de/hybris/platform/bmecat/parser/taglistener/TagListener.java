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
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.util.Map;

import org.xml.sax.Attributes;


/**
 * Interface for the TagListener
 * 
 * 
 * @deprecated will be replaced by {@link de.hybris.bootstrap.xml.TagListener}
 */
@Deprecated
@SuppressWarnings("deprecation")
public interface TagListener
{
	public void characters(char[] characterArray, int offset, int length);

	public void startElement(BMECatObjectProcessor processor, Attributes attributes) throws ParseAbortException;

	public void endElement(BMECatObjectProcessor processor) throws ParseAbortException;

	public TagListener getSubTagListener(String tagname);

	public Map getListenersMap();

	public String getTagName();

	public String getAttribute(String qname);

	public Object getResult();

	//public void deregisterTagListener(String tagname);
	public boolean hasSubTags();

	public boolean hasAttributes();

	public void setStartLineNumber(final int startLineNumber);

	public void setEndLineNumber(final int endLineNumber);
}

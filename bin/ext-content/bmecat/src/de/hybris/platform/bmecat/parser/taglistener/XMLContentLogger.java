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

/**
 * XMLContentLogger
 * 
 * 
 * @deprecated will be replaced by {@link de.hybris.bootstrap.xml.XMLContentLogger}
 */
@Deprecated
public interface XMLContentLogger
{
	public void setXML(String xml);

	public String getXML();
}

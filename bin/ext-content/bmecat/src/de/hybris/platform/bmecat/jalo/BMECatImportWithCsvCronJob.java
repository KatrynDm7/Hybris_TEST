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
package de.hybris.platform.bmecat.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


public class BMECatImportWithCsvCronJob extends GeneratedBMECatImportWithCsvCronJob
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(BMECatImportWithCsvCronJob.class.getName());

	//macro definition
	private Map<String, String> bmecatInfo = new HashMap<String, String>();

	//mapping
	private Map<String, String> bmecatMappings = new HashMap<String, String>();

	//default impex content in the jar directory
	private String defaultImpExContent;



	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	public Map<String, String> getBmecatInfo()
	{
		return bmecatInfo;
	}

	public void setBmecatInfo(final Map<String, String> bmecatInfo)
	{
		this.bmecatInfo = bmecatInfo;
	}

	public Map<String, String> getBmecatMappings()
	{
		return bmecatMappings;
	}

	public void setBmecatMappings(final Map<String, String> bmecatMappings)
	{
		this.bmecatMappings = bmecatMappings;
	}

	public String getDefaultImpExContent()
	{
		return defaultImpExContent;
	}

	public void setDefaultImpExContent(final String defaultImpExContent)
	{
		this.defaultImpExContent = defaultImpExContent;
	}

}

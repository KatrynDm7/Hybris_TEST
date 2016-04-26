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
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.CatalogGroupSystem;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;CatalogGroupSystem&gt; tag
 * 
 * 
 * 
 */
public class CatalogGroupSystemTagListener extends DefaultBMECatTagListener
{
	public CatalogGroupSystemTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.GROUP_SYSTEM_ID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.GROUP_SYSTEM_NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.GROUP_SYSTEM_DESCRIPTION),
				new CatalogStructureTagListener(this) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final CatalogGroupSystem groupSystem = new CatalogGroupSystem();
		groupSystem.setId((String) getSubTagValue(BMECatConstants.XML.TAG.GROUP_SYSTEM_ID));
		groupSystem.setName((String) getSubTagValue(BMECatConstants.XML.TAG.GROUP_SYSTEM_NAME));
		groupSystem.setDescription((String) getSubTagValue(BMECatConstants.XML.TAG.GROUP_SYSTEM_DESCRIPTION));
		groupSystem.setCategories(getSubTagValueCollection(BMECatConstants.XML.TAG.CATALOG_STRUCTURE));
		processor.process(this, groupSystem);

		return groupSystem;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.CATALOG_GROUP_SYSTEM;
	}
}

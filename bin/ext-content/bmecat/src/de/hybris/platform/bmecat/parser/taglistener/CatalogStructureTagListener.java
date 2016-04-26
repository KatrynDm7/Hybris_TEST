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
import de.hybris.platform.bmecat.parser.CatalogStructure;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;CatalogStructure&gt; tag
 * 
 * 
 * 
 */
public class CatalogStructureTagListener extends DefaultBMECatTagListener
{
	public CatalogStructureTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.GROUP_ID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.GROUP_NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.GROUP_DESCRIPTION),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.PARENT_ID),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.GROUP_ORDER),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.KEYWORD), new MimeInfoTagListener(this),
				new AbortTagListener(this) });


	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final CatalogStructure catalogStructure = new CatalogStructure();
		catalogStructure.setId((String) getSubTagValue(BMECatConstants.XML.TAG.GROUP_ID));
		catalogStructure.setName((String) getSubTagValue(BMECatConstants.XML.TAG.GROUP_NAME));
		catalogStructure.setDescription((String) getSubTagValue(BMECatConstants.XML.TAG.GROUP_DESCRIPTION));
		catalogStructure.setParentID((String) getSubTagValue(BMECatConstants.XML.TAG.PARENT_ID));
		catalogStructure.setOrder((Integer) getSubTagValue(BMECatConstants.XML.TAG.GROUP_ORDER));
		catalogStructure.setKeywords(getSubTagValueCollection(BMECatConstants.XML.TAG.KEYWORD));
		catalogStructure.setMedias(getSubTagValueCollection(BMECatConstants.XML.TAG.MIME_INFO));
		catalogStructure.setType(getAttribute(BMECatConstants.XML.ATTRIBUTE.CATALOG_STRUCTURE.TYPE));
		catalogStructure.setUDXValue(getSubTagValue(BMECatConstants.XML.TAG.USER_DEFINED_EXTENSIONS));
		processor.process(this, catalogStructure);
		return catalogStructure;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.CATALOG_STRUCTURE;
	}
}

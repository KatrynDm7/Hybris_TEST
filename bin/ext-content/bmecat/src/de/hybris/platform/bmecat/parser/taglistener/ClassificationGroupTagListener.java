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
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.ClassificationGroup;

import java.util.Arrays;
import java.util.Collection;


/**
 * 
 * 
 */
public class ClassificationGroupTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public ClassificationGroupTagListener(final TagListener parent)
	{
		super(parent);
	}

	/**
	 * @param parent
	 */
	public ClassificationGroupTagListener(final DefaultBMECatTagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_ID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_DESCR),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_PARENT_ID) });
	}

	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor)
	{
		final ClassificationGroup classificationGroup = new ClassificationGroup();
		classificationGroup.setID((String) getSubTagValue(BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_ID));
		classificationGroup.setName((String) getSubTagValue(BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_NAME));
		classificationGroup.setDescription((String) getSubTagValue(BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_DESCR));
		classificationGroup.setParentID((String) getSubTagValue(BMECatConstants.XML.TAG.CLASSIFICATION_GROUP_PARENT_ID));
		return classificationGroup;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.CLASSIFICATION_GROUP;
	}

}

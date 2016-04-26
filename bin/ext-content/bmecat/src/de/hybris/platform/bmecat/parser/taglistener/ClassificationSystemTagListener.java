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
import de.hybris.platform.bmecat.parser.ClassificationSystem;

import java.util.Arrays;
import java.util.Collection;


/**
 * 
 * 
 * 
 */
public class ClassificationSystemTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public ClassificationSystemTagListener(final TagListener parent)
	{
		super(parent);
	}

	/**
	 * @param parent
	 */
	public ClassificationSystemTagListener(final DefaultBMECatTagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.CLASSIFICATION_SYSTEM_NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.CLASSIFICATION_SYSTEM_VERSION),
				new ClassificationGroupsTagListener(this) });
	}

	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final ClassificationSystem classificationSystem = new ClassificationSystem();
		classificationSystem.setName((String) getSubTagValue(BMECatConstants.XML.TAG.CLASSIFICATION_SYSTEM_NAME));
		classificationSystem.setVersion((String) getSubTagValue(BMECatConstants.XML.TAG.CLASSIFICATION_SYSTEM_VERSION));
		classificationSystem.setGroups((Collection) getSubTagValue(BMECatConstants.XML.TAG.CLASSIFICATION_GROUPS));
		processor.process(this, classificationSystem);

		return classificationSystem;
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.CLASSIFICATION_SYSTEM;
	}

}

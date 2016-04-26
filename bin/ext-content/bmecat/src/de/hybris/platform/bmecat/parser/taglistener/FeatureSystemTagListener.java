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
import de.hybris.platform.bmecat.parser.FeatureSystem;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;FeatureSystem&gt; tag
 * 
 * 
 * 
 */
public class FeatureSystemTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public FeatureSystemTagListener(final TagListener parent)
	{
		super(parent);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 */
	public FeatureSystemTagListener(final DefaultBMECatTagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.FEATURE_SYSTEM_NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.FEATURE_SYSTEM_DESCR), new FeatureGroupTagListener(this) });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#processEndElement(de.hybris.platform.bmecat
	 * .parser.BMECatObjectProcessor)
	 */
	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor) throws ParseAbortException
	{
		final FeatureSystem featureSystem = new FeatureSystem();
		featureSystem.setName((String) getSubTagValue(BMECatConstants.XML.TAG.FEATURE_SYSTEM_NAME));
		featureSystem.setDescription((String) getSubTagValue(BMECatConstants.XML.TAG.FEATURE_SYSTEM_DESCR));
		featureSystem.setGroups(getSubTagValueCollection(BMECatConstants.XML.TAG.FEATURE_GROUP));
		processor.process(this, featureSystem);

		return featureSystem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.FEATURE_SYSTEM;
	}

}

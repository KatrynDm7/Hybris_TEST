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
import de.hybris.platform.bmecat.parser.FeatureGroup;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;FeatureGroup&gt; tag
 * 
 * 
 * 
 */
public class FeatureGroupTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public FeatureGroupTagListener(final TagListener parent)
	{
		super(parent);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 */
	public FeatureGroupTagListener(final DefaultBMECatTagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays
				.asList(new TagListener[]
				{ new StringValueTagListener(this, BMECatConstants.XML.TAG.FEATURE_GROUP_ID),
						new StringValueTagListener(this, BMECatConstants.XML.TAG.FEATURE_GROUP_NAME),
						new StringValueTagListener(this, BMECatConstants.XML.TAG.FEATURE_GROUP_DESCR),
						new FeatureTemplateTagListener(this) });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#processEndElement(de.hybris.platform.bmecat
	 * .parser.BMECatObjectProcessor)
	 */
	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor)
	{
		final FeatureGroup featureGroup = new FeatureGroup();
		featureGroup.setDescription((String) getSubTagValue(BMECatConstants.XML.TAG.FEATURE_GROUP_DESCR));
		featureGroup.setId((String) getSubTagValue(BMECatConstants.XML.TAG.FEATURE_GROUP_ID));
		featureGroup.setName((String) getSubTagValue(BMECatConstants.XML.TAG.FEATURE_GROUP_NAME));
		featureGroup.setTemplates(getSubTagValueCollection(BMECatConstants.XML.TAG.FEATURE_TEMPLATE));
		return featureGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.FEATURE_GROUP;
	}

}

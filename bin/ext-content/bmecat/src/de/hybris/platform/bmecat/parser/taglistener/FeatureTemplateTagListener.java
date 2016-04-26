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
import de.hybris.platform.bmecat.parser.FeatureTemplate;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;FeatureTemplate&gt; tag
 * 
 * 
 * 
 */
public class FeatureTemplateTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public FeatureTemplateTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.FT_NAME),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.FT_ORDER),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.FT_UNIT) });
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
		final FeatureTemplate featureTemplate = new FeatureTemplate();
		featureTemplate.setFTName((String) getSubTagValue(BMECatConstants.XML.TAG.FT_NAME));
		featureTemplate.setFTOrder((Integer) getSubTagValue(BMECatConstants.XML.TAG.FT_ORDER));
		featureTemplate.setFTUnit((String) getSubTagValue(BMECatConstants.XML.TAG.FT_UNIT));
		return featureTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.FEATURE_TEMPLATE;
	}

}

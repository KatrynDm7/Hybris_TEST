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
import de.hybris.platform.bmecat.parser.ArticleFeatures;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;ArticleFeatures&gt; tag
 * 
 * 
 * 
 */
public class ArticleFeaturesTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public ArticleFeaturesTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new FeatureTagListener(this), new StringValueTagListener(this, BMECatConstants.XML.TAG.REFERENCE_FEATURE_SYSTEM_NAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.REFERENCE_FEATURE_GROUP_ID),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.REFERENCE_FEATURE_GROUP_NAME) });
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
		final ArticleFeatures features = new ArticleFeatures();

		features.setFeatures(getSubTagValueCollection(BMECatConstants.XML.TAG.FEATURE));
		features.setReferenceFeatureSystemName((String) getSubTagValue(BMECatConstants.XML.TAG.REFERENCE_FEATURE_SYSTEM_NAME));
		features.setReferenceFeatureGroupId((String) getSubTagValue(BMECatConstants.XML.TAG.REFERENCE_FEATURE_GROUP_ID));
		features.setReferenceFeatureGroupName((String) getSubTagValue(BMECatConstants.XML.TAG.REFERENCE_FEATURE_GROUP_NAME));
		return features;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_FEATURES;
	}

}

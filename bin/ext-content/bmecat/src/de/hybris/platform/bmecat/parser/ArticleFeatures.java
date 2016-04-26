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
package de.hybris.platform.bmecat.parser;

import de.hybris.bootstrap.xml.AbstractValueObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;


/**
 * Object which holds the value of a parsed &lt;ARTICLEFEATURES&gt; tag
 * 
 * 
 * 
 */
public class ArticleFeatures extends AbstractValueObject
{
	public static final String FEATURE = "Feature";

	private String referenceFeatureSystemName;
	private String referenceFeatureGroupId;
	private String referenceFeatureGroupName;

	private final Map values;

	public ArticleFeatures()
	{
		super();
		values = new CaseInsensitiveMap();
	}

	/**
	 * BMECat: ARTICLE_FEATURES.FEATURE
	 * 
	 * @param feature
	 *           the feature to add
	 */
	public void addFeatures(final Feature feature)
	{
		List temp = (List) values.get(FEATURE);
		if (temp == null)
		{
			temp = new ArrayList();
		}
		temp.add(feature);
		values.put(FEATURE, temp);
	}

	/**
	 * BMECat: ARTICLE_FEATURES.FEATURE
	 * 
	 * @return Returns the features.
	 */
	public Collection<Feature> getFeatures()
	{
		return (Collection) values.get(FEATURE);
	}

	/**
	 * BMECat: ARTICLE_FEATURES.FEATURE
	 * 
	 * @param features
	 *           the features to set.
	 */
	public void setFeatures(final Collection<Feature> features)
	{
		values.put(FEATURE, features);
	}

	public void setReferenceFeatureSystemName(final String referenceFeatureSystemName)
	{
		this.referenceFeatureSystemName = referenceFeatureSystemName;
	}

	public void setReferenceFeatureGroupId(final String referenceFeatureGroupId)
	{
		this.referenceFeatureGroupId = referenceFeatureGroupId;
	}

	/**
	 * @return Returns the referenceFeatureGroupName.
	 */
	public String getReferenceFeatureGroupId()
	{
		return referenceFeatureGroupId;
	}

	/**
	 * @return Returns the referenceFeatureSystemName.
	 */
	public String getReferenceFeatureSystemName()
	{
		return referenceFeatureSystemName;
	}

	public String getReferenceFeatureGroupName()
	{
		return referenceFeatureGroupName;
	}

	/**
	 * Sets the referenceFeatureGroupName.
	 */
	public void setReferenceFeatureGroupName(final String referenceFeatureGroupName)
	{
		this.referenceFeatureGroupName = referenceFeatureGroupName;
	}
}

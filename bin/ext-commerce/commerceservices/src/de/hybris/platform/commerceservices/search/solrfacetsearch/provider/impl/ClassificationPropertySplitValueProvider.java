/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This ValueProvider will provide the value for a classification attribute on a product. It splits string value of
 * attributes base on splitRegex. Default value for splitRegex is '/'. For example : for attribute value
 * 'fixed/telephoto' it returns two values 'fixed' and 'telephoto'.
 */
@SuppressWarnings("deprecation")
public class ClassificationPropertySplitValueProvider extends CommerceClassificationPropertyValueProvider
{
	private String splitRegex = "/";


	@Override
	protected List<FieldValue> extractFieldValues(final IndexedProperty indexedProperty, final LanguageModel language,
			final List<FeatureValue> list) throws FieldValueProviderException
	{
		final List<FieldValue> result = new ArrayList<FieldValue>();

		for (final FeatureValue featureValue : list)
		{
			Object value = featureValue.getValue();
			if (value instanceof ClassificationAttributeValue)
			{
				value = ((ClassificationAttributeValue) value).getName();
			}
			final List<String> rangeNameList = getRangeNameList(indexedProperty, value);
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
					language == null ? null : language.getIsocode());
			for (final String fieldName : fieldNames)
			{
				if (rangeNameList.isEmpty())
				{
					addFieldValues(result, fieldName, value);
				}
				else
				{
					for (final String rangeName : rangeNameList)
					{
						addFieldValues(result, fieldName, rangeName == null ? value : rangeName);
					}
				}
			}
		}
		return result;
	}

	protected void addFieldValues(final List<FieldValue> result, final String fieldName, final Object value)
	{
		if (value instanceof String)
		{
			final String stringValue = (String) value;
			final String[] values = stringValue.split(getSplitRegex());
			for (final String fieldValue : values)
			{
				result.add(new FieldValue(fieldName, fieldValue));
			}
		}
		else
		{
			result.add(new FieldValue(fieldName, value));
		}
	}

	public String getSplitRegex()
	{
		return splitRegex;
	}

	public void setSplitRegex(final String splitRegex)
	{
		this.splitRegex = splitRegex;
	}
}

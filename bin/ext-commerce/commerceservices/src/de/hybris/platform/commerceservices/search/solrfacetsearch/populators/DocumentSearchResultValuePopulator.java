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
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.DocumentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class DocumentSearchResultValuePopulator implements Populator<DocumentData<SearchQuery, Document>, SearchResultValueData>
{
	@Override
	public void populate(final DocumentData<SearchQuery, Document> source, final SearchResultValueData target)
	{
		target.setValues(getValues(source));
		target.setFeatureValues(getFeatureValues(source));
	}

	protected Map<String, Object> getValues(final DocumentData<SearchQuery, Document> source)
	{
		final Map<String, Object> values = new HashMap<>();
		values.putAll(source.getDocument().getFields());
		return values;
	}

	protected Map<ClassAttributeAssignmentModel, Object> getFeatureValues(final DocumentData<SearchQuery, Document> source)
	{
		final IndexedType indexedType = source.getSearchQuery().getIndexedType();
		final Document document = source.getDocument();

		final Map<ClassAttributeAssignmentModel, Object> featureValues = new LinkedHashMap<ClassAttributeAssignmentModel, Object>();

		for (final IndexedProperty indexedProperty : indexedType.getIndexedProperties().values())
		{
			final ClassAttributeAssignmentModel classAttributeAssignment = indexedProperty.getClassAttributeAssignment();
			if (classAttributeAssignment != null && Boolean.TRUE.equals(classAttributeAssignment.getListable()))
			{
				final Object value = document.getFieldValue(indexedProperty.getName());
				if (value != null)
				{
					featureValues.put(classAttributeAssignment, value);
				}
			}
		}

		return featureValues;
	}
}

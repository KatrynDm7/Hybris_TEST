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
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrDocumentData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.springframework.beans.factory.annotation.Required;


/**
 * @deprecated Since 5.7, this populator is no longer used by default.
 */
@Deprecated
public class SolrDocumentSearchResultValuePopulator
		implements Populator<SolrDocumentData<SearchQuery, SolrDocument>, SearchResultValueData>
{
	private FieldNameProvider fieldNameProvider;

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	public void populate(final SolrDocumentData<SearchQuery, SolrDocument> source, final SearchResultValueData target)
	{
		final Map<String, Object> values = new HashMap<String, Object>();

		for (final IndexedProperty property : source.getSearchQuery().getIndexedType().getIndexedProperties().values())
		{
			values.put(property.getName(), getValue(source, property.getName()));
		}

		target.setValues(values);
		target.setFeatureValues(getFeatureValues(source.getSearchQuery().getIndexedType(), source));
	}

	protected Object getValue(final SolrDocumentData<SearchQuery, SolrDocument> source, final String propertyName)
	{
		final IndexedProperty indexedProperty = source.getSearchQuery().getIndexedType().getIndexedProperties().get(propertyName);
		if (indexedProperty == null)
		{
			return null;
		}

		return getValue(source, indexedProperty);
	}

	protected Object getValue(final SolrDocumentData<SearchQuery, SolrDocument> source, final IndexedProperty property)
	{
		final String fieldName = translateFieldName(source.getSearchQuery(), property);
		return source.getSolrDocument().getFieldValue(fieldName);
	}

	protected String translateFieldName(final SearchQuery searchQuery, final IndexedProperty property)
	{
		if (property.isLocalized())
		{
			return getFieldNameProvider().getFieldName(property, searchQuery.getLanguage(), FieldNameProvider.FieldType.INDEX);
		}
		else if (property.isCurrency())
		{
			return getFieldNameProvider().getFieldName(property, searchQuery.getCurrency(), FieldNameProvider.FieldType.INDEX);
		}
		return getFieldNameProvider().getFieldName(property, null, FieldNameProvider.FieldType.INDEX);
	}

	protected Map<ClassAttributeAssignmentModel, Object> getFeatureValues(final IndexedType indexedType,
			final SolrDocumentData<SearchQuery, SolrDocument> source)
	{
		final Map<ClassAttributeAssignmentModel, Object> result = new LinkedHashMap<ClassAttributeAssignmentModel, Object>();

		// Pull the classification features
		for (final IndexedProperty indexedProperty : indexedType.getIndexedProperties().values())
		{
			final ClassAttributeAssignmentModel classAttributeAssignment = indexedProperty.getClassAttributeAssignment();
			if (classAttributeAssignment != null && Boolean.TRUE.equals(classAttributeAssignment.getListable()))
			{
				final Object value = getValue(source, indexedProperty.getName());
				if (value != null)
				{
					result.put(classAttributeAssignment, value);
				}
			}

		}

		return result;
	}
}

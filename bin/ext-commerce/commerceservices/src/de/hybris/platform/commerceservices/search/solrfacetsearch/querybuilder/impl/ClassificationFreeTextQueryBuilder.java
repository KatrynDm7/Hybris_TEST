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
package de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of FreeTextQueryBuilder that finds all the searchable ClassAttributeAssignments
 */
public class ClassificationFreeTextQueryBuilder extends AbstractFreeTextQueryBuilder
{
	private int boost;

	protected int getBoost()
	{
		return boost;
	}

	@Required
	public void setBoost(final int boost)
	{
		this.boost = boost;
	}

	@Override
	public void addFreeTextQuery(final SearchQuery searchQuery, final String fullText, final String[] textWords)
	{
		for (final IndexedProperty indexedProperty : getSearchableClassificationProperties(searchQuery.getIndexedType()))
		{
			addFreeTextQuery(searchQuery, indexedProperty, fullText, textWords, getBoost());
		}
	}

	protected List<IndexedProperty> getSearchableClassificationProperties(final IndexedType indexedType)
	{
		final List<IndexedProperty> result = new ArrayList<IndexedProperty>();

		for (final IndexedProperty indexedProperty : indexedType.getIndexedProperties().values())
		{
			if (!indexedProperty.isFacet())
			{
				final ClassAttributeAssignmentModel classAttributeAssignment = indexedProperty.getClassAttributeAssignment();
				if (classAttributeAssignment != null && Boolean.TRUE.equals(classAttributeAssignment.getSearchable()))
				{
					result.add(indexedProperty);
				}
			}
		}

		return result;
	}
}

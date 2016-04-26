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

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.util.Feature;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.impl.ClassificationPropertyValueProvider;

import java.util.Collection;
import java.util.Collections;


/**
 * This ValueProvider will provide the value for a classification attribute on a product.
 */
@SuppressWarnings("deprecation")
public class CommerceClassificationPropertyValueProvider extends ClassificationPropertyValueProvider
{
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ClassAttributeAssignmentModel classAttributeAssignmentModel = indexedProperty.getClassAttributeAssignment();
			final ClassAttributeAssignment classAttributeAssignment = modelService.getSource(classAttributeAssignmentModel);

			final Product product = (Product) modelService.getSource(model);
			final FeatureContainer cont = FeatureContainer.loadTyped(product, classAttributeAssignment);
			if (cont.hasFeature(classAttributeAssignment))
			{
				final Feature feature = cont.getFeature(classAttributeAssignment);
				if (feature == null || feature.isEmpty())
				{
					return Collections.emptyList();
				}
				else
				{
					return getFeaturesValues(indexConfig, feature, indexedProperty);
				}
			}
			else
			{
				return Collections.emptyList();
			}
		}
		else
		{
			throw new FieldValueProviderException("Cannot provide classification property of non-product item");
		}
	}
}

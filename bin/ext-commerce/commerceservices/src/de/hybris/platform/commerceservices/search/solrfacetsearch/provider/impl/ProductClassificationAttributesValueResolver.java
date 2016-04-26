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

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Resolver for product classification attributes.
 * <p>
 * <h4>Supported parameters:</h4>
 * <p>
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Table showing supported parameters.">
 * <tr bgcolor="#ccccff">
 * <th>Parameter
 * <th>Default value
 * <th>Description
 * <tr valign=top>
 * <td>optional
 * <td>true
 * <td>If false, indicates that the resolved values should not be null and not an empty string (for every qualifier). If
 * these conditions are not met, an exception of type {@link FieldValueProviderException} is thrown.
 * <tr valign=top bgcolor="#eeeeff">
 * <td>split
 * <td>false
 * <td>If true, splits any resolved value around matches of a regular expression (only if the value is of type String).
 * <tr valign=top>
 * <td>splitRegex
 * <td>\s+
 * <td>If split is true this is the regular expression to use.
 * <tr valign=top bgcolor="#eeeeff">
 * <td>format
 * <td>null
 * <td>The ID of the Format Bean that is going to be used to format the attribute value object before applying the split
 * </table>
 * </blockquote>
 */
public class ProductClassificationAttributesValueResolver extends AbstractValueResolver<ProductModel, FeatureList, Object>
{
	final static Logger LOG = Logger.getLogger(ProductClassificationAttributesValueResolver.class);

	public static final String OPTIONAL_PARAM = "optional";
	public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

	private ClassificationService classificationService;

	public ClassificationService getClassificationService()
	{
		return classificationService;
	}

	@Required
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

	@Override
	protected FeatureList loadData(final IndexerBatchContext batchContext, final Collection<IndexedProperty> indexedProperties,
			final ProductModel product)
	{
		return loadFeatures(indexedProperties, product);
	}

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel model,
			final ValueResolverContext<FeatureList, Object> resolverContext) throws FieldValueProviderException
	{
		boolean hasValue = false;

		final FeatureList featureList = resolverContext.getData();
		if (featureList != null)
		{
			final Feature feature = getFeature(indexedProperty, featureList);
			if (feature != null)
			{
				final List<FeatureValue> featureValues = getFeatureValues(feature, resolverContext.getQualifier());
				if ((featureValues != null) && !featureValues.isEmpty())
				{
					for (final FeatureValue featureValue : featureValues)
					{
						final Object attributeValue = featureValue.getValue();

						hasValue |= filterAndAddFieldValues(document, batchContext, indexedProperty, attributeValue,
								resolverContext.getFieldQualifier());
					}
				}
			}
		}

		if (!hasValue)
		{
			final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
					OPTIONAL_PARAM_DEFAULT_VALUE);
			if (!isOptional)
			{
				throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
			}
		}
	}

	protected FeatureList loadFeatures(final Collection<IndexedProperty> indexedProperties, final ProductModel productModel)
	{
		final List<ClassAttributeAssignmentModel> classAttributeAssignments = new ArrayList<ClassAttributeAssignmentModel>();

		for (final IndexedProperty indexedProperty : indexedProperties)
		{
			if (indexedProperty.getClassAttributeAssignment() != null)
			{
				classAttributeAssignments.add(indexedProperty.getClassAttributeAssignment());
			}
		}

		return classificationService.getFeatures(productModel, classAttributeAssignments);
	}

	protected Feature getFeature(final IndexedProperty indexedProperty, final FeatureList featureList)
	{
		return featureList.getFeatureByAssignment(indexedProperty.getClassAttributeAssignment());
	}

	protected List<FeatureValue> getFeatureValues(final Feature feature, final Qualifier qualifier)
			throws FieldValueProviderException
	{
		Locale locale = null;

		if (qualifier != null)
		{
			locale = qualifier.getValueForType(Locale.class);
		}

		// the API does not take into consideration the current session language
		if ((feature instanceof LocalizedFeature) && (locale != null))
		{
			return ((LocalizedFeature) feature).getValues(locale);
		}
		else
		{
			return feature.getValues();
		}
	}
}

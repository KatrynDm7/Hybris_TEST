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

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.strategies.PickupAvailabilityStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

public class ProductPickupAvailabilityValueProvider extends AbstractPropertyFieldValueProvider implements Serializable,
		FieldValueProvider
{
	private static final long serialVersionUID = 1288577223117699469L;

	private FieldNameProvider fieldNameProvider;
	private PickupAvailabilityStrategy pickupAvailabilityStrategy;

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected PickupAvailabilityStrategy getPickupAvailabilityStrategy()
	{
		return pickupAvailabilityStrategy;
	}

	@Required
	public void setPickupAvailabilityStrategy(final PickupAvailabilityStrategy pickupAvailabilityStrategy)
	{
		this.pickupAvailabilityStrategy = pickupAvailabilityStrategy;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

			final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();

			if (baseSiteModel != null && baseSiteModel.getStores() != null && !baseSiteModel.getStores().isEmpty())
			{
				addFieldValues(
						fieldValues,
						indexedProperty,
						getPickupAvailabilityStrategy().isPickupAvailableForProduct(product,
								indexConfig.getBaseSite().getStores().get(0)));
			}

			return fieldValues;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get stock of non-product item");
		}
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}
}

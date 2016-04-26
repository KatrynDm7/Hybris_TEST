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
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.ProductPromotionModel;
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
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * This ValueProvider will provide the product banner from the product's primary promotion. Implementation uses only the
 * DefaultPromotionGroup.
 */
public class PrimaryPromotionImageValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{
	private FieldNameProvider fieldNameProvider;
	private PromotionsService promotionService;

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected PromotionsService getPromotionsService()
	{
		return promotionService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionService)
	{
		this.promotionService = promotionService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
			if (baseSiteModel != null && baseSiteModel.getDefaultPromotionGroup() != null)
			{
				for (final ProductPromotionModel promotion : getPromotionsService().getProductPromotions(
						Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), (ProductModel) model))
				{
					return createFieldValues(indexedProperty, promotion.getProductBanner());
				}
			}
			return Collections.EMPTY_LIST;
		}
		else
		{
			throw new FieldValueProviderException("Cannot get promotion image of non-product item");
		}
	}

	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final MediaModel media)
	{
		if (media != null)
		{
			return createFieldValues(indexedProperty, media.getURL());
		}
		else
		{
			return Collections.EMPTY_LIST;
		}
	}

	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final String value)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}

		return fieldValues;
	}
}

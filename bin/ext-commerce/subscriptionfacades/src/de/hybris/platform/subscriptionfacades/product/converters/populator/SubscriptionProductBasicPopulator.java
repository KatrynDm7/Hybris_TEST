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
package de.hybris.platform.subscriptionfacades.product.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionservices.model.SubscriptionProductModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populate the product data with the most basic product data.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class SubscriptionProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{
	private Converter<SubscriptionTermModel, SubscriptionTermData> subscriptionTermConverter;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		if (productModel instanceof SubscriptionProductModel)
		{
			final SubscriptionProductModel subscriptionProductModel = (SubscriptionProductModel) productModel;
			productData.setSubscriptionTerm(getSubscriptionTermConverter().convert(subscriptionProductModel.getSubscriptionTerm()));
			productData.setItemType(SubscriptionProductModel._TYPECODE);
		}
		else
		{
			if (StringUtils.isEmpty(productData.getItemType()))
			{
				productData.setItemType(ProductModel._TYPECODE);
			}
		}
	}

	protected Converter<SubscriptionTermModel, SubscriptionTermData> getSubscriptionTermConverter()
	{
		return subscriptionTermConverter;
	}

	@Required
	public void setSubscriptionTermConverter(final Converter<SubscriptionTermModel, SubscriptionTermData> subscriptionTermConverter)
	{
		this.subscriptionTermConverter = subscriptionTermConverter;
	}
}

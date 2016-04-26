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
package de.hybris.platform.commercefacades.product.impl;

import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Iterables;


public class DefaultProductVariantFacade extends DefaultProductFacade<ProductModel>
{

	@Override
	public ProductData getProductForOptions(final ProductModel productModel, final Collection<ProductOption> options)
	{
		if (CollectionUtils.isNotEmpty(options) && options.contains(ProductOption.VARIANT_FIRST_VARIANT)
				&& CollectionUtils.isNotEmpty(productModel.getVariants()))
		{
			final ProductModel firstVariant = Iterables.get(productModel.getVariants(), 0);
			return super.getProductForOptions(firstVariant, options);
		}
		else
		{
			return super.getProductForOptions(productModel, options);
		}
	}

}

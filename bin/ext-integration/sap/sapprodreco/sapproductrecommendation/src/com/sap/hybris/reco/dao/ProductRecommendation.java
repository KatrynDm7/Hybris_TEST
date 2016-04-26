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
package com.sap.hybris.reco.dao;

import de.hybris.platform.core.model.product.ProductModel;

/**
 * 
 */
public class ProductRecommendation
{
	private ProductModel product;

	/**
	 * @return ProductModel product
	 */
	public ProductModel getProduct()
	{
		return product;
	}

	/**
	 * @param product
	 *           the product to set
	 */
	public void setProduct(final ProductModel product)
	{
		this.product = product;
	}
}

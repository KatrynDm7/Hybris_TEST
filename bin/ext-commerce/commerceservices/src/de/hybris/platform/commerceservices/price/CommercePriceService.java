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
package de.hybris.platform.commerceservices.price;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;


/**
 * Commerce service that exposes b2c focused methods for getting the price of the product. This would typically replace
 * direct use of the default PriceService implementation in a b2c project.
 * 
 * @spring.bean commercePriceService
 */
public interface CommercePriceService
{

	/**
	 * Retrieve the minimum price from all variants
	 * 
	 * @param product
	 *           the product
	 * @return PriceInformation
	 */
	PriceInformation getFromPriceForProduct(ProductModel product);

	/**
	 * Retrieve the first price returned by ProductItem
	 * 
	 * @param product
	 *           the product
	 * @return PriceInformation
	 */
	PriceInformation getWebPriceForProduct(ProductModel product);
}

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
package de.hybris.platform.commerceservices.product;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import java.util.Collection;


/**
 * Product related methods that add extra b2c relevant functions not provided in {@link ProductService}
 */
public interface CommerceProductService
{

	/**
	 * Gets the super categories except classification classes.
	 * 
	 * @param productModel
	 *           the product model to retrieve super categories from
	 * @return the super categories except classification classes items
	 * @throws IllegalArgumentException
	 *            the illegal argument exception when given product model is <code>null</code>
	 */
	Collection<CategoryModel> getSuperCategoriesExceptClassificationClassesForProduct(ProductModel productModel)
			throws IllegalArgumentException;

	/**
	 * @deprecated Use {@link CommerceStockService}
	 * 
	 * @param productModel
	 *           the product model to look stock level for
	 * @return the available stock
	 */
	@Deprecated
	Integer getStockLevelForProduct(ProductModel productModel);

}

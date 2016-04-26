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
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

/**
 * A strategy interface for adding products to cart cart.
 */
public interface CommerceAddToCartStrategy
{
	/**
		 * Adds to the (existing) {@link de.hybris.platform.core.model.order.CartModel} the (existing) {@link de.hybris.platform.core.model.product.ProductModel} in the given {@link de.hybris.platform.core.model.product.UnitModel} and
		 * with the given <code>quantity</code>. If in the cart already an entry with the given product and given unit exists
		 * the given <code>quantity</code> is added to the the quantity of this cart entry unless <code>forceNewEntry</code>
		 * is set to true. After this the cart is calculated.
		 *
		 * @param parameter - A parameter object containing all attributes needed for add to cart
		 *                  <P>
		 *                  {@link CommerceCartParameter#cart}                 - The user's cart in session
		 *                  {@link CommerceCartParameter#pointOfService}       - The store object for pick up in store items (only needs to be passed in if you are adding an item to pick up
		 *                  {@link CommerceCartParameter#product}              - The {@link de.hybris.platform.core.model.product.ProductModel} to add
		 *                  {@link CommerceCartParameter#quantity}             - The quantity to add
		 *                  {@link CommerceCartParameter#unit}                 - The UnitModel of the product @see {@link de.hybris.platform.core.model.product.ProductModel#getUnit()}
		 *                  {@link CommerceCartParameter#createNewEntry}       - The flag for creating a new {@link de.hybris.platform.core.model.order.CartEntryModel}
		 *                  </P>
		 * @return the cart modification data that includes a statusCode and the actual quantity added to the cart
		 * @throws CommerceCartModificationException
		 *          if the <code>product</code> is a base product OR the quantity is less than 1 or no usable unit was
		 *          found (only when given <code>unit</code> is also <code>null</code>) or any other reason the cart could
		 *          not be modified.
		 */
	CommerceCartModification addToCart(CommerceCartParameter parameter) throws CommerceCartModificationException;
}

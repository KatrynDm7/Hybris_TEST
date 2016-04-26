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
package de.hybris.platform.sap.sapordermgmtcfgfacades.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.facades.strategy.ProductConfigAddToCartStrategy;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * DefaultAddToCartStrategy is being overriden by ProductConfigAddToCartStrategy and this class makes sure that the
 * functionality offered by both classes do not get lost.
 *
 */

public class CommerceConfigAddToCartStrategy extends ProductConfigAddToCartStrategy
{
	@Override
	protected long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel)
	{

		return quantityToAdd;
	}

}

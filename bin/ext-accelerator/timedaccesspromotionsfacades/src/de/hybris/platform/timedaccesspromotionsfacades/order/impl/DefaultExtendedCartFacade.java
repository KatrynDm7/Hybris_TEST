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
package de.hybris.platform.timedaccesspromotionsfacades.order.impl;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.timedaccesspromotionsfacades.order.ExtendedCartFacade;

import org.apache.log4j.Logger;


/**
 * Implementation for {@link ExtendedCartFacade}.
 */
public class DefaultExtendedCartFacade extends DefaultCartFacade implements ExtendedCartFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultExtendedCartFacade.class);

	@Override
	public CartModificationData addNewEntryToCart(final String productCode, final long quantity, final String promotionCode)
			throws CommerceCartModificationException
	{
		final ProductModel product = getProductService().getProductForCode(productCode);
		final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartParameter parameter = new CommerceCartParameter();

		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setProduct(product);
		parameter.setQuantity(quantity);
		parameter.setUnit(product.getUnit());
		parameter.setCreateNewEntry(true);
		parameter.setPromotionCode(promotionCode);

		final CommerceCartModification modification = getCommerceCartService().addToCart(parameter);
		return getCartModificationConverter().convert(modification);
	}
}

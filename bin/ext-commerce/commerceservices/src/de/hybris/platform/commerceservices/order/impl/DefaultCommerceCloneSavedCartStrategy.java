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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCloneSavedCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Default implementation of the interface {@link CommerceCloneSavedCartStrategy}.
 *
 */
public class DefaultCommerceCloneSavedCartStrategy extends AbstractCommerceCloneSavedCartStrategy
{
	@Override
	public CommerceSaveCartResult cloneSavedCart(final CommerceSaveCartParameter parameter) throws CommerceSaveCartException
	{
		final CommerceSaveCartResult cloneCartResult = new CommerceSaveCartResult();

		this.beforeCloneSaveCart(parameter);

		final CartModel clonedCart = getCartService().clone(null, null, parameter.getCart(), null);
		clonedCart.setPaymentTransactions(null);
		clonedCart.setCode(null); // save new cart, do not update existing one
		cloneCartResult.setSavedCart(clonedCart);
		getModelService().save(clonedCart);

		this.afterCloneSaveCart(parameter, cloneCartResult);

		return cloneCartResult;
	}
}
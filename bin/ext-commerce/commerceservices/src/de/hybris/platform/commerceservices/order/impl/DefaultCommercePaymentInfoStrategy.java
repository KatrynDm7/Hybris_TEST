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

import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommercePaymentInfoStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.*;

/**
 *  A default strategy for managing payment info for cart
 *
 */
public class DefaultCommercePaymentInfoStrategy implements CommercePaymentInfoStrategy
{
	private ModelService modelService;
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;


	@Override
	public boolean storePaymentInfoForCart(final CommerceCheckoutParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		final PaymentInfoModel paymentInfoModel = parameter.getPaymentInfo();

		validateParameterNotNull(cartModel, "Cart model cannot be null");
		validateParameterNotNull(paymentInfoModel, "payment info model cannot be null");

		cartModel.setPaymentInfo(paymentInfoModel);
		getModelService().saveAll(paymentInfoModel,cartModel);
		getModelService().refresh(cartModel);

		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setEnableHooks(true);
		commerceCartParameter.setCart(cartModel);
		getCommerceCartCalculationStrategy().calculateCart(commerceCartParameter);

		return true;
	}

	protected CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	@Required
	public void setCommerceCartCalculationStrategy(final CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}

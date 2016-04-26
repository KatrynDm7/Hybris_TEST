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
package de.hybris.platform.acceleratorwebservicesaddon.order.hook;

import de.hybris.platform.acceleratorwebservicesaddon.payment.service.PaymentSubscriptionResultService;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class WebServicesPlaceOrderHook implements CommercePlaceOrderMethodHook
{
	private static final Logger LOG = Logger.getLogger(WebServicesPlaceOrderHook.class);
	private PaymentSubscriptionResultService paymentSubscriptionResultService;

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult orderModel)
	{
		LOG.debug("Remove payment subscription result for cart");
		final CartModel cart = parameter.getCart();
		if (cart != null)
		{
			LOG.debug("cart code=" + cart.getCode() + " cart guid=" + cart.getGuid());
			paymentSubscriptionResultService.removePaymentSubscriptionResultForCart(cart.getCode(), cart.getGuid());
		}
	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter parameter)
	{
		//not needed
	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
	{
		//not needed
	}

	public PaymentSubscriptionResultService getPaymentSubscriptionResultService()
	{
		return paymentSubscriptionResultService;
	}

	@Required
	public void setPaymentSubscriptionResultService(final PaymentSubscriptionResultService paymentSubscriptionResultService)
	{
		this.paymentSubscriptionResultService = paymentSubscriptionResultService;
	}
}

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
package de.hybris.platform.b2ctelcofacades.bundle.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Telco implementation of checkout facade.
 */
public class DefaultTelcoCheckoutFacade extends DefaultAcceleratorCheckoutFacade
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultTelcoCheckoutFacade.class);
	private SubscriptionFacade subscriptionFacade;
	private static final String PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY = "subscription-only";

	@Override
	protected void afterPlaceOrder(final CartModel cartModel, final OrderModel orderModel)
	{
		super.afterPlaceOrder(cartModel, orderModel);
		final OrderData orderData = getOrderConverter().convert(orderModel);
		try
		{
			getSubscriptionFacade().createSubscriptions(orderData, new HashMap<String, String>());
		}
		catch (final SubscriptionFacadeException e)
		{
			LOG.error("Creating subscriptions failed", e);
		}
	}

	/**
	 * @return the subscriptionFacade
	 */
	protected SubscriptionFacade getSubscriptionFacade()
	{
		return subscriptionFacade;
	}

	/**
	 * @param subscriptionFacade
	 *           the subscriptionFacade to set
	 */
	@Required
	public void setSubscriptionFacade(final SubscriptionFacade subscriptionFacade)
	{
		this.subscriptionFacade = subscriptionFacade;
	}

	@Override
	public ExpressCheckoutResult performExpressCheckout()
	{
		return super.performExpressCheckout();
	}

	@Override
	public boolean setCheapestDeliveryModeForCheckout()
	{
		final List<? extends DeliveryModeData> availableDeliveryModes = getSupportedDeliveryModes();
		if (!availableDeliveryModes.isEmpty())
		{
			final boolean isSubscriptionOnlyCart = cartContainsSubscriptionProductsOnly(getCheckoutCart());

			for (final DeliveryModeData deliveryMethod : availableDeliveryModes)
			{
				if (isSubscriptionOnlyCart)
				{
					if (StringUtils.containsIgnoreCase(deliveryMethod.getCode(), PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY))
					{
						return setDeliveryMode(deliveryMethod.getCode());
					}
				}
				else
				{
					if (!StringUtils.containsIgnoreCase(deliveryMethod.getCode(), PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY))
					{
						return setDeliveryMode(deliveryMethod.getCode());
					}
				}
			}

		}
		return false;
	}

	protected boolean cartContainsSubscriptionProductsOnly(final CartData cartData)
	{
		for (final OrderEntryData entry : cartData.getEntries())
		{
			if (entry.getProduct() != null && entry.getProduct().getSubscriptionTerm() == null)
			{
				if (entry.getDeliveryPointOfService() == null)
				{
					return false;
				}
			}
		}
		return true;
	}
}

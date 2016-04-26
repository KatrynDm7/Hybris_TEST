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
package de.hybris.platform.b2ctelcostorefront.tags;

import de.hybris.platform.acceleratorstorefrontcommons.tags.Functions;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OrderPriceData;

import org.apache.commons.collections.CollectionUtils;


/**
 * JSP EL TelcoFunctions. This file contains static methods that are used by JSP EL.
 */
public class TelcoFunctions extends Functions
{
	/**
	 * Test if a cart has an applied promotion for the specified entry number.
	 * 
	 * @param cart
	 *           the cart
	 * @param entryNumber
	 *           the entry number
	 * @return true if there is an applied promotion for the entry number
	 */
	public static boolean doesAppliedPromotionExistForOrderEntry(final AbstractOrderData cart, final int entryNumber)
	{
		return cart != null && doesPromotionExistForOrderEntry(cart.getAppliedProductPromotions(), entryNumber);
	}

	/**
	 * Test if a cart has an applied promotion for the specified entry number and billing time.
	 * 
	 * @param cart
	 *           the cart
	 * @param entryNumber
	 *           the entry number
	 * @param billingTime
	 *           the billing time
	 * @return true if there is an applied promotion for the entry number and billing time
	 */
	public static boolean doesAppliedPromotionExistForOrderEntryAndBillingTime(final AbstractOrderData cart,
			final int entryNumber, final BillingTimeData billingTime)
	{
		final OrderPriceData orderPriceData = getOrderPricesForBillingTime(cart, billingTime);

		return cart != null
				&& orderPriceData != null
				&& doesPromotionExistForOrderEntry(orderPriceData.getAppliedProductPromotions(), entryNumber);

	}

	/**
	 * Test if a cart has an potential promotion for the specified entry number and billing time.
	 * 
	 * @param cart
	 *           the cart
	 * @param entryNumber
	 *           the entry number
	 * @param billingTime
	 *           the billing time
	 * @return true if there is an potential promotion for the entry number and billing time
	 */
	public static boolean doesPotentialPromotionExistForOrderEntryAndBillingTime(final AbstractOrderData cart,
			final int entryNumber, final BillingTimeData billingTime)
	{
		final OrderPriceData orderPriceData = getOrderPricesForBillingTime(cart, billingTime);

		return cart != null
				&& orderPriceData != null
				&& doesPromotionExistForOrderEntry(orderPriceData.getPotentialProductPromotions(), entryNumber);

	}


	/**
	 * Test if a cart has any potential promotion for the specified cart.
	 * 
	 * @param cart
	 *           the cart
	 * @return true if there is any potential promotion for the given cart
	 */
	public static boolean doesPotentialPromotionExistForOrder(final AbstractOrderData cart)
	{
		if (cart != null && CollectionUtils.isNotEmpty(cart.getOrderPrices()))
		{
			for (final OrderPriceData priceData : cart.getOrderPrices())
			{
				if (CollectionUtils.isNotEmpty(priceData.getPotentialOrderPromotions()))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Test if a cart has any applied promotion for the specified cart.
	 * 
	 * @param cart
	 *           the cart
	 * @return true if there is any applied promotion for the given cart
	 */
	public static boolean doesAppliedPromotionExistForOrder(final AbstractOrderData cart)
	{
		if (cart != null && CollectionUtils.isNotEmpty(cart.getOrderPrices()))
		{
			for (final OrderPriceData priceData : cart.getOrderPrices())
			{
				if (CollectionUtils.isNotEmpty(priceData.getAppliedOrderPromotions()))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Return the {@link OrderPriceData} for the given <code>billingTime</code> and <code>cart</code>.
	 */
	protected static OrderPriceData getOrderPricesForBillingTime(final AbstractOrderData cart, final BillingTimeData billingTime)
	{
		if (cart != null && billingTime != null && CollectionUtils.isNotEmpty(cart.getOrderPrices()))
		{
			for (final OrderPriceData orderPriceData : cart.getOrderPrices())
			{
				if (orderPriceData.getBillingTime() != null
						&& billingTime.getCode().equals(orderPriceData.getBillingTime().getCode()))
				{
					return orderPriceData;
				}
			}
		}

		return null;
	}
}

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
package de.hybris.platform.timedaccesspromotionsservices.order.impl;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.timedaccesspromotionsservices.constants.TimedaccesspromotionsservicesConstants;
import de.hybris.platform.timedaccesspromotionsservices.order.FlashbuyOrderCompleteStrategy;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


/**
 * The strategy is for B2C flash buy checkout process </br> After place order, it's order process complete in B2C</br>
 * After order complete, flashbuy service will allocate the promotion product, then flashbuy is complete
 */
public class FlashbuyPlaceOrderMethodHook implements CommercePlaceOrderMethodHook, FlashbuyOrderCompleteStrategy
{

	private static final Logger LOG = Logger.getLogger(FlashbuyPlaceOrderMethodHook.class);

	@Resource
	private SiteConfigService siteConfigService;

	@Resource
	private FlashbuyPromotionService flashbuyPromotionService;

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{

		final String orderCompleteStrategy = siteConfigService
				.getProperty(TimedaccesspromotionsservicesConstants.ORDER_COMPLETE_STRATEGY_KEY);

		if (TimedaccesspromotionsservicesConstants.ORDER_COMPLETE_STRATEGY_B2C.equalsIgnoreCase(orderCompleteStrategy))
		{

			orderComplete(result.getOrder());

		}

	}

	@Override
	public void orderComplete(final AbstractOrderModel order)
	{
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			final String promotionMatcher = entry.getPromotionMatcher();
			final String productCode = entry.getProduct().getCode();
			final String promotionCode = entry.getPromotionCode();

			if (promotionCode != null && promotionMatcher != null)
			{
				flashbuyPromotionService.allocate(promotionCode, productCode, promotionMatcher);
			}
		}
	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{

	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{

	}

	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	public FlashbuyPromotionService getFlashbuyPromotionService()
	{
		return flashbuyPromotionService;
	}

	public void setFlashbuyPromotionService(final FlashbuyPromotionService flashbuyPromotionService)
	{
		this.flashbuyPromotionService = flashbuyPromotionService;
	}



}

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
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.strategies.saving.impl.DefaultSaveAbstractOrderStrategy;
import de.hybris.platform.timedaccesspromotionsservices.constants.TimedaccesspromotionsservicesConstants;
import de.hybris.platform.timedaccesspromotionsservices.order.FlashbuyOrderCompleteStrategy;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import javax.annotation.Resource;


/**
 * The strategy is for CNACC flash buy checkout process </br> After payment complete, it's order process complete in
 * CNACC</br> After order complete, flashbuy service will allocate the promotion product, then flashbuy is complete
 */
public class ExtendedSaveOrderStrategy extends DefaultSaveAbstractOrderStrategy implements FlashbuyOrderCompleteStrategy
{
	@Resource
	private SiteConfigService siteConfigService;

	@Resource
	private FlashbuyPromotionService flashbuyPromotionService;

	@Override
	public AbstractOrderModel saveOrder(final AbstractOrderModel order)
	{

		final AbstractOrderModel returnOrder = super.saveOrder(order);

		final String orderCompleteStrategy = siteConfigService
				.getProperty(TimedaccesspromotionsservicesConstants.ORDER_COMPLETE_STRATEGY_KEY);

		if (TimedaccesspromotionsservicesConstants.ORDER_COMPLETE_STRATEGY_CNACC.equalsIgnoreCase(orderCompleteStrategy))
		{

			orderComplete(returnOrder);

		}

		return returnOrder;
	}

	@Override
	public void orderComplete(final AbstractOrderModel order)
	{
		if (order.getPaymentStatus().equals(PaymentStatus.PAID))
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

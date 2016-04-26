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
package de.hybris.platform.timedaccesspromotionsfacades.product.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.order.CartService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 * Populator for Flashbuy Promotion
 *
 */
public class FlashbuyPromotionPopulator implements Populator<AbstractPromotionModel, PromotionData>
{

	private CartService cartService;
	private PriceDataFactory priceDataFactory;
	private TimeService timeService;

	@Override
	public void populate(final AbstractPromotionModel source, final PromotionData target)
	{
		validateParameterNotNull(source, "Parameter source cannot be null.");
		validateParameterNotNull(target, "Parameter target cannot be null.");

		if (source instanceof FlashbuyPromotionModel)
		{
			final FlashbuyPromotionModel flashbuyPromotionModel = (FlashbuyPromotionModel) source;

			target.setAvailableUnitsPerProduct(flashbuyPromotionModel.getAvailableUnitsPerProduct());
			target.setAvailableUnitsPerUserAndProduct(flashbuyPromotionModel.getAvailableUnitsPerUserAndProduct());
			target.setItemType(FlashbuyPromotionModel._TYPECODE);
			target.setStartBuyDate(flashbuyPromotionModel.getStartBuyDate());

			final Date startbuyDate = flashbuyPromotionModel.getStartBuyDate();
			final Date currentDate = getTimeService().getCurrentTime();
			// Count down time calculation
			long time = startbuyDate.getTime() - currentDate.getTime();

			if (time < 0)
			{
				time = 0;
			}

			target.setCountDownTime(time);

			final String cart_currency = cartService.getSessionCart().getCurrency().getIsocode();

			for (final PromotionPriceRowModel ppr : flashbuyPromotionModel.getProductFixedUnitPrice())
			{
				if (cart_currency.equals(ppr.getCurrency().getIsocode()))
				{
					final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(ppr.getPrice()),
							ppr.getCurrency());
					target.setFixedPrice(priceData);
					return;
				}
			}

		}

	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

}

/*
 *
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
 */
package de.hybris.platform.timedaccesspromotionsservices.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryAdjustAction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionPriceRow;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;


public class FlashbuyPromotion extends GeneratedFlashbuyPromotion
{
	private final static String PROMOTION_MATCHER = "promotionMatcher";

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FlashbuyPromotion.class.getName());

	private boolean hasPromotionPriceRowForCurrency(final AbstractOrder order,
			final Collection<PromotionPriceRow> promotionPriceRows)
	{
		final String name = getComposedType().getName() + " (" + getCode() + ": " + getTitle() + ")";
		if (promotionPriceRows.isEmpty())
		{
			LOG.warn(name + " has no PromotionPriceRow. Skipping evaluation");
			return false;
		}
		final Currency currency = order.getCurrency();
		for (final PromotionPriceRow ppr : promotionPriceRows)
		{
			if (currency.equals(ppr.getCurrency()))
			{
				return true;
			}
		}
		LOG.warn(name + " has no PromotionPriceRow for currency " + currency.getName() + ". Skipping evaluation");
		return false;
	}

	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{
		final List<PromotionResult> results = new ArrayList<PromotionResult>();

		final PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, promoContext);
		final Collection<PromotionPriceRow> promotionPriceRows = getProductFixedUnitPrice(ctx);
		final AbstractOrder order = promoContext.getOrder();

		final boolean hasValidPromotionPriceRow = hasPromotionPriceRowForCurrency(order, promotionPriceRows);

		final Date currentTime = ctx.getAdjustedCurrentTime();
		final Date flashBuyStartDate = getStartBuyDate();

		if ((hasValidPromotionPriceRow) && (rsr.isAllowedToContinue()) && (!(rsr.getAllowedProducts().isEmpty()))
				&& flashBuyStartDate.before(currentTime))
		{
			final PromotionOrderView view = promoContext.createView(ctx, this, rsr.getAllowedProducts());
			final String customerId = ctx.getUser().getLogin();
			final FlashbuyPromotionService flashbuyPromotionService = (FlashbuyPromotionService) Registry.getApplicationContext()
					.getBean("flashbuyPromotionService");

			for (final PromotionOrderEntry entry : view.getAllEntries(ctx))
			{
				final String productCode = entry.getProduct(ctx).getCode(ctx);
				try
				{
					final String promotionMatcher = entry.getBaseOrderEntry().getAttribute(PROMOTION_MATCHER).toString();
					final long reservedQuantity = flashbuyPromotionService.getReserverdQuantity(getCode(ctx), productCode,
							promotionMatcher);
					//judge whether the current cart entry have reserved quantity for this promotion code
					if (reservedQuantity > 0)
					{
						promoContext.startLoggingConsumed(this);

						final long quantityOfOrderEntry = entry.getBaseOrderEntry().getQuantity(ctx).longValue();
						//the quantity to consume should be the smaller one
						//condition1: not all the amount is approved, then reservedQuantity should be applied
						//condition2:after approval, the customer deduct the quantity for the product in shopping cart, then quantityOfOrderEntry should be applied
						final long quantityToDiscount = Math.min(reservedQuantity, quantityOfOrderEntry);
						final Double fixedUnitPrice = getPriceForOrder(ctx, promotionPriceRows, order, "productFixedUnitPrice");

						if (fixedUnitPrice != null)
						{
							for (final PromotionOrderEntryConsumed poec : view.consume(ctx, quantityToDiscount))
							{
								poec.setAdjustedUnitPrice(ctx, fixedUnitPrice);
								poec.setOrderEntry(ctx, entry.getBaseOrderEntry());
							}

							final double adjustment = quantityToDiscount
									* (fixedUnitPrice.doubleValue() - entry.getBasePrice(ctx).doubleValue());

							final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
									promoContext.getOrder(), 1.0F);

							result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed(this, true));
							final PromotionOrderEntryAdjustAction poeac = PromotionsManager.getInstance()
									.createPromotionOrderEntryAdjustAction(ctx, entry.getBaseOrderEntry(), quantityOfOrderEntry,
											adjustment);
							result.addAction(ctx, poeac);

							results.add(result);
						}
						else
						{
							promoContext.abandonLogging(this);
						}
					}
				}
				catch (JaloInvalidParameterException | JaloSecurityException e)
				{
					LOG.error("Failed to get attribute " + PROMOTION_MATCHER, e);
				}
				catch (final Throwable t)
				{
					promoContext.abandonLogging(this);
				}
			}

		}
		return results;
	}

	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{
		final AbstractOrder order = promotionResult.getOrder(ctx);
		if ((order != null) && (promotionResult.getFired(ctx)))
		{
			final Double fixedUnitPrice = getPriceForOrder(ctx, getProductFixedUnitPrice(ctx), order, "productFixedUnitPrice");

			if (fixedUnitPrice != null)
			{
				final long quantityToDiscount = promotionResult.getConsumedCount(ctx, false);

				final double totalDiscount = promotionResult.getTotalDiscount(ctx);
				final Currency orderCurrency = order.getCurrency(ctx);

				final Object[] args =
				{ fixedUnitPrice, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, fixedUnitPrice.doubleValue()),
						Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount),
						quantityToDiscount };
				return formatMessage(getMessageFired(ctx), args, locale);

			}
		}

		return "";
	}

}

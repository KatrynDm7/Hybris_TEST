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
package de.hybris.platform.subscriptionfacades.order.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.BillingTimePriceData;
import de.hybris.platform.subscriptionfacades.data.OrderPriceData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract class for order converters for subscriptions. This class is responsible to merge entries together from the
 * child carts, and the total, the total tax, delivery cost,discounts and subtotals are stored as per billing time in a
 * {@link BillingTimePriceData} object which can be used by the sub classes.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public abstract class AbstractSubscriptionOrderPopulator<SOURCE extends AbstractOrderModel, TARGET extends AbstractOrderData>
		extends AbstractOrderPopulator<SOURCE, TARGET>
{
	private static final Logger LOG = Logger.getLogger(AbstractSubscriptionOrderPopulator.class);

	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;
	private CartService cartService;
	private OrderService orderService;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		if (source.getBillingTime() == null)
		{
			// compatibility mode: do not perform the subscription specific populator tasks
			return;
		}

		target.setOrderPrices(buildOrderPrices(source));

		if (source.getUser() instanceof CustomerModel)
		{
			target.setMerchantCustomerId(((CustomerModel) source.getUser()).getCustomerID());
		}

		addCommon(source, target);
		addTotals(source, target);
		addPromotions(source, target);
	}

	@Override
	protected void addCommon(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		prototype.setTotalItems(calcTotalItems(source));
	}

	@Override
	protected void addTotals(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		prototype.setDeliveryCost(source.getDeliveryMode() == null ? null : createPrice(source, getDeliveryCost(source)));
	}

	protected Double getDeliveryCost(final AbstractOrderModel source)
	{
		double subtotal = source.getDeliveryCost();
		for (final AbstractOrderModel orderModel : source.getChildren())
		{
			subtotal += orderModel.getDeliveryCost();
		}
		return subtotal;
	}

	public List<OrderPriceData> buildOrderPrices(final AbstractOrderModel source)
	{
		final Map<String, OrderPriceData> orderPriceContainer = new HashMap<>();

		final Collection<AbstractOrderModel> abstractOrders = new ArrayList<>();
		abstractOrders.add(source);
		abstractOrders.addAll(source.getChildren());

		for (final AbstractOrderModel abstractOrder : abstractOrders)
		{
			final OrderPriceData orderPrice = new OrderPriceData();
			orderPrice.setTotalPrice(createPrice(abstractOrder, abstractOrder.getTotalPrice()));
			orderPrice.setTotalTax(createPrice(abstractOrder, abstractOrder.getTotalTax()));
			orderPrice.setSubTotal(createPrice(abstractOrder, abstractOrder.getSubtotal()));
			orderPrice.setDeliveryCost(createPrice(abstractOrder, abstractOrder.getDeliveryCost()));

			final PromotionOrderResults promoOrderResults = getPromotionsService().getPromotionResults(abstractOrder);
			if (promoOrderResults != null)
			{
				orderPrice.setAppliedOrderPromotions(getSubscriptionPromotions(abstractOrder,
						promoOrderResults.getAppliedOrderPromotions()));
				orderPrice.setAppliedProductPromotions(getSubscriptionPromotions(abstractOrder,
						promoOrderResults.getAppliedProductPromotions()));
				orderPrice.setPotentialOrderPromotions(getSubscriptionPromotions(abstractOrder,
						promoOrderResults.getPotentialOrderPromotions()));
				orderPrice.setPotentialProductPromotions(getSubscriptionPromotions(abstractOrder,
						promoOrderResults.getPotentialProductPromotions()));
			}

			final BillingTimeData billingTimeData = getBillingTimeConverter().convert(abstractOrder.getBillingTime());
			orderPrice.setBillingTime(billingTimeData);
			orderPriceContainer.put(abstractOrder.getBillingTime().getCode(), orderPrice);
		}

		// extract the totals in the same order as the billingtimes are sorted in

		return buildBillingTimes(source).stream().map(billingTime -> orderPriceContainer.get(billingTime.getCode())).collect(Collectors.toList());
	}

	/**
	 * Modified to calculate discounts per billing time level. Iterate over child carts and add promotions to totals
	 */
	@Override
	protected void addPromotions(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		addPromotions(source, getPromotionsService().getPromotionResults(source), prototype);
		final List<OrderPriceData> orderPrices = prototype.getOrderPrices();

		final Map<String, OrderPriceData> orderPriceContainer = new HashMap<>();
		//create a Map of billingtimecode and total
		for (final OrderPriceData orderPrice : orderPrices)
		{
			orderPriceContainer.put(orderPrice.getBillingTime().getCode(), orderPrice);
		}

		final Collection<AbstractOrderModel> abstractOrders = new ArrayList<>();
		abstractOrders.add(source);
		abstractOrders.addAll(source.getChildren());

		for (final AbstractOrderModel abstractOrder : abstractOrders)
		{
			//get the total depending on billing time from order
			final OrderPriceData billingTimeTotal = orderPriceContainer.get(abstractOrder.getBillingTime().getCode());

			final BigDecimal currentTotalDiscounts = billingTimeTotal.getTotalDiscounts() == null ? BigDecimal.ZERO
					: billingTimeTotal.getTotalDiscounts().getValue();
			final BigDecimal newTotal = currentTotalDiscounts.add(BigDecimal.valueOf(getProductsDiscountsAmount(abstractOrder)
					+ getOrderDiscountsAmount(abstractOrder)));
			billingTimeTotal.setTotalDiscounts(createPrice(abstractOrder, newTotal.doubleValue()));
		}
	}

	/**
	 * This method builds an ordered list of billing times in the carts.
	 *
	 * @param source
	 *           - master cart model
	 * @return List of BillingTimeData objects existing in the cart in the correct order
	 */
	protected List<BillingTimeData> buildBillingTimes(final AbstractOrderModel source)
	{
		final List<BillingTimeData> billingTimeContainer = new ArrayList<>();

		final Collection<AbstractOrderModel> abstractOrders = new ArrayList<>();
		abstractOrders.add(source);
		abstractOrders.addAll(source.getChildren());

		billingTimeContainer.addAll(abstractOrders.stream().map(abstractOrder -> getBillingTimeConverter().convert(abstractOrder.getBillingTime())).collect(Collectors.toList()));

		return billingTimeContainer;
	}

	/**
	 * re-map the consumed order entry numbers in child carts to the master cart entry number.
	 */
	protected List<PromotionResultData> getSubscriptionPromotions(final AbstractOrderModel abstractOrder,
			final List<PromotionResult> promotionsResultList)
	{
		final List<PromotionResultData> promotionResultData = super.getPromotions(promotionsResultList);

		for (final PromotionResultData promotionResult : promotionResultData)
		{
			if (CollectionUtils.isNotEmpty(promotionResult.getConsumedEntries()))
			{
				for (final PromotionOrderEntryConsumedData consumedEntry : promotionResult.getConsumedEntries())
				{
					try
					{
						Integer masterOrderEntryNo;
						if (abstractOrder instanceof CartModel)
						{
							final CartEntryModel cartEntry = getCartService().getEntryForNumber((CartModel) abstractOrder,
									consumedEntry.getOrderEntryNumber());
							masterOrderEntryNo = cartEntry.getMasterEntry() == null ? cartEntry.getEntryNumber() : cartEntry
									.getMasterEntry().getEntryNumber();
						}
						else
						{
							final OrderEntryModel orderEntry = getOrderService().getEntryForNumber((OrderModel) abstractOrder,
									consumedEntry.getOrderEntryNumber());
							masterOrderEntryNo = orderEntry.getMasterEntry() == null ? orderEntry.getEntryNumber() : orderEntry
									.getMasterEntry().getEntryNumber();
						}
						consumedEntry.setOrderEntryNumber(masterOrderEntryNo);
					}
					catch (final UnknownIdentifierException ignored)
					{
						// ignoring exception
					}
				}
			}
		}

		return promotionResultData;
	}

	protected Converter<BillingTimeModel, BillingTimeData> getBillingTimeConverter()
	{
		return billingTimeConverter;
	}

	@Required
	public void setBillingTimeConverter(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter)
	{
		this.billingTimeConverter = billingTimeConverter;
	}

	protected CartService getCartService()
	{
		return this.cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

}

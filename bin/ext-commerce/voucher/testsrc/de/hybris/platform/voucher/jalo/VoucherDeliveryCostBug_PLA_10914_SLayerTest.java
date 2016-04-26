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
package de.hybris.platform.voucher.jalo;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.FindDiscountValuesStrategy;
import de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy;
import de.hybris.platform.order.strategies.calculation.impl.*;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * Tests proper order calculation using {@link CalculationService} for free shipping voucher and delivery cost.
 */
public class VoucherDeliveryCostBug_PLA_10914_SLayerTest extends AbstractVoucherTest
{

	private DefaultCalculationService calculationService;

	@Resource
	private CartService cartService;

	@Override
	@Before
	public void setUp() throws ConsistencyCheckException
	{
		super.setUp();

		setUpCalculationService();
	}

	private void setUpCalculationService()
	{
		ConfigurableApplicationContext appCtx = (ConfigurableApplicationContext) Registry.getApplicationContext();

		calculationService = new DefaultCalculationService();
		final CommonI18NService commonI18NService = (CommonI18NService) appCtx.getBean("commonI18NService");
		final ModelService modelService = (ModelService) appCtx.getBean("modelService");
		final DefaultFindDeliveryCostStrategy findDeliveryCostStrategy = new DefaultFindDeliveryCostStrategy();
		final DefaultOrderRequiresCalculationStrategy orderRequiresCalculationStrategy = new DefaultOrderRequiresCalculationStrategy();
		final DefaultFindPaymentCostStrategy findPaymentCostStrategy = new DefaultFindPaymentCostStrategy();
		final FindPricingWithCurrentPriceFactoryStrategy findPricingWithCurrentPriceFactoryStrategy = new FindPricingWithCurrentPriceFactoryStrategy();
		final FindOrderDiscountValuesStrategy findOrderDiscountValuesStrategy = new FindOrderDiscountValuesStrategy();

		findDeliveryCostStrategy.setModelService(modelService);
		findPaymentCostStrategy.setModelService(modelService);
		findPricingWithCurrentPriceFactoryStrategy.setModelService(modelService);
		findOrderDiscountValuesStrategy.setModelService(modelService);

		final List<FindTaxValuesStrategy> findTaxesStrategies = new ArrayList<>();
		findTaxesStrategies.add(findPricingWithCurrentPriceFactoryStrategy);

		final List<FindDiscountValuesStrategy> findDiscountsStrategies = new ArrayList<>();
		findDiscountsStrategies.add(findOrderDiscountValuesStrategy);
		findDiscountsStrategies.add(findPricingWithCurrentPriceFactoryStrategy);

		calculationService.setModelService(modelService);
		calculationService.setCommonI18NService(commonI18NService);
		calculationService.setFindDeliveryCostStrategy(findDeliveryCostStrategy);
		calculationService.setOrderRequiresCalculationStrategy(orderRequiresCalculationStrategy);
		calculationService.setFindPaymentCostStrategy(findPaymentCostStrategy);
		calculationService.setFindPriceStrategy(findPricingWithCurrentPriceFactoryStrategy);
		calculationService.setFindTaxesStrategies(findTaxesStrategies);
		calculationService.setFindDiscountsStrategies(findDiscountsStrategies);
	}

	@Test
	public void testServiceLayerBehaviour() throws CalculationException
	{
		final CartModel cart = cartService.getSessionCart();
		cart.setDeliveryMode(deliveryMode);
		cart.setDiscounts(Arrays.asList((DiscountModel) promotionVoucher));
		cartService.addNewEntry(cart, product, 1, unit); // only in 4.4 !!!
		modelService.saveAll(cart);
		calculationService.recalculate(cart);

		assertEquals(cart.getDeliveryCost().doubleValue(), deliveryCost, 0.0000001);
		List<DiscountValue> globalDiscountValues = cart.getGlobalDiscountValues();
		assertEquals(1, globalDiscountValues.size());
		DiscountValue discountValue = globalDiscountValues.get(0);
		final double expected = discountAmount + deliveryCost;
		assertEquals(expected, discountValue.getAppliedValue(), 0.000001);
		//check total : 15 + 2.5 - (10 off + free shipping) = 5
		assertEquals(5, cart.getTotalPrice().doubleValue(), 0.000001);

		cart.setDeliveryMode(null);
		modelService.save(cart);
		calculationService.recalculate(cart);

		assertEquals(0.0, cart.getDeliveryCost().doubleValue(), 0.0000001);
		globalDiscountValues = cart.getGlobalDiscountValues();
		assertEquals(1, globalDiscountValues.size());
		discountValue = globalDiscountValues.get(0);
		final double expectedAfter = discountAmount;
		assertEquals(expectedAfter, discountValue.getAppliedValue(), 0.000001); // <---- this should show the error
	}

}

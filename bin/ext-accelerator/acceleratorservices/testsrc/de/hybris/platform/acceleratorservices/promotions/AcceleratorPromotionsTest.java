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
package de.hybris.platform.acceleratorservices.promotions;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartService;
import de.hybris.platform.commerceservices.order.impl.DefaultCommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AcceleratorPromotionsTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(AcceleratorPromotionsTest.class);
	private static final String TEST_BASESITE_UID = "testSite";

	@Resource
	private DefaultCommercePlaceOrderStrategy defaultCommercePlaceOrderStrategy;

	@Resource
	private UserService userService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private DefaultCommerceCartService defaultCommerceCartService;

	@Before
	public void setUp() throws Exception
	{
		// final Create data for tests
		LOG.info("Creating data for commerce cart with promotions ..");
		userService.setCurrentUser(userService.getAdminUser());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		// importing test csv
		importCsv("/acceleratorservices/test/testPlaceOrder.csv", "utf-8");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);

		LOG.info("Finished data for commerce cart " + (System.currentTimeMillis() - startTime) + "ms");
	}

	// Testing the order placement with AcceleratorBOGOFPromotion.  See ACC-8276 for details of error
	@Test
	public void shouldPlaceOrderWithPromotions() throws InvalidCartException, CalculationException
	{
		// Get cart with items qualifying for BOGOPromotion
		final UserModel promoted = userService.getUserForUID("promoted");
		final Collection<CartModel> cartModels = promoted.getCarts();
		Assert.assertEquals(1, cartModels.size());
		final CartModel promotedCart = cartModels.iterator().next();

		// Update calculations and promotions on the cart
		final CommerceCartParameter calcParameter = new CommerceCartParameter();
		calcParameter.setEnableHooks(true);
		calcParameter.setCart(promotedCart);
		defaultCommerceCartService.calculateCart(calcParameter);

		// Verify promotions
		Assert.assertEquals(1, promotedCart.getAllPromotionResults().size());
		Assert.assertEquals(Double.valueOf("0.0"), promotedCart.getEntries().get(0).getTotalPrice());
		Assert.assertEquals(Double.valueOf("611.55"), promotedCart.getEntries().get(1).getTotalPrice());

		// Place order
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(promotedCart);
		parameter.setSalesApplication(SalesApplication.WEB);

		final OrderModel orderModel = defaultCommercePlaceOrderStrategy.placeOrder(parameter).getOrder();

		Assert.assertNotNull(orderModel);
		// Verify promotions transferred to order
		Assert.assertEquals(1, orderModel.getAllPromotionResults().size());
		Assert.assertEquals(Double.valueOf("0.0"), promotedCart.getEntries().get(0).getTotalPrice());
		Assert.assertEquals(Double.valueOf("611.55"), promotedCart.getEntries().get(1).getTotalPrice());
	}
}

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
package de.hybris.platform.commerceservices.order.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit test for restore cart functionality
 */
@IntegrationTest
public class DefaultCommerceCartServiceRestorationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultCommerceCartServiceRestorationTest.class);

	private static final String TEST_BASESITE_UID = "testSite";

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private ModelService modelService;

	@Resource
	private CommerceCartService commerceCartService;

	@Resource
	private CartService cartService;


	@Before
	public void setUp() throws Exception
	{

		LOG.info("Creating data for commerce cart ..");

		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		// importing test csv
		importCsv("/commerceservices/test/testCommerceCart.csv", "utf-8");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);

		LOG.info("Finished data for commerce cart " + (System.currentTimeMillis() - startTime) + "ms");
	}

	@Test
	public void testCartRestore() throws CommerceCartRestorationException
	{

		final CartModel cart = cartService.getSessionCart();
		cart.setSite(baseSiteService.getCurrentBaseSite());

		final PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();

		paymentTransactionModel.setEntries(Collections.EMPTY_LIST);

		modelService.save(paymentTransactionModel);

		final List<PaymentTransactionModel> paymentTransactions = new ArrayList<PaymentTransactionModel>();
		paymentTransactions.add(paymentTransactionModel);

		final String currentGuid = cart.getGuid();
		final String currentCartPK = cart.getPk().getLongValueAsString();

		cart.setPaymentTransactions(paymentTransactions);

		//save the cart and refresh it again to make sure the changes are reflected
		modelService.save(cart);
		modelService.refresh(cart);

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cart);

		assertEquals("No modifications should be present", 0, commerceCartService.restoreCart(parameter).getModifications().size());
		assertFalse("GUID shouldn't be the same", cart.getGuid().equals(currentGuid));
		assertEquals("Payment Transaction list should be empty", 0, cart.getPaymentTransactions().size());
		assertEquals("PK value shouldn't be altered", cart.getPk().getLongValueAsString(), currentCartPK);
		assertEquals("Cart should be recalculated", Boolean.TRUE, cart.getCalculated());
	}

}

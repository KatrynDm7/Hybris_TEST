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
package de.hybris.platform.chinaaccelerator.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.chinaaccelerator.facades.checkout.ChinaCheckoutFacade;
import de.hybris.platform.chinaaccelerator.services.enums.InvoiceCategory;
import de.hybris.platform.chinaaccelerator.services.enums.InvoiceTitle;
import de.hybris.platform.chinaaccelerator.services.model.invoice.InvoiceModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.ZoneDeliveryModeService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class ChinaCheckoutFacadeIntegrationTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(ChinaCheckoutFacadeIntegrationTest.class);
	@Resource
	private ChinaCheckoutFacade chinaCheckoutFacade;

	@Resource
	private ModelService modelService;

	@Resource(name = "zoneDeliveryModeService")
	private ZoneDeliveryModeService zoneDeliveryModeService;

	@Resource
	private CartService cartService;

	private CartModel cartModel;
	private InvoiceModel invoiceModel;
	private BaseSiteModel baseSiteModel;

	@Resource(name = "acceleratorCheckoutFacade")
	private AcceleratorCheckoutFacade checkoutFacade;

	@Resource
	private BaseSiteService baseSiteService;

	@Before
	public void setUp()
	{
		baseSiteModel = modelService.create(BaseSiteModel.class);
		baseSiteModel.setUid("TestSite");
		baseSiteService.setCurrentBaseSite(baseSiteModel, false);
		cartModel = cartService.getSessionCart();
		LOG.debug(cartModel.getPk());
		cartModel.setCode("TEST-CAET");
		invoiceModel = modelService.create(InvoiceModel.class);
		invoiceModel.setCategory(InvoiceCategory.FOOD);
		invoiceModel.setTitle(InvoiceTitle.INDIVIDUAL);
		invoiceModel.setInvoicedName("msg");
		cartModel.setInvoice(invoiceModel);
		cartService.saveOrder(cartModel);
		cartService.setSessionCart(cartModel);
		LOG.debug(cartModel.getPk());
	}

	@Test
	public void testMerge()
	{
		cartModel = cartService.getSessionCart();
		LOG.debug(cartModel.getCode());
		cartService.saveOrder(cartModel);
	}

	@Test
	public void testGetCart()
	{
		final CartModel cartModel2 = chinaCheckoutFacade.getCart();
		LOG.debug(cartModel2.getCode());
		assertNotNull(cartModel2);
		assertEquals(cartModel2.getCode(), "TEST-CAET");
		assertEquals(cartModel2.getInvoice().getPk(), cartModel.getInvoice().getPk());

	}

	@Test
	public void testConvertCart()
	{
		final CartData cata = chinaCheckoutFacade.convertCart(chinaCheckoutFacade.getCart());
		assertNotNull(cata);
		assertEquals(cata.getCode(), cartModel.getCode());
		assertEquals(cata.getInvoice().getInvoicedName(), cartModel.getInvoice().getInvoicedName());

	}

}
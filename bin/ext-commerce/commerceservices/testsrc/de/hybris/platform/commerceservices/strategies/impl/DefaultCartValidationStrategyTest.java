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
package de.hybris.platform.commerceservices.strategies.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCartValidationStrategyTest
{
	private DefaultCartValidationStrategy strategy;
	@Mock
	private ModelService modelService;
	@Mock
	private CartService cartService;
	@Mock
	private ProductService productService;
	@Mock
	private CommerceStockService commerceStockService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private UserService userService;
	@Mock
	private CartModel cart;
	@Mock
	private CartEntryModel cartEntry;
	@Mock
	private ProductModel product;
	@Mock
	private BaseStoreModel store;
	@Mock
	private UserModel previousUser;
	@Mock
	private UserModel currentUser;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		strategy = new DefaultCartValidationStrategy();
		strategy.setModelService(modelService);
		strategy.setCartService(cartService);
		strategy.setProductService(productService);
		strategy.setCommerceStockService(commerceStockService);
		strategy.setBaseStoreService(baseStoreService);
		strategy.setUserService(userService);

		given(cartEntry.getProduct()).willReturn(product);
		given(product.getCode()).willReturn("1");

		given(baseStoreService.getCurrentBaseStore()).willReturn(store);
		given(cartService.getEntriesForProduct(cart, product)).willReturn(Collections.<CartEntryModel> emptyList());
	}

	@Test
	public void testUnavailableStock()
	{
		given(productService.getProductForCode(cartEntry.getProduct().getCode())).willThrow(
				new UnknownIdentifierException("Product is unavailable."));

		final CommerceCartModification modification = strategy.validateCartEntry(cart, cartEntry);
		Assert.assertEquals(CommerceCartModificationStatus.UNAVAILABLE, modification.getStatusCode());
	}

	@Test
	public void testNoStock()
	{
		given(productService.getProductForCode(cartEntry.getProduct().getCode())).willReturn(product);
		given(commerceStockService.getStockLevelForProductAndBaseStore(product, store)).willReturn(Long.valueOf(0));
		given(cartEntry.getQuantity()).willReturn(Long.valueOf(20));

		final CommerceCartModification modification = strategy.validateCartEntry(cart, cartEntry);
		Assert.assertEquals(CommerceCartModificationStatus.NO_STOCK, modification.getStatusCode());
	}

	@Test
	public void testLowStock()
	{
		given(productService.getProductForCode(cartEntry.getProduct().getCode())).willReturn(product);
		given(commerceStockService.getStockLevelForProductAndBaseStore(product, store)).willReturn(Long.valueOf(10));
		given(cartEntry.getQuantity()).willReturn(Long.valueOf(20));

		final CommerceCartModification modification = strategy.validateCartEntry(cart, cartEntry);
		Assert.assertEquals(CommerceCartModificationStatus.LOW_STOCK, modification.getStatusCode());
	}

	@Test
	public void testShouldRemovePaymentAndDelivery()
	{
		final CartModel cartToBeCleaned = new CartModel();
		setupCartToBeCleaned(cartToBeCleaned);

		given(userService.getCurrentUser()).willReturn(currentUser);
		strategy.cleanCart(cartToBeCleaned);

		Assert.assertNull(cartToBeCleaned.getPaymentInfo());
		Assert.assertNull(cartToBeCleaned.getDeliveryAddress());
	}

	@Test
	public void testShouldNotRemovePaymentAndDelivery()
	{
		final CartModel cartToBeCleaned = new CartModel();
		setupCartToBeCleaned(cartToBeCleaned);

		given(userService.getCurrentUser()).willReturn(previousUser);
		strategy.cleanCart(cartToBeCleaned);

		Assert.assertNotNull(cartToBeCleaned.getPaymentInfo());
		Assert.assertNotNull(cartToBeCleaned.getDeliveryAddress());
	}

	protected void setupCartToBeCleaned(final CartModel cartModel)
	{
		final PaymentInfoModel paymentInfo = mock(PaymentInfoModel.class);
		given(paymentInfo.getUser()).willReturn(previousUser);
		cartModel.setPaymentInfo(paymentInfo);

		final AddressModel deliveryAddress = mock(AddressModel.class);
		given(deliveryAddress.getOwner()).willReturn(previousUser);
		cartModel.setDeliveryAddress(deliveryAddress);
	}
}
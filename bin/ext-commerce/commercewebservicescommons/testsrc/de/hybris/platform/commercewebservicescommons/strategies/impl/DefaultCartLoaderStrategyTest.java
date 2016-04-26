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
package de.hybris.platform.commercewebservicescommons.strategies.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;


/**
 * Test suite for {@link DefaultCartLoaderStrategy}
 * 
 */
@UnitTest
public class DefaultCartLoaderStrategyTest
{
	static final String CURRENT_CART_ID = "current";
	static final String CART_GUID = "6d868385adf11f729b6e30acd2c44195ccd6e882";
	static final String CART_CODE = "00000001";

	private DefaultCartLoaderStrategy cartLoaderStrategy;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private UserService userService;
	@Mock
	private CommerceCartService commerceCartService;
	@Mock
	private CartService cartService;
	@Mock
	private BaseSiteModel currentBaseSiteModel;
	@Mock
	private BaseSiteModel otherBaseSiteModel;
	@Mock
	private CustomerModel customerUserModel;
	@Mock
	private CustomerModel anonymousUserModel;
	@Mock
	private CartModel cartModel;
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	private CurrencyModel currencyModel;
	@Mock
	private CurrencyModel otherCurrencyModel;
	@Mock
	private ModelService modelService;
	@Mock
	private UserModel userModel;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		cartLoaderStrategy = new DefaultCartLoaderStrategy();
		cartLoaderStrategy.setBaseSiteService(baseSiteService);
		cartLoaderStrategy.setUserService(userService);
		cartLoaderStrategy.setCommerceCartService(commerceCartService);
		cartLoaderStrategy.setCartService(cartService);
		cartLoaderStrategy.setCommerceCommonI18NService(commerceCommonI18NService);
		cartLoaderStrategy.setModelService(modelService);

		given(commerceCommonI18NService.getCurrentCurrency()).willReturn(currencyModel);
		given(cartModel.getCurrency()).willReturn(currencyModel);
	}

	@Test(expected = CartException.class)
	public void testEmptyCartId() throws CommerceCartRestorationException
	{
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(userService.getCurrentUser()).willReturn(customerUserModel);

		cartLoaderStrategy.loadCart("");

		verify(commerceCartService, never()).restoreCart(any(CartModel.class));
	}

	@Test(expected = CartException.class)
	public void testNullCartId() throws CommerceCartRestorationException
	{
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(userService.getCurrentUser()).willReturn(customerUserModel);

		cartLoaderStrategy.loadCart(null);

		verify(commerceCartService, never()).restoreCart(any(CartModel.class));
	}

	@Test(expected = IllegalStateException.class)
	public void testNoUserInSession() throws ServletException, IOException
	{
		given(userService.getCurrentUser()).willReturn(null);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = IllegalStateException.class)
	public void testNoBaseSiteInSession() throws ServletException, IOException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(baseSiteService.getCurrentBaseSite()).willReturn(null);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = AccessDeniedException.class)
	public void testFailWhenNonCustomerUserAccessingCart()
	{
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(userService.getCurrentUser()).willReturn(userModel);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = AccessDeniedException.class)
	public void testAnonymousUserCurrentCart()
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(anonymousUserModel))).willReturn(Boolean.TRUE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);

		cartLoaderStrategy.loadCart(CURRENT_CART_ID);
	}

	@Test
	public void testAnonymousUser() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(anonymousUserModel))).willReturn(Boolean.TRUE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		//		given(commerceCartService.getCartForGuidAndSiteAndUser(CART_GUID, currentBaseSiteModel, anonymousUserModel)).willReturn(
		//				cartModel);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getGuid()).willReturn(CART_GUID);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartService.getSessionCart()).willReturn(cartModel);

		cartLoaderStrategy.loadCart(CART_GUID);

		verify(commerceCartService, times(1)).restoreCart(cartModel);
	}

	@Test(expected = CartException.class)
	public void testAnonymousUserRestorationFailed() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(anonymousUserModel))).willReturn(Boolean.TRUE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSiteAndUser(CART_GUID, currentBaseSiteModel, anonymousUserModel)).willReturn(
				cartModel);

		given(commerceCartService.restoreCart(cartModel)).willThrow(
				new CommerceCartRestorationException("Couldn't restore cart: " + CART_GUID));

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test
	public void testValidateBaseSiteFromLoadedCart() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(anonymousUserModel))).willReturn(Boolean.TRUE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getGuid()).willReturn(CART_GUID);

		cartLoaderStrategy.loadCart(CART_GUID);

		verify(commerceCartService, times(1)).restoreCart(cartModel);
	}

	@Test(expected = CartException.class)
	public void testValidateBaseSiteFromLoadedCartMismatch() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(anonymousUserModel))).willReturn(Boolean.TRUE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getSite()).willReturn(otherBaseSiteModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getGuid()).willReturn(CART_GUID);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = CartException.class)
	public void testValidateBaseSiteFromLoadedCartMismatchWhenCustomer() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getSite()).willReturn(otherBaseSiteModel);
		given(cartModel.getUser()).willReturn(customerUserModel);
		given(cartModel.getGuid()).willReturn(CART_GUID);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = CartException.class)
	public void testAnonymousUserCartNotFound()
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(anonymousUserModel))).willReturn(Boolean.TRUE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSiteAndUser(CART_GUID, currentBaseSiteModel, anonymousUserModel)).willReturn(
				null);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test
	public void testCustomerUserCurrentCart() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSiteAndUser(null, currentBaseSiteModel, customerUserModel))
				.willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCode()).willReturn(CART_CODE);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);

		cartLoaderStrategy.loadCart(CURRENT_CART_ID);

		verify(commerceCartService, times(1)).restoreCart(cartModel);
	}

	@Test(expected = CartException.class)
	public void testCustomerUserCurrentCartRestorationFailed() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSiteAndUser(null, currentBaseSiteModel, customerUserModel))
				.willReturn(cartModel);

		given(commerceCartService.restoreCart(cartModel)).willThrow(
				new CommerceCartRestorationException("Couldn't restore cart: " + CURRENT_CART_ID));

		cartLoaderStrategy.loadCart(CURRENT_CART_ID);
	}

	@Test(expected = CartException.class)
	public void testCustomerUserCurrentCartNotFound()
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSiteAndUser(null, currentBaseSiteModel, customerUserModel)).willReturn(null);

		cartLoaderStrategy.loadCart(CURRENT_CART_ID);
	}

	@Test
	public void testCustomerUser() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCode()).willReturn(CART_CODE);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);

		cartLoaderStrategy.loadCart(CART_CODE);
		verify(commerceCartService, times(1)).restoreCart(cartModel);
	}

	@Test(expected = CartException.class)
	public void testCustomerUserRestorationFailed() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(commerceCartService.restoreCart(cartModel)).willThrow(
				new CommerceCartRestorationException("Couldn't restore cart: " + CART_CODE));

		cartLoaderStrategy.loadCart(CART_CODE);
	}

	@Test(expected = CartException.class)
	public void testCustomerUserCartNotFound()
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(null);

		cartLoaderStrategy.loadCart(CART_CODE);
	}

	@Test(expected = CartException.class)
	public void testFailWithErrorWhenAnonymousCartExpired()
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(anonymousUserModel))).willReturn(Boolean.TRUE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForGuidAndSiteAndUser(CART_GUID, currentBaseSiteModel, anonymousUserModel)).willReturn(
				cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getGuid()).willReturn("Different_guid");

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = CartException.class)
	public void testFailWithErrorWhenCartExpired()
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(Boolean.valueOf(userService.isAnonymousUser(customerUserModel))).willReturn(Boolean.FALSE);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getGuid()).willReturn("Different_code");

		cartLoaderStrategy.loadCart(CART_CODE);
	}

	//    public void testFailWithErrorWhenDifferentCartOwner()

	@Test
	public void testApplyCurrencyToCartAndRecalculate() throws CalculationException
	{
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCurrency()).willReturn(otherCurrencyModel);

		cartLoaderStrategy.applyCurrencyToCartAndRecalculateIfNeeded();
		verify(commerceCartService, times(1)).recalculateCart(cartModel);
	}

	@Test(expected = CartException.class)
	public void testApplyCurrencyToCartAndRecalculateWithException() throws CalculationException
	{
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCurrency()).willReturn(otherCurrencyModel);
		doThrow(new CalculationException("Some calculation exception")).when(commerceCartService).recalculateCart(cartModel);

		try
		{
			cartLoaderStrategy.applyCurrencyToCartAndRecalculateIfNeeded();
		}
		catch (Exception e)
		{
			verify(commerceCartService, times(1)).recalculateCart(cartModel);
			throw e;
		}
	}

	@Test
	public void testApplyCurrencyToCartAndNoRecalculate() throws CalculationException
	{
		given(cartService.getSessionCart()).willReturn(cartModel);

		cartLoaderStrategy.applyCurrencyToCartAndRecalculateIfNeeded();
		verify(commerceCartService, times(0)).recalculateCart(cartModel);
	}
}

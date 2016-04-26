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
package de.hybris.platform.yacceleratorstorefront.security;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;


@UnitTest
public class StorefrontAuthenticationSuccessHandlerTest
{

	private final StorefrontAuthenticationSuccessHandler authenticationSuccessHandler = BDDMockito
			.spy(new StorefrontAuthenticationSuccessHandler());

	@Mock
	private SessionService sessionService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@Mock
	private CartFacade cartFacade;
	@Mock
	private CustomerFacade customerFacade;
	@Mock
	private BruteForceAttackCounter bruteForceAttackCounter;
	@Mock
	private CartData savedCart1;
	@Mock
	private CartData savedCart2;
	@Mock
	private CartData sessionCart;

	private List<String> excludedCartsGuid;

	private static final String CART_MERGED = "cartMerged";
	private static final String SAVED_CART_1 = "savedCart1";
	private static final String SESSION_CART = "sessionCart";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		BDDMockito.given(authenticationSuccessHandler.getCartFacade()).willReturn(cartFacade);
		BDDMockito.given(authenticationSuccessHandler.getCustomerFacade()).willReturn(customerFacade);
		BDDMockito.given(authenticationSuccessHandler.getBruteForceAttackCounter()).willReturn(bruteForceAttackCounter);
		BDDMockito.given(authenticationSuccessHandler.getSessionService()).willReturn(sessionService);
		BDDMockito.given(authenticationSuccessHandler.getListRedirectUrlsForceDefaultTarget()).willReturn(new ArrayList<String>());
		createUserCarts();
	}

	@Test
	public void shouldContinueToDefaultUrl()
	{
		BDDMockito.given(request.getAttribute(CART_MERGED)).willReturn(Boolean.TRUE);
		BDDMockito.doReturn(Boolean.FALSE).when(authenticationSuccessHandler).isAlwaysUseDefaultTargetUrl();
		authenticationSuccessHandler.setDefaultTargetUrl("/im/a/default/");

		Assert.assertTrue(StringUtils.equals("/im/a/default/", authenticationSuccessHandler.determineTargetUrl(request, response)));
	}

	@Test
	public void shouldContinueToCheckoutNoMerge()
	{
		BDDMockito.given(request.getAttribute(CART_MERGED)).willReturn(Boolean.FALSE);
		BDDMockito.doReturn(Boolean.FALSE).when(authenticationSuccessHandler).isAlwaysUseDefaultTargetUrl();
		authenticationSuccessHandler.setDefaultTargetUrl("/checkout");

		Assert.assertTrue(StringUtils.equals("/checkout", authenticationSuccessHandler.determineTargetUrl(request, response)));
	}

	@Test
	public void shouldRedirectToCartFromCheckoutMerge()
	{
		BDDMockito.given(request.getAttribute(CART_MERGED)).willReturn(Boolean.TRUE);
		BDDMockito.doReturn(Boolean.FALSE).when(authenticationSuccessHandler).isAlwaysUseDefaultTargetUrl();
		authenticationSuccessHandler.setDefaultTargetUrl("/checkout");

		Assert.assertTrue(StringUtils.equals("/cart", authenticationSuccessHandler.determineTargetUrl(request, response)));
	}

	@Test
	public void shouldNotReturnSessionCart()
	{
		excludedCartsGuid.add(SESSION_CART);
		BDDMockito.given(authenticationSuccessHandler.getCartFacade().getMostRecentCartGuidForUser(excludedCartsGuid)).willReturn(
				null);

		Assert.assertNull(authenticationSuccessHandler.getMostRecentSavedCartGuid(SESSION_CART));
	}

	@Test
	public void shouldReturnSavedCart()
	{
		excludedCartsGuid.add(SESSION_CART);
		BDDMockito.given(authenticationSuccessHandler.getCartFacade().getMostRecentCartGuidForUser(excludedCartsGuid)).willReturn(
				SAVED_CART_1);

		Assert.assertEquals(authenticationSuccessHandler.getMostRecentSavedCartGuid(SESSION_CART), SAVED_CART_1);
	}

	@Test
	public void shouldNotMergeCartsNoneSaved() throws Exception
	{
		setupAuthenticationHandler();
		BDDMockito.given(authenticationSuccessHandler.getMostRecentSavedCartGuid(SESSION_CART)).willReturn(null);

		authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		BDDMockito.verify(authenticationSuccessHandler.getCartFacade(), BDDMockito.times(0)).restoreCartAndMerge(
				BDDMockito.anyString(), BDDMockito.anyString());
	}

	@Test
	public void shouldNotMergeCartsCurrentCartEmpty() throws Exception
	{
		BDDMockito.given(Boolean.valueOf(cartFacade.hasEntries())).willReturn(Boolean.FALSE);

		setupAuthenticationHandler();
		authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		BDDMockito.verify(authenticationSuccessHandler.getCartFacade(), BDDMockito.times(0)).restoreCartAndMerge(SAVED_CART_1,
				SESSION_CART);
	}

	@Test
	public void shouldMergeCarts() throws Exception
	{
		BDDMockito.given(Boolean.valueOf(cartFacade.hasEntries())).willReturn(Boolean.TRUE);

		setupAuthenticationHandler();
		authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		BDDMockito.verify(authenticationSuccessHandler.getCartFacade()).restoreCartAndMerge(SAVED_CART_1, SESSION_CART);
	}

	protected void setupAuthenticationHandler()
	{
		BDDMockito.doReturn(Boolean.FALSE).when(authenticationSuccessHandler).isAlwaysUseDefaultTargetUrl();

		BDDMockito.doNothing().when(customerFacade).loginSuccess();
		BDDMockito.given(customerFacade.getCurrentCustomerUid()).willReturn("customer");

		excludedCartsGuid.add(SESSION_CART);
		BDDMockito.given(authenticationSuccessHandler.getCartFacade().getMostRecentCartGuidForUser(excludedCartsGuid)).willReturn(
				SAVED_CART_1);
		BDDMockito.given(cartFacade.hasSessionCart()).willReturn(Boolean.TRUE);
		BDDMockito.given(cartFacade.getSessionCartGuid()).willReturn(SESSION_CART);

		BDDMockito.doNothing().when(bruteForceAttackCounter).resetUserCounter("customer");
	}

	protected void createUserCarts()
	{
		BDDMockito.given(savedCart1.getGuid()).willReturn(SAVED_CART_1);
		BDDMockito.given(savedCart2.getGuid()).willReturn("savedCart2");
		BDDMockito.given(sessionCart.getGuid()).willReturn(SESSION_CART);
		final OrderEntryData entry = BDDMockito.mock(OrderEntryData.class);
		final List<OrderEntryData> orderEntries = new ArrayList();
		orderEntries.add(entry);

		BDDMockito.given(sessionCart.getEntries()).willReturn(orderEntries);

		final List<CartData> savedCarts = new ArrayList();

		savedCarts.add(sessionCart);

		excludedCartsGuid = new ArrayList<String>();
	}
}

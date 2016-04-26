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
package de.hybris.platform.ycommercewebservices.v2.filter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link de.hybris.platform.ycommercewebservices.v2.filter.CartMatchingFilter}
 *
 */
@UnitTest
public class CartMatchingFilterTest
{
	static final String DEFAULT_REGEXP = "^/[^/]+/users/[^/]+/carts/([^/]+)";
	static final String CURRENT_CART_ID = "current";
	static final String CART_GUID = "6d868385adf11f729b6e30acd2c44195ccd6e882";
	static final String CART_CODE = "00000001";

	private CartMatchingFilter cartMatchingFilter;
	@Mock
	private HttpServletRequest httpServletRequest;
	@Mock
	private HttpServletResponse httpServletResponse;
	@Mock
	private FilterChain filterChain;
	@Mock
	private CartLoaderStrategy cartLoaderStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		cartMatchingFilter = new CartMatchingFilter();
		cartMatchingFilter.setRegexp(DEFAULT_REGEXP);
		cartMatchingFilter.setCartLoaderStrategy(cartLoaderStrategy);
	}

	@Test
	public void testEmptyPathInfo() throws ServletException, IOException, CommerceCartRestorationException
	{
		given(httpServletRequest.getPathInfo()).willReturn(null);

		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

		verify(cartLoaderStrategy, never()).loadCart(anyString());
		verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
	}

	@Test
	public void testAnonymousNoCartInPath() throws ServletException, IOException, CommerceCartRestorationException
	{
		given(httpServletRequest.getPathInfo()).willReturn("/path/users/anonymous/addresses");

		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

		verify(cartLoaderStrategy, never()).loadCart(anyString());
		verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
	}

	@Test
	public void testAmnonymousCartByGuid() throws ServletException, IOException
	{
		given(httpServletRequest.getPathInfo()).willReturn("/path/users/anonymous/carts/" + CART_GUID);

		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

		verify(cartLoaderStrategy, times(1)).loadCart(CART_GUID);
		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
	}

	@Test
	public void testAmnonymousCartByGuidLongPath() throws ServletException, IOException
	{
		given(httpServletRequest.getPathInfo()).willReturn("/path/users/anonymous/carts/" + CART_GUID + "/long/path");

		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

		verify(cartLoaderStrategy, times(1)).loadCart(CART_GUID);
		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
	}

	@Test
	public void testUserCartByCode() throws ServletException, IOException
	{
		given(httpServletRequest.getPathInfo()).willReturn("/path/users/demo@customer.com/carts/" + CART_CODE);

		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

		verify(cartLoaderStrategy, times(1)).loadCart(CART_CODE);
		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
	}

	@Test
	public void testUserCartByCodeLongPath() throws ServletException, IOException
	{
		given(httpServletRequest.getPathInfo()).willReturn("/path/users/demo@customer.com/carts/" + CART_CODE + "/long/path");

		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

		verify(cartLoaderStrategy, times(1)).loadCart(CART_CODE);
		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
	}

	@Test
	public void testCurrentCart() throws ServletException, IOException
	{
		given(httpServletRequest.getPathInfo()).willReturn("/path/users/demo@customer.com/carts/" + CURRENT_CART_ID);

		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

		verify(cartLoaderStrategy, times(1)).loadCart(CURRENT_CART_ID);
		cartMatchingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
	}

}

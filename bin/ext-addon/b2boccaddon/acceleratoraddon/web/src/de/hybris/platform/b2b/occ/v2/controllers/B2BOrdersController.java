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
package de.hybris.platform.b2b.occ.v2.controllers;

import de.hybris.platform.b2b.occ.security.SecuredAccessConstants;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ycommercewebservices.v2.controller.BaseController;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@ApiVersion("v2")
public class B2BOrdersController extends BaseController
{
	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	@Resource(name = "b2bCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "userService")
	private UserService userService;

	/**
	 * Places a B2B Order.
	 * 
	 * @param cartId
	 *           the cart ID
	 * @param termsChecked
	 *           whether terms were accepted or not
	 * @param securityCode
	 *           for credit card payments
	 * @return a representation of {@link de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO}
	 * @throws InvalidCartException
	 */
	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	@RequestMappingOverride(priorityProperty = "b2bocc.B2BOrdersController.placeOrder.priority")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public OrderWsDTO placeOrder(@RequestParam(required = true) final String cartId,
			@RequestParam(required = true) final boolean termsChecked, @RequestParam(required = false) final String securityCode,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws InvalidCartException
	{
		if (userFacade.isAnonymousUser())
		{
			throw new AccessDeniedException("Access is denied");
		}

		final CartModel cart = commerceCartService.getCartForCodeAndUser(cartId, userService.getCurrentUser());
		if (cart == null)
		{
			throw new CartException("Cart not found.", CartException.NOT_FOUND, cartId);
		}
		cartService.setSessionCart(cart);

		final PlaceOrderData placeOrderData = new PlaceOrderData();
		placeOrderData.setTermsCheck(termsChecked);
		placeOrderData.setSecurityCode(securityCode);

		final OrderData orderData = checkoutFacade.placeOrder(placeOrderData);
		return dataMapper.map(orderData, OrderWsDTO.class, fields);
	}
}

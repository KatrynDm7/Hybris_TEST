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
package de.hybris.platform.ycommercewebservices.v2.controller;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ycommercewebservices.exceptions.NoCheckoutCartException;
import de.hybris.platform.ycommercewebservices.exceptions.PaymentAuthorizationException;
import de.hybris.platform.ycommercewebservices.strategies.OrderCodeIdentificationStrategy;
import de.hybris.platform.ycommercewebservices.v2.helper.OrdersHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Web Service Controller for the ORDERS resource. Most methods check orders of the user. Methods require authentication
 * and are restricted to https channel.
 *
 * @pathparam code Order GUID (Globally Unique Identifier) or order CODE
 * @pathparam userId User identifier or one of the literals below :
 *            <ul>
 *            <li>'current' for currently authenticated user</li>
 *            <li>'anonymous' for anonymous user</li>
 *            </ul>
 */
@Controller
@RequestMapping(value = "/{baseSiteId}")
public class OrdersController extends BaseCommerceController
{
	private final static Logger LOG = Logger.getLogger(OrdersController.class);

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;
	@Resource(name = "orderCodeIdentificationStrategy")
	private OrderCodeIdentificationStrategy orderCodeIdentificationStrategy;
	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;
	//TODO avoid using following services
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "ordersHelper")
	private OrdersHelper ordersHelper;

	/**
	 * Returns details of a specific order based on order GUID (Globally Unique Identifier) or order CODE. The response
	 * contains a detailed order information.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order data
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/orders/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'getOrder',#code,#fields)")
	@ResponseBody
	public OrderWsDTO getOrder(@PathVariable final String code, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		OrderData orderData;
		if (orderCodeIdentificationStrategy.isID(code))
		{
			orderData = orderFacade.getOrderDetailsForGUID(code);
		}
		else
		{
			orderData = orderFacade.getOrderDetailsForCodeWithoutUser(code);
		}

		return dataMapper.map(orderData, OrderWsDTO.class, fields);
	}

	/**
	 * Returns specific order details based on a specific order code. The response contains detailed order information.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order data
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/orders/{code}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@Cacheable(value = "orderCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'getOrderForUserByCode',#code,#fields)")
	@ResponseBody
	public OrderWsDTO getOrderForUserByCode(@PathVariable final String code,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final OrderData orderData = orderFacade.getOrderDetailsForCode(code);
		final OrderWsDTO dto = dataMapper.map(orderData, OrderWsDTO.class, fields);
		return dto;
	}

	/**
	 * Returns order history data for all orders placed by the specific user for the specific base store. Response
	 * contains orders search result displayed in several pages if needed.
	 *
	 * @queryparam statuses Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only
	 *             return orders with status CANCELLED or CHECKED_VALID.
	 * @queryparam currentPage The current result page requested.
	 * @queryparam pageSize The number of results returned per page.
	 * @queryparam sort Sorting method applied to the return results.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Order history data.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 120)
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.GET)
	@ResponseBody
	public OrderHistoryListWsDTO getOrdersForUser(@RequestParam(required = false) final String statuses,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false) final String sort, @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletResponse response)
	{
		validateStatusesEnumValue(statuses);

		final OrderHistoryListWsDTO orderHistoryList = ordersHelper.searchOrderHistory(statuses, currentPage, pageSize, sort,
				addPaginationField(fields));

		// X-Total-Count header
		setTotalCountHeader(response, orderHistoryList.getPagination());

		return orderHistoryList;
	}

	/**
	 * Returns {@value  de.hybris.platform.ycommercewebservices.v2.controller.BaseController#HEADER_TOTAL_COUNT} header with a total number of results (orders history for all
	 * orders placed by the specific user for the specific base store).
	 *
	 * @queryparam statuses Filters only certain order statuses. It means: statuses=CANCELLED,CHECKED_VALID would only
	 *             return orders with status CANCELLED or CHECKED_VALID.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.HEAD)
	@ResponseBody
	public void getCountOrdersForUser(@RequestParam(required = false) final String statuses, final HttpServletResponse response)
	{
		final OrderHistoriesData orderHistoriesData = ordersHelper.searchOrderHistory(statuses, 0, 1, null);

		setTotalCountHeader(response, orderHistoriesData.getPagination());
	}

	/**
	 * Authorizes cart and places the order. Response contains the new order data.
	 *
	 * @formparam cartId Cart code for logged in user, cart GUID for guest checkout
	 * @formparam securityCode CCV security code.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Created order data
	 * @throws PaymentAuthorizationException
	 *            When there are problems with the payment authorization. For example: there is no session cart or no
	 *            payment information set for the cart.
	 * @throws InvalidCartException
	 * @throws WebserviceValidationException
	 *            When the cart is not filled properly (e. g. delivery mode is not set, payment method is not set)
	 * @throws ProductLowStockException
	 *            When product is out of stock in store
	 * @throws StockSystemException
	 *            When there is no information about stock for stores.
	 */
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/users/{userId}/orders", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public OrderWsDTO placeOrder(@RequestParam(required = true) final String cartId,
			@RequestParam(required = false) final String securityCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws PaymentAuthorizationException,
			InvalidCartException, WebserviceValidationException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.info("placeOrder");
		}

		cartLoaderStrategy.loadCart(cartId);

		validateCartForPlaceOrder();

		//authorize
		if (!checkoutFacade.authorizePayment(securityCode))
		{
			throw new PaymentAuthorizationException();
		}

		//placeorder
		final OrderData orderData = checkoutFacade.placeOrder();
		final OrderWsDTO dto = dataMapper.map(orderData, OrderWsDTO.class, fields);
		return dto;
	}

}

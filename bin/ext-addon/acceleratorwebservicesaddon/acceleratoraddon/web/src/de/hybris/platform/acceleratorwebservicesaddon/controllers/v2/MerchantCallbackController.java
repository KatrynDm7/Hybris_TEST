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
package de.hybris.platform.acceleratorwebservicesaddon.controllers.v2;


import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorservices.payment.PaymentService;
import de.hybris.platform.acceleratorwebservicesaddon.payment.facade.CommerceWebServicesPaymentFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Controller to handle merchant callbacks from a subscription provider
 */
@Controller
public class MerchantCallbackController
{
	private final static Logger LOG = Logger.getLogger(MerchantCallbackController.class);

	@Resource(name = "acceleratorPaymentService")
	private PaymentService acceleratorPaymentService;
	@Resource(name = "commerceWebServicesPaymentFacade")
	protected CommerceWebServicesPaymentFacade commerceWebServicesPaymentFacade;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "cartLoaderStrategy")
	private CartLoaderStrategy cartLoaderStrategy;
	@Resource(name = "userFacade")
	protected UserFacade userFacade;
	@Resource(name = "checkoutFacade")
	CheckoutFacade checkoutFacade;

	@RequestMapping(value = "/{baseSiteId}/integration/merchant_callback", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void handleMerchantCallback(final HttpServletRequest request)
	{
		acceleratorPaymentService.handleCreateSubscriptionCallback(getParameterMap(request));
	}

	protected Map<String, String> getParameterMap(final HttpServletRequest request)
	{
		final Map<String, String> map = new HashMap<>();
		final Enumeration myEnum = request.getParameterNames();
		while (myEnum.hasMoreElements())
		{
			final String paramName = (String) myEnum.nextElement();
			final String paramValue = request.getParameter(paramName);
			map.put(paramName, paramValue);
		}
		return map;
	}

	@RequestMapping(value = "/{baseSiteId}/integration/users/{userId}/carts/{cartId}/payment/sop/response", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void handleMerchantCallback(final HttpServletRequest request, @PathVariable final String userId,
			@PathVariable final String cartId)
	{
		setCurrentUser(userId);
		loadCart(cartId);
		final Map<String, String> parameterMap = getParameterMap(request);
		final PaymentSubscriptionResultData paymentSubscriptionResultData = commerceWebServicesPaymentFacade
				.completeSopCreateSubscription(parameterMap, true);

		commerceWebServicesPaymentFacade.savePaymentSubscriptionResult(paymentSubscriptionResultData, cartId);

		if (paymentSubscriptionResultData.isSuccess() && paymentSubscriptionResultData.getStoredCard() != null
				&& StringUtils.isNotBlank(paymentSubscriptionResultData.getStoredCard().getSubscriptionId()))
		{
			final CCPaymentInfoData newPaymentSubscription = paymentSubscriptionResultData.getStoredCard();
			if (userFacade.getCCPaymentInfos(true).size() <= 1)
			{
				userFacade.setDefaultPaymentInfo(newPaymentSubscription);
			}
			checkoutFacade.setPaymentDetails(newPaymentSubscription.getId());
		}
		acceleratorPaymentService.handleCreateSubscriptionCallback(parameterMap);
	}

	protected void setCurrentUser(final String uid)
	{
		final UserModel userModel = userService.getUserForUID(uid);
		userService.setCurrentUser(userModel);
	}

	protected void loadCart(final String cartId)
	{
		cartLoaderStrategy.loadCart(cartId);
	}
}

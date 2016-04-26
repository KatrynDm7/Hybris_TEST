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
 */

package de.hybris.platform.financialacceleratorstorefront.controllers.pages;

import de.hybris.platform.acceleratorfacades.flow.impl.SessionOverrideCheckoutFlowFacade;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestRegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialacceleratorstorefront.controllers.imported.AcceleratorCheckoutController;
import de.hybris.platform.financialacceleratorstorefront.controllers.imported.AcceleratorControllerConstants;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * CheckoutController.
 */
public class CheckoutController extends AcceleratorCheckoutController
{

	protected static final Logger LOG = Logger.getLogger(CheckoutController.class);
	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	protected static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";

	@Resource(name = "orderFacade")
	OrderFacade orderFacade;

	@Override
	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String orderConfirmation(@PathVariable("orderCode") final String orderCode, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{

		final String redirect;

		// override destination of orderConfirmationPage
		SessionOverrideCheckoutFlowFacade.resetSessionOverrides();
		redirect = super.processOrderCode(orderCode, model, request);

		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		final CategoryData categoryData = orderData.getEntries().get(0).getProduct().getDefaultCategory();
		if (categoryData != null)
		{
			model.addAttribute("categoryName", categoryData.getName());
		}

		if (redirect.equals(AcceleratorControllerConstants.Views.Pages.Checkout.CheckoutLoginPage))
		{
			return ControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage;
		}
		else
		{
			return redirect;
		}

	}

	@Override
	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	public String orderConfirmation(final GuestRegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{

		final String redirect;

		// override destination of orderConfirmationPage
		super.getGuestRegisterValidator().validate(form, bindingResult);
		redirect = processRegisterGuestUserRequest(form, bindingResult, model, request, response, redirectModel);

		if (redirect.equals(AcceleratorControllerConstants.Views.Pages.Checkout.CheckoutLoginPage))
		{
			return ControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage;
		}
		else
		{
			return redirect;
		}
	}

	@ExceptionHandler(UnknownIdentifierException.class)
	public String handleUnknownIdentifierException(final UnknownIdentifierException exception, final HttpServletRequest request)
	{
		request.setAttribute("message", exception.getMessage());
		LOG.warn(exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}
}

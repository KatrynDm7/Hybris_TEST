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
package de.hybris.platform.b2ctelcostorefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.b2ctelcostorefront.controllers.TelcoControllerConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Checkout Register Controller. Handles login and register for the checkout flow.
 */
@Controller
@RequestMapping(value = "/register/checkout")
public class TelcoCheckoutRegisterController extends AbstractRegisterPageController
{
	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("checkout-register");
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		//Redirect to the main checkout controller to handle checkout.
		return "/checkout";
	}

	@Override
	protected String getView()
	{
		return TelcoControllerConstants.Views.Pages.Checkout.CheckoutRegisterPage;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doCheckoutRegister(final Model model) throws CMSItemNotFoundException
	{
		return getDefaultRegistrationPage(model);
	}

	/**
	 * Create new customer.
	 *
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/newcustomer", method = RequestMethod.POST)
	public String doCheckoutRegister(@Valid final RegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		return processRegisterUserRequest(null, form, bindingResult, model, request, response, redirectModel);
	}
}

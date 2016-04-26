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
package de.hybris.platform.financialacceleratorstorefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialacceleratorstorefront.controllers.imported.AcceleratorCheckoutLoginController;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


public class CheckoutLoginController extends AcceleratorCheckoutLoginController
{
	protected static final Logger LOG = Logger.getLogger(CheckoutLoginController.class);

	@Resource(name = "cartFacade")
	private InsuranceCartFacade cartFacade;

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public String doCheckoutLogin(@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			final HttpSession session, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		model.addAttribute("expressCheckoutAllowed", Boolean.valueOf(getCheckoutFlowFacade().isExpressCheckoutEnabledForStore()));

		LOG.debug("In the CheckoutLoginController for GET for /checkout/login");
		final CategoryData categoryData = cartFacade.getSelectedInsuranceCategory();
		if (categoryData != null)
		{
			model.addAttribute("categoryName", categoryData.getName());
		}

		return getDefaultLoginPage(loginError, session, model);
	}
}

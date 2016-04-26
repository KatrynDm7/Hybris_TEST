/*
 *
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
 */
package de.hybris.platform.financialacceleratorstorefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.financialacceleratorstorefront.constants.WebConstants;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.financialfacades.facades.InsuranceCheckoutFacade;
import de.hybris.platform.financialfacades.strategies.CustomerFormPrePopulateStrategy;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/cart")
public class ChangePlanController extends AbstractPageController
{

	private static final Logger LOG = Logger.getLogger(ChangePlanController.class);

	@Resource
	private CartFacade cartFacade;

	@Resource
	private CustomerFormPrePopulateStrategy customerFormPrePopulateStrategy;

	@Resource
	private InsuranceCheckoutFacade insuranceCheckoutFacade;

	@Resource
	private InsuranceCartFacade insuranceCartFacade;

	/**
	 * Choose cover redirect to the Product's default category page if available, or else redirect to Home.
	 */
	@RequestMapping(value = "/chooseCover", method = RequestMethod.GET)
	public String redirectToChooseCoverPage(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam(value = "viewStatus", required = false, defaultValue = "edit") final String viewStatus)
			throws CommerceCartModificationException, IOException, ServletException
	{
		final CartData sessionCart = cartFacade.getSessionCart();
		String redirectUrlOrCart = ROOT;
		if (!cartFacade.hasSessionCart() || sessionCart.getEntries().isEmpty())
		{
			LOG.info("Missing or empty cart");

			// No session cart or empty session cart. Bounce back to the home page.
			return REDIRECT_PREFIX + redirectUrlOrCart;
		}
		if (sessionCart.getEntries() != null)
		{
			for (final OrderEntryData entryData : sessionCart.getEntries())
			{
				// The redirect URL is at this stage picking from the last item in the cart.
				// This should be improved later on when we start to have multiple bundles in the cart from different
				// categories.
				final ProductData product = entryData.getProduct();
				final CategoryData categoryData = product.getDefaultCategory();
				final String queryString = StringUtils.isNotEmpty(viewStatus) ? WebConstants.QUERY_STRING_SEPERATOR
						+ WebConstants.QUERY_STRING_VIEWSTATUS + viewStatus : StringUtils.EMPTY;
				if (categoryData != null)
				{
					redirectUrlOrCart = categoryData.getUrl() + queryString;
				}
			}
		}
		return REDIRECT_PREFIX + redirectUrlOrCart;
	}

	/**
	 * Change plan that removes all the bundle in the cart as well as redirect to the Product's default category page if
	 * available, or else redirect to Cart.
	 */
	@RequestMapping(value = "/changePlan", method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String removePlanInCart(
			final RedirectAttributes redirectModel,
			@RequestParam(value = "isStoreFormData", defaultValue = "true") final Boolean isStoreFormData,
			@RequestParam(value = "redirectUrl", required = false, defaultValue = StringUtils.EMPTY) final String redirectUrl,
			@RequestParam(value = "isSaveCart", required = false, defaultValue = "false") final boolean isSaveCart,
			@RequestParam(value = "isSameInsuranceType", required = false, defaultValue = "false") final boolean isSameInsuranceType,
			final HttpServletRequest request, final HttpServletResponse response) throws CommerceCartModificationException,
			IOException, ServletException
	{
		String redirectUrlOrCart = "/cart";
		if (!cartFacade.hasSessionCart() || !cartFacade.hasEntries())
		{
			LOG.info("Missing or empty cart");

			// No session cart or empty session cart. Bounce back to the cart page.
			return REDIRECT_PREFIX + redirectUrlOrCart;
		}

		final CartData sessionCart = cartFacade.getSessionCart();

		if (Boolean.TRUE.equals(isStoreFormData))
		{
			customerFormPrePopulateStrategy.storeCustomerFormData();
		}
		else
		{
			customerFormPrePopulateStrategy.removeStoredCustomerFormData();
		}

		if (redirectUrl.isEmpty())
		{
			for (final OrderEntryData entryData : sessionCart.getEntries())
			{
				// The redirect URL is at this stage picking from the last item in the cart.
				// This should be improved later on when we start to have multiple bundles in the cart from different
				// categories.
				final ProductData product = entryData.getProduct();
				final CategoryData categoryData = product.getDefaultCategory();
				if (categoryData != null)
				{
					redirectUrlOrCart = categoryData.getUrl();
				}
			}
		}

		if (isSaveCart)
		{
			try
			{
				getInsuranceCartFacade().saveCurrentUserCart();
			}
			catch (final CommerceSaveCartException e)
			{
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"save.cart.failed.exception.message");
			}
		}

		if (isSameInsuranceType)
		{
			// For insurance products the assumption is to only have 1 bundle in the cart.
			// clear the bundle from the cart and maintain the same cart for the new product.
			getInsuranceCartFacade().removeMainBundleFromSessionCart();
		}
		else
		{
			// Make sure the cart from session has been removed from session
			getInsuranceCartFacade().removeSessionCart();
		}

		if (!redirectUrl.isEmpty())
		{
			request.getRequestDispatcher(redirectUrl).forward(request, response);
			return null;
		}
		else
		{
			return REDIRECT_PREFIX + redirectUrlOrCart;
		}
	}

	@RequestMapping(value = "/rollover/checkFormData", method = RequestMethod.GET, produces = "application/json")
	public String checkFormData(final Model model) throws CommerceCartModificationException, IOException, ServletException
	{
		model.addAttribute("hasFormData", insuranceCheckoutFacade.hasSavedFormData());
		return ControllerConstants.Views.Fragments.Cart.CheckFormData;
	}

	public CartFacade getCartFacade()
	{
		return cartFacade;
	}

	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	public CustomerFormPrePopulateStrategy getCustomerFormPrePopulateStrategy()
	{
		return customerFormPrePopulateStrategy;
	}

	public void setCustomerFormPrePopulateStrategy(final CustomerFormPrePopulateStrategy customerFormPrePopulateStrategy)
	{
		this.customerFormPrePopulateStrategy = customerFormPrePopulateStrategy;
	}

	public InsuranceCheckoutFacade getInsuranceCheckoutFacade()
	{
		return insuranceCheckoutFacade;
	}

	public void setInsuranceCheckoutFacade(final InsuranceCheckoutFacade insuranceCheckoutFacade)
	{
		this.insuranceCheckoutFacade = insuranceCheckoutFacade;
	}

	public InsuranceCartFacade getInsuranceCartFacade()
	{
		return insuranceCartFacade;
	}

	public void setInsuranceCartFacade(final InsuranceCartFacade insuranceCartFacade)
	{
		this.insuranceCartFacade = insuranceCartFacade;
	}
}

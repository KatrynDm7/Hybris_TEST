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

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateQuantityForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialacceleratorstorefront.controllers.imported.AcceleratorCartPageController;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Controller for cart page
 */
public class CartPageController extends AcceleratorCartPageController
{
	protected static final Logger LOG = Logger.getLogger(CartPageController.class);

	public static final String HIDE_CART_BILLING_EVENTS_OPTIONS = "storefront.hide.cart.billing.events";

	@Resource(name = "cartFacade")
	private InsuranceCartFacade cartFacade;

	/*
	 * Display the cart page
	 */
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public String showCart(final Model model) throws CMSItemNotFoundException, CommerceCartModificationException
	{
		LOG.debug("In the FinancialAcceleratorStoreFront GET for /cart");
		super.prepareDataForPage(model);
		final CategoryData categoryData = cartFacade.getSelectedInsuranceCategory();
		if (categoryData != null)
		{
			model.addAttribute("categoryName", categoryData.getName());
		}
		return ControllerConstants.Views.Pages.Cart.CartPage;
	}

	@Override
	protected void createProductList(final Model model) throws CMSItemNotFoundException
	{
		super.createProductList(model);

		final CartData cartData = cartFacade.getSessionCart();
		boolean hasPickUpCartEntries = false;
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				if (!hasPickUpCartEntries && entry.getDeliveryPointOfService() != null)
				{
					hasPickUpCartEntries = true;
				}
				final UpdateQuantityForm uqf = new UpdateQuantityForm();
				uqf.setQuantity(entry.getQuantity());
				model.addAttribute("updateQuantityForm" + entry.getEntryNumber(), uqf);
			}
		}

		model.addAttribute("cartData", cartData);
	}

	@ModelAttribute("hideCartBillingEvents")
	public boolean isHideCartBillingEvents()
	{
		return getSiteConfigService().getBoolean(HIDE_CART_BILLING_EVENTS_OPTIONS, true);
	}

}

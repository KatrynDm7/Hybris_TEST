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
package de.hybris.platform.storefront.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.storefront.controllers.ControllerConstants;
import de.hybris.platform.storefront.controllers.util.CartHelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Delivery method page controller.
 */
@Controller
@RequestMapping(value = "/checkout/multi/delivery-method")
public class DeliveryMethodCheckoutStepController extends AbstractCheckoutStepController
{
	private static final String DELIVERY_METHOD = "delivery-method";
	private static final String PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY = "subscription-only";

	@RequestMapping(value = "/choose", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = DELIVERY_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		cartData.setEntries(CartHelper.removeEmptyEntries(cartData.getEntries()));
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryAddress", getCheckoutFacade().getCheckoutCart().getDeliveryAddress());
		final List<? extends DeliveryModeData> supportedDeliveryMethods = getSupportedDeliveryMethods(cartData);
		model.addAttribute("selectedDeliveryMethodId", getSelectedDeliveryMethodId(cartData, supportedDeliveryMethods));
		model.addAttribute("deliveryMethods", supportedDeliveryMethods);

		this.prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryMethod.breadcrumb"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		setCheckoutStepLinksForModel(model, getCheckoutStep());

		return ControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
	}

	/**
	 * This method gets called when the "Use Selected Delivery Method" button is clicked. It sets the selected delivery
	 * mode on the checkout facade and reloads the page highlighting the selected delivery Mode.
	 *
	 * @param selectedDeliveryMethod
	 *           - the id of the delivery mode.
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectDeliveryMode(@RequestParam("delivery_method") final String selectedDeliveryMethod)
	{
		if (StringUtils.isNotEmpty(selectedDeliveryMethod))
		{
			getCheckoutFacade().setDeliveryMode(selectedDeliveryMethod);
		}

		return getCheckoutStep().nextStep();
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	/**
	 * In case a cart contains subscription products only display the delivery mode for subscriptions.
	 */
	protected List<? extends DeliveryModeData> getSupportedDeliveryMethods(final CartData cartData)
	{
		final List<? extends DeliveryModeData> deliveryMethods = getCheckoutFacade().getSupportedDeliveryModes();
		final List<DeliveryModeData> filteredDeliveryMethods = new ArrayList<DeliveryModeData>();
		final boolean isSubscriptionOnlyCart = cartContainsSubscriptionProductsOnly(cartData);

		for (final DeliveryModeData deliveryMethod : deliveryMethods)
		{
			if (isSubscriptionOnlyCart)
			{
				if (StringUtils.containsIgnoreCase(deliveryMethod.getCode(), PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY))
				{
					filteredDeliveryMethods.add(deliveryMethod);
				}
			}
			else
			{
				if (!StringUtils.containsIgnoreCase(deliveryMethod.getCode(), PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY))
				{
					filteredDeliveryMethods.add(deliveryMethod);
				}
			}
		}

		return CollectionUtils.isEmpty(filteredDeliveryMethods) ? deliveryMethods : filteredDeliveryMethods;
	}

	protected boolean cartContainsSubscriptionProductsOnly(final CartData cartData)
	{
		for (final OrderEntryData entry : cartData.getEntries())
		{
			if (entry.getProduct() != null && entry.getProduct().getSubscriptionTerm() == null)
			{
				if (entry.getDeliveryPointOfService() == null)
				{
					return false;
				}
			}
		}
		return true;
	}

	protected String getSelectedDeliveryMethodId(final CartData cartData,
			final List<? extends DeliveryModeData> supportedDeliveryModes)
	{
		String selectedDeliveryMethodId = null;

		if (cartData.getDeliveryMode() != null)
		{
			for (final DeliveryModeData supportedDeliveryMode : supportedDeliveryModes)
			{
				if (StringUtils.equals(supportedDeliveryMode.getCode(), cartData.getDeliveryMode().getCode()))
				{
					selectedDeliveryMethodId = cartData.getDeliveryMode().getCode();
					break;
				}
			}
		}

		if (StringUtils.isEmpty(selectedDeliveryMethodId))
		{
			cartData.setDeliveryMode(null);

			if (CollectionUtils.isNotEmpty(supportedDeliveryModes))
			{
				selectedDeliveryMethodId = supportedDeliveryModes.iterator().next().getCode();
			}
		}

		return selectedDeliveryMethodId;
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(DELIVERY_METHOD);
	}
}

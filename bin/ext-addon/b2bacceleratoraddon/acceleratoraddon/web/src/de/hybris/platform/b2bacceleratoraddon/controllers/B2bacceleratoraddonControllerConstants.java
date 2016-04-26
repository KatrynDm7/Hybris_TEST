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
package de.hybris.platform.b2bacceleratoraddon.controllers;

/**
 */
public interface B2bacceleratoraddonControllerConstants
{
	static final String ADDON_PREFIX = "addon:/b2bacceleratoraddon/";
	static final String STOREFRONT_PREFIX = "/";

	interface Views
	{
		interface Pages
		{
			interface MultiStepCheckout
			{
				String ChoosePaymentTypePage = ADDON_PREFIX + "pages/checkout/multi/choosePaymentTypePage";
				String CheckoutSummaryPage = ADDON_PREFIX + "pages/checkout/multi/checkoutSummaryPage";
				String AddEditDeliveryAddressPage = ADDON_PREFIX + "pages/checkout/multi/addEditDeliveryAddressPage";
				String ChooseDeliveryMethodPage = ADDON_PREFIX + "pages/checkout/multi/chooseDeliveryMethodPage";
				String AddPaymentMethodPage = ADDON_PREFIX + "pages/checkout/multi/addPaymentMethodPage";
				String SilentOrderPostPage = ADDON_PREFIX + "pages/checkout/multi/silentOrderPostPage";
				String HostedOrderPostPage = STOREFRONT_PREFIX + "pages/checkout/multi/hostedOrderPostPage";

			}

			interface Checkout
			{
				String CheckoutLoginPage = STOREFRONT_PREFIX + "pages/checkout/checkoutLoginPage";
				String QuoteCheckoutConfirmationPage = ADDON_PREFIX + "pages/checkout/quoteCheckoutConfirmationPage";
				String ReplenishmentCheckoutConfirmationPage = ADDON_PREFIX + "pages/checkout/replenishmentCheckoutConfirmationPage";
			}

		}

		interface Fragments
		{

			interface Product
			{
				String ProductLister = ADDON_PREFIX + "fragments/product/productLister";
			}
			interface Checkout
			{
				String ReadOnlyExpandedOrderForm =  ADDON_PREFIX + "fragments/checkout/readOnlyExpandedOrderForm";
				String TermsAndConditionsPopup = STOREFRONT_PREFIX + "fragments/checkout/termsAndConditionsPopup";
			}


		}
	}
}

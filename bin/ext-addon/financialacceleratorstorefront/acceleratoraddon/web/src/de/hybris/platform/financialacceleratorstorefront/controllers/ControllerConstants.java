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
package de.hybris.platform.financialacceleratorstorefront.controllers;

import de.hybris.platform.financialservices.model.components.CMSAgentRootComponentModel;
import de.hybris.platform.financialservices.model.components.CMSAllOurServicesComponentModel;
import de.hybris.platform.financialservices.model.components.CMSLinkImageComponentModel;
import de.hybris.platform.financialservices.model.components.CMSMultiComparisonTabContainerModel;
import de.hybris.platform.financialservices.model.components.CMSViewPoliciesComponentModel;
import de.hybris.platform.financialservices.model.components.CMSViewQuotesComponentModel;
import de.hybris.platform.financialservices.model.components.ComparisonPanelCMSComponentModel;


/**
 */
public interface ControllerConstants
{
	final String ADDON_PREFIX = "addon:/financialacceleratorstorefront/";

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Pages
		{
			interface Cart
			{
				String CartPage = ADDON_PREFIX + "pages/cart/cartPage";
			}

			interface Checkout
			{
				String CheckoutConfirmationPage = ADDON_PREFIX + "pages/checkout/checkoutConfirmationPage";
			}

			interface Agent
			{
				String AgentList = ADDON_PREFIX + "pages/find-agent/findAgentList";
				String ContactAgentForm = ADDON_PREFIX + "pages/find-agent/contactAgentForm";
			}
		}

		interface Fragments
		{
			interface Cart
			{
				String AddToCartFragment = ADDON_PREFIX + "fragments/cart/addToCartFragment";
				String CheckFormData = ADDON_PREFIX + "fragments/cart/checkFormData";
			}

			interface Catalog
			{
				String SwitchComparisonTabFragment = ADDON_PREFIX + "fragments/catalog/switchComparisonTabFragment";
			}
		}
	}

	interface Actions
	{
		interface Cms
		{
			String _Prefix = "/view/";
			String _Suffix = "Controller";

			/**
			 * CMS components that have specific handlers
			 */
			String CMSMultiComparisonTabContainer = _Prefix + CMSMultiComparisonTabContainerModel._TYPECODE + _Suffix;
			String CMSViewPoliciesComponent = _Prefix + CMSViewPoliciesComponentModel._TYPECODE + _Suffix;
			String CMSViewQuotesComponent = _Prefix + CMSViewQuotesComponentModel._TYPECODE + _Suffix;
			String CMSAllOurServicesComponent = _Prefix + CMSAllOurServicesComponentModel._TYPECODE + _Suffix;
			String CMSLinkImageComponent = _Prefix + CMSLinkImageComponentModel._TYPECODE + _Suffix;
			String ComparisonPanelCMSComponent = _Prefix + ComparisonPanelCMSComponentModel._TYPECODE + _Suffix;
			String CMSAgentRootComponent = _Prefix + CMSAgentRootComponentModel._TYPECODE + _Suffix;
		}
	}
}

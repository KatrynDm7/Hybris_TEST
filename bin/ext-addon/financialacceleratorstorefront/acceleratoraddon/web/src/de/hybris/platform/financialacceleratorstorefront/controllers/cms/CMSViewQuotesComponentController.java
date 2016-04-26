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
package de.hybris.platform.financialacceleratorstorefront.controllers.cms;

import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.financialfacades.facades.InsurancePolicyFacade;
import de.hybris.platform.financialfacades.facades.InsuranceQuoteFacade;
import de.hybris.platform.financialservices.model.components.CMSViewQuotesComponentModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("CMSViewQuotesComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.CMSViewQuotesComponent)
public class CMSViewQuotesComponentController extends SubstitutingCMSAddOnComponentController<CMSViewQuotesComponentModel>
{
	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "quoteFacade")
	private InsuranceQuoteFacade quoteFacade;

	@Resource(name = "cartFacade")
	private InsuranceCartFacade cartFacade;

	@Resource(name = "policyFacade")
	private InsurancePolicyFacade insurancePolicyFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final CMSViewQuotesComponentModel component)
	{
		if (getUserFacade().isAnonymousUser())
		{
			model.addAttribute("isAnonymousUser", true);
		}
		else
		{
			final CartData sessionCart = getCartFacade().getSessionCart();
			final List<CartData> cartDataList = getCartFacade().getSavedCartsForCurrentUser();
			final List<CartData> allCartDataList = new ArrayList<CartData>();

			allCartDataList.addAll(cartDataList);

			if (getCartFacade().hasEntries())
			{
				boolean foundCode = false;
				for (final CartData cartData : allCartDataList)
				{
					if (cartData.getCode().equals(sessionCart.getCode()))
					{
						foundCode = true;
						break;
					}
				}
				if (!foundCode)
				{
					allCartDataList.add(sessionCart);
				}
			}

			final List<InsuranceQuoteListingData> quotes = getQuoteFacade().sortCartListForQuoteListing(allCartDataList);
			model.addAttribute("quotes", quotes);
			model.addAttribute("numDisplayableQuotes", component.getNumberOfQuotesToDisplay());

			if (insurancePolicyFacade.getPoliciesForCurrentCustomer() == null
					|| insurancePolicyFacade.getPoliciesForCurrentCustomer().isEmpty())
			{
				model.addAttribute("policiesExists", false);
			}
			else
			{
				model.addAttribute("policiesExists", true);
			}
		}
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	protected InsuranceQuoteFacade getQuoteFacade()
	{
		return quoteFacade;
	}

	public void setQuoteFacade(final InsuranceQuoteFacade quoteFacade)
	{
		this.quoteFacade = quoteFacade;
	}

	protected InsuranceCartFacade getCartFacade()
	{
		return cartFacade;
	}

	public void setCartFacade(final InsuranceCartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}
}

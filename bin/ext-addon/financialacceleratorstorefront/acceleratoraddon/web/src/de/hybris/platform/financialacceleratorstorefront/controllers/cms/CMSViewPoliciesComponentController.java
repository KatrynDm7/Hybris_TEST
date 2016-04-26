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
package de.hybris.platform.financialacceleratorstorefront.controllers.cms;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.financialfacades.facades.InsurancePolicyFacade;
import de.hybris.platform.financialservices.model.components.CMSViewPoliciesComponentModel;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("CMSViewPoliciesComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.CMSViewPoliciesComponent)
public class CMSViewPoliciesComponentController extends SubstitutingCMSAddOnComponentController<CMSViewPoliciesComponentModel>
{
	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "policyFacade")
	private InsurancePolicyFacade insurancePolicyFacade;

	@Resource(name = "cartFacade")
	private InsuranceCartFacade cartFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final CMSViewPoliciesComponentModel component)
	{
		if (userFacade.isAnonymousUser())
		{
			model.addAttribute("isAnonymousUser", true);
		}
		else
		{
			final List<InsurancePolicyListingData> policiesForCurrentCustomer = insurancePolicyFacade
					.getPoliciesForCurrentCustomer();
			model.addAttribute("numDisplayablePolicies", component.getNumberOfPoliciesToDisplay());
			model.addAttribute("policies", policiesForCurrentCustomer);

			if (cartFacade.getSavedCartsForCurrentUser() == null || cartFacade.getSavedCartsForCurrentUser().isEmpty())
			{
				model.addAttribute("quotesExists", false);
			}
			else
			{
				model.addAttribute("quotesExists", true);
			}
		}
	}
}

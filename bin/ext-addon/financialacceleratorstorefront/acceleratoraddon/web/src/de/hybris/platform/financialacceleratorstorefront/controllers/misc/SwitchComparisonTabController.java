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
package de.hybris.platform.financialacceleratorstorefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.constants.FinancialacceleratorstorefrontConstants;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialservices.model.components.CMSComparisonTabComponentModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * SwitchComparisonTabController
 */
@Controller("SwitchComparisonTabController")
public class SwitchComparisonTabController extends AbstractController
{
	protected static final Logger LOG = Logger.getLogger(SwitchComparisonTabController.class);

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	protected static final String COMPONENT_UID_PATH_VARIABLE_PATTERN = "{componentUid:.*}";

	@RequestMapping(value = "/c/tab/" + COMPONENT_UID_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String getComparisonTab(final HttpServletRequest request, @PathVariable final String componentUid, final Model model)
			throws CMSItemNotFoundException
	{
		if (!isValidRequest(request))
		{
			model.addAttribute("isSessionExpires", true);

			return ControllerConstants.Views.Fragments.Catalog.SwitchComparisonTabFragment;
		}

		final CMSComparisonTabComponentModel component = (CMSComparisonTabComponentModel) cmsComponentService
				.getSimpleCMSComponent(componentUid);
		model.addAttribute("component", component);
		return ControllerConstants.Views.Fragments.Catalog.SwitchComparisonTabFragment;
	}

	protected boolean isValidRequest(final HttpServletRequest request)
	{
		boolean isValid = true;

		final HttpSession session = request.getSession();

		if (session.isNew() || session.getAttribute(FinancialacceleratorstorefrontConstants.TRIP_DETAILS_DESTINATION) == null)
		{
			isValid = false;
		}

		return isValid;
	}

}

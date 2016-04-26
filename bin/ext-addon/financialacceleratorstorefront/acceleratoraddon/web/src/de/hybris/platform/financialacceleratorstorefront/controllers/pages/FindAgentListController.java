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
 */

package de.hybris.platform.financialacceleratorstorefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialfacades.facades.AgentFacade;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import javax.naming.directory.InvalidAttributeValueException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for agent list.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/find-agent")
public class FindAgentListController extends AbstractPageController
{
	// CMS Pages
	private static final String AGENT_LIST_CMS_PAGE = "find-agent";
	private static final String AGENT_DATA = "agentData";

	private static final String ACTIVE_CATEGORY = "activeCategory";

	private AgentFacade agentFacade;

	@RequestMapping(method = RequestMethod.GET)
	public String showList(@RequestParam(value = ACTIVE_CATEGORY, required = false) final String activeCategory,
			@RequestParam(value = "agent", required = false) final String agentId, final Model model)
			throws CMSItemNotFoundException, YFormServiceException, InvalidAttributeValueException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(AGENT_LIST_CMS_PAGE));

		model.addAttribute(ACTIVE_CATEGORY, activeCategory);

		if (StringUtils.isNotBlank(agentId))
		{
			final AgentData agent = getAgentFacade().getAgentByUid(agentId);
			model.addAttribute(AGENT_DATA, agent);
		}

		return ControllerConstants.Views.Pages.Agent.AgentList;
	}

	public AgentFacade getAgentFacade()
	{
		return agentFacade;
	}

	@Required
	public void setAgentFacade(final AgentFacade agentFacade)
	{
		this.agentFacade = agentFacade;
	}
}

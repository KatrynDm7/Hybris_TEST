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
import de.hybris.platform.constants.FinancialacceleratorstorefrontConstants;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialfacades.facades.AgentFacade;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
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
 * Controller for contact agent form.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/contact-agent")
public class ContactAgentFormController extends AbstractPageController
{
	// CMS Pages
	private static final String CONTACT_AGENT_FORM_CMS_PAGE = "contact-agent";
	private static final String AGENT_DATA = "agentData";
	private static final String THANK_YOU = "thankyou";

	private static final String ACTIVE_CATEGORY = "activeCategory";
	private static final String SEND_STATUS = "sendStatus";
	private static final String SEND = "send";

	private YFormFacade yFormFacade;
	private SessionService sessionService;
	private AgentFacade agentFacade;

	@RequestMapping(method = RequestMethod.GET)
	public String showForm(@RequestParam(value = ACTIVE_CATEGORY, required = true) final String activeCategory,
			@RequestParam(value = "agent", required = true) final String agentId,
			@RequestParam(value = SEND_STATUS, required = false) final String viewStatus, final Model model)
			throws CMSItemNotFoundException, YFormServiceException, InvalidAttributeValueException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(CONTACT_AGENT_FORM_CMS_PAGE));

		if (SEND.equals(viewStatus))
		{
			final String dataId = sessionService.getAttribute(FinancialacceleratorstorefrontConstants.FINANCIAL_FIND_AGENT_FORM_ID);
			if (StringUtils.isNotEmpty(dataId))
			{
				final YFormDataData yfdd = getYformFacade().getYFormData(dataId, YFormDataTypeEnum.DATA);
				agentFacade.sendMail(yfdd.getContent());

				sessionService.removeAttribute(FinancialacceleratorstorefrontConstants.FINANCIAL_FIND_AGENT_FORM_ID);
				model.addAttribute(THANK_YOU, Boolean.TRUE);
			}
			else
			{
				throw new InvalidAttributeValueException(
						"Can't upload yFormData. Attribute 'financialFindAgentForm' doesn't exist in session.");
			}
		}

		if (StringUtils.isNotBlank(agentId))
		{
			final AgentData agent = getAgentFacade().getAgentByUid(agentId);
			model.addAttribute(AGENT_DATA, agent);
			model.addAttribute(ACTIVE_CATEGORY, activeCategory);
		}

		return ControllerConstants.Views.Pages.Agent.ContactAgentForm;
	}

	@Required
	protected YFormFacade getYformFacade()
	{
		return yFormFacade;
	}

	@Required
	public void setYformFacade(final YFormFacade yformFacade)
	{
		this.yFormFacade = yformFacade;
	}

	@Override
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
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

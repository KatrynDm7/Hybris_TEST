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
package de.hybris.platform.financialacceleratorstorefront.component.renderer;

import de.hybris.platform.constants.FinancialacceleratorstorefrontConstants;
import de.hybris.platform.financialfacades.facades.AgentFacade;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialfacades.strategies.impl.ContactExpertYFormPreprocessorStrategy;
import de.hybris.platform.financialservices.model.components.CMSContactExpertSubmitComponentModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Renderer for ContactExpert form to add ContactExpertForm to CMS component.
 */
public class ContactExpertSubmitComponentRenderer<C extends CMSContactExpertSubmitComponentModel> extends
		DefaultAddOnSubstitutingCMSComponentRenderer<C>
{
	private static final String AGENT = "agent";
	private static final String AGENT_DATA = "agentData";

	private YFormFacade yformFacade;
	private AgentFacade agentFacade;
	private SessionService sessionService;

	private ContactExpertYFormPreprocessorStrategy contactExpertPreprocessorStrategy;

	private static final Logger LOG = Logger.getLogger(ContactExpertSubmitComponentRenderer.class);

	/**
	 * Method to render CMS component which contains yForm.
	 *
	 * @see de.hybris.platform.acceleratorcms.component.renderer.CMSComponentRenderer
	 */
	@Override
	public void renderComponent(final PageContext pageContext, final C component) throws ServletException, IOException
	{
		final String agentStatus = pageContext.getRequest().getParameter(AGENT);

		final String applicationId = component.getApplicationId();
		final String formId = component.getFormId();

		try
		{
			if (StringUtils.isNotBlank(agentStatus))
			{
				final AgentData agent = getAgentFacade().getAgentByUid(agentStatus);
				if ((agent != null) && (agentStatus.equals(agent.getUid())))
				{
					pageContext.getSession().setAttribute(AGENT_DATA, agent);
					getSessionService().setAttribute(AGENT_DATA, agent);
				}

				final String newSessionFormDataId = getYformFacade().getNewFormDataId();
				final String formDataContent = getYformFacade().getFormDataContentTemplate(applicationId, formId);

				pageContext.getSession().setAttribute(FinancialacceleratorstorefrontConstants.FINANCIAL_FIND_AGENT_FORM_ID,
						newSessionFormDataId);
				getSessionService().setAttribute(FinancialacceleratorstorefrontConstants.FINANCIAL_FIND_AGENT_FORM_ID,
						newSessionFormDataId);

				final YFormDataData yfdd = getYformFacade().createYFormData(applicationId, formId, newSessionFormDataId,
						YFormDataTypeEnum.DATA, null, formDataContent);

				final Map<String, Object> contactExpertParams = new HashMap<>();
				contactExpertParams.put(ContactExpertYFormPreprocessorStrategy.AGENT_PARAMETER, agentStatus);

				final String content = getYformFacade().getInlineFormHtml(applicationId, formId, YFormDataActionEnum.EDIT,
						yfdd.getId(), getContactExpertPreprocessorStrategy(), contactExpertParams);

				writeContent(pageContext, content);
			}
		}
		catch (final YFormServiceException e)
		{
			LOG.error(e.getMessage(), e);
		}

		super.renderComponent(pageContext, component);
	}

	/**
	 * This method writes yForm's html content to the page
	 *
	 * @param pageContext
	 * @param value
	 * @throws IOException
	 */
	private void writeContent(final PageContext pageContext, final String value) throws IOException
	{
		pageContext.getOut().write(value);
	}

	protected YFormFacade getYformFacade()
	{
		return yformFacade;
	}

	@Required
	public void setYformFacade(final YFormFacade yformFacade)
	{
		this.yformFacade = yformFacade;
	}

	protected ContactExpertYFormPreprocessorStrategy getContactExpertPreprocessorStrategy()
	{
		return contactExpertPreprocessorStrategy;
	}

	@Required
	public void setContactExpertPreprocessorStrategy(final ContactExpertYFormPreprocessorStrategy contactExpertPreprocessorStrategy)
	{
		this.contactExpertPreprocessorStrategy = contactExpertPreprocessorStrategy;
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

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}
}

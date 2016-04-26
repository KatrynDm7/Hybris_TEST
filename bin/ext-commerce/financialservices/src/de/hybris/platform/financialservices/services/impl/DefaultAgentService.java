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
package de.hybris.platform.financialservices.services.impl;

import de.hybris.platform.financialservices.dao.InsuranceAgentDao;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.financialservices.services.AgentService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of Agent service which provides functionality to manage Agents.
 */
public class DefaultAgentService implements AgentService
{

	private InsuranceAgentDao insuranceAgentDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentModel getAgentForCode(final String uid)
	{
		if (StringUtils.isBlank(uid))
		{
			throw new IllegalArgumentException("Agent uid must not be null or empty");
		}
		final AgentModel user = insuranceAgentDao.findAgentByUid(uid);
		if (user == null)
		{
			throw new UnknownIdentifierException("Cannot find user with uid \'" + uid + "\'");
		}
		else
		{
			return user;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<AgentModel> getAgentsByCategory(final String categoryCode)
	{
		if (StringUtils.isBlank(categoryCode))
		{
			throw new IllegalArgumentException("Category code must not be null or empty");
		}
		return insuranceAgentDao.findAgentsByCategory(categoryCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<AgentModel> getAgents()
	{
		return insuranceAgentDao.findAllAgents();
	}

	@Required
	public void setInsuranceAgentDao(final InsuranceAgentDao insuranceAgentDao)
	{
		this.insuranceAgentDao = insuranceAgentDao;
	}
}

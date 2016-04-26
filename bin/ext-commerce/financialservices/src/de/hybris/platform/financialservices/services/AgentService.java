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
package de.hybris.platform.financialservices.services;


import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;

/**
 * The interface of Agent Service.
 */
public interface AgentService
{
    /**
     * Get AgentData by uid.
     *
     * @param agentUid
     *           the id of requested agent
     * @return the AgentModel
     * @throws java.lang.IllegalArgumentException if the given <code>agentUid</code> is null or empty
     * @throws UnknownIdentifierException if the agent with given <code>agentUid</code> does not exist
     */
    AgentModel getAgentForCode(final String agentUid) throws IllegalArgumentException, UnknownIdentifierException;

    /**
     * Get agents related to the category by categoryCode.
     *
     * @param categoryCode
     *           the id of category to filter agents
     * @return the List of agents related to category
     * @throws java.lang.IllegalArgumentException if the given <code>categoryCode</code> is null or empty
     */
    Collection<AgentModel> getAgentsByCategory(final String categoryCode) throws IllegalArgumentException;

    /**
     * Get all existing agents.
     *
     * @return the list of AgentModel
     */
    Collection<AgentModel> getAgents();
}

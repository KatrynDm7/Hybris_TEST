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
package de.hybris.platform.financialservices.dao;

import de.hybris.platform.financialservices.model.AgentModel;

import java.util.List;


/**
 * Agent Dao interface which provides functionality to manage Agents.
 */
public interface InsuranceAgentDao
{
    /**
     * Get AgentModel by uid.
     *
     * @param uid the id of requested agent
     *
     * @return the AgentModel
     * @throws de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
     *          if nothing was found
     * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
     *          if by the given search parameters too many models where found
     *
     */
	AgentModel findAgentByUid(String uid);

    /**
     * Get Agents for given category.
     *
     * @param categoryCode the id of given agent
     *
     * @return the List<AgentModel>
     * @throws de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
     *          if the given <code>categoryCode</code> is null or empty
     * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
     *          if by the given search parameters too many models where found
     */
	List<AgentModel> findAgentsByCategory(String categoryCode);

    /**
     * Get All existing agents.
     *
     * @return the List<AgentModel>
     * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
     *          if by the given search parameters too many models where found
     */
	List<AgentModel> findAllAgents();
}

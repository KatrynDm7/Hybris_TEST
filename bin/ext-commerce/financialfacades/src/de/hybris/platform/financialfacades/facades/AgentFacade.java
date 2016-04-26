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
package de.hybris.platform.financialfacades.facades;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;


/**
 * Facade which provides functionality to manage Agents.
 */
public interface AgentFacade
{
    /**
	 * Get AgentData by uid.
	 *
	 * @param agentUid
	 *           the id of requested agent
	 * @return the AgentData
     * @throws java.lang.IllegalArgumentException if the given <code>agentUid</code> is null or empty
	 */
	AgentData getAgentByUid(final String agentUid) throws IllegalArgumentException;

    /**
     * Get agents related to category by categoryCode.
     *
     * @param categoryCode
     *           the id of category to filter agents
     * @return the List of agents related to category
     * @throws java.lang.IllegalArgumentException if the given <code>categoryCode</code> is null or empty
     */
	List<AgentData> getAgentsByCategory(final String categoryCode)throws IllegalArgumentException;

	/**
	 * Get all existing agents.
	 *
	 * @return the list of AgentData
	 */
	List<AgentData> getAgents();

     /**
      * For the category (given by categoryCode) get all subcategories related to some (1 or more) agents.
      *
      * @param rootCategoryCode
      *             the id of the root category to get it's child categories
      *
      * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
      *            if no insurance root category (set by method <code>setRootCategoryCode</code>) can be found.
      * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
      *            if more than one category with the specified for insurance root category code and
      *            catalogVersion was found.
      * @throws IllegalArgumentException
      *            if code of insurance root category is <code>null</code>.
      */
    List<CategoryData> getCategories(final String rootCategoryCode)
            throws UnknownIdentifierException, AmbiguousIdentifierException, IllegalArgumentException;

    /**
     *  Prepare customer's request to the agent using YFormData content and start process of sending email to the agent.
     *
     * @param content
     *              the xml content of YFormData (get by method <code>YFormData.getContent</code>)
     */
    public void sendMail(String content);
}

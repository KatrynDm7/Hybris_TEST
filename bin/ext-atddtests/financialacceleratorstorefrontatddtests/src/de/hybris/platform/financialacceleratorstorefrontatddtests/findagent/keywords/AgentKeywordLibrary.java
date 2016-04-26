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
package de.hybris.platform.financialacceleratorstorefrontatddtests.findagent.keywords;

import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialfacades.facades.AgentFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class AgentKeywordLibrary extends AbstractKeywordLibrary
{
    private static final Logger LOG = Logger.getLogger(AgentKeywordLibrary.class);

    @Autowired
    public AgentFacade agentFacade;

    /**
     * Load agent from facade and check it for nullability
     *
     * @param uid
     */
    public void loadAgentByUid(final String uid)
    {
        AgentData agentData = agentFacade.getAgentByUid(uid);

        assertNotNull(agentData);
    }

    /**
     * Check inner parameter of agent - category.
     *
     * @param agentUid
     * @param categoryCode
     */
    public void loadAgentAndCheckCategory(final String agentUid, final String categoryCode)
    {
        final AgentData agentData = agentFacade.getAgentByUid(agentUid);

        assertNotNull(agentData);

        assertThat(getCategoriesCodeCollection(agentData.getCategories()), hasItem(categoryCode));
    }

    public void canLoadAgentsInCategory(String categoryCode)
    {
        List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);

        assertNotNull(agents);
    }

    public void categoryContains(String categoryCode, int number)
    {
        LOG.info("categorycode: " + categoryCode);
        LOG.info("number: " + number);
        List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);
        LOG.info("agents: " + agents);
        assertEquals(number, agents.size());
    }

    public void loadAgentsByCategoryAndVerify(String category)
    {
        System.out.print(category);
        List<AgentData> agents = agentFacade.getAgentsByCategory(category);

        assertNotNull(agents);
        assertThat(agents, not(is( Collections.<AgentData>emptyList() )));
    }

    public void loadFromAnyCategoryIsSafely(String categoryCode)
    {
        List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);
        assertNotNull(agents);
    }

    public void unknownCategoryIsEmpty(String categoryCode)
    {
        List<AgentData> agents = agentFacade.getAgentsByCategory(categoryCode);
        assertNotNull(agents);
        assertTrue(agents.isEmpty());
    }

    public void allAgentsContain(String agentUid)
    {
        List<AgentData> agents = agentFacade.getAgents();
        assertNotNull(agents);

        assertThat(getAgentUidCollection(agents), hasItem(agentUid));
    }

    public void allAgentsContainAgentWithCategory(String categoryCode)
    {
        List<AgentData> agents = agentFacade.getAgents();

        assertThat(getCategoriesCodeCollectionFromAgents(agents), hasItem(categoryCode));
    }


    protected List<String> getCategoriesCodeCollectionFromAgents(final List<AgentData> list)
    {
        final List<String> result = new ArrayList<>();

        for (AgentData ad : list)
            result.addAll(getCategoriesCodeCollection(ad.getCategories()));

        return result;
    }

    protected List<String> getCategoriesCodeCollection(final List<CategoryData> list)
    {
        final List<String> result = new ArrayList<>();

        for (CategoryData cd : list)
            result.add(cd.getCode());

        return result;
    }

    protected List<String> getAgentUidCollection(final List<AgentData> list)
    {
        final List<String> result = new ArrayList<>();

        for (AgentData cd : list)
            result.add(cd.getUid());

        return result;
    }
}

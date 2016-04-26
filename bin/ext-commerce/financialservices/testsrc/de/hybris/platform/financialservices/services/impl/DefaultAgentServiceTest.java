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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.financialservices.dao.InsuranceAgentDao;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;


/**
 * Tests for the {@link de.hybris.platform.financialservices.services.impl.DefaultAgentService}.
 */
@IntegrationTest
public class DefaultAgentServiceTest extends ServicelayerTransactionalTest
{
    private static final String FAKE_AGENT_NAME = "I.C.Lumps";
    private static final String REAL_AGENT_CODE = "ethan_brown";
    private static final String AGENT_CATEGORY_NAME = "insurances_property2";

    private static final String UNEXISTING_CATEGORY_CODE = "testCategory";
    //    private static final String AGENT_UID = "testAgent";
//
    private static final String FAKE_AGENT_UID = "fake_uid";

    final static String CODE_OF_CATEGORY_1 = "insurance_category_1";
    final static String CODE_OF_CATEGORY_2 = "insurance_category_2";
    final static String CODE_OF_CATEGORY_3 = "insurance_category_3";
    final static String CODE_OF_CATEGORY_4 = "insurance_category_4";
    final static String CODE_OF_CATEGORY_5 = "insurance_category_5";
    final static String CODE_OF_CATEGORY_6 = "insurance_category_6";

    final static String AGENT_UID_1 = "agent1";
    final static String AGENT_UID_2 = "agent2";
    final static String AGENT_UID_3 = "agent3";
    final static String AGENT_UID_4 = "agent4";
    final static String AGENT_UID_5 = "agent5";
    final static String AGENT_UID_6 = "agent6";

    @Mock
    private CategoryModel insuranceCategory1;
    @Mock
    private CategoryModel insuranceCategory2;
    @Mock
    private CategoryModel insuranceCategory3;
    @Mock
    private CategoryModel insuranceCategory4;
    @Mock
    private CategoryModel insuranceCategory5;
    @Mock
    private CategoryModel insuranceCategory6;

    private List<CategoryModel> subcategoriesOfCategory1;
    private List<CategoryModel> subcategoriesOfCategory2;
    private List<CategoryModel> subcategoriesOfCategory5;

    private List<CategoryModel> categoriesOfAgent1;
    private List<CategoryModel> categoriesOfAgent2;
    private List<CategoryModel> categoriesOfAgent3;
    private List<CategoryModel> categoriesOfAgent5;

    private List<AgentModel> agentsForCategory1;
    private List<AgentModel> agentsForCategory3;
    private List<AgentModel> agentsForCategory5;
    private List<AgentModel> agentsForCategory6;

    private List<AgentModel> allAgentsList;

    @Mock
    private AgentModel agent1;
    @Mock
    private AgentModel agent2;
    @Mock
    private AgentModel agent3;
    @Mock
    private AgentModel agent4;
    @Mock
    private AgentModel agent5;

    @Mock
    private InsuranceAgentDao agentDao;

    @Resource
    private DefaultAgentService agentService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        agentService = new DefaultAgentService();
        agentService.setInsuranceAgentDao(agentDao);

        given(insuranceCategory1.getCode()).willReturn(CODE_OF_CATEGORY_1);
        given(insuranceCategory2.getCode()).willReturn(CODE_OF_CATEGORY_2);
        given(insuranceCategory3.getCode()).willReturn(CODE_OF_CATEGORY_3);
        given(insuranceCategory4.getCode()).willReturn(CODE_OF_CATEGORY_4);
        given(insuranceCategory5.getCode()).willReturn(CODE_OF_CATEGORY_5);
        given(insuranceCategory6.getCode()).willReturn(CODE_OF_CATEGORY_6);

        subcategoriesOfCategory1 = Arrays.asList(insuranceCategory2, insuranceCategory5);
        subcategoriesOfCategory2 = Arrays.asList(insuranceCategory3, insuranceCategory4);
        subcategoriesOfCategory5 = Arrays.asList(insuranceCategory6);

        given(insuranceCategory1.getCategories()).willReturn(subcategoriesOfCategory1);
        given(insuranceCategory2.getCategories()).willReturn(subcategoriesOfCategory2);
        given(insuranceCategory3.getCategories()).willReturn(Collections.EMPTY_LIST);
        given(insuranceCategory4.getCategories()).willReturn(Collections.EMPTY_LIST);
        given(insuranceCategory5.getCategories()).willReturn(subcategoriesOfCategory5);
        given(insuranceCategory6.getCategories()).willReturn(Collections.EMPTY_LIST);

        categoriesOfAgent1 = Arrays.asList(insuranceCategory3, insuranceCategory5);
        categoriesOfAgent2 = Arrays.asList(insuranceCategory1);
        categoriesOfAgent3 = Arrays.asList(insuranceCategory6);
        categoriesOfAgent5 = Arrays.asList(insuranceCategory6);

        given(agent1.getUid()).willReturn(AGENT_UID_1);
        given(agent2.getUid()).willReturn(AGENT_UID_2);
        given(agent3.getUid()).willReturn(AGENT_UID_3);
        given(agent4.getUid()).willReturn(AGENT_UID_4);
        given(agent5.getUid()).willReturn(AGENT_UID_5);

        given(agent1.getCategories()).willReturn(categoriesOfAgent1);
        given(agent2.getCategories()).willReturn(categoriesOfAgent2);
        given(agent3.getCategories()).willReturn(categoriesOfAgent3);
        given(agent4.getCategories()).willReturn(Collections.EMPTY_LIST);
        given(agent5.getCategories()).willReturn(categoriesOfAgent5);

        allAgentsList = Arrays.asList(agent1, agent2, agent3, agent4, agent5);

        agentsForCategory1 = Arrays.asList(agent2);
        agentsForCategory3 = Arrays.asList(agent1);
        agentsForCategory5 = Arrays.asList(agent1);
        agentsForCategory6 = Arrays.asList(agent3, agent5);

        given(agentDao.findAgentByUid(AGENT_UID_1)).willReturn(agent1);
        given(agentDao.findAgentByUid(AGENT_UID_2)).willReturn(agent2);
        given(agentDao.findAgentByUid(AGENT_UID_3)).willReturn(agent3);
        given(agentDao.findAgentByUid(AGENT_UID_4)).willReturn(agent4);
        given(agentDao.findAgentByUid(AGENT_UID_5)).willReturn(agent5);

        given(agentDao.findAgentsByCategory(CODE_OF_CATEGORY_1)).willReturn(agentsForCategory1);
        given(agentDao.findAgentsByCategory(CODE_OF_CATEGORY_2)).willReturn(Collections.EMPTY_LIST);
        given(agentDao.findAgentsByCategory(CODE_OF_CATEGORY_3)).willReturn(agentsForCategory3);
        given(agentDao.findAgentsByCategory(CODE_OF_CATEGORY_4)).willReturn(Collections.EMPTY_LIST);
        given(agentDao.findAgentsByCategory(CODE_OF_CATEGORY_5)).willReturn(agentsForCategory5);
        given(agentDao.findAgentsByCategory(CODE_OF_CATEGORY_6)).willReturn(agentsForCategory6);

    }

    @Test
    public void testGetAgentByNullUid()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Agent uid must not be null or empty");
        agentService.getAgentForCode(null);
    }

    @Test
    public void testGetAgentByEmptyUid()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Agent uid must not be null or empty");
        agentService.getAgentForCode("");
    }

    @Test
    public void testGetAgentByWrongUid()
    {
        thrown.expect(UnknownIdentifierException.class);
        thrown.expectMessage("Cannot find user with uid \'" + FAKE_AGENT_UID + "\'");
        agentService.getAgentForCode(FAKE_AGENT_UID);
    }

    @Test
    public void testGetAgentByDuplicateUid()
    {
        final String exceptionMessage = "More then one model with uid='" + AGENT_UID_3 + "' was found!";
        given(agentDao.findAgentByUid(AGENT_UID_3)).willThrow(new AmbiguousIdentifierException(exceptionMessage));

        thrown.expect(AmbiguousIdentifierException.class);
        thrown.expectMessage(exceptionMessage);

        agentService.getAgentForCode(AGENT_UID_3);
    }

    @Test
    public void testGetAgentByUid()
    {
        final AgentModel agent = agentService.getAgentForCode(AGENT_UID_3);

        Assert.assertNotNull(agent);
        Assert.assertNotNull(agent.getUid());
        Assert.assertEquals("Wrong agent uid!", AGENT_UID_3, agent.getUid());
    }

    @Test
    public void testGetAgentsByCategoryWhenNoAgents()
    {
        final Collection<AgentModel> agents = agentService.getAgentsByCategory(CODE_OF_CATEGORY_4);

        Assert.assertNotNull(agents);
        Assert.assertTrue("Wrong agents count!", agents.isEmpty());
    }

    @Test
    public void testGetAgentsByCategoryWhenOneAgent()
    {
        final Collection<AgentModel> agents = agentService.getAgentsByCategory(CODE_OF_CATEGORY_1);

        Assert.assertNotNull(agents);
        Assert.assertTrue("Wrong agents count!", !agents.isEmpty());
        for(final AgentModel currentAgent: agents)
        {
            Assert.assertNotNull(currentAgent);

            Assert.assertNotNull(currentAgent.getUid());
            Assert.assertNotNull(currentAgent.getCategories());
            Assert.assertFalse("Wrong categories count!", currentAgent.getCategories().isEmpty());

            boolean isCategoryFound = false;
            for(final CategoryModel currentCategory: currentAgent.getCategories())
            {
                Assert.assertNotNull(currentCategory);
                Assert.assertNotNull(currentCategory.getCode());
                if(CODE_OF_CATEGORY_1.equals(currentCategory.getCode()))
                {
                    isCategoryFound = true;
                }
            }
            Assert.assertTrue("No given category!", isCategoryFound);
        }
    }

    @Test
    public void testGetAgentsByCategoryWhenTwoAgents()
    {
        final Collection<AgentModel> agents = agentService.getAgentsByCategory(CODE_OF_CATEGORY_6);

        Assert.assertNotNull(agents);
        Assert.assertTrue("Wrong agents count!", !agents.isEmpty());
        for(final AgentModel currentAgent: agents)
        {
            Assert.assertNotNull(currentAgent);

            Assert.assertNotNull(currentAgent.getUid());
            Assert.assertNotNull(currentAgent.getCategories());
            Assert.assertTrue("Wrong categories count!",!currentAgent.getCategories().isEmpty());

            boolean isCategoryFound = false;
            for(final CategoryModel currentCategory: currentAgent.getCategories())
            {
                Assert.assertNotNull(currentCategory);
                Assert.assertNotNull(currentCategory.getCode());
                if(CODE_OF_CATEGORY_6.equals(currentCategory.getCode()))
                {
                    isCategoryFound = true;
                }
            }
            Assert.assertTrue("No given category!", isCategoryFound);
        }
    }

    @Test
    public void testGetAgentsByNullCategory()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Category code must not be null or empty");
        final Collection<AgentModel> agents = agentService.getAgentsByCategory(null);
    }

    @Test
    public void testGetAgentsByEmptyCategory()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Category code must not be null or empty");
        final Collection<AgentModel> agents = agentService.getAgentsByCategory("");
    }

    @Test
    public void testGetAgentsByWrongCategory()
    {
        final Collection<AgentModel> allAgents = agentService.getAgentsByCategory(UNEXISTING_CATEGORY_CODE);
        assertNotNull(allAgents);
        assertTrue("Category with code='" + UNEXISTING_CATEGORY_CODE + "' does not exist! Empty List should be returned!", allAgents.isEmpty());
    }

    @Test
    public void testGetAllAgentsWhenNoAgents()
    {
        given(agentDao.findAllAgents()).willReturn(Collections.EMPTY_LIST);
        final Collection<AgentModel> allAgents = agentService.getAgents();
        assertNotNull(allAgents);
        assertTrue("No agents should be return!", allAgents.isEmpty());
    }

    @Test
    public void testGetAllAgents()
    {
        given(agentDao.findAllAgents()).willReturn(allAgentsList);
        final Collection<AgentModel> allAgents = agentService.getAgents();
        assertNotNull(allAgents);
        assertFalse("List of agents should not be empty!", allAgents.isEmpty());
        assertEquals("Wrong agents number!", allAgentsList.size(), allAgents.size());
    }
}
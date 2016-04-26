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
package de.hybris.platform.financialservices.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.financialservices.dao.InsuranceAgentDao;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test of Insurance Agent Dao.
 */

@IntegrationTest
public class DefaultInsuranceAgentDaoIntegrationTest extends ServicelayerTest
{
    private static final String AGENT_CATEGORY_NAME = "insurances_property";
    private static final String FAKE_AGENT_UID = "FAKE_UID";
    private static final String AGENT_UID = "agent2";

    private static final String CODE_OF_CATEGORY_1 = "category1";
    private static final String CODE_OF_CATEGORY_3 = "category3";
    private static final String CODE_OF_CATEGORY_4 = "category4";
    private static final String CODE_OF_CATEGORY_5 = "category5";
    private static final String CODE_OF_CATEGORY_6 = "category6";

    private static final String EXTRA_AGENT_UID = "test.test@test.com";

    private static final String ERROR_MESSAGE = "All agents must be returned!";

    private static Logger LOG = Logger.getLogger(DefaultInsuranceAgentDaoIntegrationTest.class);
    @Resource
    private InsuranceAgentDao insuranceAgentDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
        try
        {
            LOG.info("Creating data for Express Checkout Integration Test ..");
            importCsv("/financialstore/test/testEssentialData.impex", "utf-8");
            importCsv("/financialstore/import/coredata/productCatalogs/financialProductCatalog/catalog.impex", "utf-8");
            importCsv("/financialstore/import/sampledata/productCatalogs/financialProductCatalog/categories.impex", "utf-8");
            importCsv("/financialstore/test/testRegions.impex", "utf-8");
            importCsv("/financialstore/import/sampledata/stores/insurance/points-of-service.impex", "utf-8");
            importCsv("/financialstore/test/testAgents.impex", "utf-8");
        }
        catch(final ImpExException e)
        {
            Assert.fail("Adding impex data failed!");
        }
    }

    @Test
    public void getAgentByEmptyUidTest()
    {
        thrown.expect(ModelNotFoundException.class);
        thrown.expectMessage("No result for the given example [AgentModel (<unsaved>)] was found. Searched with these attributes: {uid=}");
        insuranceAgentDao.findAgentByUid("");
    }

    @Test
    public void getAgentByNullUidTest()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Agent uid must not be null");
        insuranceAgentDao.findAgentByUid(null);
    }

    @Test
    public void getAllAgentsTest()
    {
        final List<AgentModel> allAgents = insuranceAgentDao.findAllAgents();
        Assert.assertNotNull(allAgents);
        Assert.assertFalse(ERROR_MESSAGE, allAgents.isEmpty());

        final Set<AgentModel> firstAttemptSet = new HashSet<>(allAgents);

        Assert.assertFalse(firstAttemptSet.isEmpty());
        final int firstAttemptCount = firstAttemptSet.size();

        try
        {
            importCsv("/financialstore/test/testExtraAgent.impex", "utf-8");
        }
        catch(final ImpExException e)
        {
            Assert.fail("Adding new agent via impex failed!");
        }

        final List<AgentModel> allAgents2 = insuranceAgentDao.findAllAgents();
        Assert.assertNotNull(allAgents2);

        final Set<AgentModel> secondAttemptSet = new HashSet<>(allAgents2);
        Assert.assertFalse(ERROR_MESSAGE, secondAttemptSet.isEmpty());
        Assert.assertTrue(firstAttemptCount + 1 == secondAttemptSet.size());

        firstAttemptSet.removeAll(new HashSet<>(allAgents2));
        Assert.assertTrue(ERROR_MESSAGE, firstAttemptSet.isEmpty());

        secondAttemptSet.removeAll(new HashSet<>(allAgents));
        Assert.assertTrue(ERROR_MESSAGE, secondAttemptSet.size() == 1);

        if(secondAttemptSet.iterator().hasNext()) {
            final AgentModel extraAgent = secondAttemptSet.iterator().next();
            Assert.assertNotNull("Agent uid must not be NULL!", extraAgent.getUid());
            Assert.assertEquals("Wrong agent uid value.", EXTRA_AGENT_UID, extraAgent.getUid());
        }
        else
        {
            Assert.fail("The last agent must exist");
        }

    }

    @Test
    public void getAgentByWrongUidTest()
    {
        thrown.expect(ModelNotFoundException.class);
        thrown.expectMessage("No result for the given example [AgentModel (<unsaved>)] was found. Searched with these attributes: {uid=" + FAKE_AGENT_UID + "}");
        insuranceAgentDao.findAgentByUid(FAKE_AGENT_UID);

    }

    @Test
    public void getAgentByCorrectUidTest()
    {
        final AgentModel returned =  insuranceAgentDao.findAgentByUid(AGENT_UID);

        Assert.assertNotNull(returned);
        Assert.assertNotNull(returned.getUid());
        Assert.assertEquals("Wrong agent uid!", AGENT_UID, returned.getUid());
    }

    @Test
    public void getAgentsByEmptyCategoryCodeTest()
    {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Category code must not be null or empty");
        final List<AgentModel> agentByUID = insuranceAgentDao.findAgentsByCategory("");
        Assert.assertNotNull(agentByUID);

    }

    @Test
    public void getAgentsByCorrectCategoryWhenCategoryIsRootTest()
    {
        final List<AgentModel> allAgents = insuranceAgentDao.findAgentsByCategory(CODE_OF_CATEGORY_1);
        Assert.assertNotNull(allAgents);
        Assert.assertFalse("List of agents for category " + CODE_OF_CATEGORY_1 + " should not be empty", allAgents.isEmpty());
        Assert.assertTrue(allAgents.size() == 1);
    }

    @Test
    public void getAgentsByCorrectCategoryWhenCategoryIsLeafTest()
    {
        final List<AgentModel> allAgents = insuranceAgentDao.findAgentsByCategory(CODE_OF_CATEGORY_6);
        Assert.assertNotNull(allAgents);
        Assert.assertFalse("List of agents for category " + CODE_OF_CATEGORY_6 + " should not be empty", allAgents.isEmpty());
        Assert.assertTrue(allAgents.size() == 2);
    }

    @Test
    public void getAgentsByCorrectCategoryWhenCategoryIsBranchTest()
    {
        final List<AgentModel> allAgents = insuranceAgentDao.findAgentsByCategory(CODE_OF_CATEGORY_5);
        Assert.assertNotNull(allAgents);
        Assert.assertFalse("List of agents for category " + CODE_OF_CATEGORY_5 + " should not be empty", allAgents.isEmpty());
        Assert.assertTrue(allAgents.size() == 1);
    }


    @Test
    public void getAgentsByWrongCategoryCodeTest()
    {
        final List<AgentModel> allAgents = insuranceAgentDao.findAgentsByCategory(AGENT_CATEGORY_NAME);
        Assert.assertNotNull(allAgents);
        Assert.assertTrue("Category with code='" + AGENT_CATEGORY_NAME + "' does not exist! Empty List should be returned!", allAgents.isEmpty());
    }

    @Test
    public void getAgentsByCategoryCodeWhenAgentHasFewCategoriesTest()
    {
        final List<AgentModel> allAgents = insuranceAgentDao.findAgentsByCategory(CODE_OF_CATEGORY_3);
        Assert.assertNotNull(allAgents);
        Assert.assertFalse("List of agents for category " + CODE_OF_CATEGORY_3 + " should not be empty", allAgents.isEmpty());
        Assert.assertTrue(allAgents.size() == 1);
    }

    @Test
    public void getAgentsByCategoryCodeWhenNoAgentsForThisCategoryTest()
    {
        final List<AgentModel> allAgents = insuranceAgentDao.findAgentsByCategory(CODE_OF_CATEGORY_4);
        Assert.assertNotNull(allAgents);
        Assert.assertTrue("There is no any agents for this category! Empty List should be returned!", allAgents.isEmpty());
    }
}
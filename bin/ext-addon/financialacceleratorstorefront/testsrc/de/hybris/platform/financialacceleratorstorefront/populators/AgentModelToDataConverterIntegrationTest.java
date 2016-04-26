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
package de.hybris.platform.financialacceleratorstorefront.populators;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Resource;

/**
 * Test for AgentModel to AgentData Converter.
 */

@IntegrationTest
public class AgentModelToDataConverterIntegrationTest extends ServicelayerTest
{
    private static final String AGENT_UID = "test_agent";

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PHONE = "(123) 456-78-90";
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Smith";
    private static final String TEST_NAME = TEST_FIRST_NAME + " " + TEST_LAST_NAME;

    @Resource
    private Converter<AgentModel, AgentData> agentConverter;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConvert()
    {
        final AgentModel agentModel = new AgentModel();
        agentModel.setUid(AGENT_UID);
        agentModel.setName(TEST_FIRST_NAME + " " + TEST_LAST_NAME);

        AddressModel testAddressData = new AddressModel();
        testAddressData.setEmail(TEST_EMAIL);
        testAddressData.setPhone1(TEST_PHONE);
        testAddressData.setFirstname(TEST_FIRST_NAME);
        testAddressData.setLastname(TEST_LAST_NAME);
        agentModel.setEnquiry(testAddressData);

        final AgentData result = agentConverter.convert(agentModel);

        Assert.assertNotNull(result);
        Assert.assertEquals(AGENT_UID, result.getUid());
        Assert.assertEquals(TEST_FIRST_NAME, result.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, result.getLastName());
        Assert.assertEquals(TEST_NAME, result.getName());

        final AddressData resultEnquiryData = result.getEnquiryData();
        Assert.assertNotNull(resultEnquiryData);
        Assert.assertNotNull(resultEnquiryData.getEmail());
        Assert.assertNotNull(resultEnquiryData.getPhone());
        Assert.assertNotNull(resultEnquiryData.getFirstName());
        Assert.assertNotNull(resultEnquiryData.getLastName());
    }
}
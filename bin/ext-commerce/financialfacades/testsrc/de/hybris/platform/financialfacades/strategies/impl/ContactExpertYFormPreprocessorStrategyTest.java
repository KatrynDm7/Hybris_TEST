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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.financialfacades.strategies.impl.ContactExpertYFormPreprocessorStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;


/**
 * Tests for the {@link de.hybris.platform.financialacceleratorstorefront.strategies.impl.ContactExpertYFormPreprocessorStrategy}.
 */
@UnitTest
public class ContactExpertYFormPreprocessorStrategyTest
{
    private static final String EXTRA_PARAMETER = "/form/contact-expert/test";
    private static final String EXTRA_VALUE = "extraValue";

    private static final String AGENT_PARAMETER = "/form/contact-expert/agent";
    private static final String AGENT_VALUE = "agent@test.com";

    private static final String XML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><contact-expert><agent/><name/><email-address/><phone/><call-back/><interest/><message/></contact-expert></form>";
    private static final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><contact-expert><agent>" + AGENT_VALUE +"</agent><name/><email-address/><phone/><call-back/><interest/><message/></contact-expert></form>";
    private static final String WRONG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><contact-expert><name/><email-address/><phone/><call-back/><interest/><message/></contact-expert></form>";

    private ContactExpertYFormPreprocessorStrategy contactExpertYFormStrategy;

    @Before
    public void setUp()
    {
        contactExpertYFormStrategy= new ContactExpertYFormPreprocessorStrategy();
    }

    @Test
	public void transformTestXmlNull()
	{
        final HashMap<String, Object> params = new HashMap<>();
        params.put(AGENT_PARAMETER, AGENT_VALUE);
        final String resultXml = contactExpertYFormStrategy.transform(null, params);
        Assert.assertNull(resultXml);
    }

    @Test
    public void transformTestXmlEmpty()
    {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(EXTRA_PARAMETER, EXTRA_VALUE);
        params.put(AGENT_PARAMETER, AGENT_VALUE);
        final String emptyString= "";
        final String resultXmlContent = contactExpertYFormStrategy.transform(emptyString, params);
        Assert.assertNotNull(resultXmlContent);
        Assert.assertEquals(emptyString, resultXmlContent);
    }

    @Test
    public void transformTestXmlWrong()
    {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(EXTRA_PARAMETER, AGENT_VALUE);
        params.put(AGENT_PARAMETER, AGENT_VALUE);
        final String resultXmlContent = contactExpertYFormStrategy.transform(WRONG_XML, params);
        Assert.assertNotNull(resultXmlContent);
        Assert.assertEquals(WRONG_XML, resultXmlContent);
    }

    @Test
    public void transformTestParamsNull()
    {
        final String resultXmlContent = contactExpertYFormStrategy.transform(XML_CONTENT, null);
        Assert.assertNotNull(resultXmlContent);
        Assert.assertEquals(XML_CONTENT, resultXmlContent);
    }

    @Test
    public void transformTestParamsEmpty()
    {
        final HashMap<String, Object> params = new HashMap<>();
        final String resultXmlContent = contactExpertYFormStrategy.transform(XML_CONTENT, params);
        Assert.assertNotNull(resultXmlContent);
        Assert.assertEquals(XML_CONTENT, resultXmlContent);
    }

    @Test
    public void transformTestParamsNoKey()
    {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(EXTRA_PARAMETER, AGENT_VALUE);
        final String resultXmlContent = contactExpertYFormStrategy.transform(XML_CONTENT, params);
        Assert.assertNotNull(resultXmlContent);
        Assert.assertEquals(XML_CONTENT, resultXmlContent);
    }

    @Test
    public void transformTestParamsExtraKey()
    {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(EXTRA_PARAMETER, AGENT_VALUE);
        params.put(AGENT_PARAMETER, AGENT_VALUE);
        final String resultXmlContent = contactExpertYFormStrategy.transform(XML_CONTENT, params);
        Assert.assertNotNull(resultXmlContent);
        Assert.assertEquals(EXPECTED_XML, resultXmlContent);
    }

    @Test
    public void transformTestParamsSingleKey()
    {
        final HashMap<String, Object> params = new HashMap<>();
        params.put(AGENT_PARAMETER, AGENT_VALUE);
        final String resultXmlContent = contactExpertYFormStrategy.transform(XML_CONTENT, params);
        Assert.assertNotNull(resultXmlContent);
        Assert.assertEquals(EXPECTED_XML, resultXmlContent);
    }
}

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
package de.hybris.platform.financialfacades.facades.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.insurance.data.PolicyItemRequestData;
import de.hybris.platform.commercefacades.insurance.data.PolicyRequestData;
import de.hybris.platform.commercefacades.insurance.data.PolicyResponseData;
import de.hybris.platform.financialfacades.facades.PolicyServiceFacade;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * The class of MockPolicyServiceFacadeIntegrationTest {@link MockPolicyServiceFacade}.
 */
@IntegrationTest
public class MockPolicyServiceFacadeIntegrationTest extends ServicelayerTest
{
	@Resource
	private PolicyServiceFacade policyServiceFacade;

	@Before
	public void setUp()
	{
		// implement setup. 
	}

	@Test
	public void testPolicyCreationRequest()
	{
		final String requestId = "1234567890";
		final String propertyKey = "key";
		final String propertyValue = "value";
		final Map<String, String> properties = Maps.newHashMap();
		properties.put(propertyKey, propertyValue);
		final PolicyRequestData requestData = new PolicyRequestData();
		requestData.setItems(Lists.<PolicyItemRequestData> newArrayList());

		final PolicyItemRequestData item = new PolicyItemRequestData();
		item.setId(requestId);
		item.setProperties(properties);

		requestData.getItems().add(item);

		final PolicyResponseData responseData = policyServiceFacade.requestPolicyCreation(requestData);

		assertEquals(requestId, responseData.getItems().get(0).getId());
		assertEquals(propertyValue, responseData.getItems().get(0).getProperties().get(propertyKey));
	}
}

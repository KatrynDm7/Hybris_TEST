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
package de.hybris.platform.financialservices.findagent.data;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.financialfacades.findagent.data.AgentData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for the {@link de.hybris.platform.financialfacades.findagent.data.AgentData}.
 */
@UnitTest
public class AgentDataTest
{

	private static final String CATEGORY_CODE = "testCategory";
	private static final String TEST_EMAIL = "test@test.com";
	private static final String TEST_PHONE = "(123) 456-78-90";

	private AgentData agentData;

	@Before
	public void setUp()
	{
		agentData = new AgentData();
	}

	@Test
	public void testCategories()
	{
		final List<CategoryData> emptyAgentCategories = agentData.getCategories();
		Assert.assertNull(emptyAgentCategories);

		final CategoryData testCategory = new CategoryData();
		testCategory.setCode(CATEGORY_CODE);

		final List<CategoryData> testCategories = new ArrayList<>();
		testCategories.add(testCategory);

		agentData.setCategories(testCategories);

		final List<CategoryData> agentCategories = agentData.getCategories();
		Assert.assertNotNull(agentCategories);
		Assert.assertEquals("Wrong categories count!", testCategories.size(), agentCategories.size());

		final CategoryData categoryData = agentCategories.get(0);
		Assert.assertNotNull(categoryData);
		Assert.assertEquals("Wrong agent's category!", testCategory, categoryData);
	}

	@Test
	public void testEnquiryData()
	{
		final String emptyEmail = agentData.getUid();
		Assert.assertNull(emptyEmail);

		final AddressData testAddressData = new AddressData();
		testAddressData.setEmail(TEST_EMAIL);
		testAddressData.setPhone(TEST_PHONE);
		agentData.setEnquiryData(testAddressData);

		Assert.assertNotNull(agentData.getEnquiryData());
		Assert.assertEquals("Wrong agent's enquiry data", testAddressData, agentData.getEnquiryData());
	}
}

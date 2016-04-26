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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.financialfacades.populators.PropertyInsuranceDetailsPopulator;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;


public class PropertyInsuranceDetailsPopulatorTest
{
	private PropertyInsuranceDetailsPopulator propertyInsuranceDetailsPopulator;

	@Before
	public void setup()
	{
		propertyInsuranceDetailsPopulator = new PropertyInsuranceDetailsPopulator();
	}

	@Test
	public void testPopulate()
	{
		final String firstName = "firstName";
		final String lastName = "lastName";

		final String formContentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><holder-details-section>" + "<first-name>"
				+ firstName + "</first-name><last-name>" + lastName + "</last-name></holder-details-section></form>";

		final YFormDataData formDataData = new YFormDataData();
		formDataData.setContent(formContentXML);
		InsurancePolicyData detailData = new InsurancePolicyData();
		propertyInsuranceDetailsPopulator.populate(formDataData, detailData);
		Assert.assertEquals(firstName, detailData.getPolicyHolderDetail().getFirstName());
		Assert.assertEquals(lastName, detailData.getPolicyHolderDetail().getLastName());
	}
}

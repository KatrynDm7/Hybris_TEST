/*
 *
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
 */
package de.hybris.platform.financialfacades.populators;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;
import de.hybris.platform.financialservices.model.InsurancePolicyModel;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class InsurancePolicyListPopulatorTest
{
	public static final String TEST_ID = "ID-1010101";
	public static final Date TEST_DATE_1 = new Date(0);
	public static final Date TEST_DATE_2 = new Date(5000);
	public static final String TEST_URL = "http://localhost/pdf";

	@InjectMocks
	private InsurancePolicyListPopulator<InsurancePolicyModel, InsurancePolicyListingData> defaultInsuranceProductPopulator;

	@Mock
	private InsurancePolicyModel insurancePolicyModel;

	@Before
	public void setup()
	{
		defaultInsuranceProductPopulator = new InsurancePolicyListPopulator<>();
		defaultInsuranceProductPopulator.setDateFormatForDisplay("dd-mm-yyyy");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPopulatorWorks()
	{
		Assert.assertEquals(defaultInsuranceProductPopulator.getDateFormatForDisplay(), "dd-mm-yyyy");

		given(insurancePolicyModel.getPolicyId()).willReturn(TEST_ID);
		given(insurancePolicyModel.getPolicyStartDate()).willReturn(TEST_DATE_1);
		given(insurancePolicyModel.getPolicyExpiryDate()).willReturn(TEST_DATE_2);
		given(insurancePolicyModel.getPolicyUrl()).willReturn(TEST_URL);

		final InsurancePolicyListingData policyData = new InsurancePolicyListingData();

		defaultInsuranceProductPopulator.populate(insurancePolicyModel, policyData);

		Assert.assertNotNull(policyData);
		Assert.assertEquals(policyData.getPolicyNumber(), TEST_ID);
		Assert.assertEquals(policyData.getPolicyRawStartDate(), TEST_DATE_1);
		Assert.assertEquals(policyData.getPolicyRawExpiryDate(), TEST_DATE_2);
		Assert.assertEquals(policyData.getPolicyStartDate(), "01-00-1970");
		Assert.assertEquals(policyData.getPolicyExpiryDate(), "01-00-1970");
		Assert.assertEquals(policyData.getPolicyUrl(), TEST_URL);
	}
}

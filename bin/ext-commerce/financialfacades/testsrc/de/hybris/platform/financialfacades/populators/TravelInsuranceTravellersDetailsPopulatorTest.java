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

import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData;
import de.hybris.platform.financialfacades.populators.TravelInsuranceTravellersDetailsPopulator;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * The class of TravelInsuranceTravellersDetailsPopulatorTest.
 */
public class TravelInsuranceTravellersDetailsPopulatorTest
{
	private TravelInsuranceTravellersDetailsPopulator travellersDetailsPopulator;

	@Before
	public void setup()
	{
		travellersDetailsPopulator = new TravelInsuranceTravellersDetailsPopulator();
	}

	@Test
	public void testPopulate()
	{
		final String firstName = "firstName";
		final String firstName1 = "firstName1";
		final String firstName2 = "firstName2";
		final String lastName = "lastName";
		final String lastName1 = "lastName1";
		final String lastName2 = "lastName2";
		final String age32 = "32";
		final String age33 = "33";
		final String age34 = "34";
		final int noOfTravellers = 3;

		final String formContentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><personal-details>" + "<first-name>"
				+ firstName + "</first-name><last-name>" + lastName + "</last-name><age>" + age32 + "</age>"
				+ "<phone>441234567890</phone><email>test@test.com</email>"
				+ "<address-line1></address-line1><address-line2></address-line2>"
				+ "<address-city></address-city><address-postcode></address-postcode>"
				+ "<address-country></address-country></personal-details>" + "<number-of-travellers>" + noOfTravellers
				+ "</number-of-travellers><traveller-1>" + "<first-name-1>" + firstName1 + "</first-name-1><last-name-1>" + lastName1
				+ "</last-name-1><age-1>" + age33 + "</age-1>" + "</traveller-1><traveller-2><first-name-2>" + firstName2
				+ "</first-name-2><last-name-2>" + lastName2 + "</last-name-2>" + "<age-2>" + age34
				+ "</age-2></traveller-2><traveller-3><first-name-3></first-name-3>"
				+ "<last-name-3></last-name-3><age-3></age-3></traveller-3><traveller-4>"
				+ "<first-name-4></first-name-4><last-name-4></last-name-4><age-4></age-4>"
				+ "</traveller-4><traveller-5><first-name-5/><last-name-5/><age-5/>"
				+ "</traveller-5><traveller-6><first-name-6/><last-name-6/><age-6/>"
				+ "</traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/>"
				+ "</traveller-8><traveller-9><first-name-9/><last-name-9/><age-9/>" + "</traveller-9></form>";
		final YFormDataData formDataData = new YFormDataData();
		formDataData.setContent(formContentXML);

		final InsuranceQuoteReviewData detailData = new InsuranceQuoteReviewData();
		travellersDetailsPopulator.populate(formDataData, detailData);

		Assert.assertEquals(noOfTravellers, detailData.getTravellers().size());

		Assert.assertEquals(firstName, detailData.getTravellers().get(0).getFirstName());
		Assert.assertEquals(lastName, detailData.getTravellers().get(0).getLastName());
		Assert.assertEquals(age32, detailData.getTravellers().get(0).getAge().toString());

		Assert.assertEquals(firstName1, detailData.getTravellers().get(1).getFirstName());
		Assert.assertEquals(lastName1, detailData.getTravellers().get(1).getLastName());
		Assert.assertEquals(age33, detailData.getTravellers().get(1).getAge().toString());

		Assert.assertEquals(firstName2, detailData.getTravellers().get(2).getFirstName());
		Assert.assertEquals(lastName2, detailData.getTravellers().get(2).getLastName());
		Assert.assertEquals(age34, detailData.getTravellers().get(2).getAge().toString());
	}
}

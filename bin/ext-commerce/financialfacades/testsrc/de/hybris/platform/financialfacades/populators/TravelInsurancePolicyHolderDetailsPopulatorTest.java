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

import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import org.junit.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * The class of TravelInsurancePolicyHolderDetailsPopulatorTest.
 */
public class TravelInsurancePolicyHolderDetailsPopulatorTest
{
	private TravelInsurancePolicyHolderDetailsPopulator policyHolderDetailsPopulator;

	private I18NFacade i18NFacade;

	@Before
	public void setup()
	{
		policyHolderDetailsPopulator = new TravelInsurancePolicyHolderDetailsPopulator();
		i18NFacade = Mockito.mock(I18NFacade.class);
		policyHolderDetailsPopulator.setI18NFacade(i18NFacade);
	}

	@Test
	public void testPopulate()
	{
		final String firstName = "firstName";
		final String lastName = "lastName";
		final String address1 = "address1";
		final String address2 = "address2";
		final String city = "city";
		final String country = "country";
		final String postcode = "postcode";

		final String formContentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><personal-details>" + "<first-name>"
				+ firstName + "</first-name><last-name>" + lastName + "</last-name><age>32</age>"
				+ "<phone>441234567890</phone><email>test@test.com</email>" + "<address-line1>" + address1
				+ "</address-line1><address-line2>" + address2 + "</address-line2>" + "<address-city>" + city
				+ "</address-city><address-postcode>" + postcode + "</address-postcode>" + "<address-country>" + country
				+ "</address-country></personal-details>" + "<number-of-travellers>5</number-of-travellers><traveller-1>"
				+ "<first-name-1>Cindy</first-name-1><last-name-1>Lee</last-name-1><age-1>30</age-1>"
				+ "</traveller-1><traveller-2><first-name-2>Yue</first-name-2><last-name-2>Lee</last-name-2>"
				+ "<age-2>1</age-2></traveller-2><traveller-3><first-name-3>Xiang</first-name-3>"
				+ "<last-name-3>Lee</last-name-3><age-3>2</age-3></traveller-3><traveller-4>"
				+ "<first-name-4>Nong</first-name-4><last-name-4>Lee</last-name-4><age-4>3</age-4>"
				+ "</traveller-4><traveller-5><first-name-5/><last-name-5/><age-5/>"
				+ "</traveller-5><traveller-6><first-name-6/><last-name-6/><age-6/>"
				+ "</traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/>"
				+ "</traveller-8><traveller-9><first-name-9/><last-name-9/><age-9/>" + "</traveller-9></form>";
		final YFormDataData formDataData = new YFormDataData();
		formDataData.setContent(formContentXML);

		final InsuranceQuoteReviewData detailData = new InsuranceQuoteReviewData();

		policyHolderDetailsPopulator.populate(formDataData, detailData);

		Assert.assertEquals(firstName, detailData.getPolicyHolderDetail().getFirstName());
		Assert.assertEquals(lastName, detailData.getPolicyHolderDetail().getLastName());
		Assert.assertEquals(address1, detailData.getPolicyHolderDetail().getAddressLine1());
		Assert.assertEquals(address2, detailData.getPolicyHolderDetail().getAddressLine2());
		Assert.assertEquals(city, detailData.getPolicyHolderDetail().getAddressCity());
		Assert.assertEquals(country, detailData.getPolicyHolderDetail().getAddressCountry());
		Assert.assertEquals(postcode, detailData.getPolicyHolderDetail().getPostcode());
	}

	@Test
	public void shouldReturnEmptyAddressWithEmptyAddressLine2()
	{
		final String firstName = "firstName";
		final String lastName = "lastName";
		final String address1 = "address1";
		final String city = "city";
		final String country = "country";
		final String postcode = "postcode";

		final String formContentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><personal-details>" + "<first-name>"
				+ firstName + "</first-name><last-name>" + lastName + "</last-name><age>32</age>"
				+ "<phone>441234567890</phone><email>test@test.com</email>" + "<address-line1>" + address1
				+ "</address-line1><address-line2/><address-city>" + city + "</address-city><address-postcode>" + postcode
				+ "</address-postcode>" + "<address-country>" + country + "</address-country></personal-details>"
				+ "<number-of-travellers>5</number-of-travellers><traveller-1>"
				+ "<first-name-1>Cindy</first-name-1><last-name-1>Lee</last-name-1><age-1>30</age-1>"
				+ "</traveller-1><traveller-2><first-name-2>Yue</first-name-2><last-name-2>Lee</last-name-2>"
				+ "<age-2>1</age-2></traveller-2><traveller-3><first-name-3>Xiang</first-name-3>"
				+ "<last-name-3>Lee</last-name-3><age-3>2</age-3></traveller-3><traveller-4>"
				+ "<first-name-4>Nong</first-name-4><last-name-4>Lee</last-name-4><age-4>3</age-4>"
				+ "</traveller-4><traveller-5><first-name-5/><last-name-5/><age-5/>"
				+ "</traveller-5><traveller-6><first-name-6/><last-name-6/><age-6/>"
				+ "</traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/>"
				+ "</traveller-8><traveller-9><first-name-9/><last-name-9/><age-9/>" + "</traveller-9></form>";
		final YFormDataData formDataData = new YFormDataData();
		formDataData.setContent(formContentXML);

		final InsuranceQuoteReviewData detailData = new InsuranceQuoteReviewData();

		policyHolderDetailsPopulator.populate(formDataData, detailData);

		Assert.assertEquals(firstName, detailData.getPolicyHolderDetail().getFirstName());
		Assert.assertEquals(lastName, detailData.getPolicyHolderDetail().getLastName());
		Assert.assertEquals(address1, detailData.getPolicyHolderDetail().getAddressLine1());
		Assert.assertEquals(StringUtils.EMPTY, detailData.getPolicyHolderDetail().getAddressLine2());
		Assert.assertEquals(city, detailData.getPolicyHolderDetail().getAddressCity());
		Assert.assertEquals(country, detailData.getPolicyHolderDetail().getAddressCountry());
		Assert.assertEquals(postcode, detailData.getPolicyHolderDetail().getPostcode());
	}

	@Test
	public void shouldReturnEmptyAddressWithNoAddressLine2NodeList()
	{
		final String firstName = "firstName";
		final String lastName = "lastName";
		final String address1 = "address1";
		final String city = "city";
		final String country = "country";
		final String postcode = "postcode";

		final String formContentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><personal-details>" + "<first-name>"
				+ firstName + "</first-name><last-name>" + lastName + "</last-name><age>32</age>"
				+ "<phone>441234567890</phone><email>test@test.com</email>" + "<address-line1>" + address1
				+ "</address-line1><address-city>" + city + "</address-city><address-postcode>" + postcode + "</address-postcode>"
				+ "<address-country>" + country + "</address-country></personal-details>"
				+ "<number-of-travellers>5</number-of-travellers><traveller-1>"
				+ "<first-name-1>Cindy</first-name-1><last-name-1>Lee</last-name-1><age-1>30</age-1>"
				+ "</traveller-1><traveller-2><first-name-2>Yue</first-name-2><last-name-2>Lee</last-name-2>"
				+ "<age-2>1</age-2></traveller-2><traveller-3><first-name-3>Xiang</first-name-3>"
				+ "<last-name-3>Lee</last-name-3><age-3>2</age-3></traveller-3><traveller-4>"
				+ "<first-name-4>Nong</first-name-4><last-name-4>Lee</last-name-4><age-4>3</age-4>"
				+ "</traveller-4><traveller-5><first-name-5/><last-name-5/><age-5/>"
				+ "</traveller-5><traveller-6><first-name-6/><last-name-6/><age-6/>"
				+ "</traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/>"
				+ "</traveller-8><traveller-9><first-name-9/><last-name-9/><age-9/>" + "</traveller-9></form>";
		final YFormDataData formDataData = new YFormDataData();
		formDataData.setContent(formContentXML);

		final InsuranceQuoteReviewData detailData = new InsuranceQuoteReviewData();

		policyHolderDetailsPopulator.populate(formDataData, detailData);

		Assert.assertEquals(firstName, detailData.getPolicyHolderDetail().getFirstName());
		Assert.assertEquals(lastName, detailData.getPolicyHolderDetail().getLastName());
		Assert.assertEquals(address1, detailData.getPolicyHolderDetail().getAddressLine1());
		Assert.assertEquals(StringUtils.EMPTY, detailData.getPolicyHolderDetail().getAddressLine2());
		Assert.assertEquals(city, detailData.getPolicyHolderDetail().getAddressCity());
		Assert.assertEquals(country, detailData.getPolicyHolderDetail().getAddressCountry());
		Assert.assertEquals(postcode, detailData.getPolicyHolderDetail().getPostcode());
	}

}

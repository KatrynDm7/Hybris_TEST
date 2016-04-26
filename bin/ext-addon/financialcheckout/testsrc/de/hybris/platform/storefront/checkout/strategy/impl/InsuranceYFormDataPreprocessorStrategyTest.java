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
package de.hybris.platform.storefront.checkout.strategy.impl;

import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormProcessorException;

import java.util.Map;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.google.common.collect.Maps;


/**
 * The class of InsuranceYFormDataPreprocessorStrategyTest.
 */
public class InsuranceYFormDataPreprocessorStrategyTest
{

	@InjectMocks
	private InsuranceYFormDataPreprocessorStrategy insuranceYFormDataPreprocessorStrategy;

	@Before
	public void setup()
	{
		insuranceYFormDataPreprocessorStrategy = new InsuranceYFormDataPreprocessorStrategy();
	}

	@Test
	public void shouldUpdateXmlContent() throws YFormProcessorException
	{
		final String age50 = "50";
		final String age45 = "45";
		final String age35 = "35";
		final Integer noOfTravellers = Integer.valueOf(3);
		final String xmlContent = "<form><personal-details><first-name/><last-name/><age/><phone/>"
				+ "<email/><address-line1/><address-line2/><address-city/><address-postcode/>"
				+ "<address-country/></personal-details><number-of-travellers>10</number-of-travellers>"
				+ "<traveller-1><first-name-1/><last-name-1/><age-1/>"
				+ "</traveller-1><traveller-2><first-name-2/><last-name-2/><age-2></age-2></traveller-2>"
				+ "<traveller-3><first-name-3/><last-name-3/><age-3/></traveller-3><traveller-4>"
				+ "<first-name-4/><last-name-4/><age-4/></traveller-4><traveller-5><first-name-5/>"
				+ "<last-name-5/><age-5/></traveller-5><traveller-6><first-name-6/><last-name-6/>"
				+ "<age-6/></traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/></traveller-8>"
				+ "<traveller-9><first-name-9/><last-name-9/><age-9/></traveller-9></form>";

		final String expectedXmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><personal-details><first-name/><last-name/><age>"
				+ age50
				+ "</age><phone/>"
				+ "<email/><address-line1/><address-line2/><address-city/><address-postcode/>"
				+ "<address-country/></personal-details><number-of-travellers>"
				+ noOfTravellers.toString()
				+ "</number-of-travellers>"
				+ "<traveller-1><first-name-1/><last-name-1/><age-1>"
				+ age45
				+ "</age-1>"
				+ "</traveller-1><traveller-2><first-name-2/><last-name-2/><age-2>"
				+ age35
				+ "</age-2></traveller-2>"
				+ "<traveller-3><first-name-3/><last-name-3/><age-3/></traveller-3><traveller-4>"
				+ "<first-name-4/><last-name-4/><age-4/></traveller-4><traveller-5><first-name-5/>"
				+ "<last-name-5/><age-5/></traveller-5><traveller-6><first-name-6/><last-name-6/>"
				+ "<age-6/></traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/></traveller-8>"
				+ "<traveller-9><first-name-9/><last-name-9/><age-9/></traveller-9></form>";

		final Map<String, Object> params = Maps.newHashMap();

		params.put("/form/number-of-travellers", noOfTravellers);
		params.put("/form/personal-details/age", age50);
		params.put("/form/traveller-1/age-1", age45);
		params.put("/form/traveller-2/age-2", age35);

		params.put("/form/traveller-3/age-3", null);
		params.put("/form/traveller-2/age-20", age35);

		final String resultXml = insuranceYFormDataPreprocessorStrategy.updateXmlContent(xmlContent, params);

		Assert.assertEquals(expectedXmlContent, resultXml);
	}

	@Test
	public void shouldNotUpdateXmlContentWhenXmlContentIsNull() throws YFormProcessorException
	{
		final Map<String, Object> params = Maps.newHashMap();
		params.put("/form/number-of-travellers", 1);

		final String resultXml = insuranceYFormDataPreprocessorStrategy.updateXmlContent(null, params);

		Assert.assertNull(resultXml);
	}

	@Test
	public void shouldNotUpdateXmlContentWhenParameterIsEmpty() throws YFormProcessorException
	{
		final String xmlContent = "<form><personal-details><first-name/><last-name/><age/><phone/>"
				+ "<email/><address-line1/><address-line2/><address-city/><address-postcode/>"
				+ "<address-country/></personal-details><number-of-travellers>10</number-of-travellers>"
				+ "<traveller-1><first-name-1/><last-name-1/><age-1/>"
				+ "</traveller-1><traveller-2><first-name-2/><last-name-2/><age-2/></traveller-2>"
				+ "<traveller-3><first-name-3/><last-name-3/><age-3/></traveller-3><traveller-4>"
				+ "<first-name-4/><last-name-4/><age-4/></traveller-4><traveller-5><first-name-5/>"
				+ "<last-name-5/><age-5/></traveller-5><traveller-6><first-name-6/><last-name-6/>"
				+ "<age-6/></traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/></traveller-8>"
				+ "<traveller-9><first-name-9/><last-name-9/><age-9/></traveller-9></form>";

		final Map<String, Object> params = Maps.newHashMap();

		final String resultXml = insuranceYFormDataPreprocessorStrategy.updateXmlContent(xmlContent, params);

		Assert.assertEquals(resultXml, xmlContent);
	}
}

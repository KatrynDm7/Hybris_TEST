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

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storefront.constants.InsurancecheckoutConstants;
import de.hybris.platform.storefront.form.data.FormDetailData;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormProcessorException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;


/**
 * The class of TravelInsuranceTransformerYFormPreprocessorStrategyTest.
 */
public class TravelInsuranceTransformerYFormPreprocessorStrategyTest
{

	@InjectMocks
	private TravelInsuranceTransformerYFormPreprocessorStrategy preprocessorStrategy;

	@Mock
	private CartService cartService;

	@Mock
	private ModelService modelService;

	@Before
	public void setup()
	{
		preprocessorStrategy = new TravelInsuranceTransformerYFormPreprocessorStrategy();

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testTransform() throws YFormProcessorException
	{
		final String age15 = "15";
		final String age25 = "25";
		final String age35 = "35";
		final String age45 = "45";
		final Integer noOfTravellers = Integer.valueOf(4);

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

		final String expectedXmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><form><personal-details><first-name/><last-name/><age>"
				+ age15
				+ "</age><phone/>"
				+ "<email/><address-line1/><address-line2/><address-city/><address-postcode/>"
				+ "<address-country/></personal-details><number-of-travellers>"
				+ noOfTravellers.toString()
				+ "</number-of-travellers>"
				+ "<traveller-1><first-name-1/><last-name-1/><age-1>"
				+ age25
				+ "</age-1>"
				+ "</traveller-1><traveller-2><first-name-2/><last-name-2/><age-2>"
				+ age35
				+ "</age-2></traveller-2>"
				+ "<traveller-3><first-name-3/><last-name-3/><age-3>"
				+ age45
				+ "</age-3></traveller-3><traveller-4>"
				+ "<first-name-4/><last-name-4/><age-4/></traveller-4><traveller-5><first-name-5/>"
				+ "<last-name-5/><age-5/></traveller-5><traveller-6><first-name-6/><last-name-6/>"
				+ "<age-6/></traveller-6><traveller-7><first-name-7/><last-name-7/><age-7/>"
				+ "</traveller-7><traveller-8><first-name-8/><last-name-8/><age-8/></traveller-8>"
				+ "<traveller-9><first-name-9/><last-name-9/><age-9/></traveller-9></form>";
		final Integer cartEntryNumber = Integer.valueOf(0);

		final FormDetailData data = new FormDetailData();
		data.setOrderEntryNumber(cartEntryNumber);

		final Map<String, Object> params = Maps.newHashMap();
		params.put(InsuranceYFormDataPreprocessorStrategy.FORM_DETAIL_DATA, data);

		final CartModel cartModel = new CartModel();

		final Map<String, Object> infoMap = Maps.newHashMap();
		infoMap.put(InsurancecheckoutConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, noOfTravellers);

		final List<String> ages = Arrays.asList(age15, age25, age35, age45);
		infoMap.put(InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE, ages);

		final InsuranceQuoteModel quoteModel = new InsuranceQuoteModel();
		quoteModel.setProperties(infoMap);

		cartModel.setInsuranceQuote(quoteModel);

		Mockito.when(cartService.getSessionCart()).thenReturn(cartModel);

		final String resultXml = preprocessorStrategy.transform(xmlContent, params);

		Assert.assertFalse(params.containsKey(InsurancecheckoutConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE));
		Assert.assertEquals(expectedXmlContent, resultXml);
	}
}

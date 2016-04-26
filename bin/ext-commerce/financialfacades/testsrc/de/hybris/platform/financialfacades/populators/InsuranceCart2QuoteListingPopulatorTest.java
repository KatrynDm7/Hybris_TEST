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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


@UnitTest
public class InsuranceCart2QuoteListingPopulatorTest
{
	@InjectMocks
	InsuranceCart2QuoteListingPopulator insuranceCart2QuoteListingPopulator;

	String testFormattedValue = "£12.00";
	String testProductName = "name";
	String testCartCode = "code";
	String testQuoteFormattedExpiryDate = "26-12-2014";
	InsuranceQuoteListingData quoteListingData;
	CartData cartData;
	InsuranceQuoteData quoteData;

	List<ImageData> imageDataList = new ArrayList<>();

	@Before
	public void setup()
	{
		insuranceCart2QuoteListingPopulator = new InsuranceCart2QuoteListingPopulator();
		MockitoAnnotations.initMocks(this);

		quoteListingData = new InsuranceQuoteListingData();
		cartData = new CartData();
		final PriceData priceData = new PriceData();
		priceData.setFormattedValue(testFormattedValue);
		cartData.setTotalPrice(priceData);
		cartData.setCode(testCartCode);

		final List<OrderEntryData> entries = new ArrayList<>();
		final OrderEntryData orderEntryData = new OrderEntryData();
		final ProductData productData = new ProductData();
		productData.setName(testProductName);
		productData.setImages(imageDataList);
		orderEntryData.setProduct(productData);
		entries.add(orderEntryData);
		cartData.setEntries(entries);
		final SubscriptionPricePlanData pricePlanData = new SubscriptionPricePlanData();
		productData.setPrice(pricePlanData);

		quoteData = new InsuranceQuoteData();
		cartData.setInsuranceQuote(quoteData);
		quoteData.setFormattedExpiryDate(testQuoteFormattedExpiryDate);
	}

	@Test
	public void testPopulate_and_with_no_quoteId()
	{
		insuranceCart2QuoteListingPopulator.populate(cartData, quoteListingData);
		Assert.assertEquals(testFormattedValue, quoteListingData.getQuotePrice());
		Assert.assertEquals(testProductName, quoteListingData.getPlanName());
		Assert.assertEquals(imageDataList, quoteListingData.getQuoteImages());
		Assert.assertEquals(testQuoteFormattedExpiryDate, quoteListingData.getQuoteExpiryDate());

		Assert.assertEquals(testCartCode, quoteListingData.getQuoteNumber());
	}

	@Test
	public void testPopulate_and_with_quoteId()
	{
		final String testQuoteId = "12345";
		quoteData.setQuoteId(testQuoteId);
		insuranceCart2QuoteListingPopulator.populate(cartData, quoteListingData);

		Assert.assertEquals(testQuoteId, quoteListingData.getQuoteNumber());
	}
}

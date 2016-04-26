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
package de.hybris.platform.storefront.facades.impl;

import de.hybris.platform.commercefacades.insurance.data.InsuranceBenefitData;
import de.hybris.platform.commercefacades.insurance.data.InsuranceCoverageData;
import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.financialfacades.facades.InsuranceQuoteFacade;
import de.hybris.platform.storefront.checkout.facades.impl.DefaultInsuranceQuoteReviewFacade;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


/**
 * The class of DefaultInsuranceQuoteReviewFacadeTest.
 */
public class DefaultInsuranceQuoteReviewFacadeTest
{
	@InjectMocks
	private DefaultInsuranceQuoteReviewFacade insuranceQuoteReviewFacade;

	@Mock
	private CartFacade cartFacade;

	@Mock
	private InsuranceQuoteFacade insuranceQuoteFacade;

	private final String datetimeFormat = "dd-MM-yyyy";

	@Before
	public void setup()
	{
		insuranceQuoteReviewFacade = new DefaultInsuranceQuoteReviewFacade();
		insuranceQuoteReviewFacade.setDatetimeFormat(datetimeFormat);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnInsuranceQuoteReviews()
	{
		final String cartCode = "123456789";
		final Integer days = Integer.valueOf(30);
		final Date expectedExpiresDate = DateTime.now().plus(Days.days(days)).toDate();
		final CartData cartData = new CartData();
		cartData.setCode(cartCode);
		final OrderEntryData entry = new OrderEntryData();
		final ProductData product = new ProductData();
		entry.setProduct(product);
		cartData.setEntries(Lists.<OrderEntryData> newArrayList());
		cartData.getEntries().add(entry);

		Mockito.when(cartFacade.hasSessionCart()).thenReturn(Boolean.TRUE);
		Mockito.when(cartFacade.hasEntries()).thenReturn(Boolean.TRUE);
		Mockito.when(cartFacade.getSessionCart()).thenReturn(cartData);
		Mockito.when(insuranceQuoteFacade.getQuoteWorkflowType()).thenReturn("");

		final List<InsuranceQuoteReviewData> insuranceQuoteReviews = insuranceQuoteReviewFacade.getInsuranceQuoteReviews();

		Assert.assertNotNull(insuranceQuoteReviews);
		Assert.assertFalse(insuranceQuoteReviews.isEmpty());
		Assert.assertEquals(product, insuranceQuoteReviews.get(0).getMainProduct().getCoverageProduct());
		Assert.assertEquals(cartCode, insuranceQuoteReviews.get(0).getQuoteId());

		final SimpleDateFormat sdf = new SimpleDateFormat(datetimeFormat);

		Assert.assertEquals(sdf.format(expectedExpiresDate), insuranceQuoteReviews.get(0).getQuoteExpires());
	}

	@Test
	public void shouldReturnCorrectIncluded()
	{
		final String cartCode = "123456789";
		final CartData cartData = new CartData();
		cartData.setCode(cartCode);
		final OrderEntryData entry = new OrderEntryData();

		final ProductData product = new ProductData();
		entry.setProduct(product);
		cartData.setEntries(Lists.<OrderEntryData> newArrayList());
		cartData.getEntries().add(entry);

		final SubscriptionPricePlanData pricePlanData = new SubscriptionPricePlanData();
		product.setPrice(pricePlanData);

		final List<OneTimeChargeEntryData> oneTimeCharges = new ArrayList<OneTimeChargeEntryData>();
		final OneTimeChargeEntryData otce0 = new OneTimeChargeEntryData();
		final BillingTimeData btd0 = new BillingTimeData();
		btd0.setName("Pay Now");
		btd0.setCode("paynow");
		otce0.setBillingTime(btd0);
		oneTimeCharges.add(otce0);

		final OneTimeChargeEntryData otce1 = new OneTimeChargeEntryData();
		final BillingTimeData btd1 = new BillingTimeData();
		btd1.setName("Event Clothing");
		btd1.setCode("eventclothing");
		otce1.setBillingTime(btd1);
		oneTimeCharges.add(otce1);

		final OneTimeChargeEntryData otce2 = new OneTimeChargeEntryData();
		final BillingTimeData btd2 = new BillingTimeData();
		btd2.setName("Cars and Transport");
		btd2.setCode("carsttransport");
		otce2.setBillingTime(btd2);
		oneTimeCharges.add(otce2);

		pricePlanData.setOneTimeChargeEntries(oneTimeCharges);

		Mockito.when(cartFacade.hasSessionCart()).thenReturn(Boolean.TRUE);
		Mockito.when(cartFacade.hasEntries()).thenReturn(Boolean.TRUE);
		Mockito.when(cartFacade.getSessionCart()).thenReturn(cartData);

		final List<InsuranceQuoteReviewData> insuranceQuoteReviews = insuranceQuoteReviewFacade.getInsuranceQuoteReviews();

		Assert.assertNotNull(insuranceQuoteReviews);
		Assert.assertFalse(insuranceQuoteReviews.isEmpty());

		final List<InsuranceBenefitData> includedArray = insuranceQuoteReviews.get(0).getMainProduct().getBenefits();
		Assert.assertEquals(includedArray.size(), 2);
		Assert.assertEquals(includedArray.get(0).getName(), "Cars and Transport");
		Assert.assertEquals(includedArray.get(1).getName(), "Event Clothing");

	}

	@Test
	public void shouldReturnCorrectExtras()
	{
		final String cartCode = "123456789";
		final CartData cartData = new CartData();
		cartData.setCode(cartCode);
		cartData.setEntries(Lists.<OrderEntryData> newArrayList());

		final OrderEntryData entry = new OrderEntryData();
		final ProductData product = new ProductData();
		entry.setProduct(product);
		entry.setRemoveable(true);
		cartData.getEntries().add(entry);

		final OrderEntryData entry1 = new OrderEntryData();
		final ProductData product1 = new ProductData();
		product1.setName("Venue Cover");
		entry1.setProduct(product1);
		entry1.setRemoveable(true);
		cartData.getEntries().add(entry1);

		final OrderEntryData entry2 = new OrderEntryData();
		final ProductData product2 = new ProductData();
		product2.setName("Excess Waiver");
		entry2.setProduct(product2);
		entry2.setRemoveable(true);
		cartData.getEntries().add(entry2);

		Mockito.when(cartFacade.hasSessionCart()).thenReturn(Boolean.TRUE);
		Mockito.when(cartFacade.hasEntries()).thenReturn(Boolean.TRUE);
		Mockito.when(cartFacade.getSessionCart()).thenReturn(cartData);

		final List<InsuranceQuoteReviewData> insuranceQuoteReviews = insuranceQuoteReviewFacade.getInsuranceQuoteReviews();

		Assert.assertNotNull(insuranceQuoteReviews);
		Assert.assertFalse(insuranceQuoteReviews.isEmpty());
		Assert.assertEquals(product, insuranceQuoteReviews.get(0).getMainProduct().getCoverageProduct());
		Assert.assertEquals(cartCode, insuranceQuoteReviews.get(0).getQuoteId());

		final List<InsuranceCoverageData> optionalProducts = insuranceQuoteReviews.get(0).getOptionalProducts();
		Assert.assertEquals(optionalProducts.size(), 2);
		Assert.assertEquals(optionalProducts.get(0).getCoverageProduct().getName(), "Venue Cover");
		Assert.assertEquals(optionalProducts.get(1).getCoverageProduct().getName(), "Excess Waiver");

	}
}
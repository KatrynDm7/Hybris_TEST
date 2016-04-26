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


import com.google.common.collect.Lists;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.financialfacades.populators.InsuranceOrderPolicyPopulator;
import de.hybris.platform.financialfacades.populators.InsurancePolicyDetailsPopulator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;


/**
 * The class of InsuranceOrderPolicyPopulatorTest.
 */
@UnitTest
public class InsuranceOrderPolicyPopulatorTest
{

	@InjectMocks
	private InsuranceOrderPolicyPopulator insuranceOrderPolicyPopulator;
    
    @Mock
    private InsurancePolicyDetailsPopulator insurancePolicyDetailsPopulator;

	@Mock
	private Converter<AbstractOrderEntryModel, OrderEntryData> insuranceOrderEntryConverter;

	@Before
	public void setup()
	{
		insuranceOrderPolicyPopulator = new InsuranceOrderPolicyPopulator();

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldGroupOrderEntryDataByBundle()
	{
		OrderData orderData = new OrderData();
		orderData.setEntries(Lists.<OrderEntryData> newArrayList());

		OrderEntryData entryDataA = new OrderEntryData();
		entryDataA.setBundleNo(1);
		OrderEntryData entryDataB = new OrderEntryData();
		entryDataB.setBundleNo(1);
		OrderEntryData entryDataC = new OrderEntryData();
		entryDataC.setBundleNo(2);
		OrderEntryData entryDataD = new OrderEntryData();
		entryDataD.setBundleNo(2);
		OrderEntryData entryDataE = new OrderEntryData();
		entryDataE.setBundleNo(3);

		orderData.getEntries().add(entryDataA);
		orderData.getEntries().add(entryDataB);
		orderData.getEntries().add(entryDataC);
		orderData.getEntries().add(entryDataD);
		orderData.getEntries().add(entryDataE);


		Map<Integer, List<OrderEntryData>> resultMap = insuranceOrderPolicyPopulator.groupOrderEntryDataByBundle(orderData);

		Assert.assertEquals(3, resultMap.size());
		Assert.assertEquals(entryDataA, resultMap.get(1).get(0));
		Assert.assertEquals(entryDataB, resultMap.get(1).get(1));
		Assert.assertEquals(entryDataC, resultMap.get(2).get(0));
		Assert.assertEquals(entryDataD, resultMap.get(2).get(1));
		Assert.assertEquals(entryDataE, resultMap.get(3).get(0));
	}

	@Test
	public void shouldPopulateInsurancePolicyData()
	{

		BillingTimeData payNowBillingTime = new BillingTimeData();
		payNowBillingTime.setCode("paynow");
		payNowBillingTime.setName("paynow");
		OneTimeChargeEntryData paynowCharge = new OneTimeChargeEntryData();
		paynowCharge.setBillingTime(payNowBillingTime);
		paynowCharge.setName("paynow");

		BillingTimeData baggageBillingTime = new BillingTimeData();
		baggageBillingTime.setCode("baggage");
		baggageBillingTime.setName("Baggage");
		OneTimeChargeEntryData baggageCoverage = new OneTimeChargeEntryData();
		baggageCoverage.setBillingTime(baggageBillingTime);
		baggageCoverage.setName("Baggage");

		BillingTimeData personalMoneyBillingTime = new BillingTimeData();
		personalMoneyBillingTime.setCode("personalMoney");
		personalMoneyBillingTime.setName("Personal Money");
		OneTimeChargeEntryData personalMoneyCoverage = new OneTimeChargeEntryData();
		personalMoneyCoverage.setBillingTime(personalMoneyBillingTime);
		personalMoneyCoverage.setName("personalMoney");

		BillingTimeData repatriationBillingTime = new BillingTimeData();
		repatriationBillingTime.setCode("repatriation");
		repatriationBillingTime.setName("Repatriation");
		OneTimeChargeEntryData repatriationCoverage = new OneTimeChargeEntryData();
		repatriationCoverage.setBillingTime(repatriationBillingTime);
		repatriationCoverage.setName("repatriation");


		// Build productA - main product
		ProductData productA = new ProductData();
		productA.setName("productA");
		SubscriptionPricePlanData pricePlanA = new SubscriptionPricePlanData();
		pricePlanA.setOneTimeChargeEntries(Lists.<OneTimeChargeEntryData> newArrayList());
		pricePlanA.getOneTimeChargeEntries().add(paynowCharge);
		pricePlanA.getOneTimeChargeEntries().add(baggageCoverage);
		pricePlanA.getOneTimeChargeEntries().add(personalMoneyCoverage);
		productA.setPrice(pricePlanA);

		// Build productB - optional product
		ProductData productB = new ProductData();
		productB.setName("productB");

		// Build productC - main product
		ProductData productC = new ProductData();
		productC.setName("productC");
		SubscriptionPricePlanData pricePlanC = new SubscriptionPricePlanData();
		pricePlanC.setOneTimeChargeEntries(Lists.<OneTimeChargeEntryData> newArrayList());
		pricePlanC.getOneTimeChargeEntries().add(paynowCharge);
        pricePlanC.getOneTimeChargeEntries().add(repatriationCoverage);
        pricePlanC.getOneTimeChargeEntries().add(personalMoneyCoverage);
        pricePlanC.getOneTimeChargeEntries().add(baggageCoverage);
        productC.setPrice(pricePlanC);

		// Build productD - optional product
		ProductData productD = new ProductData();
        productD.setName("productD");
		// Build productE - optional product

		ProductData productE = new ProductData();
        productE.setName("productE");

		OrderData orderData = new OrderData();
		orderData.setEntries(Lists.<OrderEntryData> newArrayList());

		OrderEntryData entryDataA = new OrderEntryData();
		entryDataA.setBundleNo(1);
		entryDataA.setProduct(productA);
        orderData.getEntries().add(entryDataA);

		OrderEntryData entryDataB = new OrderEntryData();
		entryDataB.setBundleNo(1);
		entryDataB.setProduct(productB);
        orderData.getEntries().add(entryDataB);
                                    
		OrderEntryData entryDataC = new OrderEntryData();
		entryDataC.setBundleNo(2);
        entryDataC.setProduct(productC);
        orderData.getEntries().add(entryDataC);
                                     
		OrderEntryData entryDataD = new OrderEntryData();
		entryDataD.setBundleNo(2);
        entryDataD.setProduct(productD);
        orderData.getEntries().add(entryDataD);
                                    
		OrderEntryData entryDataE = new OrderEntryData();
		entryDataE.setBundleNo(2);
        entryDataE.setProduct(productE);
        orderData.getEntries().add(entryDataE);
                                      
        
        insuranceOrderPolicyPopulator.populate(new OrderModel(), orderData);

        Assert.assertNotNull(orderData.getInsurancePolicy());
        Assert.assertEquals(productA, orderData.getInsurancePolicy().get(0).getMainProduct().getCoverageProduct());
        Assert.assertEquals("Baggage", orderData.getInsurancePolicy().get(0).getMainProduct().getBenefits().get(0).getName());
        Assert.assertEquals("Personal Money", orderData.getInsurancePolicy().get(0).getMainProduct().getBenefits().get(1).getName());
        Assert.assertEquals("productB", orderData.getInsurancePolicy().get(0).getOptionalProducts().get(0).getCoverageProduct().getName());
        Assert.assertEquals(productC, orderData.getInsurancePolicy().get(1).getMainProduct().getCoverageProduct());
        Assert.assertEquals("Baggage", orderData.getInsurancePolicy().get(1).getMainProduct().getBenefits().get(0).getName());
        Assert.assertEquals("Personal Money", orderData.getInsurancePolicy().get(1).getMainProduct().getBenefits().get(1).getName());
        Assert.assertEquals("Repatriation", orderData.getInsurancePolicy().get(1).getMainProduct().getBenefits().get(2).getName());
        Assert.assertEquals("productD", orderData.getInsurancePolicy().get(1).getOptionalProducts().get(0).getCoverageProduct().getName());
        Assert.assertEquals("productE", orderData.getInsurancePolicy().get(1).getOptionalProducts().get(1).getCoverageProduct().getName());
	}

}

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
package de.hybris.platform.financialfacades.services.document.generation.pdf.fop.impl;

import de.hybris.platform.commercefacades.insurance.data.InsuranceBenefitData;
import de.hybris.platform.commercefacades.insurance.data.InsuranceCoverageData;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.insurance.data.PolicyHolderDetailData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.financialfacades.services.document.generation.pdf.fop.PolicyDocumentDataProcessService;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;


/**
 * Mock Policy Document Data Process Strategy
 */
class MockPolicyDocumentDataProcessStrategy implements PolicyDocumentDataProcessService
{
	protected static final Logger LOG = Logger.getLogger(MockPolicyDocumentDataProcessStrategy.class);

	/*
	 * Mock service to get Policy Data for demo
	 */
	@Override
	public InsurancePolicyData getPolicyDocumentData(final String itemRefId)
	{
		LOG.warn("Not for go live.  Development in progress.");
		InsurancePolicyData policyData;
		if (itemRefId.equals("travel01"))
		{
			policyData = getPolicyDocumentDataList().get(0);
		}
		else
		{
			policyData = getPolicyDocumentDataList().get(1);
		}
		return policyData;
	}

	/*
	 * Mock service to get list of Policy Data for demo
	 */
	protected List<InsurancePolicyData> getPolicyDocumentDataList()
	{
		final List<InsurancePolicyData> policyDocumentDataList = Lists.newArrayList();

		final InsurancePolicyData policyDataA = createMockPolicyA();
		policyDocumentDataList.add(policyDataA);

		final InsurancePolicyData policyDataB = createMockPolicyB();
		policyDocumentDataList.add(policyDataB);

		return policyDocumentDataList;
	}

	protected InsurancePolicyData createMockPolicyB()
	{
		final InsurancePolicyData policyData = new InsurancePolicyData();
		final PolicyHolderDetailData policyHolder = new PolicyHolderDetailData();
		final CategoryData categoryData = new CategoryData();
		policyData.setCategoryData(categoryData);
		policyData.setPolicyHolderDetail(policyHolder);

		policyHolder.setTitle("Mrs.");
		policyHolder.setFirstName("Jane");
		policyHolder.setLastName("Doe");

		categoryData.setName("Travel");
		categoryData.setCode("insurances_travel");

		policyData.setPolicyStartDate("31/11/2015");
		policyData.setPolicyNumber("CO73827-223");
		final PriceData recurringPriceData = new PriceData();
		recurringPriceData.setFormattedValue("£102.00");
		recurringPriceData.setValue(new BigDecimal(102.00D));
		recurringPriceData.setCurrencyIso("GBP");
		policyData.setRecurringPrice(recurringPriceData);
		policyData.setPeriodRetrievedFrom("2 months");
		policyData.setDurationRetrievedFrom("2 years");

		final InsuranceCoverageData coverageData = new InsuranceCoverageData();
		policyData.setMainProduct(coverageData);

		final List<InsuranceBenefitData> benefits = Lists.newArrayList();
		coverageData.setBenefits(benefits);

		final InsuranceBenefitData benefit1 = new InsuranceBenefitData();
		final InsuranceBenefitData benefit2 = new InsuranceBenefitData();
		benefit1.setName("Annual trip");
		benefit2.setName("Annual trip two");
		benefits.add(benefit1);
		benefits.add(benefit2);
		coverageData.setBenefits(benefits);

		return policyData;
	}

	protected InsurancePolicyData createMockPolicyA()
	{
		final InsurancePolicyData policyData = new InsurancePolicyData();
		final PolicyHolderDetailData policyHolder = new PolicyHolderDetailData();
		final CategoryData categoryData = new CategoryData();
		policyData.setCategoryData(categoryData);
		policyData.setPolicyHolderDetail(policyHolder);

		policyHolder.setTitle("Mr.");
		policyHolder.setFirstName("John");
		policyHolder.setLastName("Doe");

		categoryData.setName("Travel");
		categoryData.setCode("insurances_travel");

		policyData.setPolicyStartDate("01/09/2015");
		policyData.setPolicyNumber("BH98203-332");
		final PriceData recurringPriceData = new PriceData();
		recurringPriceData.setFormattedValue("£72.00");
		recurringPriceData.setValue(new BigDecimal(72.00D));
		recurringPriceData.setCurrencyIso("GBP");
		policyData.setRecurringPrice(recurringPriceData);
		policyData.setPeriodRetrievedFrom("month");
		policyData.setDurationRetrievedFrom("1 year");

		final InsuranceCoverageData coverageData = new InsuranceCoverageData();
		policyData.setMainProduct(coverageData);

		final List<InsuranceBenefitData> benefits = Lists.newArrayList();
		coverageData.setBenefits(benefits);

		final InsuranceBenefitData benefit1 = new InsuranceBenefitData();
		final InsuranceBenefitData benefit2 = new InsuranceBenefitData();
		benefit1.setName("Single trip");
		benefit2.setName("Single trips two");
		benefits.add(benefit1);
		benefits.add(benefit2);
		coverageData.setBenefits(benefits);
		return policyData;
	}
}

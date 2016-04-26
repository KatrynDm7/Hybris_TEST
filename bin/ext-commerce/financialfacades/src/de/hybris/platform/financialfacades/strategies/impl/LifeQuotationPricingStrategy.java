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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.commercefacades.quotation.QuotationItemRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationItemResponseData;
import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.financialservices.constants.FinancialservicesConstants;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class LifeQuotationPricingStrategy extends AbstractQuotationPricingStrategy
{
	private static final Logger LOG = Logger.getLogger(LifeQuotationPricingStrategy.class);

	protected static final double MONTHLY_DIVISOR = 12d;
	protected static final double COVERAGE_AMOUNT_DIVISOR = 100000d;
	protected static final double COVERAGE_AMOUNT_DEFAULT_ADDITION = 1d;
	protected static final double FINAL_AMOUNT_MULTIPLIER = 200d;
	protected static final double SMOKER_MULTIPLIER = 1.6d;
	protected static final double JOINT_APPLICANT_MULTIPLIER = 1.8d;

	protected static final double OPTIONAL_PRODUCT_FACTOR = 1.9d;

	protected static final String LIFE_VALUE_CURRENT_SMOKER = "current smoker";
	protected static final String LIFE_VALUE_JOIN_APPLICANTS = "yourself and second person";

	protected static final String PREMIUM_PROTECTION = "PREMIUM_PROTECTION";
	protected static final String RENEWAL_OPTION = "RENEWAL_OPTION";
	protected static final String PAYMENT_PROTECTION = "PAYMENT_PROTECTION";

	private Map<String, Double> formulaFactors;

	@Override
	public void initStrategy()
	{
		super.initStrategy();
		// Optional product factors
		formulaFactors = Maps.newHashMap();
		formulaFactors.put(PREMIUM_PROTECTION, .95d);
		formulaFactors.put(RENEWAL_OPTION, .51d);
		formulaFactors.put(PAYMENT_PROTECTION, .87d);
	}

	@Override
	protected List<QuotationItemResponseData> executeAlternativeAlgorithm(final QuotationRequestData quotationRequestData,
			final String basePrice)
	{
		final List<QuotationItemResponseData> productList = Lists.newArrayList();
		QuotationItemResponseData productData;

		for (final QuotationItemRequestData itemData : quotationRequestData.getItems())
		{
			productData = new QuotationItemResponseData();

			// main product plan
			productData.setId(itemData.getId());

			Double price = Double.valueOf(basePrice) / OPTIONAL_PRODUCT_FACTOR;

			if (itemData.getId().contains(PREMIUM_PROTECTION))
			{
				price *= formulaFactors.get(PREMIUM_PROTECTION);
			}
			else if (itemData.getId().contains(RENEWAL_OPTION))
			{
				price *= formulaFactors.get(RENEWAL_OPTION);
			}
			else if (itemData.getId().contains(PAYMENT_PROTECTION))
			{
				price *= formulaFactors.get(PAYMENT_PROTECTION);
			}

			final String priceString = getPriceFormat(price);


			productData.setPayNowPrice(priceString);
			productData.setRecurringPrice(priceString);
			productList.add(productData);
		}

		return productList;
	}

	/**
	 * Method used to evaluate the QuotationRequestData.
	 *
	 * @param requestData
	 *           the request data
	 * @return true if it is valid request
	 */
	@Override
	protected boolean isValidQuote(final QuotationRequestData requestData)
	{
		boolean isValid = false;

		if (requestData.getProperties().get(FinancialservicesConstants.LIFE_WHO_COVERED) != null
				&& requestData.getProperties().get(FinancialservicesConstants.LIFE_COVERAGE_REQUIRE) != null
				&& requestData.getProperties().get(FinancialservicesConstants.LIFE_MAIN_SMOKE) != null
				&& requestData.getItems() != null)
		{
			isValid = true;
		}

		return isValid;
	}

	/**
	 * Method which encapsulates the algorithm to calculate paynow price based on the static factors and the request
	 * data.
	 *
	 * @param requestData
	 *           the request data
	 * @param itemData
	 *           the item data
	 * @return final pay now price.
	 */
	@Override
	protected String executeAlgorithm(final QuotationRequestData requestData, final QuotationItemRequestData itemData)
	{
		double price = (getCoverageRequired(requestData) / MONTHLY_DIVISOR) / getBaseFactor(requestData);

		if (isJointApplicant(requestData))
		{
			price = price * JOINT_APPLICANT_MULTIPLIER;
		}

		if (isMainSmoke(requestData) || (isJointApplicant(requestData) && isSecondSmoke(requestData)))
		{
			price = price * SMOKER_MULTIPLIER;
		}

		LOG.debug("Pay Now price for the given Data " + price);

		return getPriceFormat(price);
	}

	protected String getPriceFormat(final double price)
	{
		return Double.toString((double) Math.round(price * 100) / 100);
	}

	protected double getCoverageRequired(final QuotationRequestData requestData)
	{
		return MapUtils.getDoubleValue(requestData.getProperties(), FinancialservicesConstants.LIFE_COVERAGE_REQUIRE);
	}

	protected boolean isJointApplicant(final QuotationRequestData requestData)
	{
		return LIFE_VALUE_JOIN_APPLICANTS.equalsIgnoreCase(MapUtils.getString(requestData.getProperties(),
				FinancialservicesConstants.LIFE_WHO_COVERED));
	}

	protected boolean isMainSmoke(final QuotationRequestData requestData)
	{
		return LIFE_VALUE_CURRENT_SMOKER.equalsIgnoreCase(MapUtils.getString(requestData.getProperties(),
				FinancialservicesConstants.LIFE_MAIN_SMOKE));
	}

	protected boolean isSecondSmoke(final QuotationRequestData requestData)
	{
		return isJointApplicant(requestData)
				&& LIFE_VALUE_CURRENT_SMOKER.equalsIgnoreCase(MapUtils.getString(requestData.getProperties(),
						FinancialservicesConstants.LIFE_SECOND_SMOKE));
	}



	protected double getBaseFactor(final QuotationRequestData requestData)
	{
		final double amount = MapUtils.getDouble(requestData.getProperties(), FinancialservicesConstants.LIFE_COVERAGE_REQUIRE, 0d);
		final double factor = amount / COVERAGE_AMOUNT_DIVISOR + COVERAGE_AMOUNT_DEFAULT_ADDITION;

		return FINAL_AMOUNT_MULTIPLIER * factor;
	}

}

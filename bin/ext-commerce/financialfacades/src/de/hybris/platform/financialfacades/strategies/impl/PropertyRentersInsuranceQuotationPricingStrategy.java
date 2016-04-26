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
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class PropertyRentersInsuranceQuotationPricingStrategy extends PropertyInsuranceQuotationPricingStrategy
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PropertyRentersInsuranceQuotationPricingStrategy.class);

	protected static final String PRO_PROPERTY_RENTERS = "PRO_PROPERTY_RENTERS";
	protected static final String PRO_TEMP_ACCOM_RENTERS = "PRO_TEMP_ACCOM_RENTERS";
	protected static final String PRO_BICYCLES_COVER_RENTERS = "PRO_BICYCLES_COVER_RENTERS";
	protected static final String PRO_JEWELRY_COVER_RENTERS = "PRO_JEWELRY_COVER_RENTERS";

	@Override
	public void initStrategy()
	{
		super.initStrategy();

		// Optional product factors
		formulaFactors.put(PRO_PROPERTY_RENTERS, 1d);
		formulaFactors.put(PRO_TEMP_ACCOM_RENTERS, 0.22d);
		formulaFactors.put(PRO_BICYCLES_COVER_RENTERS, 0.35d);
		formulaFactors.put(PRO_JEWELRY_COVER_RENTERS, 0.45d);
	}


	/**
	 * Method used to evaluate the QuotationRequestData.
	 *
	 * @param requestData
	 * @return true if it is valid request
	 */
	@Override
	protected boolean isValidQuote(final QuotationRequestData requestData)
	{
		boolean isValid = false;

		if (requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_TYPE) != null
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
	 * @param itemData
	 * @return final paynow price.
	 */
	@Override
	protected String executeAlgorithm(final QuotationRequestData requestData, final QuotationItemRequestData itemData)
	{
		final double annualPrice = calculateAnnual(requestData);

		return Math.round(annualPrice * 100) / 100 < 5 ? DEFAULT_FALLBACK_PRICE : Double
				.toString(Math.round(annualPrice * 100) / 100);
	}

	@Override
	protected double calculateMonthly(final QuotationRequestData requestData)
	{
		final double contentValue = getPropertyContentValue(requestData);
		final double contentValueFactor = getPropertyCoverValueFactor();
		return contentValue * contentValueFactor;
	}

	@Override
	protected double calculateAnnual(final QuotationRequestData requestData)
	{
		return calculateMonthly(requestData) * getPropertyAnnualFactor();
	}

	@Override
	// temporary method implementation for demo to calculate optional product based on base price.
	protected List<QuotationItemResponseData> executeAlternativeAlgorithm(final QuotationRequestData quotationRequestData,
			final String basePrice)
	{
		final List<QuotationItemResponseData> productList = new ArrayList<QuotationItemResponseData>();
		QuotationItemResponseData productData;

		for (final QuotationItemRequestData itemData : quotationRequestData.getItems())
		{
			productData = new QuotationItemResponseData();

			productData.setId(itemData.getId());
			Double value = 0d;

			if (itemData.getId().contains(PRO_PROPERTY_RENTERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_PROPERTY_RENTERS);
			}
			else if (itemData.getId().contains(PRO_TEMP_ACCOM_RENTERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_TEMP_ACCOM_RENTERS);
			}
			else if (itemData.getId().contains(PRO_BICYCLES_COVER_RENTERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_BICYCLES_COVER_RENTERS);
			}
			else if (itemData.getId().contains(PRO_JEWELRY_COVER_RENTERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_JEWELRY_COVER_RENTERS);
			}

			//It is the same price calculation for the optional products regarding the payment term
			productData.setPayNowPrice(Double.toString(value + BASE_PRICE));
			productData.setRecurringPrice(Double.toString(value + BASE_PRICE));

			productList.add(productData);
		}

		return productList;
	}
}

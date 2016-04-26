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

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;


public class PropertyHomeownersInsuranceQuotationPricingStrategy extends PropertyInsuranceQuotationPricingStrategy
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PropertyHomeownersInsuranceQuotationPricingStrategy.class);

	protected static final String PRO_PROPERTY_HOMEOWNERS = "PRO_PROPERTY_HOMEOWNERS";
	protected static final String PRO_FLOOD_COVER_HOMEOWNERS = "PRO_FLOOD_COVER_HOMEOWNERS";
	protected static final String PRO_TEMP_ACCOM_HOMEOWNERS = "PRO_TEMP_ACCOM_HOMEOWNERS";
	protected static final String PRO_BICYCLES_COVER_HOMEOWNERS = "PRO_BICYCLES_COVER_HOMEOWNERS";
	protected static final String PRO_JEWELRY_COVER_HOMEOWNERS = "PRO_JEWELRY_COVER_HOMEOWNERS";

	@Override
	public void initStrategy()
	{
		super.initStrategy();

		//Property cover required factors
		formulaFactors.put(PRO_TYPE_BUILDING_ONLY, 0.01d);
		formulaFactors.put(PRO_TYPE_BUILDING_AND_CONTENT, 0.02d);
		formulaFactors.put(PRO_TYPE_CONTENT_ONLY, 0.03d);

		// Optional product factors
		formulaFactors.put(PRO_PROPERTY_HOMEOWNERS, 1d);
		formulaFactors.put(PRO_FLOOD_COVER_HOMEOWNERS, 0.2d);
		formulaFactors.put(PRO_TEMP_ACCOM_HOMEOWNERS, 0.22d);
		formulaFactors.put(PRO_BICYCLES_COVER_HOMEOWNERS, 0.35d);
		formulaFactors.put(PRO_JEWELRY_COVER_HOMEOWNERS, 0.45d);
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

		if (requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_VALUE) != null
				&& requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_TYPE) != null
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
		final double rebuildValue = getPropertyRebuildValue(requestData);
		final double rebuildValueFactor = getPropertyRebuildValueFactor();

		final String coverRequired = requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_COVER_REQUIRED);

		double price = NumberUtils.DOUBLE_ZERO;

		if (PRO_TYPE_CONTENT_ONLY.equalsIgnoreCase(coverRequired))
		{
			price = contentValue * contentValueFactor;
		}

		if (PRO_TYPE_BUILDING_ONLY.equalsIgnoreCase(coverRequired))
		{
			price = rebuildValue * rebuildValueFactor * 2;
		}

		if (PRO_TYPE_BUILDING_AND_CONTENT.equalsIgnoreCase(coverRequired))
		{
			price = contentValue * contentValueFactor + rebuildValue * rebuildValueFactor * 2;
		}

		return price;
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

			if (itemData.getId().contains(PRO_PROPERTY_HOMEOWNERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_PROPERTY_HOMEOWNERS);
			}
			else if (itemData.getId().contains(PRO_FLOOD_COVER_HOMEOWNERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_FLOOD_COVER_HOMEOWNERS);
			}
			else if (itemData.getId().contains(PRO_TEMP_ACCOM_HOMEOWNERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_TEMP_ACCOM_HOMEOWNERS);
			}
			else if (itemData.getId().contains(PRO_BICYCLES_COVER_HOMEOWNERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_BICYCLES_COVER_HOMEOWNERS);
			}
			else if (itemData.getId().contains(PRO_JEWELRY_COVER_HOMEOWNERS))
			{
				value = Double.valueOf(basePrice) * formulaFactors.get(PRO_JEWELRY_COVER_HOMEOWNERS);
			}

			//It is the same price calculation for the optional products regarding the payment term
			productData.setPayNowPrice(Double.toString(value + BASE_PRICE));
			productData.setRecurringPrice(Double.toString(value + BASE_PRICE));


			productList.add(productData);
		}

		return productList;
	}
}

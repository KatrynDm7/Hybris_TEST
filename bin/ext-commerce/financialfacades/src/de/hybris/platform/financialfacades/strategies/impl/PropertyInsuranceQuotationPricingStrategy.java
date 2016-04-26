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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.quotation.QuotationItemResponseData;
import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Maps;


public abstract class PropertyInsuranceQuotationPricingStrategy extends AbstractQuotationPricingStrategy
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PropertyInsuranceQuotationPricingStrategy.class);

	protected static final String PRO_TYPE_BUILDING_ONLY = "Buildings-Only";
	protected static final String PRO_TYPE_BUILDING_AND_CONTENT = "Building-and-Contents";
	protected static final String PRO_TYPE_CONTENT_ONLY = "Contents-Only";
	protected static final String DEFAULT_FALLBACK_PRICE = "5";
	protected static final String PRO_STANDARD_YES = "yes";
	protected static final String PRO_CONTENTS_FACTOR = "contentValueFactor";
	protected static final String PRO_REBUILD_FACTOR = "rebuildValueFactor";
	protected static final String PRO_ANNUAL_FACTOR = "propertyAnnualFactor";
	protected static final int BASE_PRICE = 1;

	protected Map<String, Double> formulaFactors;

	@Override
	public void initStrategy()
	{
		super.initStrategy();

		formulaFactors = Maps.newHashMap();
		//Property cover required factors
		formulaFactors.put(PRO_TYPE_BUILDING_ONLY, 0.01d);
		formulaFactors.put(PRO_TYPE_BUILDING_AND_CONTENT, 0.02d);
		formulaFactors.put(PRO_TYPE_CONTENT_ONLY, 0.03d);

		formulaFactors.put(PRO_CONTENTS_FACTOR, 0.0021d);
		formulaFactors.put(PRO_REBUILD_FACTOR, 0.00006d);
		formulaFactors.put(PRO_ANNUAL_FACTOR, 9.8d);
	}

	/**
	 * Helper method used to extract Property Content Value from the requestData
	 *
	 * @param requestData
	 * @return value of property
	 */
	protected double getPropertyContentValue(final QuotationRequestData requestData)
	{

		final String coverRequired = requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_COVER_REQUIRED);
		if (PRO_TYPE_CONTENT_ONLY.equalsIgnoreCase(coverRequired) || PRO_TYPE_BUILDING_AND_CONTENT.equalsIgnoreCase(coverRequired))
		{
			validateParameterNotNullStandardMessage("Property Is Standard 50000 Content Cover",
					requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER));
			final String isStandard = requestData.getProperties().get(
					FinancialfacadesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER);
			if (PRO_STANDARD_YES.equalsIgnoreCase(isStandard))
			{
				return 50000;
			}
			else
			{
				validateParameterNotNullStandardMessage("Property Defined Content Cover",
						requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000));
				if (StringUtils.isNotEmpty(requestData.getProperties().get(
						FinancialfacadesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000)))
				{
					return Double.valueOf(
							requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000))
							.doubleValue();
				}
				else
				{
					return NumberUtils.DOUBLE_ZERO;
				}
			}
		}
		return NumberUtils.DOUBLE_ZERO;
	}

	/**
	 * Helper method used to extract Property Rebuild Value from the requestData
	 *
	 * @param requestData
	 * @return value of property
	 */
	protected double getPropertyRebuildValue(final QuotationRequestData requestData)
	{
		if (StringUtils.isNotEmpty(requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_REBUILD_COST)))
		{
			return Double.valueOf(requestData.getProperties().get(FinancialfacadesConstants.PROPERTY_DETAILS_REBUILD_COST))
					.doubleValue();
		}
		else
		{
			return NumberUtils.DOUBLE_ZERO;
		}
	}

	@Override
	protected List<QuotationItemResponseData> populateQuotationProductData(final QuotationRequestData quotationRequestData)
	{
		final List<QuotationItemResponseData> productList = super.populateQuotationProductData(quotationRequestData);

		for (final QuotationItemResponseData quotationItemResponseData : productList)
		{
			final double annualPrice = calculateAnnual(quotationRequestData);
			final double monthlyPrice = calculateMonthly(quotationRequestData);

			quotationItemResponseData.setRecurringPrice(String.valueOf(monthlyPrice));
			quotationItemResponseData.setPayNowPrice(String.valueOf(annualPrice));
		}

		return productList;
	}

	/**
	 * Helper method to get property insurance cover value factor.
	 *
	 * @return factor double value
	 */
	protected double getPropertyCoverValueFactor()
	{
		return formulaFactors.get(PRO_CONTENTS_FACTOR);
	}

	/**
	 * Helper method to get property insurance rebuild value factor.
	 *
	 * @return factor double value
	 */
	protected double getPropertyRebuildValueFactor()
	{
		return formulaFactors.get(PRO_REBUILD_FACTOR);
	}

	/**
	 * Helper method to get property insurance annual price factor.
	 *
	 * @return factor double value
	 */
	protected double getPropertyAnnualFactor()
	{
		return formulaFactors.get(PRO_ANNUAL_FACTOR);
	}

	/**
	 * Method to calculate monthly price for property insurance.
	 *
	 * @param requestData
	 *           the quotation request data
	 * @return double price value
	 */
	protected abstract double calculateMonthly(final QuotationRequestData requestData);

	/**
	 * Method to calculate annual price for property insurance.
	 *
	 * @param requestData
	 *           the quotation request data
	 * @return double price value
	 */
	protected abstract double calculateAnnual(final QuotationRequestData requestData);
}

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class TravelInsuranceQuotationPricingStrategy extends AbstractQuotationPricingStrategy
{
	private static final Logger LOG = Logger.getLogger(TravelInsuranceQuotationPricingStrategy.class);

	protected static final int BASE_FACTOR = 5;

	private Map<String, Double> formulaFactors;
	private Map<String, String> factorProperties;

	/**
	 * Initialization method - called from the Spring init-method attribute. This sets up all of the various weighting
	 * factors for the price calculation formula, as well as defining which property may be used to override that factor
	 * with a new, run-time value.
	 */
	@Override
	public void initStrategy()
	{
		super.initStrategy();

		LOG.info("Initializing bean - setting up base formula factor values");

		formulaFactors = new HashMap<String, Double>();

		//Destination Factors
		formulaFactors.put(FinancialfacadesConstants.TRA_EU, 0.1);
		formulaFactors.put(FinancialfacadesConstants.TRA_ANZAC, 0.2);
		formulaFactors.put(FinancialfacadesConstants.TRA_WW_EXC, 0.2);
		formulaFactors.put(FinancialfacadesConstants.TRA_WW_INC, 0.3);
		formulaFactors.put(FinancialfacadesConstants.TRA_UK, 0.2);

		//Number of days Factors
		formulaFactors.put(FinancialfacadesConstants.TRA_DAYS_0_31, 0.1);
		formulaFactors.put(FinancialfacadesConstants.TRA_DAYS_31_100, 0.2);
		formulaFactors.put(FinancialfacadesConstants.TRA_DAYS_100_365, 0.3);

		//Age Factors
		formulaFactors.put(FinancialfacadesConstants.TRA_AGE_0_17, 0.1);
		formulaFactors.put(FinancialfacadesConstants.TRA_AGE_18_65, 0.2);
		formulaFactors.put(FinancialfacadesConstants.TRA_AGE_OVER_65, 0.5);

		//Number of travel Factors
		formulaFactors.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, 10.0);

		//Coverage Factors
		formulaFactors.put("cancellation", 1000.0);
		formulaFactors.put("wintersports", 20.0);
		formulaFactors.put("businesscover", 20.0);
		formulaFactors.put("valuablesextensioncover", 20.0);
		formulaFactors.put("hazardousactivitiescover", 20.0);
		formulaFactors.put("golfcover", 20.0);
		formulaFactors.put("excesswaivercoverage", 20.0);

		// Optional product factors
		formulaFactors.put(FinancialfacadesConstants.TRA_WINTER, .1);
		formulaFactors.put(FinancialfacadesConstants.TRA_GOLF, .15);
		formulaFactors.put(FinancialfacadesConstants.TRA_BUSINESS, .2);
		formulaFactors.put(FinancialfacadesConstants.TRA_VALUABLES, .05);
		formulaFactors.put(FinancialfacadesConstants.TRA_HAZARDOUS, .1);
		formulaFactors.put(FinancialfacadesConstants.TRA_EXCESS, .15);

		/*
		 * Certain factors have properties which may override then, so we construct a map of incoming values and their
		 * asssociated properties
		 */

		LOG.info("Initializing bean - setting up override properties");

		factorProperties = new HashMap<String, String>();
		factorProperties.put(FinancialfacadesConstants.TRA_EU, FinancialfacadesConstants.PROPERTY_DEST_EU);
		factorProperties.put(FinancialfacadesConstants.TRA_ANZAC, FinancialfacadesConstants.PROPERTY_DEST_ANZAC);
		factorProperties.put(FinancialfacadesConstants.TRA_WW_INC, FinancialfacadesConstants.PROPERTY_DEST_WW_INC);
		factorProperties.put(FinancialfacadesConstants.TRA_WW_EXC, FinancialfacadesConstants.PROPERTY_DEST_WW_EXC);
		factorProperties.put(FinancialfacadesConstants.TRA_UK, FinancialfacadesConstants.PROPERTY_DEST_UK);

		factorProperties.put(FinancialfacadesConstants.TRA_DAYS_0_31, FinancialfacadesConstants.PROPERTY_DAYS_0_31);
		factorProperties.put(FinancialfacadesConstants.TRA_DAYS_31_100, FinancialfacadesConstants.PROPERTY_DAYS_32_100);
		factorProperties.put(FinancialfacadesConstants.TRA_DAYS_100_365, FinancialfacadesConstants.PROPERTY_DAYS_101_365);

		factorProperties.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS,
				FinancialfacadesConstants.PROPERTY_NO_TRAVELLERS);

		factorProperties.put(FinancialfacadesConstants.TRA_AGE_0_17, FinancialfacadesConstants.PROPERTY_AGE_0_17);
		factorProperties.put(FinancialfacadesConstants.TRA_AGE_18_65, FinancialfacadesConstants.PROPERTY_AGE_18_65);
		factorProperties.put(FinancialfacadesConstants.TRA_AGE_OVER_65, FinancialfacadesConstants.PROPERTY_AGE_65_PLUS);

	}

	/**
	 * Derives the calculation weighting factor value for a given factor name. It does this by : - checking to see if the
	 * factor has been associated with a system property - if so, then it returns the overridden factor value specified
	 * in the property - if not then it returns the standard value as defined in the <formulaFactors> map.
	 *
	 * @param factorName
	 *           the factor that we are looking for
	 * @return the value as specified either in the override property, or the help map of factors
	 */
	public Double getFormulaFactor(final String factorName)
	{
		final Double internalValue = formulaFactors.get(factorName);
		if (factorProperties.keySet().contains(factorName) && getConfigurationService() != null
				&& getConfigurationService().getConfiguration() != null)
		{
			final String propertyName = factorProperties.get(factorName);
			return getConfigurationService().getConfiguration().getDouble(propertyName, internalValue);
		}
		return internalValue;
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

			// main product plan
			productData.setId(itemData.getId());
			Double value = 2d;

			if (itemData.getId().indexOf(FinancialfacadesConstants.TRA_WINTER) != -1)
			{
				value = Double.valueOf(basePrice) * getFormulaFactor(FinancialfacadesConstants.TRA_WINTER);
			}
			else if (itemData.getId().indexOf(FinancialfacadesConstants.TRA_GOLF) != -1)
			{
				value = Double.valueOf(basePrice) * getFormulaFactor(FinancialfacadesConstants.TRA_GOLF);
			}
			else if (itemData.getId().indexOf(FinancialfacadesConstants.TRA_BUSINESS) != -1)
			{
				value = Double.valueOf(basePrice) * getFormulaFactor(FinancialfacadesConstants.TRA_BUSINESS);
			}
			else if (itemData.getId().indexOf(FinancialfacadesConstants.TRA_VALUABLES) != -1)
			{
				value = Double.valueOf(basePrice) * getFormulaFactor(FinancialfacadesConstants.TRA_VALUABLES);
			}
			else if (itemData.getId().indexOf(FinancialfacadesConstants.TRA_HAZARDOUS) != -1)
			{
				value = Double.valueOf(basePrice) * getFormulaFactor(FinancialfacadesConstants.TRA_HAZARDOUS);
			}
			else if (itemData.getId().indexOf(FinancialfacadesConstants.TRA_EXCESS) != -1)
			{
				value = Double.valueOf(basePrice) * getFormulaFactor(FinancialfacadesConstants.TRA_EXCESS);
			}

			value = (double) Math.round(value * 100) / 100;

			if (value < 1)
			{
				value = 2d;
			}

			productData.setPayNowPrice(Double.toString(Math.round(value * 100) / 100));


			productList.add(productData);
		}

		return productList;
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

		if (requestData.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_DAYS) != null
				&& requestData.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION) != null
				&& requestData.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES) != null
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
		/*
		 * The formula for travel each item is ( ( destinationFactor + daysfactor + (ageFactor for each traveller ) +
		 * (travellersFactor) + ) base price )
		 *
		 * ((item.getCoverage(cancellation/1000)+ item.getCoverage(wintersports/20) + item.getCoverage(golfcover/20) +
		 * item.getCoverage(businesscover/20) + item.getCoverage(hazardous/20) + item.getCoverage(valuablesextension/20) +
		 * item.getCoverage(excesswaiver/20) )
		 */

		final double price = (getCostOfTripFactor(requestData) + getDestinationFactor(requestData) + getDayFactor(requestData)
				+ getAgeFactor(requestData) + getTravellerFactor(requestData))
				* BASE_FACTOR * getCoverageFactor(itemData);

		return Double.toString(Math.round(price * 100) / 100);
	}

	/**
	 * @param requestData
	 * @return double
	 */
	private double getCostOfTripFactor(final QuotationRequestData requestData)
	{
		if (getConfigurationService() != null && getConfigurationService().getConfiguration() != null)
		{
			final double costOfTripDivisor = getConfigurationService().getConfiguration().getDouble(
					FinancialfacadesConstants.COST_OF_TRIP_DIVISOR, 2000.00);
			final String costOfTrip = requestData.getProperties().get(FinancialfacadesConstants.TRIP_COST);
			if (costOfTrip != null)
			{
				final double costOfTripVal = Double.parseDouble(costOfTrip);
				return (costOfTripVal / costOfTripDivisor) + 1;
			}
		}
		return 0;
	}

	/**
	 * Helper method used to extract destination calculation factor based on request data
	 *
	 * @param requestData
	 * @return destination calculation factor
	 */
	protected double getDestinationFactor(final QuotationRequestData requestData)
	{
		final String destination = requestData.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION);

		return getFormulaFactor(destination);
	}

	/**
	 * Helper method used to extract number of travel days calculation factor based on request data
	 *
	 * @param requestData
	 * @return number of days calculation factor
	 */
	protected double getDayFactor(final QuotationRequestData requestData)
	{
		final int days = MapUtils.getIntValue(requestData.getProperties(), FinancialfacadesConstants.TRIP_DETAILS_NO_OF_DAYS, 1);

		final String daysKey = days < 32 ? "0-31" : days > 100 ? "100-365" : "31-100";

		return getFormulaFactor(daysKey);
	}

	/**
	 * Helper method used to extract ages(all travellers) calculation factor based on request data
	 *
	 * @param requestData
	 * @return age calculation factor
	 */
	protected double getAgeFactor(final QuotationRequestData requestData)
	{
		final String[] ages = StringUtils.split(
				requestData.getProperties().get(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES), DEFAULT_DATA_SEPERATOR);

		double factor = 0;

		for (final String age : ages)
		{
			final String ageKey = Integer.parseInt(age) < 18 ? "0-17" : Integer.parseInt(age) > 65 ? "65+" : "18-65";

			factor = factor + getFormulaFactor(ageKey);
		}

		return factor;
	}

	/**
	 * Helper method used to extract number of travellers calculation factor based on request data
	 *
	 * @param requestData
	 * @return number of travellers calculation factor
	 */
	protected double getTravellerFactor(final QuotationRequestData requestData)
	{
		final int noOfTravellers = Integer.parseInt(requestData.getProperties().get(
				FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS));

		return noOfTravellers / getFormulaFactor(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS);
	}

	/**
	 * Helper method used to extract coverage calculation factor based on request data
	 *
	 * @param itemData
	 * @return coverage calculation factor
	 */
	protected double getCoverageFactor(final QuotationItemRequestData itemData)
	{
		double factor = 0;

		for (final String key : itemData.getProperties().keySet())
		{
			if (getFormulaFactor(key) != null)
			{
				factor = factor + (Double.parseDouble(itemData.getProperties().get(key)) / getFormulaFactor(key));
			}
		}
		return factor;
	}

}

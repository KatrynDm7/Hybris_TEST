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

import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Maps;


/**
 * The class of TravelInsuranceAddToCartStrategy.
 */
public class TravelInsuranceAddToCartStrategy extends AbstractInsuranceAddToCartStrategy
{
	private static final Logger LOG = Logger.getLogger(TravelInsuranceAddToCartStrategy.class);


	@Override
	protected void addToCartInternal(final Map<String, Object> properties)
	{
		if (properties.containsKey(PROPERTY_PRODUCT_CODE) && properties.containsKey(PROPERTY_BUNDLE_NO))
		{
			persistInsuranceInformation();
		}
	}

	@Override
	protected void populateInsuranceDetailsInformation(final InsuranceQuoteModel quoteModel) throws YFormServiceException
	{

		final Map<String, Object> infoMap = Maps.newHashMap();

		final DateTimeFormatter formatter = DateTimeFormat.forPattern(FinancialfacadesConstants.INSURANCE_GENERIC_DATE_FORMAT);

		infoMap.put(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION,
				getSessionService().getAttribute(FinancialfacadesConstants.TRIP_DETAILS_DESTINATION));

		final String startDate = getSessionService().getAttribute(FinancialfacadesConstants.TRIP_DETAILS_START_DATE);
		final String endDate = getSessionService().getAttribute(FinancialfacadesConstants.TRIP_DETAILS_END_DATE);
		final String noOfTravellers = getSessionService().getAttribute(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS);
		final String noOfDays = getSessionService().getAttribute(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_DAYS);
		final String tripCost = getSessionService().getAttribute(FinancialfacadesConstants.TRIP_COST);

		final Collection<String> travellerAges = getSessionService().getAttribute(
				FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES);

		if (StringUtils.isNotEmpty(startDate))
		{
			infoMap.put(FinancialfacadesConstants.TRIP_DETAILS_START_DATE, formatter.parseDateTime(startDate).toDate());
		}

		if (StringUtils.isNotEmpty(endDate) && !FinancialfacadesConstants.NOT_APPLICABLE_TEXT.equalsIgnoreCase(endDate))
		{
			infoMap.put(FinancialfacadesConstants.TRIP_DETAILS_END_DATE, formatter.parseDateTime(endDate).toDate());
		}

		if (StringUtils.isNotEmpty(noOfTravellers))
		{
			infoMap.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, Integer.valueOf(noOfTravellers));
		}

		if (StringUtils.isNotEmpty(noOfDays))
		{
			infoMap.put(FinancialfacadesConstants.TRIP_DETAILS_NO_OF_DAYS, Integer.valueOf(noOfDays));
		}

		if (CollectionUtils.isNotEmpty(travellerAges))
		{
			infoMap.put(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES, travellerAges);
			infoMap.put(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES_FOR_PRE_FORM_POPULATE, travellerAges);
		}

		if (StringUtils.isNotBlank(tripCost))
		{
			infoMap.put(FinancialfacadesConstants.TRIP_COST, tripCost);
		}

		quoteModel.setProperties(infoMap);
	}

}

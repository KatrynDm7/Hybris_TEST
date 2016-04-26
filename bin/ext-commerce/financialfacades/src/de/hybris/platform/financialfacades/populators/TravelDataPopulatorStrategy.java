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

import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialservices.enums.QuoteType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


public class TravelDataPopulatorStrategy extends InsuranceDataPopulatorStrategy
{

	protected static final String CHECKOUT_CART_TITLE_TRAVEL = "checkout.cart.title.travel";

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.financialacceleratorstorefront.populators.InsuranceDataPopulatorStrategy#getInsuranceInfo(de
	 * .hybris.platform.commercefacades.quotation.InsuranceQuoteData, java.util.Map)
	 */
	@Override
	public void processInsuranceQuoteData(final InsuranceQuoteData quoteData, final Map<String, Object> infoMap)
	{
		final String tripDestination = MapUtils.getString(infoMap, FinancialfacadesConstants.TRIP_DETAILS_DESTINATION,
				StringUtils.EMPTY);
		final Integer noTravellers = MapUtils.getInteger(infoMap, FinancialfacadesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS,
				NumberUtils.INTEGER_ZERO);
		final Integer noDays = MapUtils.getInteger(infoMap, FinancialfacadesConstants.TRIP_DETAILS_NO_OF_DAYS,
				NumberUtils.INTEGER_ZERO);

		if (!StringUtils.isEmpty(tripDestination) && noTravellers != null && noDays != null && noTravellers > 0 && noDays > 0)
		{
			// We are pretty certain at this point that we are dealing with travel insurance
			quoteData.setQuoteType(QuoteType.TRAVEL);
			quoteData.setQuoteTitle(CHECKOUT_CART_TITLE_TRAVEL);
			quoteData.setTripDestination(tripDestination);
			quoteData.setTripNoOfTravellers(noTravellers);
			quoteData.setTripNoOfDays(noDays);

			quoteData.setTripTravellersAge((List) infoMap.get(FinancialfacadesConstants.TRIP_DETAILS_TRAVELLER_AGES));

			final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormatForDisplay());
			if (infoMap.containsKey(FinancialfacadesConstants.TRIP_DETAILS_START_DATE))
			{
				final Object obj = MapUtils.getObject(infoMap, FinancialfacadesConstants.TRIP_DETAILS_START_DATE);
				if (obj instanceof Date)
				{
					final String formattedDate = sdf.format((Date) obj);
					quoteData.setTripStartDate(formattedDate);
					quoteData.setStartDate((Date) obj);
					quoteData.setFormattedStartDate(formattedDate);
				}
			}

			if (infoMap.containsKey(FinancialfacadesConstants.TRIP_DETAILS_END_DATE))
			{
				final Object obj = MapUtils.getObject(infoMap, FinancialfacadesConstants.TRIP_DETAILS_END_DATE);
				if (obj instanceof Date)
				{
					quoteData.setTripEndDate(sdf.format((Date) obj));
				}
			}
		}
	}

}

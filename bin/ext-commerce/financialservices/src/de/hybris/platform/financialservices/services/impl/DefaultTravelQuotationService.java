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
package de.hybris.platform.financialservices.services.impl;

import de.hybris.platform.financialservices.constants.FinancialservicesConstants;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;


/**
 * This implements the quotation service for the Travel Insurance type
 *
 */
public class DefaultTravelQuotationService extends AbstractQuotationService
{

	/**
	 * Helper method used to populate all the mandatory form information required for the quotation engine.
	 *
	 * @return returns Map contains all the form information retrieved from session
	 */
	@Override
	protected Map<String, String> populateCommonProperties(final InsuranceQuoteModel quoteModel)
	{
		final Map<String, String> property = new HashMap<String, String>();

		// Prepare generic parameters
		if (quoteModel != null && MapUtils.isNotEmpty(quoteModel.getProperties()))
		{
			property.put(
					FinancialservicesConstants.TRIP_COST,
					MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.TRIP_COST,
							MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.TRIP_COST, StringUtils.EMPTY)));
			property.put(FinancialservicesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, MapUtils.getString(quoteModel.getProperties(),
					FinancialservicesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS, StringUtils.EMPTY));
			property.put(FinancialservicesConstants.TRIP_DETAILS_NO_OF_DAYS, MapUtils.getString(quoteModel.getProperties(),
					FinancialservicesConstants.TRIP_DETAILS_NO_OF_DAYS, StringUtils.EMPTY));
			property.put(FinancialservicesConstants.TRIP_DETAILS_DESTINATION, MapUtils.getString(quoteModel.getProperties(),
					FinancialservicesConstants.TRIP_DETAILS_DESTINATION, StringUtils.EMPTY));
			property.put(FinancialservicesConstants.TRIP_DETAILS_TRAVELLER_AGES, StringUtils.join((List) quoteModel.getProperties()
					.get(FinancialservicesConstants.TRIP_DETAILS_TRAVELLER_AGES), ","));
		}
		//else search for the session before added to the cart
		else
		{
			property.put(FinancialservicesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS,
					(String) getSessionService().getAttribute(FinancialservicesConstants.TRIP_DETAILS_NO_OF_TRAVELLERS));
			property.put(FinancialservicesConstants.TRIP_DETAILS_NO_OF_DAYS,
					(String) getSessionService().getAttribute(FinancialservicesConstants.TRIP_DETAILS_NO_OF_DAYS));
			property.put(FinancialservicesConstants.TRIP_DETAILS_DESTINATION,
					(String) getSessionService().getAttribute(FinancialservicesConstants.TRIP_DETAILS_DESTINATION));
			property.put(FinancialservicesConstants.TRIP_DETAILS_TRAVELLER_AGES, StringUtils.join((List) getSessionService()
					.getAttribute(FinancialservicesConstants.TRIP_DETAILS_TRAVELLER_AGES), ","));
			property.put(FinancialservicesConstants.TRIP_COST,
					(String) getSessionService().getAttribute(FinancialservicesConstants.TRIP_COST));


		}

		return property;
	}

}

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
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;


/**
 * This implements the quotation service for the Property Insurance type(s)
 *
 */
public class DefaultPropertyQuotationService extends AbstractQuotationService
{

	/**
	 * Helper method used to populate all the mandatory form information required for the quotation engine.
	 *
	 * @return returns Map contains all the form information retrieved from session
	 */
	@Override
	protected Map<String, String> populateCommonProperties(final InsuranceQuoteModel quote)
	{
		final Map<String, String> property = new HashMap<String, String>();

		// Prepare generic parameters
		if (quote != null && MapUtils.isNotEmpty(quote.getProperties()))
		{

			property.put(FinancialservicesConstants.PROPERTY_DETAILS_VALUE,
					MapUtils.getString(quote.getProperties(), FinancialservicesConstants.PROPERTY_DETAILS_VALUE, StringUtils.EMPTY));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_TYPE,
					MapUtils.getString(quote.getProperties(), FinancialservicesConstants.PROPERTY_DETAILS_TYPE, StringUtils.EMPTY));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_REBUILD_COST, MapUtils.getString(quote.getProperties(),
					FinancialservicesConstants.PROPERTY_DETAILS_REBUILD_COST, StringUtils.EMPTY));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER, MapUtils.getString(
					quote.getProperties(), FinancialservicesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER,
					StringUtils.EMPTY));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000, MapUtils.getString(
					quote.getProperties(), FinancialservicesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000,
					StringUtils.EMPTY));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_COVER_REQUIRED, MapUtils.getString(quote.getProperties(),
					FinancialservicesConstants.PROPERTY_DETAILS_COVER_REQUIRED, StringUtils.EMPTY));
		}
		//else search for the session before added to the cart
		else
		{
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_VALUE,
					(String) getSessionService().getAttribute(FinancialservicesConstants.PROPERTY_DETAILS_VALUE));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_TYPE,
					(String) getSessionService().getAttribute(FinancialservicesConstants.PROPERTY_DETAILS_TYPE));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_REBUILD_COST,
					(String) getSessionService().getAttribute(FinancialservicesConstants.PROPERTY_DETAILS_REBUILD_COST));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER, (String) getSessionService()
					.getAttribute(FinancialservicesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000, (String) getSessionService()
					.getAttribute(FinancialservicesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000));
			property.put(FinancialservicesConstants.PROPERTY_DETAILS_COVER_REQUIRED,
					(String) getSessionService().getAttribute(FinancialservicesConstants.PROPERTY_DETAILS_COVER_REQUIRED));

		}

		return property;
	}

}

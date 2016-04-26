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


public class DefaultAutoQuotationService extends AbstractQuotationService
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

			property.put(FinancialservicesConstants.AUTO_VEHICLE_VALUE,
					MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.AUTO_VEHICLE_VALUE, StringUtils.EMPTY));

			property.put(FinancialservicesConstants.AUTO_DRIVER_DOB,
					MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.AUTO_DRIVER_DOB, StringUtils.EMPTY));
			property.put(FinancialservicesConstants.AUTO_COVER_START,
					MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.AUTO_COVER_START, StringUtils.EMPTY));

		}
		//else search for the session before added to the cart
		else
		{
			property.put(FinancialservicesConstants.AUTO_VEHICLE_VALUE,
					(String) getSessionService().getAttribute(FinancialservicesConstants.AUTO_VEHICLE_VALUE));
			property.put(FinancialservicesConstants.AUTO_DRIVER_DOB,
					(String) getSessionService().getAttribute(FinancialservicesConstants.AUTO_DRIVER_DOB));
			property.put(FinancialservicesConstants.AUTO_COVER_START,
					(String) getSessionService().getAttribute(FinancialservicesConstants.AUTO_COVER_START));
		}

		return property;
	}
}

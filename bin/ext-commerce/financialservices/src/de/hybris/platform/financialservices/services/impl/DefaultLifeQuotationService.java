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

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;


public class DefaultLifeQuotationService extends AbstractQuotationService
{

	/**
	 * Helper method used to populate all the mandatory form information required for the quotation engine.
	 *
	 * @return returns Map contains all the form information retrieved from session
	 */
	@Override
	protected Map<String, String> populateCommonProperties(final InsuranceQuoteModel quoteModel)
	{
		final Map<String, String> property = Maps.newHashMap();

		// Prepare generic parameters
		if (quoteModel != null && MapUtils.isNotEmpty(quoteModel.getProperties()))
		{

			property.put(FinancialservicesConstants.LIFE_WHO_COVERED,
					MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.LIFE_WHO_COVERED, StringUtils.EMPTY));

			property.put(FinancialservicesConstants.LIFE_COVERAGE_REQUIRE, MapUtils.getString(quoteModel.getProperties(),
					FinancialservicesConstants.LIFE_COVERAGE_REQUIRE, StringUtils.EMPTY));

			property.put(FinancialservicesConstants.LIFE_MAIN_SMOKE,
					MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.LIFE_MAIN_SMOKE, StringUtils.EMPTY));

			property.put(FinancialservicesConstants.LIFE_SECOND_SMOKE,
					MapUtils.getString(quoteModel.getProperties(), FinancialservicesConstants.LIFE_SECOND_SMOKE, StringUtils.EMPTY));

		}
		//else search for the session before added to the cart
		else
		{
			property.put(FinancialservicesConstants.LIFE_WHO_COVERED,
					(String) getSessionService().getAttribute(FinancialservicesConstants.LIFE_WHO_COVERED));
			property.put(FinancialservicesConstants.LIFE_COVERAGE_REQUIRE,
					(String) getSessionService().getAttribute(FinancialservicesConstants.LIFE_COVERAGE_REQUIRE));
			property.put(FinancialservicesConstants.LIFE_MAIN_SMOKE,
					(String) getSessionService().getAttribute(FinancialservicesConstants.LIFE_MAIN_SMOKE));
			property.put(FinancialservicesConstants.LIFE_SECOND_SMOKE,
					(String) getSessionService().getAttribute(FinancialservicesConstants.LIFE_SECOND_SMOKE));

		}

		return property;
	}
}

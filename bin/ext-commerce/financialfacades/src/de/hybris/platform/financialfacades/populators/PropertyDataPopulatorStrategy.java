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
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;


/**
 * @author david.boland
 *
 */
public class PropertyDataPopulatorStrategy extends InsuranceDataPopulatorStrategy
{

	protected static final String CHECKOUT_CART_TITLE_RENTERS = "checkout.cart.title.renters";
	protected static final String CHECKOUT_CART_TITLE_HOMEOWNERS = "checkout.cart.title.homeowners";

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
		final String addressLine1 = MapUtils.getString(infoMap, FinancialfacadesConstants.PROPERTY_ADDRESS1, StringUtils.EMPTY);
		final String coverRequired = MapUtils.getString(infoMap, FinancialfacadesConstants.PROPERTY_DETAILS_COVER_REQUIRED,
				StringUtils.EMPTY);
		final String propertyType = MapUtils.getString(infoMap, FinancialfacadesConstants.PROPERTY_DETAILS_TYPE, StringUtils.EMPTY);
		final String propertyValue = MapUtils.getString(infoMap, FinancialfacadesConstants.PROPERTY_DETAILS_VALUE,
				StringUtils.EMPTY);

		if (!StringUtils.isEmpty(addressLine1))
		{
			if (!StringUtils.isEmpty(propertyValue))
			{
				quoteData.setQuoteType(QuoteType.PROPERTY_HOMEOWNERS);
				quoteData.setQuoteTitle(CHECKOUT_CART_TITLE_HOMEOWNERS);
			}
			else
			{
				quoteData.setQuoteType(QuoteType.PROPERTY_RENTERS);
				quoteData.setQuoteTitle(CHECKOUT_CART_TITLE_RENTERS);
			}

			quoteData.setPropertyAddressLine1(addressLine1);
			quoteData.setPropertyCoverRequired(coverRequired);
			quoteData.setPropertyType(propertyType);
			quoteData.setPropertyValue(propertyValue);

			final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormatForDisplay());
			if (infoMap.containsKey(FinancialfacadesConstants.PROPERTY_DETAILS_START_DATE))
			{
				final Object obj = MapUtils.getObject(infoMap, FinancialfacadesConstants.PROPERTY_DETAILS_START_DATE);
				if (obj instanceof Date)
				{
					final String formattedDate = sdf.format((Date) obj);
					quoteData.setPropertyStartDate(formattedDate);
					quoteData.setStartDate((Date) obj);
					quoteData.setFormattedStartDate(formattedDate);
				}
			}
		}
	}
}

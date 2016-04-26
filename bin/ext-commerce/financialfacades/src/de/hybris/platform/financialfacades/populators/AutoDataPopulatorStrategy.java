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

import de.hybris.platform.commercefacades.insurance.data.AutoDetailData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialservices.enums.QuoteType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Class of AutoDataPopulatorStrategy.
 */
public class AutoDataPopulatorStrategy extends InsuranceDataPopulatorStrategy
{

	protected static final String CHECKOUT_CART_TITLE_AUTO = "checkout.cart.title.auto";

	@Override
	public void processInsuranceQuoteData(final InsuranceQuoteData quoteData, final Map<String, Object> infoMap)
	{
		if (infoMap.get(FinancialfacadesConstants.AUTO_VEHICLE_MAKE) != null)
		{
			quoteData.setQuoteType(QuoteType.AUTO);
			quoteData.setQuoteTitle(CHECKOUT_CART_TITLE_AUTO);

			final String vehicleRegistration = MapUtils.getString(infoMap, FinancialfacadesConstants.AUTO_VEHICLE_LICENSE,
					StringUtils.EMPTY);
			final String autoMake = MapUtils.getString(infoMap, FinancialfacadesConstants.AUTO_VEHICLE_MAKE, StringUtils.EMPTY);
			final String autoModel = MapUtils.getString(infoMap, FinancialfacadesConstants.AUTO_VEHICLE_MODEL, StringUtils.EMPTY);
			final String autoYear = MapUtils.getString(infoMap, FinancialfacadesConstants.AUTO_VEHICLE_YEAR, StringUtils.EMPTY);
			final String autoValue = MapUtils.getString(infoMap, FinancialfacadesConstants.AUTO_VEHICLE_VALUE, StringUtils.EMPTY);
			final String autoState = MapUtils.getString(infoMap, FinancialfacadesConstants.AUTO_STATE, StringUtils.EMPTY);

			final Date autoCoverStart = findPossibleDate(infoMap, FinancialfacadesConstants.AUTO_COVER_START);
			final Date autoDOB = findPossibleDate(infoMap, FinancialfacadesConstants.AUTO_DRIVER_DOB);

			final AutoDetailData autoDetailData = new AutoDetailData();

			autoDetailData.setAutoLicense(vehicleRegistration);
			autoDetailData.setAutoMake(autoMake);
			autoDetailData.setAutoModel(autoModel);
			autoDetailData.setAutoYear(autoYear);
			autoDetailData.setAutoPrice(autoValue);
			autoDetailData.setAutoState(autoState);

			final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormatForDisplay());

			if (autoCoverStart != null)
			{
				final String formattedDate = sdf.format(autoCoverStart);
				autoDetailData.setAutoCoverStart(autoCoverStart);
				autoDetailData.setAutoFormattedCoverStart(formattedDate);

			}

			if (autoDOB != null)
			{
				final String formattedDate = sdf.format(autoDOB);
				autoDetailData.setAutoDateOfBirth(autoDOB);
				autoDetailData.setAutoFormattedDateOfBirth(formattedDate);

			}

			quoteData.setAutoDetail(autoDetailData);
		}
	}
}

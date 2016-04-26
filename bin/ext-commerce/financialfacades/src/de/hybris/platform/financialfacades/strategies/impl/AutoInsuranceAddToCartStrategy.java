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

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Maps;


/**
 * The class of AutoInsuranceAddToCartStrategy.
 */
public class AutoInsuranceAddToCartStrategy extends AbstractInsuranceAddToCartStrategy
{
	@Override
	protected void populateInsuranceDetailsInformation(final InsuranceQuoteModel quoteModel) throws YFormServiceException
	{
		final Map<String, Object> infoMap = Maps.newHashMap();

		final DateTimeFormatter formatter = DateTimeFormat.forPattern(FinancialfacadesConstants.INSURANCE_GENERIC_DATE_FORMAT);

		final String vehicleValue = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_VEHICLE_VALUE);
		final String driverDob = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_DRIVER_DOB);
		final String vehicleMake = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_VEHICLE_MAKE);
		final String vehicleModel = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_VEHICLE_MODEL);
		final String vehicleYear = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_VEHICLE_YEAR);
		final String vehicleLicense = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_VEHICLE_LICENSE);
		final String state = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_STATE);
		final String coverageStartDate = getSessionService().getAttribute(FinancialfacadesConstants.AUTO_COVER_START);

		if (StringUtils.isNotEmpty(driverDob))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_DRIVER_DOB, formatter.parseDateTime(driverDob).toLocalDate());
		}

		if (StringUtils.isNotEmpty(coverageStartDate))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_COVER_START, formatter.parseDateTime(coverageStartDate).toLocalDate());
		}

		if (StringUtils.isNotEmpty(vehicleValue))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_VEHICLE_VALUE, vehicleValue);
		}

		if (StringUtils.isNotEmpty(vehicleMake))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_VEHICLE_MAKE, vehicleMake);
		}

		if (StringUtils.isNotEmpty(vehicleModel))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_VEHICLE_MODEL, vehicleModel);
		}

		if (StringUtils.isNotEmpty(vehicleYear))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_VEHICLE_YEAR, vehicleYear);
		}

		if (StringUtils.isNotEmpty(vehicleLicense))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_VEHICLE_LICENSE, vehicleLicense);
		}

		if (StringUtils.isNotEmpty(state))
		{
			infoMap.put(FinancialfacadesConstants.AUTO_STATE, state);
		}

		quoteModel.setProperties(infoMap);
	}

	@Override
	protected void addToCartInternal(final Map<String, Object> properties)
	{
		if (properties.containsKey(PROPERTY_PRODUCT_CODE) && properties.containsKey(PROPERTY_BUNDLE_NO))
		{
			persistInsuranceInformation();
		}
	}
}

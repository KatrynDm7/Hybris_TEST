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

import de.hybris.platform.financialservices.constants.FinancialservicesConstants;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;


/**
 * The class of LifeInsuranceAddToCartStrategy.
 */
public class LifeInsuranceAddToCartStrategy extends AbstractInsuranceAddToCartStrategy
{
	
    protected static final String WHO_COVERED_LEVEL_TWO = "yourself and second person";
    
    @Override
	protected void populateInsuranceDetailsInformation(final InsuranceQuoteModel quoteModel) throws YFormServiceException
	{
		final Map<String, Object> infoMap = Maps.newHashMap();

		final String coverageRequired = getSessionService().getAttribute(FinancialservicesConstants.LIFE_COVERAGE_REQUIRE);
		final String whoCovered = getSessionService().getAttribute(FinancialservicesConstants.LIFE_WHO_COVERED);
		final String mainSmoke = getSessionService().getAttribute(FinancialservicesConstants.LIFE_MAIN_SMOKE);
		final String secondSmoke = getSessionService().getAttribute(FinancialservicesConstants.LIFE_SECOND_SMOKE);
        final String coverageLast = getSessionService().getAttribute(FinancialservicesConstants.LIFE_COVERAGE_LAST);
        final String coverageStartDate = getSessionService().getAttribute(FinancialservicesConstants.LIFE_COVERAGE_START_DATE);
        final String relationship = getSessionService().getAttribute(FinancialservicesConstants.LIFE_RELATIONSHIP);
        final String mainDob = getSessionService().getAttribute(FinancialservicesConstants.LIFE_MAIN_DOB);
        final String secondDob = getSessionService().getAttribute(FinancialservicesConstants.LIFE_SECOND_DOB);
        

		if (StringUtils.isNotEmpty(coverageRequired))
		{
			infoMap.put(FinancialservicesConstants.LIFE_COVERAGE_REQUIRE, coverageRequired);
		}

		if (StringUtils.isNotEmpty(whoCovered))
		{
			infoMap.put(FinancialservicesConstants.LIFE_WHO_COVERED, whoCovered);
		}

		if (StringUtils.isNotEmpty(mainSmoke))
		{
			infoMap.put(FinancialservicesConstants.LIFE_MAIN_SMOKE, mainSmoke);
		}

        if (StringUtils.isNotEmpty(coverageLast))
        {
            infoMap.put(FinancialservicesConstants.LIFE_COVERAGE_LAST, coverageLast);
        }
        
		if (StringUtils.isNotEmpty(coverageStartDate))
		{
			infoMap.put(FinancialservicesConstants.LIFE_COVERAGE_START_DATE, coverageStartDate);
		}
        
        if (StringUtils.isNotEmpty(mainDob))
        {
			infoMap.put(FinancialservicesConstants.LIFE_MAIN_DOB, mainDob);
		}

        if (WHO_COVERED_LEVEL_TWO.equalsIgnoreCase(whoCovered))
        {
            if (StringUtils.isNotEmpty(secondSmoke))
            {
                infoMap.put(FinancialservicesConstants.LIFE_SECOND_SMOKE, secondSmoke);
            }

            if (StringUtils.isNotEmpty(secondDob))
            {
                infoMap.put(FinancialservicesConstants.LIFE_SECOND_DOB, secondDob);
            }

            if (StringUtils.isNotEmpty(relationship))
            {
                infoMap.put(FinancialservicesConstants.LIFE_RELATIONSHIP, relationship);
            }
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

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

import de.hybris.platform.commercefacades.insurance.data.LifeDetailData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.financialservices.constants.FinancialservicesConstants;
import de.hybris.platform.financialservices.enums.QuoteType;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;


/**
 * Class of LifeDataPopulatorStrategy.
 */
public class LifeDataPopulatorStrategy extends InsuranceDataPopulatorStrategy
{
    
	protected static final String CHECKOUT_CART_TITLE_AUTO = "checkout.cart.title.life";

	@Override
	public void processInsuranceQuoteData(InsuranceQuoteData quoteData, Map<String, Object> infoMap)
	{
		if (validate(infoMap))
        {
            quoteData.setQuoteType(QuoteType.LIFE);
            quoteData.setQuoteTitle(CHECKOUT_CART_TITLE_AUTO);

            final String who = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_WHO_COVERED,
					StringUtils.EMPTY);
            final String coverageRequired = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_COVERAGE_REQUIRE, StringUtils.EMPTY);
            final String coverageLast = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_COVERAGE_LAST, StringUtils.EMPTY);
            final String startDate = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_COVERAGE_START_DATE, StringUtils.EMPTY);
            final String mainDob = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_MAIN_DOB, StringUtils.EMPTY);
            final String secondDob = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_SECOND_DOB, StringUtils.EMPTY);
            final String mainSmoke = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_MAIN_SMOKE, StringUtils.EMPTY);
            final String secondSmoke = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_SECOND_SMOKE, StringUtils.EMPTY);
            final String relationship = MapUtils.getString(infoMap, FinancialservicesConstants.LIFE_RELATIONSHIP, StringUtils.EMPTY);
            
            
            final LifeDetailData lifeDetailData = new LifeDetailData();
            lifeDetailData.setLifeWhoCovered(who);
            lifeDetailData.setLifeCoverageAmount(coverageRequired);
            lifeDetailData.setLifeCoverageLast(coverageLast);
            lifeDetailData.setLifeMainSmoke(mainSmoke);
            lifeDetailData.setLifeSecondSmoke(secondSmoke);
            lifeDetailData.setLifeRelationship(relationship);
            
            final SimpleDateFormat sdf = new SimpleDateFormat(getDateFormatForDisplay());
            
            if (StringUtils.isNotEmpty(startDate))
            {
                lifeDetailData.setLifeCoverStartDate(sdf.format(DateTime.parse(startDate).toDate()));
            }
            
            if (StringUtils.isNotEmpty(mainDob))
            {
                lifeDetailData.setLifeMainDob(sdf.format(DateTime.parse(mainDob).toDate()));
            }
            
			if (StringUtils.isNotEmpty(secondDob))
			{
				lifeDetailData.setLifeSecondDob(sdf.format(DateTime.parse(secondDob).toDate()));
			}
            
            quoteData.setLifeDetail(lifeDetailData);
        }
	}

	protected boolean validate(final Map<String, Object> infoMap)
	{
		return infoMap.get(FinancialservicesConstants.LIFE_WHO_COVERED) != null
				&& infoMap.get(FinancialservicesConstants.LIFE_COVERAGE_REQUIRE) != null
				&& infoMap.get(FinancialservicesConstants.LIFE_COVERAGE_LAST) != null
				&& infoMap.get(FinancialservicesConstants.LIFE_COVERAGE_START_DATE) != null;
	}
}

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
 */
package de.hybris.platform.entitlementfacades.entitlement.populator;

import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.entitlementfacades.data.EntitlementData;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * UnitTest for Converter implementation for
 * {@link de.hybris.platform.entitlementfacades.entitlement.populator.TimeframeConditionEntitlementPopulator}.
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/entitlementfacades/test/entitlementfacades-timeframe-test-spring.xml")
public class TimeframeConditionEntitlementPopulatorTest
{

    private static final String GRANT_PARAMETER_START = "startTime";
    private static final String GRANT_PARAMETER_END = "endTime";
    private static final String GRANT_VALUE_TIMEFRAME = "2014-01-01T13:14:15Z";
    private static final String TIMEFRAME_TYPE = "timeframe";
    private static final String NOT_TIMEFRAME_TYPE = "not_timeframe";

	@Autowired
	private DateTimeConverter dateTimeConverter;

	@Autowired
    private TimeframeConditionEntitlementPopulator<ConditionData, EntitlementData> timeframePopulator;

    @Test
    public void testPopulateNotString()
    {
        final ConditionData source = new ConditionData();
        source.setType(NOT_TIMEFRAME_TYPE);

        final EntitlementData result = new EntitlementData();
        timeframePopulator.populate(source, result);

        Assert.assertNull(result.getConditionString());
    }


    @Test
    public void testPopulateNoProperty()
    {
        final ConditionData source = new ConditionData();
        source.setType(TIMEFRAME_TYPE);

        final EntitlementData result = new EntitlementData();
        timeframePopulator.populate(source, result);

        Assert.assertNull(result.getStartTime());
        Assert.assertNull(result.getEndTime());
    }

    @Test
    public void testPopulateCondition() throws ParseException
	{
        final ConditionData source = new ConditionData();
        source.setType(TIMEFRAME_TYPE);
        source.setProperty(GRANT_PARAMETER_START, GRANT_VALUE_TIMEFRAME);
        source.setProperty(GRANT_PARAMETER_END, GRANT_VALUE_TIMEFRAME);

        final EntitlementData result = new EntitlementData();
        timeframePopulator.populate(source, result);

		final Date grantValueTimeframe = dateTimeConverter.convertStringToDate(GRANT_VALUE_TIMEFRAME);
        Assert.assertEquals(grantValueTimeframe, result.getStartTime());
        Assert.assertEquals(grantValueTimeframe, result.getEndTime());
    }

    @Test
    public void testPopulateOpenTimeframeCondition() throws ParseException
	{
        final ConditionData source = new ConditionData();
        source.setType(TIMEFRAME_TYPE);
        source.setProperty(GRANT_PARAMETER_START, GRANT_VALUE_TIMEFRAME);

        final EntitlementData result = new EntitlementData();
        timeframePopulator.populate(source, result);

		final Date grantValueTimeframe = dateTimeConverter.convertStringToDate(GRANT_VALUE_TIMEFRAME);
        Assert.assertEquals(grantValueTimeframe, result.getStartTime());
        Assert.assertNull(result.getEndTime());
    }
}

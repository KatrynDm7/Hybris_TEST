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

package de.hybris.platform.emsclient.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.entitlementservices.data.EmsGrantData;
import de.hybris.platform.entitlementservices.enums.EntitlementTimeUnit;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;

import java.text.ParseException;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/emsclient/test/emsclient-timeframe-test-spring.xml")
public class EmsGrantDataTimeConditionPopulatorTest
{
	@Autowired
	private EmsGrantDataTimeConditionPopulator populator;

	@Autowired
	private DateTimeConverter dateTimeConverter;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldCountStartDateFromOne()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(1);
		emsData.setTimeUnitDuration(1);
		emsData.setTimeUnit(EntitlementTimeUnit.DAY);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);

		assertEquals(
				conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START),
				dateTimeConverter.convertDateToString(date));
	}

	@Test
	public void shouldUseDuration() throws ParseException
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(1);
		emsData.setTimeUnitDuration(1);
		emsData.setTimeUnit(EntitlementTimeUnit.DAY);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);

		assertEquals(
				conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START),
				dateTimeConverter.convertDateToString(date));
		assertTrue(date.before(dateTimeConverter.convertStringToDate(
				conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END))));
	}

	@Test
	public void startDurationTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		emsGrantData.setTimeUnitStart(1);
		emsGrantData.setTimeUnitDuration(2);
		emsGrantData.setTimeUnit(EntitlementTimeUnit.DAY);

		populator.populate(emsGrantData, conditionData);

		assertNotNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNotNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test
	public void startDurationNoTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		emsGrantData.setTimeUnitStart(1);
		emsGrantData.setTimeUnitDuration(2);

		populator.populate(emsGrantData, conditionData);

		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test
	public void startNoDurationTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		emsGrantData.setTimeUnitStart(1);
		emsGrantData.setTimeUnit(EntitlementTimeUnit.DAY);

		populator.populate(emsGrantData, conditionData);

		assertNotNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test
	public void startNoDurationNoTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		emsGrantData.setTimeUnitStart(1);

		populator.populate(emsGrantData, conditionData);

		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test
	public void noStartDurationTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		emsGrantData.setTimeUnitDuration(2);
		emsGrantData.setTimeUnit(EntitlementTimeUnit.DAY);

		thrown.expect(ConversionException.class);
		populator.populate(emsGrantData, conditionData);
	}

	@Test
	public void noStartDurationNoTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		emsGrantData.setTimeUnitDuration(2);

		populator.populate(emsGrantData, conditionData);

		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test
	public void noStartNoDurationTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		emsGrantData.setTimeUnit(EntitlementTimeUnit.DAY);

		populator.populate(emsGrantData, conditionData);

		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test
	public void noStartNoDurationNoTimeUnit()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsGrantData = new EmsGrantData();

		populator.populate(emsGrantData, conditionData);

		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test(expected = ConversionException.class)
	public void shouldRejectNullStartTime()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(null);
		emsData.setTimeUnitDuration(1);
		emsData.setTimeUnit(EntitlementTimeUnit.DAY);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);

		Assert.assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		Assert.assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test
	public void shouldIgnoreBoundsIfNoTimeUniteHasBeenSet()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(1);
		emsData.setTimeUnitDuration(2);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);

		Assert.assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		Assert.assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test(expected = ConversionException.class)
	public void shouldRejectInvalidStartTime()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(0);
		emsData.setTimeUnitDuration(1);
		emsData.setTimeUnit(EntitlementTimeUnit.DAY);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);
	}

	/**
	 * Null duration means open end date
	 */
	@Test
	public void shouldAcceptNullDuration()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(1);
		emsData.setTimeUnitDuration(null);
		emsData.setTimeUnit(EntitlementTimeUnit.DAY);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);

		assertNotNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	/**
	 * Duration of 0 means open end date
	 */
	@Test
	public void shouldAcceptZeroDuration()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(1);
		emsData.setTimeUnitDuration(0);
		emsData.setTimeUnit(EntitlementTimeUnit.DAY);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);

		assertNotNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_START));
		assertNull(conditionData.getProperty(EmsGrantDataTimeConditionPopulator.GRANT_PARAMETER_END));
	}

	@Test(expected = ConversionException.class)
	public void shouldRejectInvalidDuration()
	{
		final ConditionData conditionData = new ConditionData();
		final EmsGrantData emsData = new EmsGrantData();
		final Date date = new Date();
		emsData.setTimeUnitStart(1);
		emsData.setTimeUnitDuration(-1);
		emsData.setTimeUnit(EntitlementTimeUnit.DAY);
		emsData.setCreatedAt(date);

		populator.populate(emsData, conditionData);
	}
}

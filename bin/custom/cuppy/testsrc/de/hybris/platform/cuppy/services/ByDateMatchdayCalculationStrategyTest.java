/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.cuppy.services;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.services.impl.ByDateMatchdayCalculationStrategy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;


/**
 *
 */
public class ByDateMatchdayCalculationStrategyTest
{
	/** Instance of class to test. */
	private ByDateMatchdayCalculationStrategy strategy;

	/** Mocked dependency. */
	private MatchService matchService;

	/** Test data. */
	private CompetitionModel comp;
	private MatchModel match;
	private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);

	/**
	 * Sets up the mocked dependencies and prepares test data.
	 */
	@Before
	public void setUp() throws ParseException
	{
		strategy = new ByDateMatchdayCalculationStrategy();

		matchService = createMock(MatchService.class);
		strategy.setMatchService(matchService);

		comp = new CompetitionModel();
		comp.setCode("comp");

		match = new MatchModel();
		match.setDate(dateFormat.parse("05.08.2011 17:00:00"));
		match.setMatchday(5);
	}

	@Test
	public void testDayBefore() throws ParseException
	{
		assertEquals(1, calculateMatchDayByDate(null, "01.08.2011 17:00:00"));
	}

	@Test
	public void testSameDayButTimeBefore() throws ParseException
	{
		assertEquals(5, calculateMatchDayByDate(match, "05.08.2011 16:00:00"));
	}

	@Test
	public void testSameDaySameTime() throws ParseException
	{
		assertEquals(5, calculateMatchDayByDate(match, "05.08.2011 17:00:00"));
	}

	@Test
	public void testSameDayButTimeAfter() throws ParseException
	{
		assertEquals(5, calculateMatchDayByDate(match, "05.08.2011 18:00:00"));
	}

	@Test
	public void testDayAfter() throws ParseException
	{
		assertEquals(6, calculateMatchDayByDate(match, "06.08.2011 17:00:00"));
	}

	private int calculateMatchDayByDate(final MatchModel matchBefore, final String dateString)
	{
		try
		{
			final Date date = dateFormat.parse(dateString);
			expect(matchService.getMatchBefore(EasyMock.<CompetitionModel> anyObject(), EasyMock.<Date> anyObject())).andReturn(
					matchBefore);
			replay(matchService);

			final int matchday = strategy.getMatchday(comp, -1, date);

			verify(matchService);
			return matchday;
		}
		catch (final ParseException e)
		{
			fail(e.getMessage());
			return -1;
		}
	}
}

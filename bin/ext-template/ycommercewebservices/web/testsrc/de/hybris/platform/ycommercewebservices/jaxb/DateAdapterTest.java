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
package de.hybris.platform.ycommercewebservices.jaxb;

import de.hybris.bootstrap.annotations.UnitTest;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.springframework.util.Assert;


@UnitTest
public class DateAdapterTest
{
	private final DateAdapter dateAdapter = new DateAdapter();

	@Test
	public void nullUnmarshallTest() throws ParseException
	{
		Assert.isNull(dateAdapter.unmarshal(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongDateUnmarshallTest() throws ParseException
	{
		dateAdapter.unmarshal("blaBlaBla");
	}

	@Test
	public void correctDateUnmarshallTest() throws ParseException
	{
		final Date date = dateAdapter.unmarshal("2013-02-14T13:15:03-0800");
		Assert.notNull(date);
		final DateTime dateTime = new DateTime(date, DateTimeZone.UTC);
		Assert.isTrue(dateTime.getYear() == 2013);
		Assert.isTrue(dateTime.getMonthOfYear() == 2);
		Assert.isTrue(dateTime.getDayOfMonth() == 14);
		Assert.isTrue(dateTime.getHourOfDay() == 13 + 8);
		Assert.isTrue(dateTime.getMinuteOfHour() == 15);
		Assert.isTrue(dateTime.getSecondOfMinute() == 3);
	}

	@Test
	public void nullMarshallTest()
	{
		Assert.isNull(dateAdapter.marshal(null));
	}

	@Test
	public void dateMarshallTest()
	{
		Assert.isTrue(dateAdapter.marshal(new Date(0L)).startsWith("1970-01-01T01:00:00"));
	}
}

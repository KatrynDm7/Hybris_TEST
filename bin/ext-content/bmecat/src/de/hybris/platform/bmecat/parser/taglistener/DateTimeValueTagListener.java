/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


/**
 * Parses the &lt;DateTime&gt; tag
 * 
 */
public class DateTimeValueTagListener extends DefaultBMECatTagListener
{
	private final static String DATEPATTERN = "yyyy-MM-dd";
	private final static String TIMEPATTERN = "HH:mm:ss";
	private final static String TIMEZONEPATTERN = "zzz";

	private final SimpleDateFormat DATEFORMATER = new SimpleDateFormat(DATEPATTERN);
	private final SimpleDateFormat DATETIMEFORMATER = new SimpleDateFormat(DATEPATTERN + " " + TIMEPATTERN);
	private final SimpleDateFormat DATETIMEZONEFORMATER = new SimpleDateFormat(DATEPATTERN + " " + TIMEZONEPATTERN);
	private final SimpleDateFormat DATETIMETIMEZONEFORMATER = new SimpleDateFormat(DATEPATTERN + " " + TIMEPATTERN + " "
			+ TIMEZONEPATTERN);

	/**
	 * @param parent
	 */
	public DateTimeValueTagListener(final TagListener parent)
	{
		super(parent);
	}

	/**
	 * @see de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#createSubTagListeners()
	 */
	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.DATE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.TIME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.TIMEZONE) });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		String dateString = (String) getSubTagValue(BMECatConstants.XML.TAG.DATE);
		if (dateString != null)
		{
			dateString = dateString.trim();
			if ("".equals(dateString))
			{
				dateString = null;
			}
		}
		String timeString = (String) getSubTagValue(BMECatConstants.XML.TAG.TIME);
		if (timeString != null)
		{
			timeString = timeString.trim();
			if ("".equals(timeString))
			{
				timeString = null;
			}
		}
		String timeZoneString = (String) getSubTagValue(BMECatConstants.XML.TAG.TIMEZONE);
		if (timeZoneString != null)
		{
			timeZoneString = timeZoneString.trim();
			if ("".equals(timeZoneString))
			{
				timeZoneString = null;
			}
			else if (timeZoneString.charAt(0) == '+' || timeZoneString.charAt(0) == '-')
			{
				timeZoneString = "GMT" + timeZoneString;
			}
		}

		Date date = null;

		try
		{
			if (timeString != null && timeZoneString != null) //DATE + TIME + TIMEZONE
			{
				date = DATETIMETIMEZONEFORMATER.parse(dateString + " " + timeString + " " + timeZoneString);
			}
			else if (timeString != null) //DATE + TIME
			{
				date = DATETIMEFORMATER.parse(dateString + " " + timeString);
			}
			else if (timeZoneString != null) //DATE + TIMEZONE
			{
				date = DATETIMEZONEFORMATER.parse(dateString + " " + timeZoneString);
			}
			else
			//DATE
			{
				date = DATEFORMATER.parse(dateString);
			}
		}
		catch (final ParseException exp)
		{
			exp.printStackTrace();
		}

		return new DateTime(date, getAttribute(BMECatConstants.XML.ATTRIBUTE.DATETIME.TYPE));
	}

	public String getTagName()
	{
		return BMECatConstants.XML.TAG.DATETIME;
	}
}

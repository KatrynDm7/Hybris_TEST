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

import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;



/**
 * DateAdaper is used by JAXB to convert Dates to String and vice versa.
 */
public class DateAdapter extends XmlAdapter<String, Date>
{
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern(DATE_FORMAT);

	@Override
	public String marshal(final Date d)
	{
		if (d == null)
		{
			return null;
		}
		return dateFormat.print(d.getTime());
	}

	@Override
	public Date unmarshal(final String d) throws ParseException
	{
		if (d == null)
		{
			return null;
		}
		return dateFormat.parseDateTime(d).toDate();
	}
}

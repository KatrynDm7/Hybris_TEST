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
package de.hybris.platform.bmecat.parser;

import java.util.Date;


/**
 * Object which holds the value of a parsed &lt;DATETIME&gt; tag
 * 
 * 
 */
public class DateTime extends Date
{
	public static final String TYPE_GENERATION_DATE = "generation_date";
	public static final String TYPE_AGREEMENT_START_DATE = "agreement_start_date";
	public static final String TYPE_AGREEMENT_END_DATE = "agreement_end_date";
	public static final String TYPE_START_DATE = "valid_start_date";
	public static final String TYPE_END_DATE = "valid_end_date";

	private final String type;

	public DateTime(final Date source, final String type)
	{
		super(source.getTime());
		this.type = type;
	}

	public String getType()
	{
		return this.type;
	}
}

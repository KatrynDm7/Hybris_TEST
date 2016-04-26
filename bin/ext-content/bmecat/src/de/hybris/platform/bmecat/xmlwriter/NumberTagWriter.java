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
package de.hybris.platform.bmecat.xmlwriter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Number&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class NumberTagWriter extends SimpleTagWriter
{
	private final DecimalFormat decimalFormat = new DecimalFormat();

	/**
	 * @param parent
	 * @param tagName
	 */
	public NumberTagWriter(final XMLTagWriter parent, final String tagName)
	{
		this(parent, tagName, false);
	}

	public NumberTagWriter(final XMLTagWriter parent, final String tagName, final boolean mandatory)
	{
		super(parent, tagName, mandatory);

		final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
		symbols.setDecimalSeparator('.');
		this.decimalFormat.setDecimalFormatSymbols(symbols);
		this.decimalFormat.applyPattern("#0.00#");
		this.decimalFormat.setDecimalSeparatorAlwaysShown(false);
	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final Object content;
		if (object instanceof Double)
		{
			content = decimalFormat.format(((Double) object).doubleValue());
		}
		else
		{
			content = object;
		}
		super.writeContent(xmlOut, content);
	}
}

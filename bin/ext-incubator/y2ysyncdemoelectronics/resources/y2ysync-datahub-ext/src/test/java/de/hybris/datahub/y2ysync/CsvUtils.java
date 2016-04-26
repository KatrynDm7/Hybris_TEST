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

package de.hybris.datahub.y2ysync;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Utilities for testing CSV manipulations.
 */
public class CsvUtils
{
	/**
	 * Creates a parsed comma-separated row of values
	 *
	 * @param rowValues column values
	 * @return a list attributes representing a CSV row
	 */
	public static List<String> toRow(final String... rowValues)
	{
		return Arrays.asList(rowValues);
	}

	public static List<List<String>> toDataRow(final String... values)
	{
		return toCsv(toRow(values));
	}


	public static List<String> toHeaders(final String... attributes)
	{
		return toRow(attributes);
	}

	/**
	 * Creates a CSV row from the values.
	 *
	 * @param values values in the order they should appear on the CSV row.
	 * @return the concatenated row of values.
	 */
	public static String toCsvRow(final String... values)
	{
		Preconditions.checkArgument(values != null, "At least one value is required");
		return StringUtils.join(values, ',');
	}

	/**
	 * Creates a list of CSV rows.
	 *
	 * @param rows lists of values representing a single CSV row.
	 * @return a list of row lists.
	 */
	@SafeVarargs
	public static List<List<String>> toCsv(final List<String>... rows)
	{
		return Arrays.asList(rows);
	}

	/**
	 * Creates a text from CSV lines.
	 *
	 * @param rows lines of comma-separated values
	 * @return lines concatenated into a text
	 */
	public static String toCsv(final String... rows)
	{
		Preconditions.checkArgument(rows != null, "At least the header row is required");
		return StringUtils.join(rows, "\n");
	}
}

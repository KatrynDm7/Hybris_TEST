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

package de.hybris.datahub.y2ysync.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to encapsulate the sections of a csv file.
 */
public class CsvData
{
	private final List<String> csvHeaders;
	private final List<List<String>> csvBody;

	public CsvData(final List<String> headers, final List<List<String>> body)
	{
		csvHeaders = (headers != null ? headers : new ArrayList<>());
		csvBody = (body != null ? body : new ArrayList<>());
	}

	public List<String> getCsvHeaders()
	{
		return csvHeaders;
	}

	public List<List<String>> getCsvBody()
	{
		return csvBody;
	}
}

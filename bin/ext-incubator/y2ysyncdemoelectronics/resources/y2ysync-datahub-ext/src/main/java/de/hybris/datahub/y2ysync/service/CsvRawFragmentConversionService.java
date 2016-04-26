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

package de.hybris.datahub.y2ysync.service;

import de.hybris.datahub.y2ysync.domain.CsvData;

import java.util.List;
import java.util.Map;

public interface CsvRawFragmentConversionService
{
	/**
	 * Converts csv data into a List of raw fragments
	 *
	 * @param csvData contains the headers and body of the csv data
	 * @param rawItemType The type of raw fragment data contained in the csv input used to validateCsv the csv headers
	 * @return A list of raw fragments represented as Maps
	 * @throws IllegalArgumentException if there is not at least one valid RawItem attribute specified in the header
	 */
	public List<Map<String, String>> createRawFragments(final CsvData csvData, final String rawItemType);
}

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

public interface CsvReaderService
{
	/**
	 * Splits apart the csv input into header and body sections
	 *
	 * @param csvInput The csv input containing the raw fragment data
	 * @return A CsvData instance with the body and headers sections of the csv data split apart
	 * @throws IllegalArgumentException if csv input does not contain any data
	 */
	public CsvData extractBodyAndHeaders(final String csvInput);
}

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

package de.hybris.datahub.y2ysync.service.impl;

import de.hybris.datahub.y2ysync.domain.CsvData;
import de.hybris.datahub.y2ysync.service.CsvReaderService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.csvreader.CsvReader;


public class DefaultCsvReaderService implements CsvReaderService
{
	private static final Logger logger = LoggerFactory.getLogger(DefaultCsvReaderService.class);

	public CsvData extractBodyAndHeaders(final String csvInput)
	{
		final CsvReader csvReader = new CsvReader(new ByteArrayInputStream(csvInput.getBytes(StandardCharsets.UTF_8)), ';',
				StandardCharsets.UTF_8);
		csvReader.setComment('#');
		csvReader.setUseComments(true);

		final List<String> headers = new ArrayList<>();
		final List<List<String>> rows = new ArrayList<>();
		try
		{
			csvReader.readHeaders();
			headers.addAll(Arrays.asList(csvReader.getHeaders()));
			if (CollectionUtils.isEmpty(headers))
			{
				throw new IllegalArgumentException("Csv input does not contain any data");
			}
			while (csvReader.readRecord())
			{
				final String[] rowValues = csvReader.getValues();
				final List<String> row = Arrays.asList(rowValues);
				rows.add(row);
			}
		}
		catch (final IOException e)
		{
			logger.error("Could not read csv input", e);
			throw new IllegalStateException("Could not read csv input", e);
		}
		finally
		{
			csvReader.close();
		}
		return new CsvData(headers, rows);
	}
}

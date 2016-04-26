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
import de.hybris.datahub.y2ysync.service.CsvRawFragmentConversionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Function;


public class DefaultCsvRawFragmentConversionService implements CsvRawFragmentConversionService
{
	@Override
	public List<Map<String, String>> createRawFragments(final CsvData bodyAndHeaders, final String rawItemType)
	{
		return new RawAttributesMapTransformer(bodyAndHeaders.getCsvHeaders()).apply(bodyAndHeaders.getCsvBody());
	}

	private class RawAttributesMapTransformer implements Function<List<List<String>>, List<Map<String, String>>>
	{
		private final List<String> keys;
		private final CsvValueTransformer valueTransformer;

		public RawAttributesMapTransformer(final List<String> headers)
		{
			assert headers != null && !headers.isEmpty() : "Expect headers be validated before getting here";
			keys = headers;
			valueTransformer = new CsvValueTransformer();
		}

		@Nullable
		@Override
		public List<Map<String, String>> apply(final List<List<String>> dataRows)
		{
			assert dataRows != null : "Data should have been validated by now";
			final List<Map<String, String>> rawFragments = new ArrayList<>(dataRows.size());
			for (final List<String> row : dataRows)
			{
				final Map<String, String> rawFragment = toAttributesMap(row);
				rawFragments.add(rawFragment);
			}
			return rawFragments;
		}

		private Map<String, String> toAttributesMap(final List<String> row)
		{
			final int attribCnt = Math.min(keys.size(), row.size());
			final Map<String, String> rawFragment = new HashMap<>();
			for (int i = 0; i < attribCnt; ++i)
			{
				final String rawValue = valueTransformer.csvToRawFragmentValue(row.get(i));
				if (rawValue != null)
				{
					rawFragment.put(keys.get(i), rawValue);
				}
			}
			return rawFragment;
		}
	}

	private class CsvValueTransformer
	{
		public String csvToRawFragmentValue(final String csvValue)
		{
			if ("<ignore>".equals(csvValue))
			{
				return null;
			}
			return csvValue;
		}
	}
}

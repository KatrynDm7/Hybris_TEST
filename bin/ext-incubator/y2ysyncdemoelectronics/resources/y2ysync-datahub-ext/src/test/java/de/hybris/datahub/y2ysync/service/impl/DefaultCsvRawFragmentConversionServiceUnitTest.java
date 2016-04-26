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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import de.hybris.datahub.y2ysync.CsvUtils;
import de.hybris.datahub.y2ysync.domain.CsvData;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.datahub.domain.test.TestRawCategory;
import com.hybris.datahub.service.RawItemService;
import com.hybris.datahub.util.RawAttributeModelDefinitionBuilder;
import com.hybris.datahub.util.RawItemMetadataBuilder;


@RunWith(MockitoJUnitRunner.class)
public class DefaultCsvRawFragmentConversionServiceUnitTest
{
	private static final String VALID_RAW_TYPE = TestRawCategory.class.getSimpleName();
	private static final String VALID_ATTRIBUTE = "name";
	private static final String INVALID_ATTRIBUTE = "bogus";
	private static final String DEFAULT_EMPTY_VALUE = "<empty>";
	private static final String EMPTY_STRING = "";

	@InjectMocks
	private final DefaultCsvRawFragmentConversionService csvService = new DefaultCsvRawFragmentConversionService();
	@Mock
	private RawItemService rawItemService;

	@Before
	public void setUp()
	{
		setUp(rawItemService);
		RawItemMetadataBuilder.forType(VALID_RAW_TYPE)
				.addAttribute(RawAttributeModelDefinitionBuilder.forAttributeName(VALID_ATTRIBUTE).build()).build();
	}

	private void setUp(final RawItemService service)
	{
		doReturn(Boolean.TRUE).when(service).isRawItemType(VALID_RAW_TYPE);
	}


	@Test
	public void testConversionWhenAtLeastOneValidAttributeIsInHeaders()
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(INVALID_ATTRIBUTE, VALID_ATTRIBUTE), CsvUtils.toDataRow("Something",
				"Gifts"));
		final List<Map<String, String>> fragments = csvService.createRawFragments(data, VALID_RAW_TYPE);

		assertEquals("number of converted items", 1, fragments.size());
		final Map<String, String> fragment = fragments.get(0);
		assertEquals("Invalid attribute value not set", "Something", fragment.get(INVALID_ATTRIBUTE));
		assertEquals("Valid attribute value not set", "Gifts", fragment.get(VALID_ATTRIBUTE));
	}

	@Test
	public void testConversionWithMultipleCsvDataRows()
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE), CsvUtils.toCsv(CsvUtils.toRow("Gifts"),
				CsvUtils.toRow("Toys"), CsvUtils.toRow("Serious Stuff")));
		assert data.getCsvBody().size() > 1 : "Multiple data rows expected";
		final List<Map<String, String>> fragments = csvService.createRawFragments(data, VALID_RAW_TYPE);

		assertEquals(data.getCsvBody().size(), fragments.size());
	}

	@Test
	public void testConversionWhenRowHasMoreValuesThanHeaders()
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE), CsvUtils.toDataRow("Gifts", "Presents"));
		final List<Map<String, String>> fragments = csvService.createRawFragments(data, VALID_RAW_TYPE);

		assertEquals("number of converted items", 1, fragments.size());
		final Map<String, String> fragment = fragments.get(0);
		assertEquals("number of converted attributes", 1, fragment.size());
		assertEquals("attribute value", "Gifts", fragment.get(VALID_ATTRIBUTE));
	}

	@Test
	public void testConversionWhenHeaderHasMoreAttributesThanRowValues()
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE, "other"), CsvUtils.toDataRow("Gifts"));
		final List<Map<String, String>> fragments = csvService.createRawFragments(data, VALID_RAW_TYPE);

		assertEquals("number of converted items", 1, fragments.size());
		final Map<String, String> fragment = fragments.get(0);
		assertEquals("present attribute value", "Gifts", fragment.get(VALID_ATTRIBUTE));
		assertNull("absent attribute value", fragment.get("other"));
	}

	@Test
	public void testConversionOfSpaceValue()
	{
		assertEquals(" ", convertCsvValue(" "));
	}

	@Test
	public void testConversionOfNullValues()
	{
		assertNull(convertCsvValue(null));
	}

	@Test
	public void testConversionOfEmptyStringValue()
	{
		final String value = convertCsvValue(EMPTY_STRING);

		assertThat(value).isEqualTo(EMPTY_STRING);
	}

	@Test
	public void testConversionOfExplicitEmptyValue()
	{
		final String value = convertCsvValue(DEFAULT_EMPTY_VALUE);

		assertThat(value).isEqualTo(DEFAULT_EMPTY_VALUE);
	}


	private String convertCsvValue(final String value)
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE), CsvUtils.toDataRow(value));
		final List<Map<String, String>> fragments = csvService.createRawFragments(data, VALID_RAW_TYPE);

		assertEquals("number of converted items", 1, fragments.size());
		final Map<String, String> fragment = fragments.get(0);
		return fragment.get(VALID_ATTRIBUTE);
	}


}

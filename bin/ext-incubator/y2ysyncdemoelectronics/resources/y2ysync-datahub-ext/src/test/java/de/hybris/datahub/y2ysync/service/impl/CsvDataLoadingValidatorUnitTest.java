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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.hybris.datahub.api.JavaBeanGenerator;

import de.hybris.datahub.y2ysync.CsvUtils;
import de.hybris.datahub.y2ysync.domain.CsvData;

import com.hybris.datahub.domain.test.TestCanonicalCategory;
import com.hybris.datahub.domain.test.TestRawCategory;
import com.hybris.datahub.runtime.domain.DataHubFeed;
import com.hybris.datahub.service.DataHubFeedService;
import com.hybris.datahub.service.RawItemService;
import com.hybris.datahub.util.RawAttributeModelDefinitionBuilder;
import com.hybris.datahub.util.RawItemMetadataBuilder;
import com.hybris.datahub.validation.ValidationException;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class CsvDataLoadingValidatorUnitTest extends TestCase
{

	public static final String INVALID_RAW_TYPE = TestCanonicalCategory.class.getSimpleName();
	public static final String VALID_ATTRIBUTE = "name";
	public static final String INVALID_ATTRIBUTE = "bogus";
	public static final String INVALID_FEED_NAME = "dummy feed";
	private static final String VALID_RAW_TYPE = TestRawCategory.class.getSimpleName();
	private static final String VALID_FEED_NAME = "validFeed";
	@InjectMocks
	private CsvDataLoadingValidator validator = new CsvDataLoadingValidator();
	@Mock
	private DataHubFeedService dataHubFeedService;
	@Mock
	private RawItemService rawItemService;

	@Before
	public void setUp()
	{
		RawItemMetadataBuilder.forType(VALID_RAW_TYPE).addAttribute(
				RawAttributeModelDefinitionBuilder.forAttributeName(VALID_ATTRIBUTE).build()).build();

		when(dataHubFeedService.findDataFeedByName(VALID_FEED_NAME)).
				thenReturn(JavaBeanGenerator.create(DataHubFeed.class));
		doReturn(true).when(rawItemService).isRawItemType(VALID_RAW_TYPE);
	}

	@Test
	public void testValidate() throws Exception
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE), CsvUtils.toDataRow("Gifts"));
		validator.validateCsv(data, VALID_RAW_TYPE);
	}

	@Test
	public void testValidateForFailure() throws Exception
	{
		validator.validateForFailure(VALID_FEED_NAME, VALID_RAW_TYPE);
	}

	@Test
	public void testValidateForFailureWithInvalidFeed()
	{
		try
		{
			validator.validateForFailure(INVALID_FEED_NAME, VALID_RAW_TYPE);
		}
		catch (ValidationException e)
		{
			assertEquals(CsvDataLoadingValidator.FEED, e.getFailures().get(0).getPropertyName());
		}
	}

	@Test
	public void testValidateForFailureWithInvalidRawType()
	{
		try
		{
			validator.validateForFailure(VALID_FEED_NAME, INVALID_RAW_TYPE);
		}
		catch (ValidationException e)
		{
			assertEquals(CsvDataLoadingValidator.ITEM_TYPE, e.getFailures().get(0).getPropertyName());
		}
	}

	@Test
	public void testValidateForFailureWithAllInvalid()
	{
		try
		{
			validator.validateForFailure(INVALID_FEED_NAME, INVALID_RAW_TYPE);
		}
		catch (ValidationException e)
		{
			assertEquals(2, e.getFailures().size());
		}
	}

	@Test(expected = ValidationException.class)
	public void testReportsEmptyCsvData() throws ValidationException
	{
		doReturn(false).when(rawItemService).isRawItemType(VALID_RAW_TYPE);
		final List<String> no_headers = Collections.emptyList();
		final List<List<String>> no_data = Collections.emptyList();
		final CsvData emptyCsv = new CsvData(no_headers, no_data);
		validator.validateCsv(emptyCsv, VALID_RAW_TYPE);
	}

	@Test(expected = ValidationException.class)
	public void testReportsInvalidRawType() throws ValidationException
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE), CsvUtils.toDataRow("Gifts"));
		validator.validateCsv(data, INVALID_RAW_TYPE);
	}

	@Test(expected = ValidationException.class)
	public void testReportsNullCsvHeader() throws ValidationException
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE, null), CsvUtils.toDataRow("Something"));
		validator.validateCsv(data, VALID_RAW_TYPE);
	}

	@Test(expected = ValidationException.class)
	public void testReportsEmptyCsvHeader() throws ValidationException
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(VALID_ATTRIBUTE, ""), CsvUtils.toDataRow("Something"));
		validator.validateCsv(data, VALID_RAW_TYPE);
	}

	@Test(expected = ValidationException.class)
	public void testReportsAbsenceOfAtLeastOneValidAttributeInHeaders() throws ValidationException
	{
		final CsvData data = new CsvData(CsvUtils.toHeaders(INVALID_ATTRIBUTE), CsvUtils.toDataRow("Something"));
		validator.validateCsv(data, VALID_RAW_TYPE);
	}
}
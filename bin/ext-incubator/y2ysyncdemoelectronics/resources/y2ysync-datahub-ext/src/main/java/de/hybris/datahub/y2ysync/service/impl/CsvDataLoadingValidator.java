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

import static com.hybris.datahub.validation.ValidationFailureType.EXCEPTION;
import static com.hybris.datahub.validation.ValidationFailureType.FATAL;

import de.hybris.datahub.y2ysync.domain.CsvData;

import com.hybris.datahub.model.RawItem;
import com.hybris.datahub.runtime.domain.DataHubFeed;
import com.hybris.datahub.service.DataHubFeedService;
import com.hybris.datahub.service.RawItemService;
import com.hybris.datahub.validation.ValidationContext;
import com.hybris.datahub.validation.ValidationException;
import com.hybris.datahub.validation.ValidationFailure;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public class CsvDataLoadingValidator
{
	static final String ITEM_TYPE = "itemType";
	static final String FEED = "feed";
	static final String CSV_HEADER = "csvHeader";
	private RawItemService rawItemService;
	private DataHubFeedService dataHubFeedService;

	/**
	 * @param bodyAndHeaders
	 * @param itemType
	 * @throws com.hybris.datahub.validation.ValidationException
	 */
	public void validateCsv(final CsvData bodyAndHeaders, final String itemType)
			throws ValidationException
	{
		final ValidationContext validationContext = new ValidationContext();
		validateHeaderAttributes(validationContext, bodyAndHeaders.getCsvHeaders(), itemType);
		if (validationContext.getFailures().size() > 0)
		{
			throw new ValidationException(validationContext.getFailures());
		}
	}

	/**
	 * @param feedName
	 * @param itemType
	 * @throws ValidationException
	 */
	@Transactional(readOnly = true)
	public void validateForFailure(final String feedName, final String itemType)
			throws ValidationException
	{
		final ValidationContext validationContext = new ValidationContext();
		validateDataHubFeed(validationContext, feedName);
		validateRawItemType(validationContext, itemType);
		if (validationContext.getFailures().size() > 0)
		{
			throw new ValidationException(validationContext.getFailures());
		}
	}

	private void validateHeaderAttributes(final ValidationContext validationContext, final List<String> headers, final String type)
	{
		boolean validAttributeFound = false;
		for (final String attributeName : headers)
		{
			try
			{
				validAttributeFound = validAttributeFound | validateAttribute(type, attributeName);
			}
			catch (final IllegalArgumentException e)
			{
				validationContext.addFailure(new ValidationFailure(CSV_HEADER, e.getMessage(), EXCEPTION));
				validAttributeFound = true;
			}
		}
		if (!validAttributeFound)
		{
			validationContext.addFailure(new ValidationFailure(CSV_HEADER, "At least one valid attribute needs to exist in csv header for type \"" + type + "\"", EXCEPTION));
		}
	}

	private boolean validateAttribute(final String type, final String attributeName)
	{
		if (StringUtils.isEmpty(attributeName))
		{
			throw new IllegalArgumentException("CSV Header String cannot be null or empty");
		}
		return RawItem.getAttributeDefinition(type, attributeName).isPresent();
	}

	private void validateRawItemType(final ValidationContext validationContext, final String rawItemType)
	{
		if (!rawItemService.isRawItemType(rawItemType))
		{
			validationContext.addFailure(new ValidationFailure(ITEM_TYPE, rawItemType + " type is not a subtype of RawItem", FATAL));
		}
	}

	private void validateDataHubFeed(final ValidationContext validationContext, final String feedName)
	{

		final DataHubFeed feed = dataHubFeedService.findDataFeedByName(feedName);
		if (feed == null)
		{
			validationContext.addFailure(new ValidationFailure(FEED, "The feed '" + feedName + "' was not found", FATAL));
		}
	}

	@Required
	public void setRawItemService(final RawItemService rawItemService)
	{
		this.rawItemService = rawItemService;
	}

	@Required
	public void setDataHubFeedService(final DataHubFeedService dataHubFeedService)
	{
		this.dataHubFeedService = dataHubFeedService;
	}
}
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

package de.hybris.datahub.y2ysync.facade.impl;

import de.hybris.datahub.y2ysync.domain.CsvData;
import de.hybris.datahub.y2ysync.facade.CsvImportFacade;
import de.hybris.datahub.y2ysync.service.CsvRawFragmentConversionService;
import de.hybris.datahub.y2ysync.service.CsvReaderService;
import de.hybris.datahub.y2ysync.service.impl.CsvDataLoadingValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.hybris.datahub.dto.item.ResultData;
import com.hybris.datahub.runtime.domain.DataHubFeed;
import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.service.CanonicalItemService;
import com.hybris.datahub.service.DataHubFeedService;
import com.hybris.datahub.service.DataLoadingService;
import com.hybris.datahub.validation.ValidationException;
import com.hybris.datahub.validation.ValidationFailure;


public class DefaultCsvImportFacade implements CsvImportFacade
{

	private static final String FEED_NAME = "Y2YSYNC_FEED";
	private static final String POOL_NAME = "Y2YSYNC_POOL";

	private static final Logger LOG = LoggerFactory.getLogger(DefaultCsvImportFacade.class);
	private CsvDataLoadingValidator dataLoadingValidator;
	private DataHubFeedService dataHubFeedService;
	private CsvReaderService csvReaderService;
	private CsvRawFragmentConversionService csvRawFragmentConversionService;
	private DataLoadingService dataLoadingService;
	private MessageChannel rawFragmentInputChannel;
	private PlatformTransactionManager transactionManager;
	private CanonicalItemService canonicalItemService;

	@Override
	public ResultData importCsv(final String itemType, final boolean delete, final String csvInput) throws ValidationException
	{
		final int numberOfProcessedFragments;

		if (delete)
		{
			numberOfProcessedFragments = deleteCanonicalItems(itemType, csvInput);
		}
		else
		{
			numberOfProcessedFragments = importRawItems(itemType, csvInput);
		}

		final ResultData result = new ResultData();
		result.setNumberProcessed(numberOfProcessedFragments);
		return result;
	}

	private int importRawItems(final String itemType, final String csvInput) throws ValidationException
	{
		final String rawItemType = itemType + "Raw";

		final int numberOfProcessedFragments;
		final List<Map<String, String>> rawFragments = validateAndCreateRawFragments(FEED_NAME, rawItemType, csvInput);

		LOG.info("Importing " + rawFragments.size() + " raw fragments of type " + itemType + " into data feed " + FEED_NAME);
		rawFragmentInputChannel.send(new GenericMessage<Object>(rawFragments, constructMessageHeader(rawItemType, FEED_NAME)));

		numberOfProcessedFragments = rawFragments.size();
		return numberOfProcessedFragments;
	}

	private int deleteCanonicalItems(final String itemType, final String csvInput)
	{
		final String canonicalItemType = itemType + "Canonical";

		final int numberOfProcessedFragments;
		final CsvData bodyAndHeaders = csvReaderService.extractBodyAndHeaders(csvInput);
		final List<Map<String, String>> rawFragments = csvRawFragmentConversionService.createRawFragments(bodyAndHeaders, itemType);

		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		final DataHubPool targetPool = template.execute(status -> dataHubFeedService.findPoolByName(POOL_NAME));

		for (final Map<String, String> rawFragment : rawFragments)
		{
			deleteInTx(canonicalItemType, template, targetPool, rawFragment);
		}

		numberOfProcessedFragments = rawFragments.size();
		return numberOfProcessedFragments;
	}

	private void deleteInTx(final String canonicalItemType, final TransactionTemplate template, final DataHubPool targetPool,
			final Map<String, String> rawFragment)
	{
		template.execute(status -> deleteByIntegrationKey(canonicalItemType, targetPool, rawFragment));
	}

	private Integer deleteByIntegrationKey(final String canonicalItemType, final DataHubPool targetPool,
			final Map<String, String> rawFragment)
	{
		return Integer.valueOf(canonicalItemService.deleteByIntegrationKey(canonicalItemType, rawFragment.get("integrationKey"), targetPool));
	}

	private List<Map<String, String>> validateAndCreateRawFragments(final String feedName, final String itemType,
			final String csvInput) throws ValidationException
	{
		final CsvData bodyAndHeaders = csvReaderService.extractBodyAndHeaders(csvInput);

		try
		{
			dataLoadingValidator.validateCsv(bodyAndHeaders, itemType);
		}
		catch (final ValidationException e)
		{
			createFailedDataLoadingAction(e, feedName, bodyAndHeaders.getCsvBody().size());
			throw e;
		}
		return csvRawFragmentConversionService.createRawFragments(bodyAndHeaders, itemType);
	}


	private void createFailedDataLoadingAction(final ValidationException e, final String feedName, final long rawFragmentCount)
	{

		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final DataHubFeed feed = dataHubFeedService.findDataFeedByName(feedName);
				final String message = buildErrorMessage(e);
				dataLoadingService.createFailedAction(feed, Long.valueOf(rawFragmentCount), message);
			}
		});
	}

	private String buildErrorMessage(final ValidationException e)
	{
		final StrBuilder builder = new StrBuilder();
		for (final ValidationFailure failure : e.getFailures())
		{
			builder.appendSeparator("; ");
			builder.append(failure.getMessage());
		}
		return builder.toString();
	}

	private Map<String, Object> constructMessageHeader(final String itemType, final String feedName)
	{
		final Map<String, Object> header = new HashMap<>();
		header.put("itemType", itemType);
		header.put("feedName", feedName);
		return header;
	}

	@Required
	public void setCsvReaderService(final CsvReaderService csvReaderService)
	{
		this.csvReaderService = csvReaderService;
	}

	@Required
	public void setCsvRawFragmentConversionService(final CsvRawFragmentConversionService csvRawFragmentConversionService)
	{
		this.csvRawFragmentConversionService = csvRawFragmentConversionService;
	}


	@Required
	public void setRawFragmentInputChannel(final MessageChannel rawFragmentInputChannel)
	{
		this.rawFragmentInputChannel = rawFragmentInputChannel;
	}

	@Required
	public void setTransactionManager(final PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	@Required
	public void setDataLoadingValidator(final CsvDataLoadingValidator dataLoadingValidator)
	{
		this.dataLoadingValidator = dataLoadingValidator;
	}

	@Required
	public void setDataLoadingService(final DataLoadingService dataLoadingService)
	{
		this.dataLoadingService = dataLoadingService;
	}

	@Required
	public void setDataHubFeedService(final DataHubFeedService dataHubFeedService)
	{
		this.dataHubFeedService = dataHubFeedService;
	}

	@Required
	public void setCanonicalItemService(final CanonicalItemService canonicalItemService)
	{
		this.canonicalItemService = canonicalItemService;
	}
}

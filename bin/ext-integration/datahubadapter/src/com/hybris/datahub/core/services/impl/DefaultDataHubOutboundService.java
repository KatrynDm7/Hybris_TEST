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

package com.hybris.datahub.core.services.impl;

import com.hybris.datahub.core.dto.ResultData;
import com.hybris.datahub.core.rest.DataHubCommunicationException;
import com.hybris.datahub.core.rest.DataHubOutboundException;
import com.hybris.datahub.core.rest.client.DataHubOutboundClient;
import com.hybris.datahub.core.services.DataHubOutboundService;
import com.hybris.datahub.core.util.OutboundServiceCsvUtils;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * Implementation of the outbound service based on a REST client.
 */
public class DefaultDataHubOutboundService implements DataHubOutboundService
{
	private static final String DEFAULT_FEED = "DEFAULT_FEED";
	private static final String DEFAULT_POOL = "GLOBAL";
	private DataHubOutboundClient dataHubOutboundClient;
	private OutboundServiceCsvUtils csvUtils;

	@Override
	public ResultData sendToDataHub(final String feedName, final String rawType, final Object obj)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		Preconditions.checkArgument(StringUtils.isNotEmpty(feedName), "feedName cannot be empty");
		Preconditions.checkArgument(StringUtils.isNotEmpty(rawType), "rawType cannot be empty");
		Preconditions.checkArgument(obj != null, "obj cannot be null");

		final String[] csvArray = csvUtils.convertObjectToCsv(obj);
		return dataHubOutboundClient.exportData(csvArray, feedName, rawType);
	}

	@Override
	public ResultData sendToDataHub(final String rawType, final Object obj) throws DataHubOutboundException,
			DataHubCommunicationException
	{
		return sendToDataHub(DEFAULT_FEED, rawType, obj);
	}

	@Override
	public ResultData sendToDataHub(final String feedName, final String rawType, final Map<String, Object> objectMap)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		Preconditions.checkArgument(StringUtils.isNotEmpty(feedName), "feedName cannot be empty");
		Preconditions.checkArgument(StringUtils.isNotEmpty(rawType), "rawType cannot be empty");
		Preconditions.checkArgument(objectMap != null, "obj cannot be null");

		final String[] csvArray = csvUtils.convertMapToCsv(objectMap);
		return dataHubOutboundClient.exportData(csvArray, feedName, rawType);
	}

	@Override
	public ResultData sendToDataHub(final String rawType, final Map<String, Object> objectMap) throws DataHubOutboundException,
			DataHubCommunicationException
	{
		return sendToDataHub(DEFAULT_FEED, rawType, objectMap);
	}

	@Override
	public ResultData sendToDataHub(final String feedName, final String rawType, final List<Map<String, Object>> objList)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		Preconditions.checkArgument(StringUtils.isNotEmpty(feedName), "feedName cannot be empty");
		Preconditions.checkArgument(StringUtils.isNotEmpty(rawType), "rawType cannot be empty");
		Preconditions.checkArgument(objList != null, "obj cannot be null");

		final String[] csvArray = csvUtils.convertListToCsv(objList);
		return dataHubOutboundClient.exportData(csvArray, feedName, rawType);
	}

	@Override
	public ResultData sendToDataHub(final String rawType, final List<Map<String, Object>> objList)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		return sendToDataHub(DEFAULT_FEED, rawType, objList);
	}

	@Override
	public ResultData deleteItem(final String poolName, final String canonicalItemType, final Map<String, String> keyFields)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(poolName), "feed name cannot be null or blank");
		Preconditions.checkArgument(StringUtils.isNotBlank(canonicalItemType), "canonical type code cannot be null or blank");
		Preconditions.checkArgument(MapUtils.isNotEmpty(keyFields), "One or more primary keys must be provided");

		return dataHubOutboundClient.deleteItem(poolName, canonicalItemType, keyFields);
	}

	@Override
	public ResultData deleteItem(final String canonicalItemType, final Map<String, String> keyFields)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		return deleteItem(DEFAULT_POOL, canonicalItemType, keyFields);
	}

	@Override
	public ResultData deleteByFeed(final String feedName, final String rawItemType) throws DataHubCommunicationException,
			DataHubOutboundException
	{
		Preconditions.checkArgument(feedName != null, "Data feed name cannot be null");
		Preconditions.checkArgument(rawItemType != null, "Raw item type cannot be null");

		return dataHubOutboundClient.deleteByFeed(feedName, rawItemType);
	}

	@Override
	public ResultData deleteByFeed(final String rawItemType) throws DataHubOutboundException, DataHubCommunicationException
	{
		return deleteByFeed(DEFAULT_FEED, rawItemType);
	}

	@Override
	public ResultData deleteByFeed(final String feedName, final String rawItemType, final Map<String, Object> keyFields)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		Preconditions.checkArgument(feedName != null, "Data feed name cannot be null");
		Preconditions.checkArgument(rawItemType != null, "Raw item type cannot be null");
		Preconditions.checkArgument(MapUtils.isNotEmpty(keyFields), "One or more primary keys must be provided");

		return dataHubOutboundClient.deleteByFeed(feedName, rawItemType, csvUtils.transmissionSafe(keyFields));
	}

	@Override
	public ResultData deleteByFeed(final String rawItemType, final Map<String, Object> keyFields) throws DataHubOutboundException,
			DataHubCommunicationException
	{
		return deleteByFeed(DEFAULT_FEED, rawItemType, keyFields);
	}

	/**
	 * Injects Data Hub client to be used for communication with the Data Hub.
	 *
	 * @param dataHubOutboundClient an implementation of the client to use.
	 */
	@Required
	public void setDataHubOutboundClient(final DataHubOutboundClient dataHubOutboundClient)
	{
		this.dataHubOutboundClient = dataHubOutboundClient;
	}

	/**
	 * Injects the utilities class
	 *
	 * @param csvUtils the utilities implementation to use.
	 */
	@Required
	public void setCsvUtils(final OutboundServiceCsvUtils csvUtils)
	{
		this.csvUtils = csvUtils;
	}
}

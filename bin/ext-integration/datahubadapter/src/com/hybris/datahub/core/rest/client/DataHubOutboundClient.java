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

package com.hybris.datahub.core.rest.client;

import com.hybris.datahub.core.dto.ResultData;
import com.hybris.datahub.core.rest.DataHubCommunicationException;
import com.hybris.datahub.core.rest.DataHubOutboundException;

import java.util.Map;


/**
 * A REST client to communicate to the Data Hub. Sends CSV Raw Item content to Data Hub
 */
public interface DataHubOutboundClient
{
	/**
	 * @param csvContent the CSV content
	 * @param dataFeed the name of the data feed
	 * @param rawItemType the raw item type
	 * @return the result of the export
	 * @throws com.hybris.datahub.core.rest.DataHubCommunicationException if communication with the Data Hub server failed
	 * @throws com.hybris.datahub.core.rest.DataHubOutboundException if the Data Hub server was unable to successfully complete the export operation
	 */
	public ResultData exportData(final String[] csvContent, final String dataFeed, final String rawItemType)
			throws DataHubOutboundException, DataHubCommunicationException;

	/**
	 * Deletes item with an integration key comprised of the specified primary keys, of the type specified, in the pool
	 * specified on the Data Hub server
	 *
	 * @param poolName name of the data pool the item to be deleted resides in
	 * @param canonicalItemType type code for the canonical item to delete
	 * @param keyFields map of primary key attribute names and values to uniquely identify the item to be deleted
	 * @return the result of the deletion
	 * @throws java.lang.IllegalStateException if exception communicating with the Data Hub occurs
	 * @throws com.hybris.datahub.core.rest.DataHubCommunicationException if communication with the Data Hub server failed
	 * @throws com.hybris.datahub.core.rest.DataHubOutboundException if the Data Hub server was unable to successfully complete the delete operation
	 */
	public ResultData deleteItem(String poolName, String canonicalItemType, Map<String, String> keyFields)
			throws DataHubOutboundException, DataHubCommunicationException;

	/**
	 * Deletes all items originating from the data feed and raw item type specified
	 *
	 * @param feedName Name of the data feed the items originated from
	 * @param rawItemType Name of the raw item type the items originated from
	 * @return the result of the deletion
	 * @throws java.lang.IllegalStateException if exception communicating with the Data Hub occurs
	 * @throws com.hybris.datahub.core.rest.DataHubCommunicationException if communication with the Data Hub server failed
	 * @throws com.hybris.datahub.core.rest.DataHubOutboundException if the Data Hub server was unable to successfully complete the export operation
	 */
	public ResultData deleteByFeed(String feedName, String rawItemType) throws DataHubOutboundException,
			DataHubCommunicationException;

	/**
	 * Deletes item with an integration key comprised of the specified primary keys, originating from the data feed and
	 * raw item type specified
	 *
	 * @param feedName Name of the data feed the items originated from
	 * @param rawItemType Name of the raw item type the items originated from
	 * @param keyFields Map of primary key attribute names and values to uniquely identify the item to be deleted
	 * @return the result of the deletion
	 * @throws java.lang.IllegalStateException if exception communicating with the Data Hub occurs
	 * @throws com.hybris.datahub.core.rest.DataHubCommunicationException if communication with the Data Hub server failed
	 * @throws com.hybris.datahub.core.rest.DataHubOutboundException if the Data Hub server was unable to successfully complete the export operation
	 */
	public ResultData deleteByFeed(String feedName, String rawItemType, Map<String, Object> keyFields)
			throws DataHubOutboundException, DataHubCommunicationException;
}

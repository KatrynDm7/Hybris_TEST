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
 *
 *
 */
package com.sap.wec.adtreco.be.intf;

import java.io.IOException;
import java.net.URISyntaxException;

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.wec.adtreco.be.HMCConfigurationReader;
import de.hybris.platform.sap.core.odata.util.ODataClientService;


/**
 *
 */
public interface ADTInitiativesBE extends BackendBusinessObject
{
	/**
	 * @return path
	 */
	public String getPath();
	
	/**
	 * @param path
	 */
	public void setPath(String path);
	
	/**
	 * @return ODataClientService
	 */
	public ODataClientService getClientService();
	
	/**
	 * @param clientService
	 */
	public void setClientService(final ODataClientService clientService);
	
	/**
	 * @return HMCConfigurationReader
	 */
	public HMCConfigurationReader getConfiguration();
	
	/**
	 * @param configuration
	 */
	public void setConfiguration(final HMCConfigurationReader configuration);

	/**
	 * Get a single campaign from the hybris Marketing server
	 *
	 * @param select
	 * @param keyValue
	 * @param entitySetName
	 * @return ODataEntry
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public ODataEntry getInitiative(String select, String keyValue, String entitySetName) throws ODataException, URISyntaxException, IOException;

	/**
	 * Get a list of campaigns from the hybris Marketing server
	 * @param select
	 * @param filter
	 * @param entitySetName
	 * @param expand
	 * @return ODataFeed
	 * @throws ODataException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public ODataFeed getInitiatives(String select, String filter, String entitySetName, String expand, String orderBy) throws ODataException, IOException, URISyntaxException;
}

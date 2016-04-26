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
package com.sap.wec.adtreco.be.impl;

import de.hybris.platform.sap.core.bol.backend.BackendBusinessObjectBase;
import de.hybris.platform.sap.core.bol.backend.BackendType;

import de.hybris.platform.sap.core.odata.util.ODataClientService;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.sap.wec.adtreco.be.HMCConfigurationReader;
import com.sap.wec.adtreco.be.intf.ADTInitiativesBE;



/**
 * 
 */
@BackendType("CEI")
public class ADTInitiativesBeCEIImpl extends BackendBusinessObjectBase implements ADTInitiativesBE
{
	private static final String SERVICE_URL = "/sap/opu/odata/sap/CUAN_COMMON_SRV";
	private static final String EXPAND = "TargetGroup";
	protected String path;
	protected ODataClientService clientService;
	protected HMCConfigurationReader configuration;
	protected String httpDestinationId;


	public ODataFeed getInitiatives(final String select, final String filter, final String entitySetName, final String expand, String orderBy)
			throws ODataException, URISyntaxException, IOException
	{
		ODataFeed feed = null;
		feed = this.clientService.readFeed(configuration.getHttpDestination().getTargetURL()+ SERVICE_URL, ODataClientService.APPLICATION_XML, entitySetName, expand, 
				select, filter,	orderBy, configuration.getHttpDestination().getUserid(), configuration.getHttpDestination().getPassword());
		return feed;
	}

	public ODataEntry getInitiative(final String select, final String keyValue, final String entitySetName) throws ODataException, IOException, URISyntaxException
	{
		ODataEntry entry = null;
		this.configuration.loadADTConfiguration();
		String serviceUri = configuration.getHttpDestination().getTargetURL() + SERVICE_URL;
		String contentType = ODataClientService.APPLICATION_XML;
		String user = configuration.getHttpDestination().getUserid();
		String password = configuration.getHttpDestination().getPassword();
				
		entry = this.clientService.readEntry(serviceUri, contentType, entitySetName, select, null, EXPAND, keyValue, user, password);
		
		return entry;
	}
	
	public ODataClientService getClientService()
	{
		return clientService;
	}

	public void setClientService(final ODataClientService clientService)
	{
		this.clientService = clientService;
	}

	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(final HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}	

	public String getPath()
	{
		return this.path;
	}

	public void setPath(final String path)
	{
		this.path = path;
	}

}

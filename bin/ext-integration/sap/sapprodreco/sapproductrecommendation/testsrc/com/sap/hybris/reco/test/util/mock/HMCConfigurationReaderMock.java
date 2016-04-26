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
package com.sap.hybris.reco.test.util.mock;

import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;

import javax.annotation.Resource;

import com.sap.hybris.reco.common.util.HMCConfigurationReader;



/**
 *
 */
public class HMCConfigurationReaderMock extends HMCConfigurationReader
{
	private HTTPDestination httpDestination;
	private String httpDestinationId;

	private String rfcDestinationId = "MOCK";
	private String userType = "SAP_HYBRIS_CONSUMER";
	private String usage = "SCENARIO"; //DEFAULT
	
	/**
	 * Get the extensions configuration parameters from the hMC SAP Global Configuration
	 */
	public void setMockConfiguration(String userType, String usage, HTTPDestination httpDestination)
	{
		setUserType(userType);
		setUsage(usage);
		setHttpDestination(httpDestination);
	}


	/**
	 * @return httpDestination
	 */
	public HTTPDestination getHttpDestination()
	{
		return httpDestination;
	}

	/**
	 * @param httpDestination
	 */
	public void setHttpDestination(HTTPDestination httpDestination)
	{
		this.httpDestination = httpDestination;
	}

	/**
	 * @return httpDestinationId
	 */
	public String getHttpDestinationId()
	{
		return httpDestinationId;
	}

	/**
	 * @param httpDestinationId
	 */
	public void setHttpDestinationId(String httpDestinationId)
	{
		this.httpDestinationId = httpDestinationId;
	}
	

	/**
	 * @return rfcDestinationId
	 */
	public String getRfcDestinationId()
	{
		return rfcDestinationId;
	}

	/**
	 * @param rfcDestinationId
	 */
	public void setRfcDestinationId(String rfcDestinationId)
	{
		this.rfcDestinationId = rfcDestinationId;
	}

	/**
	 * @return userType
	 */
	public String getUserType()
	{
		return userType;
	}

	/**
	 * @param userType
	 */
	public void setUserType(String userType)
	{		
		this.userType = userType;
	}
	
	/**
	 * @param itemType
	 */
	public void setItemType(String itemType)
	{
	}

	/**
	 * @return usage
	 */
	public String getUsage()
	{
		return usage;
	}
	
	/**
	 * @param usage
	 */
	public void setUsage(String usage)
	{
		this.usage = usage;
	}
	
}

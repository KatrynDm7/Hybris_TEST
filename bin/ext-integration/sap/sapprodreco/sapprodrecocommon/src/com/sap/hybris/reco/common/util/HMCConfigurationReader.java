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
package com.sap.hybris.reco.common.util;

import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;

import javax.annotation.Resource;


/**
 *
 */
public class HMCConfigurationReader
{
	@Resource(name = "sapCoreDefaultSAPGlobalConfigurationService")
	private SAPGlobalConfigurationService globalConfigurationService;
	
	@Resource(name = "sapCoreHTTPDestinationService")
	private HTTPDestinationServiceImpl httpDestinationService;
	private HTTPDestination httpDestination;
	private String httpDestinationId;
	
	@Resource(name = "sapPRIModuleConfigurationAccess")
	private ModuleConfigurationAccess baseStoreConfigurationService;
	
	private String rfcDestinationId;
	
	private String userType;
	private String usage;
	
	/**
	 * Get the extensions configuration parameters from the hMC SAP Global Configuration
	 */
	public void loadPRIConfiguration()
	{
		String httpId = (String) globalConfigurationService.getProperty("sapproductrecommendation_httpdest");		
		this.setHttpDestinationId(httpId);
		loadHTTPDestination();
	}

	/**
	 * Get the HTTP Destination details from the hMC SAP Integration HTTP Destination configuration
	 * 
	 */
	public void loadHTTPDestination()
	{

		if (this.httpDestinationService != null)
		{
			this.httpDestination = this.httpDestinationService.getHTTPDestination(this.getHttpDestinationId());
		}
	}
	
	/**
	 * Get the RFC Destination details from the hMC SAP Integration HTTP Destination configuration
	 * 
	 */
	public void loadRFCConfiguration()
	{
		String rfcId = (String) globalConfigurationService.getProperty("sapproductrecommendation_rfcdest");
		this.setRfcDestinationId(rfcId);		
	}
	
	/**
	 * Get the User Type from the PRI configuration in the Base Store configuration
	 * 
	 */
	public void loadUserTypeConfiguration()
	{
		String userType = "";
		if (baseStoreConfigurationService!= null) {
			userType = (String) baseStoreConfigurationService.getProperty("sapproductrecommendation_usertype");
		}
		this.setUserType(userType);
	}
	
	/**
	 * Get the User Type from the PRI configuration in the Base Store configuration
	 * 
	 */
	public void loadUsageConfiguration()
	{
		String usage = "";
		if (globalConfigurationService!= null) {
			usage = globalConfigurationService.getProperty("sapproductrecommendation_usage").toString();
		}
		this.setUsage(usage);
	}
	
	/**
	 * @return globalConfigurationService
	 */
	public SAPGlobalConfigurationService getGlobalConfigurationService()
	{
		return globalConfigurationService;
	}

	/**
	 * @param globalConfigurationService
	 */
	public void setGlobalConfigurationService(SAPGlobalConfigurationService globalConfigurationService)
	{
		this.globalConfigurationService = globalConfigurationService;
	}
	
	/**
	 * @return baseStoreConfigurationService
	 */
	public ModuleConfigurationAccess getBaseStoreConfigurationService()
	{
		return baseStoreConfigurationService;
	}


	/**
	 * @param baseStoreConfigurationService
	 */
	public void setBaseStoreConfigurationService(ModuleConfigurationAccess baseStoreConfigurationService)
	{
		this.baseStoreConfigurationService = baseStoreConfigurationService;
	}

	/**
	 * @return httpDestinationService
	 */
	public HTTPDestinationServiceImpl getHttpDestinationService()
	{
		return httpDestinationService;
	}

	/**
	 * @param httpDestinationService
	 */
	public void setHttpDestinationService(HTTPDestinationServiceImpl httpDestinationService)
	{
		this.httpDestinationService = httpDestinationService;
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
		this.loadRFCConfiguration();
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
		this.loadUserTypeConfiguration();
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
		this.loadUsageConfiguration();
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

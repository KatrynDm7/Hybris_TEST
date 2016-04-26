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
package com.sap.wec.adtreco.be;

import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;

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

	/*
	private String httpDestinationId;
	*/
	private String idOrigin = "";
	private String filterCategory = "";
	private String anonIdOrigin = "";


	/**
	 * Get the extensions configuration parameters from the hMC SAP Global Configuration
	 */
	public void loadADTConfiguration()
	{
		loadHTTPDestination();
		loadIdOriginConfiguration();
		loadFilterCategoryConfiguration();
	}

	/**
	 * Get the HTTP Destination details from the hMC SAP Integration HTTP Destination configuration
	 * 
	 */
	public void loadHTTPDestination()
	{
		if (this.globalConfigurationService != null & this.httpDestinationService != null)
		{
			this.httpDestination = (HTTPDestination) httpDestinationService.getHTTPDestination((String)globalConfigurationService.getProperty("sapadtreco_httpdest"));    
		}
	}
	
	/**
	 * Get the RFC Destination details from the hMC SAP Integration HTTP Destination configuration
	 * 
	 */
	public void loadIdOriginConfiguration()
	{
		idOrigin = (String) globalConfigurationService.getProperty("sapadtreco_idOrigin");
		if(idOrigin == null){
			idOrigin = "";
		}
	}
	
	/**
	 * Get the User Type from the PRI configuration in the Base Store configuration
	 * 
	 */
	public void loadFilterCategoryConfiguration()
	{
		filterCategory = (String) globalConfigurationService.getProperty("sapadtreco_filterCategory");
		if( filterCategory == null){
			filterCategory = "";
		}
	}
	
	/**
	 * Get backoffice global configuration service
	 * @return SAPGlobalConfigurationService globalConfigurationService
	 */
	public SAPGlobalConfigurationService getGlobalConfigurationService()
	{
		return globalConfigurationService;
	}

	/**
	 * @param globalConfigurationService
	 */
	public void setGlobalConfigurationService(final SAPGlobalConfigurationService globalConfigurationService)
	{
		this.globalConfigurationService = globalConfigurationService;
	}

	/**
	 * Get Contact ID Origin backoffice configuration
	 * @return String idOrigin
	 */
	public String getIdOrigin()
	{
		return idOrigin;
	}

	/**
	 * @param idOrigin
	 */
	public void setIdOrigin(final String idOrigin)
	{
		this.idOrigin = idOrigin;
	}
	
	/**
	 * @return String anonIdOrigin
	 */
	public String getAnonIdOrigin() {
		return anonIdOrigin;
	}

	/**
	 * @param anonIdOrigin
	 */
	public void setAnonIdOrigin(String anonIdOrigin) {
		this.anonIdOrigin = anonIdOrigin;
	}

	/**
	 * Get Campaign Filter Category from backoffice configuration
	 * @return String filterCategory
	 */
	public String getFilterCategory()
	{
		return filterCategory;
	}

	/**
	 * @param filterCategory
	 */
	public void setFilterCategory(final String filterCategory)
	{
		this.filterCategory = filterCategory;
	}
	
	/**
	 * Get HTTP Destination object containing the backoffice HTTP Destination configuration
	 * @return HTTPDestination httpDestination
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

}
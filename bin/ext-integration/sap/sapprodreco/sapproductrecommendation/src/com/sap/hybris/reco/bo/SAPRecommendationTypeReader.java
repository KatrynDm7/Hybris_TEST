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
package com.sap.hybris.reco.bo;

import de.hybris.platform.core.Registry;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.olingo.odata2.api.exception.ODataException;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.dao.SAPRecommendationType;

/**
 * To fetch a list of model types from PRI using OData service
 */
public class SAPRecommendationTypeReader
{
	protected HMCConfigurationReader configuration;

	/**
	 * @return a list of SAPRecommendationModelType
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public List<SAPRecommendationType> getAllRecommendationTypes() throws ODataException, URISyntaxException,
			IOException
	{
		String usage = configuration.getUsage();
		if(usage.equals("SCENARIO")){
			SAPRecommendationScenarioReader reader = (SAPRecommendationScenarioReader) Registry.getApplicationContext().getBean("sapRecommendationScenarioReader");
			return (reader.getAllRecommendationScenarios());
		}else  if(usage.equals("MODELTYPE")){
			SAPRecommendationModelTypeReader reader = (SAPRecommendationModelTypeReader) Registry.getApplicationContext().getBean("sapRecommendationModelTypeReader");
			return (reader.getAllRecommendationModelTypes());
		}
		return null;
	}
	
	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}


}

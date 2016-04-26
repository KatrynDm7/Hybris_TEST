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
package com.sap.hybris.reco.test.bo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.junit.Test;

import com.sap.hybris.reco.bo.SAPRecommendationTypeReader;
import com.sap.hybris.reco.dao.SAPRecommendationType;
import com.sap.hybris.reco.test.util.mock.HMCConfigurationReaderMock;

import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationImpl;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;

public class SAPRecommendationTypeReaderTest {
	
	SAPRecommendationTypeReader typeReader;
	HMCConfigurationReaderMock configuration;

	@Test
	public void test() {
		SAPHTTPDestinationModel model = new SAPHTTPDestinationModel();
		model.setHttpDestinationName("ANJHTTP");
		model.setTargetURL("https://ldcianj.wdf.sap.corp:44300");
		model.setUserid("reco_model");
		model.setPassword("welcome");
		
		HTTPDestination httpDestination = new HTTPDestinationImpl(model);
		configuration.setUsage("SCENARIO");	
		configuration.setHttpDestination(httpDestination);
		typeReader.setConfiguration(configuration);
		
		try {
			List <SAPRecommendationType> list = typeReader.getAllRecommendationTypes();
			assertNotNull(list);
		} catch (ODataException | URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

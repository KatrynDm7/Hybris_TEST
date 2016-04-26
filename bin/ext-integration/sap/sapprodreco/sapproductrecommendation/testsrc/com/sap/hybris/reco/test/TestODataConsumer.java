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
package com.sap.hybris.reco.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import de.hybris.bootstrap.annotations.UnitTest;

import com.sap.hybris.reco.be.RecommendationEntityManager;
import com.sap.hybris.reco.be.impl.RecommendationEntityManagerCEI;
import com.sap.hybris.reco.bo.SAPRecommendationTypeReader;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;

import de.hybris.platform.sap.core.odata.util.ODataClientService;

import com.sap.hybris.reco.dao.SAPRecommendationType;


/**
 */
@ContextConfiguration(locations =
{ "SapProdRecoTest-spring.xml" })
@UnitTest
public class TestODataConsumer
{
/*	// BE Injection Test
	@Resource(name = "testBusinessObjectBaseBEInjection")
	private BusinessObjectBase boForBEInjection;
	@Resource(name = "testBackendBusinessObjectBaseBEInjection")
	private BackendBusinessObjectBase beForBEInjection;

	// BE Single Implementation Test
	@Resource(name = "testBusinessObjectBaseBESingleImplementation")
	private BusinessObjectBase boForBESingleImpl;
	@Resource(name = "testBackendBusinessObjectBaseBESingleImplementation")
	private BackendBusinessObjectBase beForBESingleImpl;

	// BE Determination Test
	@Resource(name = "testBusinessObjectBaseBEDetermination")
	private BusinessObjectBase boForBEDetermination;*/
//	@Resource(name = "testPRIdefaultrecommendationEntityManagerCEI")
//	private RecommendationEntityManagerCEI modelMgr;
//	@Resource(name = "testPRIsapRecommendationTypeReader")
//	private SAPRecommendationTypeReader reader;
/*	@Resource(name = "testBackendBusinessObjectBaseBEDeterminationERP")
	private BackendBusinessObjectBase beForBEDeterminationERP;

	// BE Determination Test
	@Resource(name = "testBusinessObjectBaseBENotUniqueDetermination")
	private BusinessObjectBase boForBENotUniqueDetermination;*/

	/**
	 * Run All Tests
	 */
	@Test
	public void testSearchAll()
	{
		final SAPRecommendationTypeReader reader = new SAPRecommendationTypeReader();
		RecommendationEntityManager modelMgr = new RecommendationEntityManagerCEI();
		modelMgr.setClientService(new ODataClientService());
		modelMgr.setConfiguration(new HMCConfigurationReader());

		try
		{
			final List<SAPRecommendationType> models = reader.getAllRecommendationTypes();
			Assert.assertNotNull(models);
			Assert.assertEquals(true, models.size() > 0);
		}
		catch (ODataException | URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}

	}

}

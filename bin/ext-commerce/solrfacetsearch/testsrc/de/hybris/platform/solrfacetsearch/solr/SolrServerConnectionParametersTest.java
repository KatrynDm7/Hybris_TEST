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
package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.solrfacetsearch.config.ClusterConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;

import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


public class SolrServerConnectionParametersTest extends AbstractIntegrationTest
{
	@Resource
	private FacetSearchConfigService facetSearchConfigService;

	@Test
	public void testConnectionParameters() throws FacetConfigServiceException, IOException, ImpExException
	{
		FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final ClusterConfig clusterConfigInitial = facetSearchConfig.getSolrConfig().getClusterConfig();
		//check default values
		Assert.assertNotNull(clusterConfigInitial);
		Assert.assertEquals(5000, clusterConfigInitial.getAliveCheckInterval().intValue());
		Assert.assertEquals(8000, clusterConfigInitial.getConnectionTimeout().intValue());
		Assert.assertEquals(30000, clusterConfigInitial.getSocketTimeout().intValue());
		Assert.assertEquals(100, clusterConfigInitial.getMaxConnections().intValue());
		Assert.assertEquals(50, clusterConfigInitial.getMaxConnectionsPerHost().intValue());
		Assert.assertTrue(clusterConfigInitial.isTcpNoDelay());

		importConfig("/test/integration/SolrServerConnectionParametersTest.csv");

		facetSearchConfig = facetSearchConfigService.getConfiguration(getFacetSearchConfigName());
		final ClusterConfig solrClusterConfigChanged = facetSearchConfig.getSolrConfig().getClusterConfig();
		Assert.assertEquals(100, solrClusterConfigChanged.getAliveCheckInterval().intValue());
		Assert.assertEquals(200, solrClusterConfigChanged.getConnectionTimeout().intValue());
		Assert.assertEquals(300, solrClusterConfigChanged.getSocketTimeout().intValue());
		Assert.assertEquals(400, solrClusterConfigChanged.getMaxConnections().intValue());
		Assert.assertEquals(500, solrClusterConfigChanged.getMaxConnectionsPerHost().intValue());
		Assert.assertFalse(solrClusterConfigChanged.isTcpNoDelay());
	}
}

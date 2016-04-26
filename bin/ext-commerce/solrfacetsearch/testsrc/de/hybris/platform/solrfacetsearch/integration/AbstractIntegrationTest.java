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
package de.hybris.platform.solrfacetsearch.integration;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.config.impl.FacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerServiceIntegrationTest;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.SolrService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;


public abstract class AbstractIntegrationTest extends ServicelayerTest
{
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String SOLR_TEST_INSTANCE = "solr.standalone.test.instance";

	protected static final String HW_CATALOG = "hwcatalog";
	protected static final String ONLINE_CATALOG_VERSION = "Online";
	protected static final String STAGED_CATALOG_VERSION = "Staged";

	private static final String PRODUCT_CODE = "HW1100-0024";
	protected static final String FACET_SEARCH_CONFIG_NAME = "testFacetSearchConfig";


	private String testId;
	private String solrTestInstanceEndpoint;

	@Resource
	private FacetSearchConfigService facetSearchConfigService;

	@Resource
	private FacetSearchConfigDao facetSearchConfigDao;

	@Resource(name = "standaloneSolrService")
	private SolrService solrService;

	@Resource
	private ConfigurationService configurationService;

	private FacetSearchConfig facetSearchConfig;
	private SolrFacetSearchConfigModel solrFacetSearchConfigModel;
	private SolrIndexedTypeModel indexedTypeModel;

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	protected String getProductCode()
	{
		return PRODUCT_CODE;
	}

	@Before
	public void setUp() throws Exception
	{
		testId = Long.toString(Thread.currentThread().getId()) + System.currentTimeMillis();
		solrTestInstanceEndpoint = configurationService.getConfiguration().getString(SOLR_TEST_INSTANCE);

		createCoreData();

		importCsv("/test/solrBasics.csv", DEFAULT_ENCODING);
		importCsv("/test/solrHwcatalogStaged.csv", DEFAULT_ENCODING);
		importCsv("/test/solrHwcatalogOnline.csv", DEFAULT_ENCODING);
		importConfig("/test/solrConfigBase.csv");

		loadInitialData();
	}

	@After
	public void tearDown() throws Exception
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();

		for (final IndexedType indexedType : facetSearchConfig.getIndexConfig().getIndexedTypes().values())
		{
			solrService.deleteIndex(facetSearchConfig, indexedType);
		}
	}

	protected void loadInitialData() throws Exception
	{
		// Do nothing by default
	}

	protected String getFacetSearchConfigName()
	{
		return FACET_SEARCH_CONFIG_NAME + testId;
	}

	protected FacetSearchConfig getFacetSearchConfig() throws FacetConfigServiceException
	{
		if (facetSearchConfig == null)
		{
			facetSearchConfig = facetSearchConfigService.getConfiguration(getFacetSearchConfigName());
		}

		return facetSearchConfig;
	}

	protected SolrClient getSolrClient() throws SolrServiceException, FacetConfigServiceException
	{
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		return solrService.getSolrClientForIndexing(facetSearchConfig, indexedType);
	}

	protected void importConfig(final String resource) throws IOException, ImpExException
	{
		importConfig(resource, Collections.emptyMap());
	}

	protected void importConfig(final String resource, final Map<String, String> params) throws IOException, ImpExException
	{
		final InputStream inputStream = DefaultIndexerServiceIntegrationTest.class.getResourceAsStream(resource);
		String impexContent = IOUtils.toString(inputStream, DEFAULT_ENCODING);

		final Map<String, String> impexParams = new HashMap<String, String>();
		impexParams.put("testId", testId);
		impexParams.put("solrServerEndpoint", solrTestInstanceEndpoint);
		impexParams.putAll(params);

		for (final Map.Entry<String, String> impexParam : impexParams.entrySet())
		{
			impexContent = impexContent.replaceAll(Pattern.quote("${" + impexParam.getKey() + "}"), impexParam.getValue());
		}

		final InputStream newInputStream = IOUtils.toInputStream(impexContent, DEFAULT_ENCODING);
		importStream(newInputStream, DEFAULT_ENCODING, resource);
	}

	protected String readFile(final String pathName) throws IOException
	{
		final InputStream inputStream = AbstractIntegrationTest.class.getResourceAsStream(pathName);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		final StringBuffer buffer = new StringBuffer();

		String line = "";

		while ((line = reader.readLine()) != null)
		{
			buffer.append(line);
		}

		return buffer.toString();
	}

	protected void initializeIndexedType()
	{
		if (indexedTypeModel == null)
		{
			solrFacetSearchConfigModel = facetSearchConfigDao.findSolrFacetSearchConfigByName(getFacetSearchConfigName());
			indexedTypeModel = solrFacetSearchConfigModel.getSolrIndexedTypes().get(0);
		}
	}

	public SolrIndexedTypeModel getIndexedTypeModel()
	{
		initializeIndexedType();
		return indexedTypeModel;
	}

	public SolrFacetSearchConfigModel getSolrFacetSearchConfigModel()
	{
		return solrFacetSearchConfigModel;
	}

	public String getTestId()
	{
		return testId;
	}
}

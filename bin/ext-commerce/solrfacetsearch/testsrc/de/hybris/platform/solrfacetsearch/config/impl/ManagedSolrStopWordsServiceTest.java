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
package de.hybris.platform.solrfacetsearch.config.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.solrfacetsearch.config.ClusterConfig;
import de.hybris.platform.solrfacetsearch.config.EndpointURL;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigExportException;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.config.impl.ManagedSolrStopWords.WordSet;
import de.hybris.platform.solrfacetsearch.indexer.SolrCoreNameResolver;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrStopWordModel;
import de.hybris.platform.solrfacetsearch.solr.SolrService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.text.MessageFormat;
import java.util.Collections;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@UnitTest
public class ManagedSolrStopWordsServiceTest
{
	private static final String SOLR_ENDPOINT = "http://localhost:8983/solr";
	private static final String TENANT = "master";
	private static final String SOLR_CORE = "electronics_Product";
	private static final String SOLR_CORE_NAME = TENANT + "_" + SOLR_CORE;

	@Mock
	private FacetSearchConfig facetSearchConfig;

	@Mock
	private IndexedType indexedType;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private SolrCoreNameResolver solrCoreNameResolver;

	@Mock
	private Configuration configuration;

	@Mock
	private ResponseEntity<String> response;

	@Mock
	private ManagedSolrStopWords managedSolrStopWords;

	@Mock
	private WordSet wordSet;

	@Mock
	private IndexConfig indexConfig;

	@Mock
	private LanguageModel enLang;

	@Mock
	private LanguageModel deLang;

	@Mock
	private FacetSearchConfigDao facetSearchConfigDao;

	@Mock
	private SolrFacetSearchConfigModel solrFacetSearchConfigModel;

	@Mock
	private SolrStopWordModel stopWord1;

	@Mock
	private SolrStopWordModel stopWord2;

	@Mock
	private SolrStopWordModel stopWord3;

	@Mock
	private SolrConfig solrConfig;

	@Mock
	private ClusterConfig clusterConfig;

	@Mock
	private EndpointURL endpointURL;

	@Mock
	private TenantService tenantService;

	@Mock
	private SolrService solrService; // NOPMD will be automatically injected in the managedSolrStopWordsService

	@InjectMocks
	private final ManagedSolrStopWordsService managedSolrStopWordsService = new ManagedSolrStopWordsService();

	@Before
	public void setUp() throws FacetConfigServiceException, SolrServiceException
	{
		MockitoAnnotations.initMocks(this);
		createTestData();
	}

	private void createTestData()
	{
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(ManagedSolrStopWordsService.STOPWORDS_FILTER_IGNORE_CASE_KEY, "true"))
				.thenReturn("false");
		Mockito.when(solrCoreNameResolver.getSolrCoreName(indexedType)).thenReturn(SOLR_CORE);

		Mockito.when(response.getStatusCode()).thenReturn(HttpStatus.OK);
		Mockito.when(restTemplate.postForEntity(Matchers.notNull(String.class), Matchers.any(), Matchers.eq(String.class)))
				.thenReturn(response);
		Mockito.when(restTemplate.postForObject(Matchers.notNull(String.class), Matchers.any(), Matchers.eq(String.class)))
				.thenReturn("OK");

		Mockito.when(managedSolrStopWords.getWordSet()).thenReturn(wordSet);
		Mockito.when(wordSet.getInitArgs()).thenReturn(Collections.singletonMap("ignoreCase", "false"));
		Mockito.when(wordSet.getManagedList()).thenReturn(Collections.emptySet());

		Mockito.when(restTemplate.getForObject(Matchers.notNull(String.class), Matchers.eq(ManagedSolrStopWords.class)))
				.thenReturn(managedSolrStopWords);
		Mockito.when(restTemplate.getForObject(Matchers.notNull(String.class), Matchers.eq(String.class))).thenReturn("OK");

		Mockito.when(enLang.getIsocode()).thenReturn("en");
		Mockito.when(deLang.getIsocode()).thenReturn("de");


		Mockito.when(stopWord1.getLanguage()).thenReturn(enLang);
		Mockito.when(stopWord1.getStopWord()).thenReturn("and");

		Mockito.when(stopWord2.getLanguage()).thenReturn(enLang);
		Mockito.when(stopWord2.getStopWord()).thenReturn("or");

		Mockito.when(stopWord3.getLanguage()).thenReturn(deLang);
		Mockito.when(stopWord3.getStopWord()).thenReturn("und");

		Mockito.when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		Mockito.when(facetSearchConfig.getSolrConfig()).thenReturn(solrConfig);

		Mockito.when(solrConfig.getClusterConfig()).thenReturn(clusterConfig);
		Mockito.when(endpointURL.isMaster()).thenReturn(true);
		Mockito.when(endpointURL.getUrl()).thenReturn(SOLR_ENDPOINT);

		Mockito.when(facetSearchConfigDao.findSolrFacetSearchConfigByName(Mockito.anyString()))
				.thenReturn(solrFacetSearchConfigModel);
		Mockito.when(clusterConfig.getEndpointURLs()).thenReturn(Collections.singletonList(endpointURL));

		Mockito.when(indexConfig.getLanguages()).thenReturn(Lists.newArrayList(enLang, deLang));
		Mockito.when(solrFacetSearchConfigModel.getStopWords()).thenReturn(Lists.newArrayList(stopWord1, stopWord2, stopWord3));

		Mockito.when(tenantService.getCurrentTenantId()).thenReturn(TENANT);
	}

	private String getUrl(final String lang)
	{
		return MessageFormat.format(ManagedSolrStopWordsService.PATH, SOLR_ENDPOINT, SOLR_CORE_NAME, lang);
	}

	@Test
	public void fullConfigurationTest() throws FacetConfigExportException
	{
		//when
		managedSolrStopWordsService.exportStopWords(facetSearchConfig, indexedType);

		//get single managed resource once
		Mockito.verify(restTemplate).getForObject(getUrl("en"), ManagedSolrStopWords.class);
		Mockito.verify(restTemplate).getForObject(getUrl("de"), ManagedSolrStopWords.class);

		//add all words
		Mockito.verify(restTemplate).postForEntity(getUrl("en"), Lists.newArrayList("or", "and"), String.class);
		Mockito.verify(restTemplate).postForEntity(getUrl("de"), Lists.newArrayList("und"), String.class);

		//no delete
		Mockito.verify(restTemplate, Mockito.never()).delete(Mockito.anyString());

		//no resource was created
		Mockito.verify(restTemplate, Mockito.never()).postForObject(getUrl("en"),
				Collections.singletonMap("class", "org.apache.solr.rest.schema.analysis.ManagedWordSetResource"), String.class);
		Mockito.verify(restTemplate, Mockito.never()).postForObject(getUrl("de"),
				Collections.singletonMap("class", "org.apache.solr.rest.schema.analysis.ManagedWordSetResource"), String.class);
	}

	@Test(expected = FacetConfigExportException.class)
	public void failedGetFromServer() throws FacetConfigExportException
	{
		//given
		Mockito.when(restTemplate.getForObject(getUrl("en"), ManagedSolrStopWords.class))
				.thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

		//when
		managedSolrStopWordsService.exportStopWords(facetSearchConfig, indexedType);
	}

	@Test(expected = FacetConfigExportException.class)
	public void failedDeleteStopword() throws FacetConfigExportException
	{
		//given
		final ManagedSolrStopWords model = Mockito.mock(ManagedSolrStopWords.class);
		final WordSet ws = Mockito.mock(WordSet.class);

		Mockito.when(ws.getManagedList()).thenReturn(Sets.newHashSet("if"));
		Mockito.when(model.getWordSet()).thenReturn(ws);

		Mockito.when(restTemplate.getForObject(getUrl("en"), ManagedSolrStopWords.class)).thenReturn(model);

		Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(restTemplate)
				.delete(getUrl("en") + "/if");

		//when
		managedSolrStopWordsService.exportStopWords(facetSearchConfig, indexedType);
	}

	@Test(expected = FacetConfigExportException.class)
	public void failedAddStopword() throws FacetConfigExportException
	{
		//given
		Mockito.when(restTemplate.postForEntity(getUrl("en"), Lists.newArrayList("or", "and"), String.class))
				.thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

		//when
		managedSolrStopWordsService.exportStopWords(facetSearchConfig, indexedType);
	}

	@Test
	public void missingManagedResourceTest() throws FacetConfigExportException
	{
		//given
		Mockito.when(restTemplate.getForObject(getUrl("en"), ManagedSolrStopWords.class))
				.thenThrow(new HttpServerErrorException(HttpStatus.NOT_FOUND));

		//when
		managedSolrStopWordsService.exportStopWords(facetSearchConfig, indexedType);

		//then
		Mockito.verify(restTemplate).getForObject(getUrl("en"), ManagedSolrStopWords.class);
		Mockito.verify(restTemplate).getForObject(getUrl("de"), ManagedSolrStopWords.class);

		Mockito.verify(restTemplate).postForObject(getUrl("en"),
				Collections.singletonMap("class", "org.apache.solr.rest.schema.analysis.ManagedWordSetResource"), String.class);

		Mockito.verify(restTemplate).postForEntity(getUrl("en"), Lists.newArrayList("or", "and"), String.class);
		Mockito.verify(restTemplate).postForEntity(getUrl("de"), Lists.newArrayList("und"), String.class);

		Mockito.verify(restTemplate, Mockito.never()).delete(Mockito.anyString());
	}

	@Test
	public void wordsToDeleteTest() throws FacetConfigExportException
	{
		//given
		final ManagedSolrStopWords model = Mockito.mock(ManagedSolrStopWords.class);
		final WordSet ws = Mockito.mock(WordSet.class);

		Mockito.when(ws.getManagedList()).thenReturn(Sets.newHashSet("if"));
		Mockito.when(model.getWordSet()).thenReturn(ws);

		Mockito.when(restTemplate.getForObject(getUrl("en"), ManagedSolrStopWords.class)).thenReturn(model);

		//when
		managedSolrStopWordsService.exportStopWords(facetSearchConfig, indexedType);

		//then
		Mockito.verify(restTemplate).getForObject(getUrl("en"), ManagedSolrStopWords.class);
		Mockito.verify(restTemplate).getForObject(getUrl("de"), ManagedSolrStopWords.class);

		Mockito.verify(restTemplate).postForEntity(getUrl("en"), Lists.newArrayList("or", "and"), String.class);
		Mockito.verify(restTemplate).postForEntity(getUrl("de"), Lists.newArrayList("und"), String.class);

		Mockito.verify(restTemplate).delete(getUrl("en") + "/if");
	}
}

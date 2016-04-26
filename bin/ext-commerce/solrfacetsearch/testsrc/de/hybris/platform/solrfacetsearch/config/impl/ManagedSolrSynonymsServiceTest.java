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
import de.hybris.platform.solrfacetsearch.config.impl.ManagedSolrSynonymsService.SynonymMappingResponse;
import de.hybris.platform.solrfacetsearch.config.impl.ManagedSolrSynonymsService.SynonymMappings;
import de.hybris.platform.solrfacetsearch.indexer.SolrCoreNameResolver;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSynonymConfigModel;
import de.hybris.platform.solrfacetsearch.solr.SolrService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;


@UnitTest
public class ManagedSolrSynonymsServiceTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	private final static String EN_ISOCODE = "en";
	private final static String DE_ISOCODE = "de";
	private final static String TENANT_ID = "master";
	private final static String CORE_NAME = "test_Product";
	private final static String SOLR_SERVER_URL = "http://localhost:8983/solr";

	private Map<String, Set<String>> synonymMapEn;
	private Map<String, Set<String>> synonymMapDe;
	private Map<String, Set<String>> synonymMapForMerge;
	private Map<String, Set<String>> synonymMapWithoutOneSymmetric;
	private List<SolrSynonymConfigModel> synonymList;

	@Mock
	private FacetSearchConfig facetSearchConfig;

	@Mock
	private IndexedType indexedType;

	@Mock
	private SynonymMappings synonymMappings;

	@Mock
	private SynonymMappingResponse synonymMappingResponse;

	@Mock
	private ResponseEntity<String> response;

	@Mock
	private SolrCoreNameResolver solrCoreNameResolver;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Mock
	private LanguageModel enLang;

	@Mock
	private LanguageModel deLang;

	@Mock
	private SolrConfig solrConfig;

	@Mock
	private ClusterConfig clusterConfig;

	@Mock
	private EndpointURL endpointURL;

	@Mock
	private TenantService tenantService;

	@Mock
	private IndexConfig indexConfig;

	@Mock
	private FacetSearchConfigDao facetSearchConfigDao;

	@Mock
	private SolrFacetSearchConfigModel solrFacetSearchConfigModel;

	@Mock
	private SolrService solrService; // NOPMD will be automatically injected in the managedSolrSynonymsService

	@InjectMocks
	private final ManagedSolrSynonymsService managedSolrSynonymsService = new ManagedSolrSynonymsService();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		createTestData();
	}

	private void createTestData() throws Exception
	{
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(ManagedSolrSynonymsService.SYNONYMS_FILTER_IGNORE_CASE_KEY, "true")).thenReturn(
				"false");

		Mockito.when(solrCoreNameResolver.getSolrCoreName(indexedType)).thenReturn(CORE_NAME);

		Mockito.when(facetSearchConfig.getIndexConfig()).thenReturn(indexConfig);
		Mockito.when(facetSearchConfig.getSolrConfig()).thenReturn(solrConfig);
		Mockito.when(solrConfig.getClusterConfig()).thenReturn(clusterConfig);
		Mockito.when(clusterConfig.getEndpointURLs()).thenReturn(Collections.singletonList(endpointURL));
		Mockito.when(endpointURL.isMaster()).thenReturn(true);
		Mockito.when(endpointURL.getUrl()).thenReturn(SOLR_SERVER_URL);

		Mockito.when(tenantService.getCurrentTenantId()).thenReturn(TENANT_ID);
		Mockito.when(indexConfig.getLanguages()).thenReturn(Lists.newArrayList(enLang, deLang));

		Mockito.when(enLang.getIsocode()).thenReturn(EN_ISOCODE);
		Mockito.when(deLang.getIsocode()).thenReturn(DE_ISOCODE);

		Mockito.when(facetSearchConfigDao.findSolrFacetSearchConfigByName(Mockito.anyString())).thenReturn(
				solrFacetSearchConfigModel);

		synonymList = Arrays.asList(createSynonym("from_1", "to_1", enLang), createSynonym("from_2.1,from_2.2", "to_2", enLang),
				createSynonym("from_3.1,from_3.2", "to_3.1,to_3.2", enLang), createSynonym("from_4", null, enLang),
				createSynonym("from_5", "", enLang), createSynonym("from_1", "to_1", deLang),
				createSynonym("from_2.1,from_2.2", "to_2", deLang), createSynonym("from_6.1,from_6.2,from_6.3", "", enLang));

		synonymMapEn = new HashMap<>();
		synonymMapEn.put("from_1", new HashSet<>(Arrays.asList("to_1")));
		synonymMapEn.put("from_2.1", new HashSet<>(Arrays.asList("to_2")));
		synonymMapEn.put("from_2.2", new HashSet<>(Arrays.asList("to_2")));
		synonymMapEn.put("from_3.1", new HashSet<>(Arrays.asList("to_3.1", "to_3.2")));
		synonymMapEn.put("from_3.2", new HashSet<>(Arrays.asList("to_3.1", "to_3.2")));
		synonymMapEn.put("from_4", new HashSet<>(Arrays.asList("from_4")));
		synonymMapEn.put("from_5", new HashSet<>(Arrays.asList("from_5")));
		synonymMapEn.put("from_6.1", new HashSet<>(Arrays.asList("from_6.1", "from_6.2", "from_6.3")));
		synonymMapEn.put("from_6.2", new HashSet<>(Arrays.asList("from_6.1", "from_6.2", "from_6.3")));
		synonymMapEn.put("from_6.3", new HashSet<>(Arrays.asList("from_6.1", "from_6.2", "from_6.3")));


		synonymMapDe = new HashMap<>();
		synonymMapDe.put("from_1", new HashSet<>(Arrays.asList("to_1")));
		synonymMapDe.put("from_2.1", new HashSet<>(Arrays.asList("to_2")));
		synonymMapDe.put("from_2.2", new HashSet<>(Arrays.asList("to_2")));


		synonymMapForMerge = new HashMap<>();
		synonymMapForMerge.put("from_1", new HashSet<>(Arrays.asList("to_1")));
		synonymMapForMerge.put("from_2.1", new HashSet<>(Arrays.asList("to_2")));
		synonymMapForMerge.put("from_for_delete", new HashSet<>(Arrays.asList("to_for_delete")));

		synonymMapWithoutOneSymmetric = new HashMap<>();
		synonymMapWithoutOneSymmetric.put("from_1", new HashSet<>(Arrays.asList("to_1")));
		synonymMapWithoutOneSymmetric.put("from_2.1", new HashSet<>(Arrays.asList("to_2")));
		synonymMapWithoutOneSymmetric.put("from_2.2", new HashSet<>(Arrays.asList("to_2")));
		synonymMapWithoutOneSymmetric.put("from_3.1", new HashSet<>(Arrays.asList("to_3.1", "to_3.2")));
		synonymMapWithoutOneSymmetric.put("from_3.2", new HashSet<>(Arrays.asList("to_3.1", "to_3.2")));
		synonymMapWithoutOneSymmetric.put("from_4", new HashSet<>(Arrays.asList("from_4")));
		synonymMapWithoutOneSymmetric.put("from_5", new HashSet<>(Arrays.asList("from_5")));
		synonymMapWithoutOneSymmetric.put("from_6.1", new HashSet<>(Arrays.asList("from_6.1", "from_6.3")));
		synonymMapWithoutOneSymmetric.put("from_6.3", new HashSet<>(Arrays.asList("from_6.1", "from_6.3")));
	}

	private SolrSynonymConfigModel createSynonym(final String synonymFrom, final String synonymTo, final LanguageModel lang)
	{
		final SolrSynonymConfigModel syn = Mockito.mock(SolrSynonymConfigModel.class);
		Mockito.when(syn.getSynonymFrom()).thenReturn(synonymFrom);
		Mockito.when(syn.getSynonymTo()).thenReturn(synonymTo);
		Mockito.when(syn.getLanguage()).thenReturn(lang);
		return syn;
	}

	@Test
	public void testUpdateSynonymsForConfiguration() throws Exception
	{
		//given
		Mockito.when(solrFacetSearchConfigModel.getSynonyms()).thenReturn(synonymList);
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(SynonymMappingResponse.class))).thenReturn(
				synonymMappingResponse);
		Mockito.when(synonymMappingResponse.getSynonymMappings()).thenReturn(synonymMappings);
		Mockito.when(synonymMappings.getInitArgs()).thenReturn(
				Collections.singletonMap(ManagedSolrSynonymsService.IGNORE_CASE_ATTRIBUTE, "false"));
		Mockito.when(synonymMappings.getManagedMap()).thenReturn(synonymMapForMerge);
		Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.anyMap(), Mockito.eq(String.class))).thenReturn(
				response);
		Mockito.when(response.getStatusCode()).thenReturn(HttpStatus.OK);

		//when
		managedSolrSynonymsService.exportSynonyms(facetSearchConfig, indexedType);

		//then
		Mockito.verify(restTemplate, Mockito.times(2)).delete(Mockito.anyString());
		Mockito.verify(restTemplate, Mockito.times(2)).postForEntity(Mockito.anyString(), Mockito.anyMap(),
				Mockito.eq(String.class));
	}

	@Test
	public void testUpdateSynonymsForConfigurationWithNewSymmetric() throws Exception
	{
		//given
		Mockito.when(solrFacetSearchConfigModel.getSynonyms()).thenReturn(synonymList);
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(SynonymMappingResponse.class))).thenReturn(
				synonymMappingResponse);
		Mockito.when(synonymMappingResponse.getSynonymMappings()).thenReturn(synonymMappings);
		Mockito.when(synonymMappings.getInitArgs()).thenReturn(
				Collections.singletonMap(ManagedSolrSynonymsService.IGNORE_CASE_ATTRIBUTE, "false"));

		Mockito.when(synonymMappings.getManagedMap()).thenReturn(synonymMapWithoutOneSymmetric).thenReturn(synonymMapDe);
		Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.anyMap(), Mockito.eq(String.class))).thenReturn(
				response);
		Mockito.when(response.getStatusCode()).thenReturn(HttpStatus.OK);

		//when
		managedSolrSynonymsService.exportSynonyms(facetSearchConfig, indexedType);

		//then
		Mockito.verify(restTemplate, Mockito.times(2)).delete(Mockito.anyString());
		Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(Mockito.anyString(), Mockito.anyMap(),
				Mockito.eq(String.class));
	}

	@Test
	public void testAddingSynonyms() throws Exception
	{
		//given
		Mockito.when(solrFacetSearchConfigModel.getSynonyms()).thenReturn(synonymList);
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(SynonymMappingResponse.class))).thenReturn(
				synonymMappingResponse);
		Mockito.when(synonymMappingResponse.getSynonymMappings()).thenReturn(synonymMappings);
		Mockito.when(synonymMappings.getInitArgs()).thenReturn(
				Collections.singletonMap(ManagedSolrSynonymsService.IGNORE_CASE_ATTRIBUTE, "false"));
		Mockito.when(synonymMappings.getManagedMap()).thenReturn(Collections.emptyMap());
		Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.anyMap(), Mockito.eq(String.class))).thenReturn(
				response);
		Mockito.when(response.getStatusCode()).thenReturn(HttpStatus.OK);

		//when
		managedSolrSynonymsService.exportSynonyms(facetSearchConfig, indexedType);

		//then
		Mockito.verify(restTemplate, Mockito.times(0)).delete(Mockito.anyString());
		Mockito.verify(restTemplate, Mockito.times(1 + 1)).postForEntity(Mockito.anyString(), Mockito.anyMap(),
				Mockito.eq(String.class));
	}

	@Test
	public void testRemovingSynonyms() throws Exception
	{
		//given
		Mockito.when(solrFacetSearchConfigModel.getSynonyms()).thenReturn(Collections.emptyList());
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(SynonymMappingResponse.class))).thenReturn(
				synonymMappingResponse);
		Mockito.when(synonymMappingResponse.getSynonymMappings()).thenReturn(synonymMappings);
		Mockito.when(synonymMappings.getInitArgs()).thenReturn(
				Collections.singletonMap(ManagedSolrSynonymsService.IGNORE_CASE_ATTRIBUTE, "false"));
		Mockito.when(synonymMappings.getManagedMap()).thenReturn(synonymMapEn).thenReturn(synonymMapDe);

		//when
		managedSolrSynonymsService.exportSynonyms(facetSearchConfig, indexedType);

		//then
		Mockito.verify(restTemplate, Mockito.times(10 + 3)).delete(Mockito.anyString());
		Mockito.verify(restTemplate, Mockito.times(0)).postForEntity(Mockito.anyString(), Mockito.anyMap(),
				Mockito.eq(String.class));
	}

	@Test
	public void testUpdateSynonymsWhenNothingHasChanged() throws Exception
	{
		//given
		Mockito.when(solrFacetSearchConfigModel.getSynonyms()).thenReturn(synonymList);
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(SynonymMappingResponse.class))).thenReturn(
				synonymMappingResponse);
		Mockito.when(synonymMappingResponse.getSynonymMappings()).thenReturn(synonymMappings);
		Mockito.when(synonymMappings.getInitArgs()).thenReturn(
				Collections.singletonMap(ManagedSolrSynonymsService.IGNORE_CASE_ATTRIBUTE, "false"));

		Mockito.when(synonymMappings.getManagedMap()).thenReturn(synonymMapEn).thenReturn(synonymMapDe);

		//when
		managedSolrSynonymsService.exportSynonyms(facetSearchConfig, indexedType);

		//then
		Mockito.verify(restTemplate, Mockito.times(0)).delete(Mockito.anyString());
		Mockito.verify(restTemplate, Mockito.times(0)).postForEntity(Mockito.anyString(), Mockito.anyMap(),
				Mockito.eq(String.class));
	}

	@Test(expected = FacetConfigExportException.class)
	public void testRestClientExceptionThrown() throws Exception
	{
		//given
		Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(SynonymMappingResponse.class))).thenThrow(
				new RestClientException("exception"));

		//when
		managedSolrSynonymsService.exportSynonyms(facetSearchConfig, indexedType);
	}

	@Test
	public void testNullConfigParameter() throws Exception
	{
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// when
		managedSolrSynonymsService.exportSynonyms(null, null);
	}

}

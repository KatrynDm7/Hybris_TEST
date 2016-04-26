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
package de.hybris.platform.solrfacetsearch.suggester;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexUpdateException;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.junit.Test;


public class SolrAutoSuggestServiceTest extends AbstractIntegrationTest
{
	@Resource
	private CommonI18NService commonI18NService;

	@Resource(name = "solrAutoSuggestService")
	private SolrAutoSuggestService solrAutoSuggestService;

	@Override
	protected void loadInitialData() throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException,
			SolrServerException
	{
		final String xmlFile = readFile("/test/integration/SolrAutoSuggestServiceTest.xml");
		final DirectXmlRequest xmlRequest = new DirectXmlRequest("/update", xmlFile);

		final SolrClient solrClient = getSolrClient();
		solrClient.request(xmlRequest);
		solrClient.commit();
	}

	@Test
	public void testSuggestions() throws SolrAutoSuggestException, IndexUpdateException
	{
		final LanguageModel english = commonI18NService.getLanguage("en");

		SolrSuggestion suggestion = null;

		suggestion = solrAutoSuggestService.getAutoSuggestionsForQuery(english, getIndexedTypeModel(), "anyWord");
		assertThat(suggestion.getSuggestions().containsKey("anyWord")).isFalse();

		suggestion = solrAutoSuggestService.getAutoSuggestionsForQuery(english, getIndexedTypeModel(), "suggest"); //intentionally slightly
		// mis-spelled
		assertThat(suggestion.getSuggestions().keySet().size()).isGreaterThan(0);
		assertThat(suggestion.getSuggestions().get("suggest")).isNotEmpty();

		suggestion = solrAutoSuggestService.getAutoSuggestionsForQuery(english, getIndexedTypeModel(), "des");
		assertThat(suggestion.getSuggestions().get("des")).isNull();
	}
}

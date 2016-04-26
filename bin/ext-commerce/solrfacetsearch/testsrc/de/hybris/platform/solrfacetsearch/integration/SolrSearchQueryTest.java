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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SolrQueryConverter;
import de.hybris.platform.solrfacetsearch.search.SolrQueryPostProcessor;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultSolrQueryConverter;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.testframework.Transactional;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.fest.util.Collections;
import org.junit.Test;


@Transactional
public class SolrSearchQueryTest extends AbstractIntegrationTest
{
	@Resource
	private FieldNameTranslator fieldNameTranslator;

	@Resource
	private SolrQueryConverter solrQueryConverter;

	@Override
	protected void loadInitialData() throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException,
			SolrServerException
	{
		//create a file reader to get the string in the xml file
		final String xmlFile = readFile("/test/integration/SolrSearchQuery.xml");
		final DirectXmlRequest xmlRequest = new DirectXmlRequest("/update", xmlFile);

		final SolrClient solrClient = getSolrClient();
		solrClient.request(xmlRequest);
		solrClient.commit();
	}

	@Test
	public void testSearchQuery() throws Exception
	{
		final SolrClient solrClient = getSolrClient();

		//test number of all imported products
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);

		String language = "de";
		query.setLanguage(language);
		final String currency = "eur";
		query.setCurrency(currency);

		assertEquals(10, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());

		query.clearAllFields();

		//test single field
		final String FIELD_NAME = "name";
		final String FIELD_CATEGORY = "categoryCode";
		query.addFacetValue(FIELD_NAME, "Dell");
		assertEquals(1, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());
		query.clearAllFields();
		query.addFacetValue(FIELD_NAME, "maxtor");

		assertEquals(2, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());
		query.clearAllFields();
		query.addFacetValue(FIELD_NAME, "notFound");

		assertEquals(0, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());

		//test multifields
		language = "en";
		query.setLanguage(language);
		query.clearAllFields();
		query.addFacetValue(FIELD_CATEGORY, "camera");
		assertEquals(3, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());
		query.addFacetValue(FIELD_NAME, "sony");
		assertEquals(2, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());

		//test remove field with value
		query.removeFacetValue(FIELD_NAME, "sony");
		assertEquals(3, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());
		//test remove field
		query.removeField("categoryCode");
		assertEquals(10, solrClient.query(solrQueryConverter.convertSolrQuery(query)).getResults().getNumFound());
	}

	@Test
	public void testSearchQueryWithPreProcessors() throws Exception
	{
		final SolrClient solrClient = getSolrClient();

		//test number of all imported products
		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();
		final SearchQuery query = new SearchQuery(facetSearchConfig, indexedType);

		final String language = "de";
		query.setLanguage(language);
		final String currency = "eur";
		query.setCurrency(currency);

		final DefaultSolrQueryConverter converter = new DefaultSolrQueryConverter();
		converter.setFieldNameTranslator(fieldNameTranslator);
		converter.setQueryPostProcessors(Collections.<SolrQueryPostProcessor> list(new SolrQueryPostProcessor()
		{
			@Override
			public SolrQuery process(final SolrQuery query, final SearchQuery solrSearchQuery)
			{
				query.setSort("id", ORDER.asc);
				query.setStart(Integer.valueOf(0));
				query.setRows(Integer.valueOf(10));
				return query;
			}
		}));

		Object prev = null;
		SolrDocumentList results = solrClient.query(converter.convertSolrQuery(query)).getResults();
		assertEquals("Result size limited in PostProcessor", 10, results.size());
		for (final SolrDocument doc : results)
		{
			final Object fieldValue = doc.getFieldValue("id");
			if (prev != null)
			{
				assertTrue(prev instanceof Comparable);
				assertTrue(((Comparable) prev).compareTo(fieldValue) < 0);
			}
			prev = fieldValue;
		}
		converter.setQueryPostProcessors(Collections.<SolrQueryPostProcessor> list(new SolrQueryPostProcessor()
		{

			@Override
			public SolrQuery process(final SolrQuery query, final SearchQuery solrSearchQuery)
			{
				query.setSort("id", ORDER.desc);
				query.setStart(Integer.valueOf(2));
				query.setRows(Integer.valueOf(10));
				return query;
			}
		}));
		results = solrClient.query(converter.convertSolrQuery(query)).getResults();
		assertEquals("Result size limited in PostProcessor", 8, results.size());
		for (final SolrDocument doc : results)
		{
			final Object fieldValue = doc.getFieldValue("id");
			if (prev != null)
			{
				assertTrue(prev instanceof Comparable);
				assertTrue(((Comparable) prev).compareTo(fieldValue) > 0);
			}
			prev = fieldValue;
		}
		converter.setQueryPostProcessors(Collections.<SolrQueryPostProcessor> list(new SolrQueryPostProcessor()
		{
			@Override
			public SolrQuery process(final SolrQuery query, final SearchQuery solrSearchQuery)
			{
				query.setSort("id", ORDER.asc);
				return query;
			}
		}, new SolrQueryPostProcessor()
		{

			@Override
			public SolrQuery process(final SolrQuery query, final SearchQuery solrSearchQuery)
			{
				query.setStart(Integer.valueOf(3));
				return query;
			}
		}, new SolrQueryPostProcessor()
		{

			@Override
			public SolrQuery process(final SolrQuery query, final SearchQuery solrSearchQuery)
			{
				query.setRows(Integer.valueOf(6));
				return query;
			}
		}));
		results = solrClient.query(converter.convertSolrQuery(query)).getResults();
		assertEquals("Result size limited in PostProcessor", 6, results.size());
		for (final SolrDocument doc : results)
		{
			final Object fieldValue = doc.getFieldValue("id");
			if (prev != null)
			{
				assertTrue(prev instanceof Comparable);
				assertTrue(((Comparable) prev).compareTo(fieldValue) <= 0);
			}
			prev = fieldValue;
		}
	}
}

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
package de.hybris.platform.solrfacetsearch.provider.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public abstract class AbstractValueResolverTest
{
	protected static final String INDEXED_PROPERTY_NAME = "indexedProperty";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private SessionService sessionService;

	@Mock
	private QualifierProvider qualifierProvider;

	@Mock
	private JaloSession jaloSession;

	@Mock
	private InputDocument inputDocument;

	@Mock
	private IndexerBatchContext batchContext;

	private FacetSearchConfig facetSearchConfig;

	private IndexConfig indexConfig;

	private IndexedType indexedType;

	private IndexedProperty indexedProperty;

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	protected QualifierProvider getQualifierProvider()
	{
		return qualifierProvider;
	}

	protected InputDocument getInputDocument()
	{
		return inputDocument;
	}

	protected IndexerBatchContext getBatchContext()
	{
		return batchContext;
	}

	protected FacetSearchConfig getFacetSearchConfig()
	{
		return facetSearchConfig;
	}

	protected IndexConfig getIndexConfig()
	{
		return indexConfig;
	}

	protected IndexedType getIndexedType()
	{
		return indexedType;
	}

	protected IndexedProperty getIndexedProperty()
	{
		return indexedProperty;
	}

	@Before
	public void setUpAbstractValueResolverTest()
	{
		MockitoAnnotations.initMocks(this);

		when(sessionService.getRawSession(any(Session.class))).thenReturn(jaloSession);

		facetSearchConfig = new FacetSearchConfig();
		indexConfig = new IndexConfig();
		indexedType = new IndexedType();

		facetSearchConfig.setIndexConfig(indexConfig);

		when(batchContext.getFacetSearchConfig()).thenReturn(facetSearchConfig);
		when(batchContext.getIndexedType()).thenReturn(indexedType);

		indexedProperty = new IndexedProperty();
		indexedProperty.setName(INDEXED_PROPERTY_NAME);
		indexedProperty.setValueProviderParameters(new HashMap<String, String>());
		indexedProperty.setLocalized(false);
	}
}

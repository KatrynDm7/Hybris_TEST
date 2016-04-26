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
package de.hybris.platform.solrfacetsearch.parameterprovider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.solrfacetsearch.config.FlexibleSearchQuerySpec;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeFlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.config.factories.FlexibleSearchQuerySpecFactory;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.provider.ContextAwareParameterProvider;
import de.hybris.platform.solrfacetsearch.provider.ParameterProvider;
import de.hybris.platform.testframework.Transactional;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;


@Transactional
public class ContextAwareParameterProviderTest extends AbstractIntegrationTest
{
	@Resource
	private FlexibleSearchQuerySpecFactory flexibleSearchQuerySpecFactory;

	private IndexedType indexedType;

	@Override
	protected void loadInitialData() throws Exception
	{
		importConfig("/test/integration/ContextAwareParameterProviderTest.csv");

		indexedType = getFacetSearchConfig().getIndexConfig().getIndexedTypes().values().iterator().next();
	}

	/**
	 * This case examines a full-indexer query, which is equipped with {@link ContextAwareParameterProvider}
	 *
	 * @throws Exception
	 */
	@Test
	public void testParameterProvider() throws Exception
	{

		final IndexedTypeFlexibleSearchQuery fullQueryData = indexedType.getFlexibleSearchQueries().get(IndexOperation.FULL);
		final FlexibleSearchQuerySpec fullQuerySpec = flexibleSearchQuerySpecFactory.createIndexQuery(fullQueryData, indexedType,
				getFacetSearchConfig());
		final Map<String, Object> parameters = fullQuerySpec.createParameters();
		assertEquals("Parameter map is of unexpected size", parameters.size(), 2);
		assertTrue("Parameter map doesn't contain expected key", parameters.containsKey("catalogVersion1"));
		assertTrue("Parameter map doesn't contain expected key", parameters.containsKey("catalogVersion2"));
		assertEquals("Parameter not as expected", "Online", parameters.get("catalogVersion1"));
		assertEquals("Parameter not as expected", "Staged", parameters.get("catalogVersion2"));
	}

	/**
	 * This case examines a delete-indexer query, which is equipped with {@link ParameterProvider}
	 *
	 * @throws Exception
	 */
	@Test
	public void testContextAwareParameterProvider() throws Exception
	{
		final IndexedTypeFlexibleSearchQuery deleteQueryData = indexedType.getFlexibleSearchQueries().get(IndexOperation.DELETE);
		final FlexibleSearchQuerySpec deleteQuerySpec = flexibleSearchQuerySpecFactory.createIndexQuery(deleteQueryData,
				indexedType, getFacetSearchConfig());
		final Map<String, Object> parameters = deleteQuerySpec.createParameters();
		assertEquals("Parameter map is of unexpected size", parameters.size(), 1);
		assertTrue("Parameter map doesn't contain expected key", parameters.containsKey("price"));
		assertEquals("Parameter not as expected", Double.valueOf(999.99), parameters.get("price"));
	}
}

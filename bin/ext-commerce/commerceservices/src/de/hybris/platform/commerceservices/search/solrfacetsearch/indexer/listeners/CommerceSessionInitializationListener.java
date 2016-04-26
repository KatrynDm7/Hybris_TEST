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
package de.hybris.platform.commerceservices.search.solrfacetsearch.indexer.listeners;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryListener;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Listener that initializes the session.
 */
public class CommerceSessionInitializationListener implements IndexerQueryListener, IndexerListener, IndexerBatchListener
{
	private BaseSiteService baseSiteService;

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Override
	public void beforeQuery(final IndexerQueryContext queryContext) throws IndexerException
	{
		initializeSession(queryContext.getFacetSearchConfig(), queryContext.getIndexedType());
	}

	@Override
	public void afterQuery(final IndexerQueryContext queryContext) throws IndexerException
	{
		// NOOP
	}

	@Override
	public void afterQueryError(final IndexerQueryContext queryContext) throws IndexerException

	{
		// NOOP
	}

	@Override
	public void beforeBatch(final IndexerBatchContext batchContext) throws IndexerException
	{
		initializeSession(batchContext.getFacetSearchConfig(), batchContext.getIndexedType());
	}

	@Override
	public void afterBatch(final IndexerBatchContext batchContext) throws IndexerException
	{
		// NOOP
	}

	@Override
	public void afterBatchError(final IndexerBatchContext batchContext) throws IndexerException

	{
		// NOOP
	}

	@Override
	public void beforeIndex(final IndexerContext context) throws IndexerException
	{
		initializeSession(context.getFacetSearchConfig(), context.getIndexedType());
	}

	@Override
	public void afterIndex(final IndexerContext context) throws IndexerException
	{
		// NOOP
	}

	@Override
	public void afterIndexError(final IndexerContext context) throws IndexerException

	{
		// NOOP
	}

	protected void initializeSession(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType)
	{
		final IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
		final BaseSiteModel baseSite = indexConfig.getBaseSite();

		if (baseSite != null)
		{
			baseSiteService.setCurrentBaseSite(baseSite, false);
		}
	}
}

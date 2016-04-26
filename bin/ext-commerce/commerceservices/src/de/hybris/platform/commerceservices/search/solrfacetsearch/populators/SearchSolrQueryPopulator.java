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
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.Operator;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class SearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private CommonI18NService commonI18NService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private CatalogVersionService catalogVersionService;
	private FacetSearchService facetSearchService;
	private FacetSearchConfigService facetSearchConfigService;
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public FacetSearchService getFacetSearchService()
	{
		return facetSearchService;
	}

	@Required
	public void setFacetSearchService(final FacetSearchService facetSearchService)
	{
		this.facetSearchService = facetSearchService;
	}

	public FacetSearchConfigService getFacetSearchConfigService()
	{
		return facetSearchConfigService;
	}

	@Required
	public void setFacetSearchConfigService(final FacetSearchConfigService facetSearchConfigService)
	{
		this.facetSearchConfigService = facetSearchConfigService;
	}

	public SolrFacetSearchConfigSelectionStrategy getSolrFacetSearchConfigSelectionStrategy()
	{
		return solrFacetSearchConfigSelectionStrategy;
	}

	@Required
	public void setSolrFacetSearchConfigSelectionStrategy(
			final SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy)
	{
		this.solrFacetSearchConfigSelectionStrategy = solrFacetSearchConfigSelectionStrategy;
	}

	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		// Setup the SolrSearchRequest
		target.setSearchQueryData(source.getSearchQueryData());
		target.setPageableData(source.getPageableData());

		final Collection<CatalogVersionModel> catalogVersions = getSessionProductCatalogVersions();
		if (catalogVersions == null || catalogVersions.isEmpty())
		{
			throw new ConversionException("Missing solr facet search indexed catalog versions");
		}

		target.setCatalogVersions(new ArrayList<CatalogVersionModel>(catalogVersions));

		try
		{
			target.setFacetSearchConfig(getFacetSearchConfig());
		}
		catch (final NoValidSolrConfigException e)
		{
			throw new ConversionException("No valid solrFacetSearchConfig found for the current context", e);
		}
		catch (final FacetConfigServiceException e)
		{
			throw new ConversionException(e.getMessage(), e);
		}

		// We can only search one core so select the indexed type
		target.setIndexedType(getIndexedType(target.getFacetSearchConfig()));

		// Create the solr search query for the config and type (this sets-up the default page size and sort order)
		final SearchQuery searchQuery = createSearchQuery(target.getFacetSearchConfig(), target.getIndexedType(), source
				.getSearchQueryData().getFreeTextSearch());
		searchQuery.setCatalogVersions(target.getCatalogVersions());
		searchQuery.setCurrency(getCommonI18NService().getCurrentCurrency().getIsocode());
		searchQuery.setLanguage(getCommonI18NService().getCurrentLanguage().getIsocode());

		// enable spell checker
		searchQuery.setEnableSpellcheck(true);

		target.setSearchQuery(searchQuery);
	}

	/**
	 * Select the first product catalog version that has a SolrFacetSearchConfig
	 *
	 * @deprecated
	 * @return the selected CatalogVersionModel
	 */
	@Deprecated
	protected CatalogVersionModel getSessionProductCatalogVersion()
	{
		for (final CatalogVersionModel catalogVersion : getSessionProductCatalogVersions())
		{
			final List<SolrFacetSearchConfigModel> facetSearchConfigs = catalogVersion.getFacetSearchConfigs();
			if (facetSearchConfigs != null && !facetSearchConfigs.isEmpty())
			{
				return catalogVersion;
			}
		}
		return null;
	}


	/**
	 * Get all the session catalog versions that belong to product catalogs of the current site.
	 *
	 * @return the list of session catalog versions
	 */
	protected Collection<CatalogVersionModel> getSessionProductCatalogVersions()
	{
		final BaseSiteModel currentSite = getBaseSiteService().getCurrentBaseSite();
		final List<CatalogModel> productCatalogs = getBaseSiteService().getProductCatalogs(currentSite);

		final Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();

		final Collection<CatalogVersionModel> result = new ArrayList<CatalogVersionModel>();
		for (final CatalogVersionModel sessionCatalogVersion : sessionCatalogVersions)
		{
			if (productCatalogs.contains(sessionCatalogVersion.getCatalog()))
			{
				result.add(sessionCatalogVersion);
			}
		}
		return result;
	}

	/**
	 * Resolves suitable {@link FacetSearchConfig} for the query based on the configured strategy bean.<br>
	 *
	 * @return {@link FacetSearchConfig} that is converted from {@link SolrFacetSearchConfigModel}
	 * @throws NoValidSolrConfigException
	 *            , FacetConfigServiceException
	 */
	protected FacetSearchConfig getFacetSearchConfig() throws NoValidSolrConfigException, FacetConfigServiceException
	{
		final SolrFacetSearchConfigModel solrFacetSearchConfigModel = solrFacetSearchConfigSelectionStrategy
				.getCurrentSolrFacetSearchConfig();
		return facetSearchConfigService.getConfiguration(solrFacetSearchConfigModel.getName());
	}

	protected IndexedType getIndexedType(final FacetSearchConfig config)
	{
		final IndexConfig indexConfig = config.getIndexConfig();

		// Strategy for working out which of the available indexed types to use
		final Collection<IndexedType> indexedTypes = indexConfig.getIndexedTypes().values();
		if (indexedTypes != null && !indexedTypes.isEmpty())
		{
			// When there are multiple - select the first
			return indexedTypes.iterator().next();
		}

		// No indexed types
		return null;
	}

	protected SearchQuery createSearchQuery(final FacetSearchConfig facetSearchConfig, final IndexedType indexedType,
			final String freeTextSearch)
	{
		SearchQuery searchQuery;

		if (facetSearchConfig.getSearchConfig().isLegacyMode())
		{
			searchQuery = new SearchQuery(facetSearchConfig, indexedType);
			searchQuery.setDefaultOperator(Operator.OR);
			searchQuery.setUserQuery(freeTextSearch);
		}
		else
		{
			searchQuery = facetSearchService.createFreeTextSearchQuery(facetSearchConfig, indexedType, freeTextSearch);
		}

		return searchQuery;
	}
}

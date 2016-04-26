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
package de.hybris.platform.commerceservices.search.solrfacetsearch.impl;

import de.hybris.platform.commerceservices.search.ProductSearchAutocompleteService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the AutocompleteService
 */
public class DefaultSolrProductSearchAutocompleteService implements ProductSearchAutocompleteService<AutocompleteSuggestion>
{
	private static final Logger LOG = Logger.getLogger(DefaultSolrProductSearchAutocompleteService.class);

	private FacetSearchConfigService facetSearchConfigService;
	private CommonI18NService commonI18NService;
	private SolrAutoSuggestService solrAutoSuggestService;
	private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;


	protected FacetSearchConfigService getFacetSearchConfigService()
	{
		return facetSearchConfigService;
	}

	@Required
	public void setFacetSearchConfigService(final FacetSearchConfigService facetSearchConfigService)
	{
		this.facetSearchConfigService = facetSearchConfigService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}



	@Override
	public List<AutocompleteSuggestion> getAutocompleteSuggestions(final String input)
	{
		try
		{
			final SolrFacetSearchConfigModel solrFacetSearchConfigModel = getSolrFacetSearchConfigSelectionStrategy()
					.getCurrentSolrFacetSearchConfig();

			final FacetSearchConfig facetSearchConfig = getFacetSearchConfigService().getConfiguration(
					solrFacetSearchConfigModel.getName());
			final IndexedType indexedType = getIndexedType(facetSearchConfig);

			final SolrIndexedTypeModel indexedTypeModel = findIndexedTypeModel(solrFacetSearchConfigModel, indexedType);

			final SolrSuggestion suggestions = getSolrAutoSuggestService().getAutoSuggestionsForQuery(
					getCommonI18NService().getCurrentLanguage(), indexedTypeModel, input);


			return findBestSuggestions(suggestions, input);
		}
		catch (final SolrAutoSuggestException e)
		{
			LOG.warn("Error retrieving autocomplete suggestions", e);
		}
		catch (final FacetConfigServiceException e)
		{
			LOG.warn("Error retrieving autocomplete suggestions", e);
		}
		catch (final IndexerException e)
		{
			LOG.warn("Error retrieving autocomplete suggestions", e);
		}
		catch (final NoValidSolrConfigException e)
		{
			LOG.warn("Error retrieving autocomplete suggestions", e);
		}
		return null;
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


	protected SolrIndexedTypeModel findIndexedTypeModel(final SolrFacetSearchConfigModel facetSearchConfigModel,
			final IndexedType indexedType) throws IndexerException
	{
		for (final SolrIndexedTypeModel type : facetSearchConfigModel.getSolrIndexedTypes())
		{
			if (solrIndexedTypeCodeResolver.resolveIndexedTypeCode(type).equals(indexedType.getUniqueIndexedTypeCode()))
			{
				return type;
			}
		}
		throw new IndexerException("Could not find matching model for type: " + indexedType.getCode());
	}


	protected SolrAutoSuggestService getSolrAutoSuggestService()
	{
		return solrAutoSuggestService;
	}

	@Required
	public void setSolrAutoSuggestService(final SolrAutoSuggestService solrAutoSuggestService)
	{
		this.solrAutoSuggestService = solrAutoSuggestService;
	}


	protected List<AutocompleteSuggestion> findBestSuggestions(final SolrSuggestion solrSuggestion, final String input)
	{
		final String trimmedInput = input.trim();

		final String lastTerm;
		final String precedingTerms;

		// Only provide suggestions for the last 'word' in the input
		final int indexOfLastSpace = trimmedInput.lastIndexOf(' ');
		if (indexOfLastSpace >= 0)
		{
			lastTerm = trimmedInput.substring(indexOfLastSpace + 1);
			precedingTerms = trimmedInput.substring(0, indexOfLastSpace).trim();
		}
		else
		{
			lastTerm = trimmedInput;
			precedingTerms = null;
		}

		final List<AutocompleteSuggestion> result = new ArrayList<AutocompleteSuggestion>();

		// Get the suggestions for the last term
		final Collection<String> suggestions = solrSuggestion.getSuggestions().get(lastTerm.toLowerCase());
		if (suggestions != null)
		{
			for (final String suggestion : suggestions)
			{
				final String fullSuggestion = precedingTerms == null ? suggestion : precedingTerms + " " + suggestion;

				final AutocompleteSuggestion autocompleteSuggestion = createAutocompleteSuggestion();
				autocompleteSuggestion.setTerm(fullSuggestion);
				result.add(autocompleteSuggestion);
			}
		}

		return result;
	}

	protected AutocompleteSuggestion createAutocompleteSuggestion()
	{
		return new AutocompleteSuggestion();
	}

	@Required
	public void setSolrIndexedTypeCodeResolver(final SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver)
	{
		this.solrIndexedTypeCodeResolver = solrIndexedTypeCodeResolver;
	}


	protected SolrFacetSearchConfigSelectionStrategy getSolrFacetSearchConfigSelectionStrategy()
	{
		return solrFacetSearchConfigSelectionStrategy;
	}

	@Required
	public void setSolrFacetSearchConfigSelectionStrategy(
			final SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy)
	{
		this.solrFacetSearchConfigSelectionStrategy = solrFacetSearchConfigSelectionStrategy;
	}
}

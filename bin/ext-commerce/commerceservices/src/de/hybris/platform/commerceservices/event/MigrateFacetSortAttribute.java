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
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.commerceservices.enums.SolrIndexedPropertyFacetSort;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author KKW
 * 
 */
public class MigrateFacetSortAttribute extends AbstractEventListener<AfterInitializationEndEvent>
{

	private final static Logger LOG = Logger.getLogger(MigrateFacetSortAttribute.class);
	private Map<String, String> facetSort2sortProvidersMapping;

	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;
	private TypeService typeService;

	@Override
	protected void onEvent(final AfterInitializationEndEvent event)
	{
		final Collection<SolrIndexedPropertyModel> modelsToSave = new ArrayList<SolrIndexedPropertyModel>();
		for (final String facetSortString : getFacetSort2sortProvidersMapping().keySet())
		{
			final String sortProviderName = getFacetSort2sortProvidersMapping().get(facetSortString);
			final EnumerationValueModel facetSort = getFacetSortInstance(facetSortString);
			final Collection<SolrIndexedPropertyModel> sortedIndexedProperties = getIndexedPropertiesForFacetSort(facetSort);
			if (!sortedIndexedProperties.isEmpty() && LOG.isInfoEnabled())
			{
				LOG.info(sortedIndexedProperties.size() + " instances of " + SolrIndexedPropertyModel._TYPECODE
						+ " were found that use " + SolrIndexedPropertyFacetSort._TYPECODE + " : " + facetSortString);
				LOG.info("Replacing with appropriate sortProvider : " + sortProviderName);
			}
			for (final SolrIndexedPropertyModel indexedProperty : sortedIndexedProperties)
			{
				indexedProperty.setCustomFacetSortProvider(sortProviderName);
				modelsToSave.add(indexedProperty);
			}
		}
		getModelService().saveAll(modelsToSave);
	}

	protected EnumerationValueModel getFacetSortInstance(final String facetSortString)
	{
		final String query = "SELECT {PK} FROM {SolrIndexedPropertyFacetSort} WHERE {code}=?code";
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query, Collections.singletonMap("code", facetSortString));
		final List<EnumerationValueModel> result = getFlexibleSearchService().<EnumerationValueModel> search(searchQuery)
				.getResult();
		return result.get(0);
	}

	protected Collection<SolrIndexedPropertyModel> getIndexedPropertiesForFacetSort(final EnumerationValueModel facetSort)
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {pk} FROM {" + SolrIndexedPropertyModel._TYPECODE
				+ "} WHERE {" + SolrIndexedPropertyModel.FACETSORT + "} = ?facetSort", Collections.singletonMap("facetSort",
				facetSort.getPk()));
		final List<SolrIndexedPropertyModel> result = getFlexibleSearchService().<SolrIndexedPropertyModel> search(searchQuery)
				.getResult();
		return result == null ? Collections.<SolrIndexedPropertyModel> emptyList() : result;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @return the typeService
	 */
	protected TypeService getTypeService()
	{
		return typeService;
	}

	/**
	 * @param typeService
	 *           the typeService to set
	 */
	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	@Required
	public void setFacetSort2sortProvidersMapping(final Map<String, String> facetSort2sortProvidersMapping)
	{
		this.facetSort2sortProvidersMapping = facetSort2sortProvidersMapping;
	}

	protected Map<String, String> getFacetSort2sortProvidersMapping()
	{
		return facetSort2sortProvidersMapping;
	}
}

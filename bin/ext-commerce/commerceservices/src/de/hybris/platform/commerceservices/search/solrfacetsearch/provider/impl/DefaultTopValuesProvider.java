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
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.TopValuesProvider;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.FacetValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultTopValuesProvider implements TopValuesProvider
{
	private int topFacetCount = 5;

	protected int getTopFacetCount()
	{
		return topFacetCount;
	}

	// Optional
	public void setTopFacetCount(final int topFacetCount)
	{
		this.topFacetCount = topFacetCount;
	}

	@Override
	public List<FacetValue> getTopValues(final IndexedProperty indexedProperty, final List<FacetValue> facets)
	{
		// // Sort the facet values by Count in descending order
		Collections.sort(facets, FacetValueCountComparator.INSTANCE);

		final List<FacetValue> topFacetValues = new ArrayList<FacetValue>();

		if (facets != null)
		{
			for (final FacetValue facetValue : facets)
			{
				if (facetValue != null && (topFacetValues.size() < getTopFacetCount() || facetValue.isSelected()))
				{
					topFacetValues.add(facetValue);
				}
			}

			if (topFacetValues.size() >= facets.size())
			{
				return Collections.emptyList();
			}
		}

		return topFacetValues;
	}

	public static class FacetValueCountComparator extends AbstractComparator<FacetValue>
	{
		public static final FacetValueCountComparator INSTANCE = new FacetValueCountComparator();

		@Override
		protected int compareInstances(final FacetValue facet1, final FacetValue facet2)
		{
			final long facet1Count = facet1.getCount();
			final long facet2Count = facet2.getCount();
			return facet1Count < facet2Count ? 1 : (facet1Count > facet2Count ? -1 : 0);
		}
	}
}

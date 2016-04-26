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

import de.hybris.platform.commerceservices.model.solrsearch.config.SolrSortModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;


public class CommerceIndexedTypePopulator implements Populator<SolrIndexedTypeModel, IndexedType>
{
	private Converter<SolrSortModel, IndexedTypeSort> indexedTypeSortConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SolrIndexedTypeModel source, final IndexedType target) throws ConversionException
	{
		target.setSorts(getSortsFromItem(source));
		target.setSortsByCode(getSortsByCode(target));
	}

	protected Map<String, IndexedTypeSort> getSortsByCode(final IndexedType source)
	{
		final Map<String, IndexedTypeSort> map = new HashMap<String, IndexedTypeSort>();
		for (final IndexedTypeSort indexedTypeSort : source.getSorts())
		{
			map.put(indexedTypeSort.getCode(), indexedTypeSort);
		}
		return map;
	}

	protected List<IndexedTypeSort> getSortsFromItem(final SolrIndexedTypeModel itemType)
	{
		final List<SolrSortModel> sorts = itemType.getSorts();
		if (CollectionUtils.isEmpty(sorts))
		{
			return Collections.emptyList();
		}

		final List<IndexedTypeSort> result = new ArrayList<IndexedTypeSort>(sorts.size());
		for (final SolrSortModel sort : sorts)
		{
			result.add(getSortFromItem(sort));
		}
		return result;
	}

	protected IndexedTypeSort getSortFromItem(final SolrSortModel sort)
	{
		return getIndexedTypeSortConverter().convert(sort);
	}

	@Required
	public void setIndexedTypeSortConverter(final Converter<SolrSortModel, IndexedTypeSort> indexedTypeSortConverter)
	{
		this.indexedTypeSortConverter = indexedTypeSortConverter;
	}

	protected Converter<SolrSortModel, IndexedTypeSort> getIndexedTypeSortConverter()
	{
		return indexedTypeSortConverter;
	}
}

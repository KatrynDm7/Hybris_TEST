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
package de.hybris.platform.b2bacceleratorfacades.search.impl;

import de.hybris.platform.b2bacceleratorfacades.api.search.SearchFacade;
import de.hybris.platform.b2bacceleratorfacades.search.data.ProductSearchStateData;
import de.hybris.platform.b2bacceleratorservices.search.B2BProductSearchService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.ProductSearchService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class DefaultB2BFlexibleSearchProductSearchFacade<ITEM extends ProductData> implements
		SearchFacade<ProductData, ProductSearchStateData>
{
	private ProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>> productSearchService;

	private static final String ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER = "storefront.advancedsearch.delimiter";
	private static final String ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER_DEFAULT = ",";

	private B2BProductSearchService b2bFlexibleSearchProductSearchService;

	private Converter<SearchPageData, ProductSearchPageData> flexibleSearchConverter;

	private Populator<Object, ProductSearchPageData<SearchStateData, ProductData>> productSearchPageVariantsPopulator;


	@Override
	public ProductSearchPageData<SearchStateData, ProductData> search(final ProductSearchStateData searchState,
			final PageableData pageableData)
	{

		final List<String> skus = splitSkusAsList(searchState.getQuery().getValue());
		final ProductSearchPageData<SearchStateData, ProductData> productSearchPageData = getFlexibleSearchConverter().convert(
				b2bFlexibleSearchProductSearchService.findProductsBySkus(skus, pageableData));

		if (searchState.getPopulateVariants().booleanValue())
		{
			productSearchPageVariantsPopulator.populate(new Object(), productSearchPageData);
		}

		return productSearchPageData;
	}

	@Override
	public List<AutocompleteSuggestionData> autocomplete(final ProductSearchStateData searchState) throws NotImplementedException
	{
		throw new UnsupportedOperationException("Autocomplete not available in flexible search");
	}


	protected List<String> splitSkusAsList(final String skus)
	{
		return Arrays.asList(StringUtils.split(skus,
				Config.getString(ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER, ADVANCED_SEARCH_PRODUCT_IDS_DELIMITER_DEFAULT)));
	}

	protected Converter<SearchPageData, ProductSearchPageData> getFlexibleSearchConverter()
	{
		return flexibleSearchConverter;
	}

	@Required
	public void setFlexibleSearchConverter(final Converter<SearchPageData, ProductSearchPageData> flexibleSearchConverter)
	{
		this.flexibleSearchConverter = flexibleSearchConverter;
	}

	@Required
	public void setB2bFlexibleSearchProductSearchService(final B2BProductSearchService b2bFlexibleSearchProductSearchService)
	{
		this.b2bFlexibleSearchProductSearchService = b2bFlexibleSearchProductSearchService;
	}

	@Required
	public void setProductSearchPageVariantsPopulator(
			final Populator<Object, ProductSearchPageData<SearchStateData, ProductData>> productSearchPageVariantsPopulator)
	{
		this.productSearchPageVariantsPopulator = productSearchPageVariantsPopulator;
	}

	protected Populator<Object, ProductSearchPageData<SearchStateData, ProductData>> getProductSearchPageVariantsPopulator()
	{
		return productSearchPageVariantsPopulator;
	}

}

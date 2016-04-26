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
package de.hybris.platform.b2bacceleratorfacades.search.converters.impl;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultB2BFlexibleSearchPopulator implements Populator<SearchPageData, ProductSearchPageData>, Serializable
{
	private Collection<ProductOption> productBasicSearchOptions;

	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	@Override
	public void populate(final SearchPageData searchPageData, final ProductSearchPageData target) throws ConversionException
	{
		target.setResults(getProductDataList(searchPageData));
		target.setPagination(searchPageData.getPagination());
		target.setSorts(searchPageData.getSorts());
		target.setCurrentQuery(getSearchStateData());
	}

	protected SearchStateData getSearchStateData()
	{
		final SearchQueryData searchQueryData = new SearchQueryData();

		final SearchStateData searchStateData = new SearchStateData();
		searchStateData.setQuery(searchQueryData);

		return searchStateData;
	}

	protected List<ProductData> getProductDataList(final SearchPageData searchPageData)
	{

		final List<ProductModel> productModelList = searchPageData.getResults();
		final List<ProductData> productDataList = new ArrayList<>(productModelList.size());

		for (final ProductModel productModel : productModelList)
		{
			final ProductData productData = new ProductData();

			if (productConfiguredPopulator != null && productModel != null)
			{
				productData.setCode(productModel.getCode());
				productConfiguredPopulator.populate(productModel, productData, productBasicSearchOptions);
			}

			productDataList.add(productData);
		}

		return productDataList;
	}

	@Required
	public void setProductConfiguredPopulator(
			final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConfiguredPopulator = productConfiguredPopulator;
	}


	protected Collection<ProductOption> getProductBasicSearchOptions()
	{
		return productBasicSearchOptions;
	}

	@Required
	public void setProductBasicSearchOptions(final Collection<ProductOption> productBasicSearchOptions)
	{
		this.productBasicSearchOptions = productBasicSearchOptions;
	}


}

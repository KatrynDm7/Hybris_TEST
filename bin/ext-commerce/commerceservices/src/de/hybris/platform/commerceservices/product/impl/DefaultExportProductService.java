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
package de.hybris.platform.commerceservices.product.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.product.ExportProductService;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Maps;


/**
 * Default implementation of {@link ExportProductService}
 */
public class DefaultExportProductService implements ExportProductService
{
	private PagedFlexibleSearchService pagedFlexibleSearchService;


	@Override
	public SearchPageData<ProductModel> getAllProducts(final Collection<CatalogVersionModel> catalogVersions, final int start,
			final int count)
	{
		final Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("catalogVersions", catalogVersions);

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(start);
		pageableData.setPageSize(count);

		return pagedFlexibleSearchService.search("SELECT {" + ProductModel.PK + "} FROM {" + ProductModel._TYPECODE + "} WHERE {"
				+ ProductModel.CATALOGVERSION + "} IN (?catalogVersions)", parameters, pageableData);
	}

	@Override
	public SearchPageData<ProductModel> getModifiedProducts(final Collection<CatalogVersionModel> catalogVersions,
			final Date timestamp, final int start, final int count)
	{
		final Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("catalogVersions", catalogVersions);
		parameters.put("modifiedTime", timestamp);

		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(start);
		pageableData.setPageSize(count);

		return pagedFlexibleSearchService.search("SELECT {" + ProductModel.PK + "} FROM {" + ProductModel._TYPECODE + "} WHERE {"
				+ ProductModel.CATALOGVERSION + "} IN (?catalogVersions) AND {" + ProductModel.MODIFIEDTIME + "} > ?modifiedTime",
				parameters, pageableData);
	}


	@Required
	public void setPagedFlexibleSearchService(final PagedFlexibleSearchService pagedFlexibleSearchService)
	{
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}


}

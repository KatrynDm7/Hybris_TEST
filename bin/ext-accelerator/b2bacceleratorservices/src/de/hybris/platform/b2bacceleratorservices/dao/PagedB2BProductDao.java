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
package de.hybris.platform.b2bacceleratorservices.dao;

import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;


public interface PagedB2BProductDao<M> extends PagedGenericDao<M>
{
	/**
	 * Finds all visible {@link de.hybris.platform.core.model.product.ProductModel} for a given list of skus
	 * 
	 * @param skus
	 * @param pageableData
	 * @return A paged result of products
	 */
	SearchPageData<ProductModel> findPagedProductsForSkus(Collection<String> skus, PageableData pageableData);
}

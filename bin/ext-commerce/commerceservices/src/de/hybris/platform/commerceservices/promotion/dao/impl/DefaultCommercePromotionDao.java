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
package de.hybris.platform.commerceservices.promotion.dao.impl;

import de.hybris.platform.commerceservices.promotion.dao.CommercePromotionDao;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultCommercePromotionDao extends AbstractItemDao implements CommercePromotionDao
{

	private final static String FIND_PROMOTION_FOR_CODE = "SELECT {" + AbstractPromotionModel.PK + "} FROM {"
			+ AbstractPromotionModel._TYPECODE + "} WHERE {" + AbstractPromotionModel.CODE + "} = ?code AND {"
			+ AbstractPromotionModel.IMMUTABLEKEYHASH + "} is null";

	private final static String FIND_ORDER_PROMOTION = "SELECT {" + OrderPromotionModel.PK + "} " + "FROM {"
			+ OrderPromotionModel._TYPECODE + " } WHERE {" + OrderPromotionModel.IMMUTABLEKEYHASH + "} is null";

	private final static String FIND_ORDER_PROMOTION_FOR_GROUP = "SELECT DISTINCT {" + OrderPromotionModel.PK + "} " + "FROM {"
			+ OrderPromotionModel._TYPECODE + " } " + "WHERE {" + OrderPromotionModel.PROMOTIONGROUP
			+ "} IN (?promotionGroups) AND {" + OrderPromotionModel.IMMUTABLEKEYHASH + "} is null";

	private final static String FIND_PRODUCT_PROMOTION = "SELECT {" + ProductPromotionModel.PK + "} " + "FROM {"
			+ ProductPromotionModel._TYPECODE + " } WHERE {" + ProductPromotionModel.IMMUTABLEKEYHASH + "} is null";

	private final static String FIND_PRODUCT_PROMOTION_FOR_GROUP = "SELECT DISTINCT {" + ProductPromotionModel.PK + "} "
			+ "FROM {" + ProductPromotionModel._TYPECODE + " } " + "WHERE {" + ProductPromotionModel.PROMOTIONGROUP
			+ "} IN (?promotionGroups) AND {" + ProductPromotionModel.IMMUTABLEKEYHASH + "} is null";

	@Override
	public List<AbstractPromotionModel> findPromotionForCode(final String code)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		return doSearch(FIND_PROMOTION_FOR_CODE, params, AbstractPromotionModel.class);
	}

	@Override
	public List<ProductPromotionModel> findProductPromotions()
	{
		return doSearch(FIND_PRODUCT_PROMOTION, null, ProductPromotionModel.class);
	}

	@Override
	public List<OrderPromotionModel> findOrderPromotions()
	{
		return doSearch(FIND_ORDER_PROMOTION, null, OrderPromotionModel.class);
	}

	@Override
	public List<ProductPromotionModel> findProductPromotions(final Collection<PromotionGroupModel> promotionGroups)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("promotionGroups", promotionGroups);
		return doSearch(FIND_PRODUCT_PROMOTION_FOR_GROUP, params, ProductPromotionModel.class);
	}

	@Override
	public List<OrderPromotionModel> findOrderPromotions(final Collection<PromotionGroupModel> promotionGroups)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("promotionGroups", promotionGroups);
		return doSearch(FIND_ORDER_PROMOTION_FOR_GROUP, params, OrderPromotionModel.class);
	}


	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}

		fQuery.setResultClassList(Collections.singletonList(resultClass));

		final SearchResult<T> searchResult = search(fQuery);
		return searchResult.getResult();
	}

}

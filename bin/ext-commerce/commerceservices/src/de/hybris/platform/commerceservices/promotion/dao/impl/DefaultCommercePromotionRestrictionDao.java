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


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hybris.platform.commerceservices.model.promotions.PromotionOrderRestrictionModel;
import de.hybris.platform.commerceservices.promotion.dao.CommercePromotionRestrictionDao;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;


public class DefaultCommercePromotionRestrictionDao extends AbstractItemDao implements CommercePromotionRestrictionDao
{
	private final static String FIND_PROMOTION_RESTRICTION = "SELECT {" + AbstractPromotionRestrictionModel.PK + "} FROM {"
			+ AbstractPromotionRestrictionModel._TYPECODE + "} WHERE {" + AbstractPromotionRestrictionModel.PROMOTION
			+ "} = ?promotion";
	private final static String FIND_PROMOTION_ORDER_RESTRICTION = "SELECT {" + PromotionOrderRestrictionModel.PK + "} FROM {"
			+ PromotionOrderRestrictionModel._TYPECODE + "} WHERE {" + PromotionOrderRestrictionModel.PROMOTION + "} = ?promotion";

	@Override
	public List<AbstractPromotionRestrictionModel> findPromotionRestriction(final AbstractPromotionModel promotion)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("promotion", promotion);
		return doSearch(FIND_PROMOTION_RESTRICTION, params, AbstractPromotionRestrictionModel.class);
	}

	@Override
	public List<PromotionOrderRestrictionModel> findPromotionOrderRestriction(final AbstractPromotionModel promotion)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("promotion", promotion);
		return doSearch(FIND_PROMOTION_ORDER_RESTRICTION, params, PromotionOrderRestrictionModel.class);
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

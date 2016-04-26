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
package de.hybris.platform.commerceservices.delivery.dao.impl;

import de.hybris.platform.commerceservices.delivery.dao.PickupDeliveryModeDao;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultPickupDeliveryModeDao extends AbstractItemDao implements PickupDeliveryModeDao
{
	private static final String STORE_TO_DELIVERY_MODE_RELATION = "BaseStore2DeliveryModeRel";

	@Override
	public List<DeliveryModeModel> findPickupDeliveryModesForAbstractOrder(final AbstractOrderModel abstractOrder)
	{
		final StringBuilder query = new StringBuilder("SELECT {").append(PickUpDeliveryModeModel.PK).append("}");
		query.append(" FROM {").append(PickUpDeliveryModeModel._TYPECODE).append(" AS dm");
		query.append("	JOIN ").append(STORE_TO_DELIVERY_MODE_RELATION).append(" AS s2d");
		query.append("	ON {dm:").append(PickUpDeliveryModeModel.PK).append("}={s2d:").append(Link.TARGET).append('}');
		query.append(" } WHERE {s2d:").append(Link.SOURCE).append("}=?store");
		query.append(" AND {").append(PickUpDeliveryModeModel.ACTIVE).append("}=?active");
		query.append(" AND {").append(PickUpDeliveryModeModel.SUPPORTEDMODE).append("}=?mode");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("active", Boolean.TRUE);
		params.put("mode", abstractOrder.getStore().getPickupInStoreMode());
		params.put("store", abstractOrder.getStore());
		return doSearch(query.toString(), params, DeliveryModeModel.class);
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

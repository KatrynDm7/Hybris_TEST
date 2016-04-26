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
package de.hybris.platform.commerceservices.stock.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.stock.dao.CommerceWarehouseDao;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.store.BaseStoreModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultCommerceWarehouseDao extends AbstractItemDao implements CommerceWarehouseDao
{
	protected final String WAREHOUSE_FOR_BASESTORE_QUERY = "SELECT {w.pk} FROM {BaseStore2WarehouseRel as r JOIN Warehouse as w "
			+ "ON {r.target} = {w.pk}} WHERE {w.default} = ?default AND {r.source} = ?baseStore";

	@Override
	public List<WarehouseModel> getDefaultWarehousesForBaseStore(final BaseStoreModel baseStore)
	{
		validateParameterNotNull(baseStore, "baseStore cannot be null");
		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("baseStore", baseStore);
		params.put("default", Boolean.TRUE);
		return getFlexibleSearchService().<WarehouseModel> search(WAREHOUSE_FOR_BASESTORE_QUERY, params).getResult();
	}
}

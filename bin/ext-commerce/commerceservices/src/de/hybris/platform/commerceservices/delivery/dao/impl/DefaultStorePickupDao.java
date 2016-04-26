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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.commerceservices.delivery.dao.StorePickupDao;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultStorePickupDao extends AbstractItemDao implements StorePickupDao
{
	protected final String PICKUP_CHECK_QUERY = "SELECT 1 FROM {StockLevel as sl JOIN PointOfService as pos "
			+ "ON {pos.baseStore}=?baseStore JOIN PoS2WarehouseRel as p2w ON {p2w.source}={pos.pk} AND {p2w.target} = {sl.warehouse}} WHERE {sl.productCode} = ?productCode";

	protected final String IN_STOCK_DEFINITION = " AND ((({sl.available} + {sl.overselling} - {sl.reserved}) > 0) "
			+ "OR {sl.inStockStatus} = ?forceInStock) AND {sl.inStockStatus} <> ?forceOutOfStock";

	protected final String STOCKLEVELS_FOR_POINTS_OF_SERVICE_QUERY = "SELECT {p2w.source},{sl.pk} from {Stocklevel as sl JOIN PointOfService as pos "
			+ "ON {pos.baseStore}=?baseStore JOIN PoS2WarehouseRel as p2w ON {p2w.source}={pos.pk} AND {p2w.target} = {sl.warehouse}} WHERE {sl.productCode} = ?productCode";

	@Override
	public Boolean checkProductForPickup(final String productCode, final BaseStoreModel baseStoreModel)
	{
		ServicesUtil.validateParameterNotNull(productCode, "productCode cannot be null");
		ServicesUtil.validateParameterNotNull(baseStoreModel, "baseStoreModel cannot be null");

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(PICKUP_CHECK_QUERY + IN_STOCK_DEFINITION);
		fQuery.addQueryParameter("productCode", productCode);
		fQuery.addQueryParameter("baseStore", baseStoreModel);
		fQuery.addQueryParameter("forceInStock", InStockStatus.FORCEINSTOCK);
		fQuery.addQueryParameter("forceOutOfStock", InStockStatus.FORCEOUTOFSTOCK);
		fQuery.setNeedTotal(false);
		fQuery.setCount(1);
		fQuery.setResultClassList(Collections.singletonList(Integer.class));

		final int resultSize = getFlexibleSearchService().search(fQuery).getResult().size();
		return Boolean.valueOf(resultSize > 0);
	}

	@Override
	public Map<PointOfServiceModel, List<StockLevelModel>> getLocalStockLevelsForProductAndBaseStore(final String productCode,
			final BaseStoreModel baseStoreModel)
	{
		validateParameterNotNull(productCode, "productCode cannot be null");
		validateParameterNotNull(baseStoreModel, "baseStoreModel cannot be null");

		final Map<String, Object> params = new HashMap<String, Object>(2);

		params.put("productCode", productCode);
		params.put("baseStore", baseStoreModel);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(STOCKLEVELS_FOR_POINTS_OF_SERVICE_QUERY, params);
		fQuery.setResultClassList(Arrays.asList(PointOfServiceModel.class, StockLevelModel.class));
		final SearchResult<List> result = search(fQuery);

		final Map<PointOfServiceModel, List<StockLevelModel>> resultMap = new HashMap<PointOfServiceModel, List<StockLevelModel>>();

		for (final List row : result.getResult())
		{
			if (resultMap.containsKey(row.get(0)))
			{
				resultMap.get(row.get(0)).add((StockLevelModel) row.get(1));
			}
			else
			{
				final List<StockLevelModel> stockLevels = new ArrayList<StockLevelModel>();
				stockLevels.add((StockLevelModel) row.get(1));
				resultMap.put((PointOfServiceModel) row.get(0), stockLevels);
			}
		}
		return resultMap;
	}
}

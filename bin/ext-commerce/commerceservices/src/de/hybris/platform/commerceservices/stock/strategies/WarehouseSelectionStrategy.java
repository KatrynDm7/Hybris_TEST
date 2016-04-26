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
package de.hybris.platform.commerceservices.stock.strategies;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


/**
 * This strategy abstracts the relationship between BaseStore and Warehouse allowing the logic to be easily changed. The
 * Warehouses returned are the "Web" or "Online" warehouses which are typically used for home delivery.
 */
public interface WarehouseSelectionStrategy
{
	List<WarehouseModel> getWarehousesForBaseStore(BaseStoreModel baseStore);
}

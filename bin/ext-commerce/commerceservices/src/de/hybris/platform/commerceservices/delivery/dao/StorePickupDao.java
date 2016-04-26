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
package de.hybris.platform.commerceservices.delivery.dao;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;
import java.util.Map;



public interface StorePickupDao extends Dao
{
	/**
	 * Checks if a product can be picked up
	 * 
	 * @param productCode
	 * @param baseStoreModel
	 */
	Boolean checkProductForPickup(String productCode, BaseStoreModel baseStoreModel);

	/**
	 * Get stock levels for given given product and base store.
	 * 
	 * @param productCode
	 * @param baseStoreModel
	 * @return Map of {@link PointOfServiceModel} and {@link de.hybris.platform.basecommerce.enums.StockLevelStatus}
	 *         information
	 */
	Map<PointOfServiceModel, List<StockLevelModel>> getLocalStockLevelsForProductAndBaseStore(String productCode,
			BaseStoreModel baseStoreModel);
}

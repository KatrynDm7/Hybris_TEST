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
package de.hybris.platform.commerceservices.stock.dao;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;


public interface CommerceWarehouseDao extends Dao
{
	/**
	 * 
	 * @param baseStore
	 * @return List of WarehouseModels
	 */
	List<WarehouseModel> getDefaultWarehousesForBaseStore(BaseStoreModel baseStore);
}

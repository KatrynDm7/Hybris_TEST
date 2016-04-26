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
package de.hybris.platform.commerceservices.stock.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.stock.dao.CommerceWarehouseDao;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultWarehouseSelectionStrategy implements WarehouseSelectionStrategy
{
	private CommerceWarehouseDao commerceWarehouseDao;

	protected CommerceWarehouseDao getCommerceWarehouseDao()
	{
		return commerceWarehouseDao;
	}

	@Required
	public void setCommerceWarehouseDao(final CommerceWarehouseDao commerceWarehouseDao)
	{
		this.commerceWarehouseDao = commerceWarehouseDao;
	}

	@Override
	public List<WarehouseModel> getWarehousesForBaseStore(final BaseStoreModel baseStore)
	{
		validateParameterNotNull(baseStore, "baseStore must not be null");

		return getCommerceWarehouseDao().getDefaultWarehousesForBaseStore(baseStore);
	}
}

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
package de.hybris.platform.commerceservices.strategies.impl;

import de.hybris.platform.commerceservices.delivery.dao.StorePickupDao;
import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.commerceservices.strategies.PickupAvailabilityStrategy;
import de.hybris.platform.commerceservices.strategies.PickupStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;
import de.hybris.platform.store.BaseStoreModel;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


public class DefaultPickupAvailabilityStrategy implements PickupAvailabilityStrategy
{
	private PickupStrategy pickupStrategy;
	private StockLevelProductStrategy stockLevelProductStrategy;
	private StorePickupDao storePickupDao;

	protected PickupStrategy getPickupStrategy()
	{
		return pickupStrategy;
	}

	@Required
	public void setPickupStrategy(final PickupStrategy pickupStrategy)
	{
		this.pickupStrategy = pickupStrategy;
	}

	protected StockLevelProductStrategy getStockLevelProductStrategy()
	{
		return stockLevelProductStrategy;
	}

	@Required
	public void setStockLevelProductStrategy(final StockLevelProductStrategy stockLevelProductStrategy)
	{
		this.stockLevelProductStrategy = stockLevelProductStrategy;
	}

	protected StorePickupDao getStorePickupDao()
	{
		return storePickupDao;
	}

	@Required
	public void setStorePickupDao(final StorePickupDao storePickupDao)
	{
		this.storePickupDao = storePickupDao;
	}

	@Override
	public Boolean isPickupAvailableForProduct(final ProductModel product, final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product must not be null");
		if (!PickupInStoreMode.DISABLED.equals(getPickupStrategy().getPickupInStoreMode()))
		{
			validateParameterNotNull(baseStore, "baseStore must not be null");
			if (CollectionUtils.isNotEmpty(baseStore.getPointsOfService()))
			{
				return getStorePickupDao().checkProductForPickup(getStockLevelProductStrategy().convert(product),
						baseStore);
			}
		}
		return Boolean.FALSE;
	}

}

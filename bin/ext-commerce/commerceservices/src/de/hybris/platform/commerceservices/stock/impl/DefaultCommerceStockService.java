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
package de.hybris.platform.commerceservices.stock.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.delivery.dao.StorePickupDao;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


public class DefaultCommerceStockService implements CommerceStockService
{
	private StockService stockService;
	private CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;
	private WarehouseSelectionStrategy warehouseSelectionStrategy;
	private StorePickupDao storePickupDao;
	private StockLevelStatusStrategy stockLevelStatusStrategy;
	private StockLevelProductStrategy stockLevelProductStrategy;

	@Override
	public boolean isStockSystemEnabled(final BaseStoreModel baseStore)
	{
		return baseStore != null && baseStore.getWarehouses() != null && !baseStore.getWarehouses().isEmpty();
	}

	@Override
	public StockLevelStatus getStockLevelStatusForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(baseStore, "baseStore cannot be null");

		return getStockService().getProductStatus(product, getWarehouseSelectionStrategy().getWarehousesForBaseStore(baseStore));
	}

	@Override
	public Long getStockLevelForProductAndBaseStore(final ProductModel product, final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(baseStore, "baseStore cannot be null");

		return getCommerceStockLevelCalculationStrategy().calculateAvailability(
				getStockService().getStockLevels(product, getWarehouseSelectionStrategy().getWarehousesForBaseStore(baseStore)));
	}

	@Override
	public StockLevelStatus getStockLevelStatusForProductAndPointOfService(final ProductModel product,
			final PointOfServiceModel pointOfService)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(pointOfService, "pointOfService cannot be null");

		if (pointOfService.getWarehouses().isEmpty())
		{
			return StockLevelStatus.OUTOFSTOCK;
		}

		return getStockService().getProductStatus(product, pointOfService.getWarehouses());
	}

	@Override
	public Long getStockLevelForProductAndPointOfService(final ProductModel product, final PointOfServiceModel pointOfService)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(pointOfService, "pointOfService cannot be null");

		return getCommerceStockLevelCalculationStrategy().calculateAvailability(
				getStockService().getStockLevels(product, pointOfService.getWarehouses()));
	}

	@Override
	public Map<PointOfServiceModel, StockLevelStatus> getPosAndStockLevelStatusForProduct(final ProductModel product,
			final BaseStoreModel baseStore)
	{
		validateParameterNotNull(product, "product cannot be null");
		validateParameterNotNull(baseStore, "baseStore cannot be empty");

		final Map<PointOfServiceModel, List<StockLevelModel>> posStockLevelModels = getStorePickupDao()
				.getLocalStockLevelsForProductAndBaseStore(getStockLevelProductStrategy().convert(product), baseStore);
		final Map<PointOfServiceModel, StockLevelStatus> posStockLevelStatus = new HashMap<PointOfServiceModel, StockLevelStatus>(
				posStockLevelModels.size());

		for (final Map.Entry<PointOfServiceModel, List<StockLevelModel>> entry : posStockLevelModels.entrySet())
		{
			final PointOfServiceModel pointOfServiceModel = entry.getKey();
			posStockLevelStatus.put(pointOfServiceModel, getStockLevelStatusStrategy().checkStatus(entry.getValue()));
		}

		return posStockLevelStatus;
	}

	protected StockService getStockService()
	{
		return stockService;
	}

	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

	protected CommerceAvailabilityCalculationStrategy getCommerceStockLevelCalculationStrategy()
	{
		return commerceStockLevelCalculationStrategy;
	}

	@Required
	public void setCommerceStockLevelCalculationStrategy(
			final CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy)
	{
		this.commerceStockLevelCalculationStrategy = commerceStockLevelCalculationStrategy;
	}

	protected WarehouseSelectionStrategy getWarehouseSelectionStrategy()
	{
		return warehouseSelectionStrategy;
	}

	@Required
	public void setWarehouseSelectionStrategy(final WarehouseSelectionStrategy warehouseSelectionStrategy)
	{
		this.warehouseSelectionStrategy = warehouseSelectionStrategy;
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


	protected StockLevelStatusStrategy getStockLevelStatusStrategy()
	{
		return stockLevelStatusStrategy;
	}

	@Required
	public void setStockLevelStatusStrategy(final StockLevelStatusStrategy stockLevelStatusStrategy)
	{
		this.stockLevelStatusStrategy = stockLevelStatusStrategy;
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
}

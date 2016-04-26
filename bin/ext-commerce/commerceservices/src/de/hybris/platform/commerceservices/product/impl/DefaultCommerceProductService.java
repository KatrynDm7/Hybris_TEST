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
package de.hybris.platform.commerceservices.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CommerceProductService}
 */
public class DefaultCommerceProductService implements CommerceProductService
{
	private StockService stockService;
	private WarehouseService warehouseService;

	protected StockService getStockService()
	{
		return stockService;
	}

	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	@Required
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}


	@Override
	public Collection<CategoryModel> getSuperCategoriesExceptClassificationClassesForProduct(final ProductModel productModel)
			throws IllegalArgumentException
	{
		validateParameterNotNull(productModel, "Product model cannot be null");

		final String catalogId = productModel.getCatalogVersion().getCatalog().getId();
		final Collection<CategoryModel> resultList = new ArrayList<CategoryModel>();

		for (final CategoryModel categoryModel : productModel.getSupercategories())
		{
			if (toBeConverted(categoryModel, catalogId))
			{
				resultList.add(categoryModel);
			}
		}
		return resultList;
	}


	@Override
	@Deprecated
	public Integer getStockLevelForProduct(final ProductModel productModel)
	{
		final List<WarehouseModel> defaultWarehouses = getWarehouseService().getDefWarehouse();
		if (Collections.isEmpty(defaultWarehouses))
		{
			return null;
		}

		final Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(productModel, defaultWarehouses);

		int stockLevel = 0;
		for (final StockLevelModel stockLevelModel : stockLevels)
		{
			stockLevel += stockLevelModel.getAvailable();
		}

		return Integer.valueOf(stockLevel);
	}

	protected boolean toBeConverted(final CategoryModel categoryModel, final String catalogId)
	{
		return categoryModel.getCatalogVersion().getCatalog().getId().equals(catalogId)
				&& !(categoryModel instanceof ClassificationClassModel);
	}

}

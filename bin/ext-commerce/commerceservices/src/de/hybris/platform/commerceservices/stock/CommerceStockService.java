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
package de.hybris.platform.commerceservices.stock;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Map;


/**
 * Service that collects functionality for stock levels related with point of service (warehouse)
 */
public interface CommerceStockService
{
	/**
	 * Indicates if stock system is enabled for given base store
	 * 
	 * @param baseStore
	 *           to be checked
	 * @return true if stock system is enabled
	 */
	boolean isStockSystemEnabled(BaseStoreModel baseStore);

	/**
	 * Returns stock level status for combination of given product and base store
	 * 
	 * @param product
	 * @param baseStore
	 * @return {@link StockLevelStatus} information
	 */
	StockLevelStatus getStockLevelStatusForProductAndBaseStore(ProductModel product, BaseStoreModel baseStore);

	/**
	 * Returns stock level value for given product and base store
	 * 
	 * @param product
	 * @param baseStore
	 * @return actual stock level
	 */
	Long getStockLevelForProductAndBaseStore(ProductModel product, BaseStoreModel baseStore);

	/**
	 * Returns stock level status for given product and point of service (that also indicates warehouse)
	 * 
	 * @param product
	 * @param pointOfServiceModel
	 * @return {@link StockLevelStatus} information
	 */
	StockLevelStatus getStockLevelStatusForProductAndPointOfService(ProductModel product, PointOfServiceModel pointOfServiceModel);

	/**
	 * Returns stock level value for given product and point of service (that also indicates warehouse)
	 * 
	 * @param product
	 * @param pointOfServiceModel
	 * @return actual stock level
	 */
	Long getStockLevelForProductAndPointOfService(ProductModel product, PointOfServiceModel pointOfServiceModel);

	/**
	 * Returns the StockLevelStatus for the Product at Points of Service where it is in stock.
	 * 
	 * @param product
	 * @param baseStore
	 * @return Map of {@link PointOfServiceModel} and {@link StockLevelStatus} information
	 */
	Map<PointOfServiceModel, StockLevelStatus> getPosAndStockLevelStatusForProduct(ProductModel product, BaseStoreModel baseStore);
}

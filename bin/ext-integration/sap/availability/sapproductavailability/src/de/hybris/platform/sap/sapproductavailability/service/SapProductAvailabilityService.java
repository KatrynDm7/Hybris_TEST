/**
 * 
 */
package de.hybris.platform.sap.sapproductavailability.service;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;
import java.util.Map;



/**
 * @author Administrator
 * 
 */
public interface SapProductAvailabilityService extends CommerceStockService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.stock.CommerceStockService#getStockLevelForProductAndBaseStore(de.hybris.platform
	 * .core.model.product.ProductModel, de.hybris.platform.store.BaseStoreModel)
	 */
	@Override
	public abstract Long getStockLevelForProductAndBaseStore(ProductModel paramProductModel, BaseStoreModel paramBaseStoreModel);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.stock.CommerceStockService#getStockLevelStatusForProductAndBaseStore(de.hybris
	 * .platform.core.model.product.ProductModel, de.hybris.platform.store.BaseStoreModel)
	 */
	@Override
	public abstract StockLevelStatus getStockLevelStatusForProductAndBaseStore(ProductModel paramProductModel,
			BaseStoreModel paramBaseStoreModel);

	/**
	 * @return true if the synchronous ATP check is active
	 */
	public boolean isSynchronousATPCheckActive();

	/**
	 * Read the product future availability from ERP
	 * 
	 * @param productModel
	 * @return product future availability
	 */
	public Map<String, Map<Date, Integer>> readProductFutureAvailability(ProductModel productModel);
}

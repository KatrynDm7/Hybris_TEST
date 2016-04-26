package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.impl.StockLevelDao;
import de.hybris.platform.warehousing.util.builder.StockLevelModelBuilder;


public class StockLevels extends AbstractItems<StockLevelModel>
{
	private StockLevelDao stockLevelDao;
	private Warehouses warehouses;
	private Products products;

	public StockLevelModel Camera(final WarehouseModel warehouse, final int quantity)
	{
		return getOrSaveAndReturn(() -> getStockLevelDao().findStockLevel(Products.CODE_CAMERA, warehouse), //
				() -> StockLevelModelBuilder.aModel() //
						.withAvailable(quantity) //
						.withMaxPreOrder(0) //
						.withPreOrder(0) //
						.withMaxStockLevelHistoryCount(-1) //
						.withReserved(0) //
						.withWarehouse(warehouse) //
						.withProduct(getProducts().Camera()) //
						.withInStockStatus(InStockStatus.NOTSPECIFIED) //
						.build());
	}
	
	public StockLevelModel MemoryCard(final WarehouseModel warehouse, final int quantity)
	{
		return getOrSaveAndReturn(() -> getStockLevelDao().findStockLevel(Products.CODE_MEMORY_CARD, warehouse), //
				() -> StockLevelModelBuilder.aModel() //
						.withAvailable(quantity) //
						.withMaxPreOrder(0) //
						.withPreOrder(0) //
						.withMaxStockLevelHistoryCount(-1) //
						.withReserved(0) //
						.withWarehouse(warehouse) //
						.withProduct(getProducts().MemoryCard()) //
						.withInStockStatus(InStockStatus.NOTSPECIFIED) //
						.build());
	}

	public StockLevelDao getStockLevelDao()
	{
		return stockLevelDao;
	}

	public void setStockLevelDao(final StockLevelDao stockLevelDao)
	{
		this.stockLevelDao = stockLevelDao;
	}

	public Warehouses getWarehouses()
	{
		return warehouses;
	}

	public void setWarehouses(final Warehouses warehouses)
	{
		this.warehouses = warehouses;
	}

	public Products getProducts()
	{
		return products;
	}

	public void setProducts(final Products products)
	{
		this.products = products;
	}

}

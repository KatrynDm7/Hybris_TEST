package de.hybris.platform.sap.sapproductavailability.service.impl;

import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DefaultSapProductAvailabilityServiceTest
{

	private DefaultSapProductAvailabilityService service;

	@Mock
	private ProductModel product;

	@Mock
	private BaseStoreModel baseStore;

	@Mock
	private StockService stockService;

	@Mock
	private WarehouseSelectionStrategy warehouseSelectionStrategy;

	@Mock
	private ModuleConfigurationAccess moduleConfigurationAccess;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		service = new DefaultSapProductAvailabilityService();
		service.setStockService(stockService);
		service.setWarehouseSelectionStrategy(warehouseSelectionStrategy);
		service.setModuleConfigurationAccess(moduleConfigurationAccess);
	}

	@Test
	public void testStockConversion()
	{
		final List<WarehouseModel> warehouses = new ArrayList<>(0);
		Mockito.when(product.getSapBaseUnitConversion()).thenReturn(new Double("0.1"));
		Mockito.when(moduleConfigurationAccess.getProperty("sapproductavailability_atpActive")).thenReturn(Boolean.FALSE);
		Mockito.when(warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore)).thenReturn(warehouses);
		Mockito.when(Integer.valueOf(stockService.getTotalStockLevelAmount(product, warehouses))).thenReturn(Integer.valueOf(60));
		final Long amount = service.getStockLevelForProductAndBaseStore(product, baseStore);

		Assert.assertEquals(600, amount.longValue());
	}

	@Test
	public void testStockConversionRounding()
	{
		final List<WarehouseModel> warehouses = new ArrayList<>(0);
		Mockito.when(product.getSapBaseUnitConversion()).thenReturn(new Double("0.31"));
		Mockito.when(moduleConfigurationAccess.getProperty("sapproductavailability_atpActive")).thenReturn(Boolean.FALSE);
		Mockito.when(warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore)).thenReturn(warehouses);
		Mockito.when(Integer.valueOf(stockService.getTotalStockLevelAmount(product, warehouses))).thenReturn(Integer.valueOf(60));
		final Long amount = service.getStockLevelForProductAndBaseStore(product, baseStore);

		Assert.assertEquals(193, amount.longValue());
	}

	@Test
	public void testStockConversionOff()
	{
		final List<WarehouseModel> warehouses = new ArrayList<>(0);
		Mockito.when(product.getSapBaseUnitConversion()).thenReturn(null);
		Mockito.when(moduleConfigurationAccess.getProperty("sapproductavailability_atpActive")).thenReturn(Boolean.FALSE);
		Mockito.when(warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore)).thenReturn(warehouses);
		Mockito.when(Integer.valueOf(stockService.getTotalStockLevelAmount(product, warehouses))).thenReturn(Integer.valueOf(60));
		final Long amount = service.getStockLevelForProductAndBaseStore(product, baseStore);

		Assert.assertEquals(60, amount.longValue());
	}

	@Test
	public void testStockConversionZero()
	{
		final List<WarehouseModel> warehouses = new ArrayList<>(0);
		Mockito.when(product.getSapBaseUnitConversion()).thenReturn(new Double(0));
		Mockito.when(moduleConfigurationAccess.getProperty("sapproductavailability_atpActive")).thenReturn(Boolean.FALSE);
		Mockito.when(warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore)).thenReturn(warehouses);
		Mockito.when(Integer.valueOf(stockService.getTotalStockLevelAmount(product, warehouses))).thenReturn(Integer.valueOf(60));
		final Long amount = service.getStockLevelForProductAndBaseStore(product, baseStore);

		Assert.assertEquals(60, amount.longValue());
	}

}

package de.hybris.platform.warehousing.atp.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultWarehouseStockServiceTest
{
	private static final Long ZERO = Long.valueOf(0);
	private static final Long TEN = Long.valueOf(10);

	@InjectMocks
	private final DefaultWarehouseStockService warehouseStockService = new DefaultWarehouseStockService();

	@Mock
	private StockService stockService;
	@Mock
	private CommerceAvailabilityCalculationStrategy commerceAvailabilityCalculationStrategy;

	@Mock
	private StockLevelModel stockLevel;

	private ProductModel product;
	private WarehouseModel warehouse;

	@Before
	public void setUp()
	{
		product = new ProductModel();
		warehouse = new WarehouseModel();

		when(stockService.getStockLevel(product, warehouse)).thenReturn(stockLevel);
		when(commerceAvailabilityCalculationStrategy.calculateAvailability(Mockito.anyCollection())).thenReturn(TEN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_ProductNull()
	{
		product = null;
		warehouseStockService.getStockLevelForProductAndWarehouse(product, warehouse);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_WarehouseNull()
	{
		warehouse = null;
		warehouseStockService.getStockLevelForProductAndWarehouse(product, warehouse);
	}

	@Test
	public void shouldNotBeAvailable_NoStockLevelFound()
	{
		when(stockService.getStockLevel(product, warehouse)).thenReturn(null);
		final Long availability = warehouseStockService.getStockLevelForProductAndWarehouse(product, warehouse);
		assertEquals(ZERO, availability);
	}

	@Test
	public void shouldNotBeAvailable_FailToCalculate()
	{
		when(commerceAvailabilityCalculationStrategy.calculateAvailability(Mockito.anyCollection())).thenReturn(null);
		final Long availability = warehouseStockService.getStockLevelForProductAndWarehouse(product, warehouse);
		assertEquals(ZERO, availability);
	}

	@Test
	public void shouldGetAvailability()
	{
		final Long availability = warehouseStockService.getStockLevelForProductAndWarehouse(product, warehouse);
		assertEquals(TEN, availability);
	}
}

package de.hybris.platform.warehousing.sourcing.context.populator.impl;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.atp.services.WarehouseStockService;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
public class AvailabilitySourcingLocationPopulatorTest
{
	private static final ProductModel PRODUCT_1 = new ProductModel();
	private static final ProductModel PRODUCT_2 = new ProductModel();
	private static final Long QUANTITY_1 = 4L;
	private static final Long QUANTITY_2 = 9L;

	@InjectMocks
	private final AvailabilitySourcingLocationPopulator populator = new AvailabilitySourcingLocationPopulator();
	private WarehouseModel warehouse;
	private SourcingLocation location;

	@Mock
	private WarehouseStockService warehouseStockService;

	@Before
	public void setUp()
	{
		location = new SourcingLocation();
		final SourcingContext context = new SourcingContext();
		final OrderModel order = new OrderModel();

		final OrderEntryModel orderEntry1 = new OrderEntryModel();
		orderEntry1.setProduct(PRODUCT_1);
		final OrderEntryModel orderEntry2 = new OrderEntryModel();
		orderEntry2.setProduct(PRODUCT_2);
		order.setEntries(Lists.newArrayList(orderEntry1, orderEntry2));

		context.setOrderEntries(Lists.newArrayList(orderEntry1, orderEntry2));
		location.setContext(context);

		warehouse = new WarehouseModel();

		Mockito.when(warehouseStockService.getStockLevelForProductAndWarehouse(PRODUCT_1, warehouse)).thenReturn(QUANTITY_1);
		Mockito.when(warehouseStockService.getStockLevelForProductAndWarehouse(PRODUCT_2, warehouse)).thenReturn(QUANTITY_2);
	}

	@Test
	public void shouldPopulateAvailability()
	{
		populator.populate(warehouse, location);
		Assert.assertEquals(QUANTITY_1, location.getAvailability().get(PRODUCT_1));
		Assert.assertEquals(QUANTITY_2, location.getAvailability().get(PRODUCT_2));
	}

	@Test
	public void shouldDefaultAvailabilityToZeroWhenNull()
	{
		Mockito.when(warehouseStockService.getStockLevelForProductAndWarehouse(PRODUCT_1, warehouse)).thenReturn(null);

		populator.populate(warehouse, location);
		Assert.assertEquals(Long.valueOf(0L), location.getAvailability().get(PRODUCT_1));
		Assert.assertEquals(QUANTITY_2, location.getAvailability().get(PRODUCT_2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailPopulate_NullSource()
	{
		warehouse = null;
		populator.populate(warehouse, location);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailPopulate_NullTarget()
	{
		location = null;
		populator.populate(warehouse, location);
		Assert.fail();
	}
}

package de.hybris.platform.warehousing.atp.strategy.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@UnitTest
public class DefaultPickupWarehouseSelectionStrategyTest
{
	private final DefaultPickupWarehouseSelectionStrategy pickupWarehouseSelectionStrategy = new DefaultPickupWarehouseSelectionStrategy();

	private PointOfServiceModel pos;
	private WarehouseModel warehouseNoPickup;
	private WarehouseModel warehouseForPickup;
	private DeliveryModeModel internationalShipping;
	private DeliveryModeModel localShipping;
	private DeliveryModeModel pickup;

	@Before
	public void setUp()
	{
		pos = new PointOfServiceModel();
		warehouseNoPickup = new WarehouseModel();
		warehouseForPickup = new WarehouseModel();
		pos.setWarehouses(Arrays.asList(warehouseNoPickup));

		internationalShipping = new DeliveryModeModel();
		localShipping = new DeliveryModeModel();
		pickup = new PickUpDeliveryModeModel();

		warehouseForPickup.setDeliveryModes(Sets.newHashSet(localShipping, pickup));
		warehouseNoPickup.setDeliveryModes(Sets.newHashSet(internationalShipping, localShipping));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullPos()
	{
		pickupWarehouseSelectionStrategy.getWarehouses(null);
	}

	@Test
	public void shouldGetWarehouses_nullWarehouses()
	{
		pos.setWarehouses(null);
		final Collection<WarehouseModel> warehouses = pickupWarehouseSelectionStrategy.getWarehouses(pos);
		assertTrue(warehouses.isEmpty());
	}

	@Test
	public void shouldGetWarehouses_nullDeliveryModes()
	{
		warehouseNoPickup.setDeliveryModes(null);
		final Collection<WarehouseModel> warehouses = pickupWarehouseSelectionStrategy.getWarehouses(pos);
		assertTrue(warehouses.isEmpty());
	}

	@Test
	public void shouldGetWarehouses_noPickup()
	{
		final Collection<WarehouseModel> warehouses = pickupWarehouseSelectionStrategy.getWarehouses(pos);
		assertTrue(warehouses.isEmpty());
	}

	@Test
	public void shouldGetWarehousesValid()
	{
		pos.setWarehouses(Arrays.asList(warehouseForPickup, warehouseNoPickup));
		final Collection<WarehouseModel> warehouses = pickupWarehouseSelectionStrategy.getWarehouses(pos);
		assertFalse(warehouses.isEmpty());
		assertTrue(warehouses.contains(warehouseForPickup));
	}
}

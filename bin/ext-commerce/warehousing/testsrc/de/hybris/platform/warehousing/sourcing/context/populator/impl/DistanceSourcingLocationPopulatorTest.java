package de.hybris.platform.warehousing.sourcing.context.populator.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DistanceSourcingLocationPopulatorTest
{
	private static Double CALCULATED_DISTANCE = 0.549310944307586;
	private static double LAT1 = 38.898556;
	private static double LON1 = -77.037852;
	private static double LAT2 = 38.897147;
	private static double LON2 = -77.043934;

	@InjectMocks
	private final DistanceSourcingLocationPopulator populator = new DistanceSourcingLocationPopulator();

	@Mock
	private PosSelectionStrategy posSelectionStrategy;

	private OrderModel order;
	private WarehouseModel warehouse;
	private PointOfServiceModel pos;
	private SourcingLocation location;
	private AddressModel address;
	private AddressModel posAddress;

	@Before
	public void setUp()
	{
		location = new SourcingLocation();
		final SourcingContext context = new SourcingContext();

		address = new AddressModel();
		address.setLatitude(LAT1);
		address.setLongitude(LON1);

		posAddress = new AddressModel();
		posAddress.setLatitude(LAT2);
		posAddress.setLongitude(LON2);

		order = new OrderModel();
		order.setDeliveryAddress(address);

		final OrderEntryModel orderEntry = new OrderEntryModel();
		orderEntry.setOrder(order);

		context.setOrderEntries(Lists.newArrayList(orderEntry));
		location.setContext(context);

		pos = new PointOfServiceModel();
		pos.setAddress(posAddress);

		warehouse = new WarehouseModel();
		warehouse.setPointsOfService(Collections.singleton(pos));

		when(posSelectionStrategy.getPointOfService(order, warehouse)).thenReturn(pos);
	}

	@Test
	public void shouldSetDistanceToNull_OrderNoAddress()
	{
		location.getContext().getOrderEntries().iterator().next().getOrder().setDeliveryAddress(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getDistance());
	}

	@Test
	public void shouldSetDistanceToNull_OrderNoLat()
	{
		location.getContext().getOrderEntries().iterator().next().getOrder().getDeliveryAddress().setLatitude(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getDistance());
	}

	@Test
	public void shouldSetDistanceToNull_OrderNoLon()
	{
		location.getContext().getOrderEntries().iterator().next().getOrder().getDeliveryAddress().setLongitude(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getDistance());
	}

	@Test
	public void shouldSetDistanceToNull_PosNoAddress()
	{
		pos.setAddress(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getDistance());
	}

	@Test
	public void shouldSetDistanceToNull_PosAddressNoLat()
	{
		pos.getAddress().setLatitude(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getDistance());
	}

	@Test
	public void shouldSetDistanceToNull_PosAddressNoLon()
	{
		pos.getAddress().setLongitude(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getDistance());
	}

	@Test
	public void shouldSetDistanceToNull_NoPosForWarehouse()
	{
		when(posSelectionStrategy.getPointOfService(order, warehouse)).thenReturn(null);
		populator.populate(warehouse, location);
		Assert.assertNull(location.getDistance());
	}

	@Test
	public void shouldCalculateDistance()
	{
		populator.populate(warehouse, location);
		Assert.assertEquals(CALCULATED_DISTANCE, location.getDistance());
	}
}

/**
 *
 */
package de.hybris.platform.warehousing.sourcing.filter.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PickupSourcingLocationFilterTest
{
	@Mock
	private PointOfServiceModel posA;
	@Mock
	private PointOfServiceModel posB;
	@Mock
	private WarehouseModel warehouseA;
	@Mock
	private WarehouseModel warehouseB;
	@Mock
	private WarehouseModel warehouseC;

	private final PickupSourcingLocationFilter filter = new PickupSourcingLocationFilter();

	@Mock
	private AbstractOrderModel order;
	@Mock
	private AbstractOrderEntryModel orderEntry1;
	@Mock
	private AbstractOrderEntryModel orderEntry2;
	@Mock
	private AbstractOrderEntryModel orderEntry3;

	@Before
	public void setUp() throws Exception
	{
		when(posA.getWarehouses()).thenReturn(Lists.newArrayList(warehouseA, warehouseB));
		when(posB.getWarehouses()).thenReturn(Lists.newArrayList(warehouseC));

		when(order.getEntries()).thenReturn(Lists.newArrayList(orderEntry1, orderEntry2, orderEntry3));
		when(orderEntry1.getDeliveryPointOfService()).thenReturn(posA);
		when(orderEntry2.getDeliveryPointOfService()).thenReturn(posB);
		when(orderEntry3.getDeliveryPointOfService()).thenReturn(null);
	}

	@Test
	public void shouldFindPickupLocations_PartialPickup()
	{
		final Collection<WarehouseModel> locations = filter.applyFilter(order, null);
		assertEquals(3, locations.size());
		assertTrue(locations.contains(warehouseA));
		assertTrue(locations.contains(warehouseB));
		assertTrue(locations.contains(warehouseC));
	}

	@Test
	public void shouldFindPickupLocations_AllPickup()
	{
		Mockito.when(order.getEntries()).thenReturn(Lists.newArrayList(orderEntry1, orderEntry2));
		final Collection<WarehouseModel> locations = filter.applyFilter(order, null);
		assertEquals(3, locations.size());
		assertTrue(locations.contains(warehouseA));
		assertTrue(locations.contains(warehouseB));
		assertTrue(locations.contains(warehouseC));
	}

	@Test
	public void shouldFindPickupLocations_SingleOrderEntry()
	{
		Mockito.when(order.getEntries()).thenReturn(Lists.newArrayList(orderEntry2));
		final Collection<WarehouseModel> locations = filter.applyFilter(order, null);
		assertEquals(1, locations.size());
		assertTrue(locations.contains(warehouseC));
	}

	@Test
	public void shouldFindPickupLocations_NoPickup()
	{
		Mockito.when(order.getEntries()).thenReturn(Lists.newArrayList(orderEntry3));
		final Collection<WarehouseModel> locations = filter.applyFilter(order, null);
		assertEquals(0, locations.size());
	}


}

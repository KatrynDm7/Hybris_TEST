/**
 *
 */
package de.hybris.platform.warehousing.sourcing.filter.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterProcessor;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterResultOperator;
import de.hybris.platform.warehousing.warehouse.service.WarehousingWarehouseService;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@UnitTest
public class DeliveryCountrySourcingLocationFilterTest
{
	@Mock
	private WarehouseModel warehouseA;
	@Mock
	private WarehouseModel warehouseB;
	@Mock
	private WarehouseModel warehouseC;
	@Mock
	private CountryModel countryCA;
	@Mock
	private BaseStoreModel baseStore;

	private DeliveryCountrySourcingLocationFilter filterA;
	private DeliveryCountrySourcingLocationFilter filterB;
	private SourcingFilterProcessor processor;

	@Mock
	private WarehousingWarehouseService warehousingService;
	@Mock
	private AbstractOrderModel order;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		filterA = new DeliveryCountrySourcingLocationFilter();
		filterB = new DeliveryCountrySourcingLocationFilter();
		filterA.setWarehousingWarehouseService(warehousingService);
		filterB.setWarehousingWarehouseService(warehousingService);

		when(warehousingService.getWarehousesByBaseStoreDeliveryCountry(baseStore, countryCA))
				.thenReturn(Lists.newArrayList(warehouseA, warehouseB));

		final AddressModel address = Mockito.mock(AddressModel.class);
		when(order.getDeliveryAddress()).thenReturn(address);
		when(order.getStore()).thenReturn(baseStore);
		when(address.getCountry()).thenReturn(countryCA).thenReturn(countryCA);

		processor = new SourcingFilterProcessor();
	}

	@Test
	public void testApplyFilter()
	{
		final Set<WarehouseModel> locations = Sets.newHashSet();

		filterA.setFilterResultOperator(SourcingFilterResultOperator.NONE);
		filterA.filterLocations(order, locations);

		assertEquals(2, locations.size());
		assertTrue(locations.contains(warehouseA));
		assertTrue(locations.contains(warehouseB));

		verify(warehousingService).getWarehousesByBaseStoreDeliveryCountry(baseStore, countryCA);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyFilterWithNullOrder()
	{
		filterA.setFilterResultOperator(SourcingFilterResultOperator.NONE);
		processor.setFilters(Lists.newArrayList(filterA));
		processor.filterLocations(null, Collections.<WarehouseModel> emptySet());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyFilterWithNullLocation()
	{
		filterA.setFilterResultOperator(SourcingFilterResultOperator.NONE);
		processor.setFilters(Lists.newArrayList(filterA));
		processor.filterLocations(order, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyFilterWithNullFilterResultOperator() throws Exception
	{
		processor.setFilters(Lists.newArrayList(filterA));
		processor.afterPropertiesSet();
		processor.filterLocations(order, Collections.<WarehouseModel> emptySet());
	}

	@Test
	public void testCombineFilteredLocationsAND()
	{
		final Set<WarehouseModel> locations = Sets.newHashSet(warehouseA, warehouseB);
		final Collection<WarehouseModel> filteredResults = Lists.newArrayList(warehouseB, warehouseC);

		filterA.setFilterResultOperator(SourcingFilterResultOperator.AND);
		filterA.combineFilteredLocations(filteredResults, locations);

		assertNotNull(locations);
		assertFalse(locations.isEmpty());
		assertEquals(1, locations.size());
		assertEquals(warehouseB, locations.iterator().next());
	}

	@Test
	public void testCombineFilteredLocationsOR()
	{
		final Set<WarehouseModel> locations = Sets.newHashSet(warehouseA, warehouseB);
		final Collection<WarehouseModel> filteredResults = Lists.newArrayList(warehouseB, warehouseC);

		filterA.setFilterResultOperator(SourcingFilterResultOperator.OR);
		filterA.combineFilteredLocations(filteredResults, locations);

		assertNotNull(locations);
		assertFalse(locations.isEmpty());
		assertEquals(3, locations.size());
		assertTrue(locations.contains(warehouseA));
		assertTrue(locations.contains(warehouseB));
		assertTrue(locations.contains(warehouseC));
	}

	@Test
	public void testCombineFilteredLocationsAND_OR() throws Exception
	{
		final Set<WarehouseModel> locations = Sets.newHashSet(warehouseA, warehouseB);
		final Collection<WarehouseModel> filteredResults = Lists.newArrayList(warehouseB, warehouseC);

		filterA.setFilterResultOperator(SourcingFilterResultOperator.OR);
		filterA.combineFilteredLocations(filteredResults, locations);

		assertFalse(locations.isEmpty());
		assertEquals(3, locations.size());
		assertTrue(locations.contains(warehouseA));
		assertTrue(locations.contains(warehouseB));
		assertTrue(locations.contains(warehouseC));

		filterB.setFilterResultOperator(SourcingFilterResultOperator.AND);
		filterB.combineFilteredLocations(Lists.newArrayList(warehouseB), locations);

		assertEquals(1, locations.size());
		assertTrue(locations.contains(warehouseB));
	}

	@Test
	public void testApplyFilterWithNullAddress()
	{
		final Set<WarehouseModel> locations = Sets.newHashSet(warehouseA, warehouseB);
		filterA.setFilterResultOperator(SourcingFilterResultOperator.NONE);
		filterA.filterLocations(new AbstractOrderModel(), locations);

		assertEquals(2, locations.size());
		assertTrue(locations.contains(warehouseA));
		assertTrue(locations.contains(warehouseB));
	}
}

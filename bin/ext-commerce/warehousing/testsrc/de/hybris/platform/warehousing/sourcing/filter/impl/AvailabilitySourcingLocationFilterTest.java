/**
 *
 */
package de.hybris.platform.warehousing.sourcing.filter.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.atp.services.WarehouseStockService;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterProcessor;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterResultOperator;
import de.hybris.platform.warehousing.warehouse.service.WarehousingWarehouseService;

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
public class AvailabilitySourcingLocationFilterTest
{
	private static final Long TEN = Long.valueOf(10);
	private static final Long ZERO = Long.valueOf(0);

	private static final String COUNTRY_CA = "CA";

	@Mock
	private WarehouseModel warehouseA;
	@Mock
	private WarehouseModel warehouseB;
	@Mock
	private WarehouseModel warehouseC;

	private AvailabilitySourcingLocationFilter filterA;
	private SourcingFilterProcessor processor;

	@Mock
	private WarehouseStockService warehouseStockService;
	@Mock
	private WarehousingWarehouseService warehouseService;
	@Mock
	private AbstractOrderModel order;
	@Mock
	private AbstractOrderEntryModel orderEntry1;
	@Mock
	private AbstractOrderEntryModel orderEntry2;
	@Mock
	private ProductModel product1;
	@Mock
	private ProductModel product2;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		filterA = new AvailabilitySourcingLocationFilter();
		filterA.setWarehouseStockService(warehouseStockService);

		// Availability results
		when(warehouseStockService.getStockLevelForProductAndWarehouse(product1, warehouseA)).thenReturn(TEN);
		when(warehouseStockService.getStockLevelForProductAndWarehouse(product1, warehouseB)).thenReturn(TEN);
		when(warehouseStockService.getStockLevelForProductAndWarehouse(product1, warehouseC)).thenReturn(ZERO);
		when(warehouseStockService.getStockLevelForProductAndWarehouse(product2, warehouseA)).thenReturn(TEN);
		when(warehouseStockService.getStockLevelForProductAndWarehouse(product2, warehouseB)).thenReturn(TEN);
		when(warehouseStockService.getStockLevelForProductAndWarehouse(product2, warehouseC)).thenReturn(ZERO);

		final AddressModel address = Mockito.mock(AddressModel.class);
		final CountryModel country = Mockito.mock(CountryModel.class);
		when(order.getDeliveryAddress()).thenReturn(address);
		when(order.getEntries()).thenReturn(Lists.newArrayList(orderEntry1, orderEntry2));
		when(orderEntry1.getProduct()).thenReturn(product1);
		when(orderEntry2.getProduct()).thenReturn(product2);
		when(address.getCountry()).thenReturn(country);
		when(country.getIsocode()).thenReturn(COUNTRY_CA);

		processor = new SourcingFilterProcessor();
	}

	@Test
	public void testAtsFilter()
	{
		filterA.setFilterResultOperator(SourcingFilterResultOperator.AND);
		final Set<WarehouseModel> locations = Sets.newHashSet(warehouseA, warehouseB, warehouseC);
		filterA.filterLocations(order, locations);

		assertEquals(2, locations.size());
		assertTrue(locations.contains(warehouseA));
		assertTrue(locations.contains(warehouseB));


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
}

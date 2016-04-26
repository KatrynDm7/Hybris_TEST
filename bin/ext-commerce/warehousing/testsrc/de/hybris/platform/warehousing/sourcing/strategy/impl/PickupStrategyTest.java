package de.hybris.platform.warehousing.sourcing.strategy.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;
import de.hybris.platform.warehousing.sourcing.result.SourcingResultFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PickupStrategyTest {

	private static final String PRODUCT_CODE_CUP = "cup-product-code";
	private static final String PRODUCT_CODE_TEA = "tea-product-code";
	private static final Long PRODUCT_QTY_CUP = new Long(4);
	private static final Long PRODUCT_QTY_CUP_INSUFFICIENT = new Long(1);
	private static final Long PRODUCT_QTY_CUP_NO_STOCK = new Long(0);
	private static final Long PRODUCT_QTY_TEA = new Long(13);
	private static final String WAREHOUSE_CODE_LAVAL = "laval store";
	private static final String WAREHOUSE_CODE_MONTREAL = "montreal store";

	@Mock
	private SourcingResultFactory sourcingResultFactory;
	@Mock
	private SourcingContext sourcingContext;
	@Mock
	private SourcingResults sourcingResults;
	@Mock
	private PosSelectionStrategy posSelectionStrategy;

	private final AbstractOrderModel order = new AbstractOrderModel();
	private final AbstractOrderEntryModel cupEntry = new AbstractOrderEntryModel();
	private final AbstractOrderEntryModel teaEntry = new AbstractOrderEntryModel();

	private final SourcingLocation laval = new SourcingLocation();
	private final WarehouseModel warehouseLaval = new WarehouseModel();
	private final PointOfServiceModel posLaval = new PointOfServiceModel();
	private final SourcingLocation montreal = new SourcingLocation();
	private final WarehouseModel warehouseMontreal = new WarehouseModel();
	private final PointOfServiceModel posMontreal = new PointOfServiceModel();

	private final ProductModel cup = new ProductModel();
	private final ProductModel tea = new ProductModel();

	@InjectMocks
	private PickupStrategy pickupStrategy;

	@Before
	public void setUp() throws Exception
	{
		cupEntry.setOrder(order);
		teaEntry.setOrder(order);

		warehouseLaval.setCode(WAREHOUSE_CODE_LAVAL);
		laval.setWarehouse(warehouseLaval);
		warehouseMontreal.setCode(WAREHOUSE_CODE_MONTREAL);
		montreal.setWarehouse(warehouseMontreal);

		cup.setCode(PRODUCT_CODE_CUP);
		cupEntry.setProduct(cup);
		cupEntry.setDeliveryPointOfService(posMontreal);
		cupEntry.setQuantity(PRODUCT_QTY_CUP);
		tea.setCode(PRODUCT_CODE_TEA);
		teaEntry.setProduct(tea);
		teaEntry.setDeliveryPointOfService(posMontreal);
		teaEntry.setQuantity(PRODUCT_QTY_TEA);

		final Set<SourcingResult> results = Sets.newHashSet();
		when(sourcingContext.getResult()).thenReturn(sourcingResults);
		when(sourcingResults.getResults()).thenReturn(results);
		when(posSelectionStrategy.getPointOfService(order, warehouseLaval)).thenReturn(posLaval);
		when(posSelectionStrategy.getPointOfService(order, warehouseMontreal)).thenReturn(posMontreal);
	}

	@Test
	public void testPickupStrategy_SingleOrderEntryAtSinglePickupLocation()
	{
		final Map<ProductModel, Long> montrealAvailability = Maps.newHashMap();
		montrealAvailability.put(cup, PRODUCT_QTY_CUP);
		montreal.setAvailability(montrealAvailability);

		final SourcingResult cupSourcingResult = new SourcingResult();
		final Map<AbstractOrderEntryModel, Long> cupSourcingResultMap = new HashMap<>();
		cupSourcingResultMap.put(cupEntry, PRODUCT_QTY_CUP);
		cupSourcingResult.setAllocation(cupSourcingResultMap);
		cupSourcingResult.setWarehouse(warehouseMontreal);

		when(sourcingContext.getOrderEntries()).thenReturn(Lists.newArrayList(cupEntry));
		when(sourcingContext.getSourcingLocations()).thenReturn(Lists.newArrayList(montreal, laval));
		when(sourcingResultFactory.create(anyMap(), any(SourcingLocation.class))).thenReturn(cupSourcingResult);

		pickupStrategy.source(sourcingContext);

		assertNotNull(sourcingContext.getResult().getResults());
		assertFalse(sourcingContext.getResult().getResults().isEmpty());
		assertEquals(cupSourcingResult, sourcingContext.getResult().getResults().iterator().next());

		verify(sourcingContext).getSourcingLocations();
		verify(sourcingResults).setComplete(Boolean.TRUE);
		verify(sourcingResultFactory).create(anyMap(), any(SourcingLocation.class));
	}

	@Test
	public void testPickupStrategy_MultipleOrderEntriesAtSinglePickupLocation()
	{
		final Map<ProductModel, Long> montrealAvailability = Maps.newHashMap();
		montrealAvailability.put(cup, PRODUCT_QTY_CUP);
		montrealAvailability.put(tea, PRODUCT_QTY_TEA);
		montreal.setAvailability(montrealAvailability);

		final SourcingResult cupAndTeaSourcingResult = new SourcingResult();
		final Map<AbstractOrderEntryModel, Long> cupAndteaSourcingResultMap = new HashMap<>();
		cupAndteaSourcingResultMap.put(cupEntry, PRODUCT_QTY_CUP);
		cupAndteaSourcingResultMap.put(teaEntry, PRODUCT_QTY_TEA);
		cupAndTeaSourcingResult.setAllocation(cupAndteaSourcingResultMap);
		cupAndTeaSourcingResult.setWarehouse(warehouseMontreal);

		when(sourcingContext.getOrderEntries()).thenReturn(Lists.newArrayList(cupEntry, teaEntry));
		when(sourcingContext.getSourcingLocations()).thenReturn(Lists.newArrayList(montreal, laval));
		when(sourcingResultFactory.create(anyMap(), any(SourcingLocation.class))).thenReturn(cupAndTeaSourcingResult);

		pickupStrategy.source(sourcingContext);

		assertNotNull(sourcingContext.getResult().getResults());
		assertFalse(sourcingContext.getResult().getResults().isEmpty());
		assertEquals(1, sourcingContext.getResult().getResults().size());
		assertTrue(sourcingContext.getResult().getResults().contains(cupAndTeaSourcingResult));
		assertEquals(2, sourcingContext.getResult().getResults().iterator().next().getAllocation().size());

		verify(sourcingContext).getSourcingLocations();
		verify(sourcingResults).setComplete(Boolean.TRUE);
		verify(sourcingResultFactory).create(anyMap(), any(SourcingLocation.class));
	}

	@Test
	public void testPickupStrategy_noPickupLocation()
	{
		final AbstractOrderEntryModel penEntry = new AbstractOrderEntryModel();
		penEntry.setDeliveryPointOfService(new PointOfServiceModel());

		when(sourcingContext.getOrderEntries()).thenReturn(Lists.newArrayList(penEntry));
		when(sourcingContext.getSourcingLocations()).thenReturn(Lists.newArrayList(montreal, laval));

		pickupStrategy.source(sourcingContext);

		assertNotNull(sourcingContext.getResult().getResults());
		assertTrue(sourcingContext.getResult().getResults().isEmpty());

		verify(sourcingContext).getSourcingLocations();
		verify(sourcingResults, never()).setComplete(any(Boolean.class));
		verify(sourcingResultFactory, never()).create(anyMap(), any(SourcingLocation.class));
	}

	@Test
	public void testPickupStrategy_SingleOrderEntryAtSinglePickupLocationWithInsufficientStock()
	{
		final Map<ProductModel, Long> availability = Maps.newHashMap();
		availability.put(cup, PRODUCT_QTY_CUP_INSUFFICIENT);
		montreal.setAvailability(availability);

		final SourcingResult cupInsufficientSourcingResult = new SourcingResult();
		final Map<AbstractOrderEntryModel, Long> cupSourcingResultMap = new HashMap<>();
		cupSourcingResultMap.put(cupEntry, PRODUCT_QTY_CUP_INSUFFICIENT);
		cupInsufficientSourcingResult.setAllocation(cupSourcingResultMap);
		cupInsufficientSourcingResult.setWarehouse(warehouseMontreal);

		when(sourcingContext.getOrderEntries()).thenReturn(Lists.newArrayList(cupEntry));
		when(sourcingContext.getSourcingLocations()).thenReturn(Lists.newArrayList(montreal, laval));
		when(sourcingResultFactory.create(anyMap(), any(SourcingLocation.class))).thenReturn(cupInsufficientSourcingResult);

		pickupStrategy.source(sourcingContext);

		assertNotNull(sourcingContext.getResult().getResults());
		assertFalse(sourcingContext.getResult().getResults().isEmpty());
		assertEquals(cupInsufficientSourcingResult, sourcingContext.getResult().getResults().iterator().next());
		assertEquals(PRODUCT_QTY_CUP_INSUFFICIENT,
				sourcingContext.getResult().getResults().iterator().next().getAllocation().get(cupEntry));

		verify(sourcingContext).getSourcingLocations();
		verify(sourcingResults).setComplete(Boolean.FALSE);
		verify(sourcingResultFactory).create(anyMap(), any(SourcingLocation.class));
	}

	@Test
	public void testPickupStrategy_SingleOrderEntryAtSinglePickupLocationWithNoStock()
	{
		final Map<ProductModel, Long> availability = Maps.newHashMap();
		availability.put(cup, PRODUCT_QTY_CUP_NO_STOCK);
		montreal.setAvailability(availability);

		when(sourcingContext.getOrderEntries()).thenReturn(Lists.newArrayList(cupEntry));
		when(sourcingContext.getSourcingLocations()).thenReturn(Lists.newArrayList(montreal, laval));

		pickupStrategy.source(sourcingContext);

		assertNotNull(sourcingContext.getResult().getResults());
		assertTrue(sourcingContext.getResult().getResults().isEmpty());

		verify(sourcingContext).getSourcingLocations();
		verify(sourcingResults).setComplete(Boolean.FALSE);
		verify(sourcingResultFactory, never()).create(anyMap(), any(SourcingLocation.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullContext()
	{
		pickupStrategy.source(null);
	}
}
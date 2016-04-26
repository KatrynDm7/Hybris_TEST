package de.hybris.platform.warehousing.allocation.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.DeliveryModeService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.allocation.AllocationException;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.allocation.DeclineEntries;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousing.inventoryevent.dao.InventoryEventDao;
import de.hybris.platform.warehousing.inventoryevent.service.impl.DefaultInventoryEventService;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.model.DeclineConsignmentEntryEventModel;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultAllocationServiceTest
{
	private static final Long BOOK_QTY = 20L;
	private static final Long HEADSET_QTY = 5L;
	private static final Long MOUSE_QTY = 2L;
	private static final Long PEN_QTY = 50L;

	@Mock
	private ModelService modelService;
	@Mock
	private DeliveryModeService deliveryModeService;
	@Mock
	private TimeService timeService;
	@Mock
	private PosSelectionStrategy posSelectionStrategy;
	@Mock
	private WarehousingCommentService<ConsignmentEntryModel> consignmentEntryCommentService;

	@Mock
	private InventoryEventDao inventoryEventDao;

	@Spy
	@InjectMocks
	private final DefaultAllocationService allocationService = new DefaultAllocationService();


	@Mock
	private DefaultInventoryEventService inventoryEventService;

	private AddressModel shippingAddress;
	private AbstractOrderModel order;
	private SourcingResults results;

	private SourcingResult electronicResult;
	private OrderEntryModel headsetEntry;
	private OrderEntryModel mouseEntry;
	private ProductModel headset;
	private ProductModel mouse;
	private PointOfServiceModel montreal;
	private PointOfServiceModel newyork;
	private WarehouseModel montrealWarehouse;
	private WarehouseModel newyorkWarehouse;

	private SourcingResult stationaryResult;
	private OrderEntryModel bookEntry;
	private OrderEntryModel penEntry;
	private ProductModel book;
	private ProductModel pen;
	private UserModel author;

	private ConsignmentModel consignment;
	private ConsignmentEntryModel consHeadsetEntry;
	private ConsignmentEntryModel consMouseEntry;
	private DeclineConsignmentEntryEventModel consignmentEntryEvent;

	@Before
	public void setUp() throws Exception
	{
		order = new AbstractOrderModel();
		results = new SourcingResults();
		electronicResult = new SourcingResult();
		stationaryResult = new SourcingResult();

		book = new ProductModel();
		book.setCode("bookcode");
		bookEntry = new OrderEntryModel();
		bookEntry.setProduct(book);

		headset = new ProductModel();
		headset.setCode("headsetcode");
		headsetEntry = new OrderEntryModel();
		headsetEntry.setProduct(headset);

		mouse = new ProductModel();
		mouse.setCode("mousecode");
		mouseEntry = new OrderEntryModel();
		mouseEntry.setProduct(mouse);

		pen = new ProductModel();
		pen.setCode("pencode");
		penEntry = new OrderEntryModel();
		penEntry.setProduct(pen);

		newyork = new PointOfServiceModel();
		newyork.setDisplayName("new york");
		newyorkWarehouse = new WarehouseModel();
		newyork.setWarehouses(Lists.newArrayList(newyorkWarehouse));
		montreal = new PointOfServiceModel();
		montreal.setDisplayName("montreal");
		montrealWarehouse = new WarehouseModel();
		montreal.setWarehouses(Lists.newArrayList(montrealWarehouse));
		shippingAddress = new AddressModel();

		consignment = new ConsignmentModel();
		consignment.setCode("original");
		consignment.setOrder(order);
		consignment.setWarehouse(newyorkWarehouse);
		consHeadsetEntry = spy(new ConsignmentEntryModel());
		consHeadsetEntry.setOrderEntry(headsetEntry);
		consHeadsetEntry.setConsignment(consignment);
		consHeadsetEntry.setQuantity(5l);
		consMouseEntry = spy(new ConsignmentEntryModel());
		consMouseEntry.setOrderEntry(mouseEntry);
		consMouseEntry.setConsignment(consignment);
		consMouseEntry.setQuantity(5l);
		consignment.setConsignmentEntries(Sets.newHashSet(consHeadsetEntry, consMouseEntry));

		consignmentEntryEvent = new DeclineConsignmentEntryEventModel();

		author = new UserModel();
		order.setUser(author);
		consignment.setOrder(order);

		doNothing().when(modelService).save(any(Object.class));
		when(modelService.create(ConsignmentModel.class)).thenReturn(new ConsignmentModel()).thenReturn(new ConsignmentModel())
				.thenReturn(new ConsignmentModel()).thenReturn(new ConsignmentModel());
		when(modelService.create(ConsignmentEntryModel.class)).thenReturn(new ConsignmentEntryModel())
				.thenReturn(new ConsignmentEntryModel()).thenReturn(new ConsignmentEntryModel())
				.thenReturn(new ConsignmentEntryModel());
		when(modelService.create(AllocationEventModel.class)).thenReturn(new AllocationEventModel());
		when(timeService.getCurrentTime()).thenReturn(new Date());

		when(posSelectionStrategy.getPointOfService(order, montrealWarehouse)).thenReturn(montreal);
		when(posSelectionStrategy.getPointOfService(order, newyorkWarehouse)).thenReturn(newyork);
		when(consHeadsetEntry.getQuantityPending()).thenReturn(HEADSET_QTY);
		when(consMouseEntry.getQuantityPending()).thenReturn(MOUSE_QTY);
		when(modelService.create(DeclineConsignmentEntryEventModel.class)).thenReturn(consignmentEntryEvent);
		doReturn(new Date()).when(allocationService).getExpectedShippingDate();

		final AllocationEventModel allocationEventModel = Mockito.spy(new AllocationEventModel());
		allocationEventModel.setQuantity(10);
		doReturn(allocationEventModel).when(inventoryEventService).getAllocationEventForDeclineEntry(Mockito.any());

	}

	/*
	 * Tests for Reallocation
	 */
	@Test
	public void shouldReallocateAndPopulateConsignmentEntryEvent_WithNote()
	{
		order.setDeliveryAddress(shippingAddress);

		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		final DeclineEntry entry = new DeclineEntry();
		entry.setConsignmentEntry(consHeadsetEntry);
		entry.setQuantity(1L);
		entry.setReason(DeclineReason.OUTOFSTOCK);
		entry.setNotes("notes");
		entries.setEntries(Collections.singleton(entry));

		allocationService.reallocate(entries);

		verify(modelService).save(consignmentEntryEvent);
		verify(consignmentEntryCommentService).createAndSaveComment(any(WarehousingCommentContext.class));
		verify(modelService, times(2)).refresh(entry.getConsignmentEntry());
		assertEquals(Long.valueOf(1), consignmentEntryEvent.getQuantity());
		assertEquals(consHeadsetEntry, consignmentEntryEvent.getConsignmentEntry());
		assertEquals(montrealWarehouse, consignmentEntryEvent.getReallocatedWarehouse());
		assertEquals(DeclineReason.OUTOFSTOCK, consignmentEntryEvent.getReason());
	}

	@Test
	public void shouldReallocateAndPopulateConsignmentEntryEvent_NoNote()
	{
		order.setDeliveryAddress(shippingAddress);

		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		final DeclineEntry entry = new DeclineEntry();
		entry.setConsignmentEntry(consHeadsetEntry);
		entry.setQuantity(1L);
		entry.setReason(DeclineReason.OUTOFSTOCK);
		entries.setEntries(Collections.singleton(entry));

		allocationService.reallocate(entries);

		verify(modelService).save(consignmentEntryEvent);
		verifyZeroInteractions(consignmentEntryCommentService);
		verify(modelService, times(2)).refresh(entry.getConsignmentEntry());
		assertEquals(Long.valueOf(1), consignmentEntryEvent.getQuantity());
		assertEquals(consHeadsetEntry, consignmentEntryEvent.getConsignmentEntry());
		assertEquals(montrealWarehouse, consignmentEntryEvent.getReallocatedWarehouse());
		assertEquals(DeclineReason.OUTOFSTOCK, consignmentEntryEvent.getReason());
	}

	@Test
	public void shouldReallocate_PartialConsignmentEntryOnly()
	{
		order.setDeliveryAddress(shippingAddress);

		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		final DeclineEntry entry = new DeclineEntry();
		entry.setConsignmentEntry(consHeadsetEntry);
		entry.setQuantity(1L);
		entries.setEntries(Collections.singleton(entry));

		final ConsignmentModel reallocated = allocationService.reallocate(entries);
		final ConsignmentEntryModel reallocatedEntry = reallocated.getConsignmentEntries().iterator().next();

		assertEquals("original_0", reallocated.getCode());
		assertEquals(1, reallocated.getConsignmentEntries().size());
		assertEquals(Long.valueOf(1), reallocatedEntry.getQuantity());
		assertEquals(headsetEntry, reallocatedEntry.getOrderEntry());
		assertEquals(montrealWarehouse, reallocated.getWarehouse());
	}

	@Test
	public void shouldReallocate_PartialConsignmentEntry_SecondDecline()
	{
		order.setDeliveryAddress(shippingAddress);

		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		final DeclineEntry entry = new DeclineEntry();
		entry.setConsignmentEntry(consHeadsetEntry);
		entry.setQuantity(1L);
		entries.setEntries(Collections.singleton(entry));

		when(consHeadsetEntry.getQuantityPending()).thenReturn(HEADSET_QTY - 1);
		when(consHeadsetEntry.getDeclineEntryEvents()).thenReturn(Collections.singleton(new DeclineConsignmentEntryEventModel()));

		final ConsignmentModel reallocated = allocationService.reallocate(entries);
		final ConsignmentEntryModel reallocatedEntry = reallocated.getConsignmentEntries().iterator().next();

		assertEquals("original_1", reallocated.getCode());
		assertEquals(1, reallocated.getConsignmentEntries().size());
		assertEquals(Long.valueOf(1), reallocatedEntry.getQuantity());
		assertEquals(headsetEntry, reallocatedEntry.getOrderEntry());
		assertEquals(montrealWarehouse, reallocated.getWarehouse());
	}

	@Test
	public void shouldReallocate_NoMoreThanThanConsignmentEntryQuantityPending()
	{
		order.setDeliveryAddress(shippingAddress);

		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		final DeclineEntry entry = new DeclineEntry();
		entry.setConsignmentEntry(consHeadsetEntry);
		entry.setQuantity(10L);
		entries.setEntries(Collections.singleton(entry));

		final ConsignmentModel reallocated = allocationService.reallocate(entries);
		final ConsignmentEntryModel reallocatedEntry = reallocated.getConsignmentEntries().iterator().next();

		assertEquals(Long.valueOf(HEADSET_QTY), consignmentEntryEvent.getQuantity());
		assertEquals(1, reallocated.getConsignmentEntries().size());
		assertEquals(Long.valueOf(HEADSET_QTY), reallocatedEntry.getQuantity());
		assertEquals(headsetEntry, reallocatedEntry.getOrderEntry());
		assertEquals(montrealWarehouse, reallocated.getWarehouse());
	}

	@Test
	public void shouldReallocate_Partial_MultipleEntries()
	{
		order.setDeliveryAddress(shippingAddress);

		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		final DeclineEntry entry1 = new DeclineEntry();
		entry1.setConsignmentEntry(consHeadsetEntry);
		entry1.setQuantity(1L);
		entry1.setNotes("notes 1");
		final DeclineEntry entry2 = new DeclineEntry();
		entry2.setConsignmentEntry(consMouseEntry);
		entry2.setQuantity(2L);
		entry2.setNotes("notes 2");
		entries.setEntries(Sets.newHashSet(entry1, entry2));

		final ConsignmentModel reallocated = allocationService.reallocate(entries);
		final ConsignmentEntryModel reallocatedHeadsetEntry = reallocated.getConsignmentEntries().stream()
				.filter(entry -> entry.getOrderEntry().equals(headsetEntry)).findFirst().get();
		final ConsignmentEntryModel reallocatedMouseEntry = reallocated.getConsignmentEntries().stream()
				.filter(entry -> entry.getOrderEntry().equals(mouseEntry)).findFirst().get();

		verify(modelService, times(2)).save(consignmentEntryEvent);
		verify(consignmentEntryCommentService, times(2)).createAndSaveComment(any(WarehousingCommentContext.class));
		verify(modelService, times(2)).refresh(consHeadsetEntry);
		verify(modelService, times(2)).refresh(consMouseEntry);

		assertEquals(2, reallocated.getConsignmentEntries().size());
		assertEquals(Long.valueOf(1L), reallocatedHeadsetEntry.getQuantity());
		assertEquals(Long.valueOf(2L), reallocatedMouseEntry.getQuantity());
		assertEquals(montrealWarehouse, reallocated.getWarehouse());
	}

	@Test
	public void shouldReallocate_EntireConsignment()
	{
		order.setDeliveryAddress(shippingAddress);

		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		final DeclineEntry entry1 = new DeclineEntry();
		entry1.setConsignmentEntry(consHeadsetEntry);
		entry1.setQuantity(HEADSET_QTY);
		final DeclineEntry entry2 = new DeclineEntry();
		entry2.setConsignmentEntry(consMouseEntry);
		entry2.setQuantity(MOUSE_QTY);
		entries.setEntries(Sets.newHashSet(entry1, entry2));

		final ConsignmentModel reallocated = allocationService.reallocate(entries);
		final ConsignmentEntryModel reallocatedHeadsetEntry = reallocated.getConsignmentEntries().stream()
				.filter(entry -> entry.getOrderEntry().equals(headsetEntry)).findFirst().get();
		final ConsignmentEntryModel reallocatedMouseEntry = reallocated.getConsignmentEntries().stream()
				.filter(entry -> entry.getOrderEntry().equals(mouseEntry)).findFirst().get();

		verify(modelService, times(2)).save(consignmentEntryEvent);
		verify(modelService, times(2)).refresh(consHeadsetEntry);
		verify(modelService, times(2)).refresh(consMouseEntry);

		assertEquals(2, reallocated.getConsignmentEntries().size());
		assertEquals(Long.valueOf(HEADSET_QTY), reallocatedHeadsetEntry.getQuantity());
		assertEquals(Long.valueOf(MOUSE_QTY), reallocatedMouseEntry.getQuantity());
		assertEquals(montrealWarehouse, reallocated.getWarehouse());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailReallocate_NullEntries()
	{
		allocationService.reallocate(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailReallocate_EmptyEntries()
	{
		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);
		entries.setEntries(Collections.emptySet());

		allocationService.reallocate(entries);
	}

	@Test(expected = AllocationException.class)
	public void shouldFailReallocate_ZeroQuantity()
	{
		final DeclineEntries entries = new DeclineEntries();
		entries.setReallocationWarehouse(montrealWarehouse);

		final DeclineEntry entry = new DeclineEntry();
		entry.setQuantity(0L);
		entry.setConsignmentEntry(consHeadsetEntry);
		entries.setEntries(Collections.singleton(entry));

		allocationService.reallocate(entries);
	}

	/*
	 * Tests for Allocation (consignment creation)
	 */
	@Test
	public void shouldMakePickupConsignment_OrderEntryHasDeliveryPos()
	{
		final DeliveryModeModel pickupDeliveryMode = new DeliveryModeModel();
		when(deliveryModeService.getDeliveryModeForCode(any())).thenReturn(pickupDeliveryMode);

		final AddressModel newyorkAddress = new AddressModel();
		newyork.setAddress(newyorkAddress);
		headsetEntry.setDeliveryPointOfService(newyork);
		order.setEntries(Lists.newArrayList(headsetEntry));

		final Map<AbstractOrderEntryModel, Long> electronicAllocation = Maps.newHashMap();
		electronicAllocation.put(headsetEntry, HEADSET_QTY);
		electronicResult.setAllocation(electronicAllocation);
		electronicResult.setWarehouse(newyorkWarehouse);

		final ConsignmentModel pickupConsignment = allocationService.createConsignment(order, "consignment1234", electronicResult);

		assertEquals(ConsignmentStatus.READY, pickupConsignment.getStatus());
		assertEquals(pickupDeliveryMode, pickupConsignment.getDeliveryMode());
		assertEquals(newyorkAddress, pickupConsignment.getShippingAddress());
	}

	@Test
	public void shouldMakeShipConsignment_OrderEntryHasNoDeliveryPos()
	{
		final DeliveryModeModel shippingDeliveryMode = new DeliveryModeModel();
		order.setDeliveryMode(shippingDeliveryMode);

		order.setEntries(Lists.newArrayList(headsetEntry));

		final Map<AbstractOrderEntryModel, Long> electronicAllocation = Maps.newHashMap();
		electronicAllocation.put(headsetEntry, HEADSET_QTY);
		electronicResult.setAllocation(electronicAllocation);
		electronicResult.setWarehouse(newyorkWarehouse);

		final ConsignmentModel shipConsignment = allocationService.createConsignment(order, "consignment1234", electronicResult);

		assertEquals(ConsignmentStatus.READY, shipConsignment.getStatus());
		assertEquals(shippingDeliveryMode, shipConsignment.getDeliveryMode());
		assertEquals(order.getDeliveryAddress(), shipConsignment.getShippingAddress());
		verify(allocationService, times(1)).getExpectedShippingDate();
	}

	@Test
	public void createShipConsignment_MultipleConsignments_MultipleResults()
	{
		order.setEntries(Lists.newArrayList(headsetEntry));
		order.setDeliveryAddress(shippingAddress);
		electronicResult.setWarehouse(montrealWarehouse);
		stationaryResult.setWarehouse(newyorkWarehouse);

		final Map<AbstractOrderEntryModel, Long> electronicAllocation = Maps.newHashMap();
		electronicAllocation.put(headsetEntry, HEADSET_QTY);
		electronicAllocation.put(mouseEntry, MOUSE_QTY);
		electronicResult.setAllocation(electronicAllocation);

		final Map<AbstractOrderEntryModel, Long> stationaryAllocation = Maps.newHashMap();
		stationaryAllocation.put(bookEntry, BOOK_QTY);
		stationaryAllocation.put(penEntry, PEN_QTY);
		stationaryResult.setAllocation(stationaryAllocation);
		results.setResults(Sets.newHashSet(electronicResult, stationaryResult));

		final Collection<ConsignmentModel> consignments = allocationService.createConsignments(order, "consignment1234", results);
		assertNotNull(consignments);
		assertEquals(2, consignments.size());

		for (final ConsignmentModel consignment : consignments)
		{
			if (consignment.getDeliveryPointOfService().equals(montreal))
			{
				validateElectronicConsignment_MultipleResults(consignment);
			}
			else
			{
				validateStationaryConsignment_MultipleResults(consignment);
			}
		}

		// verify consignment codes are unique
		assertFalse(consignments.stream().allMatch(cons -> cons.getCode().equals("consignment1234_0")));
	}

	@Test
	public void createShipConsignment_MultipleConsignments_SingleResult()
	{
		order.setEntries(Lists.newArrayList(headsetEntry));
		order.setDeliveryAddress(shippingAddress);
		electronicResult.setWarehouse(montrealWarehouse);
		stationaryResult.setWarehouse(newyorkWarehouse);

		final Map<AbstractOrderEntryModel, Long> electronicAllocation = Maps.newHashMap();
		electronicAllocation.put(headsetEntry, HEADSET_QTY);
		electronicResult.setAllocation(electronicAllocation);

		final Map<AbstractOrderEntryModel, Long> stationaryAllocation = Maps.newHashMap();
		stationaryAllocation.put(bookEntry, BOOK_QTY);
		stationaryResult.setAllocation(stationaryAllocation);
		results.setResults(Sets.newHashSet(electronicResult, stationaryResult));

		final Collection<ConsignmentModel> consignments = allocationService.createConsignments(order, "consignment1234", results);
		assertNotNull(consignments);
		assertEquals(2, consignments.size());

		for (final ConsignmentModel consignment : consignments)
		{
			if (consignment.getDeliveryPointOfService().equals(montreal))
			{
				validateElectronicConsignment_SingleResult(consignment);
			}
			else
			{
				validateStationaryConsignment_SingleResult(consignment);
			}
		}

		// verify consignment codes are unique
		assertFalse(consignments.stream().allMatch(cons -> cons.getCode().equals("consignment1234_0")));
	}


	@Test
	public void createShipConsignment_SingleConsignment_MultipleResults()
	{
		order.setEntries(Lists.newArrayList(headsetEntry));
		order.setDeliveryAddress(shippingAddress);
		electronicResult.setWarehouse(montrealWarehouse);

		final Map<AbstractOrderEntryModel, Long> allocation = Maps.newHashMap();
		allocation.put(headsetEntry, HEADSET_QTY);
		allocation.put(mouseEntry, MOUSE_QTY);
		electronicResult.setAllocation(allocation);
		results.setResults(Sets.newHashSet(electronicResult));

		final Collection<ConsignmentModel> consignments = allocationService.createConsignments(order, "consignment1234", results);
		assertNotNull(consignments);
		assertEquals(1, consignments.size());

		validateElectronicConsignment_MultipleResults(consignments.iterator().next());
	}

	@Test
	public void createShipConsignment_SingleConsignment_SingleResult()
	{
		order.setEntries(Lists.newArrayList(headsetEntry));
		order.setDeliveryAddress(shippingAddress);
		electronicResult.setWarehouse(montrealWarehouse);
		final Map<AbstractOrderEntryModel, Long> allocation = Maps.newHashMap();
		allocation.put(headsetEntry, HEADSET_QTY);
		electronicResult.setAllocation(allocation);

		final ConsignmentModel consignment = allocationService.createConsignment(order, "consignment1234", electronicResult);
		assertNotNull(consignment);
		validateElectronicConsignment_SingleResult(consignment);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullCode()
	{
		allocationService.createConsignment(new AbstractOrderModel(), null, new SourcingResult());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFail_nullSourcingResult()
	{
		allocationService.createConsignment(new AbstractOrderModel(), null, new SourcingResult());
	}

	@Test
	public void createAllocationEvents_MultipleConsignments()
	{
		order.setEntries(Lists.newArrayList(headsetEntry));
		order.setDeliveryAddress(shippingAddress);
		electronicResult.setWarehouse(montrealWarehouse);
		stationaryResult.setWarehouse(newyorkWarehouse);

		final Map<AbstractOrderEntryModel, Long> electronicAllocation = Maps.newHashMap();
		electronicAllocation.put(headsetEntry, HEADSET_QTY);
		electronicResult.setAllocation(electronicAllocation);

		final Map<AbstractOrderEntryModel, Long> stationaryAllocation = Maps.newHashMap();
		stationaryAllocation.put(bookEntry, BOOK_QTY);
		stationaryResult.setAllocation(stationaryAllocation);
		results.setResults(Sets.newHashSet(electronicResult, stationaryResult));

		final Collection<ConsignmentModel> consignments = allocationService.createConsignments(order, "consignment1234", results);
		assertNotNull(consignments);
		assertEquals(2, consignments.size());

		final WarehouseModel warehouse = new WarehouseModel();
		warehouse.setCode("warehouse_NY");
		consignments.forEach(consignment -> consignment.setWarehouse(warehouse));

		final Collection<AllocationEventModel> allocationEvents = consignments.stream()
				.map(result -> inventoryEventService.createAllocationEvents(result)).flatMap(result -> result.stream())
				.collect(Collectors.toList());
		assertNotNull(allocationEvents);

		for (final AllocationEventModel event : allocationEvents)
		{
			if (event.getConsignmentEntry().getOrderEntry() == bookEntry)
			{
				assertEquals(BOOK_QTY, Long.valueOf(event.getQuantity()));
			}
			else
			{
				assertEquals(headsetEntry, event.getConsignmentEntry().getOrderEntry());
				assertEquals(HEADSET_QTY, Long.valueOf(event.getQuantity()));
			}
		}
	}

	private void validateStationaryConsignment_SingleResult(final ConsignmentModel consignment)
	{
		assertNotNull(consignment.getShippingAddress());
		assertEquals(shippingAddress, consignment.getShippingAddress());

		assertNotNull(consignment.getConsignmentEntries());
		assertFalse(consignment.getConsignmentEntries().isEmpty());
		assertEquals(1, consignment.getConsignmentEntries().size());

		final ConsignmentEntryModel entry = consignment.getConsignmentEntries().iterator().next();
		assertEquals(bookEntry, entry.getOrderEntry());
		assertEquals(BOOK_QTY, entry.getQuantity());
		assertEquals(newyork, consignment.getDeliveryPointOfService());
	}

	private void validateElectronicConsignment_SingleResult(final ConsignmentModel consignment)
	{
		assertNotNull(consignment.getShippingAddress());
		assertEquals(shippingAddress, consignment.getShippingAddress());

		assertNotNull(consignment.getConsignmentEntries());
		assertFalse(consignment.getConsignmentEntries().isEmpty());
		assertEquals(1, consignment.getConsignmentEntries().size());

		final ConsignmentEntryModel entry = consignment.getConsignmentEntries().iterator().next();
		assertEquals(headsetEntry, entry.getOrderEntry());
		assertEquals(HEADSET_QTY, entry.getQuantity());
		assertEquals(montreal, consignment.getDeliveryPointOfService());
	}

	private void validateStationaryConsignment_MultipleResults(final ConsignmentModel stationaryConsignment)
	{
		assertNotNull(stationaryConsignment.getShippingAddress());
		assertEquals(shippingAddress, stationaryConsignment.getShippingAddress());
		assertEquals(newyork, stationaryConsignment.getDeliveryPointOfService());

		assertNotNull(stationaryConsignment.getConsignmentEntries());
		assertFalse(stationaryConsignment.getConsignmentEntries().isEmpty());
		assertEquals(2, stationaryConsignment.getConsignmentEntries().size());

		for (final ConsignmentEntryModel entry : stationaryConsignment.getConsignmentEntries())
		{
			if (entry.getOrderEntry().equals(bookEntry))
			{
				assertEquals(bookEntry, entry.getOrderEntry());
				assertEquals(BOOK_QTY, entry.getQuantity());
			}
			else
			{
				assertEquals(penEntry, entry.getOrderEntry());
				assertEquals(PEN_QTY, entry.getQuantity());
			}
		}
	}

	private void validateElectronicConsignment_MultipleResults(final ConsignmentModel electronicConsignment)
	{
		assertNotNull(electronicConsignment.getShippingAddress());
		assertEquals(shippingAddress, electronicConsignment.getShippingAddress());
		assertEquals(montreal, electronicConsignment.getDeliveryPointOfService());

		assertNotNull(electronicConsignment.getConsignmentEntries());
		assertFalse(electronicConsignment.getConsignmentEntries().isEmpty());
		assertEquals(2, electronicConsignment.getConsignmentEntries().size());

		for (final ConsignmentEntryModel entry : electronicConsignment.getConsignmentEntries())
		{
			if (entry.getOrderEntry().equals(headsetEntry))
			{
				assertEquals(headsetEntry, entry.getOrderEntry());
				assertEquals(HEADSET_QTY, entry.getQuantity());
			}
			else
			{
				assertEquals(mouseEntry, entry.getOrderEntry());
				assertEquals(MOUSE_QTY, entry.getQuantity());
			}
		}
	}
}

package de.hybris.platform.warehousing.allocation.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class OrderEntryAllocatedQuantityHandlerTest
{
	@InjectMocks
	private final OrderEntryAllocatedQuantityHandler handler = new OrderEntryAllocatedQuantityHandler();

	@Mock
	private InventoryEventService inventoryEventService;

	@Mock
	private OrderEntryModel orderEntry;

	@Test
	public void shouldInvokeDynamicAttributeHanlder_WhenConsignmentsExist()
	{
		// Given
		OrderModel order = Mockito.mock(OrderModel.class);
		ConsignmentModel consignment = Mockito.mock(ConsignmentModel.class);

		// When
		when(order.getConsignments()).thenReturn(Sets.newSet(consignment));
		when(orderEntry.getOrder()).thenReturn(order);
		handler.get(orderEntry);

		// Then
		verify(inventoryEventService, times(1)).getAllocationEventsForOrderEntry(orderEntry);
	}

	@Test
	public void shouldNotInvokeDynamicAttributeHanlder_WhenConsignmentsDontExist()
	{
		// Given
		OrderModel order = Mockito.mock(OrderModel.class);

		// When
		when(order.getConsignments()).thenReturn(Sets.newSet());
		when(orderEntry.getOrder()).thenReturn(order);
		handler.get(orderEntry);

		// Then
		verify(inventoryEventService, times(0)).getAllocationEventsForOrderEntry(orderEntry);
	}

}

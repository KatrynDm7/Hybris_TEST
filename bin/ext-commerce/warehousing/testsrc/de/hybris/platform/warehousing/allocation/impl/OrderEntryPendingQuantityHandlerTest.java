package de.hybris.platform.warehousing.allocation.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class OrderEntryPendingQuantityHandlerTest
{
	private final OrderEntryPendingQuantityHandler handler = new OrderEntryPendingQuantityHandler();

	@Mock
	private AbstractOrderEntryModel orderEntry;

	@Test
	public void shouldGetQuantityPending_NoShipment()
	{
		when(orderEntry.getQuantity()).thenReturn(5L);
		when(orderEntry.getQuantityCancelled()).thenReturn(2L);
		when(orderEntry.getQuantityShipped()).thenReturn(0L);

		final Long quantityPending = handler.get(orderEntry);
		assertEquals(Long.valueOf(3L), quantityPending);
	}

	@Test
	public void shouldGetQuantityPending_WithFullShipment()
	{
		when(orderEntry.getQuantity()).thenReturn(5L);
		when(orderEntry.getQuantityCancelled()).thenReturn(2L);
		when(orderEntry.getQuantityShipped()).thenReturn(3L);

		final Long quantityPending = handler.get(orderEntry);
		assertEquals(Long.valueOf(0L), quantityPending);
	}

	@Test
	public void shouldGetQuantityPending_WithPartialShipment()
	{
		when(orderEntry.getQuantity()).thenReturn(5L);
		when(orderEntry.getQuantityCancelled()).thenReturn(2L);
		when(orderEntry.getQuantityShipped()).thenReturn(2L);

		final Long quantityPending = handler.get(orderEntry);
		assertEquals(Long.valueOf(1L), quantityPending);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(orderEntry, 5L);
	}
}

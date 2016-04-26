package de.hybris.platform.warehousing.allocation.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConsignmentEntryPendingQuantityHandlerTest
{
	private final ConsignmentEntryPendingQuantityHandler handler = new ConsignmentEntryPendingQuantityHandler();

	@Mock
	private ConsignmentEntryModel consignmentEntry;

	@Test
	public void shouldGetQuantityPending_HasCancelledOnly()
	{
		when(consignmentEntry.getQuantity()).thenReturn(5L);
		when(consignmentEntry.getQuantityCancelled()).thenReturn(2L);

		final Long quantityPending = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(3L), quantityPending);
	}

	@Test
	public void shouldGetQuantityPending_HasCancelledAndFullShipped()
	{
		when(consignmentEntry.getQuantity()).thenReturn(5L);
		when(consignmentEntry.getQuantityCancelled()).thenReturn(2L);
		when(consignmentEntry.getQuantityShipped()).thenReturn(3L);

		final Long quantityPending = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(0L), quantityPending);
	}

	@Test
	public void shouldGetQuantityPending_HasCancelledAndPartialShipped()
	{
		when(consignmentEntry.getQuantity()).thenReturn(5L);
		when(consignmentEntry.getQuantityCancelled()).thenReturn(2L);
		when(consignmentEntry.getQuantityShipped()).thenReturn(2L);

		final Long quantityPending = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(1L), quantityPending);
	}

	@Test
	public void shouldGetQuantityPending_HasDeclineOnly()
	{
		when(consignmentEntry.getQuantity()).thenReturn(5L);
		when(consignmentEntry.getQuantityDeclined()).thenReturn(2L);

		final Long quantityPending = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(3L), quantityPending);
	}

	@Test
	public void shouldGetQuantityPending_HasCancelledAndDeclined()
	{
		when(consignmentEntry.getQuantity()).thenReturn(5L);
		when(consignmentEntry.getQuantityCancelled()).thenReturn(2L);
		when(consignmentEntry.getQuantityDeclined()).thenReturn(2L);

		final Long quantityPending = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(1L), quantityPending);
	}

	@Test
	public void shouldGetQuantityPending_HasCancelledAndDeclinedAndShipped()
	{
		when(consignmentEntry.getQuantity()).thenReturn(5L);
		when(consignmentEntry.getQuantityCancelled()).thenReturn(2L);
		when(consignmentEntry.getQuantityDeclined()).thenReturn(2L);
		when(consignmentEntry.getQuantityShipped()).thenReturn(1L);

		final Long quantityPending = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(0L), quantityPending);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(consignmentEntry, 5L);
	}
}

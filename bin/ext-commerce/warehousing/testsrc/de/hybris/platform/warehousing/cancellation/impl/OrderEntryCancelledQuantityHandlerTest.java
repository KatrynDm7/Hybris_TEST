package de.hybris.platform.warehousing.cancellation.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.warehousing.model.CancellationOrderEntryEventModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;


@UnitTest
public class OrderEntryCancelledQuantityHandlerTest
{
	private final OrderEntryCancelledQuantityHandler handler = new OrderEntryCancelledQuantityHandler();
	private AbstractOrderEntryModel orderEntry;

	@Before
	public void setUp()
	{
		orderEntry = new AbstractOrderEntryModel();

		final CancellationOrderEntryEventModel event1 = new CancellationOrderEntryEventModel();
		event1.setQuantity(5L);
		final CancellationOrderEntryEventModel event2 = new CancellationOrderEntryEventModel();
		event2.setQuantity(4L);
		final CancellationOrderEntryEventModel event3 = new CancellationOrderEntryEventModel();
		event3.setQuantity(3L);
		orderEntry.setCancellationEntryEvents(Sets.newHashSet(event1, event2, event3));

		final ConsignmentEntryModel cem1 = Mockito.spy(new ConsignmentEntryModel());
		Mockito.when(cem1.getQuantityCancelled()).thenReturn(2L);
		final ConsignmentEntryModel cem2 = Mockito.spy(new ConsignmentEntryModel());
		Mockito.when(cem2.getQuantityCancelled()).thenReturn(1L);
		orderEntry.setConsignmentEntries(Sets.newHashSet(cem1, cem2));
	}

	@Test
	public void shouldGetQuantityCancelled_EventsAndConsignment()
	{
		final Long quantityCancelled = handler.get(orderEntry);
		assertEquals(Long.valueOf(15L), quantityCancelled);
	}

	@Test
	public void shouldGetQuantityCancelled_NoEvents()
	{
		orderEntry.setCancellationEntryEvents(Collections.emptySet());
		final Long quantityCancelled = handler.get(orderEntry);
		assertEquals(Long.valueOf(3L), quantityCancelled);
	}

	@Test
	public void shouldGetQuantityCancelled_NoConsignments()
	{
		orderEntry.setConsignmentEntries(Collections.emptySet());
		final Long quantityCancelled = handler.get(orderEntry);
		assertEquals(Long.valueOf(12L), quantityCancelled);
	}

	@Test
	public void shouldGetQuantityCancelled_NoEventsAndConsignments()
	{
		orderEntry.setCancellationEntryEvents(Collections.emptySet());
		orderEntry.setConsignmentEntries(Collections.emptySet());
		final Long quantityCancelled = handler.get(orderEntry);
		assertEquals(Long.valueOf(0L), quantityCancelled);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(orderEntry, 5L);
	}
}

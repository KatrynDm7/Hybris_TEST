package de.hybris.platform.warehousing.cancellation.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.warehousing.model.CancellationConsignmentEntryEventModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@UnitTest
public class ConsignmentEntryCancelledQuantityHandlerTest
{
	private final ConsignmentEntryCancelledQuantityHandler handler = new ConsignmentEntryCancelledQuantityHandler();
	private ConsignmentEntryModel consignmentEntry;

	@Before
	public void setUp()
	{
		consignmentEntry = new ConsignmentEntryModel();

		final CancellationConsignmentEntryEventModel event1 = new CancellationConsignmentEntryEventModel();
		event1.setQuantity(5L);
		final CancellationConsignmentEntryEventModel event2 = new CancellationConsignmentEntryEventModel();
		event2.setQuantity(4L);
		final CancellationConsignmentEntryEventModel event3 = new CancellationConsignmentEntryEventModel();
		event3.setQuantity(3L);
		consignmentEntry.setCancellationEntryEvents(Sets.newHashSet(event1, event2, event3));
	}

	@Test
	public void shouldGetQuantityCancelled()
	{
		final Long quantityCancelled = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(12L), quantityCancelled);
	}

	@Test
	public void shouldGetZero_EmptySet()
	{
		consignmentEntry.setCancellationEntryEvents(Collections.emptySet());
		final Long quantityCancelled = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(0L), quantityCancelled);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(consignmentEntry, 5L);
	}
}

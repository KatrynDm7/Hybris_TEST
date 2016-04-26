package de.hybris.platform.warehousing.allocation.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.warehousing.model.DeclineConsignmentEntryEventModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@UnitTest
public class ConsignmentEntryDeclinedQuantityHandlerTest
{
	private final ConsignmentEntryDeclinedQuantityHandler handler = new ConsignmentEntryDeclinedQuantityHandler();
	private ConsignmentEntryModel consignmentEntry;

	@Before
	public void setUp()
	{
		consignmentEntry = new ConsignmentEntryModel();

		final DeclineConsignmentEntryEventModel event1 = new DeclineConsignmentEntryEventModel();
		event1.setQuantity(5L);
		final DeclineConsignmentEntryEventModel event2 = new DeclineConsignmentEntryEventModel();
		event2.setQuantity(4L);
		final DeclineConsignmentEntryEventModel event3 = new DeclineConsignmentEntryEventModel();
		event3.setQuantity(3L);
		consignmentEntry.setDeclineEntryEvents(Sets.newHashSet(event1, event2, event3));
	}

	@Test
	public void shouldGetQuantityDeclined()
	{
		final Long quantityDeclined = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(12L), quantityDeclined);
	}

	@Test
	public void shouldGetZero_EmptySet()
	{
		consignmentEntry.setDeclineEntryEvents(Collections.emptySet());
		final Long quantityDeclined = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(0L), quantityDeclined);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(consignmentEntry, 5L);
	}
}

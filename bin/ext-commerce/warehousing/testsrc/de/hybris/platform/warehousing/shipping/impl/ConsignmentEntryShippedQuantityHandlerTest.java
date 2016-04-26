package de.hybris.platform.warehousing.shipping.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.warehousing.model.ShippedConsignmentEntryEventModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@UnitTest
public class ConsignmentEntryShippedQuantityHandlerTest
{
	private final ConsignmentEntryShippedQuantityHandler handler = new ConsignmentEntryShippedQuantityHandler();
	private ConsignmentEntryModel consignmentEntry;

	@Before
	public void setUp()
	{
		consignmentEntry = new ConsignmentEntryModel();

		final ShippedConsignmentEntryEventModel event1 = new ShippedConsignmentEntryEventModel();
		event1.setQuantity(1L);
		final ShippedConsignmentEntryEventModel event2 = new ShippedConsignmentEntryEventModel();
		event2.setQuantity(3L);
		final ShippedConsignmentEntryEventModel event3 = new ShippedConsignmentEntryEventModel();
		event3.setQuantity(5L);
		consignmentEntry.setShippedEntryEvents(Sets.newHashSet(event1, event2, event3));
	}

	@Test
	public void shouldGetQuantityShipped()
	{
		final Long quantityShipped = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(9L), quantityShipped);
	}

	@Test
	public void shouldGetZero_EmptySet()
	{
		consignmentEntry.setShippedEntryEvents(Collections.emptySet());
		final Long quantityShipped = handler.get(consignmentEntry);
		assertEquals(Long.valueOf(0L), quantityShipped);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(consignmentEntry, 10L);
	}

}

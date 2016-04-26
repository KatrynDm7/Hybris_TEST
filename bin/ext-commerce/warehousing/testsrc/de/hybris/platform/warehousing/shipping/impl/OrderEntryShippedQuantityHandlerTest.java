package de.hybris.platform.warehousing.shipping.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class OrderEntryShippedQuantityHandlerTest
{
	private final OrderEntryShippedQuantityHandler handler = new OrderEntryShippedQuantityHandler();

	private AbstractOrderEntryModel orderEntry;
	private ConsignmentEntryModel consignmentEntry1;
	private ConsignmentEntryModel consignmentEntry2;

	@Before
	public void setUp()
	{
		orderEntry = new AbstractOrderEntryModel();
		consignmentEntry1 = spy(new ConsignmentEntryModel());
		consignmentEntry2 = spy(new ConsignmentEntryModel());
		when(consignmentEntry1.getQuantityShipped()).thenReturn(3L);
		when(consignmentEntry2.getQuantityShipped()).thenReturn(1L);
	}

	@Test
	public void shouldGetQuantityShipped_SingleConsignmentEntry()
	{
		orderEntry.setConsignmentEntries(Sets.newHashSet(consignmentEntry1));

		final Long quantityShipped = handler.get(orderEntry);
		assertEquals(Long.valueOf(3L), quantityShipped);
	}

	@Test
	public void shouldGetQuantityShipped_2ConsignmentEntries()
	{
		orderEntry.setConsignmentEntries(Sets.newHashSet(consignmentEntry1, consignmentEntry2));

		final Long quantityShipped = handler.get(orderEntry);
		assertEquals(Long.valueOf(4L), quantityShipped);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(orderEntry, 5L);
	}
}

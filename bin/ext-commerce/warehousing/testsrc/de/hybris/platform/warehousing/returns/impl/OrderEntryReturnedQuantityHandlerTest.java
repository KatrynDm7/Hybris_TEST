package de.hybris.platform.warehousing.returns.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class OrderEntryReturnedQuantityHandlerTest
{
	private OrderEntryReturnedQuantityHandler handler;
	private AbstractOrderEntryModel entry;
	private List<ReturnEntryModel> returnEntries;

	@Mock
	private ReturnService returnService;

	@Before
	public void setUp()
	{
		handler = new OrderEntryReturnedQuantityHandler();
		handler.setReturnService(returnService);
		entry = new OrderEntryModel();
	}

	@Test
	public void shouldGetValueWhenCompletedRefundEntriesPresent()
	{
		final ReturnEntryModel entry1 = new RefundEntryModel();
		entry1.setReceivedQuantity(4L);
		entry1.setStatus(ReturnStatus.COMPLETED);
		final ReturnEntryModel entry2 = new RefundEntryModel();
		entry2.setReceivedQuantity(3L);
		entry2.setStatus(ReturnStatus.COMPLETED);

		returnEntries = Arrays.asList(entry1, entry2);

		when(returnService.getReturnEntry(anyObject())).thenReturn(returnEntries);

		final long qtyReturned = handler.get(entry);

		assertEquals(7L, qtyReturned);
	}

	@Test
	public void shouldGetLowerWhenRefundEntriesNotCompletedStatus()
	{
		final ReturnEntryModel entry1 = new RefundEntryModel();
		entry1.setReceivedQuantity(4L);
		entry1.setStatus(ReturnStatus.CANCELED);
		final ReturnEntryModel entry2 = new RefundEntryModel();
		entry2.setReceivedQuantity(3L);
		entry2.setStatus(ReturnStatus.WAIT);
		final ReturnEntryModel entry3 = new RefundEntryModel();
		entry3.setReceivedQuantity(2L);
		entry3.setStatus(ReturnStatus.COMPLETED);
		final ReturnEntryModel entry4 = new RefundEntryModel();
		entry4.setReceivedQuantity(5L);
		entry4.setStatus(ReturnStatus.RECEIVED);
		final ReturnEntryModel entry5 = new RefundEntryModel();
		entry5.setReceivedQuantity(5L);
		entry5.setStatus(ReturnStatus.PAYMENT_CAPTURED);
		final ReturnEntryModel entry6 = new RefundEntryModel();
		entry6.setReceivedQuantity(5L);
		entry6.setStatus(ReturnStatus.PAYMENT_FAILED);
		final ReturnEntryModel entry7 = new RefundEntryModel();
		entry7.setReceivedQuantity(5L);
		entry7.setStatus(ReturnStatus.TAX_CAPTURED);
		final ReturnEntryModel entry8 = new RefundEntryModel();
		entry8.setReceivedQuantity(5L);
		entry8.setStatus(ReturnStatus.TAX_FAILED);

		returnEntries = Arrays.asList(entry1, entry2, entry3, entry4, entry5, entry6, entry7, entry8);

		when(returnService.getReturnEntry(anyObject())).thenReturn(returnEntries);

		final long qtyReturned = handler.get(entry);

		assertEquals(2L, qtyReturned);
	}

	@Test
	public void shouldGetZeroWhenReplacementEntryPresent()
	{
		final ReturnEntryModel entry1 = new ReplacementEntryModel();
		entry1.setReceivedQuantity(4L);
		entry1.setStatus(ReturnStatus.COMPLETED);
		final ReturnEntryModel entry2 = new ReplacementEntryModel();
		entry2.setReceivedQuantity(3L);
		entry2.setStatus(ReturnStatus.COMPLETED);

		returnEntries = Arrays.asList(entry1, entry2);

		when(returnService.getReturnEntry(anyObject())).thenReturn(returnEntries);

		final long qtyReturned = handler.get(entry);

		assertEquals(0L, qtyReturned);
	}

	@Test
	public void shouldGetZeroWhenNoReturnEntry()
	{
		returnEntries = new ArrayList<>();
		when(returnService.getReturnEntry(anyObject())).thenReturn(returnEntries);

		final long qtyReturned = handler.get(entry);

		assertEquals(0L, qtyReturned);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldNotSupportSet()
	{
		handler.set(entry, 0L);
	}
}

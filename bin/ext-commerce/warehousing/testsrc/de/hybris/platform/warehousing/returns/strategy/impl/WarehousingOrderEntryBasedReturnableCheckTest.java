package de.hybris.platform.warehousing.returns.strategy.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.ReturnEntryModel;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class WarehousingOrderEntryBasedReturnableCheckTest
{
	private final WarehousingOrderEntryBasedReturnableCheck check = new WarehousingOrderEntryBasedReturnableCheck();
	private OrderModel order;

	@Mock
	private OrderEntryModel orderEntryNotInOrder;
	@Mock
	private OrderEntryModel orderEntryInOrder;
	@Mock
	private ReturnService returnService;

	@Before
	public void setUp()
	{
		check.setReturnService(returnService);
		order = new OrderModel();
		order.setEntries(Arrays.asList(orderEntryInOrder));
		when(orderEntryInOrder.getQuantityShipped()).thenReturn(10L);
	}

	@Test
	public void shouldGetFalseWhenReturnQtyBelowOne()
	{
		assertFalse(check.perform(order, orderEntryInOrder, 0));
	}

	@Test
	public void shouldGetFalseWhenOrderEntryNotInOrder()
	{
		when(orderEntryNotInOrder.getQuantityShipped()).thenReturn(10L);
		assertFalse(check.perform(order, orderEntryNotInOrder, 5));
	}

	@Test
	public void shouldGetFalseWhenReturnQtyHigherThanOrderEntry()
	{
		assertFalse(check.perform(order, orderEntryInOrder, 11));
	}

	@Test
	public void shouldGetTrueWhenReturnQtyEqualOrderEntry()
	{
		assertTrue(check.perform(order, orderEntryInOrder, 10));
	}

	@Test
	public void shouldGetTrueWhenReturnQtyLowerThanOrderEntry()
	{
		assertTrue(check.perform(order, orderEntryInOrder, 5));
	}

	@Test
	public void shouldGetLowerQtyWhenIncompleteReturnExists()
	{
		final ReturnEntryModel entry = new ReturnEntryModel();
		entry.setExpectedQuantity(1L);
		entry.setStatus(ReturnStatus.WAIT);

		when(returnService.getReturnEntry(orderEntryInOrder)).thenReturn(Arrays.asList(entry));

		assertFalse(check.perform(order, orderEntryInOrder, 10));
		assertTrue(check.perform(order, orderEntryInOrder, 9));
	}

	@Test
	public void shouldGetLowerQtyWhenCompleteReturnExists()
	{
		when(orderEntryInOrder.getQuantityReturned()).thenReturn(1L);

		assertFalse(check.perform(order, orderEntryInOrder, 10));
		assertTrue(check.perform(order, orderEntryInOrder, 9));
	}

	@Test
	public void shouldGetSameQtyWhenCancelledReturnExists()
	{
		final ReturnEntryModel entry = new ReturnEntryModel();
		entry.setExpectedQuantity(1L);
		entry.setStatus(ReturnStatus.CANCELED);

		when(returnService.getReturnEntry(orderEntryInOrder)).thenReturn(Arrays.asList(entry));

		assertTrue(check.perform(order, orderEntryInOrder, 10));
	}
}

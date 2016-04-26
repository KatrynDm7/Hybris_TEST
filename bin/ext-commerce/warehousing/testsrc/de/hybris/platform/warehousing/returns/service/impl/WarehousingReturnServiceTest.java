package de.hybris.platform.warehousing.returns.service.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class WarehousingReturnServiceTest
{
	private WarehousingReturnService warehousingReturnService;

	private OrderModel order;
	private OrderEntryModel orderEntry;
	private ReturnRequestModel returnRequest;
	private String returnNotes;
	private Long expectedQuantity;
	private ReturnAction action;
	private RefundReason reason;

	@Before
	public void setUp()
	{
		warehousingReturnService = new WarehousingReturnService();
		order = new OrderModel();
		orderEntry = new OrderEntryModel();
		orderEntry.setOrder(order);
		orderEntry.setQuantity(3L);
		returnRequest = new ReturnRequestModel();
		returnRequest.setOrder(order);
		returnNotes = "Item is broken";
		expectedQuantity = 3L;
		action = ReturnAction.IMMEDIATE;
		reason = RefundReason.DAMAGEDINTRANSIT;
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createReturnRequestOrderNull()
	{
		warehousingReturnService.createReturnRequest(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createRefundRequestNull()
	{
		warehousingReturnService.createRefund(null, orderEntry, returnNotes, expectedQuantity, action, reason);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createRefundEntryNull()
	{
		warehousingReturnService.createRefund(returnRequest, null, returnNotes, expectedQuantity, action, reason);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createRefundExpectedQuantityNull()
	{
		warehousingReturnService.createRefund(returnRequest, orderEntry, returnNotes, null, action, reason);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createRefundExpectedQuantityZero()
	{
		expectedQuantity = 0L;
		warehousingReturnService.createRefund(returnRequest, orderEntry, returnNotes, expectedQuantity, action, reason);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createRefundExpectedQuantityNegative()
	{
		expectedQuantity = -1L;
		warehousingReturnService.createRefund(returnRequest, orderEntry, returnNotes, expectedQuantity, action, reason);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createRefundActionNull()
	{
		warehousingReturnService.createRefund(returnRequest, orderEntry, returnNotes, expectedQuantity, null, reason);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_createRefundReasonNull()
	{
		warehousingReturnService.createRefund(returnRequest, orderEntry, returnNotes, expectedQuantity, action, null);
	}

}

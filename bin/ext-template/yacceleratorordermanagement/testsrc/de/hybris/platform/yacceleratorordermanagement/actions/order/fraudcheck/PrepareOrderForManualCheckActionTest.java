/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.yacceleratorordermanagement.actions.order.fraudcheck;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PrepareOrderForManualCheckActionTest {

	private PrepareOrderForManualCheckAction prepareOrderForManualCheck;
	@Mock
	private ModelService modelService;
	@Mock
	private EventService eventService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		prepareOrderForManualCheck = new PrepareOrderForManualCheckAction();
		prepareOrderForManualCheck.setModelService(modelService);
		prepareOrderForManualCheck.setEventService(eventService);
	}

	@Test
	public void testExecute() throws RetryLaterException, Exception {

		final OrderProcessModel orderProcess = new OrderProcessModel();
		final OrderModel order = new OrderModel();
		order.setStatus(OrderStatus.CREATED);
		orderProcess.setOrder(order);
		prepareOrderForManualCheck.executeAction(orderProcess);
		Assert.assertEquals(OrderStatus.WAIT_FRAUD_MANUAL_CHECK, orderProcess.getOrder().getStatus());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testExecuteNullProcess() throws RetryLaterException, Exception {

		prepareOrderForManualCheck.executeAction(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testExecuteNullOrder() throws RetryLaterException, Exception {

		prepareOrderForManualCheck.executeAction(new OrderProcessModel());
	}

}

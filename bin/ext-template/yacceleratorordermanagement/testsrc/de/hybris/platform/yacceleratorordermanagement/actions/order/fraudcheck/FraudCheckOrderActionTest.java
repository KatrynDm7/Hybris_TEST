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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.FraudService;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class FraudCheckOrderActionTest
{
	private AbstractOrderEntryModel orderEntryModel;
	private FraudServiceResponse response;
	private OrderProcessModel orderProcessModel;
	private OrderModel orderModel;
	private FraudReportModel fraudReport;
	private OrderHistoryEntryModel historyEntry;

	@InjectMocks
	private final FraudCheckOrderAction action = new FraudCheckOrderAction();

	@Mock
	private ModelService modelService;
	@Mock
	private FraudService fraudService;
	@Mock
	private TimeService timeService;

	@Before
	public void setup()
	{
		orderEntryModel = spy(new AbstractOrderEntryModel());
		response = spy(new FraudServiceResponse("report", "hybris"));

		fraudReport = new FraudReportModel();
		historyEntry = new OrderHistoryEntryModel();

		final List<AbstractOrderEntryModel> orderEntriesModel = new ArrayList<>();
		orderEntriesModel.add(orderEntryModel);

		orderModel = new OrderModel();
		orderModel.setEntries(orderEntriesModel);

		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(orderModel);

		action.setScoreLimit(500d);
		action.setScoreTolerance(50d);

		when(modelService.create(FraudReportModel.class)).thenReturn(fraudReport);
		when(modelService.create(OrderHistoryEntryModel.class)).thenReturn(historyEntry);
		when(timeService.getCurrentTime()).thenReturn(new Date());
	}

	@Test
	public void fraudCheckShouldOKWhenScoreIsZero() throws Exception
	{
		when(response.getScore()).thenReturn(0d);
		when(fraudService.recognizeOrderSymptoms(anyString(), any(AbstractOrderModel.class))).thenReturn(response);

		final String transition = action.execute(orderProcessModel);
		assertTrue(transition.equals(FraudCheckOrderAction.Transition.OK.toString()));
	}

	@Test
	public void fraudCheckShouldPotentialWhenScoreIsWithinTheToleranceRange() throws Exception
	{
		when(response.getScore()).thenReturn(525d);
		when(fraudService.recognizeOrderSymptoms(anyString(), any(AbstractOrderModel.class))).thenReturn(response);

		final String transition = action.execute(orderProcessModel);
		assertTrue(transition.equals(FraudCheckOrderAction.Transition.POTENTIAL.toString()));
	}

	@Test
	public void fraudCheckShouldFraudWhenScoreIsGreaterThanTheTolerancePlusLimit() throws Exception
	{
		when(response.getScore()).thenReturn(1000d);
		when(fraudService.recognizeOrderSymptoms(anyString(), any(AbstractOrderModel.class))).thenReturn(response);

		final String transition = action.execute(orderProcessModel);
		assertTrue(transition.equals(FraudCheckOrderAction.Transition.FRAUD.toString()));
	}
}

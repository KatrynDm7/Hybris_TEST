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
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.yacceleratorordermanagement.actions.order.AbstractOrderAction;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Process decision from customer support agent to determine if an order is fraudulent or not.
 */
public class OrderManualCheckedAction extends AbstractOrderAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(OrderManualCheckedAction.class);

	protected enum Transition
	{
		OK, NOK;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();
			for (final Transition transitions : Transition.values())
			{
				res.add(transitions.toString());
			}
			return res;
		}
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

	@Override
	public final String execute(final OrderProcessModel process) throws RetryLaterException, Exception
	{
		return executeAction(process).toString();
	}

	protected Transition executeAction(final OrderProcessModel process)
	{
		ServicesUtil.validateParameterNotNull(process, "Process cannot be null");
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final OrderModel order = process.getOrder();
		ServicesUtil.validateParameterNotNull(order, "Order in process cannot be null");
		ServicesUtil.validateParameterNotNull(order.getFraudulent(), "Fraudulent value in order cannot be null");

		final OrderHistoryEntryModel historyLog = createHistoryLog(
				"Order Manually checked by CSA - Fraud = " + order.getFraudulent(), order);
		modelService.save(historyLog);
		
		LOG.info("The fraud condition of the order " + order.getCode() + " is " + order.getFraudulent().booleanValue());
		if (order.getFraudulent().booleanValue())
		{
			order.setStatus(OrderStatus.SUSPENDED);
			getModelService().save(order);
			return Transition.NOK;
		}
		else
		{
			order.setStatus(OrderStatus.FRAUD_CHECKED);
			getModelService().save(order);
			return Transition.OK;
		}
	}
}

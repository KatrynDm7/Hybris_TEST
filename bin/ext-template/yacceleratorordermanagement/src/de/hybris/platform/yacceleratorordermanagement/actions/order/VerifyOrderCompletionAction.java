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
package de.hybris.platform.yacceleratorordermanagement.actions.order;

import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;

import org.apache.log4j.Logger;


/**
 * Verifies whether all consignment processes are complete or not and updates the Order status/delivery status to
 * reflect this.
 */
public class VerifyOrderCompletionAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(VerifyOrderCompletionAction.class);

	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());
		LOG.debug("Process: " + process.getCode() + " is checking for  " + process.getConsignmentProcesses().size()
				+ " subprocess results");

		final OrderModel order = process.getOrder();

		final boolean someEntriesShipped = order.getEntries().stream().anyMatch(entry -> entry.getQuantityShipped() > 0);

		if (!someEntriesShipped)
		{
			order.setDeliveryStatus(DeliveryStatus.NOTSHIPPED);
		}
		else
		{
			final boolean someEntriesWaiting = order.getEntries().stream().anyMatch(entry -> entry.getQuantityPending() > 0);

			if (someEntriesWaiting)
			{
				order.setDeliveryStatus(DeliveryStatus.PARTSHIPPED);
			}
			else
			{
				order.setDeliveryStatus(DeliveryStatus.SHIPPED);
			}
		}

		for (final ConsignmentProcessModel subProcess : process.getConsignmentProcesses())
		{
			if (!subProcess.isDone())
			{
				LOG.debug("Process: " + process.getCode() + " found subprocess " + subProcess.getCode()
						+ " incomplete -> wait again!");
				save(order);
				return Transition.NOK;
			}
			LOG.debug("Process: " + process.getCode() + " found subprocess " + subProcess.getCode() + " complete ...");
		}
		LOG.info("Process: " + process.getCode() + " found all subprocesses completed");

		order.setStatus(OrderStatus.READY);
		save(order);
		return Transition.OK;
	}
}

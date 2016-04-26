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
package de.hybris.platform.yacceleratorordermanagement.actions.order.cancel;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.yacceleratorordermanagement.actions.consignment.VerifyConsignmentCompletionAction;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Check if cancellation is done.
 */
public class VerifyOrderPostCancellationAction extends AbstractAction<OrderProcessModel>
{
	private static Logger LOGGER = LoggerFactory.getLogger(VerifyConsignmentCompletionAction.class);

	protected enum Transition
	{
		OK, NOK, WAIT;

		public static Set<String> getStringValues()
		{
			final Set<String> res = new HashSet<String>();

			for (final Transition transition : Transition.values())
			{
				res.add(transition.toString());
			}
			return res;
		}
	}

	@Override
	public String execute(final OrderProcessModel orderProcessModel) throws Exception
	{
		LOGGER.info("Process: " + orderProcessModel.getCode() + " in step " + getClass().getSimpleName());
		String transition;

		if (orderProcessModel.getOrder().getEntries().stream().anyMatch(entry -> entry.getQuantityPending() > 0))
		{
			transition = Transition.WAIT.toString();
		}
		else if(orderProcessModel.getOrder().getConsignments().stream().anyMatch(consignment -> !consignment.getStatus().equals(ConsignmentStatus.CANCELLED)))
		{
			transition = Transition.NOK.toString();
		}
		else
		{
			transition = transitionOK(orderProcessModel);
		}
		return transition;
	}

	protected String transitionOK(final OrderProcessModel orderProcessModel)
	{

		final OrderModel order = orderProcessModel.getOrder();
		order.setStatus(OrderStatus.CANCELLED);

		getModelService().save(order);

		return Transition.OK.toString();
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}

}

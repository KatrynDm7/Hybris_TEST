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

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.cancellation.OrderCancellationService;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Cancel the order. This order cancellation assumes that no items have been allocated and so no consignment exist for
 * this order yet.
 */
public class CancelOrderAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CancelOrderAction.class);

	private OrderCancellationService orderCancellationService;

	@Override
	public void executeAction(final OrderProcessModel orderProcess) throws RetryLaterException, Exception
	{
		ServicesUtil.validateParameterNotNull(orderProcess, "Process can not be null");
		LOG.info("Process: " + orderProcess.getCode() + " in step " + getClass().getSimpleName());

		final OrderModel order = orderProcess.getOrder();
		ServicesUtil.validateParameterNotNull(order, "Order can not be null");

		getOrderCancellationService().cancelOrder(order, createCancellationEntries(order));
		order.setStatus(OrderStatus.CANCELLED);
		getModelService().save(order);
	}

	/**
	 * Create a collection of {@link CancellationEntry} from the list of order entries in the order.
	 *
	 * @param order
	 *           - the order
	 * @return collection of cancellation entries; never <tt>null</tt>
	 */
	public Collection<CancellationEntry> createCancellationEntries(final OrderModel order)
	{
		return order.getEntries().stream().map(entry -> createCancellationEntry(entry)).collect(Collectors.toList());
	}

	/**
	 * Create a {@link CancellationEntry} from an {@link AbstractOrderEntryModel}.
	 *
	 * @param orderEntry
	 *           - the order entry
	 * @return a new cancellation entry
	 */
	protected CancellationEntry createCancellationEntry(final AbstractOrderEntryModel orderEntry)
	{
		final CancellationEntry entry = new CancellationEntry();
		entry.setOrderEntry(orderEntry);
		entry.setReason(CancelReason.OTHER.getCode());
		entry.setQuantity(orderEntry.getQuantityPending());
		return entry;
	}

	protected OrderCancellationService getOrderCancellationService()
	{
		return orderCancellationService;
	}

	@Required
	public void setOrderCancellationService(final OrderCancellationService cancellationService)
	{
		this.orderCancellationService = cancellationService;
	}

}

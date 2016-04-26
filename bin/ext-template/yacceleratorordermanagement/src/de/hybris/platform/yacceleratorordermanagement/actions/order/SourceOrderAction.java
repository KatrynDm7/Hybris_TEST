/*
 *
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
 */
package de.hybris.platform.yacceleratorordermanagement.actions.order;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.warehousing.allocation.AllocationService;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;

import java.util.Collection;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Action node responsible for sourcing the order and allocating the consignments. After the consignments are created,
 * the consignment sub-process is started for every consignment.
 */
public class SourceOrderAction extends AbstractProceduralAction<OrderProcessModel>
{

	private static final Logger LOGGER = Logger.getLogger(SourceOrderAction.class);

	private SourcingService sourcingService;
	private AllocationService allocationService;
	private BusinessProcessService businessProcessService;

	@Override
	public void executeAction(final OrderProcessModel process) throws RetryLaterException, Exception
	{
		LOGGER.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		final OrderModel order = process.getOrder();

		final SourcingResults results = getSourcingService().sourceOrder(order);
		if (results != null)
		{
			results.getResults().forEach(result -> logSourcingInfo(result));
		}

		if (results == null || !results.isComplete())
		{
			LOGGER.info("Order failed to be sourced");
			order.setStatus(OrderStatus.ON_HOLD_SOURCING_FAILED);
		}
		else
		{
			LOGGER.info("Order was successfully sourced");

			final Collection<ConsignmentModel> consignments = getAllocationService().createConsignments(process.getOrder(),
					"cons" + process.getOrder().getCode(), results);
			LOGGER.debug("Number of consignments created during allocation: " + consignments.size());
			startConsignmentSubProcess(consignments, process);
			order.setStatus(OrderStatus.READY);
		}
		getModelService().save(order);
	}

	/**
	 * Create and start a consignment process for each consignment in the collection.
	 *
	 * @param consignments
	 *           - list of consignments; never <tt>null</tt>
	 * @param process
	 *           - order process model
	 */
	protected void startConsignmentSubProcess(final Collection<ConsignmentModel> consignments, final OrderProcessModel process)
	{
		for (final ConsignmentModel consignment : consignments)
		{
			final ConsignmentProcessModel subProcess = getBusinessProcessService().<ConsignmentProcessModel> createProcess(
					consignment.getCode() + "_ordermanagement", YAcceleratorOrderManagementConstants.CONSIGNMENT_SUBPROCESS_NAME);
			subProcess.setParentProcess(process);
			subProcess.setConsignment(consignment);
			save(subProcess);
			LOGGER.info("Start Consignment sub-process: '" + subProcess.getCode() + "'");
			getBusinessProcessService().startProcess(subProcess);
		}
	}

	private void logSourcingInfo(final SourcingResult result)
	{
		if (result != null)
		{
			LOGGER.info("Sourcing from Location: '" + result.getWarehouse().getCode() + "'");
			result.getAllocation().forEach(
					(product, qty) -> LOGGER.info("\tProduct [" + product.getProduct().getCode() + "]: '"
							+ product.getProduct().getName(getSessionLocale()) + "'\tQuantity: '" + qty + "'"));
		}
		else
		{
			LOGGER.info("The sourcing result is null");
		}
	}

	protected Locale getSessionLocale()
	{
		return JaloSession.getCurrentSession().getSessionContext().getLocale();
	}

	protected SourcingService getSourcingService()
	{
		return sourcingService;
	}

	@Required
	public void setSourcingService(final SourcingService sourcingService)
	{
		this.sourcingService = sourcingService;
	}

	protected AllocationService getAllocationService()
	{
		return allocationService;
	}

	@Required
	public void setAllocationService(final AllocationService allocationService)
	{
		this.allocationService = allocationService;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

}

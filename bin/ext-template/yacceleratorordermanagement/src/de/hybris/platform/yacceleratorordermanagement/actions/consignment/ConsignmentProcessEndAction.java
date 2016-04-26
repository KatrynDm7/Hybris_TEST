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
package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Update the consignment process to done and notify the corresponding order process that it is completed.
 */
public class ConsignmentProcessEndAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(ConsignmentProcessEndAction.class);

	private BusinessProcessService businessProcessService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());

		process.setDone(true);
		save(process);
		LOG.debug("Process: " + process.getCode() + " wrote DONE marker");

		final String eventId = process.getParentProcess().getCode() + "_"
				+ YAcceleratorOrderManagementConstants.ORDER_ACTION_EVENT_NAME;

		final BusinessProcessEvent event = BusinessProcessEvent.builder(eventId).withChoice("consignmentProcessEnded").build();
		getBusinessProcessService().triggerEvent(event);

		LOG.debug("Process: " + process.getCode() + " fired event "
				+ YAcceleratorOrderManagementConstants.ORDER_ACTION_EVENT_NAME);
	}
}

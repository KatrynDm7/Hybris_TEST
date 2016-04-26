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
package de.hybris.platform.b2bacceleratoraddon.actions;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.b2bacceleratorservices.event.OrderPendingApprovalEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;


/**
 * Sends Order Pending Approval Notification event.
 */
public class SendOrderPendingApprovalNotification extends AbstractProceduralAction<OrderProcessModel>
{
	private EventService eventService;

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		getEventService().publishEvent(new OrderPendingApprovalEvent(process));
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected EventService getEventService()
	{
		return eventService;
	}
}

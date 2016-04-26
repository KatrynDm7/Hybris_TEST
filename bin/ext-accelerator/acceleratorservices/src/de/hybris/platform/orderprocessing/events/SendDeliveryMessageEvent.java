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
package de.hybris.platform.orderprocessing.events;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;


public class SendDeliveryMessageEvent extends ConsignmentProcessingEvent
{
	private static final long serialVersionUID = -8586595518929550780L;

	public SendDeliveryMessageEvent(final ConsignmentProcessModel process)
	{
		super(process);
	}
}

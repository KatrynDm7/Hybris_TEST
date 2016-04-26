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


import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;

import org.apache.log4j.Logger;


/**
 * Updates a consignment status to a given status.
 */
public class UpdateConsignmentAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(UpdateConsignmentAction.class);

	private ConsignmentModel consignment;
	private ConsignmentStatus status;

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());
		consignment = process.getConsignment();
		consignment.setStatus(status);
		save(consignment);
	}

	protected ConsignmentStatus getStatus() {
		return status;
	}

	public void setStatus(ConsignmentStatus status) {
		this.status = status;
	}

}

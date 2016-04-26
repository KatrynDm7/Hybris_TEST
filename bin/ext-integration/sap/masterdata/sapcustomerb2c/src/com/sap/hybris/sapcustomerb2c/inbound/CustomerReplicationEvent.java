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
package com.sap.hybris.sapcustomerb2c.inbound;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * This class is to rise up customer ERP replication events
 */
public class CustomerReplicationEvent extends AbstractEvent implements ClusterAwareEvent
{

	private static final long serialVersionUID = -3839203917371395971L;
	private final String customerID;

	/**
	 * @param customerID
	 */
	public CustomerReplicationEvent(final String customerID)
	{
		super();
		this.customerID = customerID;
	}

	/**
	 * @return customer ID
	 */
	public String getCustomerID()
	{
		return customerID;
	}

	@Override
	public boolean publish(final int sourceNodeId, final int targetNodeId)
	{
		return true;
	}

}

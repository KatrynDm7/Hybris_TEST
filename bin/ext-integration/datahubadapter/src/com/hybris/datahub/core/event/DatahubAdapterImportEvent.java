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
 */

package com.hybris.datahub.core.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import com.hybris.datahub.core.facades.ItemImportResult;

@SuppressWarnings("serial")
public class DatahubAdapterImportEvent extends AbstractEvent
{

	private String poolName;
	private ItemImportResult.DatahubAdapterEventStatus status;

	public DatahubAdapterImportEvent(final String poolName, final ItemImportResult.DatahubAdapterEventStatus status)
	{
		super();
		this.poolName = poolName;
		this.status = status;
	}

	public String getPoolName()
	{
		return poolName;
	}

	public ItemImportResult.DatahubAdapterEventStatus getStatus()
	{
		return status;
	}
}

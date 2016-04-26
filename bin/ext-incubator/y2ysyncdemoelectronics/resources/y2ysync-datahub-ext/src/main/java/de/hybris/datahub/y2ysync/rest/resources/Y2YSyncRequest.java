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

package de.hybris.datahub.y2ysync.rest.resources;

import java.util.List;


public class Y2YSyncRequest
{
	private String syncExecutionId;
	private List<DataStream> dataStreams;

	public String getSyncExecutionId()
	{
		return syncExecutionId;
	}

	public void setSyncExecutionId(final String syncExecutionId)
	{
		this.syncExecutionId = syncExecutionId;
	}

	public List<DataStream> getDataStreams()
	{
		return dataStreams;
	}

	public void setDataStreams(final List<DataStream> dataStreams)
	{
		this.dataStreams = dataStreams;
	}
}

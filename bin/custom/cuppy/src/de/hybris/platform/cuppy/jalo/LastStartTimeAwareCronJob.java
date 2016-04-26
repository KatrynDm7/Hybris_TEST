/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.cuppy.jalo;

import java.util.Date;

import org.apache.log4j.Logger;


public class LastStartTimeAwareCronJob extends GeneratedLastStartTimeAwareCronJob
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(LastStartTimeAwareCronJob.class.getName());

	@Override
	public void setStartTime(final Date start)
	{
		final Date lastStartTime = getStartTime();
		if (lastStartTime != null)
		{
			setLastStartTime(lastStartTime);
		}
		super.setStartTime(start);
	}

}

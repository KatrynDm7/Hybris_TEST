/*
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
 *
 */
package de.hybris.platform.cuppy.services.impl;

/**
 * Important data for match reminder.
 */
public class MatchReminderHelper
{
	private final String name;
	private final String kickOffTime;
	private final String home;
	private final String guest;

	public MatchReminderHelper(final String name, final String kickOffTime, final String home, final String guest)
	{
		this.name = name;
		this.kickOffTime = kickOffTime;
		this.home = home;
		this.guest = guest;
	}

	public String getName()
	{
		return name;
	}

	public String getKickOffTime()
	{
		return kickOffTime;
	}

	public String getHome()
	{
		return home;
	}

	public String getGuest()
	{
		return guest;
	}

}

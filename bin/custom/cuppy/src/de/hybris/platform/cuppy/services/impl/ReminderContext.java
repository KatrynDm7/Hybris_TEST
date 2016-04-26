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

import de.hybris.platform.cuppy.model.PlayerModel;

import java.util.List;


/**
 * Tip context for reminder.
 */
public class ReminderContext
{

	private final PlayerModel player;
	private final List<MatchReminderHelper> reminderMatches;

	public ReminderContext(final PlayerModel player, final List<MatchReminderHelper> reminderMatches)
	{
		this.player = player;
		this.reminderMatches = reminderMatches;
	}

	public PlayerModel getPlayer()
	{
		return this.player;
	}

	public List<MatchReminderHelper> getReminderMatches()
	{
		return reminderMatches;
	}

}

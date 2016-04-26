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
import de.hybris.platform.cuppy.services.RankingData;

import java.util.List;


/**
 *
 */
public class RankingMailContext
{
	private final List<RankingData> rankings;
	private final PlayerModel player;

	public RankingMailContext(final List<RankingData> rankings, final PlayerModel player)
	{
		super();

		this.rankings = rankings;
		this.player = player;
	}

	public List<RankingData> getRankings()
	{
		return rankings;
	}

	public PlayerModel getPlayer()
	{
		return player;
	}
}

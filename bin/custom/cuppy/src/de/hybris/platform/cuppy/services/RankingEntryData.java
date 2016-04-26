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
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.PlayerModel;


/**
 * DTO containing the ranking of a player
 */
public class RankingEntryData
{
	private PlayerModel player;
	private int rank;
	private int score;
	private int lastRank;
	private int lastScore;

	public PlayerModel getPlayer()
	{
		return player;
	}

	public void setPlayer(final PlayerModel player)
	{
		this.player = player;
	}

	public int getRank()
	{
		return rank;
	}

	public void setRank(final int rank)
	{
		this.rank = rank;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(final int score)
	{
		this.score = score;
	}

	public int getLastRank()
	{
		return lastRank;
	}

	public void setLastRank(final int lastRank)
	{
		this.lastRank = lastRank;
	}

	public int getLastScore()
	{
		return lastScore;
	}

	public void setLastScore(final int lastScore)
	{
		this.lastScore = lastScore;
	}
}

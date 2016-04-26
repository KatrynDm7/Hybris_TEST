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

import de.hybris.platform.cuppy.model.CompetitionModel;

import java.util.List;


/**
 * DTO containing a sorted list of rankings in a competition for a match day
 */
public class RankingData
{
	private CompetitionModel competition;
	private List<RankingEntryData> entries;
	private int matchday;
	private int lastMatchday;

	public CompetitionModel getCompetition()
	{
		return competition;
	}

	public void setCompetition(final CompetitionModel competition)
	{
		this.competition = competition;
	}

	public List<RankingEntryData> getEntries()
	{
		return entries;
	}

	public void setEntries(final List<RankingEntryData> entries)
	{
		this.entries = entries;
	}

	@Override
	public String toString()
	{
		return competition.getCode();
	}

	public int getMatchday()
	{
		return matchday;
	}

	public void setMatchday(final int matchday)
	{
		this.matchday = matchday;
	}

	public int getLastMatchday()
	{
		return lastMatchday;
	}

	public void setLastMatchday(final int lastMatchday)
	{
		this.lastMatchday = lastMatchday;
	}
}

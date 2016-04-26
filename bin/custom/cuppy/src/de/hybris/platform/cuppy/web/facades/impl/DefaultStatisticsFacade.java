/**
 * 
 */
package de.hybris.platform.cuppy.web.facades.impl;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.TimePointStatisticModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.StatisticsService;
import de.hybris.platform.cuppy.web.data.MatchStatisticData;
import de.hybris.platform.cuppy.web.data.OverallStatisticData;
import de.hybris.platform.cuppy.web.data.PlayerStatisticData;
import de.hybris.platform.cuppy.web.data.TimepointStatisticData;
import de.hybris.platform.cuppy.web.facades.MatchFacade;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;
import de.hybris.platform.cuppy.web.facades.StatisticsFacade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "statisticsFacade")
public class DefaultStatisticsFacade implements StatisticsFacade
{
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private PlayerFacade playerFacade;
	@Autowired
	private MatchService matchService;
	@Autowired
	private MatchFacade matchFacade;
	@Autowired
	private CompetitionService competitionService;

	@Override
	public PlayerStatisticData getRandomPlayerStatistic()
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		final PlayerModel player = playerService.getRandomPlayer(compModel);
		if (player == null)
		{
			return null;
		}
		return convertToPlayer(player);
	}

	@Override
	public MatchStatisticData getNextBetableMatchStatistic()
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		final MatchModel match = matchService.getNextBetableMatch(compModel);
		if (match == null)
		{
			return null;
		}
		return convertToMatch(match);
	}

	@Override
	public MatchStatisticData getRandomMatchStatistic()
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		final MatchModel match = matchService.getRandomMatch(compModel);
		if (match == null)
		{
			return null;
		}
		return convertToMatch(match);
	}

	@Override
	public OverallStatisticData getOverallStatistic()
	{
		final CompetitionModel currentCompetition = competitionService.getCurrentCompetition();

		final OverallStatisticData result = new OverallStatisticData();
		result.setPlayersCountOverall(statisticsService.getPlayersCount());
		result.setPlayersCount(statisticsService.getPlayersCount(currentCompetition));
		result.setPlayersOnlineCount(statisticsService.getCurrentPlayersCount());
		result.setPlayersOnlineMaxCount(statisticsService.getPlayersOnlineMaxCount());
		//TODO: performance problem
		result.setAverageScore(0);
		//result.setAverageScore(statisticsService.getAverageScoreForAllPlayers());
		return result;
	}

	private MatchStatisticData convertToMatch(final MatchModel match)
	{
		final MatchStatisticData result = new MatchStatisticData();
		result.setMatch(matchFacade.getMatch(match.getId()));
		result.setTimeToBet(matchService.getTimeToBet(match));
		result.setPlayersNotPlacedBetsCount(statisticsService.getPlayersNotPlacedBetsForMatchTotal(match));
		result.setPlayersNotPlacedBetsPerc(statisticsService.getPlayersNotPlacedBetsForMatchPerc(match));
		result.setAverageScore(statisticsService.getAverageScoreForMatch(match));
		return result;
	}

	private PlayerStatisticData convertToPlayer(final PlayerModel player)
	{
		final PlayerStatisticData result = new PlayerStatisticData();
		result.setPlayer(playerFacade.getRanking(player.getUid()));
		return result;
	}

	@Override
	public List<TimepointStatisticData> getTimepointStatistics(final long since)
	{
		return convertToTimepoints(statisticsService.getTimePointStatistics(since));
	}

	@Override
	public List<TimepointStatisticData> getTimepointStatistics(final long sinceFrom, final long sinceTill)
	{
		return convertToTimepoints(statisticsService.getTimePointStatistics(sinceFrom, sinceTill));
	}

	private List<TimepointStatisticData> convertToTimepoints(final List<TimePointStatisticModel> stats)
	{
		final List<TimepointStatisticData> result = new ArrayList<TimepointStatisticData>();
		for (final TimePointStatisticModel stat : stats)
		{
			final TimepointStatisticData data = new TimepointStatisticData();
			data.setDate(stat.getCreationtime());
			data.setPlayerOnline(stat.getPlayersOnlineCount());
			result.add(data);
		}
		return result;
	}

	public void setStatisticsService(final StatisticsService statisticsService)
	{
		this.statisticsService = statisticsService;
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}

	public void setPlayerFacade(final PlayerFacade playerFacade)
	{
		this.playerFacade = playerFacade;
	}

	public void setMatchService(final MatchService matchService)
	{
		this.matchService = matchService;
	}

	public void setMatchFacade(final MatchFacade matchFacade)
	{
		this.matchFacade = matchFacade;
	}

	public void setCompetitionService(final CompetitionService competitionService)
	{
		this.competitionService = competitionService;
	}
}

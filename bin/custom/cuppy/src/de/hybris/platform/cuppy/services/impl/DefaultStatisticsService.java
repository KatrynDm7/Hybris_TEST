/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.daos.StatisticsDao;
import de.hybris.platform.cuppy.jalo.Player;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.OverallStatisticModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.TimePointStatisticModel;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.StatisticsService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "statisticsService")
public class DefaultStatisticsService implements StatisticsService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultStatisticsService.class);

	@Autowired
	private StatisticsDao statisticsDao;

	@Autowired
	private MatchService matchService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private ModelService modelService;

	@Override
	public int getCurrentPlayersCount()
	{
		final Set<User> currentPlayers = new LinkedHashSet<User>();
		for (final JaloSession session : JaloConnection.getInstance().getAllSessions())
		{
			final SessionContext ctx = session.getSessionContext();
			if (ctx != null)
			{
				final User user = session.getSessionContext().getUser();
				if (user instanceof Player && ((Player) user).isConfirmedAsPrimitive())
				{
					currentPlayers.add(user);
				}
			}
		}
		return currentPlayers.size();
	}

	@Override
	public int getPlayersCount()
	{
		return statisticsDao.getPlayersCount();
	}

	@Override
	public int getPlayersCount(final CompetitionModel competition)
	{
		return statisticsDao.getPlayersCount(competition);
	}

	@Override
	public double getAverageScoreForAllPlayers()
	{
		double scoreSum = 0;
		final List<PlayerModel> players = playerService.getAllPlayers();
		if (players.isEmpty())
		{
			return 0;
		}
		for (final PlayerModel player : players)
		{
			scoreSum = scoreSum + getAverageScoreForPlayer(player);
		}
		return scoreSum / players.size();
	}

	@Override
	public double getAverageScoreForMatch(final MatchModel match)
	{
		if (!matchService.isMatchFinished(match))
		{
			return 0;
		}
		final List<MatchBetModel> bets = matchService.getBets(match);
		if (bets.isEmpty())
		{
			return 0;
		}
		int scoreSum = 0;
		for (final MatchBetModel bet : bets)
		{
			scoreSum = scoreSum + matchService.getScore(bet);
		}
		return (double) scoreSum / (double) bets.size();
	}

	@Override
	public double getAverageScoreForPlayer(final PlayerModel player)
	{
		int score = 0;
		final List<MatchBetModel> bets = new ArrayList<MatchBetModel>();
		for (final CompetitionModel comp : player.getCompetitions())
		{
			bets.addAll(matchService.getFinishedBets(comp, player));
			score = score + matchService.getScore(comp, player);
		}
		if (bets.isEmpty())
		{
			return 0;
		}
		return (double) score / (double) bets.size();
	}

	@Override
	public int getPlayersNotPlacedBetsForMatchPerc(final MatchModel match)
	{
		final int players = statisticsDao.getPlayersCount(match.getGroup().getCompetition());
		if (players == 0)
		{
			return 100;
		}
		final int playersNoBet = getPlayersNotPlacedBetsForMatchTotal(match);
		return playersNoBet * 100 / players;
	}

	@Override
	public int getPlayersNotPlacedBetsForMatchTotal(final MatchModel match)
	{
		return statisticsDao.getPlayersNotPlacedBetsForMatchCount(match);
	}

	@Override
	public double getProbabilityForCorrectBet()
	{
		return 0;
	}

	@Override
	public void updateTimpointStatistic()
	{
		final TimePointStatisticModel stats = modelService.create(TimePointStatisticModel.class);
		stats.setPlayersOnlineCount(getCurrentPlayersCount());
		modelService.save(stats);

		final OverallStatisticModel overallStats = statisticsDao.findOverallStatistics();
		if (stats.getPlayersOnlineCount() > overallStats.getPlayersOnlineMaxCount())
		{
			overallStats.setPlayersOnlineMaxCount(stats.getPlayersOnlineCount());
			modelService.save(overallStats);
		}
	}

	@Override
	public void cleanUpTimepointStatistics()
	{
		final long time = 7 * 24 * 60 * 60 * 1000;
		final List<TimePointStatisticModel> result = statisticsDao.findOutdatedTimePointStatistics(new Date(new Date().getTime()
				- time));
		for (final TimePointStatisticModel stat : result)
		{
			modelService.remove(stat);
		}
	}

	@Override
	public int getPlayersOnlineMaxCount()
	{
		return statisticsDao.findOverallStatistics().getPlayersOnlineMaxCount();
	}

	@Override
	public Map<Date, Integer> getPlayersOnlineCount()
	{
		final Map<Date, Integer> result = new LinkedHashMap<Date, Integer>();
		for (final TimePointStatisticModel statistic : statisticsDao.findTimePointStatistics())
		{
			result.put(statistic.getCreationtime(), Integer.valueOf(statistic.getPlayersOnlineCount()));
		}
		return result;
	}

	@Override
	public List<TimePointStatisticModel> getTimePointStatistics(final long since)
	{
		return statisticsDao.findLastTimePointStatistics(new Date(new Date().getTime() - since));
	}

	@Override
	public List<TimePointStatisticModel> getTimePointStatistics(final long sinceFrom, final long sinceTill)
	{
		return statisticsDao.findLastTimePointStatistics(new Date(new Date().getTime() - sinceFrom), new Date(new Date().getTime()
				- sinceTill));
	}
}

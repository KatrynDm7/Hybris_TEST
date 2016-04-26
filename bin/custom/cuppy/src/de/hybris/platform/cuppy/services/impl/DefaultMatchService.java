/**
 *
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.daos.GroupDao;
import de.hybris.platform.cuppy.daos.MatchBetDao;
import de.hybris.platform.cuppy.daos.MatchDao;
import de.hybris.platform.cuppy.daos.NewsDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.RandomizeStrategy;
import de.hybris.platform.cuppy.services.ScoreStrategy;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "matchService")
public class DefaultMatchService implements MatchService
{
	private static final Logger LOG = Logger.getLogger(DefaultMatchService.class);

	@Autowired
	private MatchDao matchDao;
	@Autowired
	private MatchBetDao matchBetDao;
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private NewsDao newsDao;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ScoreStrategy scoreStrategy;
	@Autowired
	private RandomizeStrategy randomizeStrategy;
	@Autowired
	private I18NService i18nService;

	@Override
	public GroupModel getGroup(final CompetitionModel competition, final String code)
	{
		final List<GroupModel> result = groupDao.findGroupByCode(competition, code);

		if (result.isEmpty())
		{
			throw new UnknownIdentifierException("Group with code '" + code + "' not found!");
		}
		else if (result.size() > 1)
		{
			throw new AmbiguousIdentifierException("Group code '" + code + "' is not unique, " + result.size() + " groups found!");
		}
		return result.get(0);
	}

	@Override
	public MatchModel getMatch(final CompetitionModel competition, final int id)
	{
		final List<MatchModel> result = matchDao.findMatchById(competition, id);
		if (result.isEmpty())
		{
			throw new UnknownIdentifierException("Match with id '" + id + "' not found!");
		}
		else if (result.size() > 1)
		{
			throw new AmbiguousIdentifierException("Match id '" + id + "' is not unique, " + result.size() + " matches found!");
		}
		return result.get(0);
	}

	@Override
	public List<MatchModel> getMatches(final CompetitionModel competition)
	{
		return matchDao.findMatches(competition);
	}

	@Override
	public List<MatchModel> getMatches(final GroupModel group)
	{
		return matchDao.findMatchesByGroup(group);
	}

	@Override
	public List<GroupModel> getGroups(final CompetitionModel competition)
	{
		return groupDao.findGroups(competition);
	}

	@Override
	public void placeBet(final MatchBetModel bet)
	{
		if (isBetable(bet.getMatch()))
		{
			modelService.save(bet);
		}
		else
		{
			LOG.error("Player " + bet.getPlayer().getUid() + " tried to place bet on unbetable match " + bet.getMatch().getId()
					+ ". Will ignore bet.");
		}
	}

	@Override
	public MatchBetModel getBet(final MatchModel match, final PlayerModel player)
	{
		final List<MatchBetModel> result = matchBetDao.findMatchBetByPlayerAndMatch(player, match);
		if (result.isEmpty())
		{
			return null;
		}
		else if (result.size() > 1)
		{
			throw new AmbiguousIdentifierException("MatchBet with match '" + match + "' and player '" + player + "' is not unique, "
					+ result.size() + " match bets found!");
		}
		return result.get(0);
	}

	@Override
	public List<MatchBetModel> getBets(final MatchModel match)
	{
		return matchBetDao.findMatchBetByMatch(match);
	}

	@Override
	public List<NewsModel> getLatestNews(final PlayerModel player, final int count)
	{
		return newsDao.findNews(player, 0, count);
	}

	@Override
	public MatchBetModel getOrCreateBet(final MatchModel match, final PlayerModel player)
	{
		MatchBetModel bet = getBet(match, player);
		if (bet == null)
		{
			bet = modelService.create(MatchBetModel.class);
			bet.setMatch(match);
			bet.setPlayer(player);
		}
		return bet;
	}

	@Override
	public boolean isBetable(final MatchModel match)
	{
		if (isDummyMatch(match))
		{
			return false;
		}
		if (getTimeToBet(match) == 0)
		{
			return false;
		}
		if (resultsPartlyAvailable(match))
		{
			return false;
		}
		return true;
	}

	private boolean isDummyMatch(final MatchModel match)
	{
		return match.getHomeTeam().isDummy() || match.getGuestTeam().isDummy();
	}

	@Override
	public boolean hasBet(final MatchModel match, final PlayerModel player)
	{
		return getBet(match, player) != null;
	}

	@Override
	public boolean isMatchFinished(final MatchModel match)
	{
		// finished when results are available
		return resultsCompletelyAvailable(match) && match.getDate().before(new Date());
	}

	@Override
	public MatchModel getNextBetableMatch(final CompetitionModel competition)
	{
		final List<MatchModel> matches = getMatches(competition);
		for (final MatchModel match : matches)
		{
			if (isBetable(match))
			{
				return match;
			}
		}
		return null;
	}

	@Override
	public MatchModel getRandomMatch(final CompetitionModel competition)
	{
		final List<MatchModel> matches = new ArrayList<MatchModel>(getMatches(competition));
		for (final Iterator<MatchModel> iter = matches.iterator(); iter.hasNext();)
		{
			final MatchModel match = iter.next();
			if (isDummyMatch(match))
			{
				iter.remove();
			}
		}
		if (matches.isEmpty())
		{
			return null;
		}
		return matches.get(randomizeStrategy.getNext(0, matches.size()));
	}

	@Override
	public long getTimeToBet(final MatchModel match)
	{
		final long timeToBet = new Date(match.getDate().getTime() - (3600 * 1000)).getTime() - new Date().getTime();
		if (timeToBet < 0)
		{
			return 0;
		}
		else
		{
			return timeToBet;
		}
	}

	@Override
	public int getScore(final CompetitionModel competition, final PlayerModel player)
	{
		final List<MatchBetModel> bets = matchBetDao.findFinishedMatchBetsByPlayer(competition, player);
		int result = 0;
		for (final MatchBetModel bet : bets)
		{
			result = result + getScore(bet);
		}
		return result;
	}

	@Override
	public int getScoreWithoutCurrentMatchday(final CompetitionModel competition, final PlayerModel player)
	{
		final List<MatchModel> matches = matchDao.findFinishedMatches(competition);
		if (matches.isEmpty())
		{
			return 0;
		}
		final Integer currentMatchday = getCurrentCompletedMatchday(competition);

		final List<MatchBetModel> bets = matchBetDao.findFinishedMatchBetsByPlayer(competition, player);
		int result = 0;
		for (final MatchBetModel bet : bets)
		{
			if (currentMatchday == null || bet.getMatch().getMatchday() < currentMatchday.intValue())
			{
				result = result + getScore(bet);
			}
		}
		return result;
	}

	@Override
	public List<MatchBetModel> getBets(final CompetitionModel competition, final PlayerModel player)
	{
		return matchBetDao.findMatchBetsByPlayer(competition, player);
	}

	@Override
	public List<MatchBetModel> getFinishedBets(final CompetitionModel competition, final PlayerModel player)
	{
		return matchBetDao.findFinishedMatchBetsByPlayer(competition, player);
	}

	@Override
	public int getScore(final MatchBetModel bet)
	{
		final MatchModel match = bet.getMatch();

		if (isMatchFinished(match))
		{
			final int homeMatch = match.getHomeGoals().intValue();
			final int guestMatch = match.getGuestGoals().intValue();

			final int homeBet = bet.getHomeGoals();
			final int guestBet = bet.getGuestGoals();

			final float multiplier = match.getGroup().getMultiplier();

			return scoreStrategy.getScore(homeMatch, homeBet, guestMatch, guestBet, multiplier);
		}
		else
		{
			throw new IllegalStateException("The match " + match + " of bet " + bet + " has no result yet");
		}
	}

	@Override
	public Integer getCurrentMatchday(final CompetitionModel competition)
	{
		return matchDao.getMatchdayByDate(competition, DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
	}

	@Override
	public Integer getCurrentCompletedMatchday(final CompetitionModel competition)
	{
		final List<Integer> matchdays = getMatchdays(competition);
		if (matchdays.isEmpty())
		{
			return null;
		}
		final Integer currentMatchday = getCurrentMatchday(competition);
		if (currentMatchday == null)
		{
			return matchdays.get(matchdays.size() - 1);
		}
		if (matchDao.findFinishedMatchesForMatchday(competition, currentMatchday.intValue()).size() > 0)
		{
			return currentMatchday;
		}
		for (int i = 0; i < matchdays.size(); i++)
		{
			if (matchdays.get(i).equals(currentMatchday))
			{
				if (i > 0)
				{
					return matchdays.get(i - 1);
				}
				else
				{
					break;
				}
			}
		}
		return null;
	}

	@Override
	public Integer getLastCompletedMatchday(final CompetitionModel competition)
	{
		final List<Integer> matchdays = getMatchdays(competition);
		if (matchdays.isEmpty())
		{
			return null;
		}
		final Integer currentMatchday = getCurrentCompletedMatchday(competition);
		if (currentMatchday == null && matchdays.size() == 1)
		{
			return null;
		}
		if (currentMatchday == null)
		{
			return matchdays.get(matchdays.size() - 1);
		}
		for (int i = 0; i < matchdays.size(); i++)
		{
			if (matchdays.get(i).equals(currentMatchday))
			{
				if (i > 0)
				{
					return matchdays.get(i - 1);
				}
				else
				{
					break;
				}
			}
		}
		return null;
	}

	@Override
	public List<MatchModel> getMatches(final CompetitionModel competition, final int matchday)
	{
		return matchDao.findMatches(competition, matchday);
	}

	@Override
	public List<Integer> getMatchdays(final CompetitionModel competition)
	{
		return matchDao.findMatchdays(competition);
	}

	@Override
	public MatchModel getMatchBefore(final CompetitionModel competition, final Date date)
	{
		final List<MatchModel> matches = matchDao.findMatchBefore(competition, date);
		if (matches.isEmpty())
		{
			return null;
		}
		return matches.iterator().next();
	}

	@Override
	public List<MatchModel> getTodayMatches(final PlayerModel player)
	{
		final Calendar todayCalendar = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		todayCalendar.set(Calendar.HOUR, 0);
		todayCalendar.set(Calendar.MINUTE, 0);
		todayCalendar.set(Calendar.SECOND, 0);
		todayCalendar.set(Calendar.MILLISECOND, 0);

		final Calendar tomorrowCalendar = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
		tomorrowCalendar.setTime(new Date(todayCalendar.getTime().getTime() + (24 * 60 * 60 * 1000)));
		tomorrowCalendar.set(Calendar.HOUR, 0);
		tomorrowCalendar.set(Calendar.MINUTE, 0);
		tomorrowCalendar.set(Calendar.SECOND, 0);
		tomorrowCalendar.set(Calendar.MILLISECOND, 0);
		return matchDao.getMatchesBetween(todayCalendar.getTime(), tomorrowCalendar.getTime());
	}

	@Override
	public boolean isLastCompletedMatchdayModifiedAfter(final CompetitionModel competition, final Date date)
	{
		final Integer lastMatchday = getLastCompletedMatchday(competition);
		if (lastMatchday == null)
		{
			return false;
		}
		final Date modificationTime = matchDao.getModificationTimeByMatchday(competition, lastMatchday.intValue());
		if (modificationTime == null)
		{
			return false;
		}
		return date.before(modificationTime);
	}



	private boolean resultsCompletelyAvailable(final MatchModel match)
	{
		return match.getHomeGoals() != null && match.getGuestGoals() != null;
	}

	private boolean resultsPartlyAvailable(final MatchModel match)
	{
		return match.getHomeGoals() != null || match.getGuestGoals() != null;
	}

	public void setMatchDao(final MatchDao matchDao)
	{
		this.matchDao = matchDao;
	}

	public void setGroupDao(final GroupDao groupDao)
	{
		this.groupDao = groupDao;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setMatchBetDao(final MatchBetDao matchBetDao)
	{
		this.matchBetDao = matchBetDao;
	}

	public void setScoreStrategy(final ScoreStrategy scoreStrategy)
	{
		this.scoreStrategy = scoreStrategy;
	}

	public void setRandomizeStrategy(final RandomizeStrategy randomizeStrategy)
	{
		this.randomizeStrategy = randomizeStrategy;
	}
}

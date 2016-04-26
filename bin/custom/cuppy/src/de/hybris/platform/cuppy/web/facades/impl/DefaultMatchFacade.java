/**
 * 
 */
package de.hybris.platform.cuppy.web.facades.impl;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.web.converters.CollectionConverter;
import de.hybris.platform.cuppy.web.data.BetData;
import de.hybris.platform.cuppy.web.data.GroupData;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.data.NewsData;
import de.hybris.platform.cuppy.web.facades.MatchFacade;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "matchFacade")
public class DefaultMatchFacade implements MatchFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultMatchFacade.class);

	@Autowired
	private MatchService matchService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private CompetitionService competitionService;
	@Autowired
	private CollectionConverter<GroupModel, GroupData> groupConverter;
	@Autowired
	private CollectionConverter<MatchBetModel, BetData> betConverter;
	@Autowired
	private CollectionConverter<MatchModel, MatchData> matchConverter;
	@Autowired
	private CollectionConverter<NewsModel, NewsData> newsConverter;

	@Override
	public List<GroupData> getGroups()
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		final List<GroupModel> groups = matchService.getGroups(compModel);
		return groupConverter.convertAll(groups);
	}

	@Override
	public List<MatchData> getMatches(final GroupData group)
	{
		final CompetitionModel compModel = competitionService.getCompetition(group.getCompetition());
		final GroupModel foundGroup = matchService.getGroup(compModel, group.getCode());
		if (foundGroup == null)
		{
			return Collections.EMPTY_LIST;
		}
		return matchConverter.convertAll(matchService.getMatches(foundGroup));
	}

	@Override
	public void placeBet(final MatchData match)
	{
		if (match.getGuestBet() == null || match.getHomeBet() == null)
		{
			return;
		}
		final PlayerModel curPlayer = playerService.getCurrentPlayer();
		if (curPlayer == null)
		{
			LOG.error("No current player, will not place bet");
			return;
		}
		final CompetitionModel compModel = competitionService.getCompetition(match.getGroup().getCompetition());
		final MatchModel matchModel = matchService.getMatch(compModel, match.getId());
		final MatchBetModel bet = matchService.getOrCreateBet(matchModel, curPlayer);
		bet.setGuestGoals(match.getGuestBet().intValue());
		bet.setHomeGoals(match.getHomeBet().intValue());
		matchService.placeBet(bet);
	}

	@Override
	public List<NewsData> getLatestNews(final int count)
	{
		return newsConverter.convertAll(matchService.getLatestNews(playerService.getCurrentPlayer(), count));
	}

	@Override
	public List<MatchData> getMatches()
	{
		return matchConverter.convertAll(getMatchModels());
	}

	@Override
	public List<MatchData> getMatches(final String uid)
	{
		final PlayerModel player = playerService.getPlayer(uid);
		return (List<MatchData>) sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				playerService.setCurrentPlayer(player);
				return matchConverter.convertAll(getMatchModels());
			}
		});
	}

	public List<MatchModel> getMatchModels()
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		return matchService.getMatches(compModel);
	}

	@Override
	public List<MatchData> getClosedMatches(final String uid)
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		final PlayerModel player = playerService.getPlayer(uid);

		return (List<MatchData>) sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				playerService.setCurrentPlayer(player);
				final List<MatchModel> matches = matchService.getMatches(compModel);

				final List<MatchData> result = new ArrayList<MatchData>();
				for (final MatchModel match : matches)
				{
					if (matchService.getTimeToBet(match) == 0 && !match.getHomeTeam().isDummy() && !match.getGuestTeam().isDummy())
					{
						result.add(matchConverter.convert(match));
					}
				}
				return result;
			}
		});
	}

	@Override
	public List<BetData> getClosedBets(final int matchId)
	{
		MatchModel match = null;
		try
		{
			final CompetitionModel compModel = competitionService.getCurrentCompetition();
			match = matchService.getMatch(compModel, matchId);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("No match found for " + matchId);
			return Collections.EMPTY_LIST;
		}

		if (matchService.getTimeToBet(match) > 0)
		{
			return Collections.EMPTY_LIST;
		}

		final List<BetData> result = betConverter.convertAll(matchService.getBets(match));
		Collections.sort(result, new Comparator<BetData>()
		{
			@Override
			public int compare(final BetData bet1, final BetData bet2)
			{
				return bet1.getPlayerName().compareToIgnoreCase(bet2.getPlayerName());
			}
		});
		return result;
	}

	@Override
	public MatchData getMatch(final int id)
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		final MatchModel match = matchService.getMatch(compModel, id);
		if (match == null)
		{
			return null;
		}
		return matchConverter.convert(match);
	}

	@Override
	public List<MatchData> getMatches(final int matchday)
	{
		final List<MatchModel> matches = matchService.getMatches(competitionService.getCurrentCompetition(), matchday);
		return matches == null ? Collections.EMPTY_LIST : matchConverter.convertAll(matches);
	}


	@Override
	public List<Integer> getMatchdays()
	{
		return matchService.getMatchdays(competitionService.getCurrentCompetition());
	}

	@Override
	public Integer getCurrentMatchday()
	{
		return matchService.getCurrentMatchday(competitionService.getCurrentCompetition());
	}

	public void setMatchService(final MatchService matchService)
	{
		this.matchService = matchService;
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setCompetitionService(final CompetitionService competitionService)
	{
		this.competitionService = competitionService;
	}

	public void setGroupConverter(final CollectionConverter<GroupModel, GroupData> groupConverter)
	{
		this.groupConverter = groupConverter;
	}

	public void setBetConverter(final CollectionConverter<MatchBetModel, BetData> betConverter)
	{
		this.betConverter = betConverter;
	}

	public void setMatchConverter(final CollectionConverter<MatchModel, MatchData> matchConverter)
	{
		this.matchConverter = matchConverter;
	}

	public void setNewsConverter(final CollectionConverter<NewsModel, NewsData> newsConverter)
	{
		this.newsConverter = newsConverter;
	}
}

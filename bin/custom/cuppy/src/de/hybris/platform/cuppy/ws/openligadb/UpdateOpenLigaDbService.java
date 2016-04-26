/**
 *
 */
package de.hybris.platform.cuppy.ws.openligadb;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.services.GroupCalculationStrategy;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.MatchdayCalculationStrategy;
import de.hybris.platform.cuppy.services.TeamCalculationStrategy;
import de.hybris.platform.cuppy.services.UpdateCompetitionService;
import de.hybris.platform.cuppy.ws.openligadb.types.GetLastChangeDateByLeagueSaison;
import de.hybris.platform.cuppy.ws.openligadb.types.GetLastChangeDateByLeagueSaisonResponse;
import de.hybris.platform.cuppy.ws.openligadb.types.GetMatchdataByLeagueSaison;
import de.hybris.platform.cuppy.ws.openligadb.types.GetMatchdataByLeagueSaisonResponse;
import de.hybris.platform.cuppy.ws.openligadb.types.MatchResult;
import de.hybris.platform.cuppy.ws.openligadb.types.Matchdata;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.core.WebServiceTemplate;


/**
 * @author andreas.thaler
 *
 */
public class UpdateOpenLigaDbService implements UpdateCompetitionService
{
	private static final Logger LOG = Logger.getLogger(UpdateOpenLigaDbService.class);

	private String leagueId;
	private String leagueSeason;
	private long matchIdOffset = 0;
	private int[] resultIds;

	@Autowired
	private WebServiceTemplate openLigaDBTemplate;
	@Autowired
	private MatchService matchService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private I18NService i18nService;
	@Autowired
	private FormatFactory formatFactory;

	private GroupCalculationStrategy groupCalculationStrategy;
	private TeamCalculationStrategy teamCalculationStrategy;
	private MatchdayCalculationStrategy matchdayCalculationStrategy;

	@Override
	public boolean isUpToDate(final CompetitionModel competition, final Date lastExecutionDate)
	{
		LOG.info("Checking update status for " + competition.getCode());

		try
		{
			final GetLastChangeDateByLeagueSaison request = new GetLastChangeDateByLeagueSaison();
			request.setLeagueShortcut(leagueId);
			request.setLeagueSaison(leagueSeason);

			final GetLastChangeDateByLeagueSaisonResponse response = (GetLastChangeDateByLeagueSaisonResponse) openLigaDBTemplate
					.marshalSendAndReceive(request);

			final Calendar lastChange = response.getGetLastChangeDateByLeagueSaisonResult().toGregorianCalendar();
			final Calendar lastExecution = Calendar.getInstance(i18nService.getCurrentTimeZone(), i18nService.getCurrentLocale());
			lastExecution.setTime(lastExecutionDate);
			final boolean result = lastChange.after(lastExecution);
			if (result)
			{
				LOG.info("Update needed for " + competition.getCode());
			}
			else
			{
				LOG.info("No update needed for " + competition.getCode());
			}
			return result;
		}
		catch (final WebServiceIOException e)
		{
			LOG.warn("Can not check webservice for updates: " + e.getMessage());
			return true;
		}
	}

	@Override
	public void update(final CompetitionModel competition)
	{
		LOG.info("Updating matches for " + competition.getCode());

		try
		{
			final GetMatchdataByLeagueSaison request = new GetMatchdataByLeagueSaison();
			request.setLeagueShortcut(leagueId);
			request.setLeagueSaison(leagueSeason);

			final GetMatchdataByLeagueSaisonResponse response = (GetMatchdataByLeagueSaisonResponse) openLigaDBTemplate
					.marshalSendAndReceive(request);

			final List<Matchdata> matches = response.getGetMatchdataByLeagueSaisonResult().getMatchdata();
			Collections.sort(matches, new Comparator<Matchdata>()
			{
				@Override
				public int compare(final Matchdata o1, final Matchdata o2)
				{
					return o1.getMatchDateTime().compare(o2.getMatchDateTime());
				}
			});
			for (final Matchdata match : matches)
			{
				MatchModel matchModel = null;
				try
				{
					matchModel = matchService.getMatch(competition, (int) (match.getMatchID() - matchIdOffset));
					matchModel = updateMatch(match, matchModel);
				}
				catch (final UnknownIdentifierException e)
				{
					try {
						matchModel = createMatch(match, competition);
					} catch (NullPointerException npe) {
						LOG.warn("Error creating match " + match.getMatchID());
					}
				}
				if (matchModel != null)
					modelService.save(matchModel);
			}
			LOG.info("Finished updating matches for " + competition.getCode());
		}
		catch (final WebServiceIOException e)
		{
			LOG.warn("Can not update by webservice: " + e.getMessage());
		}
	}

	private MatchModel createMatch(final Matchdata data, final CompetitionModel competition)
	{
		final StringBuilder message = new StringBuilder();

		final MatchModel match = new MatchModel();
		match.setId((int) (data.getMatchID() - matchIdOffset));
		message.append("Created new match ").append(match.getId()).append(" for ").append(competition.getCode()).append("\n");

		match.setGroup(groupCalculationStrategy.getGroup(competition, data.getGroupName(), data.getGroupOrderID()));
		message.append("  Set group to ").append(match.getGroup().getCode()).append("\n");

		match.setHomeTeam(teamCalculationStrategy.getTeam(competition, data.getNameTeam1()));
		message.append("  Set home team to ").append(match.getHomeTeam().getIsocode()).append("\n");
		match.setGuestTeam(teamCalculationStrategy.getTeam(competition, data.getNameTeam2()));
		message.append("  Set guest team to ").append(match.getGuestTeam().getIsocode()).append("\n");

		match.setMatchday(matchdayCalculationStrategy.getMatchday(competition, data.getGroupOrderID(), data.getMatchDateTime()
				.toGregorianCalendar().getTime()));
		message.append("  Set matchday to ").append(match.getMatchday()).append("\n");

		updateLocation(data, match, message);

		updateGoals(data, match, message);

		updateMatchDate(data, match, message);

		LOG.info(message.toString());

		return match;
	}

	private MatchModel updateMatch(final Matchdata data, final MatchModel match)
	{
		final StringBuilder message = new StringBuilder();

		updateMatchDate(data, match, message);

		updateLocation(data, match, message);

		updateGoals(data, match, message);

		if (!message.toString().isEmpty())
		{
			LOG.info("Match " + match.getId() + " was updated for competition " + match.getGroup().getCompetition().getCode() + "\n"
					+ message.toString());
		}
		return match;
	}

	private void updateMatchDate(final Matchdata data, final MatchModel match, final StringBuilder message)
	{
		if (!data.getMatchDateTime().toGregorianCalendar().getTime().equals(match.getDate()))
		{
			match.setDate(data.getMatchDateTime().toGregorianCalendar().getTime());
			message.append("  Set date to ")
					.append(formatFactory.createDateTimeFormat(DateFormat.MEDIUM, -1).format(match.getDate())).append("\n");
		}
	}

	private void updateLocation(final Matchdata data, final MatchModel match, final StringBuilder message)
	{
		if (data.getLocation().getLocationCity() != null
				&& !(data.getLocation().getLocationCity() + "/" + data.getLocation().getLocationStadium())
						.equals(match.getLocation()))
		{
			match.setLocation(data.getLocation().getLocationCity() + "/" + data.getLocation().getLocationStadium());
			message.append("  Set location to ").append(match.getLocation()).append("\n");
		}
	}

	private void updateGoals(final Matchdata data, final MatchModel match, final StringBuilder message)
	{
		if (data.isMatchIsFinished())
		{
			if (resultIds == null)
			{
				setGoals(match, data.getPointsTeam1(), data.getPointsTeam2(), message);
			}
			else
			{
				final List<MatchResult> results = data.getMatchResults() == null ? Collections.EMPTY_LIST : data.getMatchResults()
						.getMatchResult();
				MatchResult result = null;
				for (final int a : resultIds)
				{
					if (result == null && results.size() > a)
					{
						result = results.get(a);
					}
				}
				if (result != null)
				{
					setGoals(match, result.getPointsTeam1(), result.getPointsTeam2(), message);
				}
			}
		}
	}

	private void setGoals(final MatchModel match, final int homeGoals, final int guestGoals, final StringBuilder message)
	{
		if (match.getHomeGoals() == null)
		{
			match.setHomeGoals(Integer.valueOf(homeGoals));
			message.append("  Set home goals to ").append(match.getHomeGoals()).append("\n");
		}
		if (match.getGuestGoals() == null)
		{
			match.setGuestGoals(Integer.valueOf(guestGoals));
			message.append("  Set guest goals to ").append(match.getGuestGoals()).append("\n");
		}
	}

	@Required
	public void setLeagueId(final String leagueId)
	{
		this.leagueId = leagueId;
	}

	@Required
	public void setLeagueSeason(final String leagueSeason)
	{
		this.leagueSeason = leagueSeason;
	}

	public void setMatchIdOffset(final long matchIdOffset)
	{
		this.matchIdOffset = matchIdOffset;
	}

	@Required
	public void setGroupCalculationStrategy(final GroupCalculationStrategy groupCalculationStrategy)
	{
		this.groupCalculationStrategy = groupCalculationStrategy;
	}

	@Required
	public void setTeamCalculationStrategy(final TeamCalculationStrategy teamCalculationStrategy)
	{
		this.teamCalculationStrategy = teamCalculationStrategy;
	}

	@Required
	public void setMatchdayCalculationStrategy(final MatchdayCalculationStrategy matchdayCalculationStrategy)
	{
		this.matchdayCalculationStrategy = matchdayCalculationStrategy;
	}

	public void setResultIds(final int[] resultIds)
	{
		this.resultIds = resultIds;
	}
}

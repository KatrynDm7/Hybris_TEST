/**
 * 
 */
package de.hybris.platform.cuppy.web.facades;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.CountryFlagModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.StatisticsService;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.data.MatchStatisticData;
import de.hybris.platform.cuppy.web.facades.impl.DefaultStatisticsFacade;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;

import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultStatisticsFacadeTest
{
	private DefaultStatisticsFacade statisticsFacade;

	private PlayerService playerService;
	private PlayerFacade playerFacade;
	private MatchService matchService;
	private MatchFacade matchFacade;
	private StatisticsService statisticsService;
	private CompetitionService competitionService;

	@Before
	public void setUp()
	{
		statisticsFacade = new DefaultStatisticsFacade();
		matchService = createMock(MatchService.class);
		playerService = createMock(PlayerService.class);
		matchFacade = createMock(MatchFacade.class);
		playerFacade = createMock(PlayerFacade.class);
		statisticsService = createMock(StatisticsService.class);
		competitionService = createMock(CompetitionService.class);

		statisticsFacade.setMatchService(matchService);
		statisticsFacade.setPlayerService(playerService);
		statisticsFacade.setMatchFacade(matchFacade);
		statisticsFacade.setPlayerFacade(playerFacade);
		statisticsFacade.setStatisticsService(statisticsService);
		statisticsFacade.setCompetitionService(competitionService);
	}

	@Test
	public void testGetNextBetableMatch()
	{
		final CompetitionModel comp = new CompetitionModel();

		final GroupModel group = new GroupModel();
		getContext(group).setLocaleProvider(new StubLocaleProvider(Locale.GERMAN));
		group.setCode("test");
		group.setCompetition(comp);

		final CountryFlagModel homeFlag = new CountryFlagModel();
		homeFlag.setURL("http://home");

		final TeamModel homeTeam = new TeamModel();
		getContext(homeTeam).setLocaleProvider(new StubLocaleProvider(Locale.GERMAN));
		homeTeam.setIsocode("home");
		homeTeam.setName("homeName");
		homeTeam.setFlag(homeFlag);

		final CountryFlagModel guestFlag = new CountryFlagModel();
		guestFlag.setURL("http://guest");

		final TeamModel guestTeam = new TeamModel();
		getContext(guestTeam).setLocaleProvider(new StubLocaleProvider(Locale.GERMAN));
		guestTeam.setIsocode("guest");
		guestTeam.setName("guestName");
		guestTeam.setFlag(guestFlag);

		final MatchModel match = new MatchModel();
		match.setId(1);
		match.setDate(new Date(4));
		match.setGroup(group);
		match.setLocation("location");
		match.setHomeGoals(Integer.valueOf(5));
		match.setHomeTeam(homeTeam);
		match.setGuestGoals(null);
		match.setGuestTeam(guestTeam);

		final MatchBetModel bet = new MatchBetModel();
		bet.setMatch(match);
		bet.setHomeGoals(1);
		bet.setGuestGoals(2);

		final MatchData matchData = new MatchData();
		matchData.setId(match.getId());

		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(matchService.getNextBetableMatch(comp)).andReturn(match);
		expect(Long.valueOf(matchService.getTimeToBet(match))).andReturn(Long.valueOf(0));
		expect(Long.valueOf(statisticsService.getPlayersNotPlacedBetsForMatchTotal(match))).andReturn(Long.valueOf(0));
		expect(Double.valueOf(statisticsService.getPlayersNotPlacedBetsForMatchPerc(match))).andReturn(Double.valueOf(0));
		expect(Double.valueOf(statisticsService.getAverageScoreForMatch(match))).andReturn(Double.valueOf(0));
		expect(matchFacade.getMatch(match.getId())).andReturn(matchData);
		replay(matchService, matchFacade, statisticsService, competitionService);

		MatchStatisticData result = statisticsFacade.getNextBetableMatchStatistic();
		assertEquals(matchData, result.getMatch());

		verify(matchService, matchFacade, statisticsService, competitionService);

		reset(matchService, competitionService);

		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(matchService.getNextBetableMatch(comp)).andReturn(null);
		replay(matchService, competitionService);

		result = statisticsFacade.getNextBetableMatchStatistic();
		assertNull(result);
		verify(matchService, competitionService);
	}

	private ItemModelContextImpl getContext(final AbstractItemModel model)
	{
		final ItemModelContextImpl context = (ItemModelContextImpl) ModelContextUtils.getItemModelContext(model);
		return context;
	}

}

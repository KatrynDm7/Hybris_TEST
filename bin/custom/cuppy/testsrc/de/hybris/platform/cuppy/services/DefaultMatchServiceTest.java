package de.hybris.platform.cuppy.services;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.reset;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.platform.cuppy.daos.GroupDao;
import de.hybris.platform.cuppy.daos.MatchBetDao;
import de.hybris.platform.cuppy.daos.MatchDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppy.services.impl.DefaultMatchService;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultMatchServiceTest
{
	private DefaultMatchService matchService;
	private MatchDao matchDao;
	private MatchBetDao matchBetDao;
	private GroupDao groupDao;
	private ModelService modelService;
	private ScoreStrategy scoreStrategy;

	@Before
	public void setUp()
	{
		matchService = new DefaultMatchService();

		matchDao = createMock(MatchDao.class);
		matchService.setMatchDao(matchDao);

		matchBetDao = createMock(MatchBetDao.class);
		matchService.setMatchBetDao(matchBetDao);

		groupDao = createMock(GroupDao.class);
		matchService.setGroupDao(groupDao);

		modelService = createMock(ModelService.class);
		matchService.setModelService(modelService);

		scoreStrategy = createMock(ScoreStrategy.class);
		matchService.setScoreStrategy(scoreStrategy);
	}

	@Test
	public void testGetGroups()
	{
		final CompetitionModel competition = new CompetitionModel();

		final GroupModel group1 = new GroupModel();
		getContext(group1).setLocaleProvider(new StubLocaleProvider(Locale.GERMANY));
		group1.setCode("codeGroup1");
		group1.setName("codeGroup1");
		group1.setCompetition(competition);
		group1.setMultiplier(1);

		final GroupModel group2 = new GroupModel();
		getContext(group2).setLocaleProvider(new StubLocaleProvider(Locale.GERMANY));
		group2.setCode("codeGroup2");
		group2.setName("codeGroup2");
		group2.setCompetition(competition);
		group2.setMultiplier(1);

		final List<GroupModel> groups = Arrays.asList(group1, group2);

		expect(groupDao.findGroups(competition)).andReturn(groups);
		replay(groupDao);

		final List<GroupModel> result = matchService.getGroups(competition);
		assertEquals(2, result.size());
		assertEquals(group1.getCode(), result.get(0).getCode());
		assertEquals(group1.getName(), result.get(0).getName());
		assertEquals(group1.getCompetition(), result.get(0).getCompetition());
		assertEquals(Float.valueOf(group1.getMultiplier()), Float.valueOf(result.get(0).getMultiplier()));

		assertEquals(group2.getCode(), result.get(1).getCode());
		assertEquals(group2.getName(), result.get(1).getName());
		assertEquals(group2.getCompetition(), result.get(1).getCompetition());
		assertEquals(Float.valueOf(group2.getMultiplier()), Float.valueOf(result.get(1).getMultiplier()));

		verify(groupDao);
	}

	@Test
	public void testGetGroup()
	{
		final CompetitionModel competition = new CompetitionModel();

		final GroupModel group1 = new GroupModel();
		getContext(group1).setLocaleProvider(new StubLocaleProvider(Locale.GERMANY));
		group1.setCode("codeGroup1");
		group1.setName("codeGroup1");
		group1.setMultiplier(2);
		group1.setCompetition(competition);
		final List<GroupModel> groups = Arrays.asList(group1);

		expect(groupDao.findGroupByCode(competition, group1.getCode())).andReturn(groups);
		replay(groupDao);

		final GroupModel result = matchService.getGroup(competition, group1.getCode());
		assertSame(group1, result);

		verify(groupDao);
		reset(groupDao);

		expect(groupDao.findGroupByCode(competition, "notFound")).andReturn(Collections.EMPTY_LIST);
		replay(groupDao);

		try
		{
			matchService.getGroup(competition, "notFound");
			fail("Exception expected");
		}
		catch (final UnknownIdentifierException e)
		{
			// OK
		}

		verify(groupDao);
	}

	public void testGetMatch()
	{
		final CompetitionModel competition = new CompetitionModel();

		final GroupModel group = new GroupModel();
		group.setCompetition(competition);
		final MatchModel match = new MatchModel();
		match.setId(5);
		match.setGroup(group);
		final List<MatchModel> matches = Arrays.asList(match);

		expect(matchDao.findMatchById(competition, 5)).andReturn(matches);
		replay(matchDao);

		MatchModel result = matchService.getMatch(competition, 5);
		assertSame(match, result);

		verify(matchDao);

		reset(matchDao);
		expect(matchDao.findMatchById(competition, 4)).andReturn(null);
		replay(matchDao);

		result = matchService.getMatch(competition, 4);
		assertNull(result);
	}

	@Test
	public void testGetMatches()
	{
		final CompetitionModel competition = new CompetitionModel();

		final GroupModel group = new GroupModel();
		group.setCompetition(competition);
		final MatchModel match1 = new MatchModel();
		match1.setId(1);
		match1.setGroup(group);
		final MatchModel match2 = new MatchModel();
		match2.setId(2);
		match2.setGroup(group);

		final List<MatchModel> matches = Arrays.asList(match1, match2);

		expect(matchDao.findMatches(competition)).andReturn(matches);
		replay(matchDao);

		List<MatchModel> result = matchService.getMatches(competition);
		assertEquals(2, result.size());
		assertSame(match1, result.get(0));
		assertSame(match2, result.get(1));

		verify(matchDao);
		reset(matchDao);

		expect(matchDao.findMatches(competition)).andReturn(Collections.EMPTY_LIST);
		replay(matchDao);

		result = matchService.getMatches(competition);
		assertTrue(result.isEmpty());

		verify(matchDao);
	}

	@Test
	public void testGetMatchesByGroup()
	{
		final GroupModel group = new GroupModel();
		group.setCode("test");

		final MatchModel match1 = new MatchModel();
		match1.setId(1);
		match1.setGroup(group);
		final MatchModel match2 = new MatchModel();
		match2.setId(2);
		match2.setGroup(group);

		final List<MatchModel> matches = Arrays.asList(match1, match2);

		expect(matchDao.findMatchesByGroup(group)).andReturn(matches);
		replay(matchDao);

		List<MatchModel> result = matchService.getMatches(group);
		assertEquals(2, result.size());
		assertSame(match1, result.get(0));
		assertSame(match2, result.get(1));

		verify(matchDao);
		reset(matchDao);

		expect(matchDao.findMatchesByGroup(group)).andReturn(Collections.EMPTY_LIST);
		replay(matchDao);

		result = matchService.getMatches(group);
		assertTrue(result.isEmpty());

		verify(matchDao);
	}

	@Test
	public void testPlaceBet()
	{
		final MatchBetModel bet = new MatchBetModel();
		final PlayerModel player = new PlayerModel();
		bet.setPlayer(player);
		final MatchModel match = new MatchModel();
		bet.setMatch(match);
		final TeamModel home = new TeamModel();
		final TeamModel guest = new TeamModel();
		match.setHomeTeam(home);
		match.setGuestTeam(guest);
		home.setDummy(false);
		guest.setDummy(false);
		match.setDate(new Date(new Date().getTime() + (4000 * 1000)));
		assertTrue(matchService.isBetable(match));

		final Capture<MatchBetModel> cap = new Capture<MatchBetModel>();
		modelService.save(capture(cap));
		expectLastCall();
		replay(modelService);

		matchService.placeBet(bet);
		assertSame(bet, cap.getValue());
		verify(modelService);

		reset(modelService);

		match.setDate(new Date());
		assertFalse(matchService.isBetable(match));
		replay(modelService);
		matchService.placeBet(bet);
		verify(modelService);
	}

	@Test
	public void testGetBet()
	{
		final MatchModel match = new MatchModel();
		match.setId(1);

		final PlayerModel player = new PlayerModel();
		player.setUid("test");

		final MatchBetModel bet = new MatchBetModel();
		bet.setPlayer(player);
		bet.setMatch(match);
		player.setMatchBets(Arrays.asList(bet));

		final List<MatchBetModel> bets = Arrays.asList(bet);

		expect(matchBetDao.findMatchBetByPlayerAndMatch(player, match)).andReturn(bets);
		replay(matchBetDao);

		final MatchBetModel result = matchService.getBet(match, player);
		assertNotNull(result);
		assertEquals(bet.getPlayer(), result.getPlayer());
		assertEquals(bet.getMatch(), result.getMatch());

		verify(matchBetDao);
	}

	@Test
	public void testGetOrCreateBet()
	{
		final MatchBetModel bet = new MatchBetModel();
		final MatchModel match = new MatchModel();
		final PlayerModel player = new PlayerModel();
		final List<MatchBetModel> bets = Collections.emptyList();
		expect(matchBetDao.findMatchBetByPlayerAndMatch(player, match)).andReturn(bets);
		expect(modelService.create(MatchBetModel.class)).andReturn(bet);
		replay(matchBetDao, modelService);
		assertEquals(bet, matchService.getOrCreateBet(match, player));
		assertEquals(match, bet.getMatch());
		assertEquals(player, bet.getPlayer());
		verify(matchBetDao, modelService);
	}

	@Test
	public void testHasBet()
	{
		final MatchModel match = new MatchModel();
		final PlayerModel player = new PlayerModel();
		final List<MatchBetModel> bets = Collections.emptyList();
		expect(matchBetDao.findMatchBetByPlayerAndMatch(player, match)).andReturn(bets);
		replay(matchBetDao);
		assertFalse(matchService.hasBet(match, player));
		verify(matchBetDao);
	}

	@Test
	public void testGetMatchBefore()
	{
		final Date date = new Date();
		final CompetitionModel competition = new CompetitionModel();
		final MatchModel match = new MatchModel();
		final List<MatchModel> matches = Arrays.asList(match);
		expect(matchDao.findMatchBefore(competition, date)).andReturn(matches);
		replay(matchDao);
		assertEquals(match, matchService.getMatchBefore(competition, date));
		verify(matchDao);
	}

	@Test
	public void testGetMatchBeforeNull()
	{
		final Date date = new Date();
		final CompetitionModel competition = new CompetitionModel();
		final List<MatchModel> matches = Collections.emptyList();
		expect(matchDao.findMatchBefore(competition, date)).andReturn(matches);
		replay(matchDao);
		assertNull(matchService.getMatchBefore(competition, date));
		verify(matchDao);
	}

	@Test
	public void testGetBets()
	{
		final MatchModel match = new MatchModel();
		final List<MatchBetModel> bets = new LinkedList<MatchBetModel>();
		expect(matchBetDao.findMatchBetByMatch(match)).andReturn(bets);
		replay(matchBetDao);
		assertEquals(bets, matchService.getBets(match));
		verify(matchBetDao);
	}

	@Test
	public void testGetScoreByPlayer()
	{
		final CompetitionModel competition = new CompetitionModel();
		final PlayerModel player = new PlayerModel();

		final List<MatchBetModel> bets = Arrays.asList(buildBet(competition, player), buildBet(competition, player),
				buildBet(competition, player));

		expect(matchBetDao.findFinishedMatchBetsByPlayer(competition, player)).andReturn(bets);
		expect(Integer.valueOf(scoreStrategy.getScore(1, 1, 1, 1, 2))).andReturn(Integer.valueOf(1)).times(3);
		replay(matchBetDao, scoreStrategy);

		final int result = matchService.getScore(competition, player);
		assertEquals(3, result);

		verify(matchBetDao, scoreStrategy);
	}

	@Test
	public void testGetRandomMatchDummy()
	{
		final CompetitionModel competition = new CompetitionModel();
		final MatchModel match = new MatchModel();
		final TeamModel team = new TeamModel();
		team.setDummy(true);
		match.setHomeTeam(team);
		final List<MatchModel> matches = Arrays.asList(match);
		expect(matchDao.findMatches(competition)).andReturn(matches);
		replay(matchDao);
		assertNull(matchService.getRandomMatch(competition));
		verify(matchDao);
	}

	@Test
	public void testGetRandomMatch()
	{
		final CompetitionModel competition = new CompetitionModel();
		final MatchModel match = new MatchModel();
		final TeamModel teamA = new TeamModel();
		teamA.setDummy(false);
		match.setGuestTeam(teamA);
		final TeamModel teamB = new TeamModel();
		teamB.setDummy(false);
		match.setHomeTeam(teamB);
		final List<MatchModel> matches = Arrays.asList(match);
		final RandomizeStrategy randomizeStrategy = createMock(RandomizeStrategy.class);
		matchService.setRandomizeStrategy(randomizeStrategy);
		expect(matchDao.findMatches(competition)).andReturn(matches);
		expect(Integer.valueOf(randomizeStrategy.getNext(0, 1))).andReturn(Integer.valueOf(0));
		replay(matchDao, randomizeStrategy);
		assertEquals(match, matchService.getRandomMatch(competition));
		verify(matchDao, randomizeStrategy);
	}

	@Test
	public void testGetBetsByCompetitionAndPlayer()
	{
		final CompetitionModel competition = new CompetitionModel();
		final PlayerModel player = new PlayerModel();
		final List<MatchBetModel> bets = new LinkedList<MatchBetModel>();
		expect(matchBetDao.findMatchBetsByPlayer(competition, player)).andReturn(bets);
		replay(matchBetDao);
		assertEquals(bets, matchService.getBets(competition, player));
		verify(matchBetDao);
	}

	@Test
	public void testGetFinishedBetsForCompetitionAndPlayer()
	{
		final CompetitionModel competition = new CompetitionModel();
		final PlayerModel player = new PlayerModel();
		final List<MatchBetModel> bets = new LinkedList<MatchBetModel>();
		expect(matchBetDao.findFinishedMatchBetsByPlayer(competition, player)).andReturn(bets);
		replay(matchBetDao);
		assertEquals(bets, matchService.getFinishedBets(competition, player));
		verify(matchBetDao);
	}

	@Test
	public void getCurrentMatchday()
	{
		final Integer result = Integer.valueOf(1);
		expect(matchDao.getMatchdayByDate((CompetitionModel) anyObject(), (Date) anyObject())).andReturn(result);
		replay(matchDao);
		assertEquals(result, matchService.getCurrentMatchday(new CompetitionModel()));
		verify(matchDao);
	}

	@Test
	public void testGetMatchesByMatchday()
	{
		final CompetitionModel competition = new CompetitionModel();
		final int matchDay = 1;
		final List<MatchModel> result = new LinkedList<MatchModel>();
		expect(matchDao.findMatches(competition, matchDay)).andReturn(result);
		replay(matchDao);
		assertEquals(result, matchService.getMatches(competition, matchDay));
		verify(matchDao);
	}

	@Test
	public void testGetMatchdays()
	{
		final CompetitionModel competition = new CompetitionModel();
		final List<Integer> result = new LinkedList<Integer>();
		expect(matchDao.findMatchdays(competition)).andReturn(result);
		replay(matchDao);
		assertEquals(result, matchService.getMatchdays(competition));
		verify(matchDao);
	}

	@Test
	public void testGetScoreByMatch()
	{
		final CompetitionModel competition = new CompetitionModel();
		final PlayerModel player = new PlayerModel();

		final MatchBetModel bet = buildBet(competition, player);

		expect(Integer.valueOf(scoreStrategy.getScore(1, 1, 1, 1, 2))).andReturn(Integer.valueOf(1));
		replay(scoreStrategy);

		final int result = matchService.getScore(bet);
		assertEquals(1, result);

		verify(scoreStrategy);
	}

	@Test
	public void testGetTimeToBet()
	{
		final MatchModel match = new MatchModel();

		// before 1 hour
		match.setDate(new Date(new Date().getTime() - (60 * 60 * 1000)));
		long result = matchService.getTimeToBet(match);
		assertEquals(0, result);

		// now
		match.setDate(new Date(new Date().getTime() + (0 * 60 * 1000)));
		result = matchService.getTimeToBet(match);
		assertEquals(0, result);

		// in 59 minutes
		match.setDate(new Date(new Date().getTime() - (59 * 60 * 1000)));
		result = matchService.getTimeToBet(match);
		assertEquals(0, result);

		// in 1 hour + 1 minute
		match.setDate(new Date(new Date().getTime() + (61 * 60 * 1000)));
		result = matchService.getTimeToBet(match);
		assertTrue((1 * 59 * 1000) < result && result < (1 * 61 * 1000));

		// in 3 hour
		match.setDate(new Date(new Date().getTime() + (3 * 60 * 60 * 1000)));
		result = matchService.getTimeToBet(match);
		assertTrue((2 * 59 * 60 * 1000) < result && result < (2 * 61 * 60 * 1000));
	}

	@Test
	public void testIsBetable()
	{
		final MatchModel match = new MatchModel();
		final TeamModel homeTeam = new TeamModel();
		homeTeam.setDummy(false); // no dummies
		match.setHomeTeam(homeTeam);
		final TeamModel guestTeam = new TeamModel();
		guestTeam.setDummy(false); // no dummies
		match.setGuestTeam(guestTeam);
		match.setDate(new Date(new Date().getTime() - (60 * 60 * 1000))); // finished one hour before
		match.setHomeGoals(Integer.valueOf(0)); // goals are noted
		match.setGuestGoals(Integer.valueOf(0));// goals are noted

		// not betable -> finished, results available
		boolean result = matchService.isBetable(match);
		assertFalse(result);

		match.setDate(new Date(new Date().getTime() + (3 * 60 * 60 * 1000)));
		match.setHomeGoals(null);
		match.setGuestGoals(null);

		//betable -> not finished, results unknown
		result = matchService.isBetable(match);
		assertTrue(result);

		homeTeam.setDummy(true);

		//not betable -> dummies
		result = matchService.isBetable(match);
		assertFalse(result);

		homeTeam.setDummy(false);
		guestTeam.setDummy(true);

		//not betable -> dummies
		result = matchService.isBetable(match);
		assertFalse(result);

		guestTeam.setDummy(false);
		match.setDate(new Date(new Date().getTime() - (60 * 60 * 1000)));

		//not betable -> finished
		result = matchService.isBetable(match);
		assertFalse(result);

		match.setDate(new Date(new Date().getTime() + (3 * 60 * 60 * 1000)));
		match.setHomeGoals(Integer.valueOf(0));

		//not betable -> results available
		result = matchService.isBetable(match);
		assertFalse(result);

		match.setHomeGoals(null);

		//betable -> not finished, results unknown
		result = matchService.isBetable(match);
		assertTrue(result);
	}

	@Test
	public void testIsMatchFinished()
	{
		final MatchModel match = new MatchModel();
		match.setDate(new Date(new Date().getTime() - (60 * 60 * 1000))); // finished one hour before
		match.setHomeGoals(Integer.valueOf(0)); // goals are noted
		match.setGuestGoals(Integer.valueOf(0));// goals are noted

		// finished -> goals available, date in past
		boolean result = matchService.isMatchFinished(match);
		assertTrue(result);

		match.setDate(new Date(new Date().getTime() + (60 * 60 * 1000))); // in one hour

		//not finished -> date in future
		result = matchService.isMatchFinished(match);
		assertFalse(result);

		match.setDate(new Date(new Date().getTime() - (60 * 60 * 1000))); // finished one hour before
		match.setHomeGoals(null);
		match.setGuestGoals(null);

		//not finished -> goals missing
		result = matchService.isMatchFinished(match);
		assertFalse(result);

		match.setGuestGoals(Integer.valueOf(0));

		//not finished -> goals missing
		result = matchService.isMatchFinished(match);
		assertFalse(result);
	}

	private MatchBetModel buildBet(final CompetitionModel competition, final PlayerModel player)
	{
		final GroupModel group = new GroupModel();
		group.setMultiplier(2);
		group.setCompetition(competition);

		final MatchModel match = new MatchModel();
		match.setHomeGoals(Integer.valueOf(1));
		match.setGuestGoals(Integer.valueOf(1));
		match.setGroup(group);
		match.setDate(new Date(new Date().getTime() - 10000));

		final MatchBetModel bet = new MatchBetModel();
		bet.setMatch(match);
		bet.setPlayer(player);
		bet.setHomeGoals(1);
		bet.setGuestGoals(1);

		return bet;
	}

	private ItemModelContextImpl getContext(final AbstractItemModel model)
	{
		final ItemModelContextImpl context = (ItemModelContextImpl) ModelContextUtils.getItemModelContext(model);
		return context;
	}

}

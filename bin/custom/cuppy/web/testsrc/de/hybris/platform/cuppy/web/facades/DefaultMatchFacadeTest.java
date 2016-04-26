/**
 * 
 */
package de.hybris.platform.cuppy.web.facades;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.CountryFlagModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.web.converters.BetConverter;
import de.hybris.platform.cuppy.web.converters.GroupConverter;
import de.hybris.platform.cuppy.web.converters.MatchConverter;
import de.hybris.platform.cuppy.web.converters.NewsConverter;
import de.hybris.platform.cuppy.web.data.BetData;
import de.hybris.platform.cuppy.web.data.GroupData;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.data.NewsData;
import de.hybris.platform.cuppy.web.facades.impl.DefaultMatchFacade;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.util.ModelUtils;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultMatchFacadeTest
{
	private DefaultMatchFacade matchFacade;

	private PlayerService playerService;
	private I18NService i18nService;
	private SessionService sessionService;
	private MatchService matchService;
	private CompetitionService competitionService;

	@Before
	public void setUp()
	{
		matchFacade = new DefaultMatchFacade();
		matchService = createMock(MatchService.class);
		playerService = createMock(PlayerService.class);
		i18nService = createMock(I18NService.class);
		sessionService = createMock(SessionService.class);
		competitionService = createMock(CompetitionService.class);

		matchFacade.setMatchService(matchService);
		matchFacade.setPlayerService(playerService);
		matchFacade.setSessionService(sessionService);
		matchFacade.setCompetitionService(competitionService);

		final BetConverter betConverter = new BetConverter();
		betConverter.setMatchService(matchService);
		betConverter.setPlayerService(playerService);
		matchFacade.setBetConverter(betConverter);

		final GroupConverter groupConverter = new GroupConverter();
		matchFacade.setGroupConverter(groupConverter);

		final MatchConverter matchConverter = new MatchConverter();
		matchConverter.setGroupConverter(groupConverter);
		matchConverter.setI18nService(i18nService);
		matchConverter.setMatchService(matchService);
		matchConverter.setPlayerService(playerService);
		matchConverter.setSessionService(sessionService);
		matchFacade.setMatchConverter(matchConverter);

		final NewsConverter newsConverter = new NewsConverter();
		newsConverter.setI18nService(i18nService);
		newsConverter.setSessionService(sessionService);
		matchFacade.setNewsConverter(newsConverter);
	}

	@Test
	public void testGetGroups()
	{
		final CompetitionModel comp = new CompetitionModel();
		final GroupModel group1 = new GroupModel();
		getContext(group1).setLocaleProvider(new StubLocaleProvider(Locale.GERMAN));
		group1.setCode("codeGroup1");
		group1.setName("nameGroup1");
		group1.setCompetition(comp);
		final GroupModel group2 = new GroupModel();
		getContext(group2).setLocaleProvider(new StubLocaleProvider(Locale.GERMAN));
		group2.setCode("codeGroup2");
		group2.setName("nameGroup2");
		group2.setCompetition(comp);

		final List<GroupModel> groups = Arrays.asList(group1, group2);
		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(matchService.getGroups(comp)).andReturn(groups);
		replay(matchService, competitionService);

		final List<GroupData> result = matchFacade.getGroups();
		assertEquals(2, result.size());
		assertEquals(group1.getCode(), result.get(0).getCode());
		assertEquals(group1.getName(), result.get(0).getName());
		assertEquals(group2.getCode(), result.get(1).getCode());
		assertEquals(group2.getName(), result.get(1).getName());

		verify(matchService, competitionService);
	}

	@Test
	public void testGetMatches()
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

		final PlayerModel player = new PlayerModel();


		final List<MatchModel> matches = Arrays.asList(match);
		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(matchService.getMatches(comp)).andReturn(matches);
		expect(playerService.getCurrentPlayer()).andReturn(player);
		expect(Integer.valueOf(matchService.getScore(bet))).andReturn(Integer.valueOf(0));
		expect(Boolean.valueOf(matchService.hasBet(match, player))).andReturn(Boolean.TRUE);
		expect(matchService.getBet(match, player)).andReturn(bet);
		expect(Boolean.valueOf(matchService.isBetable(match))).andReturn(Boolean.TRUE);
		expect(Boolean.valueOf(matchService.isMatchFinished(match))).andReturn(Boolean.TRUE);
		final Capture<SessionExecutionBody> cap = new Capture<SessionExecutionBody>();
		expect(sessionService.executeInLocalView(capture(cap))).andReturn(null);
		i18nService.setLocalizationFallbackEnabled(true);
		expectLastCall();
		replay(matchService, playerService, competitionService, sessionService, i18nService);

		final List<MatchData> result = matchFacade.getMatches();
		cap.getValue().executeWithoutResult();

		assertEquals(1, result.size());
		assertEquals(match.getId(), result.get(0).getId());
		assertEquals(match.getDate(), result.get(0).getDate());
		assertEquals(match.getLocation(), result.get(0).getLocation());
		assertEquals(match.getGroup().getCode(), result.get(0).getGroup().getCode());

		assertEquals(match.getHomeGoals().intValue(), result.get(0).getHomeGoals());
		assertEquals(match.getHomeTeam().getName(), result.get(0).getHomeTeam());
		assertEquals(match.getHomeTeam().getFlag().getDownloadURL(), result.get(0).getHomeFlagUrl());
		assertEquals(bet.getHomeGoals(), result.get(0).getHomeBet().intValue());

		assertEquals(0, result.get(0).getGuestGoals());
		assertEquals(match.getGuestTeam().getName(), result.get(0).getGuestTeam());
		assertEquals(match.getGuestTeam().getFlag().getDownloadURL(), result.get(0).getGuestFlagUrl());
		assertEquals(bet.getGuestGoals(), result.get(0).getGuestBet().intValue());

		verify(matchService, playerService, competitionService, sessionService, i18nService);
	}

	@Test
	public void testGetMatchesByPlayer()
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

		final PlayerModel player = new PlayerModel();


		final List<MatchModel> matches = Arrays.asList(match);
		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(matchService.getMatches(comp)).andReturn(matches);
		expect(playerService.getPlayer(player.getUid())).andReturn(player);
		expect(Integer.valueOf(matchService.getScore(bet))).andReturn(Integer.valueOf(0));
		expect(Boolean.valueOf(matchService.hasBet(match, player))).andReturn(Boolean.TRUE);
		expect(matchService.getBet(match, player)).andReturn(bet);
		expect(Boolean.valueOf(matchService.isBetable(match))).andReturn(Boolean.TRUE);
		expect(Boolean.valueOf(matchService.isMatchFinished(match))).andReturn(Boolean.TRUE);

		final Capture<SessionExecutionBody> capPlayer = new Capture<SessionExecutionBody>();
		expect(sessionService.executeInLocalView(capture(capPlayer))).andReturn(Collections.EMPTY_LIST);

		final Capture<PlayerModel> capPlayerModel = new Capture<PlayerModel>();
		playerService.setCurrentPlayer(capture(capPlayerModel));
		expectLastCall();
		expect(playerService.getCurrentPlayer()).andReturn(player);

		final Capture<SessionExecutionBody> cap = new Capture<SessionExecutionBody>();
		expect(sessionService.executeInLocalView(capture(cap))).andReturn(Collections.EMPTY_LIST);
		i18nService.setLocalizationFallbackEnabled(true);
		expectLastCall();

		replay(matchService, playerService, competitionService, sessionService, i18nService);

		final List<MatchData> dummyResult = matchFacade.getMatches(player.getUid());
		assertEquals(Collections.EMPTY_LIST, dummyResult);

		final List<MatchData> result = (List<MatchData>) capPlayer.getValue().execute();
		cap.getValue().executeWithoutResult();

		assertEquals(player, capPlayerModel.getValue());
		assertEquals(1, result.size());
		assertEquals(match.getId(), result.get(0).getId());
		assertEquals(match.getDate(), result.get(0).getDate());
		assertEquals(match.getLocation(), result.get(0).getLocation());
		assertEquals(match.getGroup().getCode(), result.get(0).getGroup().getCode());

		assertEquals(match.getHomeGoals().intValue(), result.get(0).getHomeGoals());
		assertEquals(match.getHomeTeam().getName(), result.get(0).getHomeTeam());
		assertEquals(match.getHomeTeam().getFlag().getDownloadURL(), result.get(0).getHomeFlagUrl());
		assertEquals(bet.getHomeGoals(), result.get(0).getHomeBet().intValue());

		assertEquals(0, result.get(0).getGuestGoals());
		assertEquals(match.getGuestTeam().getName(), result.get(0).getGuestTeam());
		assertEquals(match.getGuestTeam().getFlag().getDownloadURL(), result.get(0).getGuestFlagUrl());
		assertEquals(bet.getGuestGoals(), result.get(0).getGuestBet().intValue());

		verify(matchService, playerService, competitionService, sessionService, i18nService);
	}

	@Test
	public void testGetClosedMatches()
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

		final PlayerModel player = new PlayerModel();


		final List<MatchModel> matches = Arrays.asList(match);
		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(matchService.getMatches(comp)).andReturn(matches);
		expect(playerService.getPlayer(player.getUid())).andReturn(player);
		expect(Integer.valueOf(matchService.getScore(bet))).andReturn(Integer.valueOf(0));
		expect(Boolean.valueOf(matchService.hasBet(match, player))).andReturn(Boolean.TRUE);
		expect(Long.valueOf(matchService.getTimeToBet(match))).andReturn(Long.valueOf(0));
		expect(matchService.getBet(match, player)).andReturn(bet);
		expect(Boolean.valueOf(matchService.isBetable(match))).andReturn(Boolean.TRUE);
		expect(Boolean.valueOf(matchService.isMatchFinished(match))).andReturn(Boolean.TRUE);

		final Capture<SessionExecutionBody> capPlayer = new Capture<SessionExecutionBody>();
		expect(sessionService.executeInLocalView(capture(capPlayer))).andReturn(Collections.EMPTY_LIST);

		final Capture<PlayerModel> capPlayerModel = new Capture<PlayerModel>();
		playerService.setCurrentPlayer(capture(capPlayerModel));
		expectLastCall();
		expect(playerService.getCurrentPlayer()).andReturn(player);

		final Capture<SessionExecutionBody> capFallback = new Capture<SessionExecutionBody>();
		expect(sessionService.executeInLocalView(capture(capFallback))).andReturn(null);
		i18nService.setLocalizationFallbackEnabled(true);
		expectLastCall();
		replay(matchService, playerService, competitionService, sessionService, i18nService);

		final List<MatchData> dummyResult = matchFacade.getClosedMatches(player.getUid());
		assertEquals(Collections.EMPTY_LIST, dummyResult);

		final List<MatchData> result = (List<MatchData>) capPlayer.getValue().execute();
		capFallback.getValue().executeWithoutResult();
		assertEquals(player, capPlayerModel.getValue());

		assertEquals(1, result.size());
		assertEquals(match.getId(), result.get(0).getId());
		assertEquals(match.getDate(), result.get(0).getDate());
		assertEquals(match.getLocation(), result.get(0).getLocation());
		assertEquals(match.getGroup().getCode(), result.get(0).getGroup().getCode());

		assertEquals(match.getHomeGoals().intValue(), result.get(0).getHomeGoals());
		assertEquals(match.getHomeTeam().getName(), result.get(0).getHomeTeam());
		assertEquals(match.getHomeTeam().getFlag().getDownloadURL(), result.get(0).getHomeFlagUrl());
		assertEquals(bet.getHomeGoals(), result.get(0).getHomeBet().intValue());

		assertEquals(0, result.get(0).getGuestGoals());
		assertEquals(match.getGuestTeam().getName(), result.get(0).getGuestTeam());
		assertEquals(match.getGuestTeam().getFlag().getDownloadURL(), result.get(0).getGuestFlagUrl());
		assertEquals(bet.getGuestGoals(), result.get(0).getGuestBet().intValue());

		verify(matchService, playerService, competitionService, sessionService, i18nService);
	}

	@Test
	public void testGetMatchesByGroup() throws MalformedURLException
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

		final List<MatchModel> matches = Arrays.asList(match);

		final PlayerModel player = new PlayerModel();

		expect(competitionService.getCompetition(comp.getCode())).andReturn(comp);
		expect(matchService.getGroup(comp, group.getCode())).andReturn(group);
		expect(matchService.getMatches(group)).andReturn(matches);
		expect(Integer.valueOf(matchService.getScore(bet))).andReturn(Integer.valueOf(0));
		expect(playerService.getCurrentPlayer()).andReturn(player);
		expect(Boolean.valueOf(matchService.hasBet(match, player))).andReturn(Boolean.TRUE);
		expect(matchService.getBet(match, player)).andReturn(bet);
		expect(Boolean.valueOf(matchService.isBetable(match))).andReturn(Boolean.TRUE);
		expect(Boolean.valueOf(matchService.isMatchFinished(match))).andReturn(Boolean.TRUE);
		final Capture<SessionExecutionBody> cap = new Capture<SessionExecutionBody>();
		expect(sessionService.executeInLocalView(capture(cap))).andReturn(null);
		i18nService.setLocalizationFallbackEnabled(true);
		expectLastCall();
		replay(matchService, playerService, competitionService, sessionService, i18nService);

		final GroupData searchGroup = new GroupData();
		searchGroup.setCode("test");

		final List<MatchData> result = matchFacade.getMatches(searchGroup);
		cap.getValue().executeWithoutResult();

		assertEquals(1, result.size());
		assertEquals(match.getId(), result.get(0).getId());
		assertEquals(match.getDate(), result.get(0).getDate());
		assertEquals(match.getLocation(), result.get(0).getLocation());
		assertEquals(match.getGroup().getCode(), result.get(0).getGroup().getCode());

		assertEquals(match.getHomeGoals().intValue(), result.get(0).getHomeGoals());
		assertEquals(match.getHomeTeam().getName(), result.get(0).getHomeTeam());
		assertEquals(match.getHomeTeam().getFlag().getDownloadURL(), result.get(0).getHomeFlagUrl());
		assertEquals(bet.getHomeGoals(), result.get(0).getHomeBet().intValue());

		assertEquals(0, result.get(0).getGuestGoals());
		assertEquals(match.getGuestTeam().getName(), result.get(0).getGuestTeam());
		assertEquals(match.getGuestTeam().getFlag().getDownloadURL(), result.get(0).getGuestFlagUrl());
		assertEquals(bet.getGuestGoals(), result.get(0).getGuestBet().intValue());

		verify(matchService, playerService, competitionService, sessionService, i18nService);

		reset(matchService, competitionService);

		expect(competitionService.getCompetition(comp.getCode())).andReturn(comp);
		expect(matchService.getGroup(comp, group.getCode())).andReturn(null);
		replay(matchService, competitionService);

		final List<MatchData> result2 = matchFacade.getMatches(searchGroup);
		assertTrue(result2.isEmpty());

		verify(matchService, competitionService);
	}

	@Test
	public void testPlaceBets()
	{
		final CompetitionModel comp = new CompetitionModel();

		final GroupModel groupM1 = new GroupModel();
		groupM1.setCompetition(comp);

		final GroupData group = new GroupData();
		groupM1.setCode("bla");

		final MatchData match1 = new MatchData();
		match1.setId(1);
		match1.setGuestBet(Integer.valueOf(1));
		match1.setHomeBet(Integer.valueOf(2));
		match1.setMatchBetable(true);
		match1.setGroup(group);

		final MatchModel matchM1 = new MatchModel();
		matchM1.setId(1);
		matchM1.setGroup(groupM1);

		final PlayerModel player = new PlayerModel();

		final MatchBetModel bet = new MatchBetModel();
		bet.setMatch(matchM1);
		bet.setPlayer(player);

		final Capture<MatchBetModel> cap = new Capture<MatchBetModel>();
		expect(matchService.getMatch(comp, match1.getId())).andReturn(matchM1);
		expect(matchService.getOrCreateBet(matchM1, player)).andReturn(bet);
		expect(playerService.getCurrentPlayer()).andReturn(player);

		expect(competitionService.getCompetition(comp.getCode())).andReturn(comp);
		matchService.placeBet(capture(cap));
		expectLastCall();
		replay(matchService, playerService, competitionService);

		matchFacade.placeBet(match1);

		assertEquals(matchM1, cap.getValue().getMatch());
		assertEquals(player, cap.getValue().getPlayer());
		assertEquals(match1.getGuestBet().intValue(), cap.getValue().getGuestGoals());
		assertEquals(match1.getHomeBet().intValue(), cap.getValue().getHomeGoals());

		verify(matchService, playerService, competitionService);

		reset(matchService, competitionService);
		replay(matchService, competitionService);
		match1.setGuestBet(null);
		matchFacade.placeBet(match1);
		verify(matchService, competitionService);
	}

	@Test
	public void testGetLatestNews()
	{
		final PlayerModel curPlayer = new PlayerModel();

		expect(playerService.getCurrentPlayer()).andReturn(curPlayer);
		expect(matchService.getLatestNews(curPlayer, 10)).andReturn(Collections.EMPTY_LIST);
		replay(matchService, playerService, i18nService, sessionService);

		List<NewsData> result = matchFacade.getLatestNews(10);

		assertTrue(result.isEmpty());

		verify(matchService, playerService, i18nService, sessionService);
		reset(matchService, playerService, i18nService, sessionService);

		final NewsModel news1 = new NewsModel();
		getContext(news1).setLocaleProvider(new StubLocaleProvider(Locale.GERMAN));
		news1.setCreationtime(new Date());
		news1.setContent("test");

		expect(playerService.getCurrentPlayer()).andReturn(curPlayer);
		expect(matchService.getLatestNews(curPlayer, 10)).andReturn(Arrays.asList(news1));
		final Capture<SessionExecutionBody> cap = new Capture<SessionExecutionBody>();
		expect(sessionService.executeInLocalView(capture(cap))).andReturn(null);
		i18nService.setLocalizationFallbackEnabled(true);
		expectLastCall();
		replay(matchService, playerService, i18nService, sessionService);

		result = matchFacade.getLatestNews(10);
		cap.getValue().executeWithoutResult();

		assertEquals(1, result.size());
		assertEquals(news1.getCreationtime(), result.get(0).getCreationTime());
		assertEquals(news1.getContent(), result.get(0).getContent());

		verify(matchService, playerService, i18nService, sessionService);
	}

	@Test
	public void testGetClosedBets()
	{
		final CountryFlagModel countrFlag = new CountryFlagModel();
		ModelUtils.setFieldValue(countrFlag, "_downloadurl", "url11");
		getContext(countrFlag).getValueHistory().markDirty("downloadurl");

		final CountryModel country = new CountryModel();
		country.setFlag(countrFlag);

		final ProfilePictureModel profile1 = new ProfilePictureModel();
		ModelUtils.setFieldValue(profile1, "_downloadurl", "url1");
		getContext(profile1).getValueHistory().markDirty("downloadurl");

		final PlayerModel player1 = new PlayerModel();
		player1.setUid("test1");
		player1.setName("mane1");
		player1.setCountry(country);
		player1.setProfilePicture(profile1);

		final ProfilePictureModel profile2 = new ProfilePictureModel();
		ModelUtils.setFieldValue(profile2, "_downloadurl", "url2");
		getContext(profile2).getValueHistory().markDirty("downloadurl");

		final PlayerModel player2 = new PlayerModel();
		player2.setUid("test2");
		player2.setName("mane2");
		player2.setProfilePicture(profile2);
		player2.setCountry(country);

		final CompetitionModel comp = new CompetitionModel();
		final GroupModel group = new GroupModel();
		group.setCompetition(comp);
		final MatchModel match = new MatchModel();
		match.setId(1);
		match.setGroup(group);

		final MatchBetModel bet1 = new MatchBetModel();
		bet1.setPlayer(player1);
		bet1.setMatch(match);
		bet1.setHomeGoals(1);
		bet1.setGuestGoals(2);

		final MatchBetModel bet2 = new MatchBetModel();
		bet2.setPlayer(player2);
		bet2.setMatch(match);
		bet2.setHomeGoals(2);
		bet2.setGuestGoals(3);

		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(matchService.getMatch(comp, match.getId())).andReturn(match);
		expect(Integer.valueOf(matchService.getScore(bet1))).andReturn(Integer.valueOf(1));
		expect(Integer.valueOf(matchService.getScore(bet2))).andReturn(Integer.valueOf(2));
		expect(matchService.getBets(match)).andReturn(Arrays.asList(bet1, bet2));
		expect(Long.valueOf(matchService.getTimeToBet(match))).andReturn(Long.valueOf(0));
		expect(Boolean.valueOf(matchService.isMatchFinished(match))).andReturn(Boolean.TRUE).times(2);
		replay(matchService, competitionService);

		final List<BetData> result = matchFacade.getClosedBets(match.getId());
		assertEquals(2, result.size());
		assertEquals(player1.getUid(), result.get(0).getPlayerId());
		assertEquals(player1.getName(), result.get(0).getPlayerName());
		assertEquals(player1.getProfilePicture().getDownloadURL(), result.get(0).getPlayerPictureUrl());
		assertEquals(Integer.valueOf(bet1.getGuestGoals()), result.get(0).getGuestBet());
		assertEquals(Integer.valueOf(bet1.getHomeGoals()), result.get(0).getHomeBet());
		assertTrue(result.get(0).isMatchFinished());
		assertEquals(player1.getCountry().getFlag().getDownloadURL(), result.get(0).getPlayerFlagUrl());
		assertEquals(1, result.get(0).getScore());

		assertEquals(player2.getUid(), result.get(1).getPlayerId());
		assertEquals(player2.getName(), result.get(1).getPlayerName());
		assertEquals(player2.getProfilePicture().getDownloadURL(), result.get(1).getPlayerPictureUrl());
		assertEquals(Integer.valueOf(bet2.getGuestGoals()), result.get(1).getGuestBet());
		assertEquals(Integer.valueOf(bet2.getHomeGoals()), result.get(1).getHomeBet());
		assertEquals(player2.getCountry().getFlag().getDownloadURL(), result.get(1).getPlayerFlagUrl());
		assertTrue(result.get(1).isMatchFinished());
		assertEquals(2, result.get(1).getScore());

		verify(matchService, competitionService);
	}

	private ItemModelContextImpl getContext(final AbstractItemModel model)
	{
		final ItemModelContextImpl context = (ItemModelContextImpl) ModelContextUtils.getItemModelContext(model);
		return context;
	}

}

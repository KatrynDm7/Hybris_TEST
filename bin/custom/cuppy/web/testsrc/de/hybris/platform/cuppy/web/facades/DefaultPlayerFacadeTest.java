/**
 * 
 */
package de.hybris.platform.cuppy.web.facades;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.CountryFlagModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.RankingData;
import de.hybris.platform.cuppy.services.RankingEntryData;
import de.hybris.platform.cuppy.web.converters.CompetitionConverter;
import de.hybris.platform.cuppy.web.converters.PlayerProfileConverter;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.cuppy.web.data.PlayerRankingData;
import de.hybris.platform.cuppy.web.facades.impl.DefaultPlayerFacade;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.converter.util.ModelUtils;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultPlayerFacadeTest
{
	private DefaultPlayerFacade playerFacade;
	private PlayerService playerService;
	private CommonI18NService commonI18NService;
	private UserService userService;
	private CompetitionService competitionService;

	/**
	 * Sets up the controller to test with mocked dependencies.
	 */
	@Before
	public void setUp()
	{
		playerService = createMock(PlayerService.class);
		commonI18NService = createMock(CommonI18NService.class);
		userService = createMock(UserService.class);
		competitionService = createMock(CompetitionService.class);

		playerFacade = new DefaultPlayerFacade();
		playerFacade.setPlayerService(playerService);
		playerFacade.setCommonI18NService(commonI18NService);
		playerFacade.setUserService(userService);
		playerFacade.setCompetitionService(competitionService);

		final CompetitionConverter competitionConverter = new CompetitionConverter();
		competitionConverter.setCompetitionService(competitionService);
		competitionConverter.setPlayerService(playerService);
		playerFacade.setCompetitionConverter(competitionConverter);

		final PlayerProfileConverter playerProfileConverter = new PlayerProfileConverter();
		playerProfileConverter.setPlayerService(playerService);
		playerFacade.setPlayerConverter(playerProfileConverter);
	}

	@Test
	public void testRankingNoUser()
	{
		final CompetitionModel comp = new CompetitionModel();
		final RankingData ranking = new RankingData();
		ranking.setCompetition(comp);
		ranking.setEntries(Collections.EMPTY_LIST);

		final ProfilePictureModel defaultPicture = new ProfilePictureModel();
		ModelUtils.setFieldValue(defaultPicture, "_downloadURL", "default");
		getContext(defaultPicture).getValueHistory().markDirty("downloadURL");

		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(playerService.getRanking(comp)).andReturn(ranking);
		expect(playerService.getDefaultProfilePicture()).andReturn(defaultPicture);
		replay(playerService, competitionService);

		final List<PlayerRankingData> rankings = playerFacade.getRankings();
		assertTrue(rankings.isEmpty());

		verify(playerService, competitionService);
	}

	@Test
	public void testRanking()
	{
		final CompetitionModel comp = new CompetitionModel();

		final List<PlayerModel> players = new ArrayList<PlayerModel>();
		players.add(buildPlayer(null, "http://pic1", "de", "player1"));
		players.add(buildPlayer("http://flag2", null, "gb", "player2"));
		players.add(buildPlayer("http://flag3", "http://pic3", "us", "player3"));
		players.add(buildPlayer("http://flag4", "http://pic4", "us", "player4"));

		final ProfilePictureModel defaultPicture = new ProfilePictureModel();
		ModelUtils.setFieldValue(defaultPicture, "_downloadURL", "default");
		getContext(defaultPicture).getValueHistory().markDirty("downloadURL");

		final RankingData ranking = new RankingData();
		ranking.setCompetition(comp);
		ranking.setEntries(new ArrayList<RankingEntryData>());

		RankingEntryData entry = new RankingEntryData();
		entry.setPlayer(players.get(2));
		entry.setScore(9);
		entry.setRank(1);
		entry.setLastScore(4);
		entry.setLastRank(1);
		ranking.getEntries().add(entry);

		entry = new RankingEntryData();
		entry.setPlayer(players.get(0));
		entry.setScore(7);
		entry.setRank(2);
		entry.setLastScore(1);
		entry.setLastRank(4);
		ranking.getEntries().add(entry);

		entry = new RankingEntryData();
		entry.setPlayer(players.get(3));
		entry.setScore(7);
		entry.setRank(2);
		entry.setLastScore(3);
		entry.setLastRank(2);
		ranking.getEntries().add(entry);

		entry = new RankingEntryData();
		entry.setPlayer(players.get(1));
		entry.setScore(3);
		entry.setRank(4);
		entry.setLastScore(2);
		entry.setLastRank(3);
		ranking.getEntries().add(entry);

		expect(competitionService.getCurrentCompetition()).andReturn(comp);
		expect(playerService.getRanking(comp)).andReturn(ranking);
		expect(Boolean.valueOf(playerService.isPlayerOnline(players.get(0).getUid()))).andReturn(Boolean.TRUE);
		expect(Boolean.valueOf(playerService.isPlayerOnline(players.get(1).getUid()))).andReturn(Boolean.TRUE);
		expect(Boolean.valueOf(playerService.isPlayerOnline(players.get(2).getUid()))).andReturn(Boolean.TRUE);
		expect(Boolean.valueOf(playerService.isPlayerOnline(players.get(3).getUid()))).andReturn(Boolean.FALSE);
		expect(playerService.getDefaultProfilePicture()).andReturn(defaultPicture);
		replay(playerService, competitionService);

		final List<PlayerRankingData> rankings = playerFacade.getRankings();
		assertEquals(4, rankings.size());

		assertEquals("player3", rankings.get(0).getPlayerName());
		assertEquals(1, rankings.get(0).getRank());
		assertEquals(1, rankings.get(0).getLastRank());
		assertEquals(9, rankings.get(0).getScore());
		assertEquals(4, rankings.get(0).getLastScore());
		assertEquals("http://flag3", rankings.get(0).getFlagUrl());
		assertEquals("http://pic3", rankings.get(0).getPictureUrl());
		assertEquals(new Locale("", "US"), rankings.get(0).getLocale());
		assertEquals("player3", rankings.get(0).getPlayerEMail());
		assertTrue(rankings.get(0).isPlayerOnline());

		assertEquals("player1", rankings.get(1).getPlayerName());
		assertEquals(2, rankings.get(1).getRank());
		assertEquals(4, rankings.get(1).getLastRank());
		assertEquals(7, rankings.get(1).getScore());
		assertEquals(1, rankings.get(1).getLastScore());
		assertNull(rankings.get(1).getFlagUrl());
		assertEquals("http://pic1", rankings.get(1).getPictureUrl());
		assertEquals(new Locale("", "DE"), rankings.get(1).getLocale());
		assertTrue(rankings.get(1).isPlayerOnline());

		assertEquals("player4", rankings.get(2).getPlayerName());
		assertEquals(7, rankings.get(2).getScore());
		assertEquals(3, rankings.get(2).getLastScore());
		assertEquals(2, rankings.get(2).getRank());
		assertEquals(2, rankings.get(2).getLastRank());
		assertFalse(rankings.get(2).isPlayerOnline());

		assertEquals("player2", rankings.get(3).getPlayerName());
		assertEquals(4, rankings.get(3).getRank());
		assertEquals(3, rankings.get(3).getLastRank());
		assertEquals(2, rankings.get(3).getLastScore());
		assertEquals(3, rankings.get(3).getScore());
		assertEquals("http://flag2", rankings.get(3).getFlagUrl());
		assertEquals("default", rankings.get(3).getPictureUrl());
		assertEquals(new Locale("", "GB"), rankings.get(3).getLocale());
		assertTrue(rankings.get(3).isPlayerOnline());

		verify(playerService, competitionService);
	}

	private PlayerModel buildPlayer(final String flagUrl, final String pictureUrl, final String countryCode,
			final String playerName)
	{
		final CountryFlagModel flag = new CountryFlagModel();
		ModelUtils.setFieldValue(flag, "_downloadURL", flagUrl);
		getContext(flag).getValueHistory().markDirty("downloadURL");

		final CountryModel country = new CountryModel();
		country.setIsocode(countryCode);
		if (flagUrl != null)
		{
			country.setFlag(flag);
		}

		final ProfilePictureModel profile = new ProfilePictureModel();
		ModelUtils.setFieldValue(profile, "_downloadURL", pictureUrl);
		getContext(profile).getValueHistory().markDirty("downloadURL");

		final PlayerModel player = new PlayerModel();
		player.setUid(playerName);
		player.setName(playerName);
		player.setCountry(country);
		if (pictureUrl != null)
		{
			player.setProfilePicture(profile);
		}
		player.setEMail(playerName);

		return player;
	}



	@Test
	public void testRegistration()
	{
		final PlayerProfileData data = new PlayerProfileData();
		data.setId("testId");
		data.setEMail("testMail");
		data.setLocale(Locale.GERMANY);
		data.setName("testName");
		data.setPassword("pwd");

		final CountryModel country = new CountryModel();
		country.setIsocode("DE");
		final LanguageModel lang = new LanguageModel();
		lang.setIsocode("de");

		final PlayerModel player = new PlayerModel();
		getContext(player).setLocaleProvider(new StubLocaleProvider(Locale.GERMANY));

		final Capture<PlayerModel> cap = new Capture<PlayerModel>();
		expect(commonI18NService.getCountry("DE")).andReturn(country);
		expect(commonI18NService.getLanguage("de")).andReturn(lang);
		expect(playerService.createPlayer()).andReturn(player);
		userService.setPassword(data.getId(), data.getPassword());
		expectLastCall();
		playerService.registerPlayer(capture(cap));
		expectLastCall();
		replay(commonI18NService, playerService, userService);

		playerFacade.registerPlayer(data);

		assertEquals(data.getId(), cap.getValue().getUid());
		assertEquals(data.getEMail(), cap.getValue().getEMail());
		assertEquals(data.getLocale().getCountry(), cap.getValue().getCountry().getIsocode());
		assertEquals(data.getName(), cap.getValue().getName());
		assertEquals(data.getLocale().getLanguage(), cap.getValue().getSessionLanguage().getIsocode());

		verify(commonI18NService, playerService, userService);
	}

	@Test
	public void testGetAllCountries()
	{
		final CountryModel country1 = new CountryModel();
		country1.setIsocode("DE");

		final CountryModel country2 = new CountryModel();
		country2.setIsocode("EN");

		final TeamModel country3 = new TeamModel();
		country2.setIsocode("AT");

		final List<CountryModel> countries = Arrays.asList(country1, country2, country3);
		expect(commonI18NService.getAllCountries()).andReturn(countries);
		replay(commonI18NService);

		final List<Locale> result = playerFacade.getAllCountries();
		assertEquals(2, result.size());
		assertEquals(country1.getIsocode(), result.get(0).getCountry());
		assertEquals(country2.getIsocode(), result.get(1).getCountry());

		verify(commonI18NService);
	}

	@Test
	public void testGetCurrentPlayer()
	{
		final PlayerModel player = buildPlayer("http://flag1", "http://pic1", "de", "player1");
		expect(playerService.getCurrentPlayer()).andReturn(player);
		replay(playerService);

		final PlayerProfileData result = playerFacade.getCurrentPlayer();
		assertEquals("player1", result.getName());
		assertEquals("http://pic1", result.getPictureUrl());
		assertEquals(new Locale("", "DE"), result.getLocale());
		verify(playerService);
	}

	@Test
	public void testUpdatePlayer()
	{
		final PlayerModel player = buildPlayer("http://flag1", "http://pic1", "de", "player1");

		final PlayerProfileData update = new PlayerProfileData();
		update.setId("player1");
		update.setName("newName");
		update.setEMail("newEmail");
		update.setLocale(new Locale("", "EN"));
		update.setPassword("newPass");

		final CountryModel updatedCountry = new CountryModel();

		expect(playerService.getCurrentPlayer()).andReturn(player);
		expect(commonI18NService.getCountry("EN")).andReturn(updatedCountry);
		userService.setPassword(player.getUid(), update.getPassword());
		expectLastCall();
		playerService.updatePlayer(player);
		expectLastCall();
		replay(playerService, commonI18NService, userService);

		playerFacade.updatePlayer(update);

		assertEquals(update.getName(), player.getName());
		assertEquals(updatedCountry, player.getCountry());
		assertEquals(update.getEMail(), player.getEMail());
		verify(playerService, commonI18NService, userService);
	}

	@Test
	public void testIsCurrentPlayerAdmin()
	{
		final PlayerModel admin = new PlayerModel();
		final UserGroupModel group = new UserGroupModel();
		admin.setGroups(Collections.singleton((PrincipalGroupModel) group));

		expect(userService.getUserGroupForUID(CuppyConstants.USERGROUP_CUPPYADMINS)).andReturn(group);
		expect(playerService.getCurrentPlayer()).andReturn(admin);
		replay(userService, playerService);
		final boolean result = playerFacade.isCurrentPlayerAdmin();
		assertTrue(result);
		verify(userService, playerService);
	}

	@Test
	public void testUploadProfilePicture()
	{
		final PlayerModel player = new PlayerModel();
		final ProfilePictureModel pic = new ProfilePictureModel();
		ModelUtils.setFieldValue(pic, "_downloadurl", "test");
		getContext(pic).getValueHistory().markDirty("downloadurl");
		player.setProfilePicture(pic);

		final byte[] data = null;
		final String fileName = null;

		expect(playerService.getCurrentPlayer()).andReturn(player);
		playerService.uploadProfilePicture(player, data, fileName);
		expectLastCall();
		replay(playerService);

		final String result = playerFacade.uploadProfilePicture(data, fileName);
		assertEquals(pic.getDownloadURL(), result);

		verify(playerService);
	}

	private ItemModelContextImpl getContext(final AbstractItemModel model)
	{
		final ItemModelContextImpl context = (ItemModelContextImpl) ModelContextUtils.getItemModelContext(model);
		return context;
	}

}

/**
 * 
 */
package de.hybris.platform.cuppy.services;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.daos.PlayerDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.impl.DefaultPlayerService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultPlayerServiceTest
{
	private DefaultPlayerService playerService;
	private ModelService modelService;
	private UserService userService;
	private PlayerDao playerDao;
	private MailService mailService;

	@Before
	public void setUp()
	{
		playerDao = createMock(PlayerDao.class);
		modelService = createMock(ModelService.class);
		userService = createMock(UserService.class);
		mailService = createMock(MailService.class);

		playerService = new DefaultPlayerService();
		playerService.setModelService(modelService);
		playerService.setUserService(userService);
		playerService.setPlayerDao(playerDao);
		playerService.setMailService(mailService);
	}

	@Test
	public void testPlayerConfirmed()
	{
		final PlayerModel player = new PlayerModel();
		player.setUid("test");
		assertFalse(player.isConfirmed());

		modelService.save(player);
		expectLastCall();
		mailService.sendConfirmationMail(player);
		expectLastCall();
		replay(modelService, mailService);

		playerService.confirmPlayer(player);

		assertTrue(player.isConfirmed());
		verify(modelService, mailService);
	}

	@Test
	public void testGetCurrentUser()
	{
		final PlayerModel player = new PlayerModel();
		player.setUid("test");

		expect(userService.getCurrentUser()).andReturn(player);
		replay(userService);

		final UserModel user = playerService.getCurrentPlayer();
		assertSame(player, user);

		verify(userService);
	}

	@Test
	public void testGetPlayers()
	{
		final CompetitionModel competition = new CompetitionModel();

		final PlayerModel player1 = new PlayerModel();
		player1.setCompetitions(Collections.singleton(competition));
		player1.setUid("player1");
		final PlayerModel player2 = new PlayerModel();
		player2.setCompetitions(Collections.singleton(competition));
		player2.setUid("player1");
		final PlayerModel player3 = new PlayerModel();
		player3.setCompetitions(Collections.singleton(competition));
		player3.setUid("player1");
		final List<PlayerModel> expect = Arrays.asList(player1, player2, player3);

		expect(playerDao.findAllPlayers(competition)).andReturn(expect);
		replay(playerDao);

		final List<PlayerModel> result = playerService.getPlayers(competition);
		assertEquals(expect, result);

		verify(playerDao);
	}

	@Test
	public void testCreatePlayer()
	{
		final PlayerModel player = new PlayerModel();

		expect(modelService.create(PlayerModel.class)).andReturn(player);
		replay(modelService);

		final PlayerModel result = playerService.createPlayer();
		assertSame(player, result);

		verify(modelService);
	}

	@Test
	public void testRegisterPlayer()
	{
		final PlayerModel player = new PlayerModel();
		player.setUid("test");

		final PlayerModel admin = new PlayerModel();
		admin.setUid("cuppyadmin");

		final UserGroupModel adminGroup = new UserGroupModel();
		adminGroup.setMembers((Set) Collections.singleton(admin));

		final Capture<PlayerModel> cap = new Capture<PlayerModel>();
		modelService.save(capture(cap));
		expectLastCall();
		mailService.sendRegistrationMail(player, Collections.singletonList(admin));
		expectLastCall();
		expect(userService.getUserGroupForUID(CuppyConstants.USERGROUP_CUPPYADMINS)).andReturn(adminGroup);
		expect(playerDao.findPlayerByUid(player.getUid())).andReturn(Collections.EMPTY_LIST);
		replay(modelService, playerDao, mailService, userService);

		playerService.registerPlayer(player);
		assertSame(player, cap.getValue());
		assertNull(cap.getValue().getProfilePicture());

		verify(modelService, playerDao, mailService, userService);
	}
}

/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultMatchBetDaoTest extends AbstractDaoTest
{
	@Resource
	private MatchBetDao matchBetDao;
	@Resource
	private UserService userService;
	@Resource
	private MatchService matchService;
	@Resource
	private CompetitionService competitionService;
	@Resource
	private ModelService modelService;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	private CompetitionModel competition;

	@Before
	public void setUp() throws Exception //NOPMD
	{
		cuppySystemSetup.importBasics(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_BASICS, new String[]
		{ CuppyConstants.PARAM_BASICS_PLAYERS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP, CuppyConstants.PARAM_WC2002_BETS_PRELIMINARIES }), Type.NOTDEFINED,
				CuppyConstants.EXTENSIONNAME));

		competition = competitionService.getCompetition("wc2002");
	}

	@Test
	public void testGetMatchBet()
	{
		final PlayerModel player = (PlayerModel) userService.getUserForUID("sternthaler");

		final MatchModel match = matchService.getMatch(competition, 2);

		final List<MatchBetModel> bets = matchBetDao.findMatchBetByPlayerAndMatch(player, match);
		assertTrue(bets.size() == 1);
		assertEquals(match.getId(), bets.get(0).getMatch().getId());
		assertEquals(player.getUid(), bets.get(0).getPlayer().getUid());
		assertEquals(0, bets.get(0).getHomeGoals());
		assertEquals(2, bets.get(0).getGuestGoals());
	}

	@Test
	public void testGetFinishedMatchBetsByPlayer()
	{
		final PlayerModel player = (PlayerModel) userService.getUserForUID("sternthaler");

		List<MatchBetModel> bets = matchBetDao.findFinishedMatchBetsByPlayer(competition, player);
		assertTrue(bets.isEmpty());

		final MatchModel match1 = matchService.getMatch(competition, 9);
		match1.setHomeGoals(Integer.valueOf(1));
		match1.setGuestGoals(Integer.valueOf(2));
		modelService.save(match1);

		bets = matchBetDao.findFinishedMatchBetsByPlayer(competition, player);
		assertTrue(bets.size() == 1);
		assertEquals(match1.getId(), bets.get(0).getMatch().getId());

		final MatchModel match2 = matchService.getMatch(competition, 10);
		match2.setHomeGoals(Integer.valueOf(1));
		match2.setGuestGoals(Integer.valueOf(2));
		modelService.save(match2);

		bets = matchBetDao.findFinishedMatchBetsByPlayer(competition, player);
		assertTrue(bets.size() == 2);
		assertEquals(match1.getId(), bets.get(0).getMatch().getId());
		assertEquals(match2.getId(), bets.get(1).getMatch().getId());
	}
}

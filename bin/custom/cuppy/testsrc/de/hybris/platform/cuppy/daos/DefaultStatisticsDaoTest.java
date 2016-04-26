/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.enums.CompetitionType;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultStatisticsDaoTest extends AbstractDaoTest
{
	@Resource
	private StatisticsDao statisticsDao;

	@Resource
	private MatchService matchService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private CompetitionService competitionService;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	@Before
	public void setUp() throws Exception //NOPMD
	{
		cuppySystemSetup.importBasics(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_BASICS, new String[]
		{ CuppyConstants.PARAM_BASICS_PLAYERS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP, CuppyConstants.PARAM_WC2002_RESULTS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2002_RESULTS_FINALS, CuppyConstants.PARAM_WC2002_BETS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2002_BETS_FINALS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
	}

	@Test
	public void testGetPLayersCount()
	{
		assertEquals(19, statisticsDao.getPlayersCount());
	}

	@Test
	public void testGetPlayersNotPlacedBetsForMatchCount()
	{
		CompetitionModel comp = new CompetitionModel();
		comp.setCode("test");
		comp.setType(CompetitionType.LEAGUE);
		modelService.save(comp);

		final PlayerModel player = new PlayerModel();
		player.setGroups(Collections.<PrincipalGroupModel> singleton(userService
				.getUserGroupForUID(CuppyConstants.USERGROUP_CUPPYPLAYERS)));
		player.setCompetitions(Collections.singleton(comp));

		comp = competitionService.getCompetition("wc2002");
		final MatchModel match = matchService.getMatch(comp, 1);
		assertEquals(2, statisticsDao.getPlayersNotPlacedBetsForMatchCount(match));
	}

	@Test
	public void testGetPlayersPlacedBetsForMatchCount()
	{
		final CompetitionModel comp = competitionService.getCompetition("wc2002");
		final MatchModel match = matchService.getMatch(comp, 1);
		assertEquals(17, statisticsDao.getPlayersPlacedBetsForMatchCount(match));
	}
}

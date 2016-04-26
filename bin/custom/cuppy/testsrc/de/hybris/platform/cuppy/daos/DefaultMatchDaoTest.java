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
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultMatchDaoTest extends AbstractDaoTest
{
	@Resource
	private MatchDao matchDao;

	@Resource
	private MatchService matchService;

	@Resource
	private CompetitionService competitionService;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	private CompetitionModel competition;

	@Before
	public void setUp() throws Exception //NOPMD
	{
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		competition = competitionService.getCompetition("wc2002");
	}

	@Test
	public void testFindMatchById()
	{
		List<MatchModel> matches = matchDao.findMatchById(competition, 33);
		assertEquals(1, matches.size());
		assertEquals(33, matches.get(0).getId());
		assertEquals("DEN", matches.get(0).getHomeTeam().getIsocode());
		assertEquals("A", matches.get(0).getGroup().getCode());

		matches = matchDao.findMatchById(competition, 80);
		assertTrue(matches.isEmpty());
	}

	@Test
	public void testFindMatches()
	{
		final List<MatchModel> matches = matchDao.findMatches(competition);
		assertEquals(64, matches.size());
		assertEquals(2, matches.get(1).getId());
		assertEquals("IRL", matches.get(1).getHomeTeam().getIsocode());
		assertEquals("E", matches.get(1).getGroup().getCode());
	}

	@Test
	public void testFindMatchesByGroup()
	{
		GroupModel group = matchService.getGroup(competition, "B");

		List<MatchModel> matches = matchDao.findMatchesByGroup(group);
		assertEquals(6, matches.size());
		assertEquals(8, matches.get(1).getId());
		assertEquals("ESP", matches.get(1).getHomeTeam().getIsocode());
		assertEquals("B", matches.get(1).getGroup().getCode());

		group = matchService.getGroup(competition, "FINAL");
		matches = matchDao.findMatchesByGroup(group);
		assertEquals(1, matches.size());
		assertEquals(64, matches.get(0).getId());
		assertEquals("61-1", matches.get(0).getHomeTeam().getIsocode());
		assertEquals("FINAL", matches.get(0).getGroup().getCode());
	}

	@Test
	public void testFindFinishedMatches() throws Exception //NOPMD
	{
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_RESULTS_PRELIMINARIES }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		final List<MatchModel> matches = matchDao.findFinishedMatches(competition);
		assertEquals(6 * 8, matches.size());
		assertEquals(2, matches.get(1).getId());
		assertEquals("IRL", matches.get(1).getHomeTeam().getIsocode());
		assertEquals("E", matches.get(1).getGroup().getCode());
	}
}

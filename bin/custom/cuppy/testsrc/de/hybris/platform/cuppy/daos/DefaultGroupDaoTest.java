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
import de.hybris.platform.cuppy.services.CompetitionService;
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
public class DefaultGroupDaoTest extends AbstractDaoTest
{
	@Resource
	private GroupDao groupDao;
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
	public void testGetGroups()
	{
		final List<GroupModel> groups = groupDao.findGroups(competition);
		assertTrue(groups.size() == 13);
	}

	@Test
	public void testGetGroup()
	{
		List<GroupModel> groups = groupDao.findGroupByCode(competition, "B");
		assertTrue(groups.size() == 1);
		assertEquals("B", groups.get(0).getCode());

		groups = groupDao.findGroupByCode(competition, "SEMI");
		assertTrue(groups.size() == 1);
		assertEquals("SEMI", groups.get(0).getCode());

		groups = groupDao.findGroupByCode(competition, "test");
		assertTrue(groups.isEmpty());
	}
}

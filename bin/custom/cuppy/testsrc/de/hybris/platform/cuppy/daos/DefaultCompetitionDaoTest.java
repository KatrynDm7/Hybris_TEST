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
public class DefaultCompetitionDaoTest extends AbstractDaoTest
{
	@Resource
	private CompetitionDao competitionDao;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	@Before
	public void setUp() throws Exception //NOPMD
	{
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importWC2010(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2010, new String[]
		{ CuppyConstants.PARAM_WC2010_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
	}

	@Test
	public void testGetCompetitions()
	{
		final List<CompetitionModel> comps = competitionDao.findCompetitions();
		assertTrue(comps.size() == 3);
	}

	@Test
	public void testGetCompetition()
	{
		List<CompetitionModel> comps = competitionDao.findCompetitionByCode("wc2002");
		assertTrue(comps.size() == 1);
		assertEquals("wc2002", comps.get(0).getCode());

		comps = competitionDao.findCompetitionByCode("ec2008");
		assertTrue(comps.size() == 1);
		assertEquals("ec2008", comps.get(0).getCode());

		comps = competitionDao.findCompetitionByCode("test");
		assertTrue(comps.isEmpty());
	}
}

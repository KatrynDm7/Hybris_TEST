/**
 * 
 */
package de.hybris.platform.cuppy.data;

import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class WomensWorldCup2011DataTest extends AbstractCompetitionDataTest
{
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	public WomensWorldCup2011DataTest()
	{
		super("wwc2011");
	}

	@Before
	public void setUpDate() throws Exception //NOPMD
	{
		cuppySystemSetup.importWWC2011(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WWC2011, new String[]
		{ CuppyConstants.PARAM_WWC2011_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
	}

	@Test
	public void testMatchData() throws Exception //NOPMD
	{
		testTeams(16, 0);
	}
}

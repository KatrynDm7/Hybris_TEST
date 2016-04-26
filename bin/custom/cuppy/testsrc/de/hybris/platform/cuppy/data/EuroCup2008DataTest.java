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
public class EuroCup2008DataTest extends AbstractCompetitionDataTest
{
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	public EuroCup2008DataTest()
	{
		super("ec2008");
	}

	@Before
	public void setUpDate() throws Exception //NOPMD
	{
		cuppySystemSetup.importBasics(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_BASICS, new String[]
		{ CuppyConstants.PARAM_BASICS_PLAYERS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
	}

	@Test
	public void testMatchData() throws Exception //NOPMD
	{
		testTeams(16, 14);
		testMatches(6);
	}

	@Test
	public void testResultsPreliminaries() throws Exception //NOPMD
	{
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_RESULTS_PRELIMINARIES }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		testPreliminaries();
	}

	@Test
	public void testResultsFinals() throws Exception //NOPMD
	{
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_RESULTS_FINALS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		testFinals();
	}

	@Test
	public void testResultsAll() throws Exception //NOPMD
	{
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_RESULTS_PRELIMINARIES, CuppyConstants.PARAM_EC2008_RESULTS_FINALS }), Type.NOTDEFINED,
				CuppyConstants.EXTENSIONNAME));

		testTeams(16, 14);
		testMatches(6);
	}

	@Test
	public void testBetsPreliminaries() throws Exception //NOPMD
	{
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_BETS_PRELIMINARIES }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		testBets(431);
	}

	@Test
	public void testBetsFinals() throws Exception //NOPMD
	{
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_BETS_FINALS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		testBets(129);
	}

	@Test
	public void testBetsAll() throws Exception //NOPMD
	{
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_BETS_PRELIMINARIES, CuppyConstants.PARAM_EC2008_BETS_FINALS }), Type.NOTDEFINED,
				CuppyConstants.EXTENSIONNAME));

		testBets(431 + 129);
	}

	@Test
	public void testGroups() throws Exception //NOPMD
	{
		testGroups(7);
	}
}

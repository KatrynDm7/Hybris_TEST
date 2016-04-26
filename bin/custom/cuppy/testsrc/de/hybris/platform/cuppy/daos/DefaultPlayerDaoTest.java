/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;
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
public class DefaultPlayerDaoTest extends AbstractDaoTest
{
	@Resource
	private PlayerDao playerDao;

	@Resource
	private CompetitionService competitionService;
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
	public void testGetAll()
	{
		final List<PlayerModel> players = playerDao.findAllPlayers(competition);
		assertTrue(players.size() > 2);

		assertEquals("abrode_cuppy", players.get(0).getUid());
		assertEquals("Arin Brode", players.get(0).getName());
		assertEquals("CH", players.get(0).getCountry().getIsocode());
		assertEquals("de", players.get(0).getSessionLanguage().getIsocode());
		assertEquals("arin@wow-europe.com", players.get(0).getEMail());
		assertNull(players.get(0).getProfilePicture());

		assertEquals("ahertz_cuppy", players.get(1).getUid());
		assertEquals("Anja Hertz", players.get(1).getName());
		assertEquals("SE", players.get(1).getCountry().getIsocode());
		assertEquals("en", players.get(1).getSessionLanguage().getIsocode());
		assertEquals("ahertz@na-du.de", players.get(1).getEMail());
		assertNull(players.get(1).getProfilePicture());

		assertEquals("ariel_cuppy", players.get(2).getUid());
		assertEquals("Ariel F. Lüdi", players.get(2).getName());
		assertEquals("CH", players.get(2).getCountry().getIsocode());
		assertEquals("de", players.get(2).getSessionLanguage().getIsocode());
		assertEquals("ariel@cuppy.de", players.get(2).getEMail());
		assertNull(players.get(2).getProfilePicture());
	}

	@Test
	public void testGetPictureByCode()
	{
		final List<ProfilePictureModel> pictures = playerDao.findProfilePictureByCode("default");
		assertEquals(1, pictures.size());
		assertNotNull(pictures.get(0).getDownloadURL());
	}
}

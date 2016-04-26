/**
 * 
 */
package de.hybris.platform.cuppy.web.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.web.data.GroupData;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.facades.MatchFacade;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
public class MatchFacadeTest extends AbstractCuppyIntegrationTest
{
	@Autowired(required = true)
	private MatchFacade matchFacade;

	@Autowired(required = true)
	private PlayerFacade playerFacade;

	@Autowired(required = true)
	private MatchService matchService;

	@Autowired(required = true)
	private PlayerService playerService;

	@Autowired(required = true)
	private ModelService modelService;

	@Autowired(required = true)
	private CompetitionService competitionService;

	@Before
	public void setUp()
	{
		playerFacade.setCurrentCompetition("wc2002");
	}

	@Test
	public void testGetGroups()
	{
		final List<GroupData> groups = matchFacade.getGroups();
		assertEquals(13, groups.size());
		assertEquals("B", groups.get(1).getCode());
		assertEquals("G", groups.get(6).getCode());
	}

	@Test
	public void testGetMatches()
	{
		final List<MatchData> matches = matchFacade.getMatches();
		assertEquals(64, matches.size());

		assertEquals(1, matches.get(0).getId());
		assertEquals("A", matches.get(0).getGroup().getCode());
		assertEquals(Integer.valueOf(4), matches.get(0).getGuestBet());
		assertEquals(1, matches.get(0).getGuestGoals());
		assertEquals(Integer.valueOf(1), matches.get(0).getHomeBet());
		assertEquals(0, matches.get(0).getHomeGoals());
		assertEquals("/medias/fromjar/cuppy/flags/countries/sn.png?mime=image%2Fpng&realname=sn.png", matches.get(0)
				.getGuestFlagUrl());
		assertEquals("/medias/fromjar/cuppy/flags/countries/fr.png?mime=image%2Fpng&realname=fr.png", matches.get(0)
				.getHomeFlagUrl());
		assertEquals("Senegal", matches.get(0).getGuestTeam());
		assertEquals("Frankreich", matches.get(0).getHomeTeam());
		assertNotNull(matches.get(0).getDate());
		assertEquals("Seoul", matches.get(0).getLocation());
		assertFalse(matches.get(0).isMatchBetable());
	}

	@Test
	public void testGetMatchesByGroup()
	{
		final GroupData group = new GroupData();
		group.setCode("A");
		group.setCompetition("wc2002");

		final List<MatchData> matches = matchFacade.getMatches(group);
		assertEquals(6, matches.size());

		assertEquals(1, matches.get(0).getId());
		assertEquals("A", matches.get(0).getGroup().getCode());
		assertEquals(Integer.valueOf(4), matches.get(0).getGuestBet());
		assertEquals(1, matches.get(0).getGuestGoals());
		assertEquals(Integer.valueOf(1), matches.get(0).getHomeBet());
		assertEquals(0, matches.get(0).getHomeGoals());
		assertEquals("/medias/fromjar/cuppy/flags/countries/sn.png?mime=image%2Fpng&realname=sn.png", matches.get(0)
				.getGuestFlagUrl());
		assertEquals("/medias/fromjar/cuppy/flags/countries/fr.png?mime=image%2Fpng&realname=fr.png", matches.get(0)
				.getHomeFlagUrl());
		assertEquals("Senegal", matches.get(0).getGuestTeam());
		assertEquals("Frankreich", matches.get(0).getHomeTeam());
		assertNotNull(matches.get(0).getDate());
		assertEquals("Seoul", matches.get(0).getLocation());
		assertFalse(matches.get(0).isMatchBetable());
	}

	@Test
	public void testPlaceBets()
	{
		final CompetitionModel comp = competitionService.getCurrentCompetition();
		final MatchModel source = matchService.getMatch(comp, 41);

		final GroupData group = new GroupData();
		group.setCompetition("wc2002");

		final MatchData match = new MatchData();
		match.setId(41);
		match.setGuestBet(Integer.valueOf(1));
		match.setHomeBet(Integer.valueOf(1));
		match.setGroup(group);

		assertFalse(matchService.isBetable(source));
		matchFacade.placeBet(match);
		assertNull(matchService.getBet(source, playerService.getCurrentPlayer()));

		source.setDate(new Date(new Date().getTime() + (10000 * 1000)));
		modelService.save(source);
		assertFalse(matchService.isBetable(source));
		matchFacade.placeBet(match);
		assertNull(matchService.getBet(source, playerService.getCurrentPlayer()));

		source.setHomeGoals(null);
		source.setGuestGoals(null);
		modelService.save(source);
		assertTrue(matchService.isBetable(source));
		matchFacade.placeBet(match);
		final MatchBetModel bet = matchService.getBet(source, playerService.getCurrentPlayer());

		assertNotNull(bet);
		assertEquals(match.getGuestBet().intValue(), bet.getGuestGoals());
		assertEquals(match.getHomeBet().intValue(), bet.getHomeGoals());

		match.setGuestBet(Integer.valueOf(2));
		match.setHomeBet(Integer.valueOf(2));
		assertTrue(matchService.isBetable(source));
		matchFacade.placeBet(match);
		modelService.refresh(bet);
		assertEquals(match.getGuestBet().intValue(), bet.getGuestGoals());
		assertEquals(match.getHomeBet().intValue(), bet.getHomeGoals());
	}
}

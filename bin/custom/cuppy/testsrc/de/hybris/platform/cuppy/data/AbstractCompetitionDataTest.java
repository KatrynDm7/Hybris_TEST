/**
 * 
 */
package de.hybris.platform.cuppy.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.impex.systemsetup.ImpExSystemSetup;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;


/**
 * @author andreas.thaler
 * 
 */
@Ignore
public class AbstractCompetitionDataTest extends ServicelayerTransactionalTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ServiceLayerDataSetup serviceLayerDataSetup;
	@Resource
	private ImpExSystemSetup impExSystemSetup;

	private final String compName;

	public AbstractCompetitionDataTest(final String compName)
	{
		super();

		this.compName = compName;
	}

	@Before
	public void setUpBasics() throws Exception //NOPMD
	{
		new CoreBasicDataCreator().createEssentialData(null, null);
		CatalogManager.getInstance().createEssentialData(Collections.EMPTY_MAP, null);
		serviceLayerDataSetup.setup();
		impExSystemSetup.createAutoImpexEssentialData(new SystemSetupContext(Collections.EMPTY_MAP, Type.ESSENTIAL,
				CuppyConstants.EXTENSIONNAME));
	}

	protected void testTeams(final int teams, final int dummyTeams) throws Exception //NOPMD
	{
		assertEquals("Unexpected amount of real teams imported", teams,
				flexibleSearchService.search("SELECT {PK} FROM {" + TeamModel._TYPECODE + "!} WHERE {" + TeamModel.DUMMY + "}=false")
						.getTotalCount());

		assertEquals("Unexpected amount of dummy teams imported", dummyTeams,
				flexibleSearchService.search("SELECT {PK} FROM {" + TeamModel._TYPECODE + "!} WHERE {" + TeamModel.DUMMY + "}=true")
						.getTotalCount());

		assertEquals(
				"Not each team has a flag",
				0,
				flexibleSearchService.search(
						"SELECT {PK} FROM {" + TeamModel._TYPECODE + "!} WHERE {" + TeamModel.DUMMY + "}=false AND {" + TeamModel.FLAG
								+ "} IS NULL").getTotalCount());
	}

	protected void testBets(final int bets) throws Exception //NOPMD
	{
		assertEquals("Unexpected amount of bets imported", bets,
				flexibleSearchService.search("SELECT {PK} FROM {" + MatchBetModel._TYPECODE + "}").getTotalCount());
	}

	protected void testMatches(final int groupMatches) throws Exception //NOPMD
	{
		final List<GroupModel> groups = flexibleSearchService.<GroupModel> search(
				"SELECT {PK} FROM {Group} WHERE {" + GroupModel.COMPETITION + "}=" + getCompetition().getPk()).getResult();
		for (final GroupModel group : groups)
		{
			final List<MatchModel> matches = flexibleSearchService.<MatchModel> search("SELECT {PK} FROM {Match} WHERE {group}=?me",
					Collections.singletonMap("me", group)).getResult();
			if (group.getMultiplier() == 1)
			{
				assertEquals("Multiplier 1 group needs " + groupMatches + " matches", groupMatches, matches.size());
			}
		}
	}

	protected void testPreliminaries() throws Exception //NOPMD
	{
		final List<MatchModel> matches = flexibleSearchService.<MatchModel> search(
				"SELECT {match:" + MatchModel.PK + "} FROM {" + MatchModel._TYPECODE + " as match},{" + GroupModel._TYPECODE
						+ " as group} WHERE {match:" + MatchModel.GROUP + "}={group:" + GroupModel.PK + "} AND {group:"
						+ GroupModel.MULTIPLIER + "}=1 AND {group:" + GroupModel.COMPETITION + "}=" + getCompetition().getPk())
				.getResult();
		for (final MatchModel match : matches)
		{
			assertNotNull("Home goals not set", match.getHomeGoals());
			assertNotNull("Guest goals not set", match.getGuestGoals());
			assertFalse("Home team is a dummy", match.getHomeTeam().isDummy());
			assertFalse("Guest team is a dummy", match.getGuestTeam().isDummy());
		}
	}

	protected void testFinals() throws Exception //NOPMD
	{
		final List<MatchModel> matches = flexibleSearchService.<MatchModel> search(
				"SELECT {match:pk} FROM {Match as match},{Group as group} where {match:group}={group:pk} AND {group:"
						+ GroupModel.MULTIPLIER + "}=2 AND {group:" + GroupModel.COMPETITION + "}=" + getCompetition().getPk())
				.getResult();
		for (final MatchModel match : matches)
		{
			assertNotNull("Home goals not set for match " + match.getId(), match.getHomeGoals());
			assertNotNull("Guest goals not set", match.getGuestGoals());
			assertFalse("Home team is a dummy", match.getHomeTeam().isDummy());
			assertFalse("Guest team is a dummy", match.getGuestTeam().isDummy());
		}
	}

	protected void testGroups(final int count) throws Exception //NOPMD
	{
		final List<GroupModel> groups = flexibleSearchService.<GroupModel> search(
				"SELECT {pk} FROM {Group} where {" + GroupModel.COMPETITION + "}=" + getCompetition().getPk()).getResult();
		assertEquals("Unexpected amount of groups", count, groups.size());
		for (final GroupModel group : groups)
		{
			assertFalse("No matches for group " + group.getCode(), group.getMatches().isEmpty());
		}
	}

	private CompetitionModel getCompetition()
	{
		return flexibleSearchService
				.<CompetitionModel> search(
						"SELECT {PK} FROM {" + CompetitionModel._TYPECODE + "} WHERE {" + CompetitionModel.CODE + "}='" + compName
								+ "'").getResult().get(0);
	}
}

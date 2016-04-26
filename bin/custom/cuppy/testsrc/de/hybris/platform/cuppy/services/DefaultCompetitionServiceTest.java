/**
 * 
 */
package de.hybris.platform.cuppy.services;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.PlayerPreferencesModel;
import de.hybris.platform.cuppy.services.impl.DefaultCompetitionService;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.internal.dao.SortParameters.SortOrder;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests all methods of {@link DefaultCompetitionService}.
 * 
 * @author andreas.thaler
 * 
 */
public class DefaultCompetitionServiceTest
{
	/** Instance of class to test. */
	private DefaultCompetitionService competitionService;

	/** Mocked dependency. */
	private PlayerService playerService;
	private ModelService modelService;
	private GenericDao<CompetitionModel> competitionDao;

	/** Test data. */
	private CompetitionModel comp1;
	private CompetitionModel comp2;
	private PlayerModel player1;

	/**
	 * Sets up the mocked dependencies and prepares test data.
	 */
	@Before
	public void setUp()
	{
		competitionService = new DefaultCompetitionService();

		playerService = createMock(PlayerService.class);
		competitionService.setPlayerService(playerService);

		modelService = createMock(ModelService.class);
		competitionService.setModelService(modelService);

		competitionDao = createMock(GenericDao.class);
		competitionService.setGenericCompetitionDao(competitionDao);

		comp1 = new CompetitionModel();
		comp1.setCode("comp1");

		comp2 = new CompetitionModel();
		comp2.setCode("comp2");

		player1 = new PlayerModel();
		player1.setUid("player1");
		player1.setCompetitions(Collections.EMPTY_SET); //is default after save

	}

	/**
	 * Tests {@link DefaultCompetitionService#getAllCompetitions()}.
	 */
	@Test
	public void testGetAllCompetitions()
	{
		// test with no competitions in result
		final SortParameters sortParameters = new SortParameters();
		sortParameters.addSortParameter(CompetitionModel.CODE, SortOrder.ASCENDING);
		expect(competitionDao.find(sortParameters)).andReturn(Collections.EMPTY_LIST);
		replay(competitionDao);

		List<CompetitionModel> result = competitionService.getAllCompetitions();
		assertTrue(result.isEmpty());

		verify(competitionDao);
		reset(competitionDao);

		// test with 2 competitions in result
		expect(competitionDao.find(sortParameters)).andReturn(Arrays.asList(comp1, comp2));
		replay(competitionDao);

		result = competitionService.getAllCompetitions();
		assertEquals(2, result.size());
		assertEquals(comp1, result.get(0));
		assertEquals(comp1.getCode(), result.get(0).getCode());
		assertEquals(comp2, result.get(1));
		assertEquals(comp2.getCode(), result.get(1).getCode());

		verify(competitionDao);
	}

	/**
	 * Tests {@link DefaultCompetitionService#getCompetition(String)}.
	 */
	@Test
	public void testGetCompetition()
	{
		// test with unique competition for given code
		expect(competitionDao.find(Collections.singletonMap(CompetitionModel.CODE, comp1.getCode()))).andReturn(
				Arrays.asList(comp1));
		replay(competitionDao);

		final CompetitionModel result = competitionService.getCompetition(comp1.getCode());
		assertEquals(comp1, result);
		assertEquals(comp1.getCode(), result.getCode());

		verify(competitionDao);
		reset(competitionDao);

		// test with no competition for given code
		expect(competitionDao.find(Collections.singletonMap(CompetitionModel.CODE, comp1.getCode()))).andReturn(
				Collections.EMPTY_LIST);
		replay(competitionDao);

		try
		{
			competitionService.getCompetition(comp1.getCode());
			fail("Expected exception");
		}
		catch (final UnknownIdentifierException e) //NOPMD
		{
			//OK
		}

		verify(competitionDao);
		reset(competitionDao);

		//test with ambiguous competitions for given code
		expect(competitionDao.find(Collections.singletonMap(CompetitionModel.CODE, comp1.getCode()))).andReturn(
				Arrays.asList(comp1, comp2));
		replay(competitionDao);

		try
		{
			competitionService.getCompetition(comp1.getCode());
			fail("Expected exception");
		}
		catch (final AmbiguousIdentifierException e) //NOPMD
		{
			//OK
		}

		verify(competitionDao);
		reset(competitionDao);

		// test with null parameter
		try
		{
			competitionService.getCompetition(null);
			fail("Expected exception");
		}
		catch (final IllegalArgumentException e) //NOPMD
		{
			//OK
		}
	}

	/**
	 * Tests {@link DefaultCompetitionService#getActiveCompetitions()}.
	 */
	@Test
	public void testGetActiveCompetitions()
	{
		// test with no current player set
		expect(playerService.getCurrentPlayer()).andReturn(null);
		replay(playerService);

		List<CompetitionModel> result = competitionService.getActiveCompetitions();
		assertTrue(result.isEmpty());

		verify(playerService);
		reset(playerService);

		//test with no active competitions for current player
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		replay(playerService);

		result = competitionService.getActiveCompetitions();
		assertTrue(result.isEmpty());

		verify(playerService);
		reset(playerService);

		//test with 2 active competitions for current player
		final Set<CompetitionModel> comps = new LinkedHashSet<CompetitionModel>();
		comps.add(comp1);
		comps.add(comp2);
		player1.setCompetitions(comps);
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		replay(playerService);

		result = competitionService.getActiveCompetitions();
		Assert.assertCollection(comps, result);

		verify(playerService);
	}

	/**
	 * Tests {@link DefaultCompetitionService#setActiveCompetitions(List)}.
	 */
	@Test
	public void testSetActiveCompetitions()
	{
		// test with null parameter
		try
		{
			competitionService.setActiveCompetitions(null);
			fail("Expected exception");
		}
		catch (final IllegalArgumentException e) //NOPMD
		{
			//OK
		}

		// test with empty list of competitions on current player
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		modelService.save(player1);
		expectLastCall();
		replay(playerService);
		replay(modelService);

		competitionService.setActiveCompetitions(Collections.EMPTY_LIST);
		assertNotNull(player1.getCompetitions());
		assertTrue(player1.getCompetitions().isEmpty());

		verify(playerService);
		verify(modelService);
		reset(playerService);
		reset(modelService);

		// test with 2 competitions on current player
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		modelService.save(player1);
		expectLastCall();
		replay(playerService);
		replay(modelService);

		competitionService.setActiveCompetitions(Arrays.asList(comp1, comp2));
		assertNotNull(player1.getCompetitions());
		Assert.assertCollectionElements(player1.getCompetitions(), comp1, comp2);

		verify(playerService);
		verify(modelService);
		reset(playerService);
		reset(modelService);

		// test with no current player
		expect(playerService.getCurrentPlayer()).andReturn(null);
		replay(playerService);
		replay(modelService);

		player1.setCompetitions(Collections.singleton(comp1));
		competitionService.setActiveCompetitions(Arrays.asList(comp1, comp2));
		assertNotNull(player1.getCompetitions());
		Assert.assertCollectionElements(player1.getCompetitions(), comp1);

		verify(playerService);
		verify(modelService);
		reset(playerService);
		reset(modelService);
	}

	/**
	 * Tests {@link DefaultCompetitionService#getCurrentCompetition()}.
	 */
	@Test
	public void testGetCurrentCompetition()
	{
		// test with no current and no active competition set at current player
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		replay(playerService);

		try
		{
			competitionService.getCurrentCompetition();
		}
		catch (final NoCompetitionAvailableException e) //NOPMD
		{
			//OK
		}
		verify(playerService);
		reset(playerService);

		// test with no current player
		expect(playerService.getCurrentPlayer()).andReturn(null);
		replay(playerService);

		try
		{
			competitionService.getCurrentCompetition();
		}
		catch (final NoCompetitionAvailableException e) //NOPMD
		{
			//OK
		}
		verify(playerService);
		reset(playerService);

		// test with no current but with active competition set at current player
		player1.setCompetitions(Collections.singleton(comp2));
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		replay(playerService);

		CompetitionModel result = competitionService.getCurrentCompetition();
		assertNotNull(result);
		assertEquals(result, comp2);

		verify(playerService);
		reset(playerService);

		// test with no current (but with initialized Preferences) but with active competition set at current player
		final PlayerPreferencesModel prefs = new PlayerPreferencesModel();
		player1.setPreferences(prefs);
		player1.setCompetitions(Collections.singleton(comp2));
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		replay(playerService);

		result = competitionService.getCurrentCompetition();
		assertNotNull(result);
		assertEquals(result, comp2);

		verify(playerService);
		reset(playerService);

		// test with current and with active competition set at current player
		prefs.setCurrentCompetition(comp1);
		player1.setCompetitions(Collections.singleton(comp2));
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		replay(playerService);

		result = competitionService.getCurrentCompetition();
		assertNotNull(result);
		assertEquals(result, comp1);

		verify(playerService);
		reset(playerService);
	}

	/**
	 * Tests {@link DefaultCompetitionService#setCurrentCompetition(CompetitionModel)}.
	 */
	@Test
	public void testSetCurrentCompetition()
	{
		// test with null parameter
		try
		{
			competitionService.setCurrentCompetition(null);
			fail("Expected exception");
		}
		catch (final IllegalArgumentException e) //NOPMD
		{
			//OK
		}

		// test with no current player set
		expect(playerService.getCurrentPlayer()).andReturn(null);
		replay(playerService);

		competitionService.setCurrentCompetition(comp1);
		// nothing to assert..
		verify(playerService);
		reset(playerService);

		//test with no preferences initialized at current player
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		modelService.save(player1);
		expectLastCall();
		final PlayerPreferencesModel prefs = new PlayerPreferencesModel();
		modelService.save(prefs);
		expectLastCall();
		expect(modelService.create(PlayerPreferencesModel.class)).andReturn(prefs);
		replay(playerService);
		replay(modelService);

		competitionService.setCurrentCompetition(comp1);
		assertNotNull(player1.getPreferences());
		assertEquals(prefs, player1.getPreferences());
		assertEquals(comp1, player1.getPreferences().getCurrentCompetition());

		verify(modelService);
		verify(playerService);
		reset(playerService);
		reset(modelService);

		//test with initialized preferences but no current competition set at current player
		player1.getPreferences().setCurrentCompetition(null);
		expect(playerService.getCurrentPlayer()).andReturn(player1);
		replay(playerService);

		competitionService.setCurrentCompetition(comp1);
		assertNotNull(player1.getPreferences());
		assertEquals(comp1, player1.getPreferences().getCurrentCompetition());

		verify(playerService);
		reset(playerService);
	}
}

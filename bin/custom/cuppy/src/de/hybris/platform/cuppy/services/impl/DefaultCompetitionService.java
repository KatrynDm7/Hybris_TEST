package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.PlayerPreferencesModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Default implementation which uses a generic dao for finding competitions.
 */
@SingletonScopedComponent(value = "competitionService")
public class DefaultCompetitionService implements CompetitionService
{
	/** Used logger instance. */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultCompetitionService.class);

	/** Used for gathering current session player. */
	@Autowired
	private PlayerService playerService;
	/** Used for saving changes for current and active competitions. */
	@Autowired
	private ModelService modelService;
	/** Generic dao for searching competitions. */
	@Autowired
	private GenericDao<CompetitionModel> genericCompetitionDao;

	/**
	 * Delegates to dao.
	 */
	@Override
	public List<CompetitionModel> getAllCompetitions()
	{
		return genericCompetitionDao.find(SortParameters.singletonAscending(CompetitionModel.CODE));
	}

	/**
	 * Gets current player and returns current competition of players preferences. If not existing it returns first
	 * active competition of player. If no active competition is available for player it throws exception.
	 */
	@Override
	public CompetitionModel getCurrentCompetition() throws NoCompetitionAvailableException
	{
		CompetitionModel compResult = null;
		final PlayerModel player = playerService.getCurrentPlayer();

		if (player != null)
		{
			// return last current competition				
			final PlayerPreferencesModel prefs = player.getPreferences();
			if (prefs != null)
			{
				compResult = prefs.getCurrentCompetition();
			}

			//return first activated competition
			if (compResult == null)
			{
				// return first of active ones
				final Collection<CompetitionModel> comps = player.getCompetitions();
				if (!comps.isEmpty())
				{
					compResult = comps.iterator().next();
				}
			}
		}
		if (compResult == null)
		{
			throw new NoCompetitionAvailableException();
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Determining competition " + compResult.getCode() + " as active");
		}
		return compResult;
	}

	/**
	 * Gathers current player and sets given competition as current one at players preferences.
	 */
	@Override
	public void setCurrentCompetition(final CompetitionModel competition)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("competition", competition);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Activating competition " + competition.getCode());
		}
		final PlayerModel player = playerService.getCurrentPlayer();
		if (player != null)
		{
			PlayerPreferencesModel prefs = player.getPreferences();
			if (prefs == null)
			{
				prefs = modelService.create(PlayerPreferencesModel.class);
				player.setPreferences(prefs);
				modelService.save(player);
			}
			prefs.setCurrentCompetition(competition);
			modelService.save(prefs);
		}
	}

	/**
	 * Delegates to dao and assures uniqueness.
	 */
	@Override
	public CompetitionModel getCompetition(final String code)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("code", code);
		final List<CompetitionModel> result = genericCompetitionDao.find(Collections.singletonMap(CompetitionModel.CODE,
				(Object) code));
		ServicesUtil.validateIfSingleResult(result, CompetitionModel.class, CompetitionModel.CODE, code);
		return result.get(0);
	}

	/**
	 * Gathers current player and returns activated competitions of this player.
	 */
	@Override
	public List<CompetitionModel> getActiveCompetitions()
	{
		final PlayerModel player = playerService.getCurrentPlayer();
		if (player == null)
		{
			return Collections.EMPTY_LIST;
		}
		return new ArrayList<CompetitionModel>(player.getCompetitions());
	}

	/**
	 * Gathers current player and sets given competitions as its active ones.
	 */
	@Override
	public void setActiveCompetitions(final List<CompetitionModel> competitions)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("competitions", competitions);
		final PlayerModel player = playerService.getCurrentPlayer();
		if (player == null)
		{
			return;
		}
		player.setCompetitions(new LinkedHashSet<CompetitionModel>(competitions));
		modelService.save(player);
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setGenericCompetitionDao(final GenericDao<CompetitionModel> genericCompetitionDao)
	{
		this.genericCompetitionDao = genericCompetitionDao;
	}
}

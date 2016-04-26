/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerPreferencesModel;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;


/**
 * Service providing functionality around {@link CompetitionModel}'s. Mainly it provides methods for getting
 * competitions as well as retrieving and modifying competitions for current player.
 * 
 * @author andreas.thaler
 */
public interface CompetitionService
{
	/**
	 * Gets the {@link CompetitionModel} instance for given competition code. If not exactly one instance is found for
	 * given code an exception will be thrown.
	 * 
	 * @param code
	 *           unique competition code the related {@link CompetitionModel} is needed for
	 * @throws UnknownIdentifierException
	 *            No competition can be found for given code
	 * @throws AmbiguousIdentifierException
	 *            More then one competition was found for given code
	 * @return the competition instance for given code
	 */
	CompetitionModel getCompetition(String code) throws UnknownIdentifierException, AmbiguousIdentifierException;

	/**
	 * Gets list of all competitions in system ordered by competition code (ascending). Search restrictions are enabled.
	 * 
	 * @return all competitions in system
	 */
	List<CompetitionModel> getAllCompetitions();

	/**
	 * Gets all competitions activated for current session player. A competition active for a player will be selectable
	 * at the frontend and with this allows to bet on it. Furthermore mails will be send only for active competitions.
	 * 
	 * @return All active competition of current player or empty list if no current player is set or competition is
	 *         activated for player
	 */
	List<CompetitionModel> getActiveCompetitions();

	/**
	 * Sets the competitions active for current session player. It will replace current competitions activated. A
	 * competition active for a player will be selectable at the frontend and with this allows to bet on it. Furthermore
	 * mails will be send only for active competitions.
	 * 
	 * @param competitions
	 *           competitions to mark as active for current session player
	 */
	void setActiveCompetitions(List<CompetitionModel> competitions);

	/**
	 * Gets the competition the current session player selected last in frontend (
	 * {@link PlayerPreferencesModel#getCurrentCompetition()}). If there is no last selected competition available the
	 * first active competition of current session player gets returned. If there is no active competition set at player
	 * an exception gets thrown.
	 * 
	 * @throws NoCompetitionAvailableException
	 *            if no current competition can be determined
	 */
	CompetitionModel getCurrentCompetition() throws NoCompetitionAvailableException;

	/**
	 * Sets the current competition of current session player. This should be the competition the current player is
	 * managing at the moment at the frontend.
	 * 
	 * @param competition
	 *           competition to set as current competition
	 */
	void setCurrentCompetition(CompetitionModel competition);
}

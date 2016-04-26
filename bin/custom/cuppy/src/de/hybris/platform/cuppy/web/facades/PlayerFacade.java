/**
 * 
 */
package de.hybris.platform.cuppy.web.facades;

import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.CompetitionData;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.cuppy.web.data.PlayerRankingData;

import java.util.List;
import java.util.Locale;


/**
 * @author andreas.thaler
 * 
 */
public interface PlayerFacade
{
	/**
	 * Tested.
	 */
	List<PlayerRankingData> getRankings();

	//TODO:TEST
	PlayerRankingData getRanking(String uid);

	//TODO:TEST
	PlayerProfileData getProfile(String uid);

	//TODO:TEST
	void forgotPassword(String mail);

	/**
	 * Tested.
	 */
	void registerPlayer(PlayerProfileData registration);

	/**
	 * Tested.
	 */
	List<Locale> getAllCountries();

	/**
	 * Tested.
	 */
	PlayerProfileData getCurrentPlayer();

	/**
	 * Tested.
	 */
	void updatePlayer(PlayerProfileData player);

	/**
	 * Tested.
	 */
	boolean isCurrentPlayerAdmin();

	/**
	 * Tested.
	 */
	String uploadProfilePicture(byte[] byteData, String fileName);

	//TODO:TEST
	boolean isPlayerOnline(String playerId);

	//TODO:TEST
	CompetitionData getCurrentCompetition() throws NoCompetitionAvailableException;

	//TODO:TEST
	void setCurrentCompetition(String code);

	//TODO:TEST
	List<CompetitionData> getActiveCompetitions();

	//TODO:TEST
	List<CompetitionData> getActiveFinishedCompetitions();

	//TODO:TEST
	List<CompetitionData> getActiveUnfinishedCompetitions();

	//TODO:TEST
	void setActiveCompetitions(List<CompetitionData> competitions);

	//TODO:TEST
	List<CompetitionData> getAllCompetitions();
}

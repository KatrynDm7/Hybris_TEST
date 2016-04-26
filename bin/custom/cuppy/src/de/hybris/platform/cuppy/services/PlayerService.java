/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;

import java.util.Date;
import java.util.List;


/**
 * Service for managing {@link PlayerModel}s
 * 
 * @author andreas.thaler
 */
public interface PlayerService
{

	/**
	 * Confirms a player
	 * 
	 * @param player
	 */
	void confirmPlayer(PlayerModel player);

	/**
	 * Returns the current player in the session
	 * 
	 * @return current player
	 */
	PlayerModel getCurrentPlayer();

	/**
	 * Sets the player as current player in the session
	 * 
	 * @param player
	 */
	//TODO:TEST
	void setCurrentPlayer(PlayerModel player);

	/**
	 * Generates a new password and sends a mail containing the new password
	 * 
	 * @param mail
	 */
	//TODO:TEST
	void forgotPassword(String mail);

	/**
	 * Returns a list of players registered for the competition
	 * 
	 * @param competition
	 * @return list of players, not <tt>null</tt>
	 */
	List<PlayerModel> getPlayers(CompetitionModel competition);

	/**
	 * Returns a list of all players
	 * 
	 * @return list of all players, not <tt>null</tt>
	 */
	//TODO:TEST
	List<PlayerModel> getAllPlayers();

	/**
	 * Returns the default profile picture
	 * 
	 * @return {@link ProfilePictureModel}
	 */
	//TODO:TEST
	ProfilePictureModel getDefaultProfilePicture();

	/**
	 * Returns the default king profile picture
	 * 
	 * @return {@link ProfilePictureModel}
	 */
	//TODO:TEST
	ProfilePictureModel getDefaultKingProfilePicture();

	/**
	 * Registers a player
	 * 
	 * @param player
	 */
	void registerPlayer(PlayerModel player);

	/**
	 * Creates a player
	 * 
	 * @return {@link PlayerModel}
	 */
	PlayerModel createPlayer();

	/**
	 * Updates a player
	 * 
	 * @param curPlayer
	 */
	//TODO:TEST
	void updatePlayer(PlayerModel curPlayer);

	/**
	 * Returns the profile picture for the given code
	 * 
	 * @param code
	 * @return profile picture
	 */
	//TODO:TEST
	ProfilePictureModel getProfilePicture(String code);

	/**
	 * Uploads a profile picture
	 * 
	 * @param currentPlayer
	 * @param byteData
	 * @param fileName
	 */
	//TODO:TEST
	void uploadProfilePicture(PlayerModel currentPlayer, byte[] byteData, String fileName);

	/**
	 * Returns the player for the given uid
	 * 
	 * @param uid
	 * @return player
	 */
	//TODO:TEST
	PlayerModel getPlayer(String uid);

	PlayerModel getPlayerForEmail(String email);

	/**
	 * Returns a random player in the competition
	 * 
	 * @param competition
	 * @return random player
	 */
	//TODO:TEST
	PlayerModel getRandomPlayer(CompetitionModel competition);

	/**
	 * Returns <tt>true</tt> if the player is online
	 * 
	 * @param playerId
	 * @return <tt>true</tt> if the player is online
	 */
	//TODO:TEST
	boolean isPlayerOnline(String playerId);

	/**
	 * Returns <tt>true</tt> if the player is activated for the competition
	 * 
	 * @param competition
	 * @param player
	 * @return <tt>true</tt> if the player is activated for the competition
	 */
	//TODO:TEST
	boolean isPlayerActivatedForCompetition(CompetitionModel competition, PlayerModel player);

	/**
	 * Returns the ranking data for the competition
	 * 
	 * @param competition
	 * @return ranking data
	 */
	//TODO:TEST
	RankingData getRanking(CompetitionModel competition);

	/**
	 * Returns a list of changed rankings since the given date
	 * 
	 * @param date
	 * @return list of changed rankings
	 */
	List<RankingData> getRankingsChangedSince(Date date);

	/**
	 * Returns a list of rankings for all competitions
	 * 
	 * @return list of rankings
	 */
	List<RankingData> getRankings();

	/**
	 * Returns a new filtered list with rankings for registered competitions
	 * 
	 * @param rankings
	 * @param player
	 * @return filtered list
	 */
	List<RankingData> filterRankingsForPlayer(List<RankingData> rankings, PlayerModel player);
}

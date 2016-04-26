package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.TimePointStatisticModel;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Service to retrieve statistical information
 * 
 * @author andreas.thaler
 * 
 */
public interface StatisticsService
{
	/**
	 * Returns the number of players registered for the competition that didn't bet for the given match
	 * 
	 * @param match
	 * @return number of players
	 */
	//TODO:TEST
	int getPlayersNotPlacedBetsForMatchTotal(MatchModel match);

	/**
	 * Returns the percentage of players registered for the competition that didn't bet for the given match
	 * 
	 * @param match
	 * @return number of players
	 */
	//TODO:TEST
	int getPlayersNotPlacedBetsForMatchPerc(MatchModel match);

	/**
	 * Returns the average score of a player
	 * 
	 * @param player
	 * @return average score
	 */
	//TODO:TEST
	double getAverageScoreForPlayer(PlayerModel player);

	/**
	 * Returns the average score for this match
	 * 
	 * @param match
	 * @return average score
	 */
	//TODO:TEST
	double getAverageScoreForMatch(MatchModel match);

	/**
	 * Returns the average score of all players
	 * 
	 * @return average score
	 */
	//TODO:TEST
	double getAverageScoreForAllPlayers();

	/**
	 * Returns the probability to bet correctly
	 * 
	 * @return probability
	 */
	//TODO:TEST
	double getProbabilityForCorrectBet();

	/**
	 * Returns the number of confirmed players
	 * 
	 * @return number of players
	 */
	//TODO:TEST
	int getPlayersCount();

	/**
	 * Returns the number of players registered for this competition
	 * 
	 * @param competition
	 * @return number of players registered for this competition
	 */
	//TODO:TEST
	int getPlayersCount(final CompetitionModel competition);

	/**
	 * Returns the number of currently active players
	 * 
	 * @return number of currently active players
	 */
	//TODO:TEST
	int getCurrentPlayersCount();

	/**
	 * Saves a {@link TimePointStatisticModel} of currently active players
	 */
	//TODO:TEST
	void updateTimpointStatistic();

	/**
	 * Returns the aggregated number of online players per day
	 * 
	 * @return aggregated number of online players per day
	 */
	//TODO:TEST
	Map<Date, Integer> getPlayersOnlineCount();

	/**
	 * Returns the maximum number of online players
	 * 
	 * @return maximum number of online players
	 */
	//TODO:TEST
	int getPlayersOnlineMaxCount();

	/**
	 * Removes all {@link TimePointStatisticModel} data
	 */
	//TODO:TEST
	void cleanUpTimepointStatistics();

	/**
	 * Returns a list of {@link TimePointStatisticModel} created after the given timestamp ordered by timestamp
	 * 
	 * @param since
	 * @return list of {@link TimePointStatisticModel}
	 */
	//TODO:TEST
	List<TimePointStatisticModel> getTimePointStatistics(long since);

	/**
	 * Returns a list of {@link TimePointStatisticModel} created between the given timestamps ordered by timestamp
	 * 
	 * @param sinceFrom
	 * @param sinceTill
	 * @return list of {@link TimePointStatisticModel}
	 */
	//TODO:TEST
	List<TimePointStatisticModel> getTimePointStatistics(long sinceFrom, long sinceTill);

	// best player of country -> rank and name

}

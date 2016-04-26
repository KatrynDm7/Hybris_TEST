/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.OverallStatisticModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.TimePointStatisticModel;

import java.util.Date;
import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface StatisticsDao
{
	//TODO:TEST
	int getPlayersCount();

	//TODO:TEST
	int getPlayersCount(CompetitionModel competition);

	/**
	 * Tested.
	 */
	int getPlayersNotPlacedBetsForMatchCount(MatchModel match);

	//TODO:TEST
	List<PlayerModel> findPlayersNotPlacedBetsForMatch(MatchModel match);

	/**
	 * Tested.
	 */
	int getPlayersPlacedBetsForMatchCount(MatchModel match);

	//TODO:TEST
	List<PlayerModel> findPlayersPlacedBetsForMatch(MatchModel match);

	//TODO:TEST
	OverallStatisticModel findOverallStatistics();

	//TODO:TEST
	List<TimePointStatisticModel> findTimePointStatistics();

	//TODO:TEST
	List<TimePointStatisticModel> findOutdatedTimePointStatistics(Date outdateTime);

	//TODO:TEST
	List<TimePointStatisticModel> findLastTimePointStatistics(Date since);

	//TODO:TEST
	List<TimePointStatisticModel> findLastTimePointStatistics(Date from, Date till);
}

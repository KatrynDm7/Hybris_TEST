/**
 * 
 */
package de.hybris.platform.cuppy.web.facades;

import de.hybris.platform.cuppy.web.data.MatchStatisticData;
import de.hybris.platform.cuppy.web.data.OverallStatisticData;
import de.hybris.platform.cuppy.web.data.PlayerStatisticData;
import de.hybris.platform.cuppy.web.data.TimepointStatisticData;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface StatisticsFacade
{
	//TODO:TEST
	PlayerStatisticData getRandomPlayerStatistic();

	/**
	 * Tested.
	 */
	MatchStatisticData getNextBetableMatchStatistic();

	//TODO:TEST
	MatchStatisticData getRandomMatchStatistic();

	//TODO:TEST
	OverallStatisticData getOverallStatistic();

	//TODO:TEST
	List<TimepointStatisticData> getTimepointStatistics(long since);

	//TODO:TEST
	List<TimepointStatisticData> getTimepointStatistics(long from, long till);
}

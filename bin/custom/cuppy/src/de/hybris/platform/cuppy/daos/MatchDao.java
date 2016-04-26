/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchModel;

import java.util.Date;
import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface MatchDao
{
	/**
	 * Tested.
	 */
	List<MatchModel> findMatchById(CompetitionModel competition, int id);

	/**
	 * Tested.
	 */
	List<MatchModel> findMatches(CompetitionModel competition);

	/**
	 * Tested.
	 */
	List<MatchModel> findMatchesByGroup(GroupModel group);

	/**
	 * Tested.
	 */
	List<MatchModel> findFinishedMatches(CompetitionModel competition);

	List<MatchModel> findFinishedMatchesForMatchday(CompetitionModel competition, int matchday);

	Integer getMatchdayByDate(CompetitionModel competition, Date minimumKickoffDate);

	List<MatchModel> findMatches(CompetitionModel competition, int matchday);

	List<MatchModel> findMatchBefore(CompetitionModel competition, Date date);

	Integer getMaxMatchday(CompetitionModel competition);

	List<Integer> findMatchdays(CompetitionModel competition);

	List<MatchModel> getMatchesBetween(Date start, Date end);

	Date getModificationTimeByMatchday(CompetitionModel competition, int matchday);
}

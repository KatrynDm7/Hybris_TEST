/**
 * 
 */
package de.hybris.platform.cuppy.web.facades;

import de.hybris.platform.cuppy.web.data.BetData;
import de.hybris.platform.cuppy.web.data.GroupData;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.cuppy.web.data.NewsData;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface MatchFacade
{
	/**
	 * Tested.
	 */
	List<GroupData> getGroups();

	/**
	 * Tested.
	 */
	void placeBet(MatchData bet);

	/**
	 * Tested.
	 */
	List<MatchData> getMatches(GroupData group);

	/**
	 * Tested.
	 */
	List<MatchData> getMatches();

	/**
	 * Tested.
	 */
	List<MatchData> getMatches(String uid);

	//TODO:TEST
	List<MatchData> getMatches(int matchday);

	//TODO:TEST
	MatchData getMatch(int id);

	/**
	 * Tested.
	 */
	List<NewsData> getLatestNews(int count);

	/**
	 * Tested.
	 */
	List<MatchData> getClosedMatches(final String uid);

	/**
	 * Tested.
	 */
	List<BetData> getClosedBets(int matchId);

	//TODO:TEST
	List<Integer> getMatchdays();

	//TODO:TEST
	Integer getCurrentMatchday();
}

/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Date;
import java.util.List;


/**
 * Service for managing matches and bets
 * 
 * @author andreas.thaler
 */
public interface MatchService
{
	/**
	 * Return all groups for the competition
	 * 
	 * @param competition
	 * @return list of groups, not <tt>null</tt>
	 */
	List<GroupModel> getGroups(CompetitionModel competition);

	/**
	 * Returns all matches for a group
	 * 
	 * @param group
	 * @return list of matches, not <tt>null</tt>
	 */
	List<MatchModel> getMatches(GroupModel group);

	/**
	 * Returns all matches for a competition
	 * 
	 * @param competition
	 * @return list of matches, not <tt>null</tt>
	 */
	List<MatchModel> getMatches(CompetitionModel competition);

	/**
	 * Returns a match with given id in the competition
	 * 
	 * @param competition
	 * @param id
	 * @return a {@link MatchModel}, if the match exists
	 * @throws UnknownIdentifierException
	 *            , if the match doesn't exist or doesn't belong to the competition
	 */
	MatchModel getMatch(CompetitionModel competition, int id);

	/**
	 * Places a bet, if the following conditions are met:
	 * <ul>
	 * <li>the match doesn't contain dummy teams</li>
	 * <li>there is still time to bet</li>
	 * <li>there are no partial results available</li>
	 * </ul>
	 * 
	 * @param bet
	 */
	void placeBet(MatchBetModel bet);

	/**
	 * Returns a group for the competition
	 * 
	 * @param competition
	 * @param code
	 * @return {@link GroupModel}
	 * @throws UnknownIdentifierException
	 *            , if the group doesn't exist or doesn't belong to the competition
	 */
	GroupModel getGroup(CompetitionModel competition, String code);

	/**
	 * Returns a bet on a match for a player
	 * 
	 * @param match
	 * @param player
	 * @return {@link MatchBetModel}
	 */
	MatchBetModel getBet(MatchModel match, PlayerModel player);

	/**
	 * Returns the latest general news or news for competitions the player is interested in
	 * 
	 * @param player
	 * @param count
	 * @return a list of {@link NewsModel}, not <tt>null</tt>
	 */
	List<NewsModel> getLatestNews(PlayerModel player, int count);

	/**
	 * retrieves or creates a bet
	 * 
	 * @param match
	 * @param player
	 * @return a {@link MatchBetModel}
	 */
	MatchBetModel getOrCreateBet(MatchModel match, PlayerModel player);

	/**
	 * Returns true, if
	 * <ul>
	 * <li>the match doesn't contain dummy teams</li>
	 * <li>there is still time to bet</li>
	 * <li>there are no partial results available</li>
	 * </ul>
	 * 
	 * @param match
	 * @return <tt>true</tt>, if the match is still betable
	 */
	boolean isBetable(final MatchModel match);

	/**
	 * Returns <tt>true</tt>, if these conditions are met:
	 * <ul>
	 * <li>the match started in the past</li>
	 * <li>the results are completely available</li>
	 * </ul>
	 * 
	 * @param match
	 * @return <tt>true</tt>, if the match is finished
	 */
	boolean isMatchFinished(MatchModel match);

	/**
	 * Return <tt>true</tt>, if the player has placed a bet on the match
	 * 
	 * @param match
	 * @param player
	 * @return <tt>true</tt>, if the player has placed a bet on the match
	 */
	boolean hasBet(MatchModel match, PlayerModel player);

	/**
	 * Returns the next betable match
	 * 
	 * @param competition
	 * @return the next betable match or <tt>null</tt>
	 */
	MatchModel getNextBetableMatch(CompetitionModel competition);

	/**
	 * Returns the latest match before the given date for the competition
	 * 
	 * @param competition
	 * @param date
	 * @return latest match before the given date or <tt>null</tt>
	 */
	MatchModel getMatchBefore(CompetitionModel competition, Date date);

	/**
	 * Returns a random match in the given competition
	 * 
	 * @param competition
	 * @return random match
	 */
	MatchModel getRandomMatch(CompetitionModel competition);

	/**
	 * Returns the time left to bet in milliseconds or 0.
	 * 
	 * @param match
	 * @return time left
	 */
	long getTimeToBet(MatchModel match);

	/**
	 * Returns the score of the player for the competition
	 * 
	 * @param competition
	 *           the competition
	 * @param player
	 * @return score
	 */
	int getScore(CompetitionModel competition, PlayerModel player);

	/**
	 * Returns the score for the bet.
	 * 
	 * @param bet
	 * @return score
	 * @throws IllegalStateException
	 *            in case the match of that bet is not having a result yet
	 */
	int getScore(final MatchBetModel bet);

	/**
	 * Returns a list of bets for a match.
	 * 
	 * @param match
	 * @return list of bets, not <tt>null</tt>
	 */
	List<MatchBetModel> getBets(final MatchModel match);

	/**
	 * Returns a list of bets of a player in a competition
	 * 
	 * @param competition
	 * @param player
	 * @return list of bets
	 */
	List<MatchBetModel> getBets(CompetitionModel competition, final PlayerModel player);

	/**
	 * Returns a list of finished bets of a player in a competition
	 * 
	 * @param competition
	 * @param player
	 * @return list of bets
	 */
	List<MatchBetModel> getFinishedBets(CompetitionModel competition, final PlayerModel player);

	/**
	 * Returns the current match day of the competition with a date greater than the current date or <tt>null</tt>
	 * 
	 * @param competition
	 * @return current match day or <tt>null</tt>
	 */
	Integer getCurrentMatchday(CompetitionModel competition);

	/**
	 * Returns the matches on the matchDay within the competition
	 * 
	 * @param competition
	 * @param matchday
	 * @return list of matches, not <tt>null</tt>
	 */
	List<MatchModel> getMatches(CompetitionModel competition, int matchday);

	/**
	 * Returns all match days for the competition
	 * 
	 * @param competition
	 * @return list of match days, not <tt>null</tt>
	 */
	List<Integer> getMatchdays(CompetitionModel competition);

	/**
	 * Returns the current score of the player within the competition
	 * 
	 * @param competition
	 * @param player
	 * @return score
	 */
	//TODO:TEST
	int getScoreWithoutCurrentMatchday(CompetitionModel competition, PlayerModel player);

	/**
	 * Returns the current completed match day
	 * 
	 * @param competition
	 * @return match day
	 */
	//TODO:TEST
	Integer getCurrentCompletedMatchday(CompetitionModel competition);

	/**
	 * Returns the last completed match day
	 * 
	 * @param competition
	 * @return match day
	 */
	//TODO:TEST
	Integer getLastCompletedMatchday(CompetitionModel competition);

	/**
	 * Returns a list of matches for today
	 * 
	 * @param player
	 * @return list of matches for today, not <tt>null</tt>
	 */
	//TODO:TEST
	List<MatchModel> getTodayMatches(PlayerModel player);

	/**
	 * Returns <tt>true</tt> if there a match in the competition was modified after the given date
	 * 
	 * @param competition
	 * @param date
	 * @return <tt>true</tt> if there is a modified match
	 */
	//TODO:TEST
	boolean isLastCompletedMatchdayModifiedAfter(CompetitionModel competition, Date date);
}

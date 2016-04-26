/**
 * 
 */
package de.hybris.platform.cuppy.services;


/**
 * Strategy to return the score based on the bets and the match result.
 * 
 * @author andreas.thaler
 */
public interface ScoreStrategy
{
	/**
	 * Returns the score based on the bets and the match result.
	 * 
	 * @param homeMatch
	 * @param homeBet
	 * @param guestMatch
	 * @param guestBet
	 * @param multiplier
	 * @return the score
	 */
	int getScore(final int homeMatch, final int homeBet, final int guestMatch, final int guestBet, final float multiplier);
}

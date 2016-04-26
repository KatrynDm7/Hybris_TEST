/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.services.ScoreStrategy;

import org.springframework.stereotype.Component;


/**
 * @author andreas.thaler
 * 
 */
@Component(value = "scoreStrategy")
public class DefaultScoreStrategy implements ScoreStrategy
{

	@Override
	public int getScore(final int homeMatch, final int homeBet, final int guestMatch, final int guestBet, final float multiplier)
	{
		int result;

		if (homeBet == homeMatch && guestBet == guestMatch)
		{
			result = 3;
		}
		else if (homeMatch - guestMatch == homeBet - guestBet)
		{
			result = 2;
		}
		else if (Math.signum(homeMatch - guestMatch) == Math.signum(homeBet - guestBet))
		{
			result = 1;
		}
		else
		{
			result = 0;
		}

		result = (int) (result * multiplier);
		return result;
	}
}

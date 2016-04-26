/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;

import java.util.Date;



/**
 * Strategy to retrieve the match day of a competition either by match day or by date
 * 
 * @author andreas.thaler
 * 
 */
public interface MatchdayCalculationStrategy
{
	/**
	 * Returns the match day of a competition either by match day or by date
	 * 
	 * @param competition
	 * @param matchdayId
	 * @param date
	 * @return match day
	 */
	//TODO:TEST
	int getMatchday(CompetitionModel competition, int matchdayId, Date date);
}

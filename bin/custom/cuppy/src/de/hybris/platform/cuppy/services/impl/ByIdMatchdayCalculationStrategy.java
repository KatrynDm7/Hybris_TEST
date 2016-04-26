/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.services.MatchdayCalculationStrategy;

import java.util.Date;


/**
 * @author andreas.thaler
 * 
 */
public class ByIdMatchdayCalculationStrategy implements MatchdayCalculationStrategy
{
	@Override
	public int getMatchday(final CompetitionModel competition, final int matchdayId, final Date date)
	{
		return matchdayId;
	}
}

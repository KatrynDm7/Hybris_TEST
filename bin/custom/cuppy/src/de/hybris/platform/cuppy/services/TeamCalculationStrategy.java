/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.TeamModel;


/**
 * Strategy to retrieve a team of a competition by teamAlias
 * 
 * @author andreas.thaler
 */
public interface TeamCalculationStrategy
{
	/**
	 * Returns a team of a competition by teamAlias
	 * 
	 * @param competition
	 * @param teamAlias
	 * @return team of a competition by teamAlias or <tt>null</tt>
	 */
	//TODO:TEST
	TeamModel getTeam(CompetitionModel competition, String teamAlias);
}

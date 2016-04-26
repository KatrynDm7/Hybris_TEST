/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;


/**
 * Strategy to retrieve a group of a competition either by alias or match day
 * 
 * @author andreas.thaler
 * 
 */
public interface GroupCalculationStrategy
{
	/**
	 * Returns a group of a competition either by alias or match day
	 * 
	 * @param competition
	 * @param groupAlias
	 * @param matchday
	 * @return group
	 */
	//TODO: TEST
	GroupModel getGroup(CompetitionModel competition, String groupAlias, int matchday);
}

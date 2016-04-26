/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.TeamModel;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface TeamDao
{
	List<TeamModel> findTeamByName(final String name, final String langIso);
}

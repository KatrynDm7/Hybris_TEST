/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface GroupDao
{
	List<GroupModel> findGroupByName(final CompetitionModel competition, final String name, final String langIso);

	/**
	 * Tested.
	 */
	List<GroupModel> findGroups(CompetitionModel competition);

	/**
	 * Tested.
	 */
	List<GroupModel> findGroupByCode(CompetitionModel competition, final String code);
}

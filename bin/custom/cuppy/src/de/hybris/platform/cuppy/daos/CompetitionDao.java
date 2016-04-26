/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.CompetitionModel;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface CompetitionDao
{
	/**
	 * Tested.
	 */
	List<CompetitionModel> findCompetitionByCode(String code);

	/**
	 * Tested.
	 */
	List<CompetitionModel> findCompetitions();
}

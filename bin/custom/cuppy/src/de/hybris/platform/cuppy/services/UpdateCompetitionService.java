/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.CompetitionModel;

import java.util.Date;


/**
 * Service for updating competition data using openligadb.
 * 
 * @author andreas.thaler
 */
public interface UpdateCompetitionService
{
	/**
	 * Returns <tt>true</tt> no changes occurred after the given date
	 * 
	 * @param competition
	 * @param lastExecution
	 * @return <tt>true</tt> no changes occurred after the given date
	 */
	//TODO: TEST
	boolean isUpToDate(CompetitionModel competition, Date lastExecution);

	/**
	 * Updates a competition data using openligadb.
	 * 
	 * @param competition
	 */
	//TODO: TEST
	void update(CompetitionModel competition);
}

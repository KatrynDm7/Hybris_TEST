/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.daos.TeamDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppy.services.TeamCalculationStrategy;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author andreas.thaler
 * 
 */
public class ByNameTeamCalculationStrategy implements TeamCalculationStrategy
{
	private static final Logger LOG = Logger.getLogger(ByNameTeamCalculationStrategy.class);

	@Autowired
	private TeamDao teamDao;
	private String language;

	@Override
	public TeamModel getTeam(final CompetitionModel competition, final String teamAlias)
	{
		final List<TeamModel> result = teamDao.findTeamByName(teamAlias, language);
		if (result.isEmpty())
		{
			LOG.warn("Can not find a team for " + teamAlias);
			return null;
		}
		return result.iterator().next();
	}

	@Required
	public void setLanguage(final String language)
	{
		this.language = language;
	}
}

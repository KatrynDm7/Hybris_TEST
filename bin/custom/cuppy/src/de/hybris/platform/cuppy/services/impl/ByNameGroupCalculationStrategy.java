/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.daos.GroupDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.services.GroupCalculationStrategy;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author andreas.thaler
 * 
 */
public class ByNameGroupCalculationStrategy implements GroupCalculationStrategy
{
	private static final Logger LOG = Logger.getLogger(ByNameGroupCalculationStrategy.class);

	@Autowired
	private GroupDao groupDao;
	private String language;

	@Override
	public GroupModel getGroup(final CompetitionModel competition, final String groupAlias,
			@SuppressWarnings("unused") final int matchday)
	{
		GroupModel result;
		List<GroupModel> results = groupDao.findGroupByName(competition, groupAlias, language);
		if (results.isEmpty())
		{
			results = groupDao.findGroups(competition);
			if (results.isEmpty())
			{
				result = null;
				LOG.error("Can not find a group for " + groupAlias);
			}
			else
			{
				result = results.iterator().next();
				LOG.warn("Can not find a group for " + groupAlias + ", using " + result.getCode());
			}
		}
		else
		{
			result = results.iterator().next();
		}
		return result;
	}

	@Required
	public void setLanguage(final String language)
	{
		this.language = language;
	}
}

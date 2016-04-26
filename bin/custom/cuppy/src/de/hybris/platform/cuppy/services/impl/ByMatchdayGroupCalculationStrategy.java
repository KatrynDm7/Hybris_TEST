package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.services.GroupCalculationStrategy;
import de.hybris.platform.cuppy.services.MatchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author andreas.thaler
 * 
 */
public class ByMatchdayGroupCalculationStrategy implements GroupCalculationStrategy
{
	private static final Logger LOG = Logger.getLogger(ByMatchdayGroupCalculationStrategy.class);
	@Autowired
	private MatchService matchService;
	private List<Matchday2GroupMapping> matchday2groupMappings;

	@Override
	public GroupModel getGroup(final CompetitionModel competition, final String groupAlias, final int matchday)
	{
		for (int i = 0; i < matchday2groupMappings.size(); i++)
		{
			final Matchday2GroupMapping mapping = matchday2groupMappings.get(i);
			Matchday2GroupMapping nextMapping = null;
			if ((i + 1) < matchday2groupMappings.size())
			{
				nextMapping = matchday2groupMappings.get(i + 1);
			}
			if (mapping.getMatchday() <= matchday && (nextMapping == null || matchday < nextMapping.getMatchday()))
			{
				return matchService.getGroup(competition, mapping.getGroup());
			}
		}
		LOG.warn("No group found");
		return null;
	}

	@Required
	public void setMatchday2groupMappings(final List<Matchday2GroupMapping> matchday2groupMappings)
	{
		this.matchday2groupMappings = matchday2groupMappings;
	}
}

/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.cuppy.daos.TeamDao;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "teamDao")
public class DefaultTeamDao implements TeamDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<TeamModel> findTeamByName(final String name, final String langIso)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {t:").append(TeamModel.PK).append("} ");
		builder.append("FROM {").append(TeamModel._TYPECODE).append(" AS t} ");
		builder.append("WHERE ").append("{t:").append(TeamModel.NAME).append("[").append(langIso).append("]}")
				.append(" LIKE ?name");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("name", name);

		return flexibleSearchService.<TeamModel> search(query).getResult();
	}
}

/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.cuppy.daos.GroupDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "groupDao")
public class DefaultGroupDao implements GroupDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<GroupModel> findGroupByCode(final CompetitionModel competition, final String code)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {g:").append(GroupModel.PK).append("} ");
		builder.append("FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("WHERE ").append("{g:").append(GroupModel.CODE).append("}").append("=?code ");
		builder.append("AND ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append("ORDER BY ").append("{g:").append(GroupModel.SEQUENCENUMBER).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("code", code);
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<GroupModel> search(query).getResult();
	}

	@Override
	public List<GroupModel> findGroupByName(final CompetitionModel competition, final String name, final String langIso)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {g:").append(GroupModel.PK).append("} ");
		builder.append("FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append("AND ").append("{g:").append(GroupModel.NAME).append("[").append(langIso).append("]}").append(" LIKE ?name");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("comp", competition);
		query.addQueryParameter("name", name);

		return flexibleSearchService.<GroupModel> search(query).getResult();
	}

	@Override
	public List<GroupModel> findGroups(final CompetitionModel competition)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {g:").append(GroupModel.PK).append("} ");
		builder.append("FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append("ORDER BY ").append("{g:").append(GroupModel.SEQUENCENUMBER).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<GroupModel> search(query).getResult();
	}
}

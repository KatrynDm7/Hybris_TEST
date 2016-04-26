/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.cuppy.daos.CompetitionDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "competitionDao")
public class DefaultCompetitionDao implements CompetitionDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<CompetitionModel> findCompetitionByCode(final String code)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {c:").append(CompetitionModel.PK).append("} ");
		builder.append("FROM {").append(CompetitionModel._TYPECODE).append(" AS c} ");
		builder.append("WHERE ").append("{c:").append(CompetitionModel.CODE).append("}").append("=?code ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("code", code);

		return flexibleSearchService.<CompetitionModel> search(query).getResult();
	}

	@Override
	public List<CompetitionModel> findCompetitions()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {c:").append(CompetitionModel.PK).append("} ");
		builder.append("FROM {").append(CompetitionModel._TYPECODE).append(" AS c} ");
		builder.append("ORDER BY ").append("{c:").append(CompetitionModel.CODE).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);

		return flexibleSearchService.<CompetitionModel> search(query).getResult();
	}

}

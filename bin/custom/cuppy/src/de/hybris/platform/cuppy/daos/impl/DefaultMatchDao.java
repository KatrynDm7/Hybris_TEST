/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.cuppy.daos.MatchDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "matchDao")
public class DefaultMatchDao implements MatchDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<MatchModel> findMatchById(final CompetitionModel competition, final int id)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.ID).append("}").append("=?id ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("id", Integer.valueOf(id));
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}

	@Override
	public List<MatchModel> findMatches(final CompetitionModel competition)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}

	@Override
	public List<MatchModel> findMatchesByGroup(final GroupModel group)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.GROUP).append("}").append("=?group ");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("group", group.getPk());

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}

	@Override
	public List<MatchModel> findFinishedMatches(final CompetitionModel competition)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.GUESTGOALS).append("}").append(" IS NOT NULL ");
		builder.append("AND ").append("{m:").append(MatchModel.HOMEGOALS).append("}").append(" IS NOT NULL ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}

	@Override
	public Integer getMatchdayByDate(final CompetitionModel competition, final Date minimumKickoffDate)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.MATCHDAY).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.DATE).append("}").append(" >= ?date ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setResultClassList(Collections.singletonList(Integer.class));
		query.addQueryParameter("date", minimumKickoffDate);
		query.addQueryParameter("comp", competition);

		final List<Integer> result = flexibleSearchService.<Integer> search(query).getResult();
		if (result != null && !result.isEmpty())
		{
			return result.iterator().next();
		}
		return null;
	}

	@Override
	public List<MatchModel> findMatches(final CompetitionModel competition, final int matchday)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.MATCHDAY).append("}").append(" =?matchday ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.addQueryParameter("matchday", Integer.valueOf(matchday));
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}

	@Override
	public Integer getMaxMatchday(final CompetitionModel competition)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT MAX({m:").append(MatchModel.MATCHDAY).append("}) ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setResultClassList(Collections.singletonList(Integer.class));
		query.addQueryParameter("comp", competition);
		query.setResultClassList(Collections.singletonList(Integer.class));

		final List<Integer> result = flexibleSearchService.<Integer> search(query).getResult();
		if (result != null && !result.isEmpty())
		{
			return result.iterator().next();
		}

		return null;
	}

	@Override
	public List<MatchModel> findMatchBefore(final CompetitionModel competition, final Date date)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.DATE).append("}").append(" <= ?date ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("} DESC");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("date", date);
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}

	@Override
	public List<MatchModel> getMatchesBetween(final Date start, final Date end)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.DATE).append("}").append(" >= ?start ");
		builder.append("AND ").append("{m:").append(MatchModel.DATE).append("}").append(" <= ?end ");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("start", start);
		query.addQueryParameter("end", end);

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}

	@Override
	public List<Integer> findMatchdays(final CompetitionModel competition)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT DISTINCT {m:").append(MatchModel.MATCHDAY).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.MATCHDAY).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.addQueryParameter("comp", competition);
		query.setResultClassList(Collections.singletonList(Integer.class));

		return flexibleSearchService.<Integer> search(query).getResult();
	}

	@Override
	public Date getModificationTimeByMatchday(final CompetitionModel competition, final int matchday)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT MAX({m:").append(MatchModel.MODIFIEDTIME).append("}) ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.MATCHDAY).append("}").append(" = ?matchday ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setResultClassList(Collections.singletonList(Date.class));
		query.addQueryParameter("matchday", Integer.valueOf(matchday));
		query.addQueryParameter("comp", competition);

		final List<Date> result = flexibleSearchService.<Date> search(query).getResult();
		if (result != null && !result.isEmpty())
		{
			return result.iterator().next();
		}
		return null;
	}

	@Override
	public List<MatchModel> findFinishedMatchesForMatchday(final CompetitionModel competition, final int matchday)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchModel.PK).append("} ");
		builder.append("FROM {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchModel.GUESTGOALS).append("}").append(" IS NOT NULL ");
		builder.append("AND ").append("{m:").append(MatchModel.HOMEGOALS).append("}").append(" IS NOT NULL ");
		builder.append("AND ").append("{m:").append(MatchModel.MATCHDAY).append("}").append(" =?matchday ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");
		builder.append("ORDER BY ").append("{m:").append(MatchModel.DATE).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.addQueryParameter("comp", competition);
		query.addQueryParameter("matchday", Integer.valueOf(matchday));

		return flexibleSearchService.<MatchModel> search(query).getResult();
	}
}

/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.cuppy.daos.StatisticsDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.OverallStatisticModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.TimePointStatisticModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "statisticsDao")
public class DefaultStatisticsDao implements StatisticsDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

	@Override
	public int getPlayersNotPlacedBetsForMatchCount(final MatchModel match)
	{
		return findPlayersNotPlacedBetsForMatchInternal(match).getTotalCount();
	}

	@Override
	public List<PlayerModel> findPlayersNotPlacedBetsForMatch(final MatchModel match)
	{
		return findPlayersNotPlacedBetsForMatchInternal(match).getResult();
	}

	private SearchResult<PlayerModel> findPlayersNotPlacedBetsForMatchInternal(final MatchModel match)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.PK).append("}").append(" NOT IN ({{");
		builder.append("  SELECT {b:").append(MatchBetModel.PLAYER).append("} ");
		builder.append("  FROM {").append(MatchBetModel._TYPECODE).append(" AS b}, ");
		builder.append("    {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("  WHERE ").append("{m:").append(MatchModel.PK).append("}").append("=?match ");
		builder.append("    AND ").append("{b:").append(MatchBetModel.MATCH).append("}").append("=").append("{m:")
				.append(MatchModel.PK).append("}");
		builder.append("}}) ");
		builder.append("AND ").append("{p:").append(PlayerModel.PK).append("}").append(" IN ({{");
		builder.append("  SELECT {l:").append(LinkModel.TARGET).append("} ");
		builder.append("  FROM {").append("CompetitionPlayerRelation").append(" AS l} ");
		builder.append("  WHERE ").append("{l:").append(LinkModel.SOURCE).append("}").append("=?comp ");
		builder.append("}}) ");


		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("match", match);
		query.addQueryParameter("comp", match.getGroup().getCompetition());

		return flexibleSearchService.<PlayerModel> search(query);
	}

	@Override
	public int getPlayersPlacedBetsForMatchCount(final MatchModel match)
	{
		return getPlayersPlacedBetsForMatchInternal(match).getTotalCount();
	}

	@Override
	public List<PlayerModel> findPlayersPlacedBetsForMatch(final MatchModel match)
	{
		return getPlayersPlacedBetsForMatchInternal(match).getResult();
	}

	private SearchResult<PlayerModel> getPlayersPlacedBetsForMatchInternal(final MatchModel match)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.PK).append("}").append(" IN ({{");
		builder.append("  SELECT {b:").append(MatchBetModel.PLAYER).append("} ");
		builder.append("  FROM {").append(MatchBetModel._TYPECODE).append(" AS b}, ");
		builder.append("    {").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("  WHERE ").append("{m:").append(MatchModel.PK).append("}").append("=?match ");
		builder.append("    AND ").append("{b:").append(MatchBetModel.MATCH).append("}").append("=").append("{m:")
				.append(MatchModel.PK).append("} }})");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("match", match);

		return flexibleSearchService.<PlayerModel> search(query);
	}

	@Override
	public int getPlayersCount()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.CONFIRMED).append("}").append("=?true ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("true", Boolean.TRUE);

		return flexibleSearchService.search(query).getTotalCount();
	}

	@Override
	public int getPlayersCount(final CompetitionModel competition)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p},{CompetitionPlayerRelation as l} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.CONFIRMED).append("}").append("=?true ");
		builder.append("AND ").append("{p:").append(PlayerModel.PK).append("}={l:target}");
		builder.append("AND ").append("{l:source}=?comp ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("true", Boolean.TRUE);
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.search(query).getTotalCount();
	}

	@Override
	public OverallStatisticModel findOverallStatistics()
	{
		OverallStatisticModel result;

		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {s:").append(OverallStatisticModel.PK).append("} ");
		builder.append("FROM {").append(OverallStatisticModel._TYPECODE).append(" AS s} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		try
		{
			result = flexibleSearchService.searchUnique(query);
		}
		catch (final ModelNotFoundException e)
		{
			result = modelService.create(OverallStatisticModel.class);
			modelService.save(result);
		}
		return result;
	}

	@Override
	public List<TimePointStatisticModel> findTimePointStatistics()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {t:").append(TimePointStatisticModel.PK).append("} ");
		builder.append("FROM {").append(TimePointStatisticModel._TYPECODE).append(" AS t} ");
		builder.append("ORDER BY ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);

		return flexibleSearchService.<TimePointStatisticModel> search(query).getResult();
	}

	@Override
	public List<TimePointStatisticModel> findOutdatedTimePointStatistics(final Date outdateTime)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {t:").append(TimePointStatisticModel.PK).append("} ");
		builder.append("FROM {").append(TimePointStatisticModel._TYPECODE).append(" AS t} ");
		builder.append("WHERE ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}").append("<?date ");
		builder.append("ORDER BY ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("date", outdateTime);

		return flexibleSearchService.<TimePointStatisticModel> search(query).getResult();
	}

	@Override
	public List<TimePointStatisticModel> findLastTimePointStatistics(final Date from, final Date till)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {t:").append(TimePointStatisticModel.PK).append("} ");
		builder.append("FROM {").append(TimePointStatisticModel._TYPECODE).append(" AS t} ");
		builder.append("WHERE ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}").append(">?fromdate ");
		builder.append("AND ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}").append("<?tilldate ");
		builder.append("ORDER BY ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("fromdate", from);
		query.addQueryParameter("tilldate", till);

		return flexibleSearchService.<TimePointStatisticModel> search(query).getResult();
	}

	@Override
	public List<TimePointStatisticModel> findLastTimePointStatistics(final Date sinceTime)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {t:").append(TimePointStatisticModel.PK).append("} ");
		builder.append("FROM {").append(TimePointStatisticModel._TYPECODE).append(" AS t} ");
		builder.append("WHERE ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}").append(">?date ");
		builder.append("ORDER BY ").append("{t:").append(TimePointStatisticModel.CREATIONTIME).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("date", sinceTime);

		return flexibleSearchService.<TimePointStatisticModel> search(query).getResult();
	}
}

/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.cuppy.daos.MatchBetDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "matchBetDao")
public class DefaultMatchBetDao implements MatchBetDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<MatchBetModel> findMatchBetByPlayerAndMatch(final PlayerModel player, final MatchModel match)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchBetModel.PK).append("} ");
		builder.append("FROM {").append(MatchBetModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchBetModel.PLAYER).append("}").append("=?player ");
		builder.append("AND ").append("{m:").append(MatchBetModel.MATCH).append("}").append("=?match ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("player", player.getPk());
		query.addQueryParameter("match", match.getPk());

		return flexibleSearchService.<MatchBetModel> search(query).getResult();
	}

	@Override
	public List<MatchBetModel> findFinishedMatchBetsByPlayer(final CompetitionModel competition, final PlayerModel player)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {b:").append(MatchBetModel.PK).append("} ");
		builder.append("FROM {").append(MatchBetModel._TYPECODE).append(" AS b}, ");
		builder.append("{").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{b:").append(MatchBetModel.PLAYER).append("}").append("=?player ");
		builder.append("AND ").append("{m:").append(MatchModel.PK).append("}={b:").append(MatchBetModel.MATCH).append("} ");
		builder.append("AND ").append("{m:").append(MatchModel.GUESTGOALS).append("}").append(" IS NOT NULL ");
		builder.append("AND ").append("{m:").append(MatchModel.HOMEGOALS).append("}").append(" IS NOT NULL ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }}) ");
		builder.append("ORDER BY {b:").append(MatchBetModel.PK).append("} ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("player", player.getPk());
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<MatchBetModel> search(query).getResult();
	}

	@Override
	public List<MatchBetModel> findMatchBetsByPlayer(final CompetitionModel competition, final PlayerModel player)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {b:").append(MatchBetModel.PK).append("} ");
		builder.append("FROM {").append(MatchBetModel._TYPECODE).append(" AS b}, ");
		builder.append("{").append(MatchModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{b:").append(MatchBetModel.PLAYER).append("}").append("=?player ");
		builder.append("AND ").append("{m:").append(MatchModel.PK).append("}={b:").append(MatchBetModel.MATCH).append("} ");
		builder.append("AND ").append("{m:").append(MatchModel.GROUP).append("}").append(" IN ");
		builder.append("({{ ");
		builder.append("  SELECT {g:").append(GroupModel.PK).append("}");
		builder.append("  FROM {").append(GroupModel._TYPECODE).append(" AS g} ");
		builder.append("  WHERE ").append("{g:").append(GroupModel.COMPETITION).append("}").append("=?comp ");
		builder.append(" }})");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("player", player.getPk());
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<MatchBetModel> search(query).getResult();
	}

	@Override
	public List<MatchBetModel> findMatchBetByMatch(final MatchModel match)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {m:").append(MatchBetModel.PK).append("} ");
		builder.append("FROM {").append(MatchBetModel._TYPECODE).append(" AS m} ");
		builder.append("WHERE ").append("{m:").append(MatchBetModel.MATCH).append("}").append("=?match ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("match", match.getPk());

		return flexibleSearchService.<MatchBetModel> search(query).getResult();
	}
}

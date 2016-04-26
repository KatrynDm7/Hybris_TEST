/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.cuppy.daos.NewsDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.NewsModel;
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
@SingletonScopedComponent(value = "newsDao")
public class DefaultNewsDao implements NewsDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<NewsModel> findNews(final PlayerModel player, final int start, final int count)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {n:").append(NewsModel.PK).append("} ");
		builder.append("FROM {").append(NewsModel._TYPECODE).append(" AS n} ");
		builder.append("WHERE ").append("{n:").append(NewsModel.COMPETITION).append("} IS NULL ");
		builder.append("OR ").append("{n:").append(NewsModel.COMPETITION).append("} IN ");
		builder.append("({{ ");
		builder.append("  SELECT {c:").append(CompetitionModel.PK).append("} ");
		builder.append("  FROM {").append(CompetitionModel._TYPECODE).append(" AS c},");
		builder.append("       {").append(PlayerModel._TYPECODE).append(" AS p},");
		builder.append("       {CompetitionPlayerRelation as l} ");
		builder.append("  WHERE ").append("{p:").append(PlayerModel.PK).append("}={l:target} ");
		builder.append("  AND ").append("{c:").append(CompetitionModel.PK).append("}={l:source} ");
		builder.append("  AND ").append("{p:").append(PlayerModel.PK).append("}=?player ");
		builder.append("}}) ");
		builder.append("ORDER BY ").append("{n:").append(NewsModel.CREATIONTIME).append("} DESC");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setStart(start);
		query.setCount(count);
		query.setNeedTotal(true);
		query.addQueryParameter("player", player);
		return flexibleSearchService.<NewsModel> search(query).getResult();
	}
}

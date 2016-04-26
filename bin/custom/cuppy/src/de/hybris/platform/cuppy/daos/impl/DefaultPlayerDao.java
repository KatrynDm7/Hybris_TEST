/**
 * 
 */
package de.hybris.platform.cuppy.daos.impl;

import de.hybris.platform.cuppy.daos.PlayerDao;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "playerDao")
public class DefaultPlayerDao implements PlayerDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<PlayerModel> findAllPlayers(final CompetitionModel competition)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p},{CompetitionPlayerRelation as l} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.CONFIRMED).append("}").append("=?true ");
		builder.append("AND ").append("{p:").append(PlayerModel.PK).append("}={l:target}");
		builder.append("AND ").append("{l:source}=?comp ");
		builder.append("ORDER BY ").append("{p:").append(PlayerModel.UID).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("true", Boolean.TRUE);
		query.addQueryParameter("comp", competition);

		return flexibleSearchService.<PlayerModel> search(query).getResult();
	}

	@Override
	public List<PlayerModel> findAllPlayers()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.CONFIRMED).append("}").append("=?true ");
		builder.append("ORDER BY ").append("{p:").append(PlayerModel.UID).append("}");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("true", Boolean.TRUE);

		return flexibleSearchService.<PlayerModel> search(query).getResult();
	}

	@Override
	public List<ProfilePictureModel> findProfilePictureByCode(final String code)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(ProfilePictureModel.PK).append("} ");
		builder.append("FROM {").append(ProfilePictureModel._TYPECODE).append(" AS p} ");
		builder.append("WHERE ").append("{p:").append(ProfilePictureModel.CODE).append("}").append("=?code ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("code", code);

		return flexibleSearchService.<ProfilePictureModel> search(query).getResult();
	}

	@Override
	public List<PlayerModel> findPlayerByUid(final String uid)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.UID).append("}").append("=?id ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("id", uid);

		return flexibleSearchService.<PlayerModel> search(query).getResult();
	}

	@Override
	public List<PlayerModel> findPlayerByMail(final String mail)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT {p:").append(PlayerModel.PK).append("} ");
		builder.append("FROM {").append(PlayerModel._TYPECODE).append(" AS p} ");
		builder.append("WHERE ").append("{p:").append(PlayerModel.EMAIL).append("}").append("=?mail ");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
		query.setNeedTotal(true);
		query.addQueryParameter("mail", mail);

		return flexibleSearchService.<PlayerModel> search(query).getResult();
	}
}

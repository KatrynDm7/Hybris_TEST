/**
 * 
 */
package de.hybris.platform.cuppy.web.converters;

import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.web.data.GroupData;
import de.hybris.platform.cuppy.web.data.MatchData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "matchConverter")
public class MatchConverter extends GenericCollectionConverter<MatchModel, MatchData>
{
	@Autowired
	private CollectionConverter<GroupModel, GroupData> groupConverter;
	@Autowired
	private MatchService matchService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private I18NService i18nService;
	@Autowired
	private PlayerService playerService;

	@Override
	public MatchData convert(final MatchModel model, final MatchData data) throws ConversionException
	{
		data.setGroup(groupConverter.convert(model.getGroup()));

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				i18nService.setLocalizationFallbackEnabled(true);
				data.setHomeTeam(model.getHomeTeam().getName() == null ? model.getHomeTeam().getIsocode() : model.getHomeTeam()
						.getName());
				data.setGuestTeam(model.getGuestTeam().getName() == null ? model.getGuestTeam().getIsocode() : model.getGuestTeam()
						.getName());
			}
		});
		if (model.getGuestTeam().getFlag() != null)
		{
			data.setGuestFlagUrl(model.getGuestTeam().getFlag().getDownloadURL());
		}
		if (model.getHomeTeam().getFlag() != null)
		{
			data.setHomeFlagUrl(model.getHomeTeam().getFlag().getDownloadURL());
		}
		data.setMatchFinished(matchService.isMatchFinished(model));

		final PlayerModel player = playerService.getCurrentPlayer();
		if (player != null)
		{
			if (matchService.hasBet(model, player))
			{
				final MatchBetModel bet = matchService.getBet(model, player);
				data.setGuestBet(Integer.valueOf(bet.getGuestGoals()));
				data.setHomeBet(Integer.valueOf(bet.getHomeGoals()));
				data.setScore(data.isMatchFinished() ? matchService.getScore(bet) : 0);
			}
		}
		data.setMatchBetable(matchService.isBetable(model));
		return super.convert(model, data);
	}

	@Override
	protected MatchData createDestObject() throws ConversionException
	{
		return new MatchData();
	}

	public void setGroupConverter(final CollectionConverter<GroupModel, GroupData> groupConverter)
	{
		this.groupConverter = groupConverter;
	}

	public void setMatchService(final MatchService matchService)
	{
		this.matchService = matchService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}
}

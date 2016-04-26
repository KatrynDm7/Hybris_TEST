/**
 * 
 */
package de.hybris.platform.cuppy.web.converters;

import de.hybris.platform.cuppy.enums.CompetitionType;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.CompetitionData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "competitionConverter")
public class CompetitionConverter extends GenericCollectionConverter<CompetitionModel, CompetitionData>
{
	@Autowired
	private MatchService matchService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private CompetitionService competitionService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private I18NService i18nService;

	@Override
	public CompetitionData convert(final CompetitionModel competition, final CompetitionData data) throws ConversionException
	{
		if (competition == null)
		{
			return null;
		}
		return (CompetitionData) sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public Object execute()
			{
				i18nService.setLocalizationFallbackEnabled(true);

				try
				{
					data.setCurrentCompetition(competition.equals(competitionService.getCurrentCompetition()));
				}
				catch (final NoCompetitionAvailableException e)
				{
					// OK, set null
				}
				data.setActive(competitionService.getActiveCompetitions().contains(competition));
				data.setDeactivatable(matchService.getBets(competition, playerService.getCurrentPlayer()).isEmpty());
				data.setTournament(competition.getType().equals(CompetitionType.TOURNAMENT));
				return CompetitionConverter.super.convert(competition, data);
			}
		});
	}

	@Override
	protected CompetitionData createDestObject() throws ConversionException
	{
		return new CompetitionData();
	}

	public void setMatchService(final MatchService matchService)
	{
		this.matchService = matchService;
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}

	public void setCompetitionService(final CompetitionService competitionService)
	{
		this.competitionService = competitionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
}

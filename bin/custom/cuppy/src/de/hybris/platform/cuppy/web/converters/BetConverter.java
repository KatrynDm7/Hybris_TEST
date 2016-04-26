/**
 * 
 */
package de.hybris.platform.cuppy.web.converters;

import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.web.data.BetData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "betConverter")
public class BetConverter extends GenericCollectionConverter<MatchBetModel, BetData>
{
	private static final Logger LOG = Logger.getLogger(BetConverter.class);

	@Autowired
	private MatchService matchService;
	@Autowired
	private PlayerService playerService;

	@Override
	public BetData convert(final MatchBetModel model, final BetData data) throws ConversionException
	{
		data.setMatchFinished(matchService.isMatchFinished(model.getMatch()));
		data.setPlayerId(model.getPlayer().getUid());
		data.setPlayerName(model.getPlayer().getName());
		data.setGuestBet(Integer.valueOf(model.getGuestGoals()));
		data.setHomeBet(Integer.valueOf(model.getHomeGoals()));
		data.setScore(data.isMatchFinished() ? matchService.getScore(model) : 0);
		if (model.getPlayer().getProfilePicture() == null)
		{
			try
			{
				data.setPlayerPictureUrl(playerService.getDefaultProfilePicture().getDownloadURL());
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Could not find default picture. Upload an image with mediacode '" + CuppyConstants.DEFAULT_PICTURE_CODE
						+ "' to the system.");
			}
		}
		else
		{
			data.setPlayerPictureUrl(model.getPlayer().getProfilePicture().getDownloadURL());
		}
		if (model.getPlayer().getCountry() != null && model.getPlayer().getCountry().getFlag() != null)
		{
			data.setPlayerFlagUrl(model.getPlayer().getCountry().getFlag().getDownloadURL());
		}

		return super.convert(model, data);
	}

	@Override
	protected BetData createDestObject() throws ConversionException
	{
		return new BetData();
	}

	public void setMatchService(final MatchService matchService)
	{
		this.matchService = matchService;
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}
}

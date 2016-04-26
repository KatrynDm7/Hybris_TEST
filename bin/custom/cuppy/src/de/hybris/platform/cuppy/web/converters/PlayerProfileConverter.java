/**
 * 
 */
package de.hybris.platform.cuppy.web.converters;

import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "playerProfileConverter")
public class PlayerProfileConverter extends GenericCollectionConverter<PlayerModel, PlayerProfileData>
{
	private static final Logger LOG = Logger.getLogger(PlayerProfileConverter.class);

	@Autowired
	private PlayerService playerService;

	@Override
	public PlayerProfileData convert(final PlayerModel model, final PlayerProfileData data) throws ConversionException
	{
		data.setId(model.getUid());
		if (model.getProfilePicture() == null)
		{
			try
			{
				data.setPictureUrl(playerService.getDefaultProfilePicture().getDownloadURL());
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Could not find default picture. Upload an image with mediacode '" + CuppyConstants.DEFAULT_PICTURE_CODE
						+ "' to the system.");
			}
		}
		else
		{
			data.setPictureUrl(model.getProfilePicture().getDownloadURL());
		}
		data.setLocale(new Locale("", model.getCountry().getIsocode()));
		return super.convert(model, data);
	}

	@Override
	protected PlayerProfileData createDestObject() throws ConversionException
	{
		return new PlayerProfileData();
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}
}

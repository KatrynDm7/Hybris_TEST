/**
 * 
 */
package de.hybris.platform.cuppy.web.facades.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.RankingData;
import de.hybris.platform.cuppy.services.RankingEntryData;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.cuppy.web.converters.CollectionConverter;
import de.hybris.platform.cuppy.web.data.CompetitionData;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.cuppy.web.data.PlayerRankingData;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;
import de.hybris.platform.servicelayer.dto.converter.GenericConverter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "playerFacade")
public class DefaultPlayerFacade implements PlayerFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultPlayerFacade.class);
	@Autowired
	private PlayerService playerService;

	@Autowired
	private UserService userService;

	@Autowired
	private CompetitionService competitionService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private CollectionConverter<CompetitionModel, CompetitionData> competitionConverter;

	@Autowired
	private CollectionConverter<PlayerModel, PlayerProfileData> playerProfileConverter;

	@Override
	public List<PlayerRankingData> getRankings()
	{
		final CompetitionModel compModel = competitionService.getCurrentCompetition();
		if (compModel == null)
		{
			return Collections.EMPTY_LIST;
		}
		final List<PlayerRankingData> result = convertToRankings(playerService.getRanking(compModel));
		if (result.isEmpty())
		{
			return Collections.EMPTY_LIST;
		}
		return result;
	}

	@Override
	public PlayerRankingData getRanking(final String uid)
	{
		final List<PlayerRankingData> rankings = getRankings();
		for (final PlayerRankingData data : rankings)
		{
			if (data.getPlayerId().equals(uid))
			{
				return data;
			}
		}
		return null;
	}

	@Override
	public void registerPlayer(final PlayerProfileData registration)
	{
		final GenericConverter<PlayerProfileData, PlayerModel> converter = new GenericConverter<PlayerProfileData, PlayerModel>();
		final PlayerModel player = converter.convert(registration, playerService.createPlayer());

		player.setUid(registration.getId());
		player.setCountry(commonI18NService.getCountry(registration.getLocale().getCountry().toUpperCase()));
		try
		{
			player.setSessionLanguage(commonI18NService.getLanguage(registration.getLocale().getLanguage().toLowerCase()));
		}
		catch (final UnknownIdentifierException e)
		{
			player.setSessionLanguage(commonI18NService.getLanguage("en"));
		}

		playerService.registerPlayer(player);
		userService.setPassword(registration.getId(), registration.getPassword());
	}

	@Override
	public List<Locale> getAllCountries()
	{
		final List<Locale> result = new ArrayList<Locale>();
		final List<CountryModel> countries = commonI18NService.getAllCountries();
		for (final CountryModel country : countries)
		{
			if (country instanceof TeamModel)
			{
				continue;
			}
			result.add(new Locale(country.getIsocode(), country.getIsocode()));
		}
		Collections.sort(result, new Comparator<Locale>()
		{
			@Override
			public int compare(final Locale locale1, final Locale locale2)
			{
				return locale1.getDisplayCountry().compareTo(locale2.getDisplayCountry());
			}
		});
		return result;
	}

	@Override
	public PlayerProfileData getCurrentPlayer()
	{
		final PlayerModel player = playerService.getCurrentPlayer();
		if (player == null)
		{
			return null;
		}
		return playerProfileConverter.convert(player);
	}

	@Override
	public PlayerProfileData getProfile(final String uid)
	{
		final PlayerModel player = playerService.getPlayer(uid);
		if (player == null)
		{
			return null;
		}
		return playerProfileConverter.convert(player);
	}

	@Override
	public void updatePlayer(final PlayerProfileData player)
	{
		final PlayerModel curPlayer = playerService.getCurrentPlayer();
		if (curPlayer == null)
		{
			LOG.error("Current user is not a player, can not update " + player.getId());
			return;
		}
		if (!(curPlayer.getUid().equals(player.getId())))
		{
			LOG.error("Tried to update player " + player.getId() + " but it is not current player " + curPlayer.getUid());
			return;
		}
		curPlayer.setName(player.getName());
		curPlayer.setEMail(player.getEMail());
		curPlayer.setCountry(commonI18NService.getCountry(player.getLocale().getCountry().toUpperCase()));
		if (StringUtils.isNotBlank(player.getPassword()))
		{
			userService.setPassword(player.getId(), player.getPassword());
		}
		playerService.updatePlayer(curPlayer);
	}

	@Override
	public boolean isCurrentPlayerAdmin()
	{
		final UserGroupModel userGroup = userService.getUserGroupForUID(CuppyConstants.USERGROUP_CUPPYADMINS);
		final PlayerModel curPlayer = playerService.getCurrentPlayer();
		if (curPlayer == null)
		{
			return false;
		}
		return curPlayer.getGroups().contains(userGroup);
	}

	@Override
	public String uploadProfilePicture(final byte[] byteData, final String fileName)
	{
		final PlayerModel currentPlayer = playerService.getCurrentPlayer();
		if (currentPlayer == null)
		{
			LOG.error("Current user is not a player, can not update profile picture");
			return "";
		}
		playerService.uploadProfilePicture(currentPlayer, byteData, fileName);
		return currentPlayer.getProfilePicture().getDownloadURL();
	}

	private List<PlayerRankingData> convertToRankings(final RankingData ranking)
	{
		//get default king picture
		String defaultPictureUrl = null;
		try
		{
			defaultPictureUrl = playerService.getDefaultProfilePicture().getDownloadURL();
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Could not find default picture. Upload an image with mediacode '" + CuppyConstants.DEFAULT_PICTURE_CODE
					+ "' to the system.");
		}

		final List<PlayerRankingData> result = new ArrayList<PlayerRankingData>();
		for (final RankingEntryData entry : ranking.getEntries())
		{
			result.add(convertToRanking(ranking, entry, defaultPictureUrl));
		}
		return result;
	}

	private PlayerRankingData convertToRanking(final RankingData ranking, final RankingEntryData entry,
			final String defaultPictureUrl)
	{
		final GenericConverter<RankingEntryData, PlayerRankingData> converter = new GenericConverter<RankingEntryData, PlayerRankingData>();
		final PlayerRankingData result = converter.convert(entry, new PlayerRankingData());
		result.setPlayerId(entry.getPlayer().getUid());
		result.setPlayerName(entry.getPlayer().getName() != null ? entry.getPlayer().getName() : entry.getPlayer().getUid());
		result.setMatchday(ranking.getMatchday());
		result.setLastMatchday(ranking.getLastMatchday());
		if (entry.getPlayer().getCountry().getFlag() != null)
		{
			result.setFlagUrl(entry.getPlayer().getCountry().getFlag().getDownloadURL());
		}
		if (entry.getPlayer().getProfilePicture() == null)
		{
			try
			{
				result.setPictureUrl(defaultPictureUrl);
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Could not find default picture. Upload an image with mediacode '" + CuppyConstants.DEFAULT_PICTURE_CODE
						+ "' to the system.");
			}
		}
		else
		{
			result.setPictureUrl(entry.getPlayer().getProfilePicture().getDownloadURL());
		}
		result.setLocale(new Locale("", entry.getPlayer().getCountry().getIsocode()));
		result.setPlayerEMail(entry.getPlayer().getEMail());
		result.setPlayerOnline(playerService.isPlayerOnline(entry.getPlayer().getUid()));
		if (result.getRank() == 1 && result.getPictureUrl() != null && result.getPictureUrl().equals(defaultPictureUrl))
		{
			try
			{
				result.setPictureUrl(playerService.getDefaultKingProfilePicture().getDownloadURL());
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Could not find default king picture. Upload an image with mediacode '"
						+ CuppyConstants.DEFAULT_KING_PICTURE_CODE + "' to the system.");
			}
		}
		return result;
	}

	@Override
	public boolean isPlayerOnline(final String playerId)
	{
		return playerService.isPlayerOnline(playerId);
	}

	@Override
	public CompetitionData getCurrentCompetition()
	{
		return competitionConverter.convert(competitionService.getCurrentCompetition());
	}

	@Override
	public void setCurrentCompetition(final String code)
	{
		final CompetitionModel comp = competitionService.getCompetition(code);
		competitionService.setCurrentCompetition(comp);
	}

	@Override
	public List<CompetitionData> getActiveCompetitions()
	{
		return competitionConverter.convertAll(competitionService.getActiveCompetitions());
	}

	@Override
	public List<CompetitionData> getActiveFinishedCompetitions()
	{
		final List<CompetitionModel> finishedComps = new ArrayList<CompetitionModel>();
		for (final CompetitionModel comp : competitionService.getActiveCompetitions())
		{
			if (comp.isFinished())
			{
				finishedComps.add(comp);
			}
		}
		return competitionConverter.convertAll(finishedComps);
	}

	@Override
	public List<CompetitionData> getActiveUnfinishedCompetitions()
	{
		final List<CompetitionModel> unfinishedComps = new ArrayList<CompetitionModel>();
		for (final CompetitionModel comp : competitionService.getActiveCompetitions())
		{
			if (!comp.isFinished())
			{
				unfinishedComps.add(comp);
			}
		}
		return competitionConverter.convertAll(unfinishedComps);
	}

	@Override
	public void setActiveCompetitions(final List<CompetitionData> competitions)
	{
		final List<CompetitionModel> compModels = new ArrayList<CompetitionModel>();
		for (final CompetitionData comp : competitions)
		{
			final CompetitionModel compModel = competitionService.getCompetition(comp.getCode());
			if (compModel != null)
			{
				compModels.add(compModel);
			}
		}
		competitionService.setActiveCompetitions(compModels);
	}

	@Override
	public List<CompetitionData> getAllCompetitions()
	{
		return competitionConverter.convertAll(competitionService.getAllCompetitions());
	}

	@Override
	public void forgotPassword(final String mail)
	{
		playerService.forgotPassword(mail);
	}

	public void setPlayerService(final PlayerService playerService)
	{
		this.playerService = playerService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public void setCompetitionService(final CompetitionService competitionService)
	{
		this.competitionService = competitionService;
	}

	public void setCompetitionConverter(final CollectionConverter<CompetitionModel, CompetitionData> competitionConverter)
	{
		this.competitionConverter = competitionConverter;
	}

	public void setPlayerConverter(final CollectionConverter<PlayerModel, PlayerProfileData> playerConverter)
	{
		this.playerProfileConverter = playerConverter;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public void setPlayerProfileConverter(final CollectionConverter<PlayerModel, PlayerProfileData> playerProfileConverter)
	{
		this.playerProfileConverter = playerProfileConverter;
	}
}

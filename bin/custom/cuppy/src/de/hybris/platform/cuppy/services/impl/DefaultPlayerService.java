/**
 * 
 */
package de.hybris.platform.cuppy.services.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.daos.CompetitionDao;
import de.hybris.platform.cuppy.daos.PlayerDao;
import de.hybris.platform.cuppy.jalo.Player;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;
import de.hybris.platform.cuppy.services.MailService;
import de.hybris.platform.cuppy.services.MatchService;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.services.RandomizeStrategy;
import de.hybris.platform.cuppy.services.RankingData;
import de.hybris.platform.cuppy.services.RankingEntryData;
import de.hybris.platform.cuppy.services.SingletonScopedComponent;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author andreas.thaler
 * 
 */
@SingletonScopedComponent(value = "playerService")
public class DefaultPlayerService implements PlayerService
{
	private final static Logger LOG = Logger.getLogger(DefaultPlayerService.class);
	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	@Autowired
	private PlayerDao playerDao;

	@Autowired
	private MailService mailService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private RandomizeStrategy randomizeStrategy;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private CompetitionDao competitionDao;

	@Override
	public void confirmPlayer(final PlayerModel player)
	{
		player.setConfirmed(true);
		modelService.save(player);

		LOG.info("Player " + player.getUid() + " has been confirmed.");

		mailService.sendConfirmationMail(player);
	}

	@Override
	public PlayerModel getCurrentPlayer()
	{
		final UserModel user = userService.getCurrentUser();
		if (!(user instanceof PlayerModel))
		{
			return null;
		}
		return (PlayerModel) user;
	}

	@Override
	public void setCurrentPlayer(final PlayerModel player)
	{
		userService.setCurrentUser(player);
	}

	@Override
	public List<PlayerModel> getPlayers(final CompetitionModel competition)
	{
		return playerDao.findAllPlayers(competition);
	}

	@Override
	public List<PlayerModel> getAllPlayers()
	{
		return playerDao.findAllPlayers();
	}

	@Override
	public ProfilePictureModel getProfilePicture(final String code)
	{
		final List<ProfilePictureModel> result = playerDao.findProfilePictureByCode(code);

		if (result.isEmpty())
		{
			throw new UnknownIdentifierException("Picture with code '" + code + "' not found!");
		}
		else if (result.size() > 1)
		{
			throw new AmbiguousIdentifierException("Picture code '" + code + "' is not unique, " + result.size() + " groups found!");
		}
		return result.get(0);
	}

	@Override
	public ProfilePictureModel getDefaultProfilePicture()
	{
		return getProfilePicture(CuppyConstants.DEFAULT_PICTURE_CODE);
	}

	@Override
	public ProfilePictureModel getDefaultKingProfilePicture()
	{
		return getProfilePicture(CuppyConstants.DEFAULT_KING_PICTURE_CODE);
	}

	@Override
	public void registerPlayer(final PlayerModel player)
	{
		if (!playerDao.findPlayerByUid(player.getUid()).isEmpty())
		{
			throw new AmbiguousIdentifierException("Player with id " + player.getUid() + " already exists");
		}
		modelService.save(player);

		LOG.info("New player " + player.getUid() + " has been registered.");

		final UserGroupModel adminGroup = userService.getUserGroupForUID(CuppyConstants.USERGROUP_CUPPYADMINS);

		mailService.sendRegistrationMail(player, new ArrayList<PlayerModel>((Set) adminGroup.getMembers()));
	}

	@Override
	public PlayerModel createPlayer()
	{
		final PlayerModel result = modelService.create(PlayerModel.class);
		result.setGroups((Set) Collections.singleton(userService.getUserGroupForUID(CuppyConstants.USERGROUP_CUPPYPLAYERS)));
		return result;
	}

	@Override
	public void updatePlayer(final PlayerModel curPlayer)
	{
		modelService.save(curPlayer);
	}

	@Override
	public void uploadProfilePicture(final PlayerModel currentPlayer, final byte[] byteData, final String fileName)
	{
		final String pictureCode = PK.createUUIDPK(0).toString();
		final ProfilePictureModel pic = modelService.create(ProfilePictureModel.class);
		pic.setCode(pictureCode);
		pic.setCatalogVersion(catalogService.getDefaultCatalog().getActiveCatalogVersion());
		modelService.save(pic);
		mediaService.setDataForMedia(pic, byteData);
		pic.setRealfilename(fileName);
		modelService.refresh(pic);
		currentPlayer.setProfilePicture(pic);
		modelService.save(currentPlayer);
	}

	@Override
	public PlayerModel getPlayer(final String uid)
	{
		final List<PlayerModel> result = playerDao.findPlayerByUid(uid);

		if (result.isEmpty())
		{
			throw new UnknownIdentifierException("Player with uid '" + uid + "' not found!");
		}
		else if (result.size() > 1)
		{
			throw new AmbiguousIdentifierException("Player uid '" + uid + "' is not unique, " + result.size() + " players found!");
		}
		return result.get(0);
	}

	@Override
	public PlayerModel getRandomPlayer(final CompetitionModel competition)
	{
		final List<PlayerModel> players = playerDao.findAllPlayers(competition);
		if (players.isEmpty())
		{
			return null;
		}
		return players.get(randomizeStrategy.getNext(0, players.size()));
	}

	@Override
	public boolean isPlayerOnline(final String playerId)
	{
		for (final JaloSession session : JaloConnection.getInstance().getAllSessions())
		{
			if (session.getUser() instanceof Player && session.getUser().getUID().equals(playerId))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isPlayerActivatedForCompetition(final CompetitionModel competition, final PlayerModel player)
	{
		return player.getCompetitions().contains(competition);
	}

	@Override
	public void forgotPassword(final String mail)
	{
		final List<PlayerModel> players = playerDao.findPlayerByMail(mail);
		ServicesUtil.validateIfSingleResult(players, PlayerModel.class, PlayerModel.EMAIL, mail);

		final PlayerModel player = players.get(0);
		final String newPwd = generatePassword(5);
		userService.setPassword(player.getUid(), newPwd);
		mailService.sendNewPassword(player, newPwd);
	}

	@Override
	public RankingData getRanking(final CompetitionModel competition)
	{
		return getRanking(competition, false);
	}

	@Override
	public PlayerModel getPlayerForEmail(final String email)
	{
		final List<PlayerModel> players = playerDao.findPlayerByMail(email);
		if (players.isEmpty())
		{
			throw new UnknownIdentifierException("No player with email address: " + email);
		}
		if (players.size() > 1)
		{
			throw new AmbiguousIdentifierException("Ambinguous email address: " + email);
		}
		return players.get(0);
	}

	private RankingData getRanking(final CompetitionModel competition, final boolean onlyModified)
	{
		boolean modified = false;
		final RankingData ranking = new RankingData();
		ranking.setCompetition(competition);
		Integer matchday = matchService.getCurrentCompletedMatchday(competition);
		if (matchday == null)
		{
			ranking.setMatchday(0);
			ranking.setLastMatchday(0);
		}
		else
		{
			ranking.setMatchday(matchday.intValue());
			matchday = matchService.getLastCompletedMatchday(competition);
			if (matchday == null)
			{
				ranking.setLastMatchday(0);
			}
			else
			{
				ranking.setLastMatchday(matchday.intValue());
			}
		}
		final List<RankingEntryData> entries = new ArrayList<RankingEntryData>();
		ranking.setEntries(entries);
		for (final PlayerModel player : competition.getPlayers())
		{
			modified = createRanking(competition, player, ranking) || modified;
		}

		if (onlyModified && !modified)
		{
			return null;
		}

		//first sort by last score to get last rank
		Collections.sort(entries, new ByScoreRankingEntryComparator());

		//set last rank
		for (int i = 0; i < entries.size(); i++)
		{
			if (i > 0 && entries.get(i - 1).getLastScore() == entries.get(i).getLastScore())
			{
				entries.get(i).setLastRank(entries.get(i - 1).getLastRank());
			}
			else
			{
				entries.get(i).setLastRank(i + 1);
			}
		}

		// sort by current score to get current rank
		Collections.sort(entries, new ByLastScoreRankingEntryComparator());

		//set current rank
		for (int i = 0; i < entries.size(); i++)
		{
			if (i > 0 && entries.get(i - 1).getScore() == entries.get(i).getScore())
			{
				entries.get(i).setRank(entries.get(i - 1).getRank());
			}
			else
			{
				entries.get(i).setRank(i + 1);
			}
		}

		return ranking;
	}

	@Override
	public List<RankingData> getRankingsChangedSince(final Date date)
	{
		final List<RankingData> result = new ArrayList<RankingData>();
		for (final CompetitionModel competition : competitionDao.findCompetitions())
		{
			if (date == null || matchService.isLastCompletedMatchdayModifiedAfter(competition, date))
			{
				final RankingData ranking = getRanking(competition, true);
				if (ranking != null)
				{
					result.add(ranking);
				}
			}
		}
		return result;
	}

	@Override
	public List<RankingData> getRankings()
	{
		final List<RankingData> result = new ArrayList<RankingData>();
		for (final CompetitionModel competition : competitionDao.findCompetitions())
		{
			final RankingData ranking = getRanking(competition, false);
			if (ranking != null)
			{
				result.add(ranking);
			}
		}
		return result;
	}

	@Override
	public List<RankingData> filterRankingsForPlayer(final List<RankingData> rankings, final PlayerModel player)
	{
		final List<RankingData> result = new ArrayList<RankingData>();
		for (final RankingData ranking : rankings)
		{
			if (player.getCompetitions().contains(ranking.getCompetition()))
			{
				result.add(ranking);
			}
		}
		return result;
	}



	private class ByScoreRankingEntryComparator implements Comparator<RankingEntryData>
	{
		@Override
		public int compare(final RankingEntryData ranking1, final RankingEntryData ranking2)
		{
			int result = ranking2.getLastScore() - ranking1.getLastScore();
			if (result == 0 && ranking1.getPlayer().getName() != null && ranking2.getPlayer().getName() != null)
			{
				result = ranking1.getPlayer().getName().compareToIgnoreCase(ranking2.getPlayer().getName());
			}
			return result;
		}
	}

	private class ByLastScoreRankingEntryComparator implements Comparator<RankingEntryData>
	{
		@Override
		public int compare(final RankingEntryData ranking1, final RankingEntryData ranking2)
		{
			int result = ranking2.getScore() - ranking1.getScore();
			if (result == 0 && ranking1.getPlayer().getName() != null && ranking2.getPlayer().getName() != null)
			{
				result = ranking1.getPlayer().getName().compareToIgnoreCase(ranking2.getPlayer().getName());
			}
			return result;
		}
	}

	private boolean createRanking(final CompetitionModel competition, final PlayerModel player, final RankingData ranking)
	{
		final RankingEntryData result = new RankingEntryData();
		result.setPlayer(player);
		result.setScore(matchService.getScore(competition, player));
		result.setLastScore(matchService.getScoreWithoutCurrentMatchday(competition, player));
		ranking.getEntries().add(result);
		return result.getRank() != result.getLastRank();
	}

	private static final String PWD_CHARS = "!%?#0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static String generatePassword(final int length)
	{
		final Random rand = new Random(System.currentTimeMillis());
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			final int pos = rand.nextInt(PWD_CHARS.length());
			buffer.append(PWD_CHARS.charAt(pos));
		}
		return buffer.toString();
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public void setPlayerDao(final PlayerDao playerDao)
	{
		this.playerDao = playerDao;
	}

	public void setMailService(final MailService mailService)
	{
		this.mailService = mailService;
	}

	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}
}

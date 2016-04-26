/**
 * 
 */
package de.hybris.platform.cuppy.services;

import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;

import java.util.List;


/**
 * Service for sending cuppy mails
 * 
 * @author andreas.thaler
 */
public interface MailService
{
	/**
	 * Sends a registration mail
	 * 
	 * @param player
	 * @param admins
	 */
	//TODO:TEST
	void sendRegistrationMail(PlayerModel player, List<PlayerModel> admins);

	/**
	 * Sends a confirmation mail
	 * 
	 * @param player
	 */
	//TODO:TEST
	void sendConfirmationMail(PlayerModel player);

	/**
	 * Sends a newsletter
	 * 
	 * @param news
	 * @param players
	 */
	//TODO:TEST
	void sendNewsletter(NewsModel news, List<PlayerModel> players);

	/**
	 * Sends a reminder mail
	 * 
	 * @param matches
	 * @param player
	 */
	//TODO:TEST
	void sendReminder(List<MatchModel> matches, PlayerModel player);

	/**
	 * Sends a new password
	 * 
	 * @param player
	 * @param newPassword
	 */
	//TODO:TEST
	void sendNewPassword(PlayerModel player, String newPassword);

	/**
	 * Sends a ranking mail
	 * 
	 * @param player
	 * @param rankings
	 */
	//TODO:Test
	void sendRankingMail(PlayerModel player, List<RankingData> rankings);
}

/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface PlayerDao
{
	/**
	 * Tested.
	 */
	List<PlayerModel> findAllPlayers(CompetitionModel competition);

	//TODO: TEST
	List<PlayerModel> findAllPlayers();

	//TODO: TEST
	List<PlayerModel> findPlayerByUid(String uid);

	//TODO: TEST
	List<PlayerModel> findPlayerByMail(String mail);

	/**
	 * Tested.
	 */
	List<ProfilePictureModel> findProfilePictureByCode(String code);
}

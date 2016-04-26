/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface MatchBetDao
{
	/**
	 * Tested.
	 */
	List<MatchBetModel> findMatchBetByPlayerAndMatch(PlayerModel player, MatchModel match);

	/**
	 * Tested.
	 */
	List<MatchBetModel> findFinishedMatchBetsByPlayer(CompetitionModel competition, PlayerModel player);

	//TODO: TEST
	List<MatchBetModel> findMatchBetsByPlayer(CompetitionModel competition, PlayerModel player);

	//TODO: TEST
	List<MatchBetModel> findMatchBetByMatch(MatchModel match);
}

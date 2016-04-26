/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;

import java.util.List;


/**
 * @author andreas.thaler
 * 
 */
public interface NewsDao
{
	//TODO: TEST
	List<NewsModel> findNews(PlayerModel player, int start, int count);
}

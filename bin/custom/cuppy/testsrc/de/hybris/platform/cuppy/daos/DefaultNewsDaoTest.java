/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.services.PlayerService;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class DefaultNewsDaoTest extends AbstractDaoTest
{
	@Resource
	private NewsDao newsDao;
	@Resource
	private PlayerService playerService;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	@Before
	public void setUp() throws Exception //NOPMD
	{
		cuppySystemSetup.importBasics(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_BASICS, new String[]
		{ CuppyConstants.PARAM_BASICS_PLAYERS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP, CuppyConstants.PARAM_WC2002_BETS_PRELIMINARIES }), Type.NOTDEFINED,
				CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importEC2008(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_EC2008, new String[]
		{ CuppyConstants.PARAM_EC2008_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importWC2010(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2010, new String[]
		{ CuppyConstants.PARAM_WC2010_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importTestNews();
	}

	@Test
	public void testLatestNews()
	{
		final PlayerModel player = playerService.getPlayer("sternthaler");
		final List<NewsModel> news = newsDao.findNews(player, 0, -1);
		assertEquals(2, news.size());
	}
}

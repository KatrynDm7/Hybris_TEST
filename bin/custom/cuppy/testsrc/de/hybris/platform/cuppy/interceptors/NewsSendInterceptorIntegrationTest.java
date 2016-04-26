/**
 * 
 */
package de.hybris.platform.cuppy.interceptors;

import static junit.framework.Assert.assertNotNull;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.services.CompetitionService;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;
import de.hybris.platform.impex.systemsetup.ImpExSystemSetup;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class NewsSendInterceptorIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ServiceLayerDataSetup serviceLayerDataSetup;
	@Resource
	private ImpExSystemSetup impExSystemSetup;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	@Resource
	private ModelService modelService;
	@Resource
	private CompetitionService competitionService;

	private CompetitionModel competition;

	@Before
	public void setUp() throws Exception //NOPMD
	{
		new CoreBasicDataCreator().createEssentialData(null, null);
		CatalogManager.getInstance().createEssentialData(Collections.EMPTY_MAP, null);
		serviceLayerDataSetup.setup();
		impExSystemSetup.createAutoImpexEssentialData(new SystemSetupContext(Collections.EMPTY_MAP, Type.ESSENTIAL,
				CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		competition = competitionService.getCompetition("wc2002");
		assertNotNull(competition);
	}

	@Test
	public void testNewsCreationWithEmailWithoutCompetition()
	{
		final NewsModel news = modelService.create(NewsModel.class);
		news.setContent("test");
		news.setEMail(true);
		modelService.save(news);
	}

	@Test
	public void testNewsCreationWithEmailWithCompetition()
	{
		final NewsModel news = modelService.create(NewsModel.class);
		news.setContent("test");
		news.setEMail(true);
		news.setCompetition(competition);
		modelService.save(news);
	}
}

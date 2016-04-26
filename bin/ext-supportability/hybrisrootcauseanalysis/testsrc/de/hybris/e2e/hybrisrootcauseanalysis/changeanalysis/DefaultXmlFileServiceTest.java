/**
 *
 */
package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EXmlFileService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl.DefaultXmlFileService;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.impl.DefaultMediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Utilities;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author I310388
 *
 */
@IntegrationTest
public class DefaultXmlFileServiceTest extends ServicelayerTransactionalTest
{
	/** Edit the local|project.properties to change logging behaviour (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultXmlFileServiceTest.class.getName());

	private E2EXmlFileService xmlFileService;

	private MediaService mediaService;

	private static final String NAME_FILE_1 = "backoffice.xml";

	private static final String NAME_FILE_2 = "advancedSearch_AbstractPage_cockpitgroup_";

	private static final String NAME_SUB_FOLDER = "cockpitconfigurations";

	private MediaModel media1;
	private MediaModel media2;

	private CockpitUIComponentConfigurationModel cockpitUi;

	private ModelService modelService;

	@Before
	public void setUp()
	{
		xmlFileService = Registry.getApplicationContext().getBean(DefaultXmlFileService.class);
		mediaService = Registry.getApplicationContext().getBean(DefaultMediaService.class);

		modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");

		media1 = mediaService.getMedia("cockpitng-config");
		media2 = mediaService.getMedia("advanced_search_abstractpage_cockpitgroup_ui_config");

	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 */
	@Ignore
	@Test
	public void isFileExist1()
	{
		xmlFileService.copyToE2Efolder(media1, NAME_FILE_1);

		final File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();

		final File file = new File(dataDir.getAbsoluteFile() + File.separator + "e2erootcauseanalysis" + File.separator
				+ NAME_FILE_1);

		assertTrue("File creation", file.exists());
	}

	@Ignore
	@Test
	public void isFileExist2()
	{
		final File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();
		final File subfolder = new File(dataDir.getAbsoluteFile() + File.separator + "e2erootcauseanalysis" + File.separator
				+ NAME_SUB_FOLDER);

		final PrincipalModel principalModel = new PrincipalModel();
		principalModel.setUid("cockpitgroup");
		cockpitUi = new CockpitUIComponentConfigurationModel();
		cockpitUi.setCode("advancedSearch");
		cockpitUi.setObjectTemplateCode("AbstractPage");
		cockpitUi.setMedia(media2);
		cockpitUi.setPrincipal(principalModel);

		modelService.save(cockpitUi);

		final PK pk = cockpitUi.getPk();

		xmlFileService.copyToE2ECategorizedFold(cockpitUi, subfolder.getAbsolutePath());

		final File file = new File(subfolder.getAbsolutePath() + File.separator + NAME_FILE_2 + pk.toString() + ".xml");

		assertTrue("File creation", file.exists());
	}
}

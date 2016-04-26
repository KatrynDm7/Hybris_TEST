/**
 * 
 */
package de.hybris.platform.cuppy.daos;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.impex.systemsetup.ImpExSystemSetup;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;


/**
 * @author andreas.thaler
 * 
 */
@Ignore
public class AbstractDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private ServiceLayerDataSetup serviceLayerDataSetup;
	@Resource
	private ImpExSystemSetup impExSystemSetup;

	@Before
	public void setUpBasics() throws Exception //NOPMD
	{
		new CoreBasicDataCreator().createEssentialData(null, null);
		CatalogManager.getInstance().createEssentialData(Collections.EMPTY_MAP, null);
		//serviceLayerDataSetup.createJobPerformables();
		serviceLayerDataSetup.setup();
		impExSystemSetup.createAutoImpexEssentialData(new SystemSetupContext(Collections.EMPTY_MAP, Type.ESSENTIAL,
				CuppyConstants.EXTENSIONNAME));
	}
}

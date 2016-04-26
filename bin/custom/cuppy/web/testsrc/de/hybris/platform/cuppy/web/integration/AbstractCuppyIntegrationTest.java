/**
 * 
 */
package de.hybris.platform.cuppy.web.integration;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;
import de.hybris.platform.impex.systemsetup.ImpExSystemSetup;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup;
import de.hybris.platform.spring.TenantScope;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;


/**
 * @author andreas.thaler
 * 
 */
@Ignore
public class AbstractCuppyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = Logger.getLogger(AbstractCuppyIntegrationTest.class);

	@Resource
	private ServiceLayerDataSetup serviceLayerDataSetup;
	@Resource
	private ImpExSystemSetup impExSystemSetup;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	@Before
	public void prepareData() throws Exception //NOPMD
	{
		LOG.info("Preparing setup data");
		new CoreBasicDataCreator().createEssentialData(null, null);
		CatalogManager.getInstance().createEssentialData(Collections.EMPTY_MAP, null);
		serviceLayerDataSetup.setup();
		impExSystemSetup.createAutoImpexEssentialData(new SystemSetupContext(Collections.EMPTY_MAP, Type.ESSENTIAL,
				CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importBasics(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_BASICS, new String[]
		{ CuppyConstants.PARAM_BASICS_PLAYERS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));
		cuppySystemSetup.importWC2002(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_WC2002, new String[]
		{ CuppyConstants.PARAM_WC2002_SETUP, CuppyConstants.PARAM_WC2002_RESULTS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2002_RESULTS_FINALS, CuppyConstants.PARAM_WC2002_BETS_PRELIMINARIES,
				CuppyConstants.PARAM_WC2002_BETS_FINALS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		LOG.info("Finished preparation of setup data");

		LOG.info("Preparing session");
		JaloSession.getCurrentSession().setUser(UserManager.getInstance().getUserByLogin("sternthaler"));
		LOG.info("Finished preparation of session");
	}

	@Before
	@Override
	public void prepareApplicationContextAndSession()
	{
		LOG.info("Preparing application context");
		final GenericApplicationContext ctx = new GenericApplicationContext(Registry.getGlobalApplicationContext());
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
		xmlReader.loadBeanDefinitions(new ClassPathResource("/cuppy/cuppy-spring-services.xml", Registry.class.getClassLoader()));
		xmlReader
				.loadBeanDefinitions(new ClassPathResource("/cockpit/cockpit-spring-services.xml", Registry.class.getClassLoader()));
		ctx.getBeanFactory().registerScope("tenant", new TenantScope());
		ctx.refresh();
		ctx.getAutowireCapableBeanFactory().autowireBean(this);
		LOG.info("Finished preparation of application context");
	}
}

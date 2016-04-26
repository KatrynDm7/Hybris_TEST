/**
 *
 */
package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesXmlService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author I310388
 *
 */
public class DefaultXmlChangesServiceTest extends HybrisJUnit4TransactionalTest
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultXmlChangesServiceTest.class.getName());

	private E2EChangesXmlService changesXmlService;

	private static final String CODE = "cockpitng-config";

	private static final String NAME_FILE = "backoffice.xml";

	@Before
	public void setUp()
	{
		changesXmlService = (E2EChangesXmlService) Registry.getApplicationContext().getBean("defaultBackofficeChanges");

	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 */
	@Test
	public void getNameFileTest()
	{
		assertNotEquals("check name file ", "wbackoffice.xml", changesXmlService.getNameFile());
		assertEquals("check name file equal", NAME_FILE, changesXmlService.getNameFile());
	}

	@Test
	public void getCodeTest()
	{
		assertNotEquals("check name file ", "wbacmycode", changesXmlService.getCode());
		assertEquals("check name file equal", CODE, changesXmlService.getCode());
	}


}

/**
 *
 */
package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesPropertiesService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl.DefaultLicenceChangesService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;



/**
 * @author I310388
 *
 */
public class DefaultLicenceChangesServicetest extends HybrisJUnit4TransactionalTest
{

	private E2EChangesPropertiesService licenceService;

	private static final String NAME = "licence.properties";

	@Before
	public void setUp()
	{
		licenceService = Registry.getApplicationContext().getBean(DefaultLicenceChangesService.class);
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
	public void testSaveLicenceInProperties()
	{
		//test
		final Properties info = licenceService.getInfo();
		assertEquals("check the name file", info.getProperty("licence.email"), "support@hybris.com");
		assertEquals("check the name file", info.getProperty("licence.purpose"), null);
	}

	@Test
	public void testGetNameFile()
	{
		//test
		assertEquals("check the name file", licenceService.getNameFile(), NAME);
		assertNotEquals("check name file is different from original", licenceService.getNameFile(), "licensee.properties");
	}
}

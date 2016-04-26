/**
 *
 */
package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EPropertiesFileService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl.DefaultPropertiesFileService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author I310388
 *
 */
public class DefaultPropertiesFileServiceTest extends HybrisJUnit4TransactionalTest
{
	/** Edit the local|project.properties to change logging behaviour (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultPropertiesFileServiceTest.class.getName());

	private E2EPropertiesFileService propertiesFileService;

	private static final String NAME_FILE = "test.properties";

	private Properties prop;


	@Before
	public void setUp()
	{
		propertiesFileService = Registry.getApplicationContext().getBean(DefaultPropertiesFileService.class);
		prop = new Properties();
		prop.put("mykey1", "myvalue1");
		prop.put("mykey2", "myvalue2");
		//should create a file in the e2erootcauseanalysis
		propertiesFileService.writeFile(prop, NAME_FILE);
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
	public void isFileExist()
	{
		final File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();

		final File file = new File(dataDir.getAbsoluteFile() + File.separator + "e2erootcauseanalysis" + File.separator + NAME_FILE);

		assertTrue("File creation", file.exists());
	}

	@Test
	public void testPropertiesInFile()
	{
		final File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();
		final File file = new File(dataDir.getAbsoluteFile() + File.separator + "e2erootcauseanalysis" + File.separator + NAME_FILE);
		final Properties properties = new Properties();
		InputStream input = null;
		try
		{
			input = new FileInputStream(file);
			properties.load(input);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		assertEquals("check the properties is equal", properties, prop);
		assertEquals("check if the value is equals", properties.getProperty("mykey1"), prop.getProperty("mykey1"));
	}

}

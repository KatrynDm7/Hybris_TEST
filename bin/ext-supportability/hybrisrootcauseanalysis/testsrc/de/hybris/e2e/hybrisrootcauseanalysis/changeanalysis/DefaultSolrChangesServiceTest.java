/**
 *
 */
package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesPropertiesService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl.DefaultSolrChangesService;
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
public class DefaultSolrChangesServiceTest extends HybrisJUnit4TransactionalTest
{

	private E2EChangesPropertiesService solrService;

	private static final String NAME = "solr.properties";

	@Before
	public void setUp()
	{
		solrService = Registry.getApplicationContext().getBean(DefaultSolrChangesService.class);
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
		final Properties info = solrService.getInfo();
		assertEquals("check the name file", info.getProperty("solrfacetsearchconfig.apparel-deIndex.description"),
				"Apparel DE Solr Index");
		assertEquals("check the name file", info.getProperty("solrfacetsearchconfig.apparel-deIndex.keyword.5"), "Lieferung");
	}

	@Test
	public void testGetNameFile()
	{
		//test
		assertEquals("check the name file", solrService.getNameFile(), NAME);
		assertNotEquals("check name file is different from original", solrService.getNameFile(), "licensee.properties");
	}

}

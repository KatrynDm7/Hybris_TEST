/**
 * 
 */
package de.hybris.platform.cuppy.data;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.model.CountryFlagModel;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.cuppy.model.ProfilePictureModel;
import de.hybris.platform.cuppy.systemsetup.CuppySystemSetup;
import de.hybris.platform.impex.systemsetup.ImpExSystemSetup;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * @author andreas.thaler
 * 
 */
public class BasicDataTest extends ServicelayerTransactionalTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ServiceLayerDataSetup serviceLayerDataSetup;
	@Resource
	private ImpExSystemSetup impExSystemSetup;
	@Resource
	private CuppySystemSetup cuppySystemSetup;

	@Before
	public void setUp() throws Exception
	{
		new CoreBasicDataCreator().createEssentialData(null, null);
		CatalogManager.getInstance().createEssentialData(Collections.EMPTY_MAP, null);
		serviceLayerDataSetup.setup();
		impExSystemSetup.createAutoImpexEssentialData(new SystemSetupContext(Collections.EMPTY_MAP, Type.ESSENTIAL,
				CuppyConstants.EXTENSIONNAME));
	}

	@Test
	public void testBasics() throws Exception
	{
		assertEquals("Unexpected amount of groups imported", 0,
				flexibleSearchService.search("SELECT {PK} FROM {" + GroupModel._TYPECODE + "}").getTotalCount());

		assertEquals("Unexpected amount of countries imported", 13,
				flexibleSearchService.search("SELECT {PK} FROM {" + CountryModel._TYPECODE + "!}").getTotalCount());

		assertEquals("Unexpected amount of flags imported", 13,
				flexibleSearchService.search("SELECT {PK} FROM {" + CountryFlagModel._TYPECODE + "}").getTotalCount());

		assertEquals(
				"Not each country has a flag",
				0,
				flexibleSearchService.search(
						"SELECT {PK} FROM {" + CountryModel._TYPECODE + "!} WHERE {" + CountryModel.FLAG + "} IS NULL").getTotalCount());
	}

	@Test
	public void testPlayers() throws Exception //NOPMD
	{
		cuppySystemSetup.importBasics(new SystemSetupContext(Collections.singletonMap(CuppyConstants.PARAM_BASICS, new String[]
		{ CuppyConstants.PARAM_BASICS_PLAYERS }), Type.NOTDEFINED, CuppyConstants.EXTENSIONNAME));

		assertEquals("Unexpected amount of players imported", 19,
				flexibleSearchService.search("SELECT {PK} FROM {" + PlayerModel._TYPECODE + "}").getTotalCount());

		assertEquals("Unexpected amount of profile pictures imported", 3,
				flexibleSearchService.search("SELECT {PK} FROM {" + ProfilePictureModel._TYPECODE + "}").getTotalCount());

		assertEquals(
				"Amount of pictures at players is not as expected",
				1,
				flexibleSearchService.search(
						"SELECT {PK} FROM {" + PlayerModel._TYPECODE + "} WHERE {" + PlayerModel.PROFILEPICTURE + "} IS NOT NULL")
						.getTotalCount());
	}
}

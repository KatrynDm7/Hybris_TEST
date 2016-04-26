/**
 *
 */
package de.hybris.platform.financialservices.bundle.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.configurablebundleservices.bundle.BundleCommerceCartService;
import de.hybris.platform.configurablebundleservices.bundle.BundleRuleService;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.DisableProductBundleRuleModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;



@IntegrationTest
public class GeoDisableProductBundleRuleServiceIntegrationTest extends ServicelayerTest
{
	/**
	 *
	 */
	private static final String POSTAL_CODE_FOR_MATCH = "XX1";
	private static final String POSTAL_CODE_FOR_NON_MATCH = "XX4";

	private static final Logger LOG = Logger.getLogger(GeoDisableProductBundleRuleServiceIntegrationTest.class);

	private static final String TEST_BASESITE_UID = "testSite";

	@Resource
	private CartService cartService;

	@Resource
	private UserService userService;

	@Resource
	private AddressService addressService;

	@Resource
	private ModelService modelService;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private ProductService productService;

	@Resource
	private BundleTemplateService bundleTemplateService;

	@Resource
	private BundleCommerceCartService bundleCommerceCartService;

	private BundleRuleService bundleRuleService = new GeoDisableProductBundleRuleService();

	private ProductModel iphone4s16gb;
	private ProductModel iphone4s32gb;

	private BundleTemplateModel iPhoneDeviceSelectionBundleTemplate;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception
	{
		// final Create data for tests
		LOG.info("Creating data for DefaultBundleRuleServiceIntegrationTest ...");
		userService.setCurrentUser(userService.getAdminUser());
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);

		// importing test csv
		final String legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		LOG.info("Existing value for " + ImpExConstants.Params.LEGACY_MODE_KEY + " :" + legacyModeBackup);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");

		importCsv("/commerceservices/test/testCommerceCart.csv", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");

		importCsv("/subscriptionservices/test/testSubscriptionCommerceCartService.impex", "utf-8");
		importCsv("/configurablebundleservices/test/testBundleCommerceCartService.impex", "utf-8");
		importCsv("/configurablebundleservices/test/testApproveAllBundleTemplates.impex", "utf-8");
		importCsv("/financialstore/test/testDisableBundles.impex", "utf-8");
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);

		LOG.info("Finished data for DefaultBundleRuleServiceIntegrationTest " + (System.currentTimeMillis() - startTime) + "ms");

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
		catalogVersionService.setSessionCatalogVersion("testCatalog", "Online");

		iphone4s16gb = productService.getProductForCode("APPLE_IPHONE_4S_16GB");
		iphone4s32gb = productService.getProductForCode("APPLE_IPHONE_4S_32GB");

		iPhoneDeviceSelectionBundleTemplate = bundleTemplateService.getBundleTemplateForCode("IPhoneDeviceSelection");

		final GeoDisableProductBundleRuleService defaultBundleRuleService = (GeoDisableProductBundleRuleService) Registry
				.getApplicationContext().getBean("geoDisableProductBundleRuleService");
		setBundleRuleService(defaultBundleRuleService);

		final GeoDisableProductBundleRuleDao dao = (GeoDisableProductBundleRuleDao) Registry.getApplicationContext().getBean(
				"disableProductBundleRuleDao");

		defaultBundleRuleService.setDisableProductBundleRuleDao(dao);

		modelService.detachAll();
	}

	@Test
	public void testGetDisableRuleForBundleProductWhenInDummyRegionExists() throws CommerceCartModificationException
	{
		final UserModel telco = userService.getUserForUID("telco");
		final Collection<CartModel> cartModels = telco.getCarts();
		assertEquals(1, cartModels.size());

		final CartModel telcoMasterCart = cartModels.iterator().next();
		AddressModel address = telcoMasterCart.getDeliveryAddress();

		if (address == null)
		{
			address = addressService.createAddressForUser(telco);
			telcoMasterCart.setDeliveryAddress(address);
		}
		address.setPostalcode(POSTAL_CODE_FOR_MATCH);

		cartService.setSessionCart(telcoMasterCart);

		final DisableProductBundleRuleModel disableRule0 = getBundleRuleService().getDisableRuleForBundleProduct(telcoMasterCart,
				iphone4s16gb, iPhoneDeviceSelectionBundleTemplate, 1, false);
		assertNull(disableRule0);

		final DisableProductBundleRuleModel disableRule1 = getBundleRuleService().getDisableRuleForBundleProduct(telcoMasterCart,
				iphone4s32gb, iPhoneDeviceSelectionBundleTemplate, 1, false);
		assertNotNull(disableRule1);

		address.setPostalcode(POSTAL_CODE_FOR_NON_MATCH);
		final DisableProductBundleRuleModel disableRule2 = getBundleRuleService().getDisableRuleForBundleProduct(telcoMasterCart,
				iphone4s32gb, iPhoneDeviceSelectionBundleTemplate, 1, false);
		assertNull(disableRule2);


	}

	protected BundleRuleService getBundleRuleService()
	{
		return bundleRuleService;
	}

	public void setBundleRuleService(final GeoDisableProductBundleRuleService bundleRuleService)
	{
		this.bundleRuleService = bundleRuleService;
	}
}

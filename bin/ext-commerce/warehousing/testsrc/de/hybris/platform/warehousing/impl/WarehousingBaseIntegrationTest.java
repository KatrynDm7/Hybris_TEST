package de.hybris.platform.warehousing.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class WarehousingBaseIntegrationTest extends ServicelayerTransactionalTest
{
	private static Logger LOG = LoggerFactory.getLogger(WarehousingBaseIntegrationTest.class);

	@Resource
	protected PointOfServiceService pointOfServiceService;
	@Resource
	protected ProductService productService;
	@Resource
	protected UserService userService;
	@Resource
	protected WarehouseService warehouseService;
	@Resource
	protected BaseStoreService baseStoreService;

	public enum Products
	{
		batteriescharger, rechargeablebatteries, wirelessmouse;
	}

	public enum Customers
	{
		johnsmith, janedoe;
	}

	public enum PointsOfService
	{
		NewYork, Montreal, Kingston;
	}

	public enum Warehouses
	{
		newyork, quebec, ontario;
	}

	public enum BaseStores
	{
		ElectrotelCanada, ElectrotelUSA;
	}

	/**
	 * Create Users, Billing and Shipping Addresses and Payment Information data
	 *
	 * @throws Exception
	 */
	public void createSourcingUserData() throws Exception
	{
		LOG.debug("creating test users");
		importCsv("/warehousing/test/impex/user-test-data.impex", WarehousingTestConstants.ENCODING);
		final UserModel johnSmith = userService.getUserForUID(Customers.johnsmith.name());

		assertNotNull(johnSmith);
		assertFalse(johnSmith.getAddresses().isEmpty());
		assertFalse(johnSmith.getPaymentInfos().isEmpty());
	}

	/**
	 * Create Warehouses, Point-of-services, Products and Stock Levels data
	 *
	 * @throws Exception
	 */
	public void createSourcingStoreData() throws Exception
	{
		LOG.debug("creating electrotel test stores");
		importCsv("/warehousing/test/impex/electrotel-store-test-data.impex", WarehousingTestConstants.ENCODING);

		final ProductModel charger = productService.getProductForCode(Products.batteriescharger.name());
		assertEquals("rechargeable batteries charger", charger.getName(WarehousingTestConstants.DEFAULT_LOCALE));

		final PointOfServiceModel montreal = pointOfServiceService.getPointOfServiceForName(PointsOfService.Montreal.name());
		assertEquals("Peel Street", montreal.getAddress().getStreetname());

		final WarehouseModel quebec = warehouseService.getWarehouseForCode(Warehouses.quebec.name());
		assertFalse(quebec.getBaseStores().isEmpty());

		final BaseStoreModel canada = baseStoreService.getBaseStoreForUid(BaseStores.ElectrotelCanada.name());
		assertEquals(1, canada.getDeliveryCountries().size());
		assertEquals("CA", canada.getDeliveryCountries().iterator().next().getIsocode());
	}
}

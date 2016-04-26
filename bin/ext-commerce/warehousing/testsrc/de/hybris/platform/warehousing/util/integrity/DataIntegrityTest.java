package de.hybris.platform.warehousing.util.integrity;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.site.dao.BaseSiteDao;
import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.daos.CatalogVersionDao;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.ordersplitting.daos.WarehouseDao;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.stock.impl.StockLevelDao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.BaseStoreDao;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.util.BaseWarehousingIntegrationTest;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import de.hybris.platform.warehousing.util.models.Addresses;
import de.hybris.platform.warehousing.util.models.BaseSites;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.CatalogVersions;
import de.hybris.platform.warehousing.util.models.Catalogs;
import de.hybris.platform.warehousing.util.models.Consignments;
import de.hybris.platform.warehousing.util.models.Countries;
import de.hybris.platform.warehousing.util.models.Currencies;
import de.hybris.platform.warehousing.util.models.DeliveryModes;
import de.hybris.platform.warehousing.util.models.Languages;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Units;
import de.hybris.platform.warehousing.util.models.Vendors;
import de.hybris.platform.warehousing.util.models.Warehouses;
import de.hybris.platform.warehousing.util.models.Zones;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DataIntegrityTest extends BaseWarehousingIntegrationTest
{
	@Resource
	private LanguageDao languageDao;
	@Resource
	private Languages languages;
	private LanguageModel languageEnglish;

	@Resource
	private CountryDao countryDao;
	@Resource
	private Countries countries;
	private CountryModel countryCanada;
	private CountryModel countryUSA;

	@Resource
	private WarehousingDao<ZoneModel> zoneDao;
	@Resource
	private Zones zones;
	private ZoneModel zoneUSA;

	@Resource
	private DeliveryModeDao deliveryModeDao;
	@Resource
	private DeliveryModes deliveryModes;
	private DeliveryModeModel deliveryModePickup;

	@Resource
	private WarehousingDao<VendorModel> vendorDao;
	@Resource
	private Vendors vendors;
	private VendorModel vendorHybris;

	@Resource
	private CurrencyDao currencyDao;
	@Resource
	private Currencies currencies;
	private CurrencyModel currencyUSD;

	@Resource
	private UnitDao unitDao;
	@Resource
	private Units units;
	private UnitModel unitPiece;

	@Resource
	private CatalogDao catalogDao;
	@Resource
	private Catalogs catalogs;
	private CatalogModel catalogPrimary;

	@Resource
	private CatalogVersionDao catalogVersionDao;
	@Resource
	private CatalogVersions catalogVersions;
	private CatalogVersionModel catalogVersionStaging;

	@Resource
	private BaseStoreDao baseStoreDao;
	@Resource
	private BaseStores baseStores;
	private BaseStoreModel baseStoreNorthAmerica;

	@Resource
	private BaseSiteDao baseSiteDao;
	@Resource
	private BaseSites baseSites;
	private BaseSiteModel baseSiteAmericas;

	@Resource
	private WarehouseDao warehouseDao;
	@Resource
	private Warehouses warehouses;
	private WarehouseModel warehouseMontreal;

	@Resource
	private PointOfServiceDao pointOfServiceDao;
	@Resource
	private PointsOfService pointsOfService;
	private PointOfServiceModel pointOfServiceMontrealDowntown;
	@Resource
	private Addresses addresses;
	private AddressModel addressMontrealDeMaisonneuve;
	private AddressModel addressMontrealDuke;
	private AddressModel addressNancyHome;

	@Resource
	private ProductDao productDao;
	@Resource
	private Products products;
	private ProductModel productCamera;

	@Resource
	private StockLevelDao stockLevelDao;
	@Resource
	private StockLevels stockLevels;
	private StockLevelModel stockLevelCameraMontreal;

	@Resource
	private WarehousingDao<OrderModel> warehousingOrderDao;
	@Resource
	private Orders orders;
	private OrderModel orderSingleCameraShippedToMontrealNancyHome;
	private OrderModel orderSingleCameraShippedToMontrealDuke;
	private OrderModel orderSingleCameraPickupMontreal;

	@Resource
	private WarehousingDao<ConsignmentModel> warehousingConsignmentDao;
	@Resource
	private Consignments consignments;
	private ConsignmentModel consignmentSingleCameraShippedToMontrealNancyHome;

	@Before
	public void setUp()
	{
		languageEnglish = languages.English();
		countryCanada = countries.Canada();
		countryUSA = countries.UnitedStates();
		zoneUSA = zones.UnitedStates();
		deliveryModePickup = deliveryModes.Pickup();
		vendorHybris = vendors.Hybris();
		currencyUSD = currencies.AmericanDollar();
		unitPiece = units.Piece();
		catalogPrimary = catalogs.Primary();
		catalogVersionStaging = catalogVersions.Staging();
		baseStoreNorthAmerica = baseStores.NorthAmerica();
		baseSiteAmericas = baseSites.Americas();
		warehouseMontreal = warehouses.Montreal();
		pointOfServiceMontrealDowntown = pointsOfService.Montreal_Downtown();
		productCamera = products.Camera();
		stockLevelCameraMontreal = stockLevels.Camera(warehouseMontreal, 100);
		addressNancyHome = addresses.MontrealNancyHome();
		addressMontrealDuke = addresses.MontrealDukePos();
		addressMontrealDeMaisonneuve = addresses.MontrealDeMaisonneuvePos();
		orderSingleCameraShippedToMontrealNancyHome = orders.Camera_Shipped(1L);
		consignmentSingleCameraShippedToMontrealNancyHome = consignments.Camera_ShippedFromMontrealToMontrealNancyHome(
				ConsignmentStatus.READY, Long.valueOf(1));
		stockLevelCameraMontreal = stockLevels.Camera(warehouseMontreal, 100);

		orderSingleCameraShippedToMontrealDuke = orders.Camera_Shipped(Long.valueOf(1));
		orderSingleCameraPickupMontreal = orders.Camera_PickupInMontreal(Long.valueOf(1));
	}

	@Test
	public void verifyLanguageIntegrity()
	{
		final LanguageModel language = languageDao.findLanguagesByCode(Languages.ISOCODE_ENGLISH).iterator().next();
		assertEquals(languageEnglish.getIsocode(), language.getIsocode());
	}

	@Test
	public void verifyCountryIntegrity()
	{
		final CountryModel canada = countryDao.findCountriesByCode(Countries.ISOCODE_CANADA).iterator().next();
		assertEquals(countryCanada.getIsocode(), canada.getIsocode());

		final CountryModel usa = countryDao.findCountriesByCode(Countries.ISOCODE_UNITED_STATES).iterator().next();
		assertEquals(countryUSA.getIsocode(), usa.getIsocode());
	}

	@Test
	public void verifyZoneIntegrity()
	{
		final ZoneModel zone = zoneDao.getByCode(Zones.CODE_UNITED_STATES);
		assertEquals(zoneUSA.getCode(), zone.getCode());
		assertThat(zone.getCountries(), containsInAnyOrder(countryUSA));
	}

	@Test
	public void verifyDeliveryModeIntegrity()
	{
		final DeliveryModeModel deliveryMode = deliveryModeDao.findDeliveryModesByCode(DeliveryModes.CODE_PICKUP).iterator().next();
		assertEquals(deliveryModePickup.getCode(), deliveryMode.getCode());
	}

	@Test
	public void verifyVendorIntegrity()
	{
		final VendorModel vendor = vendorDao.getByCode(Vendors.CODE_HYBRIS);
		assertEquals(vendorHybris.getCode(), vendor.getCode());
	}

	@Test
	public void verifyCurrencyIntegrity()
	{
		final CurrencyModel currency = currencyDao.findCurrenciesByCode(Currencies.ISOCODE_USD).iterator().next();
		assertEquals(currencyUSD.getIsocode(), currency.getIsocode());
	}

	@Test
	public void verifyUnitIntegrity()
	{
		final UnitModel unit = unitDao.findUnitsByCode(Units.CODE_PIECE).iterator().next();
		assertEquals(unitPiece.getCode(), unit.getCode());
	}

	@Test
	public void verifyCatalogIntegrity()
	{
		final CatalogModel catalog = catalogDao.findCatalogById(Catalogs.ID_PRIMARY);
		assertEquals(catalogPrimary.getId(), catalog.getId());
	}

	@Test
	public void verifyCatalogVersionIntegrity()
	{
		final CatalogVersionModel catalogVersion = catalogVersionDao
				.findCatalogVersions(Catalogs.ID_PRIMARY, CatalogVersions.VERSION_STAGING).iterator().next();
		assertEquals(catalogVersionStaging.getVersion(), catalogVersion.getVersion());
		assertEquals(catalogPrimary, catalogVersion.getCatalog());
	}

	@Test
	public void verifyBaseStoreIntegrity_WithDeliveryModes()
	{
		final BaseStoreModel baseStore = baseStoreDao.findBaseStoresByUid(BaseStores.UID_NORTH_AMERICA).iterator().next();
		assertEquals(baseStoreNorthAmerica.getUid(), baseStore.getUid());
		assertEquals(catalogPrimary, baseStore.getCatalogs().iterator().next());
		assertEquals(currencyUSD, baseStore.getDefaultCurrency());
		assertEquals(languageEnglish, baseStore.getDefaultLanguage());
		assertThat(baseStore.getDeliveryCountries(), containsInAnyOrder(countryCanada, countryUSA));

		final DeliveryModeModel deliveryMode = deliveryModeDao.findDeliveryModesByCode(DeliveryModes.CODE_PICKUP).iterator().next();
		assertThat(deliveryMode.getStores(), containsInAnyOrder(baseStoreNorthAmerica));
	}

	@Test
	public void verifyBaseSiteIntegrity()
	{
		final BaseSiteModel baseSite = baseSiteDao.findBaseSiteByUID(BaseSites.UID_AMERICAS);
		assertEquals(baseSiteAmericas.getUid(), baseSite.getUid());
		assertThat(baseSite.getStores(), containsInAnyOrder(baseStoreNorthAmerica));
	}

	@Test
	public void verifyWarehouseIntegrity_NoPosWithBaseStoreWithDeliveryModes()
	{
		final WarehouseModel warehouse = warehouseDao.getWarehouseForCode(Warehouses.CODE_MONTREAL).iterator().next();
		assertEquals(warehouseMontreal.getCode(), warehouse.getCode());
		assertThat(warehouse.getBaseStores(), containsInAnyOrder(baseStoreNorthAmerica));
		assertEquals(vendorHybris, warehouse.getVendor());

		final BaseStoreModel baseStore = baseStoreDao.findBaseStoresByUid(BaseStores.UID_NORTH_AMERICA).iterator().next();
		assertThat(baseStore.getWarehouses(), containsInAnyOrder(warehouseMontreal));

		final DeliveryModeModel deliveryMode = deliveryModeDao.findDeliveryModesByCode(DeliveryModes.CODE_PICKUP).iterator().next();
		assertThat(deliveryMode.getWarehouses(), containsInAnyOrder(warehouseMontreal));
	}

	@Test
	public void verifyPointOfServiceIntegrity_WithAddressAndWarehouseAndBaseStore()
	{
		final PointOfServiceModel pointOfService = pointOfServiceDao.getPosByName(PointsOfService.NAME_MONTREAL_DOWNTOWN);
		assertEquals(pointOfServiceMontrealDowntown.getName(), pointOfService.getName());
		assertEquals(addressMontrealDeMaisonneuve.getStreetname(), pointOfService.getAddress().getStreetname());
		assertThat(pointOfService.getWarehouses(), containsInAnyOrder(warehouseMontreal));

		final WarehouseModel warehouse = warehouseDao.getWarehouseForCode(Warehouses.CODE_MONTREAL).iterator().next();
		assertThat(warehouse.getPointsOfService(), containsInAnyOrder(pointOfServiceMontrealDowntown));

		final BaseStoreModel baseStore = baseStoreDao.findBaseStoresByUid(BaseStores.UID_NORTH_AMERICA).iterator().next();
		assertThat(baseStore.getPointsOfService(), containsInAnyOrder(pointOfServiceMontrealDowntown));
	}

	@Test
	public void verifyProductIntegrity_NoPrice()
	{
		final ProductModel product = productDao.findProductsByCode(Products.CODE_CAMERA).iterator().next();
		assertEquals(productCamera.getCode(), product.getCode());
		assertEquals(catalogVersionStaging, product.getCatalogVersion());
	}

	@Test
	public void verifyStockLevelIntegrity_WithWarehouseAndProduct()
	{
		final StockLevelModel stockLevel = stockLevelDao.findStockLevel(Products.CODE_CAMERA, warehouseMontreal);
		assertEquals(Products.CODE_CAMERA, stockLevel.getProductCode());
		assertEquals(100, stockLevel.getAvailable());
		assertEquals(warehouseMontreal, stockLevel.getWarehouse());

		final WarehouseModel warehouse = warehouseDao.getWarehouseForCode(Warehouses.CODE_MONTREAL).iterator().next();
		assertThat(warehouse.getStockLevels(), containsInAnyOrder(stockLevelCameraMontreal));

		final ProductModel product = productDao.findProductsByCode(Products.CODE_CAMERA).iterator().next();
		assertThat(product.getStockLevels(), containsInAnyOrder(stockLevelCameraMontreal));
	}

	@Test
	public void verifyOrderIntegrity_WithAddress()
	{
		final OrderModel orderShip = warehousingOrderDao.getByCode(Orders.CODE_CAMERA_SHIPPED);
		assertEquals(orderSingleCameraShippedToMontrealDuke.getCode(), orderShip.getCode());
		assertEquals(currencyUSD, orderShip.getCurrency());
		assertEquals(addressNancyHome.getStreetname(), orderShip.getDeliveryAddress().getStreetname());

		final OrderModel orderPickup = warehousingOrderDao.getByCode(Orders.CODE_CAMERA_PICKUP_MONTREAL);
		assertEquals(orderSingleCameraPickupMontreal.getCode(), orderPickup.getCode());
		assertEquals(currencyUSD, orderPickup.getCurrency());
		assertEquals(pointOfServiceMontrealDowntown, orderPickup.getEntries().iterator().next().getDeliveryPointOfService());
	}

	@Test
	public void verifyConsignmentIntegrity_WithAddress()
	{
		final ConsignmentModel consignment = warehousingConsignmentDao
				.getByCode(Consignments.CODE_CAMERA_SHIPPED_FROM_MONREAL_TO_MONTREAL_NANCY_HOME);
		assertEquals(consignmentSingleCameraShippedToMontrealNancyHome.getCode(), consignment.getCode());
		assertEquals(warehouseMontreal, consignment.getWarehouse());
		assertEquals(addressNancyHome, consignment.getShippingAddress());
	}
}

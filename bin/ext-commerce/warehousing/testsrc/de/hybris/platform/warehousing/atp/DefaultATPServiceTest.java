package de.hybris.platform.warehousing.atp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.daos.UnitDao;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.atp.strategy.StockLevelSelectionStrategy;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;
import de.hybris.platform.warehousing.impl.WarehousingBaseIntegrationTest;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.model.CancellationEventModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultATPServiceTest extends WarehousingBaseIntegrationTest
{
	private final static String UNITS_CODE = "units";
	private final static String USD_CURRENCY_CODE = "USD";

	private final static String BASESTORE_NAME = "testStore";
	private final static String BASESITE_UID = "testSite";
	private final static String POS_NAME = "Nakano";
	private final static String PRODUCT_1 = "product1";
	private final static String PRODUCT_2 = "product2";
	private final static String WAREHOUSE_1 = "warehouse_1";
	private final static String WAREHOUSE_4 = "warehouse_4";
	private final static String WAREHOUSE_5 = "warehouse_5";
	private final static Long THRESHOLD = 5L;

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private BaseStoreService baseStoreService;

	@Resource
	private CommerceStockService commerceStockService;

	@Resource
	private CurrencyDao currencyDao;

	@Resource
	private ModelService modelService;

	@Resource
	private ProductService productService;

	@Resource
	private PointOfServiceService pointOfServiceService;

	@Resource
	private StockLevelSelectionStrategy stockLevelSelectionStrategy;

	@Resource
	private StockService stockService;

	@Resource
	private UnitDao unitDao;

	@Resource
	private WarehouseService warehouseService;

	private ConsignmentEntryModel consignmentEntry1;
	private ConsignmentEntryModel consignmentEntry2;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/warehousing/test/impex/atp-test-data.impex", WarehousingTestConstants.ENCODING);

		// Sets current base site and base store
		final BaseSiteModel baseSiteForUID = baseSiteService.getBaseSiteForUID(BASESITE_UID);
		baseSiteService.setCurrentBaseSite(baseSiteForUID, false);

		// Create default order and consignment data
		createSourcingUserData();
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);
		final UserModel user = userService.getUserForUID(Customers.johnsmith.name());
		final CurrencyModel currency = currencyDao.findCurrenciesByCode(USD_CURRENCY_CODE).stream().findFirst().get();
		final UnitModel unit = unitDao.findUnitsByCode(UNITS_CODE).stream().findFirst().get();

		final OrderModel order = new OrderModel();
		final OrderEntryModel orderEntry = new OrderEntryModel();
		orderEntry.setQuantity(10L);
		orderEntry.setOrder(order);
		orderEntry.setUnit(unit);
		orderEntry.setProduct(product);

		order.setCurrency(currency);
		order.setUser(user);
		order.setDate(new Date());
		order.setEntries(Arrays.asList(orderEntry));

		final ConsignmentModel consignment = new ConsignmentModel();
		consignmentEntry1 = new ConsignmentEntryModel();
		consignmentEntry1.setQuantity(6L);
		consignmentEntry1.setOrderEntry(orderEntry);
		consignmentEntry1.setConsignment(consignment);

		consignmentEntry2 = new ConsignmentEntryModel();
		consignmentEntry2.setQuantity(4L);
		consignmentEntry2.setOrderEntry(orderEntry);
		consignmentEntry2.setConsignment(consignment);

		consignment.setConsignmentEntries(Sets.newHashSet(consignmentEntry1, consignmentEntry2));
		consignment.setStatus(ConsignmentStatus.READY);
		consignment.setShippingAddress(pos.getAddress());
		consignment.setCode("test");
		consignment.setWarehouse(warehouse);
		orderEntry.setConsignmentEntries(Sets.newHashSet(consignmentEntry1, consignmentEntry2));

		modelService.save(consignmentEntry1);
		modelService.save(consignmentEntry2);
		modelService.save(consignment);
		modelService.save(orderEntry);

		// Create default inventory events
		final WarehouseModel warehouse4 = warehouseService.getWarehouseForCode(WAREHOUSE_4);
		final WarehouseModel warehouse5 = warehouseService.getWarehouseForCode(WAREHOUSE_5);
		createNewAllocationEvt(warehouse5, product, 1L);
		createNewAllocationEvt(warehouse4, product, 7L);
		createNewCancellationEvt(warehouse4, product, 7L);
	}

	@Test
	public void shouldGetATPForBaseStore()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(BASESTORE_NAME);

		// When
		final Long globalATP = commerceStockService.getStockLevelForProductAndBaseStore(product, baseStore);

		// Then
		assertThat(globalATP, is(576L));
	}

	@Test
	public void shouldGetATPForBaseStoreWithThreshold()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(BASESTORE_NAME);
		final StockLevelModel stockLevel = baseStore.getWarehouses().get(0).getStockLevels().iterator().next();
		stockLevel.setReserved(THRESHOLD.intValue());
		modelService.save(stockLevel);

		// When
		final Long globalATP = commerceStockService.getStockLevelForProductAndBaseStore(product, baseStore);

		// Then
		assertThat(globalATP, is(571L));
	}

	@Test
	public void shouldGetDifferentATPForBaseStore_NewAllocationEvent()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(BASESTORE_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);

		commerceStockService.getStockLevelForProductAndBaseStore(product, baseStore);
		createNewAllocationEvt(warehouse, product, 576L);

		// When
		final Long globalATP = commerceStockService.getStockLevelForProductAndBaseStore(product, baseStore);

		// Then
		assertThat(globalATP, is(0L));
	}

	@Test
	public void shouldGetDifferentATPForBaseStore_NewCancellationEvent()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(BASESTORE_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);

		createNewCancellationEvt(warehouse, product, 576L);

		// When
		final Long globalATP = commerceStockService.getStockLevelForProductAndBaseStore(product, baseStore);

		// Then
		assertThat(globalATP, is(1152L));
	}


	@Test
	public void shouldGetNegativeATPForBaseStore_NewAllocationEvent()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(BASESTORE_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);
		createNewAllocationEvt(warehouse, product, 676L);

		// When
		final Long globalATP = commerceStockService.getStockLevelForProductAndBaseStore(product, baseStore);

		// Then
		assertThat(globalATP, is(-100L));
	}

	@Test
	public void shouldGetDifferentATPForBaseStore_MultipleNewAllocationEvent()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(BASESTORE_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);

		createNewAllocationEvt(warehouse, product, 176L);
		createNewAllocationEvt(warehouse, product, 200L);

		// When
		final Long globalATP = commerceStockService.getStockLevelForProductAndBaseStore(product, baseStore);

		// Then
		assertThat(globalATP, is(200L));
	}

	@Test
	public void shouldGetATPForPOS()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);

		// When
		final Long localATP = commerceStockService.getStockLevelForProductAndPointOfService(product, pos);

		// Then
		assertThat(localATP, is(2L));
	}

	@Test
	public void shouldGetDifferentATPForPOS_NewAllocationEvent()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);
		createNewAllocationEvt(warehouse, product, 2L);

		// When
		final Long localATP = commerceStockService.getStockLevelForProductAndPointOfService(product, pos);

		// Then
		assertThat(localATP, is(0L));
	}

	@Test
	public void shouldGetDifferentATPForPOS_MultipleNewAllocationEvent()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);
		createNewAllocationEvt(warehouse, product, 1L);
		createNewAllocationEvt(warehouse, product, 1L);

		// When
		final Long localATP = commerceStockService.getStockLevelForProductAndPointOfService(product, pos);

		// Then
		assertThat(localATP, is(0L));
	}

	@Test
	public void shouldGetNegativeATPForPOS_NewAllocationEvent()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);
		final WarehouseModel warehouse = warehouseService.getWarehouseForCode(WAREHOUSE_1);
		createNewAllocationEvt(warehouse, product, 3L);

		// When
		final Long localATP = commerceStockService.getStockLevelForProductAndPointOfService(product, pos);

		// Then
		assertThat(localATP, is(-1L));
	}

	@Test
	public void shouldGetZeroATPForPOS_StockNotAvailable()
	{
		// Given
		final ProductModel product = productService.getProductForCode(PRODUCT_2);
		final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);

		// When
		final Long localATP = commerceStockService.getStockLevelForProductAndPointOfService(product, pos);

		// Then
		assertThat(localATP, is(0L));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldGetExceptionWhenBaseStoreNull()
	{
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		commerceStockService.getStockLevelForProductAndBaseStore(product, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldGetExceptionWhenPOSNull()
	{
		final ProductModel product = productService.getProductForCode(PRODUCT_1);
		commerceStockService.getStockLevelForProductAndPointOfService(product, null);
	}

	private void createNewAllocationEvt(final WarehouseModel warehouse, final ProductModel product, final Long allocationQuantity)
	{
		final Collection<StockLevelModel> stockLevels = stockService.getStockLevels(product, Lists.newArrayList(warehouse));
		final AllocationEventModel newAllocationEvt = modelService.create(AllocationEventModel.class);
		newAllocationEvt.setQuantity(allocationQuantity);
		newAllocationEvt.setStockLevel(stockLevelSelectionStrategy.getStockLevelForAllocation(stockLevels));
		newAllocationEvt.setConsignmentEntry(consignmentEntry1);
		modelService.save(newAllocationEvt);
	}

	private void createNewCancellationEvt(final WarehouseModel warehouse, final ProductModel product,
			final Long cancellationQuantity)
	{
		final Collection<StockLevelModel> stockLevels = stockService.getStockLevels(product, Lists.newArrayList(warehouse));
		final CancellationEventModel newCancellationEvt = modelService.create(CancellationEventModel.class);
		newCancellationEvt.setQuantity(cancellationQuantity);
		newCancellationEvt.setStockLevel(stockLevelSelectionStrategy.getStockLevelForAllocation(stockLevels));
		newCancellationEvt.setConsignmentEntry(consignmentEntry2);
		modelService.save(newCancellationEvt);
	}

}

package de.hybris.platform.warehousing.atp.dao.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.delivery.dao.StorePickupDao;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.daos.BaseStoreDao;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class WarehousingStorePickupDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private StorePickupDao storePickupDao;

	@Resource
	private ProductDao productDao;

	@Resource
	private BaseStoreDao baseStoreDao;

	@Resource
	private PointOfServiceDao pointOfServiceDao;

	private BaseStoreModel baseStore;
	private ProductModel mouse;
	private PointOfServiceModel nakano;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/warehousing/test/impex/warehousedao-test-data.impex", WarehousingTestConstants.ENCODING);

		mouse = productDao.findProductsByCode("wirelessmouse").get(0);
		baseStore = baseStoreDao.findBaseStoresByUid("testStore1").get(0);
		nakano = pointOfServiceDao.getPosByName("Nakano");

		// verify data integrity
		final Map<PointOfServiceModel, List<StockLevelModel>> stockLevels = storePickupDao
				.getLocalStockLevelsForProductAndBaseStore(mouse.getCode(), baseStore);
		assertNotNull(stockLevels);
		assertTrue(stockLevels.containsKey(nakano));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCheckProductForPickup_nullProductCode()
	{
		storePickupDao.checkProductForPickup(null, baseStore);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCheckProductForPickup_nullBaseStore()
	{
		storePickupDao.checkProductForPickup(mouse.getCode(), null);
	}

	@Test
	public void shouldCheckProductForPickup_ValidPickupProduct()
	{
		final Boolean result = storePickupDao.checkProductForPickup(mouse.getCode(), baseStore);
		assertTrue(result);
	}

	@Test
	public void shouldCheckProductForPickup_NoPickupWarehouseForProduct()
	{
		final List<ProductModel> batteries = productDao.findProductsByCode("rechargeablebatteries");
		final Boolean result = storePickupDao.checkProductForPickup(batteries.get(0).getCode(), baseStore);
		assertFalse(result);
	}

	@Test
	public void shouldCheckProductForPickup_ProductOutOfStockForPickup()
	{
		final List<ProductModel> chargers = productDao.findProductsByCode("batterycharger");
		final Boolean result = storePickupDao.checkProductForPickup(chargers.get(0).getCode(), baseStore);
		assertFalse(result);
	}
}

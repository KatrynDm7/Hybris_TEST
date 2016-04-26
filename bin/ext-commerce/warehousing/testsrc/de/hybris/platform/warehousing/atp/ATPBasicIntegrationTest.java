package de.hybris.platform.warehousing.atp;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.util.BaseWarehousingIntegrationTest;
import de.hybris.platform.warehousing.util.models.AllocationEvents;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Warehouses;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ATPBasicIntegrationTest extends BaseWarehousingIntegrationTest
{
	private static final Long MONTREAL_CAMERA_QTY = Long.valueOf(50);
	private static final Long BOSTON_CAMERA_QTY = Long.valueOf(25);
	private static final Long ALLOCATED_CAMERA_QTY = Long.valueOf(10);

	@Resource
	private CommerceStockService commerceStockService;

	@Resource
	private Products products;
	@Resource
	private BaseStores baseStores;
	@Resource
	private PointsOfService pointsOfService;
	@Resource
	private StockLevels stockLevels;
	@Resource
	private Warehouses warehouses;
	@Resource
	private AllocationEvents allocationEvents;

	private StockLevelModel stockLevelMontreal;
	private StockLevelModel stockLevelBoston;


	@Before
	public void setUp()
	{
		final WarehouseModel warehouseMontreal = warehouses.Montreal();
		warehouseMontreal.setPointsOfService(Arrays.asList(pointsOfService.Montreal_Downtown()));
		warehouseMontreal.setBaseStores(Arrays.asList(baseStores.NorthAmerica()));
		final WarehouseModel warehouseBoston = warehouses.Boston();
		warehouseBoston.setPointsOfService(Arrays.asList(pointsOfService.Boston()));
		warehouseBoston.setBaseStores(Arrays.asList(baseStores.NorthAmerica()));
		stockLevels.Camera(warehouseMontreal, MONTREAL_CAMERA_QTY.intValue());
		stockLevels.Camera(warehouseBoston, BOSTON_CAMERA_QTY.intValue());
		stockLevelMontreal = stockLevels.Camera(warehouseMontreal, ALLOCATED_CAMERA_QTY.intValue());
		stockLevelBoston = stockLevels.Camera(warehouseBoston, ALLOCATED_CAMERA_QTY.intValue());
	}

	@Test
	public void getDefault_Atp()
	{
		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());
		final Long localAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown());

		assertGlobalAtp(globalAtp, Long.valueOf(0));
		assertEquals(MONTREAL_CAMERA_QTY, localAtp);
	}

	@Test
	public void shouldDecreaseStockLevel_AllocateCameraFrom1Pos1Warehouse()
	{
		final AllocationEventModel allocationEvent = allocationEvents.Camera_ShippedFromMontrealToMontrealNancyHome(
				ALLOCATED_CAMERA_QTY, stockLevelMontreal);

		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());
		final Long localAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown());

		assertGlobalAtp(globalAtp, ALLOCATED_CAMERA_QTY);
		assertEquals(Long.valueOf(MONTREAL_CAMERA_QTY - ALLOCATED_CAMERA_QTY), localAtp);
	}

	@Test
	public void shouldDecreaseStockLevel_AllocateCameraFrom2Pos2Warehouse()
	{
		allocationEvents.Camera_ShippedFromMontrealToMontrealNancyHome(ALLOCATED_CAMERA_QTY, stockLevelMontreal);
		allocationEvents.Camera_ShippedFromBostonToMontrealNancyHome(ALLOCATED_CAMERA_QTY, stockLevelBoston);

		final Long globalAtp = commerceStockService.getStockLevelForProductAndBaseStore(products.Camera(),
				baseStores.NorthAmerica());
		final Long localMontrealAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Montreal_Downtown());
		final Long localBostonAtp = commerceStockService.getStockLevelForProductAndPointOfService(products.Camera(),
				pointsOfService.Boston());

		assertGlobalAtp(globalAtp, ALLOCATED_CAMERA_QTY * 2);
		assertEquals(Long.valueOf(MONTREAL_CAMERA_QTY - ALLOCATED_CAMERA_QTY), localMontrealAtp);
		assertEquals(Long.valueOf(BOSTON_CAMERA_QTY - ALLOCATED_CAMERA_QTY), localBostonAtp);
	}

	private void assertGlobalAtp(final Long globalAtp, final Long allocated)
	{
		assertEquals(Long.valueOf(MONTREAL_CAMERA_QTY + BOSTON_CAMERA_QTY - allocated), globalAtp);
	}
}

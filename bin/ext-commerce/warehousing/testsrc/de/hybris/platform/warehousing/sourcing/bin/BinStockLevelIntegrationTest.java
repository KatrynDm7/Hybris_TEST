package de.hybris.platform.warehousing.sourcing.bin;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.warehousing.constants.WarehousingTestConstants;

import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;


@IntegrationTest
public class BinStockLevelIntegrationTest extends ServicelayerTransactionalTest
{
	private final static int FIVE = 5;
	private final static int TEN = 10;

	private final static String POS_NAME = "Nakano";
	private final static String PRODUCT_CODE_1 = "product1";
	private final static String PRODUCT_CODE_3 = "product3";
	private final static String STOCKLEVEL_BINS = "bin1,bin2";

	@Resource
	private CommerceStockService defaultCommerceStockService;

	@Resource
	private PointOfServiceService pointOfServiceService;

	@Resource
	private ProductService productService;

	@Resource
	private StockService defaultStockService;

	@Before
	public void setup() throws IOException, ImpExException
	{
		importCsv("/warehousing/test/impex/binstocklevel-test-data.impex", WarehousingTestConstants.ENCODING);
	}

		@Test
		public void shouldFindStockLevelHavingBins()
		{
			final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);
			final ProductModel product = productService.getProductForCode(PRODUCT_CODE_1);
			final StockLevelModel stockLevel = defaultStockService.getStockLevel(product, pos.getWarehouses().get(0));
			final Long availableStock = defaultCommerceStockService.getStockLevelForProductAndPointOfService(product, pos);

			assertTrue(TEN == availableStock);
			assertTrue(STOCKLEVEL_BINS.equals(stockLevel.getBin()));
		}

		@Test
		public void shouldFindStockLevelNotHavingBins()
		{
			final PointOfServiceModel pos = pointOfServiceService.getPointOfServiceForName(POS_NAME);
			final ProductModel product = productService.getProductForCode(PRODUCT_CODE_3);
			final StockLevelModel stockLevel = defaultStockService.getStockLevel(product, pos.getWarehouses().get(0));
			final Long availableStock = defaultCommerceStockService.getStockLevelForProductAndPointOfService(product, pos);

			assertTrue(FIVE == availableStock);
			assertNull(stockLevel.getBin());
		}
}

package de.hybris.platform.warehousing.sourcing;

import static de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum.ALLOCATION;
import static de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum.DISTANCE;
import static de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum.PRIORITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.util.SourcingConfigurator;
import de.hybris.platform.warehousing.util.BaseWarehousingIntegrationTest;
import de.hybris.platform.warehousing.util.models.Addresses;
import de.hybris.platform.warehousing.util.models.BaseStores;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Users;
import de.hybris.platform.warehousing.util.models.Warehouses;

import java.util.Optional;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import com.google.common.collect.Lists;


@Ignore("Just a base class for sourcing integration tests.")
public class BaseSourcingIntegrationTest extends BaseWarehousingIntegrationTest
{
	@Resource
	protected SourcingService sourcingService;
	@Resource
	protected ModelService modelService;
	@Resource
	protected SourcingConfigurator sourcingConfigurator;

	@Resource
	protected Orders orders;
	@Resource
	protected BaseStores baseStores;
	@Resource
	protected Warehouses warehouses;
	@Resource
	protected Addresses addresses;
	@Resource
	protected StockLevels stockLevels;
	@Resource
	protected PointsOfService pointsOfService;
	@Resource
	protected Products products;
	@Resource
	protected Users users;

	@Before
	public void setupShopper()
	{
		users.Nancy();
	}

	@Before
	public void setupBaseStore()
	{
		baseStores.NorthAmerica().setPointsOfService(Lists.newArrayList( //
				pointsOfService.Boston(), //
				pointsOfService.Montreal_Downtown() //
				));
		saveAll();
	}

	@After
	public void resetFactors()
	{
		sourcingConfigurator.resetWeights();
	}

	/**
	 * Saves any unsaved models.
	 */
	protected void saveAll()
	{
		modelService.saveAll();
	}

	/**
	 * Assert that the sourcing result selected the correct warehouse and sourced the correct quantity.
	 * 
	 * @param result
	 * @param expectedWarehouse
	 * @param expectedAllocation
	 */
	protected void assertSourcingResultContents(final SourcingResults results, final WarehouseModel expectedWarehouse,
			final ProductModel product, final Long expectedAllocation)
	{
		final Optional<SourcingResult> sourcingResult = results.getResults().stream()
				.filter(result -> result.getWarehouse().getCode().equals(expectedWarehouse.getCode())).findFirst();

		assertTrue("No sourcing result with warehouse " + expectedWarehouse.getCode(), sourcingResult.isPresent());
		assertEquals(expectedWarehouse.getCode(), sourcingResult.get().getWarehouse().getCode());
		assertEquals(expectedAllocation, getAllocationForProduct(sourcingResult.get(), product));
	}

	/**
	 * Sets the sourcing factors to use.
	 * 
	 * @param allocation
	 * @param distance
	 * @param priority
	 */
	protected void setSourcingFactors(final int allocation, final int distance, final int priority)
	{
		sourcingConfigurator.setFactorWeight(ALLOCATION, allocation);
		sourcingConfigurator.setFactorWeight(DISTANCE, distance);
		sourcingConfigurator.setFactorWeight(PRIORITY, priority);
		saveAll();
	}

	/**
	 * Get the quantity allocated for the order entry with the given product.
	 * 
	 * @param result
	 * @param product
	 * @return quantity allocated
	 */
	protected Long getAllocationForProduct(final SourcingResult result, final ProductModel product)
	{
		return result.getAllocation().get(result.getAllocation().keySet().stream() //
				.filter(entry -> entry.getProduct().getCode().equals(product.getCode())) //
				.findFirst().get());
	}
}

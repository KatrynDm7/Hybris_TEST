package de.hybris.platform.warehousing.sourcing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SourceDeliverySingleEntryIntegrationTest extends BaseSourcingIntegrationTest
{
	private static final Long CAMERA_QTY = new Long(5);

	private OrderModel order;

	@Before
	public void setUp() throws Exception
	{
		order = orders.Camera_Shipped(CAMERA_QTY);
	}

	@Test
	public void shouldSource_FromWarehouseWithHighestAvailability()
	{
		// Given
		setSourcingFactors(100, 0, 0);
		stockLevels.Camera(warehouses.Montreal(), 4);
		stockLevels.Camera(warehouses.Boston(), 5);

		// When
		final SourcingResults results = sourcingService.sourceOrder(order);

		// Then
		assertTrue(results.isComplete());
		assertEquals(1, results.getResults().size());
		assertSourcingResultContents(results, warehouses.Boston(), products.Camera(), CAMERA_QTY);
	}

	@Test
	public void shouldSource_FromWarehouseWithHighestPriority()
	{
		// Given
		setSourcingFactors(0, 0, 100);
		stockLevels.Camera(warehouses.Montreal(), 10);
		stockLevels.Camera(warehouses.Boston(), 10);

		warehouses.Montreal().setPriority(Integer.valueOf(50));
		warehouses.Boston().setPriority(Integer.valueOf(1));
		saveAll();

		// When
		final SourcingResults results = sourcingService.sourceOrder(order);

		// Then
		assertTrue(results.isComplete());
		assertEquals(1, results.getResults().size());
		assertSourcingResultContents(results, warehouses.Boston(), products.Camera(), CAMERA_QTY);
	}

	@Test
	public void shouldSource_FromWarehouseWithShortestDistance()
	{
		// Given
		setSourcingFactors(0, 100, 0);
		stockLevels.Camera(warehouses.Montreal(), 10);
		stockLevels.Camera(warehouses.Boston(), 10);

		// When
		final SourcingResults results = sourcingService.sourceOrder(order);

		// Then
		assertTrue(results.isComplete());
		assertEquals(1, results.getResults().size());
		assertSourcingResultContents(results, warehouses.Montreal(), products.Camera(), CAMERA_QTY);
	}

	@Test
	public void shouldFailSource_InsufficientStock_SingleWarehouse()
	{
		// Given
		final Long availableStock = Long.valueOf(2);
		stockLevels.Camera(warehouses.Montreal(), availableStock.intValue());

		// When
		final SourcingResults results = sourcingService.sourceOrder(order);

		// Then
		assertFalse(results.isComplete());
		assertEquals(1, results.getResults().size());
		assertSourcingResultContents(results, warehouses.Montreal(), products.Camera(),
				availableStock);
	}

	@Test
	public void shouldFailSource_InsufficientStock_MultipleWarehouses()
	{
		// Given
		setSourcingFactors(0, 0, 100);
		final Long availableStock = Long.valueOf(2);
		stockLevels.Camera(warehouses.Montreal(), availableStock.intValue());
		stockLevels.Camera(warehouses.Boston(), availableStock.intValue());

		warehouses.Montreal().setPriority(Integer.valueOf(1));
		warehouses.Boston().setPriority(Integer.valueOf(5));
		saveAll();

		// When
		final SourcingResults results = sourcingService.sourceOrder(order);

		// Then
		assertFalse(results.isComplete());
		assertEquals(2, results.getResults().size());
		assertSourcingResultContents(results, warehouses.Montreal(), products.Camera(), availableStock);
		assertSourcingResultContents(results, warehouses.Boston(), products.Camera(), availableStock);
	}

}

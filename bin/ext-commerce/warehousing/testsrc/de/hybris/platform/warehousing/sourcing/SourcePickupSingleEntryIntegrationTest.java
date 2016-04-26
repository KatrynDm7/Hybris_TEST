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
public class SourcePickupSingleEntryIntegrationTest extends BaseSourcingIntegrationTest
{
	private static final Long CAMERA_QTY = new Long(5);

	private OrderModel order;

	@Before
	public void setUp() throws Exception
	{
		order = orders.Camera_PickupInMontreal(CAMERA_QTY);
	}

	@Test
	public void shouldSourceFromDesignatedPickupLocation()
	{
		// Given
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
	public void shouldFailSource_NoStock()
	{
		// Given
		stockLevels.Camera(warehouses.Montreal(), 0);

		// When
		final SourcingResults results = sourcingService.sourceOrder(order);

		// Then
		assertFalse(results.isComplete());
		assertTrue(results.getResults().isEmpty());
	}

	@Test
	public void shouldFailSource_InsufficientStock()
	{
		// Given
		final Long availableStock = Long.valueOf(2);
		stockLevels.Camera(warehouses.Montreal(), availableStock.intValue());

		// When
		final SourcingResults results = sourcingService.sourceOrder(order);

		// Then
		assertFalse(results.isComplete());
		assertEquals(1, results.getResults().size());
		assertSourcingResultContents(results, warehouses.Montreal(), products.Camera(), availableStock);
	}

}

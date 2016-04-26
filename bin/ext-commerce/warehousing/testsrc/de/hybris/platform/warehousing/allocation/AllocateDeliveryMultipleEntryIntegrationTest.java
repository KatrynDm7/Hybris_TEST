package de.hybris.platform.warehousing.allocation;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.result.SourcingResultFactory;
import de.hybris.platform.warehousing.sourcing.util.SourcingResultBuilder;
import de.hybris.platform.warehousing.util.BaseWarehousingIntegrationTest;
import de.hybris.platform.warehousing.util.models.DeliveryModes;
import de.hybris.platform.warehousing.util.models.Orders;
import de.hybris.platform.warehousing.util.models.PointsOfService;
import de.hybris.platform.warehousing.util.models.Products;
import de.hybris.platform.warehousing.util.models.StockLevels;
import de.hybris.platform.warehousing.util.models.Warehouses;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.Sets;

@IntegrationTest
public class AllocateDeliveryMultipleEntryIntegrationTest extends BaseWarehousingIntegrationTest
{
	private static final Long MONTREAL_CAMERA_QTY = Long.valueOf(3);
	private static final Long MONTREAL_MEMORY_CARD_QTY = Long.valueOf(5);
	private static final Long BOSTON_CAMERA_QTY = Long.valueOf(4);
	private static final Long BOSTON_MEMORY_CARD_QTY = Long.valueOf(1);

	@Resource
	private AllocationService allocationService;

	@Resource
	private SourcingResultFactory sourcingResultFactory;
	@Resource
	private Orders orders;
	@Resource
	private Products products;
	@Resource
	private Warehouses warehouses;
	@Resource
	private PointsOfService pointsOfService;
	@Resource
	private StockLevels stockLevels;
	@Resource
	private DeliveryModes deliveryModes;

	private OrderModel order;
	private SourcingResults sourcingResults;

	@Test
	public void shouldAllocate_From1Pos1Warehouse()
	{
		order = orders.CameraAndMemoryCard_Shipped(MONTREAL_CAMERA_QTY, MONTREAL_MEMORY_CARD_QTY);
		final WarehouseModel warehouse = warehouses.Montreal();
		warehouse.setPointsOfService(Arrays.asList(pointsOfService.Montreal_Downtown()));
		stockLevels.Camera(warehouse, MONTREAL_CAMERA_QTY.intValue());
		stockLevels.MemoryCard(warehouse, MONTREAL_MEMORY_CARD_QTY.intValue());

		final SourcingResult sourcingResult = SourcingResultBuilder.aResult() //
				.withAllocation(order.getEntries().get(0), MONTREAL_CAMERA_QTY) //
				.withAllocation(order.getEntries().get(1), MONTREAL_MEMORY_CARD_QTY) //
				.withWarehouse(warehouse) //
				.build();
		sourcingResults = new SourcingResults();
		sourcingResults.setResults(Sets.newHashSet(sourcingResult));
		sourcingResults.setComplete(Boolean.TRUE);

		final Collection<ConsignmentModel> consignments = allocationService.createConsignments(order, "con", sourcingResults);

		assertEquals(1, consignments.size());

		final ConsignmentModel consignment = consignments.iterator().next();
		assertEquals(ConsignmentStatus.READY, consignment.getStatus());
		assertEquals(2, consignment.getConsignmentEntries().size());

		final Iterator<ConsignmentEntryModel> iterator = consignment.getConsignmentEntries().iterator();
		do
		{
			final ConsignmentEntryModel entry = iterator.next();

			if (entry.getOrderEntry().getProduct().getCode() == Products.CODE_CAMERA)
			{
				assertEquals(MONTREAL_CAMERA_QTY, entry.getQuantity());
			}
			else
			{
				assertEquals(MONTREAL_MEMORY_CARD_QTY, entry.getQuantity());
			}
		}
		while (iterator.hasNext());

		assertEquals(MONTREAL_CAMERA_QTY, order.getEntries().get(0).getQuantityAllocated());
		assertEquals(MONTREAL_MEMORY_CARD_QTY, order.getEntries().get(1).getQuantityAllocated());
	}

	@Test
	public void shouldAllocate_From2Pos2Warehouse()
	{
		order = orders.CameraAndMemoryCard_Shipped(MONTREAL_CAMERA_QTY, BOSTON_MEMORY_CARD_QTY);
		final WarehouseModel montrealWarehouse = warehouses.Montreal();
		final WarehouseModel bostonWarehouse = warehouses.Boston();
		montrealWarehouse.setPointsOfService(Arrays.asList(pointsOfService.Montreal_Downtown()));
		bostonWarehouse.setPointsOfService(Arrays.asList(pointsOfService.Boston()));
		stockLevels.Camera(montrealWarehouse, MONTREAL_CAMERA_QTY.intValue());
		stockLevels.MemoryCard(bostonWarehouse, BOSTON_MEMORY_CARD_QTY.intValue());

		final SourcingResult sourcingResult1 = SourcingResultBuilder.aResult() //
				.withAllocation(order.getEntries().get(0), MONTREAL_CAMERA_QTY) //
				.withWarehouse(montrealWarehouse) //
				.build();

		final SourcingResult sourcingResult2 = SourcingResultBuilder.aResult() //
				.withAllocation(order.getEntries().get(1), BOSTON_MEMORY_CARD_QTY) //
				.withWarehouse(bostonWarehouse) //
				.build();

		sourcingResults = new SourcingResults();
		sourcingResults.setResults(Sets.newHashSet(sourcingResult1, sourcingResult2));
		sourcingResults.setComplete(Boolean.TRUE);

		final Collection<ConsignmentModel> consignments = allocationService.createConsignments(order, "con", sourcingResults);
		final Iterator<ConsignmentModel> iterator = consignments.iterator();

		assertEquals(2, consignments.size());

		do
		{
			final ConsignmentModel consignment = iterator.next();

			assertEquals(ConsignmentStatus.READY, consignment.getStatus());
			assertEquals(1, consignment.getConsignmentEntries().size());
			if (consignment.getWarehouse().getCode() == montrealWarehouse.getCode())
			{
				assertEquals(MONTREAL_CAMERA_QTY, consignment.getConsignmentEntries().iterator().next().getQuantity());
			}
			else
			{
				assertEquals(BOSTON_MEMORY_CARD_QTY, consignment.getConsignmentEntries().iterator().next().getQuantity());
			}
		}
		while (iterator.hasNext());

		assertEquals(MONTREAL_CAMERA_QTY, order.getEntries().get(0).getQuantityAllocated());
		assertEquals(BOSTON_MEMORY_CARD_QTY, order.getEntries().get(1).getQuantityAllocated());
	}

}

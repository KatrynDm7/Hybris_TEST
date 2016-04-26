package de.hybris.platform.warehousing.cancellation.strategy.impl;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.data.cancellation.CancellationEntry;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@UnitTest
public class DefaultConsignmentSelectionStrategyTest
{
	private final DefaultConsignmentSelectionStrategy strategy = new DefaultConsignmentSelectionStrategy();

	private OrderModel order;
	private ConsignmentModel consignment1;
	private ConsignmentModel consignment2;

	private CancellationEntry cancellationEntry;

	@Before
	public void setUp()
	{
		order = new OrderModel();
		consignment1 = new ConsignmentModel();
		consignment2 = new ConsignmentModel();
		order.setConsignments(Sets.newHashSet(consignment1, consignment2));

		cancellationEntry = new CancellationEntry();
	}

	@Test
	public void shouldReturnAllConsignments()
	{
		final List<ConsignmentModel> consignments = strategy.selectConsignments(order, Sets.newHashSet(cancellationEntry));
		assertTrue(consignments.contains(consignment1));
		assertTrue(consignments.contains(consignment2));
	}
}

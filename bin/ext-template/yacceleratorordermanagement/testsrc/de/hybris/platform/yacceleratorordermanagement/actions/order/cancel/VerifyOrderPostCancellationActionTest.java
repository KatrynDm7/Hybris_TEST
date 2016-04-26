package de.hybris.platform.yacceleratorordermanagement.actions.order.cancel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.yacceleratorordermanagement.actions.order.cancel.VerifyOrderPostCancellationAction;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class VerifyOrderPostCancellationActionTest
{

	private AbstractOrderEntryModel orderEntryModel;
	private OrderProcessModel orderProcessModel;
	private OrderModel orderModel;
	private ConsignmentModel consignment;

	@InjectMocks
	private final VerifyOrderPostCancellationAction action = new VerifyOrderPostCancellationAction();

	@Mock
	private ModelService modelService;

	@Before
	public void setup()
	{
		consignment = new ConsignmentModel();
		consignment.setStatus(ConsignmentStatus.CANCELLED);

		orderEntryModel = spy(new AbstractOrderEntryModel());

		final List<AbstractOrderEntryModel> orderEntriesModel = new ArrayList<>();
		orderEntriesModel.add(orderEntryModel);

		orderModel = new OrderModel();
		orderModel.setEntries(orderEntriesModel);
		orderModel.setConsignments(Sets.newHashSet(consignment));

		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(orderModel);

		when(orderEntryModel.getQuantityPending()).thenReturn(0L);
	}

	@Test
	public void shouldWAITWhenQuantityPendingIsMoreThanZero() throws Exception
	{
		when(orderEntryModel.getQuantityPending()).thenReturn(5L);

		final String transition = action.execute(orderProcessModel);
		assertEquals(VerifyOrderPostCancellationAction.Transition.WAIT.toString(), transition);
	}

	@Test
	public void shouldNOKWhenConsignmentIsNotCancelled() throws Exception
	{
		consignment.setStatus(ConsignmentStatus.WAITING);
		final String transition = action.execute(orderProcessModel);
		assertEquals(VerifyOrderPostCancellationAction.Transition.NOK.toString(), transition);
	}

	@Test
	public void shouldOKWhenQuantityPendingIsZero() throws Exception
	{
		final String transition = action.execute(orderProcessModel);
		assertEquals(VerifyOrderPostCancellationAction.Transition.OK.toString(), transition);
	}

	@Test
	public void shouldSetOrderStatusToCancelledWhenQuantityPendingIsZero() throws Exception
	{
		action.execute(orderProcessModel);

		verify(modelService).save(orderModel);
		assertTrue(orderModel.getStatus().toString().equals(OrderStatus.CANCELLED.toString()));
	}

}

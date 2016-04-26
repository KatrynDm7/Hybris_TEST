package de.hybris.platform.yacceleratorordermanagement.actions.order;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CompleteOrderActionTest
{
	OrderProcessModel orderProcessModel;
	OrderModel orderModel;

	@InjectMocks
	CompleteOrderAction action = new CompleteOrderAction();

	@Mock
	private ModelService modelService;

	@Before
	public void setup()
	{
		orderModel = new OrderModel();
		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(orderModel);
	}

	@Test
	public void shouldOK() throws Exception
	{
		final String transition = action.execute(orderProcessModel);
		assertTrue(AbstractProceduralAction.Transition.OK.toString().equals(transition));
	}

}

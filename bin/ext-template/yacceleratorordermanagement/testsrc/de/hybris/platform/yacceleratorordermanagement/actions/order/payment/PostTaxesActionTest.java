package de.hybris.platform.yacceleratorordermanagement.actions.order.payment;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.externaltax.ExternalTaxesService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PostTaxesActionTest
{
	private OrderProcessModel orderProcessModel;
	private OrderModel orderModel;

	@InjectMocks
	private PostTaxesAction action;

	@Mock
	private ExternalTaxesService externalTaxesService;

	@Before
	public void setup()
	{
		orderModel = new OrderModel();
		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(orderModel);
	}

	@Test
	public void shouldOKWhenPostTaxesIsSuccessful() throws Exception
	{
		action.executeAction(orderProcessModel);
		verify(externalTaxesService).calculateExternalTaxes(orderModel);
	}

}
